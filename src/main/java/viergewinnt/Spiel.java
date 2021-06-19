package viergewinnt;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.json.Json;
import javax.json.JsonObject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert.AlertType;

public class Spiel implements Initializable {

	@FXML
	public Button spielfeld_0_0;
	@FXML
	public Button spielfeld_0_1;
	@FXML
	public Button spielfeld_0_2;
	@FXML
	public Button spielfeld_0_3;
	@FXML
	public Button spielfeld_0_4;
	@FXML
	public Button spielfeld_0_5;
	@FXML
	public Button spielfeld_0_6;
	@FXML
	public Button spielfeld_1_0;
	@FXML
	public Button spielfeld_1_1;
	@FXML
	public Button spielfeld_1_2;
	@FXML
	public Button spielfeld_1_3;
	@FXML
	public Button spielfeld_1_4;
	@FXML
	public Button spielfeld_1_5;
	@FXML
	public Button spielfeld_1_6;
	@FXML
	public Button spielfeld_2_0;
	@FXML
	public Button spielfeld_2_1;
	@FXML
	public Button spielfeld_2_2;
	@FXML
	public Button spielfeld_2_3;
	@FXML
	public Button spielfeld_2_4;
	@FXML
	public Button spielfeld_2_5;
	@FXML
	public Button spielfeld_2_6;
	@FXML
	public Button spielfeld_3_0;
	@FXML
	public Button spielfeld_3_1;
	@FXML
	public Button spielfeld_3_2;
	@FXML
	public Button spielfeld_3_3;
	@FXML
	public Button spielfeld_3_4;
	@FXML
	public Button spielfeld_3_5;
	@FXML
	public Button spielfeld_3_6;
	@FXML
	public Button spielfeld_4_0;
	@FXML
	public Button spielfeld_4_1;
	@FXML
	public Button spielfeld_4_2;
	@FXML
	public Button spielfeld_4_3;
	@FXML
	public Button spielfeld_4_4;
	@FXML
	public Button spielfeld_4_5;
	@FXML
	public Button spielfeld_4_6;
	@FXML
	public Button spielfeld_5_0;
	@FXML
	public Button spielfeld_5_1;
	@FXML
	public Button spielfeld_5_2;
	@FXML
	public Button spielfeld_5_3;
	@FXML
	public Button spielfeld_5_4;
	@FXML
	public Button spielfeld_5_5;
	@FXML
	public Button spielfeld_5_6;
	@FXML
	public Button spielfeld_verlassen;
	@FXML
	public Button spielfeld_pause;
	@FXML
	public ProgressBar spielfeld_progressbar;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		pause();

		if (!VierGewinnt.muliplayerModus)
			return;

