import { get, optionsParDefaut } from "./commun.js";

export const options = optionsParDefaut;

export default function () {
  get("/proprietaires?nom=NOM5000", {
    "résultat de recherche rendu": (response) => response.body.includes("NOM5000"),
  });
}
