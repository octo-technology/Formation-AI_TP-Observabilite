# Création des jeux de données

Les jeux de données doivent être préparés avant le lancement d'une campagne. Leur génération ne doit pas être incluse dans la mesure de performance HTTP.

## Jeu de référence

Le jeu minimal est celui fourni par :

- `src/main/resources/schema.sql` pour le schéma ;
- `src/main/resources/data.sql` pour les données de démonstration.

Il permet de valider les scripts k6 et de comparer rapidement les résultats entre deux versions.

## Jeu volumineux cible

Prévoir un jeu comprenant au minimum :

- 10 000 propriétaires ;
- 30 000 animaux ;
- 100 000 visites ;
- plusieurs types d'animaux et spécialités.

Les relations doivent être réparties de manière réaliste afin d'éviter qu'un seul propriétaire ou animal concentre toute la charge.

Le générateur reproductible est disponible dans `performance/scripts/generer-jeu-volumineux.sh`. Il produit `performance/data/data-volumineux.sql` avec les volumes cibles par défaut :

```shell
./performance/scripts/generer-jeu-volumineux.sh
```

Les volumes sont configurables avec `NOMBRE_PROPRIETAIRES`, `NOMBRE_ANIMAUX` et `NOMBRE_VISITES`.

## Méthode de préparation

1. Générer les instructions SQL ou un fichier de données dans un répertoire temporaire non versionné.
2. Arrêter l'application avant de réinitialiser la base H2.
3. Démarrer l'application avec le jeu sélectionné.
4. Vérifier quelques volumes avec la console H2 ou des requêtes SQL.
5. Vérifier que les identifiants utilisés par les scénarios k6 existent.
6. Lancer un smoke test avant toute campagne longue.

La base H2 étant en mémoire, elle est recréée à chaque redémarrage de l'application. Le fichier généré conserve les types d'animaux, spécialités et vétérinaires chargés par `data.sql`, et remplace les propriétaires, animaux et visites de démonstration. Pour une campagne volumineuse, mesurer séparément le temps de démarrage et ne pas l'inclure dans la latence HTTP.

## Données d'écriture

Les scénarios de création doivent utiliser des valeurs uniques par itération afin d'éviter les collisions. Les données de lecture peuvent utiliser un ensemble stable d'identifiants existants.

## Contrôles avant campagne

```sql
SELECT COUNT(*) FROM proprietaires;
SELECT COUNT(*) FROM animaux;
SELECT COUNT(*) FROM visites;
SELECT COUNT(*) FROM types_animaux;
```

Conserver les volumes, la version de l'application, la version Java et la configuration machine avec chaque rapport.
