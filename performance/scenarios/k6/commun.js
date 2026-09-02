import http from "k6/http";
import { check } from "k6";

export const baseUrl = __ENV.BASE_URL || "http://localhost:8080";

export function get(path, checks = {}) {
  const response = http.get(`${baseUrl}${path}`);
  check(response, { "statut attendu": (r) => r.status === 200, ...checks });
  return response;
}

export function post(path, payload, checks = {}) {
  const response = http.post(`${baseUrl}${path}`, payload);
  check(response, { "redirection attendue": (r) => r.status >= 300 && r.status < 400, ...checks });
  return response;
}

export function valeurUnique(prefix) {
  return `${prefix}-${__VU}-${__ITER}-${Date.now()}`;
}

export const optionsParDefaut = {
  maxRedirects: 0,
  iterations: 1,
  vus: 1,
  thresholds: {
    http_req_failed: ["rate<0.01"],
    http_req_duration: ["p(95)<500"],
  },
};
