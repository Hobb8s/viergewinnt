"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const inquirer_1 = require("inquirer");
const ora_1 = tslib_1.__importDefault(require("ora"));
const chalk_1 = tslib_1.__importDefault(require("chalk"));
const dns_1 = require("dns");
const os_1 = require("os");
const clipboard = tslib_1.__importStar(require("copy-paste"));
const http_1 = require("http");
const ws_1 = require("ws");
// Main Methode
const WebSocket_1 = require("./WebSocket");
(async function main() {
    console.log(`
${chalk_1.default.bold.bgBlue('Willkommen bei der Installation des Vier Gewinnt Servers')}

`);
    // Konfiguration der Servers
    // Frage nach Port und ob auf dem Server unendlich viele Räume erstellt werden dürfen
    const { ErstelleAutomatischRäume, Port, } = await inquirer_1.prompt([
        {
            type: 'number',
            name: 'Port',
            message: 'Welchen Port soll der Server benutzen?',
            default: 3000,
        },
        {
            type: 'confirm',
            name: 'ErstelleAutomatischRäume',
            message: 'Sollen Räume automatisch erstellt werden, wenn sie nicht vorhanden sind?',
            default: true,
        },
    ]);
    const räume = [];
    if (!ErstelleAutomatischRäume) {
        // Festlegen der Raumanzahl
        const { AnzahlDerRäume } = (await inquirer_1.prompt([
            {
                type: 'number',
                name: 'AnzahlDerRäume',
                message: 'Wie viele Räume sollen ertstellt werden?',
            },
        ]));
        // Festlegen der Raumnamen
        const fragen = Array(AnzahlDerRäume)
            .fill({})
            .map((_, index) => ({
            type: 'input',
            name: `Raum_${index}`,
            message: `Geben sie den Namen des ${index + 1}. Raums ein:`,
        }));
        const e = await inquirer_1.prompt(fragen);
        for (const raum in e)
            räume.push(e[raum]);
    }
    // Prüfung der Internetverbindung
    const spinnerInternetverbindung = ora_1.default('Prüfe Internetverbindung').start();
    if (!(await prüfeInternetverbindung())) {
        spinnerInternetverbindung.fail();
        return;
    }
    spinnerInternetverbindung.succeed();
    // Initialisiere Websocket Server
    const spinnerStarteSocketIO = ora_1.default('Starte Websocket').start();
    const server = new http_1.Server();
    const wss = new ws_1.Server({ server });
    wss.on('connection', (ws) => {
        ws.on('message', (nachricht) => {
            console.log(JSON.parse(nachricht));
            // ---------------------------------------------------------------------------------
            // trete Raum bei
            // ---------------------------------------------------------------------------------
            if (JSON.parse(nachricht).aktion === 'trete Raum bei') {
                const { raumId, spieler } = JSON.parse(nachricht).daten;
                try {
                    const raum = WebSocket_1.raumBeitreten(raumId, ws, spieler, ErstelleAutomatischRäume);
                    if (raum.size > 2) {
                        WebSocket_1.raumVerlassen(raumId, ws);
                        return ws.send(JSON.stringify({
                            aktion: 'error',
                            daten: {
                                info: 'Es dürfen sich nicht mehr als 2 Clients mit einem Raum verbinden.',
                            },
                        }));
                    }
                    raum.senden(JSON.stringify({
                        aktion: 'Spieler ist beigetreten',
                        daten: raum.spieler,
                    }));
                }
                catch (e) {
                    ws.send(JSON.stringify({
                        aktion: 'error',
                        daten: { info: e },
                    }));
                }
            }
            // ---------------------------------------------------------------------------------
            // verlasse Raum
            // ---------------------------------------------------------------------------------
            if (JSON.parse(nachricht).aktion === 'verlasse Raum') {
                const { raumId, spieler: { uuid }, } = JSON.parse(nachricht).daten;
                try {
                    WebSocket_1.raumVerlassen(raumId, ws)?.senden(JSON.stringify({
                        aktion: 'Spieler hat verlassen',
                        daten: { uuid },
                    }));
                }
                catch (e) {
                    ws.send(JSON.stringify({
                        aktion: 'error',
                        daten: { info: e },
                    }));
                }
            }
            // ---------------------------------------------------------------------------------
            // verlasse Raum
            // ---------------------------------------------------------------------------------
            // if (
            //     (JSON.parse(nachricht) as Nachricht).aktion === 'verlasse Raum'
            // ) {
            //     const { raumId, spieler } = (
            //         JSON.parse(
            //             nachricht
            //         ) as NachrichtMitDaten<VerbindungAnfrage>
            //     ).daten
            //     try {
            //         raumVerlassen(raumId, ws)?.senden(
            //             JSON.stringify({
            //                 aktion: 'Spieler hat verlassen',
            //                 daten: { spieler },
            //             } as NachrichtMitDaten<any>)
            //         )
            //     } catch (e) {
            //         ws.send(
            //             JSON.stringify({
            //                 aktion: 'error',
            //                 daten: { info: e },
            //             } as NachrichtMitDaten<Error>)
            //         )
            //     }
            // }
        });
        ws.on('close', (c, r) => {
            const raumId = WebSocket_1.sucheRaumIdVonWs(ws);
            if (raumId)
                WebSocket_1.raumVerlassen(raumId, ws);
        });
    });
    server.listen(Number(Port), async () => {
        spinnerStarteSocketIO.succeed(`Server läuft unter Port ${Port}`);
        const addresse = `ws://${await findeEigeneIpAdresse()}:${Port}`;
        clipboard.copy(addresse);
        console.log(`
${chalk_1.default.black.bgCyanBright('Local:   ')} ws://127.0.0.1:${Port}

${chalk_1.default.black.bgGreen('Netzwerk:')} ${addresse} ${chalk_1.default.gray('Diese Adresse wurde in die Zwischenablage kopiert.')}

${chalk_1.default.black.bgYellow('Achtung: ')} Wenn Sie mit jemandem außerhalb ihres Heimnetzwerkes spielen wollen,
		  müssen Sie eventuell den Port ${Port} für dieses Gerät freigeben.

${chalk_1.default.bgRed('Wenn Sie den Server beenden wollen, dücken sie Strg+C.')}
`);
    });
})();
// Zusätzliche Funktionen
async function prüfeInternetverbindung() {
    return new Promise((resolve) => {
        dns_1.lookup('google.com', (err) => {
            if (err && err.code === 'ENOTFOUND')
                resolve(false);
            else
                resolve(true);
        });
    });
}
async function findeEigeneIpAdresse() {
    return new Promise((resolve) => {
        dns_1.lookup(os_1.hostname(), (err, add, fam) => {
            resolve(add);
        });
    });
}
//# sourceMappingURL=index.js.map