# Viergewinnt Server

Über diesen Server kann man das Vier-Gewinnt Spiel zusammen über das Internet Spielen.  
  
Der Server erstellt für jedes Spiel einen eigenen Raum mit maximal 2 Clients ( 2 Spielern )

## Herunterladen

[👉 Windows](https://github.com/Hobb8s/vier-gewinnt/tree/main/server)  
[👉 Mac OS X](https://github.com/Hobb8s/vier-gewinnt/tree/main/server)  
[👉 Linux](https://github.com/Hobb8s/vier-gewinnt/tree/main/server)  
[🐳 Docker](https://github.com/Hobb8s/vier-gewinnt/tree/main/server)  

## Weiterentwickeln

```sh
git clone https://github.com/Hobb8s/vier-gewinnt.git

cd vier-gewinnt/server/

# Installiert alle nötigen Pakete
yarn install

# führt das Programm aus
yarn runjs

# compiliert das Programm neu und führt das Programm aus
yarn runts

# führt 'yarn runts' mit nodemon aus
yarn serve

# compiliert das Programm neu und baut ausführbare Dateien für Windows, OS X und Linux
yarn build
```