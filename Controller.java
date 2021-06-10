import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {

	Label[] labelArray;
	Label[] labelArray2;
	public Label upperDragSpace;
	public Label lowerDragSpace;
	public Label resultLabel;
	public Label notificationLabel;
	public Label notificationLabel2;
	public Label infoLabel;
	public Label specificationDragSpace1;
	public Label specificationDragSpace2;
	public Label specificationDragSpace3;
	public Label specificationDragSpace5;
	public Label specificationDragSpace7;
	public Label specificationDragSpace4;
	public Label specificationDragSpace6;
	public Label specificationDragSpace8;
	Button[] buttonArray;
	Button[] buttonArray2;
	public Button downloadFileButton;
	public Button clearResultLabelButton;
	public Button lowerDropSpaceButton;
	public Button clearUpperDropSpaceButton;
	public Button infoButton;
	public Button downloadResultLabelButton;
	public Button downloadSpecificationFileButton;
	public Button clearSpecificationDropSpaceButton0;
	public Button clearSpecificationDropSpaceButton1;
	public Button clearSpecificationDropSpaceButton2;
	public Button clearSpecificationDropSpaceButton3;
	public Button clearSpecificationDropSpaceButton4;
	public Button clearSpecificationDropSpaceButton5;
	public Button clearSpecificationDropSpaceButton6;
	public Button clearSpecificationDropSpaceButton7;
	public Button clearSpecificationDropSpaceButton8;
	public Button boldTextButton;
	public TextField downloadFileTableHead;
	public TextField backgroundReinforcement;
	public TextField downloadFileName;
	public TextField downloadSpecificationFileName;
	Text[] textArray;
	public Text appearanceText1;
    public Text appearanceText2;
	public Text appearanceText3;
	public Circle circle1;
	public Circle circle2;
	public Circle circle3;
	public Circle circle4;
	public Circle circle5;
	public Circle circleBorderColor1;
	public Circle circleBorderColor2;
	public Circle circleBorderColor3;
	public Circle circleBorderColor4;
	public Circle circleBorderColor5;
	Border border;

	public void setBackgroundColor1() {
		Main.backgroundColor = getColorHexCode(circle1.getFill());
        Main.setBackgroundColor();
    }

    public void setBackgroundColor2() {
	    Main.backgroundColor = getColorHexCode(circle2.getFill());
        Main.setBackgroundColor();
    }

    public void setBackgroundColor3() {
	    Main.backgroundColor = getColorHexCode(circle3.getFill());
        Main.setBackgroundColor();
    }

    public void setBackgroundColor4() {
	    Main.backgroundColor = getColorHexCode(circle4.getFill());
        Main.setBackgroundColor();
    }

    public void setBackgroundColor5() {
	    Main.backgroundColor = getColorHexCode(circle5.getFill());
        Main.setBackgroundColor();
    }

    public void setTextColor1() {
        Main.textColor = getColorHexCode(circle1.getFill());
        setTextColor();
    }

    public void setTextColor2() {
        Main.textColor = getColorHexCode(circle2.getFill());
        setTextColor();
    }

    public void setTextColor3() {
        Main.textColor = getColorHexCode(circle3.getFill());
        setTextColor();
    }

    public void setTextColor4() {
        Main.textColor = getColorHexCode(circle4.getFill());
        setTextColor();
    }

    public void setTextColor5() {
        Main.textColor = getColorHexCode(circle5.getFill());
        setTextColor();
    }

    public void groupAppearanceVariables() {
		labelArray = new Label[]{
				upperDragSpace,
				lowerDragSpace,
				resultLabel,
				notificationLabel,
				notificationLabel2,
				infoLabel,
				specificationDragSpace1,
				specificationDragSpace2,
				specificationDragSpace3,
				specificationDragSpace4,
				specificationDragSpace5,
				specificationDragSpace6,
				specificationDragSpace7,
				specificationDragSpace8
		};
	    labelArray2 = new Label[]{
			    upperDragSpace,
			    lowerDragSpace,
			    specificationDragSpace1,
			    specificationDragSpace2,
			    specificationDragSpace3,
			    specificationDragSpace4,
			    specificationDragSpace5,
			    specificationDragSpace6,
			    specificationDragSpace7,
			    specificationDragSpace8
	    };
		buttonArray = new Button[]{
				downloadFileButton,
				clearResultLabelButton,
				lowerDropSpaceButton,
				clearUpperDropSpaceButton,
				infoButton,
				downloadResultLabelButton,
				downloadSpecificationFileButton,
				clearSpecificationDropSpaceButton0,
				clearSpecificationDropSpaceButton1,
				clearSpecificationDropSpaceButton2,
				clearSpecificationDropSpaceButton3,
				clearSpecificationDropSpaceButton4,
				clearSpecificationDropSpaceButton5,
				clearSpecificationDropSpaceButton6,
				clearSpecificationDropSpaceButton7,
				clearSpecificationDropSpaceButton8,
				boldTextButton
		};
		buttonArray2 = new Button[]{
				downloadFileButton,
				clearResultLabelButton,
				lowerDropSpaceButton,
				clearUpperDropSpaceButton,
				infoButton,
				downloadResultLabelButton,
				downloadSpecificationFileButton,
				clearSpecificationDropSpaceButton0,
				boldTextButton
		};
		textArray = new Text[]{
				appearanceText1,
				appearanceText2,
				appearanceText3
		};
    }

	String getColorHexCode(Paint paint) {
		return "#" + paint.toString().substring(2, 8);
	}

    void setTextColor() {
	    for (Text text :
			    textArray) {
		    text.setFill(Paint.valueOf(Main.textColor));
	    }
	    for (Label label :
			    labelArray) {
		    label.setTextFill(Paint.valueOf(Main.textColor));
	    }
    }
    
    void setBorderColor() {
		border = new Border(new BorderStroke(Paint.valueOf(Main.borderColor), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5)));
		//Border saveBorder = resultLabel.getBorder();
	    for (Label label :
			    labelArray2) {
	    	label.setBorder(border);
	    }
	    //resultLabel.setBorder(saveBorder);
    }

    public void setResultLabelText(String string) {
		resultLabel.setText(string);
    }

    public void setNotificationOpacity(int i) {
	    notificationLabel.setOpacity(i);
	    notificationLabel2.setOpacity(i);
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

	public void downloadFile() {
		Main.downloadCalculatedFile();
	}

	public void clearResultLabel() {
		Main.clearNotification();
	}

	public void clearUpperDropSpace() {
		Main.pathToProductFile = null;
		Main.reinforcementProductHashMap.clear();
	}

	public void clearLowerDropSpace() {
		Main.pathToCalculatingFile = null;
		Main.reinforcementHashMap.clear();
	}

	public void toggleInfoLabelOpacity() {
    	double d = infoLabel.getOpacity();
    	if (d == 0.0) {
    		infoLabel.setOpacity(1);
	    }
    	if (d == 1.0) {
    		infoLabel.setOpacity(0);
	    }
	}

	public void setupInfoLabel() {
    	infoLabel.setOpacity(0);
    	infoLabel.setMouseTransparent(true);
	}

	public String getDownloadFileTableHead() {
		return downloadFileTableHead.getText();
	}

	public String getBackgroundReinforcement() {
    	return backgroundReinforcement.getText();
	}

	public String getDownloadFileName() {
		return downloadFileName.getText();
	}

	public void downloadResultLabel() throws IOException {
		Main.downloadNotification();
	}

	public void visitNotificationTab() {
		setNotificationOpacity(0);
	}

	public void specificationDragDropped(DragEvent dragEvent) {
	}

	public void specificationDragOver(DragEvent dragEvent) {
	}

	public void downloadSpecificationFile(ActionEvent actionEvent) {
	}

	public void clearSpecificationDropSpace0(ActionEvent actionEvent) {
	}

	public void clearSpecificationDropSpace1(ActionEvent actionEvent) {
	}

	public void clearSpecificationDropSpace2(ActionEvent actionEvent) {
	}

	public void clearSpecificationDropSpace3(ActionEvent actionEvent) {
	}

	public void clearSpecificationDropSpace4(ActionEvent actionEvent) {
	}

	public void clearSpecificationDropSpace5(ActionEvent actionEvent) {
	}

	public void clearSpecificationDropSpace6(ActionEvent actionEvent) {
	}

	public void clearSpecificationDropSpace7(ActionEvent actionEvent) {
	}

	public void clearSpecificationDropSpace8(ActionEvent actionEvent) {
	}

	public void setBorderColor1() {
		Main.borderColor = getColorHexCode(circleBorderColor1.getFill());
		setBorderColor();
	}

	public void setBorderColor2() {
		Main.borderColor = getColorHexCode(circleBorderColor2.getFill());
		setBorderColor();
	}

	public void setBorderColor3() {
		Main.borderColor = getColorHexCode(circleBorderColor3.getFill());
		setBorderColor();
	}

	public void setBorderColor4() {
		Main.borderColor = getColorHexCode(circleBorderColor4.getFill());
		setBorderColor();
	}

	public void setBorderColor5() {
		Main.borderColor = getColorHexCode(circleBorderColor5.getFill());
		setBorderColor();
	}

	public void toggleBoldText(ActionEvent actionEvent) {
		Main.boldText = !Main.boldText;
		setFont();
	}

	void setFont() {
		if (Main.boldText) {
			String font = "System Bold";
			setFont(new Font(font, 14), new Font(font, 16), new Font(font, 20));
		}
		if (!Main.boldText) {
			String font = "System";
			setFont(new Font(font, 14), new Font(font, 16), new Font(font, 20));
		}
	}

	void setFont(Font font1, Font font2, Font font3) {
		for (Label label :
				labelArray) {
			label.setFont(font3);
		}
		infoLabel.setFont(font2);
		notificationLabel.setFont(font1);
		notificationLabel2.setFont(font1);
		for (Button button :
				buttonArray2) {
			button.setFont(font2);
		}
		for (Text text :
				textArray) {
			text.setFont(font3);
		}
	}
}