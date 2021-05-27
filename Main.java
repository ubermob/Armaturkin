import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static Parent root;
    public static String version = "0.1";
    public static String programRootPath = "C:\\Armaturkin\\";
    public static String configFileName = "config.txt";
    public static String backgroundColor = "#444444";
    public static String textColor = "#000000";
    public static String pathToProductFile;
    public static String pathToCalculatingFile;
    public static String optionalPath;
    public static FileInputStream productFileInputStream;
	public static List<File> inputFileList = new ArrayList<>();

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
        pathVerification(controller);
        primaryStage.show();
    }

    private void pathVerification(Controller controller) throws IOException {
        if (Files.exists(Path.of(pathToProductFile))) {
	        controller.setUpperDragSpaceText("Я использую предыдущий файл\nНо ты можешь обновить его");
	        productFileInputStream = new FileInputStream(pathToProductFile);
        } else {
	        controller.setUpperDragSpaceText("Я не нашёл файл с изделиями");
        }
    }

    public static void main(String[] args) throws IOException {
        loadConfig();
        launch(args);
        saveConfig();
    }

    static void loadConfig() throws IOException {
		if (Files.exists(Path.of(programRootPath, configFileName))) {
			List<String> load = Reader.read(programRootPath + configFileName);
			backgroundColor = load.get(0);
			textColor = load.get(1);
			pathToProductFile = load.get(2);
			pathToCalculatingFile = load.get(3);
			optionalPath = load.get(4);
		}
		if (Files.notExists(Path.of(programRootPath, configFileName))) {
			Files.createFile(Path.of(programRootPath, configFileName));
			saveConfig();
		}
    }

    static void saveConfig() throws IOException {
    	String [] configList = new String[5];
	    configList[0] = backgroundColor;
	    configList[1] = textColor;
	    configList[2] = pathToProductFile;
	    configList[3] = pathToCalculatingFile;
	    configList[4] = optionalPath;
	    Writer.write(programRootPath + configFileName, configList);
    }

	static void setBackgroundColor() {
		root.setStyle("-fx-background-color: " + backgroundColor + ";");
	}
}