import { get, optionsParDefaut } from "./commun.js";

export const options = optionsParDefaut;

export default function () {
  const proprietaireId = Number(__ENV.PROPRIETAIRE_ID || 5000);
  get(`/proprietaires/${proprietaireId}`, {
    "détail du propriétaire rendu": (response) => response.body.includes("Enregistrer un animal"),
    "types animaux rendus": (response) => response.body.includes("Sélectionner un type"),
  });
}
