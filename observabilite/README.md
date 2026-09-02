# Observabilite

Cette configuration fournit l'observabilite de l'application Petclinic avec
Spring Boot Actuator, Micrometer Tracing, OpenTelemetry et un collector local.

## Signaux disponibles

- **Metriques** : exposees localement par Actuator sur `/actuator/metrics`.
  Leur export est desactive par defaut ; aucune metrique n'est envoyee au
  collector.
- **Traces** : generees automatiquement pour les requetes web et exportees au
  format OTLP vers le collector lorsqu'elles sont activees.
- **Logs** : ecrits sur la console avec le nom du service, le `traceId` et le
  `spanId` lorsqu'un contexte de trace est disponible.
- **Collector** : recoit uniquement les traces OTLP sur les ports `4317`
  (gRPC) et `4318` (HTTP), puis les affiche avec l'exporteur `debug`.

Prometheus, Grafana et aucun backend de stockage ne sont installes dans cette
configuration. Ils pourront etre ajoutes ulterieurement.

## Lancer le collector

Depuis la racine du projet :

```shell
docker compose -f observabilite/compose.observabilite.yml up -d
```

Verifier son etat :

```shell
docker compose -f observabilite/compose.observabilite.yml ps
```

Consulter les traces recues par le collector :

```shell
docker compose -f observabilite/compose.observabilite.yml logs -f otel-collector
```

Arreter le collector :

```shell
docker compose -f observabilite/compose.observabilite.yml down
```

## Lancer l'application

Les traces sont desactivees par defaut afin que l'application demarre sans
dependre de la disponibilite du collector. Pour lancer l'application avec les
traces exportees vers le collector local :

```shell
OTEL_TRACES_ENABLED=true \
OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://localhost:4318/v1/traces \
./gradlew bootRun
```

L'application est alors disponible sur http://localhost:8080.

## Verifier les signaux

Verifier les metriques Actuator :

```shell
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/metrics
```

Generer une trace et un log correle :

```shell
curl http://localhost:8080/proprietaires
```

Le log applicatif contient alors un prefixe de la forme :

```text
[petclinic,<traceId>,<spanId>]
```

Le collector affiche la trace correspondante dans ses logs, notamment le span
`http get /proprietaires` et le statut HTTP `200`.

## Variables de configuration

| Variable | Defaut | Role |
| --- | --- | --- |
| `OTEL_TRACES_ENABLED` | `false` | Active l'instrumentation et l'export des traces |
| `OTEL_TRACES_SAMPLER_ARG` | `1.0` | Taux d'echantillonnage des traces |
| `OTEL_EXPORTER_OTLP_TRACES_ENDPOINT` | `http://localhost:4318/v1/traces` | Endpoint OTLP HTTP des traces |
