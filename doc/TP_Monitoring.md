# 🤖 TP - Mise en place d'une stack Prometheus / Grafana 

> [!WARNING]
> _Docker doit impérativement être installé et utilisable sur votre machine !_

## 1. Vérification de l'application et des tests de performance

- Vérifier que l'application fonctionne bien sur votre machine
- Vérifier que les tests de performance (Scénarios et Campagne nominale) s'exécutent correctement
- cf. le [README](../README.md) 

## 2. Création de la SKILL Stack Prometheus / Grafana

#### Objectif 

- Utiliser votre agent LLM (via OpenCode) pour qu'il crée le SKILL dédié pour installer, configurer et tester la Stack Prometheus / Grafana
- Cette stack servira à monitorer votre application, et voir dynamiquement la progression des tests de performance

#### Préparation

- Préparer le prompt ad hoc 
  - Par exemple, vous pouvez utiliser celui fourni ci-dessous
  
      <div width="500px">
      
      ```text
      1. Création dans le dossier OpenCode de ce projet, d'un nouveau SKILL nommé "install-prometheus-Grafana"
      2. Sa description est : Guide interactif pour installer et configurer dans cette application, Prometheus, Grafana et les autres outils nécessaires, pour réaliser le dashboard de l'application d'observabilité et de monitoring
      3. Le dashbord final doit être celui qui est défini dans le fichier observabilite/grafana-dashboard-spring-boot-observability.json
      4. Au préalable ce skill pourra être lancé seulement si Docker est disponible sur la machine de l'utilisateur
      5. Chaque étape doit être validée avant de passer à la suivante : vérifie l'état actuel avant de recommander une action, et adapte les instructions à ce que tu observes
      ```
    
      </div>

## 3. Exécution du SKILL créé (en mode séquentiel)

- Utiliser le SKILL que vous avez créé, étape par étape...

## 🎯 Vérification finale

#### a - Lancer la Stack Prometheus / Grafana

#### b - Lancer l'application

#### c - Lancer la Campagne nominale

#### d - Résultat attendu

- cf. [Exemple du Dashboad Spring](https://grafana.com/grafana/dashboards/25359-spring-boot-observability)
