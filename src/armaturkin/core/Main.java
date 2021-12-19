package armaturkin.core;

import armaturkin.controller.Controller;
import armaturkin.httpserver.HttpServer;
import armaturkin.manuallyentry.ManuallyEntry;
import armaturkin.reinforcement.Reinforcement;
import armaturkin.reinforcement.ReinforcementProduct;
import armaturkin.reinforcement.StandardsRepository;
import armaturkin.steelcomponent.SteelComponentRepository;
import armaturkin.summaryoutput.SummaryHub;
import armaturkin.summaryoutput.SummaryThreadPool;
import armaturkin.utils.Dev;
import armaturkin.utils.PcInformation;
import armaturkin.utils.StringUtil;
import armaturkin.utils.UnacceptableSymbolReplacer;
import armaturkin.view.AddonViews;
import armaturkin.view.Stages;
import armaturkin.workers.CalculatingFileWorker;
import armaturkin.workers.FileWorker;
import armaturkin.workers.ProductFileWorker;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static armaturkin.core.Log.log;

public class Main extends Application {

	public static final String version = "0.5.21b9";
	// Serial or Parallel Summary Running
	public static final boolean isSerialSummaryRunning = true;
	public static Properties properties = new Properties();
	public static Controller controller;
	public static Configuration config;
	private volatile static String notificationString = "";
	private static final Log notificationLog = new Log();
	public static Log log = new Log();
	public static HttpServer server = null;

