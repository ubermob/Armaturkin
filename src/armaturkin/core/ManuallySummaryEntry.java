package armaturkin.core;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ManuallySummaryEntry {

	private Label label;
	private final int summaryLabelID;
	private final ReinforcementLiteInfo reinforcementLiteInfo;

	public static void add(String summaryLabel, Integer diameter, RFClass rfClass, String massAsString) {
		try {
			double mass = Double.parseDouble(massAsString);
			if (mass <= 0.0) {
				throw new Exception(Main.properties.getProperty("negative_mass_exception").formatted(mass));
			}
			ManuallySummaryEntry entry = new ManuallySummaryEntry(summaryLabel, diameter, rfClass, mass);
			Main.manuallySummaryEntries.add(entry);
			Main.log.add(Main.properties.getProperty("add_manually_summary_entry").formatted(
					ManuallySummaryEntry.class,
					entry.getSummaryLabelID(),
					entry.getDiameter(),
					entry.getRfClass(),
					entry.getMass()
			));
		} catch (Exception e) {
			Main.log.add(e);
		}
	}

	public static void remove(ManuallySummaryEntry entry) {
		Main.manuallySummaryEntries.remove(entry);
		Main.controller.mSummaryHBoxRemove(entry.getLabel());
		Main.log.add(Main.properties.getProperty("remove_manually_summary_entry").formatted(
				ManuallySummaryEntry.class,
				entry.getSummaryLabelID(),
				entry.getDiameter(),
				entry.getRfClass(),
				entry.getMass()
		));
	}

	private static int parseSummaryLabel(String summaryLabel) {
		String[] labels = Main.properties.getProperty("content_row").split("-");
		for (int i = 0; i < labels.length; i++) {
			if (summaryLabel.equals(labels[i])) {
				return i + 1;
			}
		}
		return -1;
	}

	private ManuallySummaryEntry(String summaryLabel, int diameter, RFClass rfClass, double mass) {
		buildLabel(summaryLabel, diameter, rfClass, mass);
		summaryLabelID = parseSummaryLabel(summaryLabel);
		reinforcementLiteInfo = new ReinforcementLiteInfo(diameter, rfClass, mass);
	}

	public Label getLabel() {
		return label;
	}

	public int getSummaryLabelID() {
		return summaryLabelID;
	}

	public int getDiameter() {
		return reinforcementLiteInfo.getDiameter();
	}

	public RFClass getRfClass() {
		return reinforcementLiteInfo.getRfClass();
	}

	public double getMass() {
		return reinforcementLiteInfo.getMass();
	}

	// TODO: Do create a fxml card?
	private void buildLabel(String summaryLabel, int diameter, RFClass rfClass, double mass) {
		label = new Label();
		label.setPrefWidth(100);
		label.setPrefHeight(Main.controller.getMSummaryHBoxPrefHeight());
		label.setText(Main.properties.getProperty("manually_summary_entry_label_text").formatted(
				summaryLabel,
				diameter,
				RFClass.toString(rfClass),
				mass
		));
		label.setBackground(Main.controller.getBackgroundSample());
		label.setTextAlignment(Main.controller.getTextAlignmentSample());
		label.setAlignment(Pos.CENTER);
		// https://stackoverflow.com/questions/45306039/how-to-write-lambda-expression-with-eventhandler-javafx
		label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			if (event.getButton() == MouseButton.SECONDARY) {
				remove(this);
			}
		});
		Main.controller.mSummaryHBoxAdd(label);
	}
}