# Projektdokumentation

- [Projektdokumentation](#projektdokumentation)
  - [Interaktion zwischen Server und Client](#interaktion-zwischen-server-und-client)

## Interaktion zwischen Server und Client

Websockets ist eine typische Web-Technologie

Alle Clients kommunizieren mit dem Server mithilfe des Websocket-Protokoll (ws://...:...). Zum Verbinden wird außerdem eine RaumID benötigt.  
Der Server kann unbegrenzt viele Verbindungen mit Client aufbauen. Alle Verbindugen werden in Räumen organisiert mit einer eindeutigen RoomID.

![Räume](https://socket.io/images/rooms.png)

Wenn der Server ein Event von einem Client empfängt, kann der Server eine Antwort zu allen Raummitgliedern senden.
