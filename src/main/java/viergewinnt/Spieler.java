package viergewinnt;

public class Spieler {
    private int id;
    public int getId() {
        return id;
    }

    private String farbe;
    public String getFarbe() {
        return farbe;
    }

    private String name;
    public String getName() {
        return name;
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
}
