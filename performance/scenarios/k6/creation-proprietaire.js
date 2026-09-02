import { get, optionsParDefaut, post, valeurUnique } from "./commun.js";

export const options = optionsParDefaut;

export default function () {
  get("/proprietaires/nouveau", {
    "formulaire propriétaire rendu": (response) => response.body.includes("Ajouter un propriétaire"),
  });
  post("/proprietaires", {
    prenom: valeurUnique("Prenom"),
    nom: valeurUnique("NOM"),
    adresse: "1 rue de la Performance",
    ville: "Ville de test",
    telephone: "01 02 03 04 05",
  });
}
