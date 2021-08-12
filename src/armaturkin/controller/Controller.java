package armaturkin.controller;

import armaturkin.core.*;
import armaturkin.reinforcement.PairDR;
import armaturkin.reinforcement.RFClass;
import armaturkin.reinforcement.StandardsRepository;
import armaturkin.utils.Dev;
import armaturkin.utils.ReinforcementLinearMassInfo;
import armaturkin.view.*;
import armaturkin.workers.DropWorker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Arrays;

public class Controller {

	@FXML
	private AnchorPane anchorPane1;
	@FXML
	private AnchorPane anchorPane3;
	@FXML
	private AnchorPane utilAnchorPane;
	@FXML
	private Label upperDropSpace;
	@FXML
	private Label lowerDropSpace;
	@FXML
	private Label resultLabel;
	@FXML
	private Label notificationLabel;
	@FXML
	private Label notificationLabel2;
	@FXML
	private Label summaryDropSpace1;
	@FXML
	private Label summaryDropSpace2;
	@FXML
	private Label summaryDropSpace3;
	@FXML
	private Label summaryDropSpace4;
	@FXML
	private Label summaryDropSpace5;
	@FXML
	private Label summaryDropSpace6;
	@FXML
	private Label summaryDropSpace7;
	@FXML
	private Label summaryDropSpace8;
	@FXML
	private Label favoriteDropSpace;
	@FXML
	private Button downloadFileButton;
	@FXML
	private Button clearResultLabelButton;
	@FXML
	private Button lowerDropSpaceButton;
	@FXML
	private Button clearUpperDropSpaceButton;
	@FXML
	private Button showInfoButton;
	@FXML
	private Button downloadResultLabelButton;
	@FXML
	private Button downloadSummaryFileButton;
	@FXML
	private Button clearAllSummaryDropSpaceButton;
	@FXML
	private Button checkSummaryDropSpaceButton4;
	@FXML
	private Button checkSummaryDropSpaceButton6;
	@FXML
	private Button checkSummaryDropSpaceButton8;
	@FXML
	private Button checkSummaryDropSpaceButton7;
	@FXML
	private Button boldTextButton;
	@FXML
	private Button deleteLogs;
	@FXML
	private Button deleteNotifications;
	@FXML
	private Button forgetFavorite;
	@FXML
	private Button font12Button;
	@FXML
	private Button font14Button;
	@FXML
	private Button font16Button;
	@FXML
	private Button font18Button;
	@FXML
	private Button font20Button;
	@FXML
	private Button mSummaryAddButton;
	@FXML
	private Button restoreWindowSizeButton;
	@FXML
	private Button showReinforcementLinearMassListButton;
	@FXML
	private Button backgroundReinforcementAddButton;
	@FXML
	private Button testButton;
	@FXML
	private Button varStateButton;
	@FXML
	private TextField tableHead;
	@FXML
	private TextField backgroundReinforcement;
	@FXML
	private TextField fileName;
	@FXML
	private TextField summaryFileName;
	@FXML
	private TextField summaryTableHead;
	@FXML
	private TextField logLimit;
	@FXML
	private TextField notificationLimit;
	@FXML
	private TextField mSummaryTextField;
	@FXML
	private TextField mBackgroundTextField;
	@FXML
	private Text appearanceText1;
	@FXML
	private Text appearanceText2;
	@FXML
	private Text appearanceText3;
	@FXML
	private Text settingsText1;
	@FXML
	private Text settingsText2;
	private TextWrapper settingsTextWrapper2;
	@FXML
	private Text settingsText3;
	private TextWrapper settingsTextWrapper3;
	@FXML
	private Text settingsText4;
	@FXML
	private Text settingsText5;
	private TextWrapper settingsTextWrapper5;
	@FXML
	private Text settingsText6;
	private TextWrapper settingsTextWrapper6;
	@FXML
	private Text settingsText7;
	@FXML
	private Text mSummaryEntryText1;
	@FXML
	private Text mSummaryEntryText2;
	@FXML
	private Circle circle1;
	@FXML
	private Circle circle2;
	@FXML
	private Circle circle3;
	@FXML
	private Circle circle4;
	@FXML
	private Circle circle5;
	@FXML
	private Circle circleBorderColor1;
	@FXML
	private Circle circleBorderColor2;
	@FXML
	private Circle circleBorderColor3;
	@FXML
	private Circle circleBorderColor4;
	@FXML
	private Circle circleBorderColor5;
	@FXML
	private CheckBox logCheckBox;
	@FXML
	private CheckBox notificationCheckBox;
	@FXML
	private CheckBox autoParseProductListCheckBox;
	@FXML
	private HBox mSummaryHBox;
	@FXML
	private ChoiceBox<String> mSummaryChoiceBox1;
	@FXML
	private ChoiceBox<Integer> mSummaryChoiceBox2;
	@FXML
	private ChoiceBox<RFClass> mSummaryChoiceBox3;
	@FXML
	private ChoiceBox<PairDR> mBackgroundChoiceBox;

