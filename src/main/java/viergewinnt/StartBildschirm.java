package viergewinnt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartBildschirm {
    @FXML
    private Button btnNutzlos;

    public void ausgeben(ActionEvent event){
        System.out.println("Mit irgendetwas :)");
    }
}