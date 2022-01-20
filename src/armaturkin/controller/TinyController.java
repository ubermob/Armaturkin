package armaturkin.controller;

import armaturkin.core.Main;
import armaturkin.utils.FxCss;
import armaturkin.view.Stages;
import armaturkin.workers.DropWorker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Andrey Korneychuk on 11-Jan-22
 * @version 1.0
 */
public class TinyController {

	static TinyController controller;

	@FXML
	private Label dropSpace;
	@FXML
	private ListView<String> listView;

	private ObservableList<String> reportList;

	public static void init() throws IOException {
		if (controller == null) {
			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/Tiny_view.fxml"));
			Stage stage = new Stage();
			stage.setScene(new Scene(fxmlLoader.load()));
			controller = fxmlLoader.getController();
			controller.reportList = FXCollections.observableArrayList();
			controller.listView.setItems(controller.reportList);
			stage.getScene().getRoot().setStyle("-fx-background-color: " + Main.app.getConfig().getBackgroundColor() + ";");
			controller.dropSpace.setFont(Main.app.getController().getFont());
			controller.dropSpace.setTextFill(Paint.valueOf(Main.app.getConfig().getTextColor()));
			controller.dropSpace.setStyle(
					"-fx-border-color: %s; -fx-border-width: %d;".formatted(Main.app.getConfig().getBorderColor(), 5)
			);
			controller.listView.setBackground(Main.app.getController().getUserBackgroundColor());
			FxCss.setCss(stage.getScene());
			Stages.tinyStage = stage;
		}
	}

	public void setDropSpaceText(String text) {
		dropSpace.setText(text);
	}

	public void appendDropSpaceText(String text) {
		dropSpace.setText(dropSpace.getText() + "\n" + text);
	}

	public void setDragDropped(DragDropped wrapper) {
		dropSpace.setOnDragDropped(wrapper::dragDropped);
	}

	public void addToReportList(String string) {
		reportList.add(string);
	}

	public void clearReportList() {
		reportList.clear();
	}

	@FXML
	private void dragDropped() {
	}

	@FXML
	private void dragOver(DragEvent dragEvent) {
		DropWorker.dragOver(dragEvent);
	}
}