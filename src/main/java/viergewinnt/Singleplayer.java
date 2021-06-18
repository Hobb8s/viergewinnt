package viergewinnt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Singleplayer implements Initializable{

    @FXML
    public Button single_weiter;
    public TextField single_namenswahl2;
    public TextField single_namenswahl1;
    public Button single_zurück;
    public ChoiceBox<String> single_farbwahl1;
    public ChoiceBox<String> single_farbwahl2;

    // Weiterleitung zur spiel.fxml, sofern zwei unterschiedliche Namen und zwei unterschiedliche Farben ausgewählt wurden, andernfalls folgt eine Fehlermeldung
    public void singleweiter()  {
        if (single_namenswahl1.getText() == null || single_namenswahl2.getText() == null || single_namenswahl1.getText().equals(single_namenswahl2.getText())) {
            Alert keinName = new Alert(Alert.AlertType.INFORMATION);
            keinName.setTitle("Fehler");
            keinName.setContentText("Geben Sie bitte zwei unterschiedliche Namen ein!");
            keinName.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("OK gedrückt.");
                }
            });
        }
        else {
            if (single_farbwahl1.getValue() == single_farbwahl2.getValue()) {
                Alert gleicheFarbe = new Alert(Alert.AlertType.INFORMATION);
                gleicheFarbe.setTitle("Fehler");
                gleicheFarbe.setContentText("Wählen Sie bitte zwei unterschiedliche Farben aus!");
                gleicheFarbe.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("OK gedrückt.");
                    }
                });
            }
            else
            {
                if (single_farbwahl1.getValue() == null || single_farbwahl2.getValue() == null) {
                    Alert gleicheFarbe = new Alert(Alert.AlertType.ERROR);
                    gleicheFarbe.setTitle("Fehler");
                    gleicheFarbe.setContentText("Wählen Sie bitte zwei unterschiedliche Farben aus!");
                    gleicheFarbe.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("OK gedrückt.");
                        }
                    });
                }
                else
                {

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
    private String[] farben = {"Rot", "Orange", "Gelb", "Grün", "Blau", "Schwarz"};

    // Einfügen des Array in die beiden Choice-Boxen
    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        single_farbwahl1.getItems().addAll(farben);
        single_farbwahl2.getItems().addAll(farben);
    }

    // Erstellen des ersten Spielers mit dem angegebenen Namen und der ausgewählten Farbe
    public void FarbWahlSpieler1()
    {
        try {
            if (single_farbwahl1.getValue() == "Rot")
            {
                VierGewinnt.zuruecksetzen();
                VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), Color.RED);
            }
            else
            {
                if (single_farbwahl1.getValue() == "Orange")
                {
                    VierGewinnt.zuruecksetzen();
                    VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), Color.ORANGE);
                }
                else
                {
                    if (single_farbwahl1.getValue() == "Gelb")
                    {
                        VierGewinnt.zuruecksetzen();
                        VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), Color.YELLOW);
                    }
                    else
                    {
                        if (single_farbwahl1.getValue() == "Grün")
                        {
                            VierGewinnt.zuruecksetzen();
                            VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), Color.GREEN);
                        }
                        else
                        {
                            if (single_farbwahl1.getValue() == "Blau")
                            {
                                VierGewinnt.zuruecksetzen();
                                VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), Color.BLUE);
                            }
                            else
                            {
                                if (single_farbwahl1.getValue() == "Schwarz")
                                {
                                    VierGewinnt.zuruecksetzen();
                                    VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), Color.BLACK);
                                }
                            }
                        }
                    }
                }

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        };

    }

    // Erstellen des zweiten Spielers mit dem angegebenen Namen und der ausgewählten Farbe
    public void FarbWahlSpieler2()
    {
        try {
            if (single_farbwahl2.getValue() == "Rot")
            {
                VierGewinnt.spielerHinzufuegen(single_namenswahl2.getText(), Color.RED);
            }
            else
            {
                if (single_farbwahl2.getValue() == "Orange")
                {
                    VierGewinnt.spielerHinzufuegen(single_namenswahl2.getText(), Color.ORANGE);
                }
                else
                {
                    if (single_farbwahl2.getValue() == "Gelb")
                    {
                        VierGewinnt.spielerHinzufuegen(single_namenswahl2.getText(), Color.YELLOW);
                    }
                    else
                    {
                        if (single_farbwahl2.getValue() == "Grün")
                        {
                            VierGewinnt.spielerHinzufuegen(single_namenswahl2.getText(), Color.GREEN);
                        }
                        else
                        {
                            if (single_farbwahl2.getValue() == "Blau")
                            {
                                VierGewinnt.spielerHinzufuegen(single_namenswahl2.getText(), Color.BLUE);
                            }
                            else
                            {
                                if (single_farbwahl2.getValue() == "Schwarz")
                                {
                                    VierGewinnt.spielerHinzufuegen(single_namenswahl2.getText(), Color.BLACK);
                                }
                            }
                        }
                    }
                }

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        };
    }
}






