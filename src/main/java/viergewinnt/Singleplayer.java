package viergewinnt;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Singleplayer implements Initializable {

    @FXML
    public Button single_weiter;
    public TextField single_namenswahl2;
    public TextField single_namenswahl1;
    public Button single_zurück;
    public ChoiceBox<String> single_farbwahl1;
    public ChoiceBox<String> single_farbwahl2;

    // Weiterleitung zur spiel.fxml, sofern zwei unterschiedliche Namen und zwei
    // unterschiedliche Farben ausgewählt wurden, andernfalls folgt eine
    // Fehlermeldung
    public void singleweiter() {

        // Abfrage, die überprüft, ob zwei unterschiedliche Namen eingegeben wurden
        if (single_namenswahl1.getText() == null || single_namenswahl2.getText() == null
                || single_namenswahl1.getText().equals(single_namenswahl2.getText())) {
            Alert keinName = new Alert(Alert.AlertType.INFORMATION);
            keinName.setTitle("Fehler");
            keinName.setContentText("Geben Sie bitte zwei unterschiedliche Namen ein!");
        } else if (single_farbwahl1.getValue() == single_farbwahl2.getValue()) {
            Alert gleicheFarbe = new Alert(Alert.AlertType.INFORMATION);
            gleicheFarbe.setTitle("Fehler");
            gleicheFarbe.setContentText("Wählen Sie bitte zwei unterschiedliche Farben aus!");
        } else if (single_farbwahl1.getValue() == null || single_farbwahl2.getValue() == null) {
            Alert gleicheFarbe = new Alert(Alert.AlertType.ERROR);
            gleicheFarbe.setTitle("Fehler");
            gleicheFarbe.setContentText("Wählen Sie bitte zwei unterschiedliche Farben aus!");
        } else {

            // Weiterleitung zur spiel.fxml, sofern unterschiedliche Namen und Farben
            // ausgewählt wurden
            try {
                FarbWahlSpieler1();
                FarbWahlSpieler2();
                App.setRoot("spiel");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // Zurückgelangen zur startBildschirm.fxml
    public void singlezurück() throws IOException {
        App.setRoot("startBildschirm");
    }

    // Einfügen des Array in die beiden Choice-Boxen
    // Methode wird beim Starten der FXML-Datei ausgeführt
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        VierGewinnt.zuruecksetzen();
        single_farbwahl1.getItems().addAll(VierGewinnt.farben);
        single_farbwahl2.getItems().addAll(VierGewinnt.farben);
    }

    // Erstellen des ersten Spielers mit dem angegebenen Namen und der ausgewählten
    // Farbe
    public void FarbWahlSpieler1() {
        String farbe1 = single_farbwahl1.getValue();

        switch (farbe1) {
            case "Rot":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), Color.RED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "Gelb":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), Color.YELLOW);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "Grün":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), Color.GREEN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "Blau":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), Color.BLUE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "Türkis":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), Color.rgb(64, 224, 208));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "Magenta":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), Color.MAGENTA);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), Color.ORANGE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    // Erstellen des zweiten Spielers mit dem angegebenen Namen und der ausgewählten
    // Farbe
    public void FarbWahlSpieler2() {
        String farbe2 = single_farbwahl2.getValue();

        switch (farbe2) {
            case "Rot":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl2.getText(), Color.RED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "Orange":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl2.getText(), Color.ORANGE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "Gelb":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl2.getText(), Color.YELLOW);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "Grün":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl2.getText(), Color.GREEN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "Blau":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl2.getText(), Color.BLUE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "Türkis":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl2.getText(), Color.rgb(64, 224, 208));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "Magenta":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl2.getText(), Color.MAGENTA);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl2.getText(), Color.BLUE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }

    }

}
