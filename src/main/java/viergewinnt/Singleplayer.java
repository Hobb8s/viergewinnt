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
        if (single_namenswahl1.getText() == null || single_namenswahl2.getText() == null
                || single_namenswahl1.getText().equals(single_namenswahl2.getText())) {
            Alert keinName = new Alert(Alert.AlertType.INFORMATION);
            keinName.setTitle("Fehler");
            keinName.setContentText("Geben Sie bitte zwei unterschiedliche Namen ein!");
            keinName.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("OK gedrückt.");
                }
            });
        } else {
            if (single_farbwahl1.getValue() == single_farbwahl2.getValue()) {
                Alert gleicheFarbe = new Alert(Alert.AlertType.INFORMATION);
                gleicheFarbe.setTitle("Fehler");
                gleicheFarbe.setContentText("Wählen Sie bitte zwei unterschiedliche Farben aus!");
                gleicheFarbe.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("OK gedrückt.");
                    }
                });
            } else {
                if (single_farbwahl1.getValue() == null || single_farbwahl2.getValue() == null) {
                    Alert gleicheFarbe = new Alert(Alert.AlertType.ERROR);
                    gleicheFarbe.setTitle("Fehler");
                    gleicheFarbe.setContentText("Wählen Sie bitte zwei unterschiedliche Farben aus!");
                    gleicheFarbe.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("OK gedrückt.");
                        }
                    });
                } else {

                    try {
                        App.setRoot("spiel");
                        FarbWahlSpieler1();
                        FarbWahlSpieler2();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    // Weiterleitung zur startBildschirm.fxml
    public void singlezurück() throws IOException {
        App.setRoot("startBildschirm");
    }

    // Erstellen eines Array mit den verschiedenen möglichen Farben
    private String[] farben = { "Rot", "Orange", "Gelb", "Grün", "Blau", "Schwarz" };

    // Einfügen des Array in die beiden Choice-Boxen
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        single_farbwahl1.getItems().addAll(farben);
        single_farbwahl2.getItems().addAll(farben);
    }

    // Erstellen des ersten Spielers mit dem angegebenen Namen und der ausgewählten
    // Farbe
    public void FarbWahlSpieler1() throws IOException {
        VierGewinnt.zuruecksetzen();
        String farbe1 = single_farbwahl1.getValue();

        switch (farbe1) {
            case "Rot":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), Color.RED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "Orange":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), Color.ORANGE);
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

            case "Schwarz":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), Color.BLACK);
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

            case "Schwarz":
                try {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl2.getText(), Color.BLACK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }

    }

}
