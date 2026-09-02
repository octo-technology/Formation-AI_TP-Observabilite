import { optionsParDefaut, post, valeurUnique } from "./commun.js";

export const options = optionsParDefaut;

export default function () {
  post("/proprietaires/1/animaux", {
    nom: valeurUnique("Animal"),
    type: "chien",
    dateNaissance: "2020-01-01",
  });
}