	// 1st Tab
	public volatile static HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap = new HashMap<>();
	public volatile static HashMap<Integer, Reinforcement> reinforcementHashMap = new HashMap<>();
	// 3th Tab
	public volatile static HashMap<Integer, List<String>> summaryPaths = new HashMap<>();
	// 4th Tab
	public static List<ManuallyEntry> manuallySummaryEntries = new ArrayList<>();
	public static List<ManuallyEntry> backgroundReinforcementManuallyEntries = new ArrayList<>();

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/armaturkin/fxml/Main.fxml"));
		primaryStage.setScene(new Scene(loader.load()));
		controller = loader.getController();
		doingAddonViews();
		doingPrimaryStage(primaryStage);
		controller.startSetup();
		primaryStage.setTitle(getAppNameAndVersion());
		log(getProperty("application_main_line").formatted(primaryStage.getTitle(), getDate(), getTime()));
		log(PcInformation.getInformation());
		if (server != null) {
			Thread serverThread = new Thread(server);
			serverThread.start();
		}
		try (InputStream resource = getClass().getResourceAsStream("/Icon.png")) {
			primaryStage.getIcons().add(new Image(resource));
		} catch (Exception e) {
			log(e);
		}
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
		boolean isRunnable = ArgParser.parse(args);
		if (isRunnable) {
			loadMainProperties();
			Root.loadProperties();
			Root.checkDirectories();
			DesignCode.loadProperties();
			loadConfigFile();
			StandardsRepository.createPairs();
			SteelComponentRepository.load();
			// Launch
			launch(args);
			// Closing
			if (server != null) {
				server.stop();
			}
			saveConfigFile();
			Log.saveLog();
			saveAllNotification();
		} else {
			// https://stackoverflow.com/questions/12153622/how-to-close-a-javafx-application-on-window-close
			Platform.exit();
			System.exit(0);
		}
	}

	public static void loadProduct() {
		reinforcementProductHashMap.clear();
		ProductFileWorker productFileWorker = new ProductFileWorker(
				config.getPathToProductFile(),
				reinforcementProductHashMap
		);
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
					controller.getTableHead(),
					UnacceptableSymbolReplacer.replace(controller.getFileName())
			);
			Thread fileWorkerThread = new Thread(fileWorker);
			fileWorkerThread.start();
		}
	}

	public static void downloadSummaryFile() {
		if (!summaryPaths.isEmpty() || !manuallySummaryEntries.isEmpty()) {
			String path;
			if (config.isFavoritePathNotNull()) {
				path = config.getFavoritePath();
			} else {
				path = Path.of(config.getPathToSummaryCalculatingFile()).getParent().toString();
			}
			String summaryTableHead = controller.getSummaryTableHead();
			if (summaryTableHead.equals("")) {
				summaryTableHead = getProperty("default_table_main_header");
			}
			SummaryHub summaryHub = new SummaryHub(
					manuallySummaryEntries,
					path,
					UnacceptableSymbolReplacer.replace(controller.getSummaryFileName()),
					summaryTableHead
			);
			Thread summaryHubThread = new Thread(summaryHub);
			summaryHubThread.start();
		}
	}

	public static void checkSummaryDropSpace(int i) throws InterruptedException {
		if (!summaryPaths.get(i).isEmpty()) {
			SummaryThreadPool summaryThreadPool = new SummaryThreadPool(i);
			List<Log> logList = summaryThreadPool.getLogList();
			for (Log threadLog : logList) {
				log.merge(threadLog);
			}
		}
	}

	private void preloadUpperDropSpace() {
		if (config.isPathToProductFileNotNull() && config.getAutoParseProductList()) {
			Path path = Path.of(config.getPathToProductFile());
			if (Files.exists(path)) {
				controller.setUpperDropSpaceText(
						getProperty("upper_label_text_with_file").formatted(path.getFileName().toString())
				);
				loadProduct();
			}
		}
	}

	private void checkFavoriteDirectory() {
		if (config.isFavoritePathNotNull()) {
			controller.setFavoriteDropSpaceText(getProperty("favorite_is_on").formatted(config.getFavoritePath()));
			if (Files.notExists(Path.of(config.getFavoritePath()))) {
				try {
					restoreDirectory(config.getFavoritePath());
					addNotification(getProperty("favorite_is_restored_1").formatted(config.getFavoritePath()));
					log(getProperty("favorite_is_restored_2").formatted(getClass()));
				} catch (Exception e) {
					addNotification(getProperty("favorite_restore_failed_1").formatted(config.getFavoritePath()));
					log(getProperty("favorite_restore_failed_2").formatted(getClass()));
					log(e);
					controller.setFavoriteDropSpaceText(
							getProperty("favorite_restore_failed_1").formatted(config.getFavoritePath())
					);
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

	public static String getNotification() {
		return Main.notificationString;
	}

	public static Log getStoredNotifications() {
		return notificationLog;
	}

	public static List<String> getAllNotifications() {
		List<String> list = new ArrayList<>();
		for (var multiString : getStoredNotifications().getList()) {
			String[] split = multiString.split("\n");
			list.addAll(Arrays.asList(split));
		}
		list.addAll(Arrays.asList(getNotification().split("\n")));
		return list;
	}

	public static synchronized void clearNotification() {
		notificationLog.addSkipConsole(notificationString);
		notificationString = "";
	}

	private static void loadConfigFile() throws IOException {
		config = new Configuration(Root.programRootPath + Root.getProperty("config_file_name"));
	}

	private static void saveConfigFile() throws IOException {
		config.saveConfigFile();
	}

	private static void loadMainProperties() {
		try (InputStream resource = Main.class.getResourceAsStream("/Main_properties.xml")) {
			properties.loadFromXML(resource);
		} catch (Exception e) {
			log(e);
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public static void saveNotification() throws IOException {
		saveNotificationToFile(StringUtil.replaceNewLine(notificationString));
	}

	public static void saveAllNotification() throws IOException {
		StringBuilder builder = new StringBuilder();
		for (var v : notificationLog.getList()) {
			builder.append(v);
		}
		builder.append(StringUtil.replaceNewLine(notificationString));
		saveNotificationToFile(builder.toString());
	}

	private static void saveNotificationToFile(String string) throws IOException {
		StorageCleaner.clearStorage(Path.of(Root.programRootPath, Root.getProperty("notification_storage_directory")));
		if (config.getWriteNotification() && string.length() > 0) {
			StorageCleaner.copyFile(
					Path.of(Root.programRootPath, Root.getProperty("notification_file_name")),
					Path.of(Root.programRootPath,
							Root.getProperty("notification_storage_directory"),
							Root.getProperty("numbered_notification_file_name").formatted(
									StorageCleaner.getStorageSize(Root.programRootPath +
											Root.getProperty("notification_storage_directory")) + 1)
					)
			);
			Writer.write(Root.programRootPath + Root.getProperty("notification_file_name"), string);
		}
	}

	public static void deleteStorage(String path) {
		try {
			List<Path> collect = Files.list(Path.of(path)).collect(Collectors.toList());
			for (Path file : collect) {
				Files.delete(file);
			}
		} catch (IOException e) {
			log(e);
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
				log(e);
			}
		}
	}

	public static String getAppNameAndVersion() {
		return getProperty("application_name") + " ver " + version;
	}

	private static void doingPrimaryStage(Stage stage) {
		Stages.primary = stage;
		Stages.defaultHeight = stage.getHeight();
		Stages.defaultWidth = stage.getWidth();
	}

	private static void doingAddonViews() throws IOException {
		AddonViews.loadArrowLines(Main.class.getResource("/armaturkin/fxml/Arrow_lines.fxml"));
	}

	private static String getDate() {
		LocalDateTime localDateTime = LocalDateTime.now();
		return localDateTime.format(DateTimeFormatter.ofPattern(getProperty("date_pattern"), new Locale("en")));
	}

	private static String getTime() {
		LocalDateTime localDateTime = LocalDateTime.now();
		return localDateTime.format(DateTimeFormatter.ofPattern(getProperty("time_pattern")));
	}

	private static class ArgParser {

		private static boolean parse(String[] args) throws IOException {
			String[] sampleArgCommands = {
					"-help",     // 0
					"-disk",     // 1
					"-dev",      // 2
					"-linux",    // 3
					"-abs_path", // 4
					"-http"      // 5
			};
			for (var arg : args) {
				if (isMatchCommands(arg, sampleArgCommands[0])) {
					System.out.println("Version " + version);
					for (var line : Reader.read(Main.class.getResourceAsStream("/Run_arguments.txt"))) {
						System.out.println(line);
					}
					return false;
				}
				if (isMatchComplexCommands(arg, sampleArgCommands[1])) {
					String[] splitted = arg.split("=");
					if (splitted[1].length() != 1) {
						throw new UnsupportedOperationException("Invalid argument: \"" + arg + "\"");
					}
					char diskLetter = splitted[1].charAt(0);
					if (diskLetter < 'C' || 'Z' < diskLetter) {
						throw new UnsupportedOperationException("Invalid argument: \"" + arg + "\"");
					}
					if (Files.notExists(Path.of(diskLetter + ":\\"))) {
						throw new IOException("Disk \"" + diskLetter + ":\" do not exist");
					}
					Root.diskLetter = diskLetter;
					log(arg);
				}
				if (isMatchCommands(arg, sampleArgCommands[2])) {
					Dev.isDevMode = true;
					log(arg);
				}
				if (isMatchCommands(arg, sampleArgCommands[3])) {
					Root.os = Root.Os.LINUX;
				}
				if (isMatchComplexCommands(arg, sampleArgCommands[4])) {
					String absolutePath = arg.split("=")[1];
					if (Files.notExists(Path.of(absolutePath))) {
						throw new IOException("Absolute path: \"" + absolutePath + "\" do not exist");
					}
					Root.absolutePath = absolutePath;
				}
				if (isMatchComplexCommands(arg, sampleArgCommands[5])) {
					String[] splitted = arg.split("=");
					String hostname = "localhost";
					int port = 8080;
					if (splitted.length == 2) {
						try {
							String[] splittedValue = splitted[1].split(":");
							hostname = splittedValue[0];
							port = Integer.parseInt(splittedValue[1]);
						} catch (Exception e) {
							log(e);
						}
					}
					Main.server = new HttpServer(hostname, port);
				}
			}
			return true;
		}

		private static boolean isMatchCommands(String arg, String sampleArgCommand) {
			return arg.equals(sampleArgCommand);
		}

		private static boolean isMatchComplexCommands(String arg, String sampleArgCommand) {
			String[] splitted = arg.split("=");
			return splitted.length == 2 && splitted[0].equals(sampleArgCommand);
		}
	}
}