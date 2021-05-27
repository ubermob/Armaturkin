import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Controller {
	
    public Text appearanceText;
    public Text appearanceText2;
    public Label upperDragSpace;
    public Label lowerDragSpace;
    public Label resultLabel;
	public Label calculatedLabel;
	public Button saveAppearanceButton;
	public Button calculationButton;
	public Button saveInputFilePathButton;
	public Button downloadButton;
	public Button clearListButton;

	public static void initialize() {
		// ???
    }

    public void setBackgroundColor1(MouseEvent mouseEvent) {
        Main.backgroundColor = "#ffffff";
        Main.setBackgroundColor();
    }

    public void setBackgroundColor2(MouseEvent mouseEvent) {
        Main.backgroundColor = "#dadada";
        Main.setBackgroundColor();
    }

    public void setBackgroundColor3(MouseEvent mouseEvent) {
        Main.backgroundColor = "#ababab";
        Main.setBackgroundColor();
    }

    public void setBackgroundColor4(MouseEvent mouseEvent) {
        Main.backgroundColor = "#444444";
        Main.setBackgroundColor();
    }

    public void setBackgroundColor5(MouseEvent mouseEvent) {
        Main.backgroundColor = "#000000";
        Main.setBackgroundColor();
    }

    public void setTextColor1(MouseEvent mouseEvent) {
        Main.textColor = "#ffffff";
        setTextColor();
    }

    public void setTextColor2(MouseEvent mouseEvent) {
        Main.textColor = "#dadada";
        setTextColor();
    }

    public void setTextColor3(MouseEvent mouseEvent) {
        Main.textColor = "#ababab";
        setTextColor();
    }

    public void setTextColor4(MouseEvent mouseEvent) {
        Main.textColor = "#444444";
        setTextColor();
    }

    public void setTextColor5(MouseEvent mouseEvent) {
        Main.textColor = "#000000";
        setTextColor();
    }

    public void setTextColor() {
        appearanceText.setFill(Paint.valueOf(Main.textColor));
        appearanceText2.setFill(Paint.valueOf(Main.textColor));
        upperDragSpace.setTextFill(Paint.valueOf(Main.textColor));
        lowerDragSpace.setTextFill(Paint.valueOf(Main.textColor));
        resultLabel.setTextFill(Paint.valueOf(Main.textColor));
        calculatedLabel.setTextFill(Paint.valueOf(Main.textColor));
    }

    public void setOpacity() {
		calculatedLabel.setOpacity(0);
    }

    public void calculate(ActionEvent actionEvent) {

    }

    public void upperDragDropped(DragEvent dragEvent) throws FileNotFoundException {
	    List<File> fileList = getDroppedFile(dragEvent);
	    if (fileList.size() != 1) {
			setUpperDragSpaceText("Мне нужен только один файл");
	    } else {
		    String[] fileName = fileList.get(0).getName().split("\\.");
		    if (fileName[1].equalsIgnoreCase("xls") || fileName[1].equalsIgnoreCase("xlsx")) {
				setUpperDragSpaceText("Файл принят");
				File file = fileList.get(0);
				Main.productFileInputStream = new FileInputStream(file);
				Main.pathToProductFile = file.getAbsolutePath();
		    } else {
			    setUpperDragSpaceText("Это не Excel файл");
		    }
	    }
    }

    public void upperDragOver(DragEvent dragEvent) {
        DragOver(dragEvent);
    }

    public void lowerDragDropped(DragEvent dragEvent) {
	    List<File> fileList = getDroppedFile(dragEvent);
    }

    public void lowerDragOver(DragEvent dragEvent) {
        DragOver(dragEvent);
    }

    public void DragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        dragEvent.consume();
    }

    public List<File> getDroppedFile(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        List<File> fileList = new ArrayList<>();
        if (db.hasFiles()) {
            fileList = db.getFiles();
        }
        return fileList;
    }

    public void saveAppearance(ActionEvent actionEvent) throws IOException {
        /*Writer.list.clear();
        Writer.list.add(Main.backgroundColor);
        Writer.list.add(Main.textColor);
        Writer.list.add(Main.pathToProductList);
        Writer.list.add(Main.pathToCalculatingFile);
        Writer.list.add(Main.optionalPath);
        Writer.write();*/
    }

    public void setUpperDragSpaceText(String string) {
        upperDragSpace.setText(string);
    }

	public void saveInputFilePath(ActionEvent actionEvent) {

	}

	public void download(ActionEvent actionEvent) {

	}

	public void clearList(ActionEvent actionEvent) {
	}
}