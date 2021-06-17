module org.example {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javax.json.api;
    requires transitive javax.websocket.client.api;

    opens viergewinnt to javafx.fxml;
    exports viergewinnt;
}