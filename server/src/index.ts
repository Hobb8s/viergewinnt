import { prompt, QuestionCollection, Answers } from 'inquirer'
import ora from 'ora'
import chalk from 'chalk'
import { lookup } from 'dns'
import { hostname } from 'os'
import * as clipboard from 'copy-paste'
import { Server as httpServer } from 'http'
import WebSocket, { Server } from 'ws'
import * as dotenv from 'dotenv'

// Main Methode
import {
	raumBeitreten,
	raumVerlassen,
	sucheRaumIdVonWs,
	WebSocketRaum,
} from './WebSocket'
	; (async function main() {

		dotenv.config()

		console.log(`
${chalk.bold.bgBlue('Willkommen bei der Installation des Vier Gewinnt Servers')}

`)
		const { Port, ErstelleAutomatischRäume } = await konfiguration().catch(() => process.exit())

		// Prüfung der Internetverbindung

		const spinnerInternetverbindung = ora('Prüfe Internetverbindung').start()
		if (!(await prüfeInternetverbindung())) {
			spinnerInternetverbindung.fail()
			return
		}
		spinnerInternetverbindung.succeed()

		// Initialisiere Websocket Server
		const spinnerStarteSocketIO = ora('Starte Websocket').start()
		const server = new httpServer()
		const wss = new Server({ server })

		wss.on('connection', (ws: WebSocket) => {
			ws.on('message', (nachricht: string) => {
				console.log(JSON.parse(nachricht))

				// ---------------------------------------------------------------------------------
				// trete Raum bei
				// ---------------------------------------------------------------------------------

				if (
					(JSON.parse(nachricht) as Nachricht).aktion === 'trete Raum bei'
				) {
					const { raumId, spieler } = (
						JSON.parse(
							nachricht
						) as NachrichtMitDaten<VerbindungAnfrage>
					).daten

					try {
						const raum = raumBeitreten(
							raumId,
							ws,
							spieler,
							ErstelleAutomatischRäume
						)
						if (raum.size > 2) {
							raumVerlassen(raumId, ws)
							return ws.send(
								JSON.stringify({
									aktion: 'error',
									daten: {
										info: 'Es dürfen sich nicht mehr als 2 Clients mit einem Raum verbinden.',
									},
								} as NachrichtMitDaten<Error>)
							)
						}

						raum.senden(
							JSON.stringify({
								aktion: 'Spieler ist beigetreten',
								daten: raum.spieler,
							} as NachrichtMitDaten<any>)
						)
					} catch (e) {
						ws.send(
							JSON.stringify({
								aktion: 'error',
								daten: { info: e },
							} as NachrichtMitDaten<Error>)
						)
					}
				}

				// ---------------------------------------------------------------------------------
				// verlasse Raum
				// ---------------------------------------------------------------------------------

				if (
					(JSON.parse(nachricht) as Nachricht).aktion === 'verlasse Raum'
				) {
					const {
						raumId,
						spieler: { uuid },
					} = (
						JSON.parse(
							nachricht
						) as NachrichtMitDaten<VerbindungAnfrage>
					).daten

					try {
						raumVerlassen(raumId, ws)?.senden(
							JSON.stringify({
								aktion: 'Spieler hat verlassen',
								daten: { uuid },
							} as NachrichtMitDaten<{ uuid: string }>)
						)
					} catch (e) {
						ws.send(
							JSON.stringify({
								aktion: 'error',
								daten: { info: e },
							} as NachrichtMitDaten<Error>)
						)
					}
				}

				// ---------------------------------------------------------------------------------
				// Spieler wird bereit;
				// Wenn beide breit sind, wird das Spiel gestartet
				// ---------------------------------------------------------------------------------

				if (
					(JSON.parse(nachricht) as Nachricht).aktion === 'Bereit'
				) {
					const { raumId, uuid } = (
						JSON.parse(
							nachricht
						) as NachrichtMitDaten<{ raumId: string, uuid: string }>
					).daten

					const raum = WebSocketRaum.räume[raumId]

					if (raum.spieler.filter(spieler => spieler.ready).length > 0)
						raum.senden(JSON.stringify({ aktion: 'Bereit', daten: {} } as NachrichtMitDaten<{}>))
					else raum.spieler.filter(spieler => spieler.uuid == uuid)[0].ready = true
				}

				// ---------------------------------------------------------------------------------
				// Runde von einem Spieler vollendet
				// An den anderen Client wird die Veränderugn geschickt
				// ---------------------------------------------------------------------------------

				if (
					(JSON.parse(nachricht) as Nachricht).aktion === 'Runde abgeschlossen'
				) {
					const { raumId, pos } = (
						JSON.parse(
							nachricht
						) as NachrichtMitDaten<{ raumId: string, pos: { x: number, y: number } }>
					).daten

					const raum = WebSocketRaum.räume[raumId]

					raum.senden(JSON.stringify({ aktion: 'Spieler wechseln', daten: { grund: 'Runde abgeschlossen', pos } } as NachrichtMitDaten<Spielupdate>), ws);
				}
			})

			ws.on('close', (c, r) => {
				const raumId = sucheRaumIdVonWs(ws)
				if (raumId) raumVerlassen(raumId, ws)
			})
		})

		server.listen(Number(Port), async () => {
			spinnerStarteSocketIO.succeed(`Server läuft unter Port ${Port}`)

			const addresse = `ws://${await findeEigeneIpAdresse()}:${Port}`

			clipboard.copy(addresse)

			console.log(
				`
${chalk.black.bgCyanBright('Local:   ')} ws://127.0.0.1:${Port}

${chalk.black.bgGreen('Netzwerk:')} ${addresse} ${chalk.gray(
					'Diese Adresse wurde in die Zwischenablage kopiert.'
				)}

${chalk.black.bgYellow(
					'Achtung: '
				)} Wenn Sie mit jemandem außerhalb ihres Heimnetzwerkes spielen wollen,
		  müssen Sie eventuell den Port ${Port} für dieses Gerät freigeben.

${chalk.bgRed('Wenn Sie den Server beenden wollen, dücken sie Strg+C.')}
`
			)
		})
	})()