		// ---------------------------------------------------------------------------------
		// Neue NachrichtenBearbeitung wird für das Spiel erstellet mit anderen Events
		// ---------------------------------------------------------------------------------
		WebSocketClient.client.addMessageHandler(new WebSocketClient.NachrichtenBearbeitung() {

			@Override
			public void bearbeiteNachricht(String message) {

				// ---------------------------------------------------------------------------------
				// Liest das Json Schema { aktion: string, daten: any }
				// ---------------------------------------------------------------------------------

				String aktion = Json.createReader(new StringReader(message)).readObject().get("aktion").toString();
				String daten = Json.createReader(new StringReader(message)).readObject().get("daten").toString();

				// ---------------------------------------------------------------------------------
				// Fehler wird angezeigt, wenn der Server einen Fehler meldet
				// ---------------------------------------------------------------------------------

				if (aktion.equals("error")) {
					String err = Json.createReader(new StringReader(daten)).readObject().get("info").toString();

					Platform.runLater(() -> zeigeAlert(Alert.AlertType.ERROR, "Server Fehler", err));
				}

				// ---------------------------------------------------------------------------------
				// wenn ein Spieler ein Raum verlassen hat
				// ---------------------------------------------------------------------------------

				if (aktion.equals("Spieler hat verlassen")) {
					try {
						// ---------------------------------------------------------------------------------
						// Liest das Json Schema: { uuid: string }
						// ---------------------------------------------------------------------------------
						String uuid = Json.createReader(new StringReader(daten)).readObject().get("uuid").toString();

						// ---------------------------------------------------------------------------------
						// Löscht den Spieler aus der Spielerliste &
						// aktualisiert die Tableview
						// ---------------------------------------------------------------------------------

						VierGewinnt.spielerEntfernen(uuid);

						Platform.runLater(() -> {
							try {
								App.setRoot("multiplayer");
							} catch (IOException e) {
								zeigeAlert(Alert.AlertType.ERROR, "Fehler", e.getMessage());
							}

						});
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				// ---------------------------------------------------------------------------------
				// wenn ein Spieler ein Raum verlassen hat
				// ---------------------------------------------------------------------------------

				if (aktion.equals("Spieler wechseln")) {
					try {
						// ---------------------------------------------------------------------------------
						// Liest das Json Schema: { grund: string, pos?: { x: number, y: number } }
						// ---------------------------------------------------------------------------------
						JsonObject jo = Json.createReader(new StringReader(daten)).readObject();

						// ---------------------------------------------------------------------------------
						// Wenn beim Gegener die Zeit abgelaufen ist, dann wird der Spieler gewechselt.
						// ---------------------------------------------------------------------------------

						if (jo.get("grund").toString().equals("Zeit abgelaufen")) {
							spielerWechseln();
							return;
						}

						// ---------------------------------------------------------------------------------
						// Zug des Gegeners wird auch hier vollzugen &
						// Es wird überprüft, ob der Gegener gewonnen hat
						// ---------------------------------------------------------------------------------

						JsonObject pos = Json.createReader(new StringReader(jo.get("pos").toString())).readObject();

						int x = Integer.parseInt(pos.get("x").toString());
						int y = Integer.parseInt(pos.get("y").toString());

						Platform.runLater(() -> {
							steinSetzen(x, y);
							
							if (!überprüfeHatGewonnen(x, y))
								spielerWechseln();
						});

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}
		});
	}

	// Bestätigen des Abbrechens des Spiels und Zurückgelangen zur
	// startBildschirm.fxml und währenddessen Anhalten des Timers,
	// der bei Verlassen des Spiels den Text des Pause-Buttons für das nächste Spiel
	// auf Start setzt.
	public void verlassen() throws IOException {
		PauseZahl = 2;
		Alert bestätigung = new Alert(Alert.AlertType.CONFIRMATION);
		bestätigung.setTitle("Verlassen des Spiels");
		bestätigung.setContentText("Wenn Sie fortfahren, verlassen Sie das Spiel und brechen es somit ab.");

		Optional<ButtonType> result = bestätigung.showAndWait();
		if (result.get() == ButtonType.OK) {
			App.setRoot("startBildschirm");
			PauseZahl = 0;
		} else {
			bestätigung.close();
			PauseZahl = 1;
		}
	}

	// verbleibendeZeit = Zeit, die dem aktiven Spieler verbleibt, um den Spielzug
	// durchzuführen
	// PauseZahl = Erklärung folgt bei der Methode pause() ;)
	// zeit = Zeit, die ein Spieler zum Ausführen eines Zuges besitzt
	// Timer = Timer, der die aktuelle, noch zum Ausführen des Zuges übrige, Zeit
	// bestimmt
	private int zeit = 20;
	private int verbleibendeZeit = 20;
	private int PauseZahl = 0;
	private Timer timer = new Timer();

	// Erstellt eine ProgressBar, die die noch verbleibende Zeit visuell zeigt und
	// sich jede Sekunde aktualisiert
	public void rueckwaertsProgressBar() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (verbleibendeZeit > 0) {
					if (PauseZahl == 1) {
						verbleibendeZeit -= 1;
						double zeitFürProgessbar = (double) verbleibendeZeit / (double) zeit;
						spielfeld_progressbar.setProgress(zeitFürProgessbar);

					}
				} else {
					verbleibendeZeit = zeit;
					timer.cancel();
					Platform.runLater(() -> ZeitAlert());
				}
			}
		};
		timer.scheduleAtFixedRate(task, 1000, 1000);
	}

