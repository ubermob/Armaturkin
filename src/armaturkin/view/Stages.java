package armaturkin.view;

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

import static armaturkin.core.Main.getProperty;

public class Stages {

	public static Stage primary;
	public static Stage infoStage;
	public static Stage reinforcementLinearMassListStage;
	public static Stage hotRolledSteelCodeViewStage;
	public static double defaultHeight;
	public static double defaultWidth;

	public static void showInfoStage() throws IOException {
		if (infoStage == null) {
			infoStage = new Stage();
			Label label = new FXMLLoader(Main.class.getResource("/armaturkin/fxml/Info_label.fxml")).load();
			label.setBackground(Main.controller.getUserBackgroundColor());
			label.setFont(Main.controller.getFont());
			label.setTextFill(Paint.valueOf(Main.config.getTextColor()));
			infoStage.setScene(new Scene(label));
			infoStage.setTitle(getProperty("info_stage_name"));
			infoStage.initStyle(StageStyle.UTILITY);
			primary.setOnCloseRequest(windowEvent -> Stages.closeAll());
		}
		infoStage.show();
	}

	public static void showReinforcementLinearMassList() {
		if (reinforcementLinearMassListStage == null) {
			Stage stage = new Stage();
			Label label = new Label(ReinforcementLinearMassInfo.getText());
			label.setBackground(Main.controller.getUserBackgroundColor());
			label.setFont(new Font("Consolas", 20));
			label.setTextFill(Paint.valueOf(Main.config.getTextColor()));
			label.setAlignment(Pos.CENTER);
			stage.setScene(new Scene(label));
			stage.setTitle(getProperty("reinforcement_linear_mass_list_stage_name"));
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
	}
}