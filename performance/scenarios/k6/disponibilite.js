import { get, optionsParDefaut } from "./commun.js";

export const options = optionsParDefaut;

export default function () {
  get("/proprietaires", {
    "page propriétaires rendue": (response) => response.body.includes("Propriétaires"),
  });
}
