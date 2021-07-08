package armaturkin.workers;

import armaturkin.controller.Controller;
import armaturkin.view.LabelWrapper;
import armaturkin.core.Main;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DropWorker {

	public static void dragDropped(DragEvent dragEvent, int i, Controller controller) {
		List<File> fileList = getDroppedFile(dragEvent);
		Label label;
		if (i == 0) {
			label = controller.getUpperDropSpaceLabel();
		} else {
			label = controller.getLowerDropSpaceLabel();
		}
		if (fileList.size() != 1) {
			label.setText(Main.properties.getProperty("drop_worker_notification_1"));
		} else {
			if (isExcelFile(fileList.get(0).getName()) && fileList.get(0).isFile()) {
				label.setText(Main.properties.getProperty("drop_worker_file_accepted"));
				File file = fileList.get(0);
				if (i == 0) {
					Main.config.setPathToProductFile(file.getAbsolutePath());
					Main.log.add(Main.properties.getProperty("drop_worker_drop_space_file_accepted_1").formatted(
							DropWorker.class, Main.config.getPathToProductFile())
					);
					Main.loadProduct();
				}
				if (i == 1) {
					Main.config.setPathToCalculatingFile(file.getAbsolutePath());
					Main.log.add(Main.properties.getProperty("drop_worker_drop_space_file_accepted_2").formatted(
							DropWorker.class, Main.config.getPathToCalculatingFile())
					);
					Main.loadCalculatingFile();
				}
			} else {
				label.setText(Main.properties.getProperty("drop_worker_notification_2"));
			}
		}
	}

	public static void summaryDragDropped(DragEvent dragEvent, int i, Controller controller) {
		LabelWrapper labelWrapper = controller.getSummaryLabelWrapper(i);
		List<File> wildFileList = getDroppedFile(dragEvent);
		final List<String> verifiedFileList = new ArrayList<>();
		wildFileList.forEach(x -> {
			if(isExcelFile(x.getName()) && x.isFile()) {
				verifiedFileList.add(x.getAbsolutePath());
			}
		});
		if (!verifiedFileList.isEmpty()) {
			if (Main.summaryPaths.isEmpty()) {
				Main.config.setPathToSummaryCalculatingFile(verifiedFileList.get(0));
			}
			if (Main.summaryPaths.containsKey(i)) {
				Main.summaryPaths.get(i).addAll(verifiedFileList);
			} else {
				Main.summaryPaths.put(i, verifiedFileList);
			}
			for (String string : verifiedFileList) {
				Main.log.add(Main.properties.getProperty("drop_worker_summary_drop_space_file_accepted").formatted(DropWorker.class, i, string));
			}
			labelWrapper.setText(labelWrapper.getDefaultText() + "\n" +
					Main.properties.getProperty("summary_label_default_third_line").formatted(Main.summaryPaths.get(i).size())
			);
		}
	}

	public static void favoriteDragDropped(DragEvent dragEvent, Controller controller) {
		List<File> fileList = getDroppedFile(dragEvent);
		Label label = controller.getFavoriteDropSpaceLabel();
		if (fileList.size() != 1) {
			label.setText(Main.properties.getProperty("drop_worker_favorite_notification_1"));
		} else {
			if (!fileList.get(0).isDirectory()) {
				label.setText(Main.properties.getProperty("drop_worker_favorite_notification_2"));
			} else {
				Main.config.setFavoritePath(fileList.get(0).getAbsolutePath());
				label.setText(Main.properties.getProperty("favorite_is_on").formatted(Main.config.getFavoritePath()));
			}
		}
	}

	static List<File> getDroppedFile(DragEvent dragEvent) {
		Dragboard db = dragEvent.getDragboard();
		List<File> fileList = new ArrayList<>();
		if (db.hasFiles()) {
			fileList = db.getFiles();
		}
		return fileList;
	}

	public static void dragOver(DragEvent dragEvent) {
		if (dragEvent.getDragboard().hasFiles()) {
			dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}
		dragEvent.consume();
	}

	static boolean isExcelFile(String fileName) {
		return fileName.endsWith(".xls") || fileName.endsWith(".xlsx");
	}
}