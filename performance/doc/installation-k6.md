# Installation de k6

k6 est l'outil retenu pour générer la charge HTTP. Il doit être installé sur la machine qui exécute les tests, et non dans l'application Java.

La génération automatique du rapport utilise également `jq` pour lire les synthèses JSON k6.

```shell
brew install jq
jq --version
```

Sur Linux Debian ou Ubuntu :

```shell
sudo apt-get update
sudo apt-get install -y jq
jq --version
```

## macOS avec Homebrew

```shell
brew install k6
k6 version
```

## Linux Debian ou Ubuntu

```shell
sudo gpg -k
sudo apt-get update
sudo apt-get install -y gnupg ca-certificates
curl -s https://dl.k6.io/key.gpg | sudo gpg --dearmor -o /usr/share/keyrings/k6-archive-keyring.gpg
echo "deb [signed-by=/usr/share/keyrings/k6-archive-keyring.gpg] https://dl.k6.io/deb stable main" | sudo tee /etc/apt/sources.list.d/k6.list
sudo apt-get update
sudo apt-get install -y k6
k6 version
```

## Windows avec Chocolatey

```powershell
choco install k6
k6 version
```

## Installation dans un pipeline CI

Utiliser de préférence l'image officielle ou le gestionnaire de paquets de l'environnement CI. La pipeline doit vérifier la présence de k6 avant d'exécuter un test :

```shell
command -v k6
k6 version
```

La version utilisée doit être consignée dans le rapport final, car les résultats peuvent varier entre versions.

## Vérification de l'application

Démarrer l'application dans un terminal :

```shell
./gradlew bootRun
```

Dans un autre terminal, vérifier qu'elle répond avant de lancer k6 :

```shell
curl --fail --silent --show-error http://localhost:8080/proprietaires > /dev/null
```

L'URL cible pourra être remplacée avec `BASE_URL` lorsque les scripts k6 seront disponibles :

```shell
BASE_URL=http://localhost:8080 k6 run performance/scenarios/k6/parcours-principaux.js
```
