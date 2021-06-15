import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.*;

public class Controller {

	private Label[] allLabels;
	private Label[] borderModifiedLabels;
	private LabelWrapper[] allSummaryLabelWrappers;
	public Label upperDropSpace;
	public Label lowerDropSpace;
	public Label resultLabel;
	public Label notificationLabel;
	public Label notificationLabel2;
	public Label infoLabel;
	public Label summaryDropSpace1;
	public Label summaryDropSpace2;
	public Label summaryDropSpace3;
	public Label summaryDropSpace4;
	public Label summaryDropSpace5;
	public Label summaryDropSpace6;
	public Label summaryDropSpace7;
	public Label summaryDropSpace8;
	Button[] allButtons;
	Button[] boldTextModifiedButtons;
	public Button downloadFileButton;
	public Button clearResultLabelButton;
	public Button lowerDropSpaceButton;
	public Button clearUpperDropSpaceButton;
	public Button infoButton;
	public Button downloadResultLabelButton;
	public Button downloadSummaryFileButton;
	public Button clearAllSummaryDropSpaceButton;
	public Button clearSummaryDropSpaceButton1;
	public Button clearSummaryDropSpaceButton2;
	public Button clearSummaryDropSpaceButton3;
	public Button clearSummaryDropSpaceButton4;
	public Button clearSummaryDropSpaceButton5;
	public Button clearSummaryDropSpaceButton6;
	public Button clearSummaryDropSpaceButton7;
	public Button clearSummaryDropSpaceButton8;
	public Button checkSummaryDropSpaceButton4;
	public Button checkSummaryDropSpaceButton6;
	public Button checkSummaryDropSpaceButton8;
	public Button checkSummaryDropSpaceButton7;
	public Button boldTextButton;
	public TextField tableHead;
	public TextField backgroundReinforcement;
	public TextField fileName;
	public TextField summaryFileName;
	public TextField summaryTableHead;
	Text[] allTexts;
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

	public void startSetup() {
		groupAppearanceVariables();
		setBackgroundColor();
		setTextColor();
		setFont();
		setNotificationOpacity(0);
		setBorderColor();
		setupInfoLabel();
		setUpperDropSpaceText(Main.properties.getProperty("upperLabelDefaultText"));
		setLowerDropSpaceText(Main.properties.getProperty("lowerLabelDefaultText"));

		String secondLine = Main.properties.getProperty("summaryLabelDefaultSecondLine");
		setSummaryDropSpaceText(1, Main.properties.getProperty("summaryLabelDefaultFirstLine1").formatted(secondLine));
		setSummaryDropSpaceText(2, Main.properties.getProperty("summaryLabelDefaultFirstLine2").formatted(secondLine));
		setSummaryDropSpaceText(3, Main.properties.getProperty("summaryLabelDefaultFirstLine3").formatted(secondLine));
		setSummaryDropSpaceText(4, Main.properties.getProperty("summaryLabelDefaultFirstLine4").formatted(secondLine));
		setSummaryDropSpaceText(5, Main.properties.getProperty("summaryLabelDefaultFirstLine5").formatted(secondLine));
		setSummaryDropSpaceText(6, Main.properties.getProperty("summaryLabelDefaultFirstLine6").formatted(secondLine));
		setSummaryDropSpaceText(7, Main.properties.getProperty("summaryLabelDefaultFirstLine7").formatted(secondLine));
		setSummaryDropSpaceText(8, Main.properties.getProperty("summaryLabelDefaultFirstLine8").formatted(secondLine));
	}

	public void setBackgroundColor1() {
		Main.backgroundColor = getColorHexCode(circle1.getFill());
        setBackgroundColor();
    }

    public void setBackgroundColor2() {
	    Main.backgroundColor = getColorHexCode(circle2.getFill());
        setBackgroundColor();
    }

    public void setBackgroundColor3() {
	    Main.backgroundColor = getColorHexCode(circle3.getFill());
        setBackgroundColor();
    }

    public void setBackgroundColor4() {
	    Main.backgroundColor = getColorHexCode(circle4.getFill());
        setBackgroundColor();
    }

    public void setBackgroundColor5() {
	    Main.backgroundColor = getColorHexCode(circle5.getFill());
        setBackgroundColor();
    }

