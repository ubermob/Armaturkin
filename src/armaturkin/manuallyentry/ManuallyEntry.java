package armaturkin.manuallyentry;

import armaturkin.core.Main;
import armaturkin.interfaces.LightInfo;
import armaturkin.reinforcement.PairDR;
import armaturkin.reinforcement.RFClass;
import armaturkin.reinforcement.ReinforcementLiteInfo;
import armaturkin.steelcomponent.HotRolledSteelType;
import armaturkin.steelcomponent.Image;
import armaturkin.steelcomponent.SteelComponentRepository;
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

	private static final Properties COLOR_PROPERTIES;

	static {
		COLOR_PROPERTIES = new Properties();
		try (InputStream resource = Main.class.getResourceAsStream("/Colors_properties.txt")) {
			COLOR_PROPERTIES.load(resource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final Type type;
	private Label label;
	private final int summaryLabelID;
	private final LightInfo lightInfo;
	private final double parsedValue;

	public static void addManuallySummaryEntry(String summaryLabel, int diameter
			, RFClass rfClass, String TextFieldString) {
		add(
				Type.SUMMARY_REINFORCEMENT
				, summaryLabel
				, diameter
				, rfClass
				, TextFieldString
				, Main.app.getManuallyEntryModel().getManuallySummaryEntries()
		);
	}

	public static void addBackgroundReinforcement(PairDR pair, String TextFieldString) {
		add(
				Type.BACKGROUND_REINFORCEMENT
				, ""
				, pair.getDiameter()
				, pair.getRfClass()
				, TextFieldString
				, Main.app.getManuallyEntryModel().getBackgroundReinforcementManuallyEntries()
		);
	}

	public static void addSteelComponentEntry(String summaryLabel, Image image, String textFieldString) {
		try {
			double parsedValue = parseIfNotNegative(textFieldString);
			ManuallyEntry entry = getNewManuallyEntryOfAngleSteel(
					image,
					parsedValue,
					summaryLabel
			);
			var manuallySummaryEntries = Main.app.getManuallyEntryModel().getManuallySummaryEntries();
			manuallySummaryEntries.add(entry);
			Main.app.log(Main.app.getProperty("add_manually_summary_hot_rolled_steel_entry").formatted(
					ManuallyEntry.class,
					image.getHotRolledSteelType(),
					image.toString(),
					parsedValue
			));
		} catch (Exception e) {
			Main.app.log(e);
		}
	}

	public static void addSteelSheet(String summaryLabel, Number thickness, Number width, String textFieldString) {
		try {
			double parsedValue = parseIfNotNegative(textFieldString);
			ManuallyEntry entry = getNewManuallyEntryOfSheet(thickness, width, parsedValue, summaryLabel);
			var manuallySummaryEntries = Main.app.getManuallyEntryModel().getManuallySummaryEntries();
			manuallySummaryEntries.add(entry);
			Main.app.log(Main.app.getProperty("add_manually_summary_hot_rolled_steel_entry").formatted(
					ManuallyEntry.class,
					entry.getImageType(),
					entry.getImageToString(),
					parsedValue
			));
		} catch (Exception e) {
			Main.app.log(e);
		}
	}

	private static void add(Type type, String summaryLabel, int diameter,
	                        RFClass rfClass, String textFieldString, List<ManuallyEntry> list) {
		try {
			double parsedValue = parseIfNotNegative(textFieldString);
			if (type == Type.BACKGROUND_REINFORCEMENT) {
				ManuallyEntry entry = getNewManuallyEntryOfReinforcement(
						Type.BACKGROUND_REINFORCEMENT,
						summaryLabel,
						diameter,
						rfClass,
						parsedValue
				);
				list.add(entry);
				Main.app.log(Main.app.getProperty("add_background_manually_entry").formatted(
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
						parsedValue
				);
				list.add(entry);
				Main.app.log(Main.app.getProperty("add_manually_summary_entry").formatted(
						ManuallyEntry.class,
						entry.getSummaryLabelID(),
						entry.getDiameter(),
						entry.getRfClass(),
						entry.getMassReinforcement()
				));
			}
		} catch (Exception e) {
			Main.app.log(e);
		}
	}

	private static double parseIfNotNegative(String textFieldString) throws Exception {
		double parsed = Double.parseDouble(textFieldString.replace(",", "."));
		if (parsed <= 0.0) {
			throw new Exception(Main.app.getProperty("negative_number_exception").formatted(parsed));
		}
		return parsed;
	}

	private static void remove(ManuallyEntry entry) {
		var manuallySummaryEntries = Main.app.getManuallyEntryModel().getManuallySummaryEntries();
		manuallySummaryEntries.remove(entry);
		var backgroundReinforcementManuallyEntries = Main.app
				.getManuallyEntryModel().getBackgroundReinforcementManuallyEntries();
		backgroundReinforcementManuallyEntries.remove(entry);
		Main.app.getController().mSummaryHBoxRemove(entry.getLabel());
		if (entry.getType() == Type.BACKGROUND_REINFORCEMENT) {
			Main.app.log(Main.app.getProperty("remove_background_manually_entry").formatted(
					ManuallyEntry.class,
					entry.getDiameter(),
					entry.getRfClass(),
					entry.getMassReinforcement()
			));
		} else if (entry.getType() == Type.SUMMARY_REINFORCEMENT) {
			Main.app.log(Main.app.getProperty("remove_manually_summary_entry").formatted(
					ManuallyEntry.class,
					entry.getSummaryLabelID(),
					entry.getDiameter(),
					entry.getRfClass(),
					entry.getMassReinforcement()
			));
		} else if (entry.getType() == Type.SUMMARY_HOT_ROLLED_STEEL) {
			Main.app.log(Main.app.getProperty("remove_manually_summary_hot_rolled_steel_entry").formatted(
					ManuallyEntry.class,
					entry.getHotRolledSteelType(),
					entry.getImageAsString(),
					entry.getParsedValue()
			));
		}
	}

	private static ManuallyEntry getNewManuallyEntryOfReinforcement(
			Type type,
			String summaryLabel,
			int diameter,
			RFClass rfClass,
			double parsedValue) {
		return new ManuallyEntry(type, summaryLabel, diameter, rfClass, parsedValue, parsedValue);
	}

	private static ManuallyEntry getNewManuallyEntryOfAngleSteel(Image image, double parsedValue, String summaryLabel) {
		return new ManuallyEntry(
				image,
				parsedValue,
				parsedValue / 1000 * SteelComponentRepository.getAngleMassPerUnitLength(image),
				summaryLabel
		);
	}

	private static ManuallyEntry getNewManuallyEntryOfSheet(Number thickness, Number width
			, double parsedValue, String summaryLabel) {
		return new ManuallyEntry(
				parsedValue,
				width,
				thickness,
				thickness.intValue() * width.intValue() * parsedValue * SteelComponentRepository.getSteelDensity(),
				summaryLabel
		);
	}

	// Non static

	// Reinforcement fabric
	private ManuallyEntry(Type type, String summaryLabel, int diameter, RFClass rfClass,
	                      double parsedValue, double mass) {
		this.type = type;
		buildLabelReinforcement(summaryLabel, diameter, rfClass, parsedValue);
		summaryLabelID = parseSummaryLabel(summaryLabel);
		lightInfo = new ReinforcementLiteInfo(diameter, rfClass, mass);
		this.parsedValue = parsedValue;
	}

	// Angle fabric
	private ManuallyEntry(Image image, double parsedValue, double mass, String summaryLabel) {
		this.type = Type.SUMMARY_HOT_ROLLED_STEEL;
		buildLabelHotRolledSteel(image, parsedValue, summaryLabel);
		summaryLabelID = parseSummaryLabel(summaryLabel);
		lightInfo = image.getClone();
		((Image) lightInfo).setMass(mass);
		this.parsedValue = parsedValue;
	}

	// Sheet fabric
	private ManuallyEntry(double parsedValue, Number width, Number thickness, double mass, String summaryLabel) {
		type = Type.SUMMARY_HOT_ROLLED_STEEL;
		lightInfo = new Image(parsedValue, width, thickness, mass);
		buildLabelHotRolledSteel((Image) lightInfo, parsedValue, summaryLabel);
		summaryLabelID = parseSummaryLabel(summaryLabel);
		this.parsedValue = parsedValue;
	}

	/**
	 * @param summaryLabel {@link java.lang.String} from TextField
	 * @return for default values: {@code int} value from 1 to 8 corresponding {@code summaryDropSpace#}.
	 * Value from 1 for user summary builder rows.
	 * -1 for invalid {@code summaryDropSpace#}.
	 */
	private int parseSummaryLabel(String summaryLabel) {
		String[] labels;
		if (Main.app.getSummaryModel().isSummaryBuilderListNotNull()) {
			labels = (String[]) Main.app.getSummaryModel().getUserContentRowList().toArray();
		} else {
			// default value
			labels = Main.app.getProperty("default_content_row").split("-");
		}
		if (type != Type.BACKGROUND_REINFORCEMENT) {
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

	public double getMassReinforcement() {
		return ((ReinforcementLiteInfo) lightInfo).getMass();
	}

	public Type getType() {
		return type;
	}

	public HotRolledSteelType getHotRolledSteelType() {
		return ((Image) lightInfo).getHotRolledSteelType();
	}

	public String getImageAsString() {
		return lightInfo.toString();
	}

	public double getMassHotRolledSteel() {
		return ((Image) lightInfo).getMass();
	}

	public double getParsedValue() {
		return parsedValue;
	}

	public HotRolledSteelType getImageType() {
		return ((Image) lightInfo).getHotRolledSteelType();
	}

	public String getImageToString() {
		return ((Image) lightInfo).toString();
	}

	public Image getLightInfoCastingToImage() {
		return (Image) lightInfo;
	}

	public boolean isReinforcement() {
		return lightInfo instanceof ReinforcementLiteInfo;
	}

	public boolean isAngle() {
		if (isHotRolledSteelAndInstanceOfImage()) {
			return ((Image) lightInfo).getHotRolledSteelType() != HotRolledSteelType.SHEET;
		}
		return false;
	}

	public boolean isSheet() {
		if (isHotRolledSteelAndInstanceOfImage()) {
			return ((Image) lightInfo).getHotRolledSteelType() == HotRolledSteelType.SHEET;
		}
		return false;
	}

	private boolean isHotRolledSteelAndInstanceOfImage() {
		return type == Type.SUMMARY_HOT_ROLLED_STEEL && lightInfo instanceof Image;
	}

	private void buildLabelReinforcement(String summaryLabel, int diameter, RFClass rfClass, double parsedValue) {
		String labelTitle = "";
		String lastFieldName = "";
		String labelColorCode = "";
		if (type == Type.SUMMARY_REINFORCEMENT) {
			labelTitle = summaryLabel;
			lastFieldName = Main.app.getProperty("last_field_name_mass");
			labelColorCode = COLOR_PROPERTIES.getProperty("manually_entry");
		} else if (type == Type.BACKGROUND_REINFORCEMENT) {
			labelTitle = Main.app.getProperty("background_title");
			lastFieldName = Main.app.getProperty("last_field_name_length");
			labelColorCode = COLOR_PROPERTIES.getProperty("background_entry");
		}
		String labelText = Main.app.getProperty("manually_summary_entry_label_text").formatted(
				labelTitle,
				diameter,
				RFClass.toString(rfClass),
				lastFieldName,
				parsedValue
		);
		buildLabel(labelText, labelColorCode);
	}

	private void buildLabelHotRolledSteel(Image image, double length, String summaryLabel) {
		String labelTitle = Main.app.getProperty("hot_rolled_steel_title") + "\n" + summaryLabel;
		String lastFieldName = Main.app.getProperty("last_field_name_length");
		String labelColorCode = COLOR_PROPERTIES.getProperty("manually_hot_rolled_steel_entry");
		String labelText = Main.app.getProperty("manually_summary_entry_hot_rolled_steel_label_text").formatted(
				labelTitle,
				image,
				lastFieldName,
				length
		);
		buildLabel(labelText, labelColorCode);
	}

	private void buildLabel(String labelText, String labelColorCode) {
		label = new Label(labelText);
		label.setPrefWidth(100);
		label.setPrefHeight(Main.app.getController().getMSummaryHBoxPrefHeight());
		label.setBackground(new Background(new BackgroundFill(
				Paint.valueOf(labelColorCode), CornerRadii.EMPTY, Insets.EMPTY
		)));
		label.setTextAlignment(TextAlignment.CENTER);
		label.setAlignment(Pos.CENTER);
		label.setFont(new Font("System bold", 13));
		label.setTextFill(Paint.valueOf(Main.app.getConfig().getTextColor()));
		// https://stackoverflow.com/questions/45306039/how-to-write-lambda-expression-with-eventhandler-javafx
		label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			if (event.getButton() == MouseButton.SECONDARY) {
				remove(this);
			}
		});
		Main.app.getController().mSummaryHBoxAdd(label);
	}

	private enum Type {
		SUMMARY_REINFORCEMENT, BACKGROUND_REINFORCEMENT, SUMMARY_HOT_ROLLED_STEEL
	}
}