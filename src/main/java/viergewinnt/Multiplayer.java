package viergewinnt;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import com.google.gson.Gson;
import javafx.scene.control.cell.PropertyValueFactory;

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
    public ColorPicker multi_farbwahl;


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

        multi_tableview.setItems(VierGewinnt.getSpieler());
        multi_tableview.getColumns().addAll(name, farbe);
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
            Spieler spieler = new Spieler(0, multi_farbwahl.getValue(), multi_namenswahl.getText());
            // Verbinden
            WebSocketClient viergewinntClient = new WebSocketClient(new URI(multi_internetadresse.getText()));
            viergewinntClient.addMessageHandler(new WebSocketClient.NachrichtenBearbeitung() {
                @Override
                public void bearbeiteNachricht(String message) {
                    System.out.println(message);
                    String aktion = Json.createReader(new StringReader(message)).readObject().get("aktion").toString();
                    String daten = Json.createReader(
                            new StringReader(message)
                    ).readObject()
                            .get("daten")
                            .toString();


                    // Fehler wird angezeigt, wenn der Server einen Fehler meldet
                    if (aktion.equals("error")) {
                        String err = Json.createReader(
                                new StringReader(
                                        daten
                                )
                        ).readObject()
                        .get("info")
                        .toString();

                        System.out.println(err);
                        Platform.runLater(() ->
                                zeigeAlert(Alert.AlertType.ERROR, "Server Fehler", err)
                        );
                    }

                    // wenn spieler beigetreten ist
                    if (aktion.equals("Spieler ist beigetreten")) {
                        try {
                            JsonArray spielerArray = Json.createReader(
                                    new StringReader(
                                            daten
                                    )
                            ).readArray();

                            Spieler spieler1 = Spieler.ladeJson(spielerArray.getJsonObject(0).toString(), 0);
                            Spieler spieler2 = Spieler.ladeJson(spielerArray.getJsonObject(1).toString(), 0);

                            VierGewinnt.spielerHinzufuegen(spieler1);
                            VierGewinnt.spielerHinzufuegen(spieler2);

                            multi_tableview.setItems(VierGewinnt.getSpieler());
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        /*try {
                            VierGewinnt.spielerHinzufuegen(spieler);
                        } catch (Exception ignored) {}*/

                    }
                }
            });
            // Sende "trete Raum bei"
            viergewinntClient.sendMessage(
                    Json.createObjectBuilder()
                            .add("aktion", "trete Raum bei")
                            .add("daten", Json.createObjectBuilder()
                                    .add("raumId", multi_raum_ID.getText())
                                    .add("spieler", spieler.toJson())
                            ).build().toString()
            );

            // Lokalen Spieler Erstellen

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
