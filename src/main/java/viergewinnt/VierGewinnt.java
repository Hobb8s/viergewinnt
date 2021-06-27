package viergewinnt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import org.javatuples.Pair;

public class VierGewinnt {

	// Array mit den verschiedenen möglichen Farben der Spieler
	public static String[] farben = { "Rot", "Orange", "Gelb", "Grün", "Blau", "Türkis", "Magenta" };

	// Spielfeld
	private static ArrayList<Integer>[] spielfeld;

	public static ArrayList<Integer>[] getSpielfeld() {
		return spielfeld;
	}

	// Liste in denene die Spielerobjekte gespeichert werden
	private static ObservableList<Spieler> spieler;

	public static ObservableList<Spieler> getSpieler() {
		return spieler;
	}

	// Der aktive Spieler, ist der Spieler, der den aktuellen Spielzug ausführt
	private static int aktivenSpieler = 0;

	public static Spieler getAktivenSpieler() {
		return spieler.get(aktivenSpieler);
	}

	// Der passive Spieler, ist der Spieler, der den nächsten Spielzug ausführt
	public static Spieler getpassivenSpieler() {
		return spieler.get(1 - aktivenSpieler);
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
		muliplayerModus = false;
		aktivenSpieler = 0;
	}

	/**
	 * Fügt ein neuen Spieler hinzu
	 * 
	 * @param spieler Neuer Spieler
	 * @throws Exception Wenn schon 2 Spieler hinzugefügt sind, wirft die Methode
	 *                   einen Fehler
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
	 * 
	 * @param name  Name des neuen Spielers
	 * @param farbe Farbe des neuen Spielers
	 * @throws Exception Wenn schon 2 Spieler hinzugefügt sind, wirft die Methode
	 *                   einen Fehler
	 */
	public static Spieler spielerHinzufuegen(String name, Color farbe) throws Exception {
		if (VierGewinnt.spieler.size() >= 2) {
			throw new Exception("Es sind breits 2 Spieler verhanden");
		}
		return VierGewinnt.spielerHinzufuegen(new Spieler(VierGewinnt.spieler.size(), farbe, name).setUuid());
	}

	/**
	 * Entfernt ein Spieler mit der uuid
	 * 
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
	 * 
	 * @param reihe   In welcher Reihe ein Spielstein hinzugefügt werden soll
	 * @param spieler Der Spieler, der den Spielstein hinzufügt
	 * @throws Exception Ein Fehler tritt auf, wenn das Spielfeld zu klein ist oder
	 *                   wenn die ensprechde Reihe voll ist.
	 */
	public static void reiheSetzen(int reihe, Spieler spieler) throws Exception {
		if (reihe > spielfeld.length)
			throw new Exception("Das Spielfeld ist nur 7 breit");
		if (spielfeld[reihe].size() >= 6)
			throw new Exception("Diese Reihe ist bereits voll");
		spielfeld[reihe].add(spieler.id);
	}

	/**
	 * @return den Spieler, der das Spiel gewonnen hat; wenn keiner gewonnen hat
	 *         wird null zurückgegeben
	 */
	public static Spieler hatGewonnen(int x, int y) {
		if (sindVierInEinerReihe(x, y))
			return spieler.get(aktivenSpieler);
		return null;
	}

	// Überprüfung, ob sich der Spielstein im Spielfeld befindet
	private static boolean istImFeld(int x, int y, String richtung) {

		if (richtung == "unten") {
			return x >= 0 && x < spielfeld.length && y - 3 >= 0 && y < 6;
		}

		if (richtung == "rechts") {
			return x >= 0 && x + 3 < spielfeld.length && y >= 0 && y < 6;
		}

		if (richtung == "links") {
			return x - 3 >= 0 && x < spielfeld.length && y >= 0 && y < 6;
		}

		if (richtung == "rechtsoben") {
			return x >= 0 && x + 3 < spielfeld.length && y >= 0 && y + 3 < 6;
		}

		if (richtung == "rechtsunten") {
			return x >= 0 && x + 3 < spielfeld.length && y - 3 >= 0 && y < 6;
		}

		if (richtung == "linksoben") {
			return x - 3 >= 0 && x < spielfeld.length && y >= 0 && y + 3 < 6;
		}

		if (richtung == "linksunten") {
			return x - 3 >= 0 && x < spielfeld.length && y - 3 >= 0 && y < 6;
		}

		return false;
	}

