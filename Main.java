import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class Main extends Application {

	public static String version = "0.4.1";
	public static Properties properties;
	public static Parent root;
    public static Controller controller;
    public static String programRootPath;
    public static String configFileName;
    public static String logFileName;
	public static String notificationFileName;
	public static String logStorageDirectory;
    public static String backgroundColor;
    public static String textColor;
    public static String borderColor;
    public static boolean boldText;
    private static Character diskLetter;
    private static volatile String notificationString = "";

    public volatile static String pathToProductFile;
    public volatile static String pathToCalculatingFile;
    public static String optionalPath;
	public static HashMap<Integer, List<String>> summaryPaths = new HashMap<>();

	public volatile static HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap = new HashMap<>();
	public volatile static HashMap<Integer, Reinforcement> reinforcementHashMap = new HashMap<>();

	public static Log log = new Log();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(properties.getProperty("mainScene")));
        root = loader.load();
        controller = loader.getController();
        controller.startSetup();
        primaryStage.setTitle(properties.getProperty("applicationName") + " ver " + version);
        log.add(properties.getProperty("applicationMainLine").formatted(primaryStage.getTitle(), getDate(), getTime(), getHostName()));
	    primaryStage.getIcons().add(new Image(Files.newInputStream(Path.of("resources\\Icon.png"))));
        primaryStage.setScene(new Scene(root));
        preloadUpperDropSpace();
        primaryStage.show();
	    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000 / 60.0), actionEvent -> {
		    controller.setResultLabelText(notificationString);
	    }));
	    timeline.setCycleCount(Animation.INDEFINITE);
	    timeline.play();
    }

    public static void main(String[] args) throws IOException {
	    try {
		    parseArgs(args);
	    } catch (ArrayIndexOutOfBoundsException e) {
		    log.add(e);
	    }
	    loadProperties();
	    readBasicFieldsFromProperties();
    	checkDirectory();
    	try {
		    loadConfigFile();
	    } catch (Exception e) {
		    log.add(e);
	    }
        launch(args);
        saveConfigFile();
        Log.saveLog();
    }

    static void loadProduct() {
    	reinforcementProductHashMap.clear();
	    ProductFileWorker productFileWorker = new ProductFileWorker(pathToProductFile, reinforcementProductHashMap);
	    Thread productFileWorkerThread = new Thread(productFileWorker);
	    productFileWorkerThread.start();
    }

    static void loadCalculatingFile() {
    	reinforcementHashMap.clear();
		CalculatingFileWorker calculatingFileWorker = new CalculatingFileWorker(pathToCalculatingFile,
				reinforcementHashMap,
				reinforcementProductHashMap
		);
		Thread calculatingFileWorkerThread = new Thread(calculatingFileWorker);
		calculatingFileWorkerThread.start();
    }

    static void downloadCalculatedFile() {
    	if (!reinforcementHashMap.isEmpty() && !reinforcementProductHashMap.isEmpty()) {
    		FileWorker fileWorker = new FileWorker(pathToCalculatingFile,
				    reinforcementHashMap,
				    controller.getBackgroundReinforcement(),
				    controller.getTableHead(),
				    controller.getFileName()
		    );
    		Thread fileWorkerThread = new Thread(fileWorker);
    		fileWorkerThread.start();
	    }
    }

    static void downloadSummaryFile() {
    	if (!summaryPaths.isEmpty()) {
    		SummaryHub summaryHub = new SummaryHub(summaryPaths, controller.getSummaryTableHead(), controller.getSummaryFileName());
    		Thread summaryHubThread = new Thread(summaryHub);
    		summaryHubThread.start();
	    }
    }

    static void downloadNotification() throws IOException {
    	String[] string = new String[1];
    	string[0] = notificationString;
    	Writer.write(programRootPath + notificationFileName, string);
    }

    static void parseArgs(String[] args) {
    	String[] argCommand = {"-writeLog", "-logStorageLimit", "-disk"};
	    for (String arg : args) {
		    if (arg.equals(argCommand[0])) {
			    Log.enable();
			    log.add(argCommand[0]);
		    }
		    if (isMatchCommands(arg, argCommand[1])) {
			    int value = Integer.parseInt(arg.split("=")[1]);
			    Log.setLogStorageLimit(value);
			    log.add(argCommand[1] + " " + value);
		    }
		    if (isMatchCommands(arg, argCommand[2])) {
		    	char diskLetter = arg.split("=")[1].charAt(0);
		    	char letterC = 'C';
		    	char letterZ = 'Z';
		    	if (letterC <= diskLetter && diskLetter <= letterZ) {
		    		Main.diskLetter = diskLetter;
				    log.add(argCommand[2] + " " + Main.diskLetter);
			    }
		    }
	    }
    }

    static boolean isMatchCommands(String arg, String argCommand) {
    	return arg.length() >= argCommand.length() && arg.startsWith(argCommand);
    }

	private void preloadUpperDropSpace() {
		if (pathToProductFile != null) {
			Path path = Path.of(pathToProductFile);
			if (Files.exists(path)) {
				controller.setUpperDropSpaceText(
						properties.getProperty("upperLabelTextWithFile").formatted(path.getFileName().toString())
				);
				loadProduct();
			}
		}
	}

    public static void addNotification(String string) {
    	notificationString += string + "\n";
    	controller.setNotificationOpacity(1);
    }

    public static void clearNotification() {
    	notificationString = "";
    }

    static void checkDirectory() throws IOException {
    	if (diskLetter != null) {
    		Main.programRootPath = diskLetter + Main.programRootPath.substring(1);
	    }
    	if (Files.notExists(Path.of(programRootPath))) {
    		Files.createDirectory(Path.of(programRootPath));
	    }
    	if (Files.notExists(Path.of(programRootPath, logStorageDirectory))) {
    		Files.createDirectory(Path.of(programRootPath, logStorageDirectory));
	    }
    }

    static void checkSummaryDropSpace(int i) {
    	// work in progress
    }

	static void loadConfigFile() throws IOException {
		if (Files.exists(Path.of(programRootPath, configFileName))) {
			List<String> load = Reader.read(programRootPath + configFileName);
			backgroundColor = load.get(0);
			textColor = load.get(1);
			if (!load.get(2).equals("null")) {
				pathToProductFile = load.get(2);
			}
			if (!load.get(3).equals("null")) {
				pathToCalculatingFile = load.get(3);
			}
			if (!load.get(4).equals("null")) {
				optionalPath = load.get(4);
			}
			borderColor = load.get(5);
			boldText = Boolean.parseBoolean(load.get(6));
		}
		if (Files.notExists(Path.of(programRootPath, configFileName))) {
			Files.createFile(Path.of(programRootPath, configFileName));
			saveConfigFile();
		}
	}

    static void saveConfigFile() throws IOException {
    	String [] configList = {
			    backgroundColor,
			    textColor,
			    pathToProductFile,
			    pathToCalculatingFile,
			    optionalPath,
			    borderColor,
			    String.valueOf(boldText)
	    };
	    Writer.write(programRootPath + configFileName, configList);
    }

    static void loadProperties() {
    	properties = new Properties();
    	try {
		    properties.loadFromXML(Files.newInputStream(Path.of("resources\\Properties.xml")));
	    } catch (Exception e) {
    		log.add(e);
	    }
    }

    static void readBasicFieldsFromProperties() {
    	programRootPath = properties.getProperty("programRootPath");
    	configFileName = properties.getProperty("configFileName");
    	logFileName = properties.getProperty("logFileName");
	    notificationFileName = properties.getProperty("notificationFileName");
	    logStorageDirectory = properties.getProperty("logStorageDirectory");
	    backgroundColor = properties.getProperty("backgroundColor");
	    textColor = properties.getProperty("textColor");
	    borderColor = properties.getProperty("borderColor");
	    boldText = Boolean.parseBoolean(properties.getProperty("boldText"));
    }

    static String getHostName() {
    	try {
    		return InetAddress.getLocalHost().getHostName();
	    } catch (Exception e) {
		    log.add(e);
    		return "";
	    }
    }

    static String getDate() {
	    LocalDateTime localDateTime = LocalDateTime.now();
	    return localDateTime.format(DateTimeFormatter.ofPattern(properties.getProperty("datePattern")));
    }

    static String getTime() {
	    LocalDateTime localDateTime = LocalDateTime.now();
	    return localDateTime.format(DateTimeFormatter.ofPattern(properties.getProperty("timePattern")));
    }
}