set shell := ["bash", "-eu", "-o", "pipefail", "-c"]

export NOMINAL_DURATION_MINUTES := env_var_or_default("NOMINAL_DURATION_MINUTES", "2")

default:
    @just --list

java:
    @source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk env install && sdk env

build:
    ./gradlew build -x test

test:
    ./gradlew test

check: clean
    ./gradlew build

# Vérifie les ports applicatifs ne sont pas utilisés, puis démarre Spring Boot.
run:
    ./scripts/verifier-ports-application.sh
    ./gradlew bootRun

# Vérifie que les ports applicatifs sont disponibles.
test-ports:
    ./scripts/verifier-ports-application.test.sh

# Lance tous les scénarios k6 contre l'application démarrée.
performance-all:
    performance/scripts/lancer-scenarios.sh

performance-nominal:
    performance/scripts/lancer-nominal.sh

# Prépare les données, démarre l'application et exécute toute la campagne.
performance-run:
    performance/scripts/lancer-campagne.sh

# Génère le rapport depuis les synthèses k6 disponibles.
performance-reports:
    performance/scripts/generer-rapport.sh

clean:
    ./gradlew clean

tasks:
    ./gradlew tasks

# Formate les fichiers de production Java, HTML, SQL et configuration.
format:
    ./gradlew spotlessApply

format-check:
    ./gradlew spotlessCheck
