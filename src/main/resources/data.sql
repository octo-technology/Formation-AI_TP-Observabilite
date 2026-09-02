INSERT INTO veterinaires (id, prenom, nom) VALUES (1, 'Camille', 'MARTIN');
INSERT INTO veterinaires (id, prenom, nom) VALUES (2, 'Sophie', 'BERNARD');
INSERT INTO veterinaires (id, prenom, nom) VALUES (3, 'Julien', 'DUBOIS');
INSERT INTO veterinaires (id, prenom, nom) VALUES (4, 'Claire', 'MOREAU');
INSERT INTO veterinaires (id, prenom, nom) VALUES (5, 'Thomas', 'LAURENT');
INSERT INTO veterinaires (id, prenom, nom) VALUES (6, 'Elodie', 'PETIT');

INSERT INTO specialites (id, libelle) VALUES (1, 'Radiologie');
INSERT INTO specialites (id, libelle) VALUES (2, 'Chirurgie');
INSERT INTO specialites (id, libelle) VALUES (3, 'Dentisterie');

INSERT INTO veterinaires_specialites (veterinaire_id, specialite_id) VALUES (2, 1);
INSERT INTO veterinaires_specialites (veterinaire_id, specialite_id) VALUES (3, 2);
INSERT INTO veterinaires_specialites (veterinaire_id, specialite_id) VALUES (3, 3);
INSERT INTO veterinaires_specialites (veterinaire_id, specialite_id) VALUES (4, 2);
INSERT INTO veterinaires_specialites (veterinaire_id, specialite_id) VALUES (5, 1);

INSERT INTO types_animaux (id, libelle) VALUES (1, 'chat');
INSERT INTO types_animaux (id, libelle) VALUES (2, 'chien');
INSERT INTO types_animaux (id, libelle) VALUES (3, 'lezard');
INSERT INTO types_animaux (id, libelle) VALUES (4, 'serpent');
INSERT INTO types_animaux (id, libelle) VALUES (5, 'oiseau');
INSERT INTO types_animaux (id, libelle) VALUES (6, 'hamster');

INSERT INTO proprietaires (id, prenom, nom, adresse, ville, telephone) VALUES (1, 'Jean', 'LEFEVRE', '12 rue de la Liberte', 'Paris', '01 42 68 53 10');
INSERT INTO proprietaires (id, prenom, nom, adresse, ville, telephone) VALUES (2, 'Marie', 'MOREL', '8 avenue Victor Hugo', 'Lyon', '04 72 14 36 58');
INSERT INTO proprietaires (id, prenom, nom, adresse, ville, telephone) VALUES (3, 'Pierre', 'ROUSSEAU', '27 rue du Commerce', 'Nantes', '02 40 58 71 23');
INSERT INTO proprietaires (id, prenom, nom, adresse, ville, telephone) VALUES (4, 'Isabelle', 'FOURNIER', '5 rue des Ecoles', 'Bordeaux', '05 56 18 42 79');
INSERT INTO proprietaires (id, prenom, nom, adresse, ville, telephone) VALUES (5, 'Nicolas', 'GIRARD', '19 boulevard Carnot', 'Lille', '03 20 47 62 15');
INSERT INTO proprietaires (id, prenom, nom, adresse, ville, telephone) VALUES (6, 'Anne', 'MERCIER', '42 rue des Lilas', 'Toulouse', '05 61 29 84 37');
INSERT INTO proprietaires (id, prenom, nom, adresse, ville, telephone) VALUES (7, 'Laurent', 'DUPONT', '16 avenue Jean Jaures', 'Strasbourg', '03 88 25 46 91');
INSERT INTO proprietaires (id, prenom, nom, adresse, ville, telephone) VALUES (8, 'Sophie', 'LAMBERT', '31 rue Nationale', 'Rennes', '02 99 34 57 82');
INSERT INTO proprietaires (id, prenom, nom, adresse, ville, telephone) VALUES (9, 'Michel', 'BONNET', '9 chemin des Oliviers', 'Marseille', '04 91 63 28 74');
INSERT INTO proprietaires (id, prenom, nom, adresse, ville, telephone) VALUES (10, 'Chloe', 'FAURE', '24 rue de la Republique', 'Montpellier', '04 67 12 59 86');

INSERT INTO animaux (id, nom, date_naissance, type_animal_id, proprietaire_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO animaux (id, nom, date_naissance, type_animal_id, proprietaire_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO animaux (id, nom, date_naissance, type_animal_id, proprietaire_id) VALUES (3, 'Rosie', '2011-04-17', 2, 3);
INSERT INTO animaux (id, nom, date_naissance, type_animal_id, proprietaire_id) VALUES (4, 'Bijou', '2010-03-07', 2, 3);
INSERT INTO animaux (id, nom, date_naissance, type_animal_id, proprietaire_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO animaux (id, nom, date_naissance, type_animal_id, proprietaire_id) VALUES (6, 'Georges', '2010-01-20', 4, 5);
INSERT INTO animaux (id, nom, date_naissance, type_animal_id, proprietaire_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO animaux (id, nom, date_naissance, type_animal_id, proprietaire_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO animaux (id, nom, date_naissance, type_animal_id, proprietaire_id) VALUES (9, 'Chance', '2011-08-06', 5, 7);
INSERT INTO animaux (id, nom, date_naissance, type_animal_id, proprietaire_id) VALUES (10, 'Moka', '2007-02-24', 2, 8);
INSERT INTO animaux (id, nom, date_naissance, type_animal_id, proprietaire_id) VALUES (11, 'Félix', '2010-03-09', 5, 9);
INSERT INTO animaux (id, nom, date_naissance, type_animal_id, proprietaire_id) VALUES (12, 'Praline', '2010-06-24', 2, 10);
INSERT INTO animaux (id, nom, date_naissance, type_animal_id, proprietaire_id) VALUES (13, 'Néo', '2012-06-08', 1, 10);

INSERT INTO visites (id, animal_id, date_visite, motif) VALUES (1, 7, '2013-01-01', 'Vaccination contre la rage');
INSERT INTO visites (id, animal_id, date_visite, motif) VALUES (2, 8, '2013-01-02', 'Vaccination contre la rage');
INSERT INTO visites (id, animal_id, date_visite, motif) VALUES (3, 8, '2013-01-03', 'Castration');
INSERT INTO visites (id, animal_id, date_visite, motif) VALUES (4, 7, '2013-01-04', 'Stérilisation');

ALTER TABLE proprietaires ALTER COLUMN id RESTART WITH 11;
ALTER TABLE animaux ALTER COLUMN id RESTART WITH 14;
ALTER TABLE visites ALTER COLUMN id RESTART WITH 5;
ALTER TABLE veterinaires ALTER COLUMN id RESTART WITH 7;
ALTER TABLE specialites ALTER COLUMN id RESTART WITH 4;
ALTER TABLE types_animaux ALTER COLUMN id RESTART WITH 7;
