#!/usr/bin/env bash
set -euo pipefail

fichier_donnees="performance/data/data-volumineux.sql"
fichier_log="performance/logs/petclinic-performance.log"
pid=""
debut_campagne=$(date +%s)

nettoyer() {
    if [[ -n "${pid}" ]] && kill -0 "${pid}" 2>/dev/null; then
        kill "${pid}" 2>/dev/null || true
        wait "${pid}" 2>/dev/null || true
    fi
}

trap nettoyer EXIT INT TERM

mkdir -p performance/rapports/results performance/logs
shopt -s dotglob nullglob
rm -rf performance/rapports/* performance/logs/*

if [[ ! -f "${fichier_donnees}" ]]; then
    ./performance/scripts/generer-jeu-volumineux.sh
fi

./gradlew bootRun --args='--spring.profiles.active=performance' >"${fichier_log}" 2>&1 &
pid=$!

application_disponible=false
for _ in $(seq 1 240); do
    if curl --fail --silent http://localhost:8080/proprietaires >/dev/null 2>&1; then
        application_disponible=true
        break
    fi
    if ! kill -0 "${pid}" 2>/dev/null; then
        printf '%s\n' "L'application performance s'est arrêtée avant de répondre." >&2
        exit 1
    fi
    sleep 1
done

if [[ "${application_disponible}" != true ]]; then
    printf '%s\n' "L'application performance n'est pas disponible après 240 secondes." >&2
    exit 1
fi

code=0
just performance-all || code=$?
fin_campagne=$(date +%s)
export PERFORMANCE_DURATION_SECONDS=$((fin_campagne - debut_campagne))
export PERFORMANCE_REPORT_TITLE="Rapport final des tests de performance"
export PERFORMANCE_REPRODUCTION_COMMAND="just performance-run"
just performance-reports

if [[ "${FAIL_ON_PERFORMANCE_THRESHOLDS:-false}" == true ]]; then
    exit "${code}"
fi

exit 0
