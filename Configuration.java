import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Configuration {

	private final String path;
	private String backgroundColor;
	private String textColor;
	private String pathToProductFile;
	private String pathToCalculatingFile;
	private String pathToSummaryCalculatingFile;
	private String borderColor;
	private Boolean boldText;
	private Boolean writeLog;
	private Boolean writeNotification;
	private Integer logStorageLimit;
	private Integer notificationStorageLimit;
	private String favoritePath;
	private Boolean autoParseProductList;

	public Configuration(String path) throws IOException {
		this.path = path;
		defaultValues();
		loadConfigFile();
		setupLog();
	}

	public void loadConfigFile() throws IOException {
		if (Files.exists(Path.of(path))) {
			try {
				List<String> load = Reader.read(path);
				backgroundColor = load.get(0);
				textColor = load.get(1);
				if (!load.get(2).equals("null")) {
					pathToProductFile = load.get(2);
				}
				if (!load.get(3).equals("null")) {
					pathToCalculatingFile = load.get(3);
				}
				if (!load.get(4).equals("null")) {
					pathToSummaryCalculatingFile = load.get(4);
				}
				borderColor = load.get(5);
				boldText = Boolean.parseBoolean(load.get(6));
				writeLog = Boolean.parseBoolean(load.get(7));
				writeNotification = Boolean.parseBoolean(load.get(8));
				logStorageLimit = Integer.parseInt(load.get(9));
				notificationStorageLimit = Integer.parseInt(load.get(10));
				if (!load.get(11).equals("null")) {
					favoritePath = load.get(11);
				}
				autoParseProductList = Boolean.parseBoolean(load.get(12));
			} catch (Exception e) {
				Main.log.add(e);
			}
		}
		if (Files.notExists(Path.of(path))) {
			Files.createFile(Path.of(path));
			saveConfigFile();
		}
	}

	public void saveConfigFile() throws IOException {
		String[] configList = {
				backgroundColor,
				textColor,
				pathToProductFile,
				pathToCalculatingFile,
				pathToSummaryCalculatingFile,
				borderColor,
				String.valueOf(boldText),
				String.valueOf(writeLog),
				String.valueOf(writeNotification),
				String.valueOf(logStorageLimit),
				String.valueOf(notificationStorageLimit),
				favoritePath,
				String.valueOf(autoParseProductList)
		};
		Writer.write(Main.programRootPath + Main.configFileName, configList);
	}

	public void setBackgroundColor(String string) {
		backgroundColor = string;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setTextColor(String string) {
		textColor = string;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setPathToProductFile(String string) {
		pathToProductFile = string;
	}

	public boolean isPathToProductFileNotNull() {
		return pathToProductFile != null;
	}

	public String getPathToProductFile() {
		return pathToProductFile;
	}

	public void setPathToCalculatingFile(String string) {
		pathToCalculatingFile = string;
	}

	public String getPathToCalculatingFile() {
		return pathToCalculatingFile;
	}

	public void setPathToSummaryCalculatingFile(String string) {
		pathToSummaryCalculatingFile = string;
	}

	public String getPathToSummaryCalculatingFile() {
		return pathToSummaryCalculatingFile;
	}

	public void setBorderColor(String string) {
		borderColor = string;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void toggleBoldText() {
		boldText = !boldText;
	}

	public boolean getBoldText() {
		return boldText;
	}

	public void toggleWriteLog() {
		writeLog = !writeLog;
	}

	public boolean getWriteLog() {
		return writeLog;
	}

	public void toggleWriteNotification() {
		writeNotification = !writeNotification;
	}

	public boolean getWriteNotification() {
		return writeNotification;
	}

	public void setLogStorageLimit(int i) {
		logStorageLimit = i;
	}

	public int getLogStorageLimit() {
		return logStorageLimit;
	}

	public void setNotificationStorageLimit(int i) {
		notificationStorageLimit = i;
	}

	public int getNotificationStorageLimit() {
		return notificationStorageLimit;
	}

	public void setFavoritePath(String string) {
		favoritePath = string;
	}

	public String getFavoritePath() {
		return favoritePath;
	}

	public boolean isFavoritePathNotNull() {
		return favoritePath != null;
	}

	public void toggleAutoParseProductList() {
		autoParseProductList = !autoParseProductList;
	}

	public boolean getAutoParseProductList() {
		return autoParseProductList;
	}

	private void defaultValues() {
		backgroundColor = Main.properties.getProperty("backgroundColor");
		textColor = Main.properties.getProperty("textColor");
		borderColor = Main.properties.getProperty("borderColor");
		boldText = Boolean.parseBoolean(Main.properties.getProperty("boldText"));
		writeLog = Boolean.parseBoolean(Main.properties.getProperty("write_log"));
		writeNotification = Boolean.parseBoolean(Main.properties.getProperty("write_notification"));
		logStorageLimit = Integer.parseInt(Main.properties.getProperty("log_storage_limit"));
		notificationStorageLimit = Integer.parseInt(Main.properties.getProperty("notification_storage_limit"));
		autoParseProductList = Boolean.parseBoolean(Main.properties.getProperty("auto_parse_product_list"));
	}

	private void setupLog() {
		if (writeLog) {
			Log.enable();
		}
	}
}