package viergewinnt;

import javafx.scene.paint.Color;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.util.UUID;


/**
 * Alle Felder müssen public sein, da diese Klasse in JSON umgewndelt werden soll.
 */
public class Spieler {
    public int id;

    public String farbe;
    public String getFarbe() {
        return farbe;
    }

    public String name;
    public String getName() {
        return name;
    }

    private String uuid;
    public String getUuid() {
        return uuid;
    }
    public Spieler setUuid() {
        this.uuid = UUID.randomUUID().toString();
        return this;
    }
    public Spieler setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Spieler(
            int id,
            Color farbe,
            String name
    ) {
        this.id = id;
        this.farbe = "rgba(" + Math.round(farbe.getRed() * 255) + "," + Math.round(farbe.getGreen() * 255) + "," + Math.round(farbe.getBlue() * 255) + "," + farbe.getOpacity() + ")";
        this.name = name;
    }

    public Spieler(
            int id,
            String farbe,
            String name
    ) {
        this.id = id;
        this.farbe = farbe;
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " Farbe: " + farbe;
    }

    /**
     * Erstellt ein Json Object aus einer Instanz
     * @return das Json Object das erstellt wurde
     */
    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("id", id)
                .add("uuid", uuid)
                .add("farbe", farbe)
                .add("name", name)
                .build();
    }

    public static Spieler ladeJson(String json, int id) {
        JsonObject spieler = javax.json.Json.createReader(
                new StringReader(
                        json
                )
        ).readObject();
        return new Spieler(id, spieler.get("farbe").toString(), spieler.get("name").toString()).setUuid(spieler.get("uuid").toString());
    }
}