	// Überprüft die Anzahl der Steine und schließt anschließend unmögliche
	// Gewinnmöglichkeiten aus
	private static boolean überprüfeAnzahlSteine(int x, int y, String richtung) {

		if (richtung == "unten") {
			return true;
		}

		if (richtung == "rechts") {
			return VierGewinnt.spielfeld[x + 1].size() > y && VierGewinnt.spielfeld[x + 2].size() > y
					&& VierGewinnt.spielfeld[x + 3].size() > y;
		}

		if (richtung == "links") {
			return VierGewinnt.spielfeld[x - 1].size() > y && VierGewinnt.spielfeld[x - 2].size() > y
					&& VierGewinnt.spielfeld[x - 3].size() > y;
		}

		if (richtung == "rechtsoben") {
			return VierGewinnt.spielfeld[x + 1].size() > y + 1 && VierGewinnt.spielfeld[x + 2].size() > y + 2
					&& VierGewinnt.spielfeld[x + 3].size() > y + 3;
		}

		if (richtung == "rechtsunten") {
			return VierGewinnt.spielfeld[x + 1].size() > y - 1 && VierGewinnt.spielfeld[x + 2].size() > y - 2
					&& VierGewinnt.spielfeld[x + 3].size() > y - 3;
		}

		if (richtung == "linksoben") {
			return VierGewinnt.spielfeld[x - 1].size() > y + 1 && VierGewinnt.spielfeld[x - 2].size() > y + 2
					&& VierGewinnt.spielfeld[x - 3].size() > y + 3;
		}

		if (richtung == "linksunten") {
			return VierGewinnt.spielfeld[x - 1].size() > y - 1 && VierGewinnt.spielfeld[x - 2].size() > y - 2
					&& VierGewinnt.spielfeld[x - 3].size() > y - 3;
		}

		return false;
	}

	// Überprüft, ob die Steine in die linke Richtung vom selben Spieler gelegt
	// wurden
	private static Pair<Integer, Integer> linksGleich(int x, int y, String richtung) {

		if (richtung == "links" && x - 1 >= 0 && x < spielfeld.length && y >= 0 && y < 6
				&& VierGewinnt.spielfeld[x - 1].size() > y
				&& VierGewinnt.spielfeld[x - 1].get(y) == getAktivenSpieler().id) {
			return new Pair<Integer, Integer>(-1, 0);
		}

		if (richtung == "linksoben" && x - 1 >= 0 && x < spielfeld.length && y >= 0 && y + 1 < 6
				&& VierGewinnt.spielfeld[x - 1].size() > y + 1
				&& VierGewinnt.spielfeld[x - 1].get(y + 1) == getAktivenSpieler().id) {
			return new Pair<Integer, Integer>(-1, 1);
		}

		if (richtung == "linksunten" && x - 1 >= 0 && x < spielfeld.length && y - 1 >= 0 && y < 6
				&& VierGewinnt.spielfeld[x - 1].size() > y - 1
				&& VierGewinnt.spielfeld[x - 1].get(y - 1) == getAktivenSpieler().id) {
			return new Pair<Integer, Integer>(-1, -1);
		}

		return null;
	}

	// Überprüfung, ob vier Steine, die in einer Reihe liegen, vom selben Spieler
	// gelegt wurden
	private static boolean überprüfeSteineVonSpielern(int x, int y, String richtung) {

		if (richtung == "unten") {
			return VierGewinnt.spielfeld[x].get(y) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x].get(y - 1) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x].get(y - 2) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x].get(y - 3) == getAktivenSpieler().id;
		}

		if (richtung == "rechts") {
			return VierGewinnt.spielfeld[x].get(y) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x + 1].get(y) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x + 2].get(y) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x + 3].get(y) == getAktivenSpieler().id;
		}

		if (richtung == "links") {
			return VierGewinnt.spielfeld[x].get(y) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x - 1].get(y) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x - 2].get(y) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x - 3].get(y) == getAktivenSpieler().id;
		}

		if (richtung == "rechtsoben") {
			System.out.println("###");
			return VierGewinnt.spielfeld[x].get(y) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x + 1].get(y + 1) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x + 2].get(y + 2) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x + 3].get(y + 3) == getAktivenSpieler().id;
		}

		if (richtung == "rechtsunten") {
			return VierGewinnt.spielfeld[x].get(y) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x + 1].get(y - 1) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x + 2].get(y - 2) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x + 3].get(y - 3) == getAktivenSpieler().id;
		}

		if (richtung == "linksoben") {
			System.out.println("+++");
			return VierGewinnt.spielfeld[x].get(y) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x - 1].get(y + 1) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x - 2].get(y + 2) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x - 3].get(y + 3) == getAktivenSpieler().id;
		}

		if (richtung == "linksunten") {
			return VierGewinnt.spielfeld[x].get(y) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x - 1].get(y - 1) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x - 2].get(y - 2) == getAktivenSpieler().id
					&& VierGewinnt.spielfeld[x - 3].get(y - 3) == getAktivenSpieler().id;
		}

		return false;
	}

	// Überprüfung, ob vier Spielsteine in einer Reihe sind (egal ob horizontal,
	// vertikal oder diagonal)
	private static boolean sindVierInEinerReihe(int xs, int ys) {

		String[] richtungen = new String[] { "unten", "rechts", "links", "rechtsoben", "rechtsunten", "linksoben",
				"linksunten" };

		for (int i = 0; i < richtungen.length; i++) {
			int x = xs;
			int y = ys;

			if (istImFeld(x, y, richtungen[i]) && überprüfeAnzahlSteine(x, y, richtungen[i])
					&& überprüfeSteineVonSpielern(x, y, richtungen[i]))
				return true;

			Pair<Integer, Integer> lg = linksGleich(x, y, richtungen[i]);
			if (lg == null)
				continue;

			x += lg.getValue0();
			y += lg.getValue1();

			if (sindVierInEinerReihe(x, y))
				return true;

		}

		return false;
	}
}
