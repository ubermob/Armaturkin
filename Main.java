import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class Main extends Application {

    public static Parent root;
    public static Controller controller;
    public static String version = "0.3.2";
    public static String programRootPath = "C:\\Armaturkin\\";
    public static String configFileName = "config.txt";
    public static String logFileName = "log.txt";
	public static String notificationFileName = "notification.txt";
	public static String logStorageDirectory = "Log storage";
    public static String backgroundColor = "#444444";
    public static String textColor = "#ffffff";
    public static String borderColor = "#008000";
    public static boolean boldText = true;
    private static volatile String notificationString = "";

    public volatile static String pathToProductFile;
    public volatile static String pathToCalculatingFile;
    public static String optionalPath;

	public volatile static HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap = new HashMap<>();
	public volatile static HashMap<Integer, Reinforcement> reinforcementHashMap = new HashMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainScene2.fxml"));
        root = loader.load();
        controller = loader.getController();
        controller.groupAppearanceVariables();
        controller.setTextColor();
        controller.setFont();
        controller.setNotificationOpacity(0);
	    controller.setBorderColor();
        primaryStage.setTitle("Арматуркин ver " + version);
        Log.add(primaryStage.getTitle() + " " + getDateTime() + " " + getHostName());
        primaryStage.setScene(new Scene(root));
        setBackgroundColor();
        controller.setupInfoLabel();
        controller.setUpperDragSpaceText("Перетащи сюда список изделий");
	    controller.setLowerDragSpaceText("Перетащи сюда файл,\nкоторый надо посчитать");
        pathVerification();
        primaryStage.show();
	    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000 / 60.0), actionEvent -> {
		    controller.setResultLabelText(notificationString);
	    }));
	    timeline.setCycleCount(Animation.INDEFINITE);
	    timeline.play();
    }

    private void pathVerification() {
    	if (pathToProductFile != null) {
		    Path path = Path.of(pathToProductFile);
		    if (Files.exists(path)) {
			    controller.setUpperDragSpaceText("Я использую предыдущий файл\n«" + path.getFileName() +
					    "»\nНо ты можешь обновить его");
			    loadProduct();
		    }
	    }
    }

    public static void main(String[] args) throws IOException {
    	checkDirectory();
    	try {
		    parseArgs(args);
	    } catch (ArrayIndexOutOfBoundsException e) {
		    Log.add(e);
	    }
    	try {
		    loadConfigFile();
	    } catch (Exception e) {
		    Log.add(e);
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
				    controller.getDownloadFileTableHead(),
				    controller.getDownloadFileName()
		    );
    		Thread fileWorkerThread = new Thread(fileWorker);
    		fileWorkerThread.start();
	    }
    }

    static void downloadNotification() throws IOException {
    	String[] string = new String[1];
    	string[0] = notificationString;
    	Writer.write(programRootPath + notificationFileName, string);
    }

    static void parseArgs(String[] args) {
    	String[] argCommand = {"-writeLog", "-logStorageLimit"};
	    for (String arg : args) {
		    if (arg.equals(argCommand[0])) {
			    Log.enable();
			    Log.add(argCommand[0]);
		    }
	    }
	    for (String arg : args) {
		    if (logStorageLimitCondition(arg, argCommand[1])) {
		    	int value = Integer.parseInt(args[1].split("=")[1]);
			    Log.setLogStorageLimit(value);
			    Log.add(argCommand[1] + " " + value);
		    }
	    }
    }

    static boolean logStorageLimitCondition(String arg, String argCommand) {
    	return arg.length() >= argCommand.length() && arg.startsWith(argCommand);
    }

    public static void addNotification(String string) {
    	notificationString += string + "\n";
    	controller.setNotificationOpacity(1);
    }

    public static void clearNotification() {
    	notificationString = "";
    }

    static void checkDirectory() throws IOException {
    	if (Files.notExists(Path.of(programRootPath))) {
    		Files.createDirectory(Path.of(programRootPath));
	    }
    	if (Files.notExists(Path.of(programRootPath, logStorageDirectory))) {
    		Files.createDirectory(Path.of(programRootPath, logStorageDirectory));
	    }
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

    static String getHostName() {
    	try {
    		return "System name: " + InetAddress.getLocalHost().getHostName();
	    } catch (Exception e) {
		    Log.add(e);
    		return "";
	    }
    }

    static String getDateTime() {
	    LocalDateTime localDateTime = LocalDateTime.now();
	    return localDateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH-mm-ss"));
    }

	static void setBackgroundColor() {
    	root.setStyle("-fx-background-color: " + backgroundColor + ";");
    }
}