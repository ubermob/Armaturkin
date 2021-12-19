package armaturkin.controller;

import armaturkin.core.*;
import armaturkin.manuallyentry.ManuallyEntry;
import armaturkin.reinforcement.PairDR;
import armaturkin.reinforcement.RFClass;
import armaturkin.reinforcement.StandardsRepository;
import armaturkin.steelcomponent.HotRolledSteelType;
import armaturkin.steelcomponent.Image;
import armaturkin.steelcomponent.SteelComponentRepository;
import armaturkin.summaryoutput.SummaryRedirectManager;
import armaturkin.utils.Dev;
import armaturkin.utils.test.Test;
import armaturkin.view.*;
import armaturkin.workers.DropWorker;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
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

import java.io.IOException;
import java.util.*;

import static armaturkin.core.Main.getProperty;
import static javafx.collections.FXCollections.observableList;
import static javafx.collections.FXCollections.observableArrayList;

public class Controller {

	@FXML
	private AnchorPane anchorPane3, utilAnchorPane;
	@FXML
	private Label upperDropSpace, lowerDropSpace, resultLabel, notificationLabel, notificationLabel2,
			summaryDropSpace1, summaryDropSpace2, summaryDropSpace3, summaryDropSpace4, summaryDropSpace5,
			summaryDropSpace6, summaryDropSpace7, summaryDropSpace8, favoriteDropSpace;
	@FXML
	private Button downloadFileButton, clearResultLabelButton, lowerDropSpaceButton, clearUpperDropSpaceButton,
			showInfoButton, downloadResultLabelButton, downloadSummaryFileButton, clearAllSummaryDropSpaceButton,
			checkSummaryDropSpaceButton4, checkSummaryDropSpaceButton6, checkSummaryDropSpaceButton8,
			checkSummaryDropSpaceButton7, boldTextButton, deleteLogs, deleteNotifications, forgetFavorite,
			font12Button, font14Button, font16Button, font18Button, font20Button, mSummaryAddButton, mSummaryAddButton2,
			restoreWindowSizeButton, showReinforcementLinearMassListButton, backgroundReinforcementAddButton,
			showHotRolledSteelCodeButton, testButton, varStateButton;
	@FXML
	private TextField tableHead, backgroundReinforcement, fileName, summaryFileName, summaryTableHead, logLimit,
			notificationLimit, mSummaryTextField, mBackgroundTextField, mSummaryTextField2;
	@FXML
	private Text appearanceText1, appearanceText2, appearanceText3, settingsText1, settingsText2, settingsText3,
			settingsText4, settingsText5, settingsText6, settingsText7, mSummaryEntryText1, mSummaryEntryText2,
			mSummaryEntryText3, notificationText;
	@FXML
	private Circle circle1, circle2, circle3, circle4, circle5, circleBorderColor1, circleBorderColor2,
			circleBorderColor3, circleBorderColor4, circleBorderColor5;
	@FXML
	private CheckBox logCheckBox, notificationCheckBox, autoParseProductListCheckBox;
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
	@FXML
	private ChoiceBox<HotRolledSteelType> mSummaryChoiceBox4;
	@FXML
	private ChoiceBox<Image> mSummaryChoiceBox5;
	@FXML
	private ChoiceBox<Number> mSummaryChoiceBox6, mSummaryChoiceBox7;

	private Label[] allLabels;
	private Label[] borderModifiedLabels;
	private LabelWrapper[] allSummaryLabelWrappers;
	private Button[] boldTextModifiedButtons;
	private Text[] allTexts;
	private TextWrapper settingsTextWrapper2, settingsTextWrapper3, settingsTextWrapper5, settingsTextWrapper6;
	private ChoiceBoxWrapper choiceBoxWrapper1, choiceBoxWrapper2, choiceBoxWrapper3;

	public void startSetup() {
		if (!Dev.isDevMode) {
			utilAnchorPane.getChildren().remove(testButton);
			utilAnchorPane.getChildren().remove(varStateButton);
		}
		groupingAppearanceVariables();
		setBackgroundColor();
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
		setUpperDropSpaceText(getProperty("upper_label_default_text"));
		setLowerDropSpaceText(getProperty("lower_label_default_text"));

		String secondLine = getProperty("summary_label_default_second_line");
		setSummaryDropSpaceText(1, getProperty("summary_label_default_first_line_1").formatted(secondLine));
		setSummaryDropSpaceText(2, getProperty("summary_label_default_first_line_2").formatted(secondLine));
		setSummaryDropSpaceText(3, getProperty("summary_label_default_first_line_3").formatted(secondLine));
		setSummaryDropSpaceText(4, getProperty("summary_label_default_first_line_4").formatted(secondLine));
		setSummaryDropSpaceText(5, getProperty("summary_label_default_first_line_5").formatted(secondLine));
		setSummaryDropSpaceText(6, getProperty("summary_label_default_first_line_6").formatted(secondLine));
		setSummaryDropSpaceText(7, getProperty("summary_label_default_first_line_7").formatted(secondLine));
		setSummaryDropSpaceText(8, getProperty("summary_label_default_first_line_8").formatted(secondLine));

		setFavoriteDropSpaceText(getProperty("favorite_is_off"));

		settingsTextWrapper2 = new TextWrapper(settingsText2);
		settingsTextWrapper3 = new TextWrapper(settingsText3);
		settingsTextWrapper5 = new TextWrapper(settingsText5);
		settingsTextWrapper6 = new TextWrapper(settingsText6);

		setupChoiceBoxWrapper();
		setupMSummaryChoiceBox();
		setupMBackgroundChoiceBox();
	}

