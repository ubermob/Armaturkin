import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static Parent root;
    public static String version = "0.2";
    public static String programRootPath = "C:\\Armaturkin\\";
    public static String configFileName = "config.txt";
    public static String backgroundColor = "#444444";
    public static String textColor = "#ffffff";
    private static volatile String notificationString = "";

    public volatile static String pathToProductFile;
    public volatile static String pathToCalculatingFile;
    public static String optionalPath;

	public static List<File> inputFileList = new ArrayList<>();

	public volatile static ArrayList<ReinforcementProduct> reinforcementProductArrayList = new ArrayList<>();
	public volatile static ArrayList<Reinforcement> reinforcementArrayList = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainScene2.fxml"));
        root = loader.load();
        Controller controller = loader.getController();
        controller.setTextColor();
        controller.setOpacity();
        primaryStage.setTitle("Арматуркин" + " ver " + version);
        primaryStage.setScene(new Scene(root));
        setBackgroundColor();
        controller.setUpperDragSpaceText("Перетащи сюда список изделий");
	    controller.setLowerDragSpaceText("Перетащи сюда файл,\nкоторый надо посчитать");
        pathVerification(controller);
        primaryStage.show();
	    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000 / 60.0), actionEvent -> {
		    controller.setResultLabelText(notificationString);
		    if (notificationString.length() > 0) {
		    	controller.setNotificationOpacity(1);
		    }
		    if (notificationString.length() == 0) {
			    controller.setNotificationOpacity(0);
		    }
	    }));
	    timeline.setCycleCount(Animation.INDEFINITE);
	    timeline.play();
    }

    private void pathVerification(Controller controller) {
    	if (pathToProductFile != null) {
		    Path path = Path.of(pathToProductFile);
		    if (Files.exists(path)) {
			    controller.setUpperDragSpaceText("Я использую предыдущий файл\n«" + path.getFileName() + "»\nНо ты можешь обновить его");
			    loadProduct();
		    }
	    }
    }

    public static void main(String[] args) throws IOException {
    	checkDirectory();
        loadConfigFile();
        launch(args);
        saveConfigFile();
    }

    static void loadProduct() {
    	reinforcementProductArrayList.clear();
	    ProductFileWorker productFileWorker = new ProductFileWorker(pathToProductFile, reinforcementProductArrayList);
	    Thread productFileWorkerThread = new Thread(productFileWorker);
	    productFileWorkerThread.start();
    }

    static void loadCalculatingFile() {
    	reinforcementArrayList.clear();
		CalculatingFileWorker calculatingFileWorker = new CalculatingFileWorker(pathToCalculatingFile, reinforcementArrayList);
		Thread calculatingFileWorkerThread = new Thread(calculatingFileWorker);
		calculatingFileWorkerThread.start();
    }

    public static void addNotification(String string) {
    	notificationString = notificationString + string + "\n";
    }

    public static void clearNotification() {
    	notificationString = "";
    }

    static void checkDirectory() throws IOException {
    	if (Files.notExists(Path.of(programRootPath))) {
    		Files.createDirectory(Path.of(programRootPath));
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
			    optionalPath
	    };
	    Writer.write(programRootPath + configFileName, configList);
    }

	static void setBackgroundColor() {
    	root.setStyle("-fx-background-color: " + backgroundColor + ";");
    }
}