	void setBackgroundColor() {
		Main.root.setStyle("-fx-background-color: " + Main.backgroundColor + ";");
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
		allLabels = new Label[]{
				upperDropSpace,
				lowerDropSpace,
				resultLabel,
				notificationLabel,
				notificationLabel2,
				infoLabel,
				summaryDropSpace1,
				summaryDropSpace2,
				summaryDropSpace3,
				summaryDropSpace4,
				summaryDropSpace5,
				summaryDropSpace6,
				summaryDropSpace7,
				summaryDropSpace8
		};
	    borderModifiedLabels = new Label[]{
			    upperDropSpace,
			    lowerDropSpace,
			    summaryDropSpace1,
			    summaryDropSpace2,
			    summaryDropSpace3,
			    summaryDropSpace4,
			    summaryDropSpace5,
			    summaryDropSpace6,
			    summaryDropSpace7,
			    summaryDropSpace8
	    };
	    allSummaryLabelWrappers = new LabelWrapper[]{
			    new LabelWrapper(summaryDropSpace1),
			    new LabelWrapper(summaryDropSpace2),
			    new LabelWrapper(summaryDropSpace3),
			    new LabelWrapper(summaryDropSpace4),
			    new LabelWrapper(summaryDropSpace5),
			    new LabelWrapper(summaryDropSpace6),
			    new LabelWrapper(summaryDropSpace7),
			    new LabelWrapper(summaryDropSpace8)
	    };
	    allButtons = new Button[]{
				downloadFileButton,
				clearResultLabelButton,
				lowerDropSpaceButton,
				clearUpperDropSpaceButton,
				infoButton,
				downloadResultLabelButton,
				downloadSummaryFileButton,
				clearAllSummaryDropSpaceButton,
				clearSummaryDropSpaceButton1,
				clearSummaryDropSpaceButton2,
				clearSummaryDropSpaceButton3,
				clearSummaryDropSpaceButton4,
				clearSummaryDropSpaceButton5,
				clearSummaryDropSpaceButton6,
				clearSummaryDropSpaceButton7,
				clearSummaryDropSpaceButton8,
				checkSummaryDropSpaceButton4,
			    checkSummaryDropSpaceButton6,
			    checkSummaryDropSpaceButton7,
			    checkSummaryDropSpaceButton8,
			    boldTextButton
		};
		boldTextModifiedButtons = new Button[]{
				downloadFileButton,
				clearResultLabelButton,
				lowerDropSpaceButton,
				clearUpperDropSpaceButton,
				infoButton,
				downloadResultLabelButton,
				downloadSummaryFileButton,
				clearAllSummaryDropSpaceButton,
				checkSummaryDropSpaceButton4,
				checkSummaryDropSpaceButton6,
				checkSummaryDropSpaceButton7,
				checkSummaryDropSpaceButton8,
				boldTextButton
		};
		allTexts = new Text[]{
				appearanceText1,
				appearanceText2,
				appearanceText3
		};
    }

    public LabelWrapper getSummaryLabelWrapper(int i) {
		return allSummaryLabelWrappers[i - 1];
    }

	String getColorHexCode(Paint paint) {
		return "#" + paint.toString().substring(2, 8);
	}

    void setTextColor() {
	    for (Text text : allTexts) {
		    text.setFill(Paint.valueOf(Main.textColor));
	    }
	    for (Label label : allLabels) {
		    label.setTextFill(Paint.valueOf(Main.textColor));
	    }
    }
    
