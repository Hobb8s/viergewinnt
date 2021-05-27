package viergewinnt;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class VierGewinnt {

    /**
     * Spielfeld (7 breit, 6 hoch)
     */
    private static ArrayList<Integer>[] spielfeld;
    public static ArrayList<Integer>[] getSpielfeld() {
        return spielfeld;
    }

    private static ArrayList<Spieler> spieler;
    public static ArrayList<Spieler> getSpieler() {
        return spieler;
    }

    private static int aktivenSpieler = 0;
    public static Spieler getAktivenSpieler() {
        return spieler.get(aktivenSpieler);
    }

    public static boolean muliplayerModus = false;

    /**
     * Setzt das Spiel zurück
     */
    public static void zuruecksetzen() {
        spielfeld = new ArrayList[7];
        for (int i = 0; i < spielfeld.length; i++) {
            spielfeld[i] = new ArrayList<Integer>();
        }
        spieler = new ArrayList<Spieler>();
    }

    /**
     * Fügt ein neuen Spieler hinzu
     * @param spieler Neuer Spieler
     * @throws Exception Wenn schon 2 Spieler hinzugefügt sind, wirft die Methode einen Fehler
     */
    public static Spieler spielerHinzufuegen(Spieler spieler) throws Exception {
        if (VierGewinnt.spieler.size() >= 2) {
            throw new Exception("Es sind breits 2 Spieler verhanden");
        }
        VierGewinnt.spieler.add(spieler);

        return spieler;
    }

    /**
     * Fügt ein neuen Spieler hinzu
     * @param name Name des neuen Spielers
     * @param farbe Farbe des neuen Spielers
     * @throws Exception Wenn schon 2 Spieler hinzugefügt sind, wirft die Methode einen Fehler
     */
    public static Spieler spielerHinzufuegen(String name, Color farbe) throws Exception {
        if (VierGewinnt.spieler.size() >= 2) {
            throw new Exception("Es sind breits 2 Spieler verhanden");
        }
        return VierGewinnt.spielerHinzufuegen(new Spieler(
                VierGewinnt.spieler.size(),
                farbe,
                name
        ));
    }

    /**
     * Wechselt den zwischen den Spielern
     */
    public static void spielerWechseln() {
        aktivenSpieler = aktivenSpieler == 1 ? 0 : 1;
    }


    /**
     * Fügt einen Spielstein in der entsprechen Reihe hinzu
     * @param reihe In welcher Reihe ein Spielstein hinzugefügt werden soll
     * @param spieler Der Spieler, der den Spielstein hinzufügt
     * @throws Exception Ein Fehler tritt auf, wenn das Spielfeld zu klein ist oder wenn die ensprechde Reihe voll ist.
     */
    public static void reiheSetzen(int reihe, Spieler spieler) throws Exception {
        if (reihe > spielfeld.length) throw new Exception("Das Spielfeld ist nur 7 breit");
        if (spielfeld[reihe].size() >= 6) throw new Exception("Diese Reihe ist bereits voll");
        spielfeld[reihe].add(spieler.getId());
        System.out.println("Hinzugefügt: " + spielfeld[reihe].size());
    }


    /**
     * @return den Spieler, der das Spiel gewonnen hat; wenn keiner gewonnen hat wird null zurückgegeben
     */
    public static Spieler istGewonnen(int x, int y) {
        if(sindVierInEinerReihe(x, y))
            return spieler.get(aktivenSpieler);
        return null;
    }

    private static boolean sindVierInEinerReihe(int x, int y) {
        try {
            // rechtsunten
            if (spielfeld.length > x + 1 &&
                    y - 1 >= 0 &&
                    VierGewinnt.spielfeld[x + 1].size() > y - 1 &&
                    spielfeld[x + 1].get(y - 1) == getAktivenSpieler().getId() &&
                    sindVierInEinerReihe(x + 1, y - 1)) {
                return true;
            }
            // unten
            if (spielfeld.length > x &&
                    y - 1 >= 0 &&
                    VierGewinnt.spielfeld[x].size() > y - 1 &&
                    spielfeld[x].get(y-1) == getAktivenSpieler().getId() &&
                    sindVierInEinerReihe(x, y - 1)) {
                return true;
            }
            // linksunten
            if (x - 1 >= 0 &&
                    y - 1 >= 0 &&
                    VierGewinnt.spielfeld[x - 1].size() > y - 1 &&
                    spielfeld[x - 1].get(y-1) == getAktivenSpieler().getId() &&
                    sindVierInEinerReihe(x - 1, y - 1)) {
                return true;
            }
            // links
            if (x - 1 >= 0 &&
                    y  >= 0 &&
                    VierGewinnt.spielfeld[x - 1].size() > y &&
                    spielfeld[x - 1].get(y) == getAktivenSpieler().getId() &&
                    sindVierInEinerReihe(x - 1, y)) {
                return true;
            }
            // rechts
            if (spielfeld.length > x + 3 &&
                    y >= 0 &&
                    VierGewinnt.spielfeld[x + 1].size() > y &&
                    VierGewinnt.spielfeld[x + 2].size() > y &&
                    VierGewinnt.spielfeld[x + 3].size() > y &&
                    spielfeld[x + 1].get(y) == getAktivenSpieler().getId() &&
                    spielfeld[x + 2].get(y) == getAktivenSpieler().getId() &&
                    spielfeld[x + 3].get(y) == getAktivenSpieler().getId()) {
                return true;
            }
            // rechtsoben
            if (spielfeld.length > x + 3 &&
                    y >= 6 &&
                    VierGewinnt.spielfeld[x + 1].size() > y + 1 &&
                    VierGewinnt.spielfeld[x + 2].size() > y + 2 &&
                    VierGewinnt.spielfeld[x + 3].size() > y + 3 &&
                    spielfeld[x + 1].get(y + 1) == getAktivenSpieler().getId() &&
                    spielfeld[x + 2].get(y + 2) == getAktivenSpieler().getId() &&
                    spielfeld[x + 3].get(y + 3) == getAktivenSpieler().getId()) {
                return true;
            }
            // oben
            if (y >= 6 &&
                    spielfeld[x].get(y + 1) == getAktivenSpieler().getId() &&
                    spielfeld[x].get(y + 2) == getAktivenSpieler().getId() &&
                    spielfeld[x].get(y + 3) == getAktivenSpieler().getId()) {
                return true;
            }
            // linksoben
            if (x - 3 >= 0 &&
                    y >= 6 &&
                    VierGewinnt.spielfeld[x].size() > y + 1 &&
                    VierGewinnt.spielfeld[x].size() > y + 2 &&
                    VierGewinnt.spielfeld[x].size() > y + 3 &&
                    spielfeld[x - 1].get(y + 1) == getAktivenSpieler().getId() &&
                    spielfeld[x - 2].get(y + 2) == getAktivenSpieler().getId() &&
                    spielfeld[x - 3].get(y + 3) == getAktivenSpieler().getId()) {
                return true;
            }
        } catch (Exception e) {System.out.println("Reihe: " + VierGewinnt.spielfeld[x]);System.out.println("Size: " + VierGewinnt.spielfeld[x].size());System.out.println(e);}


        return false;
    }
}
