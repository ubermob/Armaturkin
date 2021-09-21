package armaturkin.manuallyentry;

import armaturkin.core.Main;
import armaturkin.interfaces.LightInfo;
import armaturkin.reinforcement.PairDR;
import armaturkin.reinforcement.RFClass;
import armaturkin.reinforcement.ReinforcementLiteInfo;
import armaturkin.steelcomponent.HotRolledSteelType;
import armaturkin.steelcomponent.Image;
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

	private static final Properties COLOR_PROPERTIES = new Properties();

	private final Type type;
	private Label label;
	private final int summaryLabelID;
	private final LightInfo lightInfo;

	public static void loadColorProperties() {
		try (InputStream resource = Main.class.getResourceAsStream("/Colors_properties.txt")) {
			COLOR_PROPERTIES.load(resource);
		} catch (Exception e) {
			Main.log.add(e);
		}
	}

	public static void addManuallySummaryEntry(String summaryLabel, int diameter, RFClass rfClass, String TextFieldString) {
		add(Type.SUMMARY_REINFORCEMENT, summaryLabel, diameter, rfClass, TextFieldString, Main.manuallySummaryEntries);
	}

	public static void addBackgroundReinforcement(PairDR pair, String TextFieldString) {
		add(Type.BACKGROUND_REINFORCEMENT, "", pair.getDiameter(), pair.getRfClass(),
				TextFieldString, Main.backgroundReinforcementManuallyEntries);
	}

	public static void addSteelComponentEntry(Image image, String textFieldString) {
		try {
			double length = checkTextFieldParse(textFieldString);
			ManuallyEntry entry = getNewManuallyEntryOfHotRolledSteel(
					Type.SUMMARY_HOT_ROLLED_STEEL,
					image,
					length
			);
			Main.manuallySummaryEntries.add(entry);
			Main.log.add(Main.properties.getProperty("add_manually_summary_hot_rolled_steel_entry").formatted(
					ManuallyEntry.class,
					image.getType(),
					image.toString(),
					length
			));
		} catch (Exception e) {
			Main.log.add(e);
		}
	}

	private static <E> void add(Type type, String summaryLabel, int diameter,
	                            RFClass rfClass, String textFieldString, List<E> list) {
		try {
			double massOrLength = checkTextFieldParse(textFieldString);
			if (type == Type.BACKGROUND_REINFORCEMENT) {
				ManuallyEntry entry = getNewManuallyEntryOfReinforcement(
						Type.BACKGROUND_REINFORCEMENT,
						summaryLabel,
						diameter,
						rfClass,
						massOrLength
				);
				ManuallyEntryAdaptor adaptor = new ManuallyEntryAdaptor(entry);
				list.add((E) adaptor);
				Main.log.add(Main.properties.getProperty("add_background_manually_entry").formatted(
						ManuallyEntry.class,
						entry.getDiameter(),
						entry.getRfClass(),
						entry.getMassReinforcement()
				));
			} else if (type == Type.SUMMARY_REINFORCEMENT) {
				ManuallyEntry entry = getNewManuallyEntryOfReinforcement(
						Type.SUMMARY_REINFORCEMENT,
						summaryLabel,
						diameter,
						rfClass,
						massOrLength
				);
				list.add((E) entry);
				Main.log.add(Main.properties.getProperty("add_manually_summary_entry").formatted(
						ManuallyEntry.class,
						entry.getSummaryLabelID(),
						entry.getDiameter(),
						entry.getRfClass(),
						entry.getMassReinforcement()
				));
			}
		} catch (Exception e) {
			Main.log.add(e);
		}
	}

	private static double checkTextFieldParse(String textFieldString) throws Exception {
		double parsed = Double.parseDouble(textFieldString.replace(",", "."));
		if (parsed <= 0.0) {
			throw new Exception(Main.properties.getProperty("negative_number_exception").formatted(parsed));
		}
		return parsed;
	}

	private static void remove(ManuallyEntry entry) {
		Main.manuallySummaryEntries.remove(entry);
		Main.backgroundReinforcementManuallyEntries.removeIf(v -> v.getManuallyEntry() == entry);
		Main.controller.mSummaryHBoxRemove(entry.getLabel());
		if (entry.getType() == Type.BACKGROUND_REINFORCEMENT) {
			Main.log.add(Main.properties.getProperty("remove_background_manually_entry").formatted(
					ManuallyEntry.class,
					entry.getDiameter(),
					entry.getRfClass(),
					entry.getMassReinforcement()
			));
		} else if (entry.getType() == Type.SUMMARY_REINFORCEMENT) {
			Main.log.add(Main.properties.getProperty("remove_manually_summary_entry").formatted(
					ManuallyEntry.class,
					entry.getSummaryLabelID(),
					entry.getDiameter(),
					entry.getRfClass(),
					entry.getMassReinforcement()
			));
		} else if (entry.getType() == Type.SUMMARY_HOT_ROLLED_STEEL) {
			Main.log.add(Main.properties.getProperty("remove_manually_summary_hot_rolled_steel_entry").formatted(
					ManuallyEntry.class,
					entry.getHotRolledSteelType(),
					entry.getImageAsString(),
					entry.getMassHotRolledSteel()
			));
		}
	}

	private static ManuallyEntry getNewManuallyEntryOfReinforcement(
			Type type,
			String summaryLabel,
			int diameter,
			RFClass rfClass,
			double mass) {
		return new ManuallyEntry(type, summaryLabel, diameter, rfClass, mass);
	}

	private static ManuallyEntry getNewManuallyEntryOfHotRolledSteel(
			Type type,
			Image image,
			double length) {
		return new ManuallyEntry(type, image, length);
	}

	// getNewManuallyEntryOfReinforcement() fabric
	private ManuallyEntry(Type type, String summaryLabel, int diameter, RFClass rfClass, double mass) {
		this.type = type;
		buildLabelReinforcement(summaryLabel, diameter, rfClass, mass);
		summaryLabelID = parseSummaryLabel(summaryLabel);
		lightInfo = new ReinforcementLiteInfo(diameter, rfClass, mass);
	}

	// getNewManuallyEntryOfHotRolledSteel() fabric
	private ManuallyEntry(Type type, Image image, double length) {
		this.type = type;
		buildLabelHotRolledSteel(image, length);
		summaryLabelID = parseSummaryLabel("");
		image.setMass(length);
		lightInfo = image;
	}

	/**
	 * @param summaryLabel {@link java.lang.String} from TextField
	 * @return {@code int} value from 1 to 8 corresponding {@code summaryDropSpace#}.
	 * -1 for invalid {@code summaryDropSpace#}.
	 */
	private int parseSummaryLabel(String summaryLabel) {
		String[] labels = Main.properties.getProperty("content_row").split("-");
		if (type == Type.SUMMARY_REINFORCEMENT) {
			for (int i = 0; i < labels.length; i++) {
				if (summaryLabel.equals(labels[i])) {
					return i + 1;
				}
			}
		}
		return -1;
	}

	public Label getLabel() {
		return label;
	}

	public int getSummaryLabelID() {
		return summaryLabelID;
	}

	public int getDiameter() {
		return ((ReinforcementLiteInfo) lightInfo).getDiameter();
	}

	public RFClass getRfClass() {
		return ((ReinforcementLiteInfo) lightInfo).getRfClass();
	}

	public double getMassReinforcement() { return ((ReinforcementLiteInfo) lightInfo).getMass(); }

	public Type getType() {
		return type;
	}

	public HotRolledSteelType getHotRolledSteelType() {
		return ((Image) lightInfo).getType();
	}

	public String getImageAsString() {
		return lightInfo.toString();
	}

	public double getMassHotRolledSteel() {
		return ((Image) lightInfo).getMass();
	}

	private void buildLabelReinforcement(String summaryLabel, int diameter, RFClass rfClass, double mass) {
		String labelTitle = "";
		String lastFieldName = "";
		String labelColorCode = "";
		if (type == Type.SUMMARY_REINFORCEMENT) {
			labelTitle = summaryLabel;
			lastFieldName = Main.properties.getProperty("last_field_name_mass");
			labelColorCode = COLOR_PROPERTIES.getProperty("manually_entry");
		} else if (type == Type.BACKGROUND_REINFORCEMENT) {
			labelTitle = Main.properties.getProperty("background_title");
			lastFieldName = Main.properties.getProperty("last_field_name_length");
			labelColorCode = COLOR_PROPERTIES.getProperty("background_entry");
		}
		String labelText = Main.properties.getProperty("manually_summary_entry_label_text").formatted(
				labelTitle,
				diameter,
				RFClass.toString(rfClass),
				lastFieldName,
				mass
		);
		buildLabel(labelText, mass, labelColorCode);
	}

	private void buildLabelHotRolledSteel(Image image, double length) {
		String labelTitle = Main.properties.getProperty("hot_rolled_steel_title");
		String lastFieldName = Main.properties.getProperty("last_field_name_length");
		String labelColorCode = COLOR_PROPERTIES.getProperty("manually_hot_rolled_steel_entry");
		String labelText = Main.properties.getProperty("manually_summary_entry_hot_rolled_steel_label_text").formatted(
				labelTitle,
				image,
				lastFieldName,
				length
		);
		buildLabel(labelText, length, labelColorCode);
	}

	// TODO: Do create a fxml card?
	private void buildLabel(String labelText, double mass, String labelColorCode) {
		label = new Label();
		label.setPrefWidth(100);
		label.setPrefHeight(Main.controller.getMSummaryHBoxPrefHeight());
		label.setText(labelText);
		label.setBackground(new Background(new BackgroundFill(Paint.valueOf(labelColorCode), CornerRadii.EMPTY, Insets.EMPTY)));
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

	private enum Type {
		SUMMARY_REINFORCEMENT, BACKGROUND_REINFORCEMENT, SUMMARY_HOT_ROLLED_STEEL
	}
}