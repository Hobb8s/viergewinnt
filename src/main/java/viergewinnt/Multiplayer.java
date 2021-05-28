package viergewinnt;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.json.Json;
import com.google.gson.Gson;

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
    public ListView<String> multi_liste;
    @FXML
    public ColorPicker multi_farbwahl;

    private boolean verbunden = false;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        VierGewinnt.zuruecksetzen();
    }

    public void zurueck() throws IOException {
        App.setRoot("startBildschirm");
    }

    public void start() throws IOException {
        App.setRoot("spiel");
    }

    public void herunterladen () {
        App.app.oeffneWebAdresse("https://github.com/Hobb8s/viergewinnt");
    }

    public void verbinden() {
        try {
            Spieler spieler = VierGewinnt.spielerHinzufuegen(new Spieler(0, multi_farbwahl.getValue(), multi_namenswahl.getText()));
            // Verbinden
            WebSocketClient viergewinntClient = new WebSocketClient(new URI(multi_internetadresse.getText()));
            viergewinntClient.addMessageHandler(new WebSocketClient.NachrichtenBearbeitung() {
                @Override
                public void bearbeiteNachricht(String message) {
                    System.out.println(message);
                    String aktion = javax.json.Json.createReader(new StringReader(message)).readObject().get("aktion").toString();

                    // Fehler wird angezeigt, wenn der Server einen Fehler meldet
                    if (aktion.equals("error")) {
                        String err = javax.json.Json.createReader(
                                new StringReader(
                                        javax.json.Json.createReader(
                                                new StringReader(message)
                                        ).readObject()
                                                .get("daten")
                                                .toString()
                                )
                        ).readObject()
                                .get("info")
                                .toString();

                        System.out.println(err);
                        Platform.runLater(() ->
                                zeigeAlert(Alert.AlertType.ERROR, "Server Fehler", err)
                        );
                    }

                    if (aktion.equals("error")) {
                        String err = javax.json.Json.createReader(
                                new StringReader(
                                        javax.json.Json.createReader(
                                                new StringReader(message)
                                        ).readObject()
                                                .get("daten")
                                                .toString()
                                )
                        ).readObject()
                                .get("info")
                                .toString();

                        System.out.println(err);
                        Platform.runLater(() ->
                                zeigeAlert(Alert.AlertType.ERROR, "Server Fehler", err)
                        );
                    }
                }
            });
            // Sende "trete Raum bei"
            viergewinntClient.sendMessage(
                    Json.createObjectBuilder()
                            .add("aktion", "trete Raum bei")
                            .add("daten", Json.createObjectBuilder()
                                    .add("raumId", multi_raum_ID.getText())
                                    .add("spieler", new Gson().toJson(spieler))
                            ).build().toString()
            );

            // Lokalen Spieler Erstellen
            String[] s = {spieler.toString()};
            multi_liste.getItems().addAll(s);

            // Lokalen Spieler an den Server schicken
        } catch (Exception e) {
            zeigeAlert(Alert.AlertType.ERROR, "Fehler", e.getMessage());
        }

    }

    public void zeigeAlert(Alert.AlertType typ, String titel, String nachricht) {
        Alert a = new Alert(typ);
        a.setTitle(titel);
        a.setContentText(nachricht);
        a.showAndWait().ifPresent(rs -> {});
    }
}