// Zusätzliche Funktionen


async function konfiguration(): Promise<{ ErstelleAutomatischRäume: boolean; Port: number }> {

	if (process.env.EXETYPE && ['DOCKER', 'AUTO', 'AUTOMATISCH'].includes(process.env.EXETYPE)) {

		console.log(`
${chalk.bold('Konfiguration: Automatisch / Docker')}
`)

		const spinnerDocker = ora('Lade automatische / Docker Konfiguration').start()
		if (!process.env.ERSTELLE_AUTOMATISCH_RAEUME) {
			spinnerDocker.fail('ERSTELLE_AUTOMATISCH_RAEUME wurde nicht als Umgebungsvariable gesetz!')
			throw ''
		}
		if (!process.env.PORT) {
			spinnerDocker.fail('PORT wurde nicht als Umgebungsvariable gesetz!')
			throw ''
		}
		spinnerDocker.succeed()
		return { ErstelleAutomatischRäume: ['true', 'wahr', 'yes', 'ja'].includes(process.env.ERSTELLE_AUTOMATISCH_RAEUME.toLowerCase()), Port: parseInt(process.env.PORT) }
	}

	console.log(`
${chalk.bold('Konfiguration: Manuell')}
	`)

	// Manuelle Konfiguration der Servers

	// Frage nach Port und ob auf dem Server unendlich viele Räume erstellt werden dürfen
	const {
		ErstelleAutomatischRäume,
		Port,
	}: { ErstelleAutomatischRäume: boolean; Port: number } = await prompt([
		{
			type: 'number',
			name: 'Port',
			message: 'Welchen Port soll der Server benutzen?',
			default: 3000,
		},
		{
			type: 'confirm',
			name: 'ErstelleAutomatischRäume',
			message:
				'Sollen Räume automatisch erstellt werden, wenn sie nicht vorhanden sind?',
			default: true,
		},
	])

	const räume: string[] = []

	if (!ErstelleAutomatischRäume) {
		// Festlegen der Raumanzahl
		const { AnzahlDerRäume } = (await prompt([
			{
				type: 'number',
				name: 'AnzahlDerRäume',
				message: 'Wie viele Räume sollen ertstellt werden?',
			},
		])) as { AnzahlDerRäume: number }

		// Festlegen der Raumnamen
		const fragen: QuestionCollection = Array<Answers>(AnzahlDerRäume)
			.fill({})
			.map((_, index) => ({
				type: 'input',
				name: `Raum_${index}`,
				message: `Geben sie den Namen des ${index + 1}. Raums ein:`,
			})) as Array<Answers>

		const e = await prompt(fragen)

		for (const raum in e) räume.push(e[raum])
	}

	return { ErstelleAutomatischRäume, Port }
}

async function prüfeInternetverbindung() {
	return new Promise((resolve) => {
		lookup('google.com', (err) => {
			if (err && err.code === 'ENOTFOUND') resolve(false)
			else resolve(true)
		})
	})
}

async function findeEigeneIpAdresse() {
	return new Promise((resolve) => {
		lookup(hostname(), (err, add, fam) => {
			resolve(add)
		})
	})
}

// Interfaces für Websockets

export interface Nachricht {
	aktion: string
}

export interface NachrichtMitDaten<T> extends Nachricht {
	daten: T
}

export interface Error {
	info: string
}

export interface Spieler {
	id: number
	name: string
	farbe: string
	uuid: string
	ready?: boolean
}

export interface VerbindungAnfrage {
	raumId: string
	spieler: Spieler
}

export interface Spielupdate {
	grund: 'Zeit abgelaufen' | 'Runde abgeschlossen'
	pos?: { x: number, y: number }
}