    void setBorderColor() {
		border = new Border(new BorderStroke(Paint.valueOf(Main.borderColor), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5)));
	    for (Label label : borderModifiedLabels) {
	    	label.setBorder(border);
	    }
    }

    public void setResultLabelText(String string) {
		resultLabel.setText(string);
    }

    public void setNotificationOpacity(int i) {
	    notificationLabel.setOpacity(i);
	    notificationLabel2.setOpacity(i);
    }

    public void upperDragDropped(DragEvent dragEvent) {
	    DropWorker.dragDropped(dragEvent, 0, this);
    }

    public void upperDragOver(DragEvent dragEvent) {
	    DropWorker.dragOver(dragEvent);
    }

    public void lowerDragDropped(DragEvent dragEvent) {
	    DropWorker.dragDropped(dragEvent, 1, this);
    }

    public void lowerDragOver(DragEvent dragEvent) {
	    DropWorker.dragOver(dragEvent);
    }

    public void setUpperDropSpaceText(String string) {
		upperDropSpace.setText(string);
    }

    public Label getUpperDropSpaceLabel() {
		return upperDropSpace;
    }

	public Label getLowerDropSpaceLabel() {
		return lowerDropSpace;
	}

    public void setLowerDropSpaceText(String string) {
		lowerDropSpace.setText(string);
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
		setUpperDropSpaceText(Main.properties.getProperty("upperLabelDefaultText"));
	}

	public void clearLowerDropSpace() {
		Main.pathToCalculatingFile = null;
		Main.reinforcementHashMap.clear();
		setLowerDropSpaceText(Main.properties.getProperty("lowerLabelDefaultText"));
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

	public String getTableHead() {
		return tableHead.getText();
	}

	public String getBackgroundReinforcement() {
    	return backgroundReinforcement.getText();
	}

	public String getFileName() {
		return fileName.getText();
	}

	public String getSummaryFileName() {
		return summaryTableHead.getText();
	}

	public String getSummaryTableHead() {
		return summaryFileName.getText();
	}

	public void downloadResultLabel() throws IOException {
		Main.downloadNotification();
	}

	public void visitNotificationTab() {
		setNotificationOpacity(0);
	}

	public void setSummaryDropSpaceText(int i, String string) {
		getSummaryLabelWrapper(i).getLabel().setText(string);
		getSummaryLabelWrapper(i).setDefaultText(string);
	}

	public void summaryDragDropped1(DragEvent dragEvent) {
		DropWorker.dragDroppedSummary(dragEvent, 1, this);
	}

	public void summaryDragOver(DragEvent dragEvent) {
		DropWorker.dragOver(dragEvent);
	}

	public void summaryDragDropped2(DragEvent dragEvent) {
		DropWorker.dragDroppedSummary(dragEvent, 2, this);
	}

	public void summaryDragDropped3(DragEvent dragEvent) {
		DropWorker.dragDroppedSummary(dragEvent, 3, this);
	}

	public void summaryDragDropped4(DragEvent dragEvent) {
		DropWorker.dragDroppedSummary(dragEvent, 4, this);
	}

	public void summaryDragDropped5(DragEvent dragEvent) {
		DropWorker.dragDroppedSummary(dragEvent, 5, this);
	}

	public void summaryDragDropped6(DragEvent dragEvent) {
		DropWorker.dragDroppedSummary(dragEvent, 6, this);
	}

	public void summaryDragDropped7(DragEvent dragEvent) {
		DropWorker.dragDroppedSummary(dragEvent, 7, this);
	}

	public void summaryDragDropped8(DragEvent dragEvent) {
		DropWorker.dragDroppedSummary(dragEvent, 8, this);
	}

	public void downloadSummaryFile() {
		Main.downloadSummaryFile();
	}

	public void clearAllSummaryDropSpace() {
		for (int i = 1; i < 9; i++) {
			clearSummaryDropSpace(i);
		}
	}

	public void clearSummaryDropSpace1() {
		clearSummaryDropSpace(1);
	}

	public void clearSummaryDropSpace2() {
		clearSummaryDropSpace(2);
	}

	public void clearSummaryDropSpace3() {
		clearSummaryDropSpace(3);
	}

	public void clearSummaryDropSpace4() {
		clearSummaryDropSpace(4);
	}

	public void clearSummaryDropSpace5() {
		clearSummaryDropSpace(5);
	}

	public void clearSummaryDropSpace6() {
		clearSummaryDropSpace(6);
	}

	public void clearSummaryDropSpace7() {
		clearSummaryDropSpace(7);
	}

	public void clearSummaryDropSpace8() {
		clearSummaryDropSpace(8);
	}

	public void clearSummaryDropSpace(int i) {
		if (Main.summaryPaths.get(i) != null) {
			Main.summaryPaths.get(i).clear();
			getSummaryLabelWrapper(i).resetTextToDefault();
		}
	}

	public void checkSummaryDropSpace4() {
		checkSummaryDropSpace(4);
	}

	public void checkSummaryDropSpace6() {
		checkSummaryDropSpace(6);
	}

	public void checkSummaryDropSpace7() {
		checkSummaryDropSpace(7);
	}

	public void checkSummaryDropSpace8() {
		checkSummaryDropSpace(8);
	}

	public void checkSummaryDropSpace(int i) {
		if (Main.summaryPaths.get(i) != null) {
			Main.checkSummaryDropSpace(i);
		}
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

	public void toggleBoldText() {
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
		for (Label label : allLabels) {
			label.setFont(font3);
		}
		infoLabel.setFont(font2);
		notificationLabel.setFont(font1);
		notificationLabel2.setFont(font1);
		for (Button button : boldTextModifiedButtons) {
			button.setFont(font2);
		}
		for (Text text : allTexts) {
			text.setFont(font3);
		}
	}
}