	// Informationsfenster für die abgelaufene Zeit
	public void ZeitAlert() {
		Alert zeitVorbei = new Alert(AlertType.INFORMATION);
		zeitVorbei.setTitle("Ablauf der Zeit");
		zeitVorbei.setContentText("Leider waren Sie zu langsam und die Zeit ist abgelaufen.");
		zeitVorbei.showAndWait().ifPresent(rs -> {
			if (rs == ButtonType.OK) {
				try {
					App.setRoot("startBildschirm");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
	}

	// Pausezahl: Zahl, die den aktuellen Stand ausgibt:
	// 0 & 2 = Zeit steht bzw. läuft nicht (Unterschied: Text -> Start bei 0 und
	// Weiter bei 2)
	// 1 = Zeit läuft
	public void pause() {
		if (PauseZahl == 0) {
			rueckwaertsProgressBar();
			PauseZahl = 1;
			spielfeld_pause.setText("Pause");
		} else {
			if (PauseZahl == 1) {
				PauseZahl = 2;
				spielfeld_pause.setText("Weiter");
			} else {
				PauseZahl = 1;
				spielfeld_pause.setText("Pause");
			}
		}
	}

	public void spielfeldClicked(ActionEvent event) {

		// !---------------------------------------------------------------------------------
		// Wenn der Multiplayer Modus aktiviert ist und der andere Spieler gerade einen
		// Zug macht, wird die Methode abgebrochen.
		// !---------------------------------------------------------------------------------
		if (VierGewinnt.muliplayerModus && VierGewinnt.getAktivenSpieler().id != 0) {
			zeigeAlert(Alert.AlertType.INFORMATION, "Du bist nicht dran!",
					"Gerade macht der andere Spieler einen Zug...");
			return;
		}

		// !---------------------------------------------------------------------------------
		// Event wird ganz nomal ausgeführt
		// !---------------------------------------------------------------------------------

		// Speichere die Id von dem Button, der das Event getriggert hat
		String id = ((Node) event.getSource()).getId();

		// Zerlege die Id um an X und Y Position des Buttons zu kommen: "spielfeld_0_1"
		// -> ["spielfeld", "0", "1"] -> [, y, x]
		String[] ids = id.split("_");

		try {
			// Stein wird in der Spalte gesetz, in der der Spieler einen Button geklickt hat
			VierGewinnt.reiheSetzen(Integer.parseInt(ids[2]), VierGewinnt.getAktivenSpieler());

			// Die Zeit beginnt weiter zu laufen und der Text des Buttons ändert sich in
			// Paus
			PauseZahl = 1;
			spielfeld_pause.setText("Pause");

			int x = Integer.parseInt(ids[2]);
			int y = VierGewinnt.hoeheVonReihe(x) - 1;

			Button b = (Button) getClass().getDeclaredField("spielfeld_" + (y - 5) * -1 + "_" + x).get(this);
			b.setStyle("-fx-background-color: " + VierGewinnt.getAktivenSpieler().farbe + ";");

			// ---------------------------------------------------------------------------------
			// Dem Server wird mitgeteilt, dass ein Spieler einen Zug vollendet hat.
			// Json Schema: { aktion: string, daten: { pos: { x: number, y: number },
			// raumId: string } }
			// ---------------------------------------------------------------------------------

			if (VierGewinnt.muliplayerModus) {
				WebSocketClient.client.sendMessage(Json.createObjectBuilder().add("aktion", "Runde abgeschlossen")
						.add("daten",
								Json.createObjectBuilder()
										.add("pos", Json.createObjectBuilder().add("x", x).add("y", y))
										.add("raumId", WebSocketClient.client.raumId))
						.build().toString());
			}

			// ---------------------------------------------------------------------------------
			// Es wird überprüft ob ein Spieler gewonnen hat
			// ---------------------------------------------------------------------------------

			if (überprüfeHatGewonnen(x, y))
				return;

			// Wenn keiner gewonnen hat wird der aktive Spieler geändert / gewchselt und die
			// nächste Runde beginnt.
			spielerWechseln();

		} catch (Exception e) {
			e.printStackTrace();
			// Wenn ein Fehler auftritt, wird der Fehler mit einem Altert angezeigt
			zeigeAlert(Alert.AlertType.ERROR, "Fehler", e.getMessage());
		}

	}

	private boolean überprüfeHatGewonnen(int x, int y) {
		Spieler hatGewonnen = VierGewinnt.hatGewonnen(x, y);

		if (hatGewonnen == null)
			return false;

		// Der Timer wird gestoppt, der Text des Pause-Button für das nächste Spiel auf
		// Start gesetzt und die
		// verbleibende Zeit für das Ausführen des Spiellzugs (im nächsten Spiel) wird
		// zurückgesetzt.
		timer.cancel();
		PauseZahl = 0;
		verbleibendeZeit = zeit;

		// Es wird angezeigt wer gewonnen hat
		zeigeAlert(Alert.AlertType.INFORMATION, "Gewonnen", "Spieler " + hatGewonnen.name + " hat gewonnen.");

		try {
			App.setRoot("startBildschirm");
		} catch (IOException e) {

			// Wenn ein Fehler auftritt, wird der Fehler mit einem Altert angezeigt
			zeigeAlert(Alert.AlertType.ERROR, "Fehler", e.getMessage());
		}

		// Da jemand das Spiel gewonnen hat, wird das laufende Spiel abgebrochen
		return true;

	}

	private void steinSetzen(int x, int y) {
		try {
			VierGewinnt.reiheSetzen(x, VierGewinnt.getAktivenSpieler());

			Button b = (Button) getClass().getDeclaredField("spielfeld_" + ((y - 5) * -1) + "_" + x).get(this);
			b.setStyle("-fx-background-color: " + VierGewinnt.getAktivenSpieler().farbe + ";");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void spielerWechseln() {
		verbleibendeZeit = zeit;
		VierGewinnt.spielerWechseln();
	}

	public void zeigeAlert(Alert.AlertType typ, String titel, String nachricht) {
		Alert a = new Alert(typ);
		a.setTitle(titel);
		a.setContentText(nachricht);
		a.showAndWait().ifPresent(rs -> {
		});
	}
}
