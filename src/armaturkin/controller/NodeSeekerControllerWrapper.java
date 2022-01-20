package armaturkin.controller;

import armaturkin.core.Main;
import armaturkin.service.LogService;
import armaturkin.view.Stages;
import armaturkin.workers.DropWorker;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.stage.Stage;
import nodeseeker.NodeSeeker;
import nodeseeker.listener.NodeSeekerListener;
import utools.stopwatch.Stopwatch;

import java.io.IOException;

/**
 * @author Andrey Korneychuk on 20-Jan-22
 * @version 1.0
 */
public class NodeSeekerControllerWrapper implements DragDropped {

	private static NodeSeekerControllerWrapper wrapper;
	private final TinyController controller;

	public static NodeSeekerControllerWrapper getInstance() throws IOException {
		if (wrapper == null) {
			wrapper = new NodeSeekerControllerWrapper();
		}
		return wrapper;
	}

	private NodeSeekerControllerWrapper() throws IOException {
		TinyController.init();
		controller = TinyController.controller;
	}

	public void show() {
		Stage stage = Stages.tinyStage;
		stage.setTitle(Main.app.getProperty("node_seeker_stage_name"));
		ObservableList<Image> icons = stage.getIcons();
		if (icons.size() >= 1) {
			// Stage icon is first element in list
			icons.clear();
		}
		icons.add(new Image(Main.class.getResourceAsStream("/icons/Icon_ns.png")));
		controller.setDropSpaceText(Main.app.getProperty("node_seeker_drop_space_text"));
		controller.setDragDropped(this);
		stage.show();
	}

	@Override
	public void dragDropped(DragEvent dragEvent) {
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.appendBefore("Total elapsed time: ");
		try {
			NodeSeeker nodeSeeker = new NodeSeeker(new NodeSeekerListenerImpl(Main.app.getLogService(), controller));
			nodeSeeker.consumeFile(DropWorker.nodeSeekerDragDropped(dragEvent, controller));
			controller.clearReportList();
			Main.app.log(Main.app.getProperty("node_seeker_stage_name") + " started");
			nodeSeeker.start();
		} catch (Exception e) {
			Main.app.log(e);
		}
		String prettyString = stopwatch.getPrettyString();
		//controller.addToReportList(prettyString);  <- this way put prettyString before adding in NodeSeekerListenerImpl
		Platform.runLater(() -> controller.addToReportList(prettyString));
		Main.app.log(prettyString);
	}

	private static class NodeSeekerListenerImpl implements NodeSeekerListener {

		private final LogService logService;
		private final TinyController controller;

		public NodeSeekerListenerImpl(LogService logService, TinyController controller) {
			this.logService = logService;
			this.controller = controller;
		}

		@Override
		public void update(String s) {
			logService.add(s);
			// https://stackoverflow.com/questions/21083945/how-to-avoid-not-on-fx-application-thread-currentthread-javafx-application-th
			Platform.runLater(() -> controller.addToReportList(s));
		}
	}
}