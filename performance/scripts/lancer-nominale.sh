#!/usr/bin/env bash
set -euo pipefail

repertoire_resultats="performance/rapports/results"
repertoire_logs="performance/logs"
base_url="${BASE_URL:-http://localhost:8080}"
fichier_rapport="${repertoire_resultats}/campagne-nominale-summary.json"
rapport_nominale="performance/rapports/rapport-campagne-nominale.md"
debut_campagne=$(date +%s)
fichier_log_application="${repertoire_logs}/petclinic-performance-nominale.log"
pid_application=""

nettoyer() {
    if [[ -n "${pid_application}" ]] && kill -0 "${pid_application}" 2>/dev/null; then
        kill "${pid_application}" 2>/dev/null || true
        wait "${pid_application}" 2>/dev/null || true
    fi
}

trap nettoyer EXIT INT TERM

mkdir -p "${repertoire_resultats}" "${repertoire_logs}"
rm -f "${fichier_rapport}" "${repertoire_resultats}/campagne-nominale.json" "${repertoire_logs}/campagne-nominale.log" "${rapport_nominale}"
if ! curl --fail --silent "${base_url}/proprietaires" >/dev/null 2>&1; then
    ./gradlew bootRun --args='--spring.profiles.active=performance' >"${fichier_log_application}" 2>&1 &
    pid_application=$!

    application_disponible=false
    for _ in $(seq 1 240); do
        if curl --fail --silent "${base_url}/proprietaires" >/dev/null 2>&1; then
            application_disponible=true
            break
        fi
        if ! kill -0 "${pid_application}" 2>/dev/null; then
            printf '%s\n' "L'application performance s'est arrêtée avant de répondre." >&2
            exit 1
        fi
        sleep 1
    done

    if [[ "${application_disponible}" != true ]]; then
        printf '%s\n' "L'application performance n'est pas disponible après 240 secondes." >&2
        exit 1
    fi
fi

if BASE_URL="${base_url}" k6 run \
    --summary-export "${repertoire_resultats}/campagne-nominale-summary.json" \
    --out "json=${repertoire_resultats}/campagne-nominale.json" \
    performance/scenarios/k6/campagne-nominale.js \
    >"${repertoire_logs}/campagne-nominale.log" 2>&1; then
    code=0
else
    code=$?
fi
fin_campagne=$(date +%s)

PERFORMANCE_RESULTS_DIR="${repertoire_resultats}" \
PERFORMANCE_SUMMARY_FILE="${repertoire_resultats}/campagne-nominale-summary.json" \
PERFORMANCE_DURATION_SECONDS="$((fin_campagne - debut_campagne))" \
PERFORMANCE_REPORT_TITLE="Rapport campagne nominale de performance" \
PERFORMANCE_REPRODUCTION_COMMAND="NOMINALE_DURATION_MINUTES=${NOMINALE_DURATION_MINUTES:-1} just performance-nominale" \
PERFORMANCE_REPORT_FILE="${rapport_nominale}" \
    performance/scripts/generer-rapport.sh

if [[ "${FAIL_ON_PERFORMANCE_THRESHOLDS:-false}" == true ]]; then
    exit "${code}"
fi

exit 0
