module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javax.json.api;
    requires javax.websocket.client.api;
    requires com.google.gson;

    opens viergewinnt to javafx.fxml;
    exports viergewinnt;
}