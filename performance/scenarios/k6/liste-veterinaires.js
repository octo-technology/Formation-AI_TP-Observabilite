import { get, optionsParDefaut } from "./commun.js";

export const options = optionsParDefaut;

export default function () {
  get("/veterinaires", {
    "liste des vétérinaires rendue": (response) => response.body.includes("Vétérinaires"),
  });
}
