import WebSocket from 'ws'
import { Spieler } from './index'

export class WebSocketRaum {
	static räume: { [key: string]: WebSocketRaum } = {}

	static raum(name: string): WebSocketRaum | undefined {
		return WebSocketRaum.räume[name]
	}

	private _wsl: WebSocket[] = []

	get wsl() {
		return this._wsl
	}

	private _spieler: Spieler[] = []

	get spieler() {
		return this._spieler
	}

	constructor(readonly name: string) {
		if (Object.keys(WebSocketRaum.räume).findIndex((v) => name == v) != -1)
			throw 'Raum kann nicht erstellt werden'
		WebSocketRaum.räume[name] = this
	}

	get size() {
		return this._wsl.length
	}

	beitreten(ws: WebSocket, s: Spieler) {
		this._wsl.push(ws)
		this._spieler.push(s)
		return this
	}

	verlassen(ws: WebSocket) {
		this._wsl.splice(
			this._wsl.findIndex((v) => v == ws),
			1
		)

		this._spieler.splice(
			this._wsl.findIndex((v) => v == ws),
			1
		)

		if (this.size === 0) delete WebSocketRaum.räume[this.name]
		else return this
	}

	senden(data: any, ws?: WebSocket) {
		this._wsl.forEach((socket) => {
			if (ws == undefined || ws != socket) socket.send(data)
		})
	}
}

export function raumBeitreten(
	raumId: string,
	ws: WebSocket,
	s: Spieler,
	automatischErstellen = true
) {
	if (
		automatischErstellen &&
		Object.keys(WebSocketRaum.räume).findIndex((v) => raumId == v) == -1
	)
		return new WebSocketRaum(raumId).beitreten(ws, s)
	const raum = WebSocketRaum.raum(raumId)
	if (raum) return raum.beitreten(ws, s)

	throw `Der Raum ${raumId} kann nicht erstellt werden.`
}

export function raumVerlassen(raumId: string, ws: WebSocket) {
	const raum = WebSocketRaum.raum(raumId)

	if (raum) return raum.verlassen(ws)

	throw `Der Raum ${raumId} exsistiert nicht.`
}

export function sucheRaumIdVonWs(ws: WebSocket) {
	for (const wsr in WebSocketRaum.räume) {
		if (WebSocketRaum.räume[wsr].wsl.findIndex((v) => v == ws) != -1)
			return wsr
	}
}
