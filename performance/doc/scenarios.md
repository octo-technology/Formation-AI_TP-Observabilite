# Scénarios de tests de performance

Ce document décrit les scénarios de performance retenus pour l'application Petclinic. Les scénarios sont exécutés avec k6 contre une instance de l'application démarrée séparément.

Les scripts correspondants sont disponibles dans `performance/scenarios/k6/` :

| Scénario | Script |
| --- | --- |
| Disponibilité | `disponibilite.js` |
| Liste des propriétaires | `liste-proprietaires.js` |
| Recherche d'un propriétaire | `recherche-proprietaire.js` |
| Détail d'un propriétaire | `detail-proprietaire.js` |
| Liste des vétérinaires | `liste-veterinaires.js` |
| Création d'un propriétaire | `creation-proprietaire.js` |
| Ajout d'un animal | `ajout-animal.js` |
| Ajout d'une visite | `ajout-visite.js` |
| Parcours principaux combinés | `parcours-principaux.js` |
| Campagne nominale | `campagne-nominale.js` |

## Préconditions communes

- L'application est démarrée sur `http://localhost:8080`.
- Le schéma est créé avec `schema.sql`.
- Le jeu de données utilisé est chargé avant le début de la mesure.
- La durée de démarrage de Spring Boot et le chargement initial de H2 ne sont pas inclus dans les latences HTTP.
- L'URL cible est configurable avec `BASE_URL`.
- Les réponses HTTP et les seuils k6 sont vérifiés à chaque requête.

Exemple de lancement :

```shell
BASE_URL=http://localhost:8080 k6 run performance/scenarios/k6/parcours-principaux.js
```

## Scénario 1 : disponibilité de l'application

### Objectif

Vérifier rapidement que l'application est démarrée et qu'une route principale répond avant toute campagne de charge.

### Parcours

```text
GET /proprietaires
```

### Vérifications

- Le statut HTTP est `200`.
- La réponse contient le titre ou un élément caractéristique de la page des propriétaires.
- Le taux d'erreur est nul pendant le smoke test.

### Profil

- 1 utilisateur virtuel.
- Durée : 30 secondes.
- Exécution avant chaque autre scénario.

## Scénario 2 : consultation de la liste des propriétaires

### Objectif

Mesurer la performance du parcours de consultation principal avec une volumétrie faible puis volumineuse.

### Parcours

```text
GET /proprietaires
```

### Vérifications

- Le statut HTTP est `200`.
- La page contient la liste des propriétaires.
- Le nombre de résultats est cohérent avec le jeu de données chargé.
- La latence p95 reste sous le seuil défini pour les lectures.

### Données

- Jeu de référence issu de `data.sql`.
- Jeu volumineux décrit dans `performance/doc/jeux-de-donnees.md`.

## Scénario 3 : recherche d'un propriétaire

### Objectif

Mesurer une requête de recherche avec paramètre, notamment lorsque la table contient 10 000 propriétaires ou davantage.

### Parcours

```text
GET /proprietaires?nom=NOM5000
```

### Vérifications

- Le statut HTTP est `200`.
- La réponse est rendue sans erreur de template.
- Le propriétaire recherché apparaît dans la réponse.
- Le nombre de résultats correspond au filtre demandé.
- La latence p95 est mesurée séparément de la consultation sans filtre.

### Variantes

- Recherche correspondant à un résultat.
- Recherche ne correspondant à aucun résultat.
- Recherche avec une chaîne vide.

## Scénario 4 : consultation du détail d'un propriétaire

### Objectif

Mesurer le chargement d'un propriétaire avec ses animaux, ses visites et la liste dynamique des types d'animaux.

### Parcours

```text
GET /proprietaires/{id}
```

Utiliser plusieurs identifiants existants, par exemple `1`, `5000` et `10000` dans le jeu volumineux.

### Vérifications

- Le statut HTTP est `200`.
- Le nom du propriétaire est présent.
- Les animaux associés sont rendus.
- Les visites associées sont rendues.
- La liste déroulante des types d'animaux est présente.
- Le type dynamique attendu est présent dans les options.

### Points d'observation

- Nombre de requêtes SQL exécutées.
- Temps de chargement des associations JPA.
- Impact de `FetchType.EAGER` sur les propriétaires possédant plusieurs animaux et visites.

## Scénario 5 : consultation de la liste des vétérinaires

### Objectif

Mesurer une page de lecture indépendante des propriétaires et des animaux.

### Parcours

```text
GET /veterinaires
```

### Vérifications

- Le statut HTTP est `200`.
- La page contient les vétérinaires.
- Les spécialités sont rendues pour les vétérinaires concernés.
- La latence p95 est enregistrée.

## Scénario 6 : création d'un propriétaire

### Objectif

Mesurer le parcours d'écriture d'un propriétaire et vérifier que la validation et la redirection restent fonctionnelles sous charge.

### Parcours

```text
GET /proprietaires/nouveau
POST /proprietaires
```

Le `POST` doit utiliser un nom, un prénom, une adresse, une ville et un téléphone uniques par itération.

### Vérifications

- Le formulaire initial répond avec le statut `200`.
- La soumission valide retourne une redirection `3xx` vers le détail du propriétaire créé.
- Aucun statut `5xx` n'est observé.
- Les écritures concurrentes ne provoquent pas de collision d'identifiant.

### Limite

Ce scénario modifie la base. Il doit être exécuté sur une base de performance réinitialisable et séparément des campagnes de lecture comparatives.

