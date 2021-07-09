package armaturkin.controller;

import armaturkin.core.*;
import armaturkin.view.Arrow;
import armaturkin.view.LabelWrapper;
import armaturkin.view.TextWrapper;
import armaturkin.workers.DropWorker;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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
	public Label favoriteDropSpace;
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
	public Button deleteLogs;
	public Button deleteNotifications;
	public Button forgotFavorite;
	public Button font12Button;
	public Button font14Button;
	public Button font16Button;
	public Button font18Button;
	public Button font20Button;
	public TextField tableHead;
	public TextField backgroundReinforcement;
	public TextField fileName;
	public TextField summaryFileName;
	public TextField summaryTableHead;
	public TextField logLimit;
	public TextField notificationLimit;
	Text[] allTexts;
	public Text appearanceText1;
    public Text appearanceText2;
	public Text appearanceText3;
	public Text settingsText1;
	public Text settingsText2;
	TextWrapper settingsTextWrapper2;
	public Text settingsText3;
	TextWrapper settingsTextWrapper3;
	public Text settingsText4;
	public Text settingsText5;
	TextWrapper settingsTextWrapper5;
	public Text settingsText6;
	TextWrapper settingsTextWrapper6;
	public Text settingsText7;
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
	public CheckBox logCheckBox;
	public CheckBox notificationCheckBox;
	public CheckBox autoParseProductListCheckBox;
	public Line redirectLine;
	public Line arrowLine1;
	public Line arrowLine2;
	public Arrow arrow;

	public void startSetup() {
		groupAppearanceVariables();
		setBackgroundColor();
		setTextColor();
		setFont();
		if (Main.config.isResultLabelFontSizeNotNull()) {
			setResultLabelFont(Main.config.getResultLabelFontSize());
		}
		setNotificationOpacity(0);
		setRedirectLineOpacity(0);
		arrow = new Arrow(arrowLine1, arrowLine2);
		setArrowOpacity(0);
		setBorderColor();
		setupInfoLabel();
		setUpperDropSpaceText(Main.properties.getProperty("upper_label_default_text"));
		setLowerDropSpaceText(Main.properties.getProperty("lower_label_default_text"));

		String secondLine = Main.properties.getProperty("summary_label_default_second_line");
		setSummaryDropSpaceText(1, Main.properties.getProperty("summary_label_default_first_line_1").formatted(secondLine));
		setSummaryDropSpaceText(2, Main.properties.getProperty("summary_label_default_first_line_2").formatted(secondLine));
		setSummaryDropSpaceText(3, Main.properties.getProperty("summary_label_default_first_line_3").formatted(secondLine));
		setSummaryDropSpaceText(4, Main.properties.getProperty("summary_label_default_first_line_4").formatted(secondLine));
		setSummaryDropSpaceText(5, Main.properties.getProperty("summary_label_default_first_line_5").formatted(secondLine));
		setSummaryDropSpaceText(6, Main.properties.getProperty("summary_label_default_first_line_6").formatted(secondLine));
		setSummaryDropSpaceText(7, Main.properties.getProperty("summary_label_default_first_line_7").formatted(secondLine));
		setSummaryDropSpaceText(8, Main.properties.getProperty("summary_label_default_first_line_8").formatted(secondLine));

		setFavoriteDropSpaceText(Main.properties.getProperty("favorite_is_off"));

		settingsTextWrapper2 = new TextWrapper(settingsText2);
		settingsTextWrapper3 = new TextWrapper(settingsText3);
		settingsTextWrapper5 = new TextWrapper(settingsText5);
		settingsTextWrapper6 = new TextWrapper(settingsText6);
	}

	private void groupAppearanceVariables() {
		allLabels = new Label[] {
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
				summaryDropSpace8,
				favoriteDropSpace
		};
		borderModifiedLabels = new Label[] {
				upperDropSpace,
				lowerDropSpace,
				summaryDropSpace1,
				summaryDropSpace2,
				summaryDropSpace3,
				summaryDropSpace4,
				summaryDropSpace5,
				summaryDropSpace6,
				summaryDropSpace7,
				summaryDropSpace8,
				favoriteDropSpace
		};
		allSummaryLabelWrappers = new LabelWrapper[] {
				new LabelWrapper(summaryDropSpace1),
				new LabelWrapper(summaryDropSpace2),
				new LabelWrapper(summaryDropSpace3),
				new LabelWrapper(summaryDropSpace4),
				new LabelWrapper(summaryDropSpace5),
				new LabelWrapper(summaryDropSpace6),
				new LabelWrapper(summaryDropSpace7),
				new LabelWrapper(summaryDropSpace8)
		};
		boldTextModifiedButtons = new Button[] {
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
				boldTextButton,
				deleteLogs,
				deleteNotifications,
				forgotFavorite,
				font12Button,
				font14Button,
				font16Button,
				font18Button,
				font20Button
		};
		allTexts = new Text[] {
				appearanceText1,
				appearanceText2,
				appearanceText3,
				settingsText1,
				settingsText2,
				settingsText3,
				settingsText4,
				settingsText5,
				settingsText6,
				settingsText7
		};
	}

	public void setBackgroundColor1() {
		Main.config.setBackgroundColor(getColorHexCode(circle1.getFill()));
        setBackgroundColor();
    }

    public void setBackgroundColor2() {
	    Main.config.setBackgroundColor(getColorHexCode(circle2.getFill()));
        setBackgroundColor();
    }

    public void setBackgroundColor3() {
	    Main.config.setBackgroundColor(getColorHexCode(circle3.getFill()));
        setBackgroundColor();
    }

    public void setBackgroundColor4() {
	    Main.config.setBackgroundColor(getColorHexCode(circle4.getFill()));
        setBackgroundColor();
    }

    public void setBackgroundColor5() {
	    Main.config.setBackgroundColor(getColorHexCode(circle5.getFill()));
        setBackgroundColor();
    }

	void setBackgroundColor() {
		Main.root.setStyle("-fx-background-color: " + Main.config.getBackgroundColor() + ";");
	}

    public void setTextColor1() {
        Main.config.setTextColor(getColorHexCode(circle1.getFill()));
        setTextColor();
    }

    public void setTextColor2() {
	    Main.config.setTextColor(getColorHexCode(circle2.getFill()));
        setTextColor();
    }

    public void setTextColor3() {
	    Main.config.setTextColor(getColorHexCode(circle3.getFill()));
        setTextColor();
    }

    public void setTextColor4() {
	    Main.config.setTextColor(getColorHexCode(circle4.getFill()));
        setTextColor();
    }

    public void setTextColor5() {
	    Main.config.setTextColor(getColorHexCode(circle5.getFill()));
        setTextColor();
    }

    public LabelWrapper getSummaryLabelWrapper(int i) {
		return allSummaryLabelWrappers[i - 1];
    }

	String getColorHexCode(Paint paint) {
		return "#" + paint.toString().substring(2, 8);
	}

    void setTextColor() {
	    for (Text text : allTexts) {
		    text.setFill(Paint.valueOf(Main.config.getTextColor()));
	    }
	    for (Label label : allLabels) {
		    label.setTextFill(Paint.valueOf(Main.config.getTextColor()));
	    }
    }
    
    void setBorderColor() {
		border = new Border(new BorderStroke(Paint.valueOf(Main.config.getBorderColor()), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5)));
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
		Main.config.setPathToProductFile(null);
		Main.reinforcementProductHashMap.clear();
		setUpperDropSpaceText(Main.properties.getProperty("upper_label_default_text"));
	}

	public void clearLowerDropSpace() {
		Main.config.setPathToCalculatingFile(null);
		Main.reinforcementHashMap.clear();
		setLowerDropSpaceText(Main.properties.getProperty("lower_label_default_text"));
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
		return summaryFileName.getText();
	}

	public String getSummaryTableHead() {
		return summaryTableHead.getText();
	}

	public void downloadResultLabel() {
		try {
			Main.saveNotification();
		} catch (Exception e) {
			Main.log.add(e);
		}
	}

	public void visitNotificationTab() {
		setNotificationOpacity(0);
	}

	public void setSummaryDropSpaceText(int i, String string) {
		getSummaryLabelWrapper(i).getLabel().setText(string);
		getSummaryLabelWrapper(i).setDefaultText(string);
	}

	public void summaryDragDropped1(DragEvent dragEvent) {
		DropWorker.summaryDragDropped(dragEvent, 1, this);
	}

	public void summaryDragOver(DragEvent dragEvent) {
		DropWorker.dragOver(dragEvent);
	}

	public void summaryDragDropped2(DragEvent dragEvent) {
		DropWorker.summaryDragDropped(dragEvent, 2, this);
	}

	public void summaryDragDropped3(DragEvent dragEvent) {
		DropWorker.summaryDragDropped(dragEvent, 3, this);
	}

	public void summaryDragDropped4(DragEvent dragEvent) {
		DropWorker.summaryDragDropped(dragEvent, 4, this);
	}

	public void summaryDragDropped5(DragEvent dragEvent) {
		DropWorker.summaryDragDropped(dragEvent, 5, this);
	}

	public void summaryDragDropped6(DragEvent dragEvent) {
		DropWorker.summaryDragDropped(dragEvent, 6, this);
	}

	public void summaryDragDropped7(DragEvent dragEvent) {
		DropWorker.summaryDragDropped(dragEvent, 7, this);
	}

	public void summaryDragDropped8(DragEvent dragEvent) {
		DropWorker.summaryDragDropped(dragEvent, 8, this);
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
		Main.config.setBorderColor(getColorHexCode(circleBorderColor1.getFill()));
		setBorderColor();
	}

	public void setBorderColor2() {
		Main.config.setBorderColor(getColorHexCode(circleBorderColor2.getFill()));
		setBorderColor();
	}

	public void setBorderColor3() {
		Main.config.setBorderColor(getColorHexCode(circleBorderColor3.getFill()));
		setBorderColor();
	}

	public void setBorderColor4() {
		Main.config.setBorderColor(getColorHexCode(circleBorderColor4.getFill()));
		setBorderColor();
	}

	public void setBorderColor5() {
		Main.config.setBorderColor(getColorHexCode(circleBorderColor5.getFill()));
		setBorderColor();
	}

	public void toggleBoldText() {
		Main.config.toggleBoldText();
		setFont();
	}

	void setFont() {
		if (Main.config.getBoldText()) {
			String font = "System Bold";
			setFont(new Font(font, 14), new Font(font, 16), new Font(font, 20));
		}
		if (!Main.config.getBoldText()) {
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

	public void setCheckBox() {
		logCheckBox.setSelected(Main.config.getWriteLog());
		notificationCheckBox.setSelected(Main.config.getWriteNotification());
		autoParseProductListCheckBox.setSelected(Main.config.getAutoParseProductList());
	}

	public void toggleWriteLog() {
		Main.config.toggleWriteLog();
	}

	public void toggleWriteNotification() {
		Main.config.toggleWriteNotification();
	}

	public void setText() {
		try {
			settingsTextWrapper2.setText(settingsTextWrapper2.getDefaultText().formatted(Main.config.getLogStorageLimit()));
			settingsTextWrapper3.setText(
					settingsTextWrapper3.getDefaultText().formatted(
							StorageCleaner.getStorageSize(Main.programRootPath + Main.logStorageDirectory),
							new LongWrapper(StorageCleaner.getSize(Main.programRootPath + Main.logStorageDirectory)).toString()
					)
			);
			settingsTextWrapper5.setText(settingsTextWrapper5.getDefaultText().formatted(Main.config.getNotificationStorageLimit()));
			settingsTextWrapper6.setText(
					settingsTextWrapper6.getDefaultText().formatted(
							StorageCleaner.getStorageSize(Main.programRootPath + Main.notificationStorageDirectory),
							new LongWrapper(StorageCleaner.getSize(Main.programRootPath + Main.notificationStorageDirectory)).toString()
					)
			);
		} catch (Exception e) {
			Main.log.add(e);
		}
	}

	public void deleteLogs() {
		Main.deleteStorage(Main.programRootPath + Main.logStorageDirectory);
		setText();
	}

	public void deleteNotifications() {
		Main.deleteStorage(Main.programRootPath + Main.notificationStorageDirectory);
		setText();
	}

	public void visitSettingsTab() {
		Main.parseTextField(0, logLimit.getText());
		Main.parseTextField(1, notificationLimit.getText());
		logLimit.clear();
		notificationLimit.clear();
		setText();
	}

	public void forgotFavorite() {
		Main.config.setFavoritePath(null);
		setFavoriteDropSpaceText(Main.properties.getProperty("favorite_is_off"));
	}

	public void favoriteDragOver(DragEvent dragEvent) {
		DropWorker.dragOver(dragEvent);
	}

	public void favoriteDragDropped(DragEvent dragEvent) {
		DropWorker.favoriteDragDropped(dragEvent, this);
	}

	public Label getFavoriteDropSpaceLabel() {
		return favoriteDropSpace;
	}

	public void setFavoriteDropSpaceText(String string) {
		favoriteDropSpace.setText(string);
	}

	public void toggleAutoParseProductList() {
		Main.config.toggleAutoParseProductList();
	}

	public void setResultLabelFont12() {
		setResultLabelFont(12);
	}

	public void setResultLabelFont14() {
		setResultLabelFont(14);
	}

	public void setResultLabelFont16() {
		setResultLabelFont(16);
	}

	public void setResultLabelFont18() {
		setResultLabelFont(18);
	}

	public void setResultLabelFont20() {
		setResultLabelFont(20);
	}

	public void setResultLabelFont(int i) {
		Font font = new Font(resultLabel.getFont().getName(), i);
		resultLabel.setFont(font);
		Main.config.setResultLabelFontSize(i);
	}

	public void click1(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(1);
		}
	}

	public void click2(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(2);
		}
	}

	public void click3(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(3);
		}
	}

	public void click4(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(4);
		}
	}

	public void click5(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(5);
		}
	}

	public void click7(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(7);
		}
	}

	public void click8(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(8);
		}
	}

	public Label[] getSummaryLabels() {
		Label[] labels = new Label[allSummaryLabelWrappers.length];
		for (int i = 0; i < labels.length; i++) {
			labels[i] = allSummaryLabelWrappers[i].getLabel();
		}
		return labels;
	}

	public void setRedirectLineOpacity(int i) {
		redirectLine.setOpacity(i);
	}

	public void setArrowOpacity(int i) {
		arrow.setOpacity(i);
	}

	public void addNote(MouseEvent mouseEvent) {
	}
}