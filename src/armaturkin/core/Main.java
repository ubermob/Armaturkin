package armaturkin.core;

import armaturkin.controller.Controller;
import armaturkin.httpserver.HttpServer;
import armaturkin.httpserver.HttpServerWrapper;
import armaturkin.utils.Dev;
import armaturkin.utils.InAppHelpArray;
import armaturkin.utils.PcInformation;
import armaturkin.view.Stages;
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
import java.util.Locale;

public class Main extends Application {

	public static final String version = "0.6.3";
	public static App app;

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
		primaryStage.setScene(new Scene(loader.load()));
		Controller controller = loader.getController();
		app.setController(controller);
		primaryStage.setTitle(getAppNameAndVersion());
		app.log(app.getProperty("application_main_line").formatted(primaryStage.getTitle(), getDate(), getTime()));
		app.log(PcInformation.getInformation());
		app.startHttpServer();
		setIconToStage(primaryStage, "/icons/Icon.png");
		app.getStorageService().checkFavoriteDirectory();
		app.getFirstHarvestingService().preloadUpperDropSpace();
		InAppHelpArray.load();
		primaryStage.show();
		Stages.doingPrimaryStage(primaryStage);
		controller.startSetup();
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000 / 60.0), actionEvent -> {
			controller.setResultLabelText(app.getActualNotification());
			controller.setCheckBox();
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	public static void main(String[] args) throws IOException {
		// create temp log for saving ArgParser work
		Log tempLog = new Log();
		HttpServerWrapper serverWrapper = new HttpServerWrapper();
		boolean isRunnable = ArgParser.parse(args, tempLog, serverWrapper);
		if (isRunnable) {
			app = new App();
			app.getLogService().merge(tempLog);
			app.setHttpServer(serverWrapper.getServer());
			// Launch
			launch(args);
			// Closing
			app.close();
		} else {
			// https://stackoverflow.com/questions/12153622/how-to-close-a-javafx-application-on-window-close
			Platform.exit();
			System.exit(0);
		}
	}

	public static void setIconToStage(Stage stage, String iconPath) {
		try (InputStream resource = Main.class.getResourceAsStream(iconPath)) {
			stage.getIcons().add(new Image(resource));
		} catch (Exception e) {
			app.log(e);
		}
	}

	public static String getAppNameAndVersion() {
		return app.getProperty("application_name") + " ver " + version;
	}

	private static String getDate() {
		LocalDateTime localDateTime = LocalDateTime.now();
		return localDateTime.format(DateTimeFormatter.ofPattern(app.getProperty("date_pattern")
				, new Locale("en")));
	}

	private static String getTime() {
		LocalDateTime localDateTime = LocalDateTime.now();
		return localDateTime.format(DateTimeFormatter.ofPattern(app.getProperty("time_pattern")));
	}

	private static class ArgParser {

		private static boolean parse(String[] args, Log tempLog, HttpServerWrapper serverWrapper) throws IOException {
			String[] sampleArgCommands = {
					"-help",     // 0
					"-disk",     // 1
					"-dev",      // 2
					"-linux",    // 3
					"-abs_path", // 4
					"-http",     // 5
					"-version"   // 6
			};
			for (var arg : args) {
				if (isMatchCommands(arg, sampleArgCommands[0])) {
					System.out.println("Version " + version);
					for (var line : Reader.readFromInternalSource("/Run_arguments.txt")) {
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
					tempLog.add(arg);
				}
				if (isMatchCommands(arg, sampleArgCommands[2])) {
					Dev.isDevMode = true;
					tempLog.add(arg);
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
					tempLog.add(arg);
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
							tempLog.add(e);
						}
					}
					serverWrapper.setServer(new HttpServer(hostname, port));
					tempLog.add(arg);
				}
				if (isMatchCommands(arg, sampleArgCommands[6])) {
					System.out.println("Version " + version);
					return false;
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