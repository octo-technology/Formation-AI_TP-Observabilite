import { get, optionsParDefaut } from "./commun.js";

export const options = optionsParDefaut;

export default function () {
  get("/proprietaires", {
    "liste des propriétaires rendue": (response) => response.body.includes("Propriétaires"),
  });
}
