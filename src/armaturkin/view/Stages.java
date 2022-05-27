package armaturkin.view;

import armaturkin.controller.Controller;
import armaturkin.core.Main;
import armaturkin.utils.InAppHelpArray;
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
	private static Label infoLabel;
	public static Stage reinforcementLinearMassListStage;
	public static Stage hotRolledSteelCodeViewStage;
	public static Stage tinyStage;
	public static double mainStageDefaultHeight;
	public static double mainStageDefaultWidth;

	public static void doingPrimaryStage(Stage stage) {
		primary = stage;
		mainStageDefaultHeight = stage.getHeight();
		mainStageDefaultWidth = stage.getWidth();
		primary.setOnCloseRequest(windowEvent -> Stages.closeAll());
	}

	public static void showDifferentInfoStage(int i) throws IOException {
		showInfoStage();
		infoLabel.setText(InAppHelpArray.getString(i));
	}

	public static void showInfoStage() throws IOException {
		if (infoStage == null) {
			Controller controller = Main.app.getController();
			infoStage = new Stage();
			Main.setIconToStage(infoStage, "/icons/Icon_i.png");
			infoLabel = new FXMLLoader(Main.class.getResource("/fxml/Info_label.fxml")).load();
			infoLabel.setBackground(controller.getUserBackgroundColor());
			infoLabel.setFont(controller.getFont());
			infoLabel.setTextFill(Paint.valueOf(Main.app.getConfig().getTextColor()));
			infoStage.setScene(new Scene(infoLabel));
			infoStage.setTitle(Main.app.getProperty("info_stage_name"));
			try {
				infoStage.setHeight(Main.app.getConfig().getInfoStageHeight());
				infoStage.setWidth(Main.app.getConfig().getInfoStageWidth());
			} catch (NullPointerException ignored) {
			}
			infoStage.setOnCloseRequest(x -> {
				double height = infoStage.getHeight();
				double width = infoStage.getWidth();
				infoStage.setHeight(height);
				infoStage.setWidth(width);
				Main.app.getConfig().setInfoStageHeight(height);
				Main.app.getConfig().setInfoStageWidth(width);
			});
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
		if (tinyStage != null) {
			tinyStage.close();
		}
	}
}