#!/usr/bin/env bash
set -Eeuo pipefail

ports=( ${APPLICATION_PORTS:-8080} )
attente_arret_secondes=${PORT_KILL_GRACE_SECONDS:-10}
motif_processus_autorises=${ALLOWED_PROCESS_REGEX:-'(java|node|npm|vite)'}

processus_du_port() {
    lsof -tiTCP:"$1" -sTCP:LISTEN || true
}

attendre_liberation() {
    local port=$1

    for _ in $(seq 1 "$attente_arret_secondes"); do
        [[ -z "$(processus_du_port "$port")" ]] && return 0
        sleep 1
    done

    return 1
}

for port in "${ports[@]}"; do
    pids=$(processus_du_port "$port")
    [[ -z "$pids" ]] && continue

    printf 'Port %s deja utilise :\n' "$port" >&2
    lsof -nP -iTCP:"$port" -sTCP:LISTEN >&2 || true

    while read -r pid; do
        [[ -z "$pid" ]] && continue
        commande=$(ps -p "$pid" -o command=)

        if [[ ! "$commande" =~ $motif_processus_autorises ]]; then
            printf 'Arret refuse pour le PID %s : %s\n' "$pid" "$commande" >&2
            exit 1
        fi

        printf 'Arret du processus applicatif %s : %s\n' "$pid" "$commande" >&2
        kill -TERM "$pid"
    done <<< "$pids"

    if ! attendre_liberation "$port"; then
        pids_restants=$(processus_du_port "$port")
        printf 'Le port %s reste utilise par : %s\n' "$port" "$pids_restants" >&2
        for pid in $pids_restants; do
            printf 'Arret force du processus %s\n' "$pid" >&2
            kill -KILL "$pid"
        done
        attendre_liberation "$port" || {
            printf 'Impossible de liberer le port %s.\n' "$port" >&2
            exit 1
        }
    fi
done
