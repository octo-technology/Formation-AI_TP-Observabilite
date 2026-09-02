import { optionsParDefaut, post, valeurUnique } from "./commun.js";

export const options = optionsParDefaut;

export default function () {
  post("/proprietaires/1/animaux/1/visites", {
    date: "2024-01-01",
    motif: valeurUnique("Visite de contrôle"),
  });
}
