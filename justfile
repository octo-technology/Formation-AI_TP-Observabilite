set shell := ["bash", "-eu", "-o", "pipefail", "-c"]

export NOMINAL_DURATION_MINUTES := env_var_or_default("NOMINAL_DURATION_MINUTES", "2")

default:
    @just --list

# Installe et active la version Java définie dans .sdkmanrc.
java:
    @source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk env install && sdk env

# Compile le code sans exécuter les tests.
build:
    ./gradlew build -x test

# Exécute tous les tests.
test:
    ./gradlew test

# Nettoie les artefacts Gradle puis reconstruit et teste l'application.
check: clean
    ./gradlew build

# Démarre l'application Spring Boot sur le port 8080.
run:
    ./gradlew bootRun

# Démarre l'application avec le profil de développement.
dev:
    ./gradlew bootRun --args='--spring.thymeleaf.cache=false'

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

# Supprime les répertoires de build Gradle.
clean:
    ./gradlew clean

# Affiche les tâches Gradle disponibles.
tasks:
    ./gradlew tasks

# Formate les fichiers de production Java, HTML, SQL et configuration.
format:
    ./gradlew spotlessApply

# Vérifie que les fichiers de production sont correctement formatés.
format-check:
    ./gradlew spotlessCheck
