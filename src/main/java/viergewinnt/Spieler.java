package viergewinnt;

import javafx.scene.paint.Color;


/**
 * Alle Felder müssen public sein, da diese Klasse in JSON umgewndelt werden soll.
 */
public class Spieler {
    public int id;

    public String farbe;

    public String name;

    public Spieler(
            int id,
            Color farbe,
            String name
    ) {
        this.id = id;
        this.farbe = "rgba(" + Math.round(farbe.getRed() * 255) + "," + Math.round(farbe.getGreen() * 255) + "," + Math.round(farbe.getBlue() * 255) + "," + farbe.getOpacity() + ")";
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " Farbe: " + farbe;
    }
}
