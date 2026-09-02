#!/usr/bin/env bash
set -euo pipefail

repertoire_resultats="performance/rapports/results"
repertoire_logs="performance/logs"
base_url="${BASE_URL:-http://localhost:8080}"
echec=0

mkdir -p "${repertoire_resultats}"
mkdir -p "${repertoire_logs}"

scenarios=(
    disponibilite
    liste-proprietaires
    recherche-proprietaire
    detail-proprietaire
    liste-veterinaires
    creation-proprietaire
    ajout-animal
    ajout-visite
    parcours-principaux
)

for scenario in "${scenarios[@]}"; do
    fichier="performance/scenarios/k6/${scenario}.js"
    if BASE_URL="${base_url}" k6 run \
        --summary-export "${repertoire_resultats}/${scenario}-summary.json" \
        --out "json=${repertoire_resultats}/${scenario}.json" \
        "${fichier}" >"${repertoire_logs}/${scenario}.log" 2>&1; then
        :
    else
        echec=1
    fi
done

if [[ "${echec}" -ne 0 ]]; then
    printf '%s\n' 'Un ou plusieurs scénarios ont dépassé leurs seuils.' >&2
fi

exit 0
