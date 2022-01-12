package armaturkin.controller;

import armaturkin.core.Main;
import armaturkin.service.LogService;
import armaturkin.view.Stages;
import armaturkin.workers.DropWorker;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import nodeseeker.NodeSeeker;
import nodeseeker.listener.NodeSeekerListener;

import java.io.IOException;

/**
 * @author Andrey Korneychuk on 11-Jan-22
 * @version 1.0
 */
public class NodeSeekerController {

	public static NodeSeekerController controller;

	@FXML
	private Label dropSpace;
	@FXML
	private ListView<String> listView;

	private ObservableList<String> reportList;

	public static void show() throws IOException {
		if (Stages.nodeSeekerStage == null) {
			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/Node_seeker_view.fxml"));
			Stage stage = new Stage();
			stage.setScene(new Scene(fxmlLoader.load()));
			controller = fxmlLoader.getController();
			controller.reportList = FXCollections.observableArrayList();
			controller.listView.setItems(controller.reportList);
			stage.setTitle(Main.app.getProperty("node_seeker_stage_name"));
			stage.getScene().getRoot().setStyle("-fx-background-color: " + Main.app.getConfig().getBackgroundColor() + ";");
			controller.dropSpace.setTextFill(Paint.valueOf(Main.app.getConfig().getTextColor()));
			controller.dropSpace.setStyle(
					"-fx-border-color: %s; -fx-border-width: %d;".formatted(Main.app.getConfig().getBorderColor(), 5)
			);
			controller.listView.setBackground(Main.app.getController().getUserBackgroundColor());
			stage.getIcons().add(new Image(Main.class.getResourceAsStream("/Icon_ns.png")));
			Stages.nodeSeekerStage = stage;
		}
		Stages.nodeSeekerStage.show();
	}

	public void dragDropped(DragEvent dragEvent) {
		try {
			NodeSeeker nodeSeeker = new NodeSeeker(
					new NodeSeekerListenerImpl(Main.app.getLogService(), controller.reportList)
			);
			nodeSeeker.consumeFile(DropWorker.nodeSeekerDragDropped(dragEvent, dropSpace));
			reportList.clear();
			reportList.add("Started");
			Main.app.log(Main.app.getProperty("node_seeker_stage_name") + " started");
			nodeSeeker.start();
		} catch (Exception e) {
			Main.app.log(e);
		}
	}

	public void dragOver(DragEvent dragEvent) {
		DropWorker.dragOver(dragEvent);
	}

	private static class NodeSeekerListenerImpl implements NodeSeekerListener {

		LogService logService;
		ObservableList<String> reportList;

		public NodeSeekerListenerImpl(LogService logService, ObservableList<String> reportList) {
			this.logService = logService;
			this.reportList = reportList;
		}

		@Override
		public void update(String s) {
			logService.add(s);
			// https://stackoverflow.com/questions/21083945/how-to-avoid-not-on-fx-application-thread-currentthread-javafx-application-th
			Platform.runLater(() -> reportList.add(s));
		}
	}
}