	private void setupChoiceBoxWrapper() {
		choiceBoxWrapper1 = new ChoiceBoxWrapper(mSummaryChoiceBox5);
		choiceBoxWrapper2 = new ChoiceBoxWrapper(mSummaryChoiceBox6);
		choiceBoxWrapper3 = new ChoiceBoxWrapper(mSummaryChoiceBox7);
	}

	private void groupingAppearanceVariables() {
		allLabels = new Label[]{
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
				summaryDropSpace8,
				favoriteDropSpace
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
		boldTextModifiedButtons = new Button[]{
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
				showHotRolledSteelCodeButton,
				mSummaryAddButton2
		};
		allTexts = new Text[]{
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
				mSummaryEntryText2,
				mSummaryEntryText3,
				notificationText
		};
	}

	@FXML
	private void setBackgroundColor1() {
		Main.config.setBackgroundColor(getColorHexCode(circle1.getFill()));
		setBackgroundColor();
	}

	@FXML
	private void setBackgroundColor2() {
		Main.config.setBackgroundColor(getColorHexCode(circle2.getFill()));
		setBackgroundColor();
	}

	@FXML
	private void setBackgroundColor3() {
		Main.config.setBackgroundColor(getColorHexCode(circle3.getFill()));
		setBackgroundColor();
	}

	@FXML
	private void setBackgroundColor4() {
		Main.config.setBackgroundColor(getColorHexCode(circle4.getFill()));
		setBackgroundColor();
	}

	@FXML
	private void setBackgroundColor5() {
		Main.config.setBackgroundColor(getColorHexCode(circle5.getFill()));
		setBackgroundColor();
	}

	private void setBackgroundColor() {
		Stages.primary.getScene().getRoot().setStyle("-fx-background-color: " + Main.config.getBackgroundColor() + ";");
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
		for (var text : allTexts) {
			text.setFill(Paint.valueOf(Main.config.getTextColor()));
		}
		for (var label : allLabels) {
			label.setTextFill(Paint.valueOf(Main.config.getTextColor()));
		}
	}

	private void setupBorderColor() {
		for (var label : borderModifiedLabels) {
			label.setStyle("-fx-border-color: %s; -fx-border-width: %d;"
					.formatted(Main.config.getBorderColor(), 5));
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
		setUpperDropSpaceText(getProperty("upper_label_default_text"));
	}

	@FXML
	private void clearLowerDropSpace() {
		Main.config.setPathToCalculatingFile(null);
		Main.reinforcementHashMap.clear();
		setLowerDropSpaceText(getProperty("lower_label_default_text"));
	}

	@FXML
	private void showInfoStage() throws IOException {
		Stages.showInfoStage();
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
	private void checkSummaryDropSpace4() throws InterruptedException {
		checkSummaryDropSpace(4);
	}

	@FXML
	private void checkSummaryDropSpace6() throws InterruptedException {
		checkSummaryDropSpace(6);
	}

	@FXML
	private void checkSummaryDropSpace7() throws InterruptedException {
		checkSummaryDropSpace(7);
	}

	@FXML
	private void checkSummaryDropSpace8() throws InterruptedException {
		checkSummaryDropSpace(8);
	}

	private void checkSummaryDropSpace(int i) throws InterruptedException {
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
							StorageCleaner.getStorageSize(Root.programRootPath + Root.getProperty("log_storage_directory")),
							new LongWrapper(StorageCleaner.getSize(Root.programRootPath + Root.getProperty("log_storage_directory"))).toString()
					)
			);
			settingsTextWrapper5.setText(settingsTextWrapper5.getDefaultText().formatted(Main.config.getNotificationStorageLimit()));
			settingsTextWrapper6.setText(
					settingsTextWrapper6.getDefaultText().formatted(
							StorageCleaner.getStorageSize(Root.programRootPath + Root.getProperty("notification_storage_directory")),
							new LongWrapper(StorageCleaner.getSize(Root.programRootPath + Root.getProperty("notification_storage_directory"))).toString()
					)
			);
		} catch (Exception e) {
			Main.log.add(e);
		}
	}

	@FXML
	private void deleteLogs() {
		Main.deleteStorage(Root.programRootPath + Root.getProperty("log_storage_directory"));
		setText();
	}

