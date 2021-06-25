package viergewinnt;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import javax.json.Json;
import javax.json.JsonArray;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class Multiplayer implements Initializable {
	@FXML
	public Button multi_zurueck;
	@FXML
	public Button multi_starten;
	@FXML
	public Button multi_verbinden;
	@FXML
	public Button multi_server_herunterladen;
	@FXML
	public TextField multi_namenswahl;
	@FXML
	public TextField multi_internetadresse;
	@FXML
	public TextField multi_raum_ID;
	@FXML
	public TableView<Spieler> multi_tableview;
	@FXML
	public ChoiceBox<String> multi_farbwahl;

	private boolean verbunden = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		VierGewinnt.zuruecksetzen();

		TableColumn<Spieler, String> name = new TableColumn<Spieler, String>("Name");
		name.setMinWidth(200);
		name.setCellValueFactory(new PropertyValueFactory<Spieler, String>("name"));

		TableColumn<Spieler, String> farbe = new TableColumn<Spieler, String>("Farbe");
		farbe.setMinWidth(200);
		farbe.setCellValueFactory(new PropertyValueFactory<Spieler, String>("farbe"));

		multi_tableview.getColumns().addAll(name, farbe);
		multi_tableview.setItems(VierGewinnt.getSpieler());

		multi_farbwahl.getItems().addAll(VierGewinnt.farben);

	}

	/**
	 * Wird aufgerufen, wenn der wenn man auf den Button 'Starten' klickt. ->
	 * Startet das Spiel
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {
		if (verbunden)
			WebSocketClient.client
					.sendMessage(
							Json.createObjectBuilder().add("aktion", "Bereit")
									.add("daten",
											Json.createObjectBuilder().add("raumId", WebSocketClient.client.raumId)
													.add("uuid", VierGewinnt.getSpieler().get(0).getUuid()))
									.build().toString());
		else
			zeigeAlert(Alert.AlertType.ERROR, "Noch nicht verbunden!",
					"Sie sind noch nicht mit einem Server verbunden.");
	}

	/**
	 * Öffnet einen Link, dass man den Viergewinnt-Server herrunterladen kann
	 */
	public void herunterladen() {
		App.app.oeffneWebAdresse("https://github.com/Hobb8s/viergewinnt/tree/main/server#viergewinnt-server");
	}

	/**
	 * Wird aufgerufen, wenn der wenn man auf den Button 'Verbinden' klickt.
	 */
	public void verbinden() {
		try {
			// ---------------------------------------------------------------------------------
			// Verbindet sich mit einem WS-Viergewinnt-Server &
			// Erstellt ein MessageHandlder: Bearbeitet die Nachrichten, die vom Server
			// kommen
			// ---------------------------------------------------------------------------------

			WebSocketClient.client = new WebSocketClient(new URI(multi_internetadresse.getText()));
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
						verbunden = false;
					}

					// ---------------------------------------------------------------------------------
					// wenn ein Spieler dem Raum beigetreten ist
					// ---------------------------------------------------------------------------------

					if (aktion.equals("Spieler ist beigetreten")) {
						try {
							// ---------------------------------------------------------------------------------
							// Liest das Json Schema: Spieler[]
							// ---------------------------------------------------------------------------------
							JsonArray spielerArray = Json.createReader(new StringReader(daten)).readArray();

							Spieler spieler1 = Spieler.ladeJson(spielerArray.getJsonObject(0).toString(), 1);
							Spieler spieler2 = null;
							if (spielerArray.size() > 1)
								spieler2 = Spieler.ladeJson(spielerArray.getJsonObject(1).toString(), 1);

							if (!VierGewinnt.getSpieler().get(0).getUuid().equals(spieler1.getUuid()))
								VierGewinnt.spielerHinzufuegen(spieler1);
							if (spieler2 != null
									&& !VierGewinnt.getSpieler().get(0).getUuid().equals(spieler2.getUuid()))
								VierGewinnt.spielerHinzufuegen(spieler2);

							multi_tableview.setItems(VierGewinnt.getSpieler());
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					// ---------------------------------------------------------------------------------
					// wenn ein Spieler ein Raum verlassen hat
					// ---------------------------------------------------------------------------------

					if (aktion.equals("Spieler hat verlassen")) {
						try {
							// ---------------------------------------------------------------------------------
							// Liest das Json Schema: { uuid: string }
							// ---------------------------------------------------------------------------------
							String uuid = Json.createReader(new StringReader(daten)).readObject().get("uuid")
									.toString();

							// ---------------------------------------------------------------------------------
							// Löscht den Spieler aus der Spielerliste &
							// aktualisiert die Tableview
							// ---------------------------------------------------------------------------------

							VierGewinnt.spielerEntfernen(uuid);

							multi_tableview.setItems(VierGewinnt.getSpieler());
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					// ---------------------------------------------------------------------------------
					// Spiel startet, da alle Spieler bereit sind
					// ---------------------------------------------------------------------------------

					if (aktion.equals("Bereit")) {
						Platform.runLater(() -> {
							try {
								App.setRoot("spiel");
							} catch (IOException e) {
								zeigeAlert(AlertType.ERROR, "Fehler", e.getMessage());
							}
						});
					}
				}
			});

			// ---------------------------------------------------------------------------------
			// Erstellt einen neuen Spieler
			// Sende "trete Raum bei" mit dem erstellten Spieler;
			// Json Schema: { aktion: "trete Raum bei", spieler: Spieler } &
			// Löscht den Spieler aus der Spielerliste &
			// aktualisiert die Tableview
			// ---------------------------------------------------------------------------------

			String farbewahl = multi_farbwahl.getValue();
			Color farbe = null;

			switch (farbewahl) {
				case "Rot":
					farbe = Color.RED;
					break;

				case "Gelb":
					farbe = Color.YELLOW;

				case "Grün":
					farbe = Color.GREEN;
					break;

				case "Blau":
					farbe = Color.BLUE;
					break;

				case "Schwarz":
					farbe = Color.BLACK;
					break;

				case "Türkis":
					farbe = Color.rgb(64, 224, 208);
					break;

				case "Magenta":
					farbe = Color.MAGENTA;
					break;

				default:
					farbe = Color.ORANGE;
					break;

			}
			WebSocketClient.client.raumId = multi_raum_ID.getText();
			Spieler spieler = new Spieler(0, farbe, multi_namenswahl.getText()).setUuid();
			WebSocketClient.client.sendMessage(Json
					.createObjectBuilder().add("aktion", "trete Raum bei").add("daten", Json.createObjectBuilder()
							.add("raumId", WebSocketClient.client.raumId).add("spieler", spieler.toJson()))
					.build().toString());
			VierGewinnt.spielerHinzufuegen(spieler);
			multi_tableview.setItems(VierGewinnt.getSpieler());

			verbunden = true;
			VierGewinnt.muliplayerModus = true;

		} catch (Exception e) {
			zeigeAlert(Alert.AlertType.ERROR, "Fehler", e.getMessage());
		}

	}

	/**
	 * Wird aufgerufen, wenn auf den Button 'Zurück' geklickt wird.
	 */
	public void zurueck() throws IOException {

		if (verbunden)
			WebSocketClient.client
					.sendMessage(
							Json.createObjectBuilder().add("aktion", "verlasse Raum")
									.add("daten",
											Json.createObjectBuilder().add("raumId", multi_raum_ID.getText())
													.add("spieler", VierGewinnt.getSpieler().get(0).toJson()))
									.build().toString());

		App.setRoot("startBildschirm");
	}

	public void zeigeAlert(Alert.AlertType typ, String titel, String nachricht) {
		Alert a = new Alert(typ);
		a.setTitle(titel);
		a.setContentText(nachricht);
		a.showAndWait().ifPresent(rs -> {
		});
	}
}