## Scénario 7 : ajout d'un animal

### Objectif

Mesurer l'ajout d'un animal à un propriétaire existant, avec un type issu de la liste des types disponibles.

### Parcours

```text
POST /proprietaires/{proprietaireId}/animaux
```

Paramètres :

- `nom` : valeur unique par itération ;
- `type` : libellé existant dans `types_animaux` ;
- `dateNaissance` : date valide.

### Vérifications

- La réponse est une redirection `3xx` vers le détail du propriétaire.
- Aucun statut `4xx` ou `5xx` inattendu n'est observé.
- Le type transmis appartient aux types chargés en base.
- Les écritures concurrentes restent persistées correctement.

## Scénario 8 : ajout d'une visite

### Objectif

Mesurer l'ajout d'une visite sur un animal existant.

### Parcours

```text
POST /proprietaires/{proprietaireId}/animaux/{animalId}/visites
```

Paramètres :

- `date` : date valide ;
- `motif` : valeur unique ou suffisamment variée par itération.

### Vérifications

- La réponse est une redirection `3xx` vers le détail du propriétaire.
- Aucun statut `5xx` n'est observé.
- Les clés étrangères `proprietaireId` et `animalId` correspondent à des données existantes.
- La latence p95 des écritures est respectée.

## Scénario 9 : parcours principaux combinés

### Objectif

Mesurer un parcours représentatif composé des principales consultations de l'application. Ce scénario permet d'observer la performance globale d'une séquence de navigation, sans mélanger les résultats avec les scénarios d'écriture dédiés.

### Parcours

```text
GET /proprietaires
GET /proprietaires?nom=NOM5000
GET /proprietaires/1
GET /veterinaires
```

Le script correspondant est `performance/scenarios/k6/parcours-principaux.js`.

### Vérifications

- La liste des propriétaires répond avec le statut `200`.
- La recherche d'un propriétaire répond avec le statut `200` et retourne le résultat attendu.
- Le détail d'un propriétaire répond avec le statut `200`.
- La liste des vétérinaires répond avec le statut `200`.
- Toutes les assertions fonctionnelles sont réussies.
- Le taux d'erreur HTTP reste inférieur à 1 %.
- La latence p95 du parcours est mesurée pour identifier l'impact de la page la plus lente.

### Profil

- 1 utilisateur virtuel par défaut.
- 1 itération par défaut pour une mesure contrôlée.
- Exécution possible avec le jeu de référence ou le jeu volumineux.

### Points d'observation

- Comparer la latence du parcours avec celle de chaque scénario individuel.
- Identifier l'influence de `GET /proprietaires` lorsque 10 000 propriétaires sont rendus.
- Vérifier que les consultations successives ne provoquent aucune erreur de session ou de persistance.

## Profils de charge

Chaque scénario peut être exécuté seul pour diagnostiquer un problème. Une campagne complète combine principalement les scénarios de lecture et une faible proportion d'écritures.

| Profil    | Charge | Durée | Usage |
|-----------| ---: | ---: | --- |
| Smoke     | 1 utilisateur virtuel | 30 s | Vérifier la disponibilité et les assertions |
| Nominale  | montée jusqu'à 10 utilisateurs virtuels | 5 min | Établir la référence |
| Endurance | charge nominale | 30 min minimum | Détecter les dérives mémoire et latence |
| Pic       | augmentation brutale | selon campagne | Observer la saturation et la récupération |

Répartition initiale recommandée pour le parcours complet :

- 35 % liste des propriétaires ;
- 25 % recherche ;
- 20 % détail d'un propriétaire ;
- 10 % liste des vétérinaires ;
- 5 % ajout d'un animal ;
- 5 % ajout d'une visite.

La création d'un propriétaire est exécutée dans une campagne dédiée afin de limiter les effets de bord sur les autres scénarios.

### Campagne nominale

La campagne nominale combine les six parcours selon la répartition recommandée ci-dessus. Pour accélérer les phases de construction et d'itération, elle monte progressivement jusqu'à 10 utilisateurs virtuels en 15 secondes, conserve cette charge pendant 30 secondes, puis redescend en 15 secondes. Sa durée totale est d'environ 1 minute. Afin d'éviter les conflits JPA connus lors d'écritures concurrentes, les ajouts d'animaux et de visites sont effectués uniquement par le VU 1 ; les autres VU réalisent des lectures à la place.

Elle est lancée avec `just performance-nominale`. Le script démarre automatiquement l'application avec le profil `performance` si elle n'est pas déjà disponible. Sa durée est configurable avec `NOMINALE_DURATION_MINUTES` : `1` par défaut, ou `5` et `10` pour des campagnes plus longues.

```shell
NOMINALE_DURATION_MINUTES=1 just performance-nominale
NOMINALE_DURATION_MINUTES=5 just performance-nominale
NOMINALE_DURATION_MINUTES=10 just performance-nominale
```

La durée de montée et de descente reste fixée à 15 secondes chacune ; la phase de maintien est calculée pour atteindre la durée totale demandée.

## Seuils de réussite

Les seuils ci-dessous sont des valeurs initiales à confirmer avec les objectifs métier :

- taux d'erreur inférieur à 1 % ;
- p95 inférieur à 500 ms pour les lectures ;
- p95 inférieur à 1 seconde pour les écritures ;
- aucun épuisement du pool Hikari ;
- aucune croissance mémoire continue pendant l'endurance.

Chaque rapport doit présenter les résultats par scénario, et non uniquement une moyenne globale.
