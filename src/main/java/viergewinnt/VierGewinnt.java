package viergewinnt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private static ObservableList<Spieler> spieler;
    public static ObservableList<Spieler> getSpieler() {
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
        spieler = FXCollections.observableArrayList();
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
        ).setUuid());
    }

    /**
     * Entfernt ein Spieler mit der uuid
     * @param uuid
     * @return
     * @throws Exception
     */
    public static void spielerEntfernen(String uuid) throws Exception {
        spieler = VierGewinnt.getSpieler().filtered(spieler -> !spieler.getUuid().equals(uuid));
    }

    /**
     * Wechselt den zwischen den Spielern
     */
    public static void spielerWechseln() {
        aktivenSpieler = aktivenSpieler == 1 ? 0 : 1;
    }


    /**
     * @param spalte Spalte, von der man die Höhe wissen will
     * @return Höhe der Spalte
     */
    public static int hoeheVonReihe(int spalte) {
        return VierGewinnt.spielfeld[spalte].size();
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
        spielfeld[reihe].add(spieler.id);
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
            if (spielfeld.length < x + 1 &&
                    y - 1 >= 0 &&
                    VierGewinnt.spielfeld[x + 1].size() >= y - 1 &&
                    spielfeld[x + 1].get(y - 1) == getAktivenSpieler().id &&
                    sindVierInEinerReihe(x + 1, y - 1)) {
                return true;
            }
            // unten
            if (spielfeld.length > x &&
                    y - 1 >= 0 &&
                    VierGewinnt.spielfeld[x].size() > y - 1 &&
                    spielfeld[x].get(y-1) == getAktivenSpieler().id &&
                    sindVierInEinerReihe(x, y - 1)) {
                return true;
            }
            // linksunten
            if (x - 1 >= 0 &&
                    y - 1 >= 0 &&
                    VierGewinnt.spielfeld[x - 1].size() > y - 1 &&
                    spielfeld[x - 1].get(y-1) == getAktivenSpieler().id &&
                    sindVierInEinerReihe(x - 1, y - 1)) {
                return true;
            }
            // links
            if (x - 1 >= 0 &&
                    y  >= 0 &&
                    VierGewinnt.spielfeld[x - 1].size() > y &&
                    spielfeld[x - 1].get(y) == getAktivenSpieler().id &&
                    sindVierInEinerReihe(x - 1, y)) {
                return true;
            }
            // rechts
            if (spielfeld.length > x + 3 &&
                    y >= 0 &&
                    VierGewinnt.spielfeld[x + 1].size() > y &&
                    VierGewinnt.spielfeld[x + 2].size() > y &&
                    VierGewinnt.spielfeld[x + 3].size() > y &&
                    spielfeld[x + 1].get(y) == getAktivenSpieler().id &&
                    spielfeld[x + 2].get(y) == getAktivenSpieler().id &&
                    spielfeld[x + 3].get(y) == getAktivenSpieler().id) {
                return true;
            }
            // rechtsoben
            if (spielfeld.length > x + 3 &&
                    y >= 0 &&
                    y + 3 <= 6 &&
                    VierGewinnt.spielfeld[x + 1].size() > y + 1 &&
                    VierGewinnt.spielfeld[x + 2].size() > y + 2 &&
                    VierGewinnt.spielfeld[x + 3].size() > y + 3 &&
                    spielfeld[x + 1].get(y + 1) == getAktivenSpieler().id &&
                    spielfeld[x + 2].get(y + 2) == getAktivenSpieler().id &&
                    spielfeld[x + 3].get(y + 3) == getAktivenSpieler().id) {
                return true;
            }
            // oben
            if (y >= 0 &&
                    y + 3 <= 6 &&
                    spielfeld[x].get(y + 1) == getAktivenSpieler().id &&
                    spielfeld[x].get(y + 2) == getAktivenSpieler().id &&
                    spielfeld[x].get(y + 3) == getAktivenSpieler().id) {
                return true;
            }
            // linksoben
            if (x - 3 >= 0 &&
                    y >= 0 &&
                    y + 3 <= 6 &&
                    VierGewinnt.spielfeld[x - 1].size() > y + 1 &&
                    VierGewinnt.spielfeld[x - 2].size() > y + 2 &&
                    VierGewinnt.spielfeld[x - 3].size() > y + 3 &&
                    spielfeld[x - 1].get(y + 1) == getAktivenSpieler().id &&
                    spielfeld[x - 2].get(y + 2) == getAktivenSpieler().id &&
                    spielfeld[x - 3].get(y + 3) == getAktivenSpieler().id) {
                return true;
            }
        } catch (Exception e) {}


        return false;
    }
}
