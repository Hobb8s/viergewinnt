package viergewinnt;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class Singleplayer {

    @FXML
    public Button single_weiter;
    public ColorPicker single_farbwahl2;
    public ColorPicker single_farbwahl1;
    public TextField single_namenswahl2;
    public TextField single_namenswahl1;
    public Button single_zurück;

    /**
     * Weiterleitung zur spiel.fxml, sofern zwei unterschiedliche Namen und zwei unterschiedliche Farben ausgewählt wurden, andernfalls folgt eine Fehlermeldung
     */
    public void singleweiter() throws IOException
    {
        if (single_namenswahl1.getText() == null || single_namenswahl2.getText() == null || single_namenswahl1.getText().equals(single_namenswahl2.getText()))
        {
            Alert keinName = new Alert(Alert.AlertType.INFORMATION);
            keinName.setTitle("Fehler");
            keinName.setContentText("Geben Sie bitte zwei unterschiedliche Namen ein!");
            keinName.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK)
                {
                    System.out.println("OK gedrückt.");
                }
            });
        }
        else
        {
            if (single_farbwahl1.getValue().toString().equals(single_farbwahl2.getValue().toString()))
            {
                Alert gleicheFarbe = new Alert(Alert.AlertType.INFORMATION);
                gleicheFarbe.setTitle("Fehler");
                gleicheFarbe.setContentText("Wählen Sie bitte zwei unterschiedliche Farben aus!");
                gleicheFarbe.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK)
                    {
                        System.out.println("OK gedrückt.");
                    }
                });
            }
            else
            {
                App.setRoot("spiel");
                try {
                    Auswahl();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * Weiterleitung zur startBildschirm.fxml
     */
    public void singlezurück() throws IOException
    {
        App.setRoot("startBildschirm");
    }

    /**
     * Hinzufügen der zwei Spieler mit den ausgewählten Namen und Farben
     */
    public void Auswahl() throws Exception
    {
        VierGewinnt.zuruecksetzen();
        VierGewinnt.spielerHinzufuegen(single_namenswahl1.getText(), single_farbwahl1.getValue());
        VierGewinnt.spielerHinzufuegen(single_namenswahl2.getText(), single_farbwahl2.getValue());

    }

}
