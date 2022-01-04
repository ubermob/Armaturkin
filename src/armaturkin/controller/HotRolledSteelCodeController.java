package armaturkin.controller;

import armaturkin.core.Main;
import armaturkin.steelcomponent.HotRolledSteelType;
import armaturkin.steelcomponent.Image;
import armaturkin.steelcomponent.SteelComponentRepository;
import armaturkin.view.Stages;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.collections.FXCollections.observableList;

/**
 * @author Andrey Korneychuk on 21-Sep-21
 * @version 1.0
 */
public class HotRolledSteelCodeController {

	public static HotRolledSteelCodeController controller;

	@FXML
	private ChoiceBox<HotRolledSteelType> choiceBox1;
	@FXML
	private ChoiceBox<Image> choiceBox2;
	@FXML
	private Label label;
	@FXML
	private ImageView imageView;

	public static void show() throws IOException {
		if (Stages.hotRolledSteelCodeViewStage == null) {
			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(
					"/armaturkin/fxml/Hot_rolled_steel_code_view.fxml"
			));
			Stage stage = new Stage();
			stage.setScene(new Scene(fxmlLoader.load()));
			controller = fxmlLoader.getController();
			controller.setChoiceBox();
			stage.setTitle(Main.properties.getProperty("hot_rolled_steel_stage_name"));
			stage.getScene().getRoot().setStyle("-fx-background-color: " + Main.config.getBackgroundColor() + ";");
			controller.label.setFont(Main.controller.getFont());
			controller.label.setTextFill(Paint.valueOf(Main.config.getTextColor()));
			Stages.primary.setOnCloseRequest(windowEvent -> Stages.closeAll());
			Stages.hotRolledSteelCodeViewStage = stage;
		}
		Stages.hotRolledSteelCodeViewStage.show();
	}

	private void setChoiceBox() {
		choiceBox1.setItems(observableList(HotRolledSteelType.getFirstTwoElementsAsList()));
		choiceBox1.setValue(HotRolledSteelType.EQUAL_LEG_ANGLE);
		choiceBox1.setOnAction(actionEvent -> {
			if (choiceBox1.getValue() == HotRolledSteelType.EQUAL_LEG_ANGLE) {
				choiceBox2.setItems(observableList(SteelComponentRepository.getFullEqualAnglesImages()));
			} else if (choiceBox1.getValue() == HotRolledSteelType.UNEQUAL_LEG_ANGLE) {
				choiceBox2.setItems(observableList(SteelComponentRepository.getFullUnequalAnglesImages()));
			}
		});
		choiceBox2.setItems(observableList(SteelComponentRepository.getFullEqualAnglesImages()));
		choiceBox2.setOnAction(actionEvent -> label.setText(SteelComponentRepository.getCodeByElement(
				choiceBox1.getValue(),
				choiceBox2.getValue()
		)));
	}
}