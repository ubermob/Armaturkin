package armaturkin.controller;

import armaturkin.core.Main;
import armaturkin.utils.FxCss;
import armaturkin.view.Stages;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.apache.poi.ss.formula.functions.T;

import java.io.IOException;

/**
 * @author Andrey Korneychuk on 13-Jan-22
 * @version 1.0
 */
public class LittleControllerUtil {

/*	public static void setup(
			String pathToFxml
			, <T> controller
			) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(pathToFxml));
		Stage stage = new Stage();
		stage.setScene(new Scene(fxmlLoader.load()));
		var controller = fxmlLoader.getController();
		controller.reportList = FXCollections.observableArrayList();
		controller.listView.setItems(controller.reportList);
		stage.setTitle(Main.app.getProperty("node_seeker_stage_name"));
		stage.getScene().getRoot().setStyle("-fx-background-color: " + Main.app.getConfig().getBackgroundColor() + ";");
		controller.dropSpace.setFont(Main.app.getController().getFont());
		controller.dropSpace.setTextFill(Paint.valueOf(Main.app.getConfig().getTextColor()));
		controller.dropSpace.setStyle(
				"-fx-border-color: %s; -fx-border-width: %d;".formatted(Main.app.getConfig().getBorderColor(), 5)
		);
		controller.listView.setBackground(Main.app.getController().getUserBackgroundColor());
		FxCss.setCss(stage.getScene());
		stage.getIcons().add(new Image(Main.class.getResourceAsStream("/Icon_ns.png")));
		Stages.tinyStage = stage;
	}*/
}