	@FXML
	private void deleteNotifications() {
		Main.deleteStorage(Root.programRootPath + Root.getProperty("notification_storage_directory"));
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
		setFavoriteDropSpaceText(getProperty("favorite_is_off"));
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
		mSummaryChoiceBox1.setItems(observableArrayList(
				Arrays.asList(getProperty("content_row").split("-"))
		));
		mSummaryChoiceBox1.setValue(getProperty("content_row").split("-")[0]);
		mSummaryChoiceBox2.setItems(observableList(StandardsRepository.getDiametersAsList()));
		mSummaryChoiceBox2.setValue(StandardsRepository.diameters[2]);
		mSummaryChoiceBox3.setItems(observableArrayList(
				RFClass.A240,
				RFClass.A400,
				RFClass.A500,
				RFClass.A500S,
				RFClass.A600
		));
		mSummaryChoiceBox3.setValue(RFClass.A500S);
		mSummaryChoiceBox4.setItems(observableList(HotRolledSteelType.getAsList()));
		mSummaryChoiceBox4.setValue(HotRolledSteelType.EQUAL_LEG_ANGLE);
		mSummaryChoiceBox4.setOnAction(actionEvent -> {
			switch (mSummaryChoiceBox4.getValue()) {
				case EQUAL_LEG_ANGLE -> setEqual();
				case UNEQUAL_LEG_ANGLE -> setUnequal();
				case SHEET -> setSheet();
			}
		});
		setEqual();
	}

	private void setEqual() {
		choiceBoxWrapper1.setList(observableList(SteelComponentRepository.getFullEqualAnglesImages()));
		choiceBoxWrapper2.reset();
		choiceBoxWrapper3.reset();
	}

	private void setUnequal() {
		choiceBoxWrapper1.setList(observableList(SteelComponentRepository.getFullUnequalAnglesImages()));
		choiceBoxWrapper2.reset();
		choiceBoxWrapper3.reset();
	}

	private void setSheet() {
		choiceBoxWrapper1.reset();
		choiceBoxWrapper2.setList(observableList(SteelComponentRepository.getSheetThicknessList()));
		// TODO: now it work in test mode (THIS IS NOT REAL WIDTH)
		List<Number> list = new ArrayList<>();
		int v = 50;
		for (int i = 0; i < 15; i++) {
			list.add(v);
			v += 10;
		}
		choiceBoxWrapper3.setList(observableList(list));
	}

	private void setupMBackgroundChoiceBox() {
		mBackgroundChoiceBox.setItems(observableList(StandardsRepository.getPairsAsList()));
		mBackgroundChoiceBox.setValue(StandardsRepository.pairs[1]);
	}

	@FXML
	private void addManuallySummaryEntry() {
		ManuallyEntry.addManuallySummaryEntry(
				mSummaryChoiceBox1.getValue(),
				mSummaryChoiceBox2.getValue(),
				mSummaryChoiceBox3.getValue(),
				mSummaryTextField.getText() // Mass by kg
		);
	}

	@FXML
	private void addManuallySummaryEntry2() {
		if (mSummaryChoiceBox4.getValue() != HotRolledSteelType.SHEET) {
			ManuallyEntry.addSteelComponentEntry(
					mSummaryChoiceBox5.getValue(),
					mSummaryTextField2.getText() // Length by mm
			);
		} else {
			ManuallyEntry.addSteelSheet(
					mSummaryChoiceBox6.getValue(),
					mSummaryChoiceBox7.getValue(),
					mSummaryTextField2.getText() // Length by mm
			);
		}
	}

	@FXML
	private void addBackgroundReinforcement() {
		// Length by m
		ManuallyEntry.addBackgroundReinforcement(mBackgroundChoiceBox.getValue(), mBackgroundTextField.getText());
	}

	@FXML
	private void restoreWindowSize() {
		Stages.primary.setHeight(Stages.defaultHeight);
		Stages.primary.setWidth(Stages.defaultWidth);
	}

	@FXML
	private void showReinforcementLinearMassList() {
		Stages.showReinforcementLinearMassList();
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

	public Font getFont() {
		return upperDropSpace.getFont();
	}

	/**
	 * @return user {@link Background} color
	 */
	public Background getUserBackgroundColor() {
		return new Background(new BackgroundFill(
				Color.valueOf(Main.config.getBackgroundColor()),
				CornerRadii.EMPTY,
				Insets.EMPTY
		));
	}

	@FXML
	private void showHotRolledSteelCode() throws IOException {
		HotRolledSteelCodeController.show();
	}

	@FXML
	private void printVarState() {
		if (Dev.isDevMode) {
			Dev.printVarState();
		}
	}

	@FXML
	private void doTest() throws Exception {
		if (Dev.isDevMode) {
			Test.test();
		}
	}

	private void localTest() throws Exception {
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
	}
}