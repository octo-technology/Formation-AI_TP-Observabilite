#!/usr/bin/env bash
set -Eeuo pipefail

racine=$(cd "$(dirname "$0")/.." && pwd)
script="$racine/scripts/verifier-ports-application.sh"
repertoire_test=$(mktemp -d)
trap 'rm -rf "$repertoire_test"' EXIT

assertion() {
    [[ "$1" == "$2" ]] || {
        printf 'Attendu: %s, obtenu: %s\n' "$2" "$1" >&2
        exit 1
    }
}

cat >"$repertoire_test/lsof" <<'EOF'
#!/usr/bin/env bash
set -u
if [[ -f "$TEST_PORT_OCCUPE" ]] && kill -0 "$TEST_PID" 2>/dev/null; then
    if [[ "$*" == *"-tiTCP:"* ]] && [[ ! -f "${TEST_LSOF_ALREADY_REPORTED:-}" ]]; then
        touch "${TEST_LSOF_ALREADY_REPORTED:-/tmp/test-lsof-reported}"
        printf '%s\n' "$TEST_PID"
    elif [[ "$*" != *"-tiTCP:"* ]]; then
        printf 'fake-process %s\n' "$TEST_PID"
    fi
fi
EOF
cat >"$repertoire_test/ps" <<'EOF'
#!/usr/bin/env bash
printf '%s\n' "${TEST_PROCESS_COMMAND:-unknown}"
EOF
chmod +x "$repertoire_test/lsof" "$repertoire_test/ps"

TEST_PORT_OCCUPE="$repertoire_test/absent" \
    APPLICATION_PORTS=18080 \
    PATH="$repertoire_test:$PATH" \
    "$script"

touch "$repertoire_test/occupe"
if TEST_PORT_OCCUPE="$repertoire_test/occupe" TEST_PID=$$ TEST_PROCESS_COMMAND='postgres -D data' \
    APPLICATION_PORTS=18080 PORT_KILL_GRACE_SECONDS=1 PATH="$repertoire_test:$PATH" \
    "$script" >/dev/null 2>&1; then
    printf 'Un processus non autorise aurait du etre refuse.\n' >&2
    exit 1
fi

TEST_PORT_LIBERE="$repertoire_test/libere" \
    bash -c 'trap "touch \"$TEST_PORT_LIBERE\"; exit 0" TERM; while true; do :; done' &
processus_test=$!
trap 'kill "$processus_test" 2>/dev/null || true; wait "$processus_test" 2>/dev/null || true; rm -rf "$repertoire_test"' EXIT
TEST_PORT_OCCUPE="$repertoire_test/occupe" \
TEST_PID="$processus_test" \
TEST_PROCESS_COMMAND='java -jar petclinic.jar' \
TEST_LSOF_ALREADY_REPORTED="$repertoire_test/reported" \
APPLICATION_PORTS=18080 PORT_KILL_GRACE_SECONDS=1 PATH="$repertoire_test:$PATH" \
"$script" >/dev/null 2>&1

kill "$processus_test" 2>/dev/null || true

printf 'Tests du controle des ports : OK\n'
