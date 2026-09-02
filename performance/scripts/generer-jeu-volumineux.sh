#!/usr/bin/env bash
set -euo pipefail

repertoire_script="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
fichier_sortie="${repertoire_script}/data/data-volumineux.sql"
nombre_proprietaires="${NOMBRE_PROPRIETAIRES:-10000}"
nombre_animaux="${NOMBRE_ANIMAUX:-30000}"
nombre_visites="${NOMBRE_VISITES:-100000}"

mkdir -p "${repertoire_script}/data"

{
    printf '%s\n' '-- Jeu volumineux généré. Ne pas exécuter avec data.sql : ce fichier contient toutes les données.'
    printf '%s\n' '-- Volumes : 10 000 propriétaires, 30 000 animaux, 100 000 visites.'
    printf 'DELETE FROM visites;\nDELETE FROM animaux;\nDELETE FROM proprietaires;\n'

    for ((id=1; id<=nombre_proprietaires; id++)); do
        printf "INSERT INTO proprietaires (id, prenom, nom, adresse, ville, telephone) VALUES (%d, 'Prenom%d', 'NOM%d', '%d rue de la Performance', 'Ville%d', '01 %02d %02d %02d %02d');\n" \
            "${id}" "${id}" "${id}" "${id}" "${id}" "$((id % 100))" "$((id % 100))" "$((id % 100))" "$((id % 100))"
    done

    for ((id=1; id<=nombre_animaux; id++)); do
        proprietaire_id=$(( (id - 1) % nombre_proprietaires + 1 ))
        type_id=$(( (id - 1) % 6 + 1 ))
        date_naissance=$(printf '2010-%02d-%02d' "$(( (id - 1) % 12 + 1 ))" "$(( (id - 1) % 28 + 1 ))")
        printf "INSERT INTO animaux (id, nom, date_naissance, type_animal_id, proprietaire_id) VALUES (%d, 'Animal%d', '%s', %d, %d);\n" \
            "${id}" "${id}" "${date_naissance}" "${type_id}" "${proprietaire_id}"
    done

    for ((id=1; id<=nombre_visites; id++)); do
        animal_id=$(( (id - 1) % nombre_animaux + 1 ))
        date_visite=$(printf '2024-%02d-%02d' "$(( (id - 1) % 12 + 1 ))" "$(( (id - 1) % 28 + 1 ))")
        printf "INSERT INTO visites (id, animal_id, date_visite, motif) VALUES (%d, %d, '%s', 'Visite de controle %d');\n" \
            "${id}" "${animal_id}" "${date_visite}" "${id}"
    done

    printf 'ALTER TABLE proprietaires ALTER COLUMN id RESTART WITH %d;\n' "$((nombre_proprietaires + 1))"
    printf 'ALTER TABLE animaux ALTER COLUMN id RESTART WITH %d;\n' "$((nombre_animaux + 1))"
    printf 'ALTER TABLE visites ALTER COLUMN id RESTART WITH %d;\n' "$((nombre_visites + 1))"
} > "${fichier_sortie}"

printf 'Jeu généré : %s\n' "${fichier_sortie}"
printf 'Propriétaires : %d\nAnimaux : %d\nVisites : %d\n' "${nombre_proprietaires}" "${nombre_animaux}" "${nombre_visites}"
