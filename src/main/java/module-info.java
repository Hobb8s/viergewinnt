module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens viergewinnt to javafx.fxml;
    exports viergewinnt;
}