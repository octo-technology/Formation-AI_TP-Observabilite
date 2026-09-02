# Tests de performance

Cette documentation décrit la préparation et l'exécution des tests de performance de Petclinic.

## Documents

- [Installation de k6](doc/installation-k6.md)
- [Création des jeux de données](doc/jeux-de-donnees.md)
- [Scénarios de tests de performance](doc/scenarios.md)
- [Rapport final des tests](rapports/rapport-final.md)

## Prérequis

- Java 21 configuré conformément à `.sdkmanrc` ;
- k6 installé et disponible dans le `PATH` ;
- `jq` installé et disponible dans le `PATH` pour générer le rapport ;
- application démarrée sur `http://localhost:8080` ;
- jeu volumineux généré dans `performance/data/` ;
- port `8080` disponible.

Les tests de charge utilisent une application démarrée séparément. Ils ne sont pas inclus dans `./gradlew test`.

<details>
  <summary><b>🛠️ Voir les détails pour le démarrage complet</b></summary>

## Démarrage complet avec le jeu volumineux

### 1. Vérifier k6

```shell
if command -v k6 >/dev/null 2>&1; then
  k6 version
else
  echo "k6 est absent. Consulter installation-k6.md."
  exit 1
fi
```

Installer k6 si nécessaire en suivant [installation-k6.md](doc/installation-k6.md), puis vérifier de nouveau `k6 version`.

### 2. Générer le jeu de données

Le script crée `performance/data/` et génère exactement 10 000 propriétaires, 30 000 animaux et 100 000 visites :

```shell
./performance/scripts/generer-jeu-volumineux.sh
```

Contrôler les volumes avant de démarrer l'application :

```shell
rg -c '^INSERT INTO proprietaires' performance/data/data-volumineux.sql
rg -c '^INSERT INTO animaux' performance/data/data-volumineux.sql
rg -c '^INSERT INTO visites' performance/data/data-volumineux.sql
```

### 3. Démarrer avec le profil performance

Le profil `performance` :

- utilise une base H2 en mémoire distincte ;
- crée le schéma avec `schema.sql` ;
- charge d'abord `data.sql` pour les types, vétérinaires et spécialités ;
- charge ensuite `performance/data/data-volumineux.sql`, qui remplace les propriétaires, animaux et visites.

Dans un premier terminal :

```shell
./gradlew bootRun --args='--spring.profiles.active=performance'
```

Le fichier de données par défaut est `performance/data/data-volumineux.sql`. Pour utiliser un autre fichier :

```shell
PERFORMANCE_DATA_FILE=/chemin/absolu/jeu.sql \
  ./gradlew bootRun --args='--spring.profiles.active=performance'
```

Attendre que l'application soit disponible :

```shell
until curl --fail --silent http://localhost:8080/proprietaires > /dev/null; do
  sleep 1
done
```

Le premier chargement peut être plus long avec 140 000 lignes SQL. Ce temps de démarrage ne doit pas être inclus dans les mesures k6.

Vérifier que le jeu est bien chargé en consultant une page volumineuse et une page de détail :

```shell
curl --fail --silent http://localhost:8080/proprietaires | rg 'Propriétaires'
curl --fail --silent http://localhost:8080/proprietaires/5000 | rg 'Enregistrer un animal|Sélectionner un type'
```

### 4. Lancer un seul scénario

Dans un second terminal, lancer par exemple le scénario de liste des propriétaires :

```shell
BASE_URL=http://localhost:8080 \
  k6 run --out json=performance/rapports/results/liste-proprietaires.json \
  performance/scenarios/k6/liste-proprietaires.js
```

La commande retourne un code différent de zéro si un seuil k6 est dépassé. Avec le jeu volumineux actuel, la page de liste contient 10 000 lignes et peut dépasser le seuil de lecture initial de 500 ms ; lors de la validation locale, son p95 était de 1,39 s. Ce résultat doit être conservé dans le rapport, pas masqué par une modification du seuil. Le scénario de détail, testé avec le même profil et jeu, a obtenu un p95 de 5,32 ms et 0 % d'erreur.

Pour lancer un autre scénario, remplacer le nom du fichier par celui indiqué dans [scenarios.md](doc/scenarios.md).

### 5. Vérifier que tout est opérationnel

Contrôler une page de liste et une page de détail :

```shell
curl --fail --silent http://localhost:8080/proprietaires | rg '10 000|Propriétaires'
curl --fail --silent http://localhost:8080/proprietaires/5000 | rg 'Enregistrer un animal|Sélectionner un type'
```

Le scénario k6 doit terminer avec les seuils respectés, notamment un taux d'erreur inférieur à 1 % et un p95 inférieur à 500 ms pour les lectures.

### 6. Générer et conserver un rapport

Créer le répertoire de sortie avant l'exécution :

```shell
mkdir -p performance/rapports/results
```

La sortie JSON k6 est exploitable pour produire le rapport Markdown. Ce rapport doit contenir le profil, le scénario, le jeu de données, le débit, le taux d'erreur, les p95/p99 et la version de k6. Le script Bash `performance/scripts/generer-rapport.sh` utilise `jq` pour lire les synthèses k6 au format `--summary-export`.

