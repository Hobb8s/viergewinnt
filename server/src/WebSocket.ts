import WebSocket from 'ws'
import { Spieler } from '.'

export class WebSocketRaum {
    static räume: { [key: string]: WebSocketRaum } = {}

    static raum(name: string): WebSocketRaum | undefined {
        return WebSocketRaum.räume[name]
    }

    private wsl: WebSocket[] = []
    public spieler: Spieler[] = []
    constructor(readonly name: string) {
        if (Object.keys(WebSocketRaum.räume).findIndex((v) => name == v) != -1)
            throw 'Raum kann nicht erstellt werden'
        WebSocketRaum.räume[name] = this
    }

    get size() {
        return this.wsl.length
    }

    beitreten(ws: WebSocket, s: Spieler) {
        this.wsl.push(ws)
        this.spieler.push(s)
        return this
    }

    verlassen(ws: WebSocket) {
        this.wsl.splice(
            this.wsl.findIndex((v) => v == ws),
            1
        )

        this.spieler.splice(
            this.wsl.findIndex((v) => v == ws),
            1
        )

        if (this.size === 0) delete WebSocketRaum.räume[this.name]
        else return this
    }

    senden(data: any, ws?: WebSocket) {
        this.wsl.forEach((socket) => {
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
