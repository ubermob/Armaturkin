import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {
	
    public Text appearanceText;
    public Text appearanceText2;
    public Label upperDragSpace;
    public Label lowerDragSpace;
    public Label resultLabel;
	public Button calculationButton;
	public Button downloadButton;
	public Label notificationLabel;
	public Polygon notificationArrow;
	public Button clearResultLabelButton;
	public Button LowerDropSpaceButton;
	public Button clearUpperDropSpaceButton;
	public TextField downloadFileTableHead;

	/*public static void initialize() {
		// ???
    }*/

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
        notificationLabel.setTextFill(Paint.valueOf(Main.textColor));
    }

    public void setResultLabelText(String string) {
		resultLabel.setText(string);
    }

    public void setOpacity() {
		notificationLabel.setOpacity(0);
		notificationArrow.setOpacity(0);
    }

    public void setNotificationOpacity(int i) {
	    notificationLabel.setOpacity(i);
	    notificationArrow.setOpacity(i);
    }

    public void calculate(ActionEvent actionEvent) {
    	// Test
	    System.out.println("test calculate button");
	    System.out.println(downloadFileTableHead.getText());
    }

    public void upperDragDropped(DragEvent dragEvent) {
	    dragDropped(dragEvent, 0);
    }

    public void upperDragOver(DragEvent dragEvent) {
        dragOver(dragEvent);
    }

    public void lowerDragDropped(DragEvent dragEvent) {
	    dragDropped(dragEvent, 1);
    }

    public void lowerDragOver(DragEvent dragEvent) {
		dragOver(dragEvent);
    }

    public void dragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        dragEvent.consume();
    }

    public void dragDropped(DragEvent dragEvent, int i) {
	    List<File> fileList = getDroppedFile(dragEvent);
	    if (fileList.size() != 1) {
	    	if (i == 0) {
			    setUpperDragSpaceText("Мне нужен только один файл");
		    }
	    	if (i == 1) {
	    		setLowerDragSpaceText("Мне нужен только один файл");
		    }
	    } else {
		    String[] fileName = fileList.get(0).getName().split("\\.");
		    if (fileName[fileName.length - 1].equalsIgnoreCase("xls") ||
				    fileName[fileName.length - 1].equalsIgnoreCase("xlsx")) {
			    if (i == 0) {
				    setUpperDragSpaceText("Файл принят");
			    }
			    if (i == 1) {
				    setLowerDragSpaceText("Файл принят");
			    }
			    File file = fileList.get(0);
			    if (i == 0) {
				    Main.pathToProductFile = file.getAbsolutePath();
				    Main.loadProduct();
			    }
			    if (i == 1) {
			    	Main.pathToCalculatingFile = file.getAbsolutePath();
			    	Main.loadCalculatingFile();
			    }
		    } else {
		    	if (i == 0) {
				    setUpperDragSpaceText("Это не Excel файл");
			    }
		    	if (i == 1) {
				    setLowerDragSpaceText("Это не Excel файл");
			    }
		    }
	    }
    }

    public List<File> getDroppedFile(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        List<File> fileList = new ArrayList<>();
        if (db.hasFiles()) {
            fileList = db.getFiles();
        }
        return fileList;
    }

    public void setUpperDragSpaceText(String string) {
		upperDragSpace.setText(string);
    }

    public void setLowerDragSpaceText(String string) {
		lowerDragSpace.setText(string);
    }

	public void download(ActionEvent actionEvent) {
		Main.downloadCalculatedFile();
	}

	public void clearResultLabel(ActionEvent actionEvent) {
		Main.clearNotification();
	}

	public void clearUpperDropSpace(ActionEvent actionEvent) {
		Main.pathToProductFile = null;
		Main.reinforcementProductHashMap.clear();
	}

	public void clearLowerDropSpace(ActionEvent actionEvent) {
		Main.pathToCalculatingFile = null;
		Main.reinforcementHashMap.clear();
	}
}