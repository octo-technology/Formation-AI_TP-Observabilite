### 🍒️ Installation de k6

k6 est l'outil retenu pour générer la charge HTTP. Il doit être installé sur la machine qui exécute les tests, et non dans l'application Java.

La génération automatique du rapport utilise également `jq` pour lire les synthèses JSON k6.

#### MacOS avec Homebrew

```shell
brew install k6
k6 version
```

#### Linux Debian ou Ubuntu

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

#### Windows avec Chocolatey

```powershell
choco install k6
k6 version
```

### 🍐 Installation de jq

#### MacOS avec Homebrew

```shell
brew install jq
jq --version
```

#### Linux Debian ou Ubuntu

```shell
sudo apt-get update
sudo apt-get install -y jq
jq --version
```

#### Windows avec Chocolatey

```powershell
choco install jq
jq --version
```

### 🍊 Installation de Just

#### MacOS avec Homebrew

```shell
brew install just
just --version
```

#### Linux Debian ou Ubuntu

```shell
sudo apt-get update
sudo apt-get install -y just
just --version
```

#### Windows avec Chocolatey

```powershell
choco install just
just --version
```

### ✅ Vérification de l'application et de k6

- Démarrer l'application dans un terminal
  ```shell
  ./gradlew bootRun
  ```

- Dans un autre terminal, vérifier qu'elle répond avant de lancer k6
  ```shell
  curl --fail --silent --show-error http://localhost:8080/proprietaires > /dev/null
  ```

  ```shell
  BASE_URL=http://localhost:8080 k6 run performance/scenarios/k6/parcours-principaux.js
  ```
