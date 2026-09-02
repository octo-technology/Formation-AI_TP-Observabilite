import { check, group } from "k6";
import http from "k6/http";
import { baseUrl, optionsParDefaut, valeurUnique } from "./commun.js";

const { iterations, vus, ...optionsSansIterations } = optionsParDefaut;
const dureeMinutes = Number(__ENV.NOMINAL_DURATION_MINUTES || 1);

if (!Number.isInteger(dureeMinutes) || dureeMinutes < 1) {
  throw new Error("NOMINAL_DURATION_MINUTES doit être un entier supérieur ou égal à 1");
}

const dureeMaintienSecondes = dureeMinutes * 60 - 30;

export const options = {
  ...optionsSansIterations,
  stages: [
    { duration: "15s", target: 10 },
    { duration: `${dureeMaintienSecondes}s`, target: 10 },
    { duration: "15s", target: 0 },
  ],
};

function get(path, checks) {
  const response = http.get(`${baseUrl}${path}`);
  check(response, { "statut attendu": (r) => r.status === 200, ...checks });
}

function post(path, payload) {
  const response = http.post(`${baseUrl}${path}`, payload);
  check(response, { "redirection attendue": (r) => r.status >= 300 && r.status < 400 });
}

export default function () {
  const choix = Math.random() * 100;
  // Chaque VU utilise son propre propriétaire pour limiter les conflits d'écriture JPA.
  const proprietaireId = ((__VU - 1) % 10) + 1;
  const animalId = proprietaireId;

  if (choix < 35) {
    group("liste des propriétaires", () => get("/proprietaires", { "liste rendue": (r) => r.body.includes("Propriétaires") }));
  } else if (choix < 60) {
    group("recherche d'un propriétaire", () => get(`/proprietaires?nom=NOM${proprietaireId}`, { "résultat rendu": (r) => r.body.includes(`NOM${proprietaireId}`) }));
  } else if (choix < 80) {
    group("détail d'un propriétaire", () => get(`/proprietaires/${proprietaireId}`, { "détail rendu": (r) => r.body.includes("Enregistrer un animal") }));
  } else if (choix < 90) {
    group("liste des vétérinaires", () => get("/veterinaires", { "liste rendue": (r) => r.body.includes("Vétérinaires") }));
  } else if (choix < 95 && __VU === 1) {
    group("ajout d'un animal", () => post(`/proprietaires/${proprietaireId}/animaux`, { nom: valeurUnique("Animal nominal"), type: valeurUnique("type-nominal"), dateNaissance: "2020-01-01" }));
  } else if (__VU === 1) {
    group("ajout d'une visite", () => post(`/proprietaires/${proprietaireId}/animaux/${animalId}/visites`, { date: "2024-01-01", motif: valeurUnique("Visite nominale") }));
  } else {
    group("détail d'un propriétaire", () => get(`/proprietaires/${proprietaireId}`, { "détail rendu": (r) => r.body.includes("Enregistrer un animal") }));
  }
}
