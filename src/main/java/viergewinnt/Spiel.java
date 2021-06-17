package viergewinnt;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert.AlertType;


public class Spiel {

	@FXML
	public Button spielfeld_0_0;
	@FXML
	public Button spielfeld_0_1;
	@FXML
	public Button spielfeld_0_2;
	@FXML
	public Button spielfeld_0_3;
	@FXML
	public Button spielfeld_0_4;
	@FXML
	public Button spielfeld_0_5;
	@FXML
	public Button spielfeld_0_6;
	@FXML
	public Button spielfeld_1_0;
	@FXML
	public Button spielfeld_1_1;
	@FXML
	public Button spielfeld_1_2;
	@FXML
	public Button spielfeld_1_3;
	@FXML
	public Button spielfeld_1_4;
	@FXML
	public Button spielfeld_1_5;
	@FXML
	public Button spielfeld_1_6;
	@FXML
	public Button spielfeld_2_0;
	@FXML
	public Button spielfeld_2_1;
	@FXML
	public Button spielfeld_2_2;
	@FXML
	public Button spielfeld_2_3;
	@FXML
	public Button spielfeld_2_4;
	@FXML
	public Button spielfeld_2_5;
	@FXML
	public Button spielfeld_2_6;
	@FXML
	public Button spielfeld_3_0;
	@FXML
	public Button spielfeld_3_1;
	@FXML
	public Button spielfeld_3_2;
	@FXML
	public Button spielfeld_3_3;
	@FXML
	public Button spielfeld_3_4;
	@FXML
	public Button spielfeld_3_5;
	@FXML
	public Button spielfeld_3_6;
	@FXML
	public Button spielfeld_4_0;
	@FXML
	public Button spielfeld_4_1;
	@FXML
	public Button spielfeld_4_2;
	@FXML
	public Button spielfeld_4_3;
	@FXML
	public Button spielfeld_4_4;
	@FXML
	public Button spielfeld_4_5;
	@FXML
	public Button spielfeld_4_6;
	@FXML
	public Button spielfeld_5_0;
	@FXML
	public Button spielfeld_5_1;
	@FXML
	public Button spielfeld_5_2;
	@FXML
	public Button spielfeld_5_3;
	@FXML
	public Button spielfeld_5_4;
	@FXML
	public Button spielfeld_5_5;
	@FXML
	public Button spielfeld_5_6;
	@FXML
	public Button spielfeld_verlassen;
	@FXML
	public Button spielfeld_pause;
	@FXML
	public ProgressBar spielfeld_progressbar;


	/**
	 * Bestätigen des Abbrechens des Spiels und Zurückgelangen zur startBildschirm.fxml
	 */
	public void verlassen() throws IOException
	{
		PauseZahl = 2;
		Alert bestätigung = new Alert(Alert.AlertType.CONFIRMATION);
		bestätigung.setTitle("Verlassen des Spiels");
		bestätigung.setContentText("Wenn Sie fortfahren, verlassen Sie das Spiel und brechen es somit ab.");

		Optional<ButtonType> result = bestätigung.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			App.setRoot("startBildschirm");
			PauseZahl = 0;
		}
		else
		{
			bestätigung.close();
			PauseZahl = 1;
		}
	}

	/**
	 verbleibendeZeit = Zeit, die den beiden Spielern noch verbleibt, um das Spiel zu beenden
	 PauseZahl = Erklärung folgt bei der Methode pause()
	 */
	int verbleibendeZeit = 20;
	int PauseZahl = 0;


	public void rueckwaertsProgressBar() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (verbleibendeZeit > 0)
				{
					if (PauseZahl == 1) {
						verbleibendeZeit = verbleibendeZeit - 1;
						// spielfeld_progressbar.setValue(verbleibendeZeit);
						System.out.println(verbleibendeZeit);
					}
				}
				else
				{
					timer.cancel();
					System.out.println("Die Zeit ist abgelaufen.");
					ZeitAlert();
				}
			}
		};
		timer.scheduleAtFixedRate(task, 1000, 1000);

	}

	public void ZeitAlert()
	{
		Alert zeitVorbei = new Alert(AlertType.CONFIRMATION);
		zeitVorbei.setTitle("Ablauf der Zeit");
		zeitVorbei.setContentText("Leider waren Sie zu langsam und die Zeit ist abgelaufen.");
		zeitVorbei.showAndWait().ifPresent(rs -> {
			if (rs == ButtonType.OK) {
				try {
					App.setRoot("startBildschirm");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	Pausezahl: 0 & 2 = Zeit steht bzw. läuft nicht   (Unterschied: Text -> Start oder Weiter)
			   1 = Zeit läuft
	 */
	public void pause()
	{
		if (PauseZahl == 0)
		{
			rueckwaertsProgressBar();
			PauseZahl = 1;
			spielfeld_pause.setText("Pause");
		}
		else
		{
			if (PauseZahl == 1)
			{
				PauseZahl = 2;
				spielfeld_pause.setText("Weiter");
			}
			else
			{
				PauseZahl = 1;
				spielfeld_pause.setText("Pause");
			}
		}
	}


	public void spielfeldClicked(ActionEvent event) {

		// Speichere die Id von dem Button, der das Event getriggert hat
		String id = ((Node) event.getSource()).getId();

		// Zerlege die Id: "spielfeld_0_1" -> ["spielfeld", "0", "1"]
		String[] ids = id.split("_");

		try {
			VierGewinnt.reiheSetzen(Integer.parseInt(ids[2]), VierGewinnt.getAktivenSpieler());
		} catch (Exception e) {
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setTitle("Fehler");
			a.setContentText(e.getMessage());
			a.showAndWait().ifPresent(rs -> {
				if (rs == ButtonType.OK) {
					System.out.println("Pressed OK.");
				}
			});
		}
		try {
			Button b = (Button) getClass().getDeclaredField(
					"spielfeld_"
							+ ((VierGewinnt.hoeheVonReihe(Integer.parseInt(ids[2])) - 6) * -1)
							+ "_"
							+ ids[2]
			).get(this);
			b.setStyle("-fx-background-color: " + VierGewinnt.getAktivenSpieler().farbe + ";");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Spieler istGewonnen = VierGewinnt.istGewonnen(Integer.parseInt(ids[2]), (Integer.parseInt(ids[1]) - 5) * (-1));
		if(istGewonnen != null/* || VierGewinnt.muliplayerModus && */) {
			Alert a = new Alert(Alert.AlertType.INFORMATION);
			a.setTitle("Fehler");
			a.setContentText("Spieler " + istGewonnen.name + " hat gewonnen.");
			a.showAndWait().ifPresent(rs -> {
				if (rs == ButtonType.OK) {
					System.out.println("Pressed OK.");
				}
			});

			// TODO: Zurücksetzen auf den Startbildschirm

			return;
		}

		VierGewinnt.spielerWechseln();
	}
}
