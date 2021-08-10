package armaturkin.core;

import armaturkin.reinforcement.PairDR;
import armaturkin.reinforcement.RFClass;
import armaturkin.reinforcement.ReinforcementLiteInfo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class ManuallyEntry {

	private static final String BACKGROUND = "bg";
	private static final int BACKGROUND_LABEL_ID = 0;
	private static Properties colorProperties;

	private Label label;
	private final int summaryLabelID;
	private final ReinforcementLiteInfo reinforcementLiteInfo;

	public static void loadColorProperties() {
		colorProperties = new Properties();
		try {
			InputStream resource = Main.class.getResourceAsStream("/Colors_properties.txt");
			colorProperties.load(resource);
			resource.close();
		} catch (Exception e) {
			Main.log.add(e);
		}
	}

	public static void addManuallySummaryEntry(String summaryLabel, int diameter, RFClass rfClass, String massAsString) {
		add(summaryLabel, diameter, rfClass, massAsString, Main.manuallySummaryEntries);
	}

	public static void addBackgroundReinforcement(PairDR pair, String lengthAsString) {
		add(BACKGROUND, pair.getDiameter(), pair.getRfClass(), lengthAsString, Main.backgroundReinforcementManuallyEntries);
	}

	private static <E> void add(String summaryLabel, int diameter, RFClass rfClass, String massAsStringOrLengthAsString, List<E> list) {
		try {
			double mass = Double.parseDouble(massAsStringOrLengthAsString.replace(",", "."));
			if (mass <= 0.0) {
				throw new Exception(Main.properties.getProperty("negative_number_exception").formatted(mass));
			}
			ManuallyEntry entry = new ManuallyEntry(summaryLabel, diameter, rfClass, mass);
			if (summaryLabel.equals(BACKGROUND)) {
				ManuallyEntryAdaptor adaptor = new ManuallyEntryAdaptor(entry);
				list.add((E) adaptor);
				Main.log.add(Main.properties.getProperty("add_background_manually_entry").formatted(
						ManuallyEntry.class,
						entry.getDiameter(),
						entry.getRfClass(),
						entry.getMass()
				));
			} else {
				list.add((E) entry);
				Main.log.add(Main.properties.getProperty("add_manually_summary_entry").formatted(
						ManuallyEntry.class,
						entry.getSummaryLabelID(),
						entry.getDiameter(),
						entry.getRfClass(),
						entry.getMass()
				));
			}
		} catch (Exception e) {
			Main.log.add(e);
		}
	}

	private static void remove(ManuallyEntry entry) {
		Main.manuallySummaryEntries.remove(entry);
		Main.backgroundReinforcementManuallyEntries.removeIf(v -> v.getManuallyEntry() == entry);
		Main.controller.mSummaryHBoxRemove(entry.getLabel());
		if (entry.getSummaryLabelID() == BACKGROUND_LABEL_ID) {
			Main.log.add(Main.properties.getProperty("remove_background_manually_entry").formatted(
					ManuallyEntry.class,
					entry.getDiameter(),
					entry.getRfClass(),
					entry.getMass()
			));
		} else {
			Main.log.add(Main.properties.getProperty("remove_manually_summary_entry").formatted(
					ManuallyEntry.class,
					entry.getSummaryLabelID(),
					entry.getDiameter(),
					entry.getRfClass(),
					entry.getMass()
			));
		}
	}

	/**
	 * @param summaryLabel {@link java.lang.String} from TextField
	 * @return {@code int} value from 1 to 8 corresponding {@code summaryDropSpace#}.
	 * 0 for valid {@code backgroundReinforcement}.
	 * -1 for invalid {@code summaryDropSpace#}.
	 */
	private static int parseSummaryLabel(String summaryLabel) {
		String[] labels = Main.properties.getProperty("content_row").split("-");
		if (summaryLabel.equals(BACKGROUND)) {
			return BACKGROUND_LABEL_ID;
		}
		for (int i = 0; i < labels.length; i++) {
			if (summaryLabel.equals(labels[i])) {
				return i + 1;
			}
		}
		return -1;
	}

	private ManuallyEntry(String summaryLabel, int diameter, RFClass rfClass, double mass) {
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
		String labelTitle = summaryLabel;
		String lastFieldName = Main.properties.getProperty("last_field_name_mass");
		String colorCode = colorProperties.getProperty("manually_entry");
		if (summaryLabel.equals(BACKGROUND)) {
			labelTitle = Main.properties.getProperty("background_title");
			lastFieldName = Main.properties.getProperty("last_field_name_length");
			colorCode = colorProperties.getProperty("background_entry");
		}
		label.setText(Main.properties.getProperty("manually_summary_entry_label_text").formatted(
				labelTitle,
				diameter,
				RFClass.toString(rfClass),
				lastFieldName,
				mass
		));
		label.setBackground(new Background(new BackgroundFill(Paint.valueOf(colorCode), CornerRadii.EMPTY, Insets.EMPTY)));
		label.setTextAlignment(TextAlignment.CENTER);
		label.setAlignment(Pos.CENTER);
		label.setFont(new Font("System bold", 13));
		label.setTextFill(Paint.valueOf(Main.config.getTextColor()));
		// https://stackoverflow.com/questions/45306039/how-to-write-lambda-expression-with-eventhandler-javafx
		label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			if (event.getButton() == MouseButton.SECONDARY) {
				remove(this);
			}
		});
		Main.controller.mSummaryHBoxAdd(label);
	}
}