Les résultats JSON sont écrits dans `performance/rapports/results/` et ignorés par Git. Les logs k6 et le log Spring sont écrits dans `performance/logs/`, notamment `performance/logs/petclinic-performance.log`.

## Lancer tous les scénarios avec Just

Après avoir démarré l'application avec le profil performance dans un premier terminal, lancer la recette suivante dans un second terminal :

```shell
./performance/scripts/generer-jeu-volumineux.sh
just performance-all
```

La recette `performance-all` exécute successivement les neuf scénarios k6 retenus :

- disponibilité ;
- liste des propriétaires ;
- recherche d'un propriétaire ;
- détail d'un propriétaire ;
- liste des vétérinaires ;
- création d'un propriétaire ;
- ajout d'un animal ;
- ajout d'une visite ;
- parcours principaux combinés.

Chaque scénario produit un fichier JSON et un rapport console dans `performance/rapports/results/`. Pour cibler une autre instance :

```shell
BASE_URL=http://localhost:8081 just performance-all
```

Les scénarios contenant des écritures doivent être exécutés sur une base de performance réinitialisée lorsque l'on souhaite comparer plusieurs campagnes.

La recette produit également un fichier `*-summary.json` compact par scénario dans `performance/rapports/results/`. Ces synthèses sont agrégées automatiquement dans `performance/rapports/rapport-final.md`. Pour régénérer uniquement le rapport à partir des synthèses existantes :

```shell
just performance-report
```

La recette `performance-all` exécute tous les scénarios et retourne `0` même si certains seuils sont dépassés ; les dépassements restent visibles dans le rapport et ne sont pas masqués. Le rapport est généré une seule fois par `performance-run`, après le calcul de la durée totale de la campagne. Pour utiliser un comportement strict adapté à une pipeline CI, définir `FAIL_ON_PERFORMANCE_THRESHOLDS=true` avec `performance-run`.

Le générateur de rapport est un script Bash : `performance/scripts/generer-rapport.sh`. Il lit les fichiers `*-summary.json` avec `jq` dans `performance/rapports/results/`, puis crée ou remplace `performance/rapports/rapport-final.md`. Il ajoute également le temps d'exécution total de la campagne lorsqu'il est appelé par `just performance-run`. Un scénario sans requête mesurable et avec 100 % d'échec réseau est signalé comme **Application indisponible**, et non comme une performance conforme.

</details>

## Lancer la campagne complète avec Just

```shell
just performance-run
```

<details>
  <summary><b>🔍️ Voir les détails</b></summary>

La recette `performance-run` automatise toute la campagne via `performance/scripts/lancer-campagne.sh` et `performance/scripts/lancer-scenarios.sh` :

1. Elle vérifie la présence de `performance/data/data-volumineux.sql`.
2. Elle exécute `performance/scripts/generer-jeu-volumineux.sh` si le fichier est absent.
3. Elle démarre l'application avec `--spring.profiles.active=performance`.
4. Elle attend que `http://localhost:8080/proprietaires` réponde.
5. Elle exécute `just performance-all`.
6. Elle exécute `just performance-reports`, même si un scénario dépasse ses seuils.
7. Elle arrête l'application à la fin de la campagne.

Le code de sortie est `0` lorsque l'orchestration est terminée et le rapport généré. Pour faire échouer la commande sur un seuil k6, utiliser `FAIL_ON_PERFORMANCE_THRESHOLDS=true just performance-run`. La commande correcte pour régénérer uniquement le rapport est `just performance-reports`.

</details>

## Scénarios

- Les scénarios k6 sont disponibles dans `performance/scenarios/k6/`
- Leur liste et leurs objectifs sont détaillés dans [scenarios.md](doc/scenarios.md)

## Campagne nominale

La campagne nominale peut être lancée directement avec `just performance-nominal`.

<details>
  <summary><b>🔍️ Détails</b></summary>
- Le script démarre automatiquement l'application avec le profil `performance` si elle n'est pas déjà disponible, puis l'arrête uniquement s'il l'a lui-même démarrée
- Il retourne `0` par défaut lorsque la campagne et la génération du rapport sont terminées, même si un seuil de performance est dépassé ; les seuils restent visibles dans le rapport.\
Pour un mode strict, utiliser `FAIL_ON_PERFORMANCE_THRESHOLDS=true just performance-nominal` :

Pour accélérer les phases de construction et d'itération, elle monte progressivement jusqu'à 10 utilisateurs virtuels en 15 secondes, maintient cette charge, puis redescend en 15 secondes.
- Sa durée est configurable en minutes avec `NOMINAL_DURATION_MINUTES` et vaut 1 minute par défaut
- Les résultats sont enregistrés dans `performance/rapports/results/campagne-nominale.json` et `campagne-nominale-summary.json`
- Le log est écrit dans `performance/logs/campagne-nominale.log` et le rapport dédié est généré dans `performance/rapports/rapport-campagne-nominale.md`

Exemples :
```shell
just performance-nominal
NOMINAL_DURATION_MINUTES=5 just performance-nominal
NOMINAL_DURATION_MINUTES=10 just performance-nominal
```

- La montée et la descente durent chacune 15 secondes
- La phase de maintien dure donc `NOMINAL_DURATION_MINUTES * 60 - 30` secondes
- La variable doit être un entier supérieur ou égal à 1

</details>