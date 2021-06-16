"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.sucheRaumIdVonWs = exports.raumVerlassen = exports.raumBeitreten = exports.WebSocketRaum = void 0;
class WebSocketRaum {
    constructor(name) {
        this.name = name;
        this._wsl = [];
        this._spieler = [];
        if (Object.keys(WebSocketRaum.räume).findIndex((v) => name == v) != -1)
            throw 'Raum kann nicht erstellt werden';
        WebSocketRaum.räume[name] = this;
    }
    static raum(name) {
        return WebSocketRaum.räume[name];
    }
    get wsl() {
        return this._wsl;
    }
    get spieler() {
        return this._spieler;
    }
    get size() {
        return this._wsl.length;
    }
    beitreten(ws, s) {
        this._wsl.push(ws);
        this._spieler.push(s);
        return this;
    }
    verlassen(ws) {
        this._wsl.splice(this._wsl.findIndex((v) => v == ws), 1);
        this._spieler.splice(this._wsl.findIndex((v) => v == ws), 1);
        if (this.size === 0)
            delete WebSocketRaum.räume[this.name];
        else
            return this;
    }
    senden(data, ws) {
        this._wsl.forEach((socket) => {
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
function sucheRaumIdVonWs(ws) {
    for (const wsr in WebSocketRaum.räume) {
        if (WebSocketRaum.räume[wsr].wsl.findIndex((v) => v == ws) != -1)
            return wsr;
    }
}
exports.sucheRaumIdVonWs = sucheRaumIdVonWs;
//# sourceMappingURL=WebSocket.js.map