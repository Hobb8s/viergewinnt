package viergewinnt;

import javafx.scene.paint.Color;

public class Spieler {
    private int id;
    public int getId() {
        return id;
    }

    private Color farbe;
    public Color getFarbe() {
        return farbe;
    }
    public String getFarbeAlsString() {
        return "rgba(" + Math.round(getFarbe().getRed() * 255) + "," + Math.round(getFarbe().getGreen() * 255) + "," + Math.round(getFarbe().getBlue() * 255) + "," + getFarbe().getOpacity() + ")";
    }

    private String name;
    public String getName() {
        return name;
    }

    public Spieler(
            int id,
            Color farbe,
            String name
    ) {
        this.id = id;
        this.farbe = farbe;
        this.name = name;
    }

    @Override
    public String toString() {
        return getName() + " Farbe: " + getFarbeAlsString();
    }
}
