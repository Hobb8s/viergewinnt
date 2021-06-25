package viergewinnt;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;


public class StartBildschirm {

    @FXML
    public Button btn_singleplayer;
    public Button btn_multiplayer;
    public Button btn_gitHub;


    // Weiterleitung zur Singleplayer.fxml
    public void singleplayerÖffnen() throws IOException
    {
        App.setRoot("singleplayer");
    }


    // Weiterleitung zur Multiplayer.fxml
    public void multiplayerÖffnen() throws IOException
    {
        App.setRoot("multiplayer");
    }


    // Weiterleitung zur Internetseite (https://github.com/Hobb8s/viergewinnt), auf welcher man eine Übersicht von dem Projekt erlangt und die Möglichkeit besitzt, den Code herunterzuladen
    public void gitHubÖffnen() throws IOException
    {
        App.app.oeffneWebAdresse("https://github.com/Hobb8s/viergewinnt");
    }

    }



