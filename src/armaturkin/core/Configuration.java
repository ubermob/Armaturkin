package armaturkin.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

/**
 * Contains user settings
 */
public class Configuration {

	private final String path;
	private final Properties properties;
	private final App app;
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
	private Integer resultLabelFontSize;
	private Boolean pythonInterpreter;
	private Double infoStageHeight;
	private Double infoStageWidth;
	private Boolean isContentContainerBorderLoggable;
	private Boolean isContentContainerLoggable;
	private Boolean isContentContainerCompactLoggable;
	private Boolean isAllowNegativeMassForManuallySummary;

	public Configuration(String path, Properties properties, App app) throws IOException {
		this.path = path;
		this.properties = properties;
		this.app = app;
		defaultValues();
		loadConfigFile();
		setupLog();
	}

	public void loadConfigFile() throws IOException {
		if (Files.exists(Path.of(path))) {
			try {
				List<String> load = Reader.readFromExternalSource(path);
				backgroundColor = load.get(0);
				textColor = load.get(1);
				pathToProductFile = parseStringIfNotNull(load.get(2));
				pathToCalculatingFile = parseStringIfNotNull(load.get(3));
				pathToSummaryCalculatingFile = parseStringIfNotNull(load.get(4));
				borderColor = load.get(5);
				boldText = Boolean.parseBoolean(load.get(6));
				writeLog = Boolean.parseBoolean(load.get(7));
				writeNotification = Boolean.parseBoolean(load.get(8));
				logStorageLimit = Integer.parseInt(load.get(9));
				notificationStorageLimit = Integer.parseInt(load.get(10));
				favoritePath = parseStringIfNotNull(load.get(11));
				autoParseProductList = Boolean.parseBoolean(load.get(12));
				resultLabelFontSize = parseIntegerIfNotNull(load.get(13));
				pythonInterpreter = Boolean.parseBoolean(load.get(14));
				infoStageHeight = parseDoubleIfNotNull(load.get(15));
				infoStageWidth = parseDoubleIfNotNull(load.get(16));
				isContentContainerBorderLoggable = Boolean.parseBoolean(load.get(17));
				isContentContainerLoggable = Boolean.parseBoolean(load.get(18));
				isContentContainerCompactLoggable = Boolean.parseBoolean(load.get(19));
				isAllowNegativeMassForManuallySummary = Boolean.parseBoolean(load.get(20));
			} catch (IndexOutOfBoundsException e) {
				app.log("IndexOutOfBoundsException possible reason: extends config in new application version");
				app.log(e);
			} catch (Exception e) {
				app.log(e);
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
				String.valueOf(autoParseProductList),
				String.valueOf(resultLabelFontSize),
				String.valueOf(pythonInterpreter),
				String.valueOf(infoStageHeight),
				String.valueOf(infoStageWidth),
				String.valueOf(isContentContainerBorderLoggable),
				String.valueOf(isContentContainerLoggable),
				String.valueOf(isContentContainerCompactLoggable),
				String.valueOf(isAllowNegativeMassForManuallySummary)
		};
		Writer.write(Root.programRootPath + Root.getProperty("config_file_name"), configList);
	}

	public void setBackgroundColor(String string) {
		backgroundColor = string;
	}

	/**
	 * @return HEX code {@link String} of background
	 */
	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setTextColor(String string) {
		textColor = string;
	}

	/**
	 * @return HEX code {@link String} of text
	 */
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

	public Integer getResultLabelFontSize() {
		return resultLabelFontSize;
	}

	public void setResultLabelFontSize(int resultLabelFontSize) {
		this.resultLabelFontSize = resultLabelFontSize;
	}

	public boolean isResultLabelFontSizeNotNull() {
		return resultLabelFontSize != null;
	}

	public Boolean getPythonInterpreter() {
		return pythonInterpreter;
	}

	public void setPythonInterpreter(Boolean pythonInterpreter) {
		this.pythonInterpreter = pythonInterpreter;
	}

	public Double getInfoStageHeight() {
		return infoStageHeight;
	}

	public void setInfoStageHeight(Double infoStageHeight) {
		this.infoStageHeight = infoStageHeight;
	}

	public Double getInfoStageWidth() {
		return infoStageWidth;
	}

	public void setInfoStageWidth(Double infoStageWidth) {
		this.infoStageWidth = infoStageWidth;
	}

	public Boolean getContentContainerBorderLoggable() {
		return isContentContainerBorderLoggable;
	}

	public void toggleContentContainerBorderLoggable() {
		isContentContainerBorderLoggable = !isContentContainerBorderLoggable;
	}

	public Boolean getContentContainerLoggable() {
		return isContentContainerLoggable;
	}

	public void toggleContentContainerLoggable() {
		isContentContainerLoggable = !isContentContainerLoggable;
	}

	public Boolean getContentContainerCompactLoggable() {
		return isContentContainerCompactLoggable;
	}

	public void toggleContentContainerCompactLoggable() {
		isContentContainerCompactLoggable = !isContentContainerCompactLoggable;
	}

	public Boolean getAllowNegativeMassForManuallySummary() {
		return isAllowNegativeMassForManuallySummary;
	}

	public void toggleAllowNegativeMassForManuallySummary() {
		isAllowNegativeMassForManuallySummary = !isAllowNegativeMassForManuallySummary;
	}

	private void defaultValues() {
		backgroundColor = properties.getProperty("background_color");
		textColor = properties.getProperty("text_color");
		borderColor = properties.getProperty("border_color");
		boldText = Boolean.parseBoolean(properties.getProperty("bold_text"));
		writeLog = Boolean.parseBoolean(properties.getProperty("write_log"));
		writeNotification = Boolean.parseBoolean(properties.getProperty("write_notification"));
		logStorageLimit = Integer.parseInt(properties.getProperty("log_storage_limit"));
		notificationStorageLimit = Integer.parseInt(properties.getProperty("notification_storage_limit"));
		autoParseProductList = Boolean.parseBoolean(properties.getProperty("auto_parse_product_list"));
		pythonInterpreter = Boolean.parseBoolean(properties.getProperty("python_interpreter"));
		boolean ccl = Boolean.parseBoolean(properties.getProperty("content_container_loggable"));
		isContentContainerBorderLoggable = ccl;
		isContentContainerLoggable = ccl;
		isContentContainerCompactLoggable = ccl;
		isAllowNegativeMassForManuallySummary = Boolean.parseBoolean(
				properties.getProperty("allow_negative_mass_for_manually_summary")
		);
	}

	private void setupLog() {
		if (writeLog) {
			LogManager.enable();
		}
	}

	private String parseStringIfNotNull(String string) {
		return string.equals("null") ? null : string;
	}

	private Integer parseIntegerIfNotNull(String string) {
		return string.equals("null") ? null : Integer.parseInt(string);
	}

	private Double parseDoubleIfNotNull(String string) {
		return string.equals("null") ? null : Double.parseDouble(string);
	}
}