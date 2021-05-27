package viergewinnt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Muiltiplayer implements Initializable {
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
            // Verbinden


            // Lokalen Spieler Erstellen
            String[] spieler = {VierGewinnt.spielerHinzufuegen(new Spieler(0, multi_farbwahl.getValue(), multi_namenswahl.getText())).toString()};
            multi_liste.getItems().addAll(spieler);

            // Lokalen Spieler an den Server schicken
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
