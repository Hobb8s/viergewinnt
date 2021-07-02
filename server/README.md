# Viergewinnt Server

Über diesen Server kann man das Vier-Gewinnt Spiel zusammen über das Internet Spielen.  
  
Der Server erstellt für jedes Spiel einen eigenen Raum mit maximal 2 Clients ( 2 Spielern )  

## Herunterladen

[👉 Windows](https://github.com/Hobb8s/viergewinnt/releases/download/v1.1/viergewinnt-server-windows.exe)  
[👉 Mac OS X](https://github.com/Hobb8s/viergewinnt/releases/download/v1.1/viergewinnt-server-macos)  
[👉 Linux](https://github.com/Hobb8s/viergewinnt/releases/download/v1.1/viergewinnt-server-linux)  
[🐳 Dockerimage](https://github.com/Hobb8s/viergewinnt/releases/download/v1.1/viergewinnt-docker.tar)  

## Anleitung

### Manuelle Konfiguration
Wenn Sie das Programm für ensprechend für Ihre Platform ausführen, startet ein Einrichtungs-Assistent

### Automatische Konfiguration
Um die manuelle Konfiguration zu überspringen, müssen folgende Umgebungsvariablen gesetzt sein.

```sh
EXETYPE=AUTO # Ändert die Ausführungsart: Manuell -> Auto / Docker
ERSTELLE_AUTOMATISCH_RAEUME=TRUE # Wenn Räume nicht automatisch erstellt werden sollen: 'FALSE'.
PORT=3000 # Legt den Port, auf dem der Server laufen soll, fest.
```

### Docker
1. Laden Sie sich das Docker Image herrunter und laden sie es als Dockerimage
```sh
docker load -i viergewinnt-server-docker.tar
```
2. Starten Sie den Docker-Container mit dem geladenen Dockerimage
```sh
docker run -d --name viergewinnt-server -p 3000:3000 -e ERSTELLE_AUTOMATISCH_RAEUME=TRUE -e PORT=3000 --restart=always viergewinnt:latest
```
3. Anschließend ist der Server lokal erreichbar unter `ws://127.0.0.1:3000`.

## Weiterentwickeln

```sh
git clone https://github.com/Hobb8s/vier-gewinnt.git

cd vier-gewinnt/server/

# installiert alle nötigen Pakete
yarn install

# führt das Programm aus
yarn runjs

# kompiliert das Programm neu und führt das Programm aus
yarn runts

# führt 'yarn runts' mit nodemon aus
yarn serve

# kompiliert das Programm neu und baut ausführbare Dateien für Windows, OS X und Linux
yarn build
```