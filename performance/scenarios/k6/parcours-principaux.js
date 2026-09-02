import { group } from "k6";
import { get, optionsParDefaut } from "./commun.js";

export const options = optionsParDefaut;

export default function () {
  group("consultations", () => {
    get("/proprietaires", {
      "liste des propriétaires rendue": (response) => response.body.includes("Propriétaires"),
    });
    get("/proprietaires?nom=NOM5000", {
      "recherche rendue": (response) => response.body.includes("NOM5000"),
    });
    get("/proprietaires/1", {
      "détail rendu": (response) => response.body.includes("Enregistrer un animal"),
    });
    get("/veterinaires", {
      "liste des vétérinaires rendue": (response) => response.body.includes("Vétérinaires"),
    });
  });
}
