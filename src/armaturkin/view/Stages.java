package armaturkin.view;

import armaturkin.controller.Controller;
import armaturkin.core.Main;
import armaturkin.utils.ReinforcementLinearMassInfo;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Stages {

	public static Stage primary;
	public static Stage infoStage;
	public static Stage reinforcementLinearMassListStage;
	public static Stage hotRolledSteelCodeViewStage;
	public static Stage nodeSeekerStage;
	public static double defaultHeight;
	public static double defaultWidth;

	public static void doingPrimaryStage(Stage stage) {
		primary = stage;
		defaultHeight = stage.getHeight();
		defaultWidth = stage.getWidth();
		primary.setOnCloseRequest(windowEvent -> Stages.closeAll());
	}

	public static void showInfoStage() throws IOException {
		if (infoStage == null) {
			Controller controller = Main.app.getController();
			infoStage = new Stage();
			Label label = new FXMLLoader(Main.class.getResource("/fxml/Info_label.fxml")).load();
			label.setBackground(controller.getUserBackgroundColor());
			label.setFont(controller.getFont());
			label.setTextFill(Paint.valueOf(Main.app.getConfig().getTextColor()));
			infoStage.setScene(new Scene(label));
			infoStage.setTitle(Main.app.getProperty("info_stage_name"));
			infoStage.initStyle(StageStyle.UTILITY);
			primary.setOnCloseRequest(windowEvent -> Stages.closeAll());
		}
		infoStage.show();
	}

	public static void showReinforcementLinearMassList() {
		if (reinforcementLinearMassListStage == null) {
			Stage stage = new Stage();
			Label label = new Label(ReinforcementLinearMassInfo.getText());
			label.setBackground(Main.app.getController().getUserBackgroundColor());
			label.setFont(new Font("Consolas", 20));
			label.setTextFill(Paint.valueOf(Main.app.getConfig().getTextColor()));
			label.setAlignment(Pos.CENTER);
			stage.setScene(new Scene(label));
			stage.setTitle(Main.app.getProperty("reinforcement_linear_mass_list_stage_name"));
			stage.initStyle(StageStyle.UTILITY);
			primary.setOnCloseRequest(windowEvent -> Stages.closeAll());
			reinforcementLinearMassListStage = stage;
		}
		reinforcementLinearMassListStage.show();
	}

	public static void closeAll() {
		if (infoStage != null) {
			infoStage.close();
		}
		if (reinforcementLinearMassListStage != null) {
			reinforcementLinearMassListStage.close();
		}
		if (hotRolledSteelCodeViewStage != null) {
			hotRolledSteelCodeViewStage.close();
		}
		if (nodeSeekerStage != null) {
			nodeSeekerStage.close();
		}
	}
}