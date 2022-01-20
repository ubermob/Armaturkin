package armaturkin.controller;

import armaturkin.core.Main;
import armaturkin.utils.PythonProvider;
import armaturkin.view.Stages;
import armaturkin.workers.DropWorker;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.stage.Stage;
import utools.stopwatch.Stopwatch;

import java.io.IOException;

/**
 * @author Andrey Korneychuk on 20-Jan-22
 * @version 1.0
 */
public class PythonSteelFrameworkParserControllerWrapper implements DragDropped {

	private static PythonSteelFrameworkParserControllerWrapper wrapper;
	private final TinyController controller;

	public static PythonSteelFrameworkParserControllerWrapper getInstance() throws IOException {
		if (wrapper == null) {
			wrapper = new PythonSteelFrameworkParserControllerWrapper();
		}
		return wrapper;
	}

	private PythonSteelFrameworkParserControllerWrapper() throws IOException {
		TinyController.init();
		controller = TinyController.controller;
	}

	public void show() {
		Stage stage = Stages.tinyStage;
		stage.setTitle(Main.app.getProperty("python_util_stage_name_1"));
		ObservableList<Image> icons = stage.getIcons();
		if (icons.size() >= 1) {
			// Stage icon is first element in list
			icons.clear();
		}
		icons.add(new Image(Main.class.getResourceAsStream("/icons/Icon_py.png")));
		controller.setDropSpaceText(Main.app.getProperty("python_util_drop_space_text_1"));
		controller.setDragDropped(this);
		stage.show();
	}

	@Override
	public void dragDropped(DragEvent dragEvent) {
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.appendBefore("Python util completed in ");
		PythonProvider pythonProvider = new PythonProvider();
		String pythonResult = "";
		try {
			pythonResult = pythonProvider.executePythonUtil(DropWorker.pythonUtil(dragEvent, controller));
			Main.app.log(pythonResult);
		} catch (Exception e) {
			Main.app.log(e);
		}
		String stopwatchPrettyString = stopwatch.getPrettyString();
		Main.app.log(stopwatchPrettyString);
		// https://stackoverflow.com/questions/454908/split-java-string-by-new-line
		String[] splitted = pythonResult.split("\\r?\\n");
		for (var v : splitted) {
			controller.addToReportList(v);
		}
		controller.addToReportList(stopwatchPrettyString);
	}
}