	private Label[] allLabels;
	private Label[] borderModifiedLabels;
	private LabelWrapper[] allSummaryLabelWrappers;
	private Button[] boldTextModifiedButtons;
	private Text[] allTexts;

	public void startSetup() {
		if (!Dev.isDevMode) {
			utilAnchorPane.getChildren().remove(testButton);
			utilAnchorPane.getChildren().remove(varStateButton);
		}
		groupingAppearanceVariables();
		setupBackgroundColor();
		setupTextColor();
		setupFont();
		if (Main.config.isResultLabelFontSizeNotNull()) {
			setResultLabelFont(Main.config.getResultLabelFontSize());
		}
		setNotificationOpacity(0);
		setRedirectLineOpacity(0);
		AddonViews.arrow = new Arrow(AddonViews.arrowLine1, AddonViews.arrowLine2);
		setArrowOpacity(0);
		setupBorderColor();
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

		setupMSummaryChoiceBox();
		setupMBackgroundChoiceBox();
	}

	private void groupingAppearanceVariables() {
		allLabels = new Label[] {
				upperDropSpace,
				lowerDropSpace,
				resultLabel,
				notificationLabel,
				notificationLabel2,
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
				showInfoButton,
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
				forgetFavorite,
				font12Button,
				font14Button,
				font16Button,
				font18Button,
				font20Button,
				mSummaryAddButton,
				restoreWindowSizeButton,
				showReinforcementLinearMassListButton,
				backgroundReinforcementAddButton,
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
				settingsText7,
				mSummaryEntryText1,
				mSummaryEntryText2
		};
	}

	@FXML
	private void setBackgroundColor1() {
		Main.config.setBackgroundColor(getColorHexCode(circle1.getFill()));
        setupBackgroundColor();
    }

	@FXML
	private void setBackgroundColor2() {
	    Main.config.setBackgroundColor(getColorHexCode(circle2.getFill()));
        setupBackgroundColor();
    }

	@FXML
	private void setBackgroundColor3() {
	    Main.config.setBackgroundColor(getColorHexCode(circle3.getFill()));
        setupBackgroundColor();
    }

	@FXML
	private void setBackgroundColor4() {
	    Main.config.setBackgroundColor(getColorHexCode(circle4.getFill()));
        setupBackgroundColor();
    }

	@FXML
	private void setBackgroundColor5() {
	    Main.config.setBackgroundColor(getColorHexCode(circle5.getFill()));
        setupBackgroundColor();
    }

	private void setupBackgroundColor() {
		Main.root.setStyle("-fx-background-color: " + Main.config.getBackgroundColor() + ";");
	}

	@FXML
	private void setTextColor1() {
        Main.config.setTextColor(getColorHexCode(circle1.getFill()));
        setupTextColor();
    }

	@FXML
	private void setTextColor2() {
	    Main.config.setTextColor(getColorHexCode(circle2.getFill()));
        setupTextColor();
    }

	@FXML
	private void setTextColor3() {
	    Main.config.setTextColor(getColorHexCode(circle3.getFill()));
        setupTextColor();
    }

	@FXML
	private void setTextColor4() {
	    Main.config.setTextColor(getColorHexCode(circle4.getFill()));
        setupTextColor();
    }

	@FXML
	private void setTextColor5() {
	    Main.config.setTextColor(getColorHexCode(circle5.getFill()));
        setupTextColor();
    }

    public LabelWrapper getSummaryLabelWrapper(int i) {
		return allSummaryLabelWrappers[i - 1];
    }

	private String getColorHexCode(Paint paint) {
		return "#" + paint.toString().substring(2, 8);
	}

    private void setupTextColor() {
	    for (Text text : allTexts) {
		    text.setFill(Paint.valueOf(Main.config.getTextColor()));
	    }
	    for (Label label : allLabels) {
		    label.setTextFill(Paint.valueOf(Main.config.getTextColor()));
	    }
    }
    
    private void setupBorderColor() {
		Border border = new Border(new BorderStroke(
				Paint.valueOf(Main.config.getBorderColor()),
				BorderStrokeStyle.SOLID,
				CornerRadii.EMPTY,
				new BorderWidths(5)
		));
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

	@FXML
	private void upperDragDropped(DragEvent dragEvent) {
	    DropWorker.dragDropped(dragEvent, 0, this);
    }

	@FXML
	private void upperDragOver(DragEvent dragEvent) {
	    DropWorker.dragOver(dragEvent);
    }

	@FXML
	private void lowerDragDropped(DragEvent dragEvent) {
	    DropWorker.dragDropped(dragEvent, 1, this);
    }

	@FXML
	private void lowerDragOver(DragEvent dragEvent) {
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

    private void setLowerDropSpaceText(String string) {
		lowerDropSpace.setText(string);
    }

	@FXML
	private void downloadFile() {
		Main.downloadCalculatedFile();
	}

	@FXML
	private void clearResultLabel() {
		Main.clearNotification();
	}

	@FXML
	private void clearUpperDropSpace() {
		Main.config.setPathToProductFile(null);
		Main.reinforcementProductHashMap.clear();
		setUpperDropSpaceText(Main.properties.getProperty("upper_label_default_text"));
	}

	@FXML
	private void clearLowerDropSpace() {
		Main.config.setPathToCalculatingFile(null);
		Main.reinforcementHashMap.clear();
		setLowerDropSpaceText(Main.properties.getProperty("lower_label_default_text"));
	}

	@FXML
	private void showInfoStage() throws IOException {
    	if (Stages.infoStage == null) {
    		Stages.infoStage = new Stage();
    		Label label = new FXMLLoader(Main.class.getResource("/armaturkin/fxml/Info_label.fxml")).load();
    		label.setBackground(getUserBackgroundColor());
    		label.setFont(getFont());
    		label.setTextFill(Paint.valueOf(Main.config.getTextColor()));
		    Stages.infoStage.setScene(new Scene(label));
		    Stages.infoStage.setTitle(Main.properties.getProperty("info_stage_name"));
		    Stages.infoStage.initStyle(StageStyle.UTILITY);
		    Stages.primary.setOnCloseRequest(windowEvent -> Stages.closeAll());
	    }
    	Stages.infoStage.show();
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

	@FXML
	private void downloadResultLabel() {
		try {
			Main.saveNotification();
		} catch (Exception e) {
			Main.log.add(e);
		}
	}

	@FXML
	private void visitNotificationTab() {
		setNotificationOpacity(0);
	}

	private void setSummaryDropSpaceText(int i, String string) {
		getSummaryLabelWrapper(i).getLabel().setText(string);
		getSummaryLabelWrapper(i).setDefaultText(string);
	}

	@FXML
	private void summaryDragDropped1(DragEvent dragEvent) {
		DropWorker.summaryDragDropped(dragEvent, 1, this);
	}

	@FXML
	private void summaryDragOver(DragEvent dragEvent) {
		DropWorker.dragOver(dragEvent);
	}

	@FXML
	private void summaryDragDropped2(DragEvent dragEvent) {
		DropWorker.summaryDragDropped(dragEvent, 2, this);
	}

	@FXML
	private void summaryDragDropped3(DragEvent dragEvent) {
		DropWorker.summaryDragDropped(dragEvent, 3, this);
	}

	@FXML
	private void summaryDragDropped4(DragEvent dragEvent) {
		DropWorker.summaryDragDropped(dragEvent, 4, this);
	}

	@FXML
	private void summaryDragDropped5(DragEvent dragEvent) {
		DropWorker.summaryDragDropped(dragEvent, 5, this);
	}

	@FXML
	private void summaryDragDropped6(DragEvent dragEvent) {
		DropWorker.summaryDragDropped(dragEvent, 6, this);
	}

	@FXML
	private void summaryDragDropped7(DragEvent dragEvent) {
		DropWorker.summaryDragDropped(dragEvent, 7, this);
	}

	@FXML
	private void summaryDragDropped8(DragEvent dragEvent) {
		DropWorker.summaryDragDropped(dragEvent, 8, this);
	}

	@FXML
	private void downloadSummaryFile() {
		Main.downloadSummaryFile();
	}

	@FXML
	private void clearAllSummaryDropSpace() {
		for (int i = 1; i < 9; i++) {
			clearSummaryDropSpace(i);
		}
	}

	@FXML
	private void clearSummaryDropSpace1() {
		clearSummaryDropSpace(1);
	}

	@FXML
	private void clearSummaryDropSpace2() {
		clearSummaryDropSpace(2);
	}

	@FXML
	private void clearSummaryDropSpace3() {
		clearSummaryDropSpace(3);
	}

	@FXML
	private void clearSummaryDropSpace4() {
		clearSummaryDropSpace(4);
	}

	@FXML
	private void clearSummaryDropSpace5() {
		clearSummaryDropSpace(5);
	}

	@FXML
	private void clearSummaryDropSpace6() {
		clearSummaryDropSpace(6);
	}

	@FXML
	private void clearSummaryDropSpace7() {
		clearSummaryDropSpace(7);
	}

	@FXML
	private void clearSummaryDropSpace8() {
		clearSummaryDropSpace(8);
	}

	private void clearSummaryDropSpace(int i) {
		if (Main.summaryPaths.get(i) != null) {
			Main.summaryPaths.get(i).clear();
			getSummaryLabelWrapper(i).resetTextToDefault();
		}
	}

	@FXML
	private void checkSummaryDropSpace4() {
		checkSummaryDropSpace(4);
	}

	@FXML
	private void checkSummaryDropSpace6() {
		checkSummaryDropSpace(6);
	}

	@FXML
	private void checkSummaryDropSpace7() {
		checkSummaryDropSpace(7);
	}

	@FXML
	private void checkSummaryDropSpace8() {
		checkSummaryDropSpace(8);
	}

	private void checkSummaryDropSpace(int i) {
		if (Main.summaryPaths.get(i) != null) {
			Main.checkSummaryDropSpace(i);
		}
	}

	@FXML
	private void setBorderColor1() {
		Main.config.setBorderColor(getColorHexCode(circleBorderColor1.getFill()));
		setupBorderColor();
	}

	@FXML
	private void setBorderColor2() {
		Main.config.setBorderColor(getColorHexCode(circleBorderColor2.getFill()));
		setupBorderColor();
	}

	@FXML
	private void setBorderColor3() {
		Main.config.setBorderColor(getColorHexCode(circleBorderColor3.getFill()));
		setupBorderColor();
	}

	@FXML
	private void setBorderColor4() {
		Main.config.setBorderColor(getColorHexCode(circleBorderColor4.getFill()));
		setupBorderColor();
	}

	@FXML
	private void setBorderColor5() {
		Main.config.setBorderColor(getColorHexCode(circleBorderColor5.getFill()));
		setupBorderColor();
	}

	@FXML
	private void toggleBoldText() {
		Main.config.toggleBoldText();
		setupFont();
	}

	private void setupFont() {
		if (Main.config.getBoldText()) {
			String font = "System Bold";
			setFont(new Font(font, 14), new Font(font, 16), new Font(font, 20));
		}
		if (!Main.config.getBoldText()) {
			String font = "System";
			setFont(new Font(font, 14), new Font(font, 16), new Font(font, 20));
		}
	}

	private void setFont(Font font1, Font font2, Font font3) {
		for (Label label : allLabels) {
			label.setFont(font3);
		}
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

	@FXML
	private void toggleWriteLog() {
		Main.config.toggleWriteLog();
	}

	@FXML
	private void toggleWriteNotification() {
		Main.config.toggleWriteNotification();
	}

	private void setText() {
		try {
			settingsTextWrapper2.setText(settingsTextWrapper2.getDefaultText().formatted(Main.config.getLogStorageLimit()));
			settingsTextWrapper3.setText(
					settingsTextWrapper3.getDefaultText().formatted(
							StorageCleaner.getStorageSize(Root.programRootPath + Root.get("log_storage_directory")),
							new LongWrapper(StorageCleaner.getSize(Root.programRootPath + Root.get("log_storage_directory"))).toString()
					)
			);
			settingsTextWrapper5.setText(settingsTextWrapper5.getDefaultText().formatted(Main.config.getNotificationStorageLimit()));
			settingsTextWrapper6.setText(
					settingsTextWrapper6.getDefaultText().formatted(
							StorageCleaner.getStorageSize(Root.programRootPath + Root.get("notification_storage_directory")),
							new LongWrapper(StorageCleaner.getSize(Root.programRootPath + Root.get("notification_storage_directory"))).toString()
					)
			);
		} catch (Exception e) {
			Main.log.add(e);
		}
	}

	@FXML
	private void deleteLogs() {
		Main.deleteStorage(Root.programRootPath + Root.get("log_storage_directory"));
		setText();
	}

	@FXML
	private void deleteNotifications() {
		Main.deleteStorage(Root.programRootPath + Root.get("notification_storage_directory"));
		setText();
	}

	@FXML
	private void visitSettingsTab() {
		Main.parseTextField(0, logLimit.getText());
		Main.parseTextField(1, notificationLimit.getText());
		logLimit.clear();
		notificationLimit.clear();
		setText();
	}

	@FXML
	private void forgetFavorite() {
		Main.config.setFavoritePath(null);
		setFavoriteDropSpaceText(Main.properties.getProperty("favorite_is_off"));
	}

	@FXML
	private void favoriteDragOver(DragEvent dragEvent) {
		DropWorker.dragOver(dragEvent);
	}

	@FXML
	private void favoriteDragDropped(DragEvent dragEvent) {
		DropWorker.favoriteDragDropped(dragEvent, this);
	}

	public Label getFavoriteDropSpaceLabel() {
		return favoriteDropSpace;
	}

	public void setFavoriteDropSpaceText(String string) {
		favoriteDropSpace.setText(string);
	}

	@FXML
	private void toggleAutoParseProductList() {
		Main.config.toggleAutoParseProductList();
	}

	@FXML
	private void setResultLabelFont12() {
		setResultLabelFont(12);
	}

	@FXML
	private void setResultLabelFont14() {
		setResultLabelFont(14);
	}

	@FXML
	private void setResultLabelFont16() {
		setResultLabelFont(16);
	}

	@FXML
	private void setResultLabelFont18() {
		setResultLabelFont(18);
	}

	@FXML
	private void setResultLabelFont20() {
		setResultLabelFont(20);
	}

	private void setResultLabelFont(int i) {
		Font font = new Font(resultLabel.getFont().getName(), i);
		resultLabel.setFont(font);
		Main.config.setResultLabelFontSize(i);
	}

	@FXML
	private void click1(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(1);
		}
	}

	@FXML
	private void click2(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(2);
		}
	}

	@FXML
	private void click3(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(3);
		}
	}

	@FXML
	private void click4(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(4);
		}
	}

	@FXML
	private void click5(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(5);
		}
	}

	@FXML
	private void click7(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(7);
		}
	}

	@FXML
	private void click8(MouseEvent mouseEvent) {
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
		AddonViews.redirectLine.setOpacity(i);
	}

	public void setArrowOpacity(int i) {
		AddonViews.arrow.setOpacity(i);
	}

	private void setupMSummaryChoiceBox() {
		mSummaryChoiceBox1.setItems(FXCollections.observableArrayList(
				Arrays.asList(Main.properties.getProperty("content_row").split("-"))
		));
		mSummaryChoiceBox1.setValue(Main.properties.getProperty("content_row").split("-")[0]);
		mSummaryChoiceBox2.setItems(FXCollections.observableList(StandardsRepository.getDiametersAsList()));
		mSummaryChoiceBox2.setValue(StandardsRepository.diameters[2]);
		mSummaryChoiceBox3.setItems(FXCollections.observableArrayList(
				RFClass.A240,
				RFClass.A400,
				RFClass.A500,
				RFClass.A500S,
				RFClass.A600
		));
		mSummaryChoiceBox3.setValue(RFClass.A500S);
	}

	private void setupMBackgroundChoiceBox() {
		mBackgroundChoiceBox.setItems(FXCollections.observableList(StandardsRepository.getPairsAsList()));
		mBackgroundChoiceBox.setValue(StandardsRepository.pairs[1]);
	}

	@FXML
	private void addManuallySummaryEntry() {
		ManuallyEntry.addManuallySummaryEntry(
				mSummaryChoiceBox1.getValue(),
				mSummaryChoiceBox2.getValue(),
				mSummaryChoiceBox3.getValue(),
				mSummaryTextField.getText()
		);
	}

	@FXML
	private void addBackgroundReinforcement() {
		ManuallyEntry.addBackgroundReinforcement(mBackgroundChoiceBox.getValue(), mBackgroundTextField.getText());
	}

	@FXML
	private void restoreWindowSize() {
		Stages.primary.setHeight(Stages.defaultHeight);
		Stages.primary.setWidth(Stages.defaultWidth);
	}

	@FXML
	private void showReinforcementLinearMassListButton() {
		if (Stages.reinforcementLinearMassListStage == null) {
			Stages.reinforcementLinearMassListStage = new Stage();
			Label label = new Label(ReinforcementLinearMassInfo.get());
			label.setBackground(getUserBackgroundColor());
			label.setFont(new Font("Consolas", 20));
			label.setTextFill(Paint.valueOf(Main.config.getTextColor()));
			label.setAlignment(Pos.CENTER);
			Stages.reinforcementLinearMassListStage.setScene(new Scene(label));
			Stages.reinforcementLinearMassListStage.setTitle(Main.properties.getProperty("reinforcement_linear_mass_list_stage_name"));
			Stages.reinforcementLinearMassListStage.initStyle(StageStyle.UTILITY);
			Stages.primary.setOnCloseRequest(windowEvent -> Stages.closeAll());
		}
		Stages.reinforcementLinearMassListStage.show();
	}

	@Deprecated
	public Background getBackgroundSample() {
		return notificationLabel.getBackground();
	}

	@Deprecated
	public TextAlignment getTextAlignmentSample() {
		return notificationLabel.getTextAlignment();
	}

	public void mSummaryHBoxAdd(Label label) {
		mSummaryHBox.getChildren().add(label);
	}

	public void mSummaryHBoxRemove(Label label) {
		mSummaryHBox.getChildren().remove(label);
	}

	public double getMSummaryHBoxPrefHeight() {
		return mSummaryHBox.getPrefHeight();
	}

	public void addArrowLines() {
		ObservableList<Node> children = anchorPane3.getChildren();
		children.add(0, AddonViews.redirectLine);
		children.add(1, AddonViews.arrowLine1);
		children.add(2, AddonViews.arrowLine2);
	}

	private Font getFont() {
		return upperDropSpace.getFont();
	}

	/**
	 * @return user {@link Background} color
	 */
	private Background getUserBackgroundColor() {
		return new Background(new BackgroundFill(
				Color.valueOf(Main.config.getBackgroundColor()),
				CornerRadii.EMPTY,
				Insets.EMPTY
		));
	}

	@FXML
	private void doTest() throws Exception {
		if (Dev.isDevMode) {
			/*System.out.println("test start");
			var v = (AnchorPane) new FXMLLoader(getClass().getResource("/armaturkin/fxml/Arrow_lines.fxml")).load();
			System.out.println(v);
			System.out.println(v.getChildren());
			var v1 = (Line) v.getChildren().get(0);
			var v2 = (Line) v.getChildren().get(1);
			var v3 = (Line) v.getChildren().get(2);
			anchorPane1.getChildren().add(v1);
			anchorPane1.getChildren().add(v2);
			anchorPane1.getChildren().add(v3);
			System.out.println(v1.getId());
			System.out.println("test end");*/

			Main.addNotification("just one string");
		}
	}

	@FXML
	private void printVarState() {
		if (Dev.isDevMode) {
			Dev.printVarState();
		}
	}
}