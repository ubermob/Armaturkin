import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DropWorker {

	static void dragDropped(DragEvent dragEvent, int i, Controller controller) {
		List<File> fileList = getDroppedFile(dragEvent);
		Label label;
		if (i == 0) {
			label = controller.getUpperDropSpaceLabel();
		} else {
			label = controller.getLowerDropSpaceLabel();
		}
		if (fileList.size() != 1) {
			label.setText(Main.properties.getProperty("dropWorkerNotification1"));
		} else {
			if (isExcelFile(fileList.get(0).getName()) && fileList.get(0).isFile()) {
				label.setText(Main.properties.getProperty("dropWorkerFileAccepted"));
				File file = fileList.get(0);
				if (i == 0) {
					Main.config.setPathToProductFile(file.getAbsolutePath());
					Main.log.add(Main.properties.getProperty("dropWorkerDropSpaceFileAccepted1").formatted(
							DropWorker.class, Main.config.getPathToProductFile())
					);
					Main.loadProduct();
				}
				if (i == 1) {
					Main.config.setPathToCalculatingFile(file.getAbsolutePath());
					Main.log.add(Main.properties.getProperty("dropWorkerDropSpaceFileAccepted2").formatted(
							DropWorker.class, Main.config.getPathToCalculatingFile())
					);
					Main.loadCalculatingFile();
				}
			} else {
				label.setText(Main.properties.getProperty("dropWorkerNotification2"));
			}
		}
	}

	static void summaryDragDropped(DragEvent dragEvent, int i, Controller controller) {
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
				Main.log.add(Main.properties.getProperty("dropWorkerSummaryDropSpaceFileAccepted").formatted(DropWorker.class, i, string));
			}
			labelWrapper.setText(labelWrapper.getDefaultText() + "\n" +
					Main.properties.getProperty("summaryLabelDefaultThirdLine").formatted(Main.summaryPaths.get(i).size())
			);
		}
	}

	static void favoriteDragDropped(DragEvent dragEvent, Controller controller) {
		List<File> fileList = getDroppedFile(dragEvent);
		Label label = controller.getFavoriteDropSpaceLabel();
		if (fileList.size() != 1) {
			label.setText(Main.properties.getProperty("drop_worker_favorite_notification1"));
		} else {
			if (!fileList.get(0).isDirectory()) {
				label.setText(Main.properties.getProperty("drop_worker_favorite_notification2"));
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

	static void dragOver(DragEvent dragEvent) {
		if (dragEvent.getDragboard().hasFiles()) {
			dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}
		dragEvent.consume();
	}

	static boolean isExcelFile(String fileName) {
		return fileName.endsWith(".xls") || fileName.endsWith(".xlsx");
	}
}