"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.raumVerlassen = exports.raumBeitreten = exports.WebSocketRaum = void 0;
class WebSocketRaum {
    constructor(name) {
        this.name = name;
        this.wsl = [];
        this.spieler = [];
        if (Object.keys(WebSocketRaum.räume).findIndex((v) => name == v) != -1)
            throw 'Raum kann nicht erstellt werden';
        WebSocketRaum.räume[name] = this;
    }
    static raum(name) {
        return WebSocketRaum.räume[name];
    }
    get size() {
        return this.wsl.length;
    }
    beitreten(ws, s) {
        this.wsl.push(ws);
        this.spieler.push(s);
        return this;
    }
    verlassen(ws) {
        this.wsl.splice(this.wsl.findIndex((v) => v == ws), 1);
        this.spieler.splice(this.wsl.findIndex((v) => v == ws), 1);
        if (this.size === 0)
            delete WebSocketRaum.räume[this.name];
        else
            return this;
    }
    senden(data, ws) {
        this.wsl.forEach((socket) => {
            if (ws == undefined || ws != socket)
                socket.send(data);
        });
    }
}
exports.WebSocketRaum = WebSocketRaum;
WebSocketRaum.räume = {};
function raumBeitreten(raumId, ws, s, automatischErstellen = true) {
    if (automatischErstellen &&
        Object.keys(WebSocketRaum.räume).findIndex((v) => raumId == v) == -1)
        return new WebSocketRaum(raumId).beitreten(ws, s);
    const raum = WebSocketRaum.raum(raumId);
    if (raum)
        return raum.beitreten(ws, s);
    throw `Der Raum ${raumId} kann nicht erstellt werden.`;
}
exports.raumBeitreten = raumBeitreten;
function raumVerlassen(raumId, ws) {
    const raum = WebSocketRaum.raum(raumId);
    if (raum)
        return raum.verlassen(ws);
    throw `Der Raum ${raumId} exsistiert nicht.`;
}
exports.raumVerlassen = raumVerlassen;
//# sourceMappingURL=WebSocket.js.map