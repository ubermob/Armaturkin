package armaturkin.workers;

import armaturkin.controller.Controller;
import armaturkin.controller.TinyController;
import armaturkin.core.Main;
import armaturkin.interfaces.FileNameCreatorImpl;
import armaturkin.summaryoutput.SummaryBuilderFileCreator;
import armaturkin.view.LabelWrapper;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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
			label.setText(Main.app.getProperty("drop_worker_notification_1"));
		} else {
			if (isExcelFile(fileList.get(0).getName()) && fileList.get(0).isFile()) {
				label.setText(Main.app.getProperty("drop_worker_file_accepted"));
				File file = fileList.get(0);
				if (i == 0) {
					Main.app.getConfig().setPathToProductFile(file.getAbsolutePath());
					Main.app.log(Main.app.getProperty("drop_worker_drop_space_file_accepted_1").formatted(
							DropWorker.class, Main.app.getConfig().getPathToProductFile())
					);
					Main.app.getFirstHarvestingService().loadProductFile();
				}
				if (i == 1) {
					Main.app.getConfig().setPathToCalculatingFile(file.getAbsolutePath());
					Main.app.log(Main.app.getProperty("drop_worker_drop_space_file_accepted_2").formatted(
							DropWorker.class, Main.app.getConfig().getPathToCalculatingFile())
					);
					Main.app.getFirstHarvestingService().loadCalculatingFile();
				}
			} else {
				label.setText(Main.app.getProperty("drop_worker_notification_2"));
			}
		}
	}

	public static void summaryDragDropped(DragEvent dragEvent, int i, Controller controller) {
		LabelWrapper labelWrapper = controller.getSummaryLabelWrapper(i);
		List<File> wildFileList = getDroppedFile(dragEvent);
		final List<String> verifiedFileList = new ArrayList<>();
		wildFileList.forEach(x -> {
			if (isExcelFile(x.getName()) && x.isFile()) {
				verifiedFileList.add(x.getAbsolutePath());
			}
		});
		var summaryPaths = Main.app.getSummaryModel().getSummaryPaths();
		if (!verifiedFileList.isEmpty()) {
			if (summaryPaths.isEmpty()) {
				Main.app.getConfig().setPathToSummaryCalculatingFile(verifiedFileList.get(0));
			}
			if (summaryPaths.containsKey(i)) {
				summaryPaths.get(i).addAll(verifiedFileList);
			} else {
				summaryPaths.put(i, verifiedFileList);
			}
			for (String string : verifiedFileList) {
				Main.app.log(Main.app.getProperty("drop_worker_summary_drop_space_file_accepted").formatted(DropWorker.class, i, string));
			}
			labelWrapper.setText(labelWrapper.getDefaultText() + "\n" +
					Main.app.getProperty("summary_label_default_third_line").formatted(summaryPaths.get(i).size())
			);
		}
	}

	public static void favoriteDragDropped(DragEvent dragEvent, Controller controller) {
		List<File> fileList = getDroppedFile(dragEvent);
		Label label = controller.getFavoriteDropSpaceLabel();
		if (fileList.size() != 1) {
			label.setText(Main.app.getProperty("drop_worker_favorite_notification_1"));
		} else {
			if (!fileList.get(0).isDirectory()) {
				label.setText(Main.app.getProperty("drop_worker_favorite_notification_2"));
			} else {
				Main.app.getConfig().setFavoritePath(fileList.get(0).getAbsolutePath());
				label.setText(Main.app.getProperty("favorite_is_on").formatted(Main.app.getConfig().getFavoritePath()));
			}
		}
	}

	public static void summaryBuilderDragDropped(DragEvent dragEvent, Controller controller) throws IOException {
		List<File> fileList = getDroppedFile(dragEvent);
		Label label = controller.getSummaryBuilderFileDropSpaceLabel();
		if (fileList.size() != 1) {
			label.setText(Main.app.getProperty("drop_worker_notification_1"));
		} else {
			if (isSummaryBuilderFile(fileList.get(0).getName())) {
				Main.app.getSummaryService().consumeSummaryBuilderFile(fileList.get(0).getAbsolutePath());
			} else {
				label.setText(Main.app.getProperty("drop_worker_notification_3"));
			}
		}
	}

	public static void summaryBuilderFileCreatorDragDropped(DragEvent dragEvent) {
		Path inputPath = Path.of(dragEvent.getDragboard().getFiles().get(0).getAbsolutePath());
		String fileName = new FileNameCreatorImpl().createFileName("", ".sb");
		Path destinationPath;
		if (Main.app.getConfig().isFavoritePathNotNull()) {
			destinationPath = Path.of(Main.app.getConfig().getFavoritePath(), fileName);
		} else {
			Path parent = inputPath.getParent();
			destinationPath = Path.of(parent.toString(), fileName);
		}
		new SummaryBuilderFileCreator().create(inputPath, "rel", "Имя блока", destinationPath);
	}

	public static String nodeSeekerDragDropped(DragEvent dragEvent, TinyController controller) {
		List<File> fileList = getDroppedFile(dragEvent);
		if (fileList.size() != 1) {
			controller.setDropSpaceText(Main.app.getProperty("drop_worker_notification_1"));
		} else {
			if (isXlsFile(fileList.get(0).getName()) || isInpFile(fileList.get(0).getName())) {
				return fileList.get(0).getAbsolutePath();
			} else {
				controller.appendDropSpaceText(Main.app.getProperty("drop_worker_notification_4"));
			}
		}
		return null;
	}

	public static String pythonUtil(DragEvent dragEvent, TinyController controller) {
		List<File> fileList = getDroppedFile(dragEvent);
		if (fileList.size() != 1) {
			controller.setDropSpaceText(Main.app.getProperty("drop_worker_notification_1"));
		} else {
			if (isXlsxFile(fileList.get(0).getName())) {
				return fileList.get(0).getAbsolutePath();
			} else {
				controller.appendDropSpaceText(Main.app.getProperty("drop_worker_notification_4"));
			}
		}
		return null;
	}

	public static void dragOver(DragEvent dragEvent) {
		if (dragEvent.getDragboard().hasFiles()) {
			dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}
		dragEvent.consume();
	}

	private static List<File> getDroppedFile(DragEvent dragEvent) {
		Dragboard db = dragEvent.getDragboard();
		List<File> fileList = new ArrayList<>();
		if (db.hasFiles()) {
			fileList = db.getFiles();
		}
		return fileList;
	}

	private static boolean isExcelFile(String fileName) {
		return fileName.endsWith(".xls") || fileName.endsWith(".xlsx");
	}

	private static boolean isSummaryBuilderFile(String fileName) {
		return fileName.endsWith(".sb");
	}

	private static boolean isXlsxFile(String fileName) {
		return fileName.endsWith(".xlsx");
	}

	private static boolean isInpFile(String fileName) {
		return fileName.endsWith(".inp");
	}

	private static boolean isXlsFile(String fileName) {
		return fileName.endsWith(".xls");
	}
}