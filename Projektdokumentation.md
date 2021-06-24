# Projektdokumentation

- [Projektdokumentation](#projektdokumentation)
  - [verschiedene Spielmodi](#verschiedene-spielmodi)
  - [Interaktion zwischen Server und Client](#interaktion-zwischen-server-und-client)
  - [Projektphase](#Projektphase)
  - [Zeitlicher Verlauf](#Zeitlicher-Verlauf)
  - [Organisationsplan](#Organisationsplan)


##  verschiedene Spielmodi

Es gibt einmal einen Single-Modus, in dem man zu zweit auf einem Endgrät spielen kann. 
Der zweite Modus ist der Multiplayer, bei dem man über das Internet gegeneinander spielen kann.


##  Interaktion zwischen Server und Client

Websockets ist eine typische Web-Technologie

Alle Clients kommunizieren mit dem Server mithilfe des Websocket-Protokoll (ws://...:...). Zum Verbinden wird außerdem eine RaumID benötigt.  
Der Server kann unbegrenzt viele Verbindungen mit Client aufbauen. Alle Verbindugen werden in Räumen organisiert mit einer eindeutigen RoomID.

![Räume](https://socket.io/images/rooms.png)

Wenn der Server ein Event von einem Client empfängt, kann der Server eine Antwort zu allen Raummitgliedern senden.


##  Projektphase

Da auf spiel.fxml alles aufbaut, haben wir diese Datei zuerst erstellt. Anschließend wurden die Spiellogik und das restliche Design zeitgleich erstellt. 
Die nächsten Phasen bestanden aus testen, ausbessern und erweitern des Codes. Als alles einigermaßen funktionierte, haben wir das Programm mit Familien und Freunden ausprobiert und entwaige Fehler beseitigt.
Zur selben Zeit wurden die Dokumentationen, Präsentation und Diagramme erstellt.


##  Zeitlicher Verlauf
siehe Projekttagebuch???
Link einfügen?
Feeeeellllllixxxxxxx??????

##  Organisationsplan

Das Projekt wurde in zwei Bereiche aufgeteilt, Robin und Moritz waren Designer und Felix und Paul haben die Spiellogik programmiert.