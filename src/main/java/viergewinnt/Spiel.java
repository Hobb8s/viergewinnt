package viergewinnt;

import java.util.Optional;
import java.util.Timer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import javafx.scene.control.ProgressBar;

public class Spiel {

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

    /**
     * Bestätigen des Abbrechens des Spiels und Zurückgelangen zur startBildschirm.fxml
     */
    public void verlassen() throws IOException
    {
        Alert bestätigung = new Alert(Alert.AlertType.CONFIRMATION);
        bestätigung.setTitle("Verlassen des Spiels");
        bestätigung.setContentText("Wenn Sie fortfahren, verlassen Sie das Spiel und brechen es somit ab.");

        Optional<ButtonType> result = bestätigung.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            App.setRoot("startBildschirm");
        }
        else
        {
            bestätigung.close();
        }
    }

    public void rueckwaertsProgressBar()
    {

    }





    public void pause()
    {


    }

    public void spielfeldClicked(ActionEvent event) {



        // Speichere die Id von dem Button, der das Event getriggert hat
        String id = ((Node) event.getSource()).getId();

        // Zerlege die Id: "spielfeld_0_1" -> ["spielfeld", "0", "1"]
        String[] ids = id.split("_");

        try {
            VierGewinnt.reiheSetzen(Integer.parseInt(ids[2]), VierGewinnt.getAktivenSpieler());
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Fehler");
            a.setContentText(e.getMessage());
            a.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        try {
            Button b = (Button) getClass().getDeclaredField(
                    "spielfeld_"
                            + ((VierGewinnt.hoeheVonReihe(Integer.parseInt(ids[2])) - 6) * -1)
                            + "_"
                            + ids[2]
            ).get(this);
            b.setStyle("-fx-background-color: " + VierGewinnt.getAktivenSpieler().farbe + ";");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Spieler istGewonnen = VierGewinnt.istGewonnen(Integer.parseInt(ids[2]), (Integer.parseInt(ids[1]) - 5) * (-1));
        if(istGewonnen != null/* || VierGewinnt.muliplayerModus && */) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Fehler");
            a.setContentText("Spieler " + istGewonnen.name + " hat gewonnen.");
            a.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });

            // TODO: Zurücksetzen auf den Startbildschirm

            return;
        }

        VierGewinnt.spielerWechseln();
    }
}
