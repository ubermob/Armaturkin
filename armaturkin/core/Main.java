package armaturkin.core;

import armaturkin.controller.Controller;
import armaturkin.workers.CalculatingFileWorker;
import armaturkin.workers.FileWorker;
import armaturkin.workers.ProductFileWorker;
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
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Main extends Application {

	public static String version = "0.5.1";
	public static Properties properties;
	public static Parent root;
    public static Controller controller;
	public static Configuration config;
    public static String programRootPath;
    public static String configFileName;
    public static String logFileName;
	public static String notificationFileName;
	public static String logStorageDirectory;
	public static String notificationStorageDirectory;
    private static Character diskLetter;
    private volatile static String notificationString = "";
	public static Log log = new Log();
	public volatile static HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap = new HashMap<>();
	public volatile static HashMap<Integer, Reinforcement> reinforcementHashMap = new HashMap<>();
	public volatile static HashMap<Integer, List<String>> summaryPaths = new HashMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/armaturkin/fxml/main_scene.fxml"));
        root = loader.load();
        controller = loader.getController();
        controller.startSetup();
        primaryStage.setTitle(properties.getProperty("application_name") + " ver " + version);
        log.add(properties.getProperty("application_main_line").formatted(primaryStage.getTitle(), getDate(), getTime(), getHostName()));
        try {
	        InputStream resource = Main.class.getResourceAsStream("/Icon.png");
	        primaryStage.getIcons().add(new Image(resource));
	        resource.close();
        } catch (Exception e) {
        	log.add(e);
        }
        primaryStage.setScene(new Scene(root));
        checkFavoriteDirectory();
        preloadUpperDropSpace();
        primaryStage.show();
	    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000 / 60.0), actionEvent -> {
		    controller.setResultLabelText(notificationString);
		    controller.setCheckBox();
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
        saveNotification();
    }

    public static void loadProduct() {
    	reinforcementProductHashMap.clear();
	    ProductFileWorker productFileWorker = new ProductFileWorker(config.getPathToProductFile(), reinforcementProductHashMap);
	    Thread productFileWorkerThread = new Thread(productFileWorker);
	    productFileWorkerThread.start();
    }

    public static void loadCalculatingFile() {
    	reinforcementHashMap.clear();
		CalculatingFileWorker calculatingFileWorker = new CalculatingFileWorker(
				config.getPathToCalculatingFile(),
				reinforcementHashMap,
				reinforcementProductHashMap
		);
		Thread calculatingFileWorkerThread = new Thread(calculatingFileWorker);
		calculatingFileWorkerThread.start();
    }

    public static void downloadCalculatedFile() {
    	if (!reinforcementHashMap.isEmpty() && !reinforcementProductHashMap.isEmpty()) {
		    String path;
		    if (config.isFavoritePathNotNull()) {
			    path = config.getFavoritePath();
		    } else {
			    path = Path.of(config.getPathToCalculatingFile()).getParent().toString();
		    }
    		FileWorker fileWorker = new FileWorker(
    				path,
				    reinforcementHashMap,
				    controller.getBackgroundReinforcement(),
				    controller.getTableHead(),
				    controller.getFileName()
		    );
    		Thread fileWorkerThread = new Thread(fileWorker);
    		fileWorkerThread.start();
	    }
    }

    public static void downloadSummaryFile() {
    	if (!summaryPaths.isEmpty()) {
    		String path;
    		if (config.isFavoritePathNotNull()) {
    			path = config.getFavoritePath();
		    } else {
			    path = Path.of(config.getPathToSummaryCalculatingFile()).getParent().toString();
		    }
    		String summaryTableHead = controller.getSummaryTableHead();
    		if (summaryTableHead.equals("")) {
    			summaryTableHead = properties.getProperty("default_table_main_header");
		    }
    		SummaryHub summaryHub = new SummaryHub(summaryPaths, path, controller.getSummaryFileName(), summaryTableHead);
    		Thread summaryHubThread = new Thread(summaryHub);
    		summaryHubThread.start();
	    }
    }

	public static void checkSummaryDropSpace(int i) {
    	if (!summaryPaths.get(i).isEmpty()) {
		    SummaryThreadStarter summaryThreadStarter = new SummaryThreadStarter(i);
		    Thread[] subThreads = summaryThreadStarter.getSubThreads();
		    List<Log> logList = summaryThreadStarter.getLogList();
		    for (Thread thread : subThreads) {
			    try {
			    	thread.join();
			    } catch (Exception e) {
			    	log.add(e);
			    }
		    }
		    for (Log threadLog : logList) {
			    log.merge(threadLog);
		    }
	    }
	}

    static void parseArgs(String[] args) {
    	String[] argCommand = {"-writeLog", "-logStorageLimit", "-disk", "-helloSpammer"};
	    for (String arg : args) {
		    if (arg.equals(argCommand[0])) {
			    Log.enable();
			    log.add(argCommand[0]);
		    }
		    /*if (isMatchCommands(arg, argCommand[1])) {
			    int value = Integer.parseInt(arg.split("=")[1]);
			    Log.setLogStorageLimit(value);
			    log.add(argCommand[1] + "=" + value);
		    }*/
		    if (isMatchCommands(arg, argCommand[2])) {
		    	char diskLetter = arg.split("=")[1].charAt(0);
		    	char letterC = 'C';
		    	char letterZ = 'Z';
		    	if (letterC <= diskLetter && diskLetter <= letterZ) {
		    		Main.diskLetter = diskLetter;
				    log.add(argCommand[2] + "=" + Main.diskLetter);
			    }
		    }
		    if (isMatchCommands(arg, argCommand[3])) {
		    	Spammer spammer = new Spammer();
			    Thread spammerThread = new Thread(spammer);
			    spammerThread.start();
			    log.add(argCommand[3]);
		    }
	    }
    }

    static boolean isMatchCommands(String arg, String argCommand) {
    	return arg.length() >= argCommand.length() && arg.startsWith(argCommand);
    }

	private void preloadUpperDropSpace() {
		if (config.isPathToProductFileNotNull() && config.getAutoParseProductList()) {
			Path path = Path.of(config.getPathToProductFile());
			if (Files.exists(path)) {
				controller.setUpperDropSpaceText(
						properties.getProperty("upper_label_text_with_file").formatted(path.getFileName().toString())
				);
				loadProduct();
			}
		}
	}

	private void checkFavoriteDirectory() {
    	if (config.isFavoritePathNotNull()) {
    		controller.setFavoriteDropSpaceText(properties.getProperty("favorite_is_on").formatted(config.getFavoritePath()));
    		if (Files.notExists(Path.of(config.getFavoritePath()))) {
    			try {
				    restoreDirectory(config.getFavoritePath());
				    addNotification(properties.getProperty("favorite_is_restored_1").formatted(config.getFavoritePath()));
				    Main.log.add(properties.getProperty("favorite_is_restored_2").formatted(getClass()));
			    } catch (Exception e) {
				    addNotification(properties.getProperty("favorite_restore_failed_1").formatted(config.getFavoritePath()));
    				Main.log.add(properties.getProperty("favorite_restore_failed_2").formatted(getClass()));
    				Main.log.add(e);
    				controller.setFavoriteDropSpaceText(properties.getProperty("favorite_restore_failed_1").formatted(config.getFavoritePath()));
			    }
		    }
	    }
	}

	private void restoreDirectory(String string) throws IOException {
		String[] branch = string.split("\\\\");
		String path = "";
		for (String directory : branch) {
			path += directory + "\\";
			if (Files.notExists(Path.of(path))) {
				Files.createDirectory(Path.of(path));
			}
		}
	}

    public static synchronized void addNotification(String string) {
    	notificationString += string + "\n";
    	controller.setNotificationOpacity(1);
    }

    public static void clearNotification() {
    	notificationString = "";
    }

    static void checkDirectory() throws IOException {
    	if (diskLetter != null) {
    		programRootPath = diskLetter + programRootPath.substring(1);
	    }
    	if (Files.notExists(Path.of(programRootPath))) {
    		Files.createDirectory(Path.of(programRootPath));
	    }
    	if (Files.notExists(Path.of(programRootPath, logStorageDirectory))) {
    		Files.createDirectory(Path.of(programRootPath, logStorageDirectory));
	    }
	    if (Files.notExists(Path.of(programRootPath, notificationStorageDirectory))) {
		    Files.createDirectory(Path.of(programRootPath, notificationStorageDirectory));
	    }
    }

	static void loadConfigFile() throws IOException {
		config = new Configuration(programRootPath + configFileName);
	}

    static void saveConfigFile() throws IOException {
	    config.saveConfigFile();
    }

    static void loadProperties() {
    	properties = new Properties();
    	try {
		    InputStream resource = Main.class.getResourceAsStream("/Properties.xml");
		    properties.loadFromXML(resource);
		    resource.close();
	    } catch (Exception e) {
    		log.add(e);
	    }
    }

    public static void saveNotification() throws IOException {
    	StorageCleaner.clearStorage(Path.of(programRootPath, notificationStorageDirectory));
    	if (config.getWriteNotification()) {
		    StorageCleaner.copyFile(
				    Path.of(programRootPath, notificationFileName),
				    Path.of(programRootPath, notificationStorageDirectory,
						    properties.getProperty("numbered_notification_file_name").formatted(
								    StorageCleaner.getStorageSize(programRootPath + notificationStorageDirectory) + 1)
				    )
		    );
		    String[] string = new String[1];
		    string[0] = notificationString;
		    Writer.write(programRootPath + notificationFileName, string);
	    }
    }

    public static void deleteStorage(String path) {
    	try {
		    List<Path> collect = Files.list(Path.of(path)).collect(Collectors.toList());
		    for (Path file : collect) {
			    Files.delete(file);
		    }
	    } catch (IOException e) {
    		log.add(e);
	    }
    }

	public static void parseTextField(int i, String string) {
    	if (string.length() > 0) {
		    int parsed;
		    try {
			    parsed = Integer.parseInt(string);
			    if (0 <= parsed && parsed <= 5000) {
				    if (i == 0) {
					    config.setLogStorageLimit(parsed);
				    }
				    if (i == 1) {
					    config.setNotificationStorageLimit(parsed);
				    }
			    }
		    } catch (Exception e) {
			    log.add(e);
		    }
	    }
	}

    static void readBasicFieldsFromProperties() {
    	programRootPath = properties.getProperty("program_root_path");
    	configFileName = properties.getProperty("config_file_name");
    	logFileName = properties.getProperty("log_file_name");
	    notificationFileName = properties.getProperty("notification_file_name");
	    logStorageDirectory = properties.getProperty("log_storage_directory");
	    notificationStorageDirectory = properties.getProperty("notification_storage_directory");
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
	    return localDateTime.format(DateTimeFormatter.ofPattern(properties.getProperty("date_pattern")));
    }

    static String getTime() {
	    LocalDateTime localDateTime = LocalDateTime.now();
	    return localDateTime.format(DateTimeFormatter.ofPattern(properties.getProperty("time_pattern")));
    }
}