#!/usr/bin/env bash
set -euo pipefail

repertoire_resultats="${PERFORMANCE_RESULTS_DIR:-performance/rapports/results}"
repertoire_rapports="${PERFORMANCE_REPORTS_DIR:-performance/rapports}"
duree_totale="${PERFORMANCE_DURATION_SECONDS:-N/A}"
titre_rapport="${PERFORMANCE_REPORT_TITLE:-Rapport final des tests de performance}"
commande_reproduction="${PERFORMANCE_REPRODUCTION_COMMAND:-just performance-run}"

if ! command -v jq >/dev/null 2>&1; then
    printf '%s\n' 'Erreur : jq est nécessaire pour lire les synthèses JSON k6.' >&2
    exit 1
fi

shopt -s nullglob
if [[ -n "${PERFORMANCE_SUMMARY_FILE:-}" ]]; then
    syntheses=("${PERFORMANCE_SUMMARY_FILE}")
else
    syntheses=("${repertoire_resultats}"/*-summary.json)
fi
if ((${#syntheses[@]} == 0)); then
    printf 'Aucune synthèse k6 trouvée dans %s\n' "${repertoire_resultats}" >&2
    exit 1
fi

mkdir -p "${repertoire_rapports}"
date_iso=$(date -u '+%Y-%m-%dT%H:%M:%SZ')
fichier_rapport="${PERFORMANCE_REPORT_FILE:-${repertoire_rapports}/rapport-final.md}"
nombre_conformes=0

format_nombre() {
    awk -v nombre="$1" 'BEGIN { printf "%.2f", nombre }' | tr '.' ','
}

format_duree() {
    awk -v duree="$1" 'BEGIN { if (duree >= 1000) printf "%.2f s", duree / 1000; else printf "%.2f ms", duree }'
}

format_duree_totale() {
    if [[ "${duree_totale}" =~ ^[0-9]+$ ]]; then
        printf '%02d min %02d s' "$((duree_totale / 60))" "$((duree_totale % 60))"
    else
        printf '%s' "${duree_totale}"
    fi
}

{
    printf '# %s\n\n' "${titre_rapport}"
    printf '%s\n' "- Généré le : ${date_iso}"
    printf '%s\n' "- Nombre de scénarios : ${#syntheses[@]}"
    printf '%s\n' "- Temps d'exécution total : $(format_duree_totale)"
    printf '%s\n\n' "- Seuils : taux d'erreur < 1 % et p95 < 500 ms"
    printf '%s\n\n' '## Résultats'
    printf '%s\n' '| Scénario | Requêtes | Débit (req/s) | Erreurs | Assertions | p95 | Statut |'
    printf '%s\n' '| --- | ---: | ---: | ---: | ---: | ---: | --- |'

    for fichier in "${syntheses[@]}"; do
        nom=$(basename "${fichier}" '-summary.json')
        requetes=$(jq -r '.metrics.http_reqs.count // 0' "${fichier}")
        debit=$(jq -r '.metrics.http_reqs.rate // 0' "${fichier}")
        erreur=$(jq -r '((.metrics.http_req_failed.value // .metrics.http_req_failed.rate // 0) * 100)' "${fichier}")
        assertions=$(jq -r '((.metrics.checks.value // .metrics.checks.rate // 0) * 100)' "${fichier}")
        p95=$(jq -r '.metrics.http_req_duration["p(95)"] // 0' "${fichier}")

        statut='Conforme'
        if awk -v erreur="${erreur}" -v p95="${p95}" 'BEGIN { exit !(erreur >= 100 && p95 == 0) }'; then
            statut='Application indisponible'
        elif ! awk -v erreur="${erreur}" -v p95="${p95}" 'BEGIN { exit !(erreur < 1 && p95 < 500) }'; then
            statut='Seuil dépassé'
        else
            nombre_conformes=$((nombre_conformes + 1))
        fi

        printf '| `%s` | %s | %s | %s %% | %s %% | %s | %s |\n' \
            "${nom}" "${requetes}" "$(format_nombre "${debit}")" \
            "$(format_nombre "${erreur}")" "$(format_nombre "${assertions}")" \
            "$(format_duree "${p95}")" "${statut}"
    done

    printf '%s\n\n' '## Synthèse'
    printf '%d scénario(s) sur %d respecte(nt) les seuils définis.\n\n' "${nombre_conformes}" "${#syntheses[@]}"
    printf '%s\n\n' '## Fichiers sources'
    printf '%s\n\n' "Les résultats sont issus des fichiers *-summary.json générés directement par k6 dans ${repertoire_resultats}. Les logs sont disponibles dans performance/logs/."
    printf '%s\n' '## Reproduction'
    printf '%s\n' '```shell'
    printf '%s\n' "${commande_reproduction}"
    printf '%s\n' '```'
} >"${fichier_rapport}"

printf '%s\n' "Rapport généré : ${fichier_rapport}"
