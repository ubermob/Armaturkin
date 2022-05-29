package armaturkin.controller;

import armaturkin.core.App;
import armaturkin.core.Configuration;
import armaturkin.core.Root;
import armaturkin.core.StorageCleaner;
import armaturkin.manuallyentry.ManuallyEntry;
import armaturkin.model.FirstHarvestingModel;
import armaturkin.model.ManuallyEntryModel;
import armaturkin.model.SummaryModel;
import armaturkin.reinforcement.PairDR;
import armaturkin.reinforcement.RFClass;
import armaturkin.reinforcement.StandardsRepository;
import armaturkin.service.FirstHarvestingService;
import armaturkin.service.SummaryService;
import armaturkin.steelcomponent.HotRolledSteelType;
import armaturkin.steelcomponent.Image;
import armaturkin.steelcomponent.SteelComponentRepository;
import armaturkin.summaryoutput.SummaryRedirectManager;
import armaturkin.utils.Dev;
import armaturkin.utils.test.Test;
import armaturkin.view.*;
import armaturkin.workers.DropWorker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableList;

public class Controller {

	@FXML
	private AnchorPane anchorPane3, utilAnchorPane;
	@FXML
	private Label upperDropSpace, lowerDropSpace, resultLabel, notificationLabel, notificationLabel2,
			summaryDropSpace1, summaryDropSpace2, summaryDropSpace3, summaryDropSpace4, summaryDropSpace5,
			summaryDropSpace6, summaryDropSpace7, summaryDropSpace8, favoriteDropSpace, summaryBuilderFileDropSpace,
			summaryBuilderFileCreatorDropSpace;
	@FXML
	private Button downloadFileButton, clearResultLabelButton, lowerDropSpaceButton, clearUpperDropSpaceButton,
			showInfoButton1, downloadResultLabelButton, downloadSummaryFileButton, clearAllSummaryDropSpaceButton,
			checkSummaryDropSpaceButton4, checkSummaryDropSpaceButton6, checkSummaryDropSpaceButton8,
			checkSummaryDropSpaceButton7, boldTextButton, deleteLogs, deleteNotifications, forgetFavorite, font10Button,
			font12Button, font14Button, font16Button, font18Button, font20Button, mSummaryAddButton, mSummaryAddButton2,
			restoreWindowSizeButton, showReinforcementLinearMassListButton, backgroundReinforcementAddButton,
			showHotRolledSteelCodeButton, testButton, varStateButton, clearSummaryBuilderDropSpaceButton,
			startNodeSeekerUtilButton, startPythonSteelFrameworkUtilButton, showInfoButton2, showInfoButton3,
			showInfoButton4;
	@FXML
	private TextField tableHead, fileName, summaryFileName, summaryTableHead, logLimit,
			notificationLimit, mSummaryTextField, mBackgroundTextField, mSummaryTextField2;
	@FXML
	private Text appearanceText1, appearanceText2, appearanceText3, settingsText1, settingsText2, settingsText3,
			settingsText4, settingsText5, settingsText6, settingsText7, mSummaryEntryText1, mSummaryEntryText2,
			mSummaryEntryText3, notificationText, mSummaryHelpingText1, mSummaryHelpingText2, mSummaryHelpingText3,
			mSummaryHelpingText4, mSummaryHelpingText5, mSummaryHelpingText6, mSummaryHelpingText7, mSummaryHelpingText8,
			mSummaryHelpingText9, mSummaryHelpingText10, mSummaryHelpingText11, mSummaryHelpingText12;
	@FXML
	private CheckBox logCheckBox, notificationCheckBox, autoParseProductListCheckBox;
	@FXML
	private HBox mSummaryHBox;
	@FXML
	private ChoiceBox<String> mSummaryChoiceBox1, mSummaryChoiceBox8;
	@FXML
	private ChoiceBox<Integer> mSummaryChoiceBox2;
	@FXML
	private ChoiceBox<RFClass> mSummaryChoiceBox3;
	@FXML
	private ChoiceBox<PairDR> mBackgroundChoiceBox;
	@FXML
	private ChoiceBox<String> mSummaryChoiceBox4;
	@FXML
	private ChoiceBox<Image> mSummaryChoiceBox5;
	@FXML
	private ChoiceBox<Number> mSummaryChoiceBox6, mSummaryChoiceBox7;

	private Label[] allLabels, borderModifiedLabels;
	private LabelWrapper[] allSummaryLabelWrappers;
	private LabelWrapper summaryBuilderLabelWrapper;
	private Button[] boldTextModifiedButtons;
	private Text[] largeSizeText, littleSizeText;
	private TextWrapper settingsTextWrapper2, settingsTextWrapper3, settingsTextWrapper5, settingsTextWrapper6;
	private ChoiceBoxWrapper choiceBoxWrapper1, choiceBoxWrapper2, choiceBoxWrapper3;

	private App app;
	private Configuration config;
	private FirstHarvestingService firstHarvestingService;
	private FirstHarvestingModel firstHarvestingModel;
	private SummaryService summaryService;
	private SummaryModel summaryModel;
	private ManuallyEntryModel manuallyEntryModel;

	public void injection(
			App app
			, Configuration config
			, FirstHarvestingService firstHarvestingService
			, FirstHarvestingModel firstHarvestingModel
			, SummaryService summaryService
			, SummaryModel summaryModel
			, ManuallyEntryModel manuallyEntryModel
	) {
		this.app = app;
		this.config = config;
		this.firstHarvestingService = firstHarvestingService;
		this.firstHarvestingModel = firstHarvestingModel;
		this.summaryService = summaryService;
		this.summaryModel = summaryModel;
		this.manuallyEntryModel = manuallyEntryModel;
	}

	public void startSetup() {
		if (!Dev.isDevMode) {
			utilAnchorPane.getChildren().remove(testButton);
			utilAnchorPane.getChildren().remove(varStateButton);
		}
		groupingAppearanceVariables();
		setBackgroundColor();
		setupTextColor();
		setupFont();
		if (config.isResultLabelFontSizeNotNull()) {
			setResultLabelFont(config.getResultLabelFontSize());
		}
		setNotificationOpacity(0);
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
		summaryBuilderLabelWrapper = new LabelWrapper(summaryBuilderFileDropSpace, getProperty("summary_builder_label_default"));
		summaryBuilderLabelWrapper.resetTextToDefault();
		summaryBuilderFileCreatorDropSpace.setText(getProperty("summary_builder_file_creator_drop_space_text"));

		settingsTextWrapper2 = new TextWrapper(settingsText2);
		settingsTextWrapper3 = new TextWrapper(settingsText3);
		settingsTextWrapper5 = new TextWrapper(settingsText5);
		settingsTextWrapper6 = new TextWrapper(settingsText6);

		setupChoiceBoxWrapper();
		setupMSummaryChoiceBox();
		setupMBackgroundChoiceBox();
	}

	@FXML
	private void setBackgroundColor(MouseEvent mouseEvent) {
		Circle circle = (Circle) mouseEvent.getSource();
		config.setBackgroundColor(getColorHexCode(circle.getFill()));
		setBackgroundColor();
	}

	private void setBackgroundColor() {
		Stages.primary.getScene().getRoot().setStyle("-fx-background-color: " + config.getBackgroundColor() + ";");
	}

	@FXML
	private void setTextColor(MouseEvent mouseEvent) {
		Circle circle = (Circle) mouseEvent.getSource();
		config.setTextColor(getColorHexCode(circle.getFill()));
		setupTextColor();
	}

	public LabelWrapper getSummaryLabelWrapper(int i) {
		return allSummaryLabelWrappers[i - 1];
	}

	private void setupTextColor() {
		for (var text : largeSizeText) {
			text.setFill(Paint.valueOf(config.getTextColor()));
		}
		for (var text : littleSizeText) {
			text.setFill(Paint.valueOf(config.getTextColor()));
		}
		for (var label : allLabels) {
			label.setTextFill(Paint.valueOf(config.getTextColor()));
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
		firstHarvestingService.downloadCalculatedFile();
	}

	@FXML
	private void clearResultLabel() {
		app.getNotificationService().clearNotification();
	}

	@FXML
	private void clearUpperDropSpace() {
		config.setPathToProductFile(null);
		firstHarvestingModel.getReinforcementProductHashMap().clear();
		setUpperDropSpaceText(getProperty("upper_label_default_text"));
	}

	@FXML
	private void clearLowerDropSpace() {
		config.setPathToCalculatingFile(null);
		firstHarvestingModel.getReinforcementHashMap().clear();
		setLowerDropSpaceText(getProperty("lower_label_default_text"));
	}

	@FXML
	private void showInfoStage1() throws IOException {
		Stages.showDifferentInfoStage(1);
	}

	@FXML
	private void showInfoStage2() throws IOException {
		Stages.showDifferentInfoStage(2);
	}

	@FXML
	private void showInfoStage3() throws IOException {
		Stages.showDifferentInfoStage(3);
	}

	@FXML
	private void showInfoStage4() throws IOException {
		Stages.showDifferentInfoStage(4);
	}

	public String getTableHead() {
		return tableHead.getText();
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
			app.getNotificationService().saveNotification();
		} catch (Exception e) {
			app.log(e);
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
	private void summaryBuilderDragDropped(DragEvent dragEvent) throws IOException {
		DropWorker.summaryBuilderDragDropped(dragEvent, this);
		for (var v : allSummaryLabelWrappers) {
			v.setBackground(getBackground("#9F2B00"));
		}
		setupMSummaryContentRowChoiceBox(
				summaryModel.getUserContentRowList()
				, mSummaryChoiceBox1
				, mSummaryChoiceBox8
		);
	}

	@FXML
	private void summaryBuilderFileCreatorDragDropped(DragEvent dragEvent) {
		DropWorker.summaryBuilderFileCreatorDragDropped(dragEvent);
	}

	@FXML
	private void clearSummaryBuilderDropSpace() {
		if (summaryModel.isSummaryBuilderListNotNull()) {
			summaryModel.removeSummaryBuilderList();
			for (var v : allSummaryLabelWrappers) {
				v.setBackground(null);
			}
			defaultSetupMSummaryContentRowChoiceBox();
			app.log(getProperty("summary_builder_remove"));
		}
	}

	public Label getSummaryBuilderFileDropSpaceLabel() {
		return summaryBuilderFileDropSpace;
	}

	@FXML
	private void downloadSummaryFile() {
		summaryService.downloadSummaryFile();
	}

	@FXML
	private void clearAllSummaryDropSpace() {
		for (int i = 1; i < 9; i++) {
			clearSummaryDropSpace(i);
		}
		clearSummaryBuilderDropSpace();
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
		var summaryPaths = summaryModel.getSummaryPaths();
		if (summaryPaths.get(i) != null) {
			summaryPaths.get(i).clear();
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
		var summaryPaths = summaryModel.getSummaryPaths();
		if (summaryPaths.get(i) != null) {
			summaryService.checkSummaryDropSpace(i);
		}
	}

	@FXML
	private void setBorderColor(MouseEvent mouseEvent) {
		Circle circle = (Circle) mouseEvent.getSource();
		config.setBorderColor(getColorHexCode(circle.getFill()));
		setupBorderColor();
	}

	private void setupBorderColor() {
		for (var label : borderModifiedLabels) {
			label.setStyle("-fx-border-color: %s; -fx-border-width: %d;"
					.formatted(config.getBorderColor(), 5));
		}
	}

	@FXML
	private void toggleBoldText() {
		config.toggleBoldText();
		setupFont();
	}

	private void setupFont() {
		if (config.getBoldText()) {
			String font = "System Bold";
			setFont(new Font(font, 14), new Font(font, 16), new Font(font, 20));
		}
		if (!config.getBoldText()) {
			String font = "System";
			setFont(new Font(font, 14), new Font(font, 16), new Font(font, 20));
		}
	}

	private void setFont(Font font14, Font font16, Font font20) {
		for (var label : allLabels) {
			label.setFont(font20);
		}
		notificationLabel.setFont(font14);
		notificationLabel2.setFont(font14);
		for (var button : boldTextModifiedButtons) {
			button.setFont(font16);
		}
		for (var text : largeSizeText) {
			text.setFont(font20);
		}
		for (var text : littleSizeText) {
			text.setFont(font14);
		}
	}

	public void setCheckBox() {
		logCheckBox.setSelected(config.getWriteLog());
		notificationCheckBox.setSelected(config.getWriteNotification());
		autoParseProductListCheckBox.setSelected(config.getAutoParseProductList());
	}

	@FXML
	private void toggleWriteLog() {
		config.toggleWriteLog();
	}

	@FXML
	private void toggleWriteNotification() {
		config.toggleWriteNotification();
	}

	private void setText() {
		try {
			settingsTextWrapper2.setText(settingsTextWrapper2.getDefaultText().formatted(config.getLogStorageLimit()));
			settingsTextWrapper3.setText(
					settingsTextWrapper3.getDefaultText().formatted(
							StorageCleaner.getStorageSize(Root.programRootPath + Root.getProperty("log_storage_directory")),
							new LongWrapper(StorageCleaner.getSize(Root.programRootPath + Root.getProperty("log_storage_directory"))).toString()
					)
			);
			settingsTextWrapper5.setText(settingsTextWrapper5.getDefaultText().formatted(config.getNotificationStorageLimit()));
			settingsTextWrapper6.setText(
					settingsTextWrapper6.getDefaultText().formatted(
							StorageCleaner.getStorageSize(Root.programRootPath + Root.getProperty("notification_storage_directory")),
							new LongWrapper(StorageCleaner.getSize(Root.programRootPath + Root.getProperty("notification_storage_directory"))).toString()
					)
			);
		} catch (Exception e) {
			app.log(e);
		}
	}

	@FXML
	private void deleteLogs() {
		app.getStorageService().deleteStorage(Root.programRootPath + Root.getProperty("log_storage_directory"));
		setText();
	}

	@FXML
	private void deleteNotifications() {
		app.getStorageService().deleteStorage(Root.programRootPath + Root.getProperty("notification_storage_directory"));
		setText();
	}

	@FXML
	private void visitSettingsTab() {
		parseTextField(0, logLimit.getText());
		parseTextField(1, notificationLimit.getText());
		logLimit.clear();
		notificationLimit.clear();
		setText();
	}

	private void parseTextField(int i, String string) {
		if (string.length() > 0) {
			int parsed;
			try {
				parsed = Integer.parseInt(string);
				if (0 <= parsed && parsed <= 5000) {
					if (i == 0) {
						config.setLogStorageLimit(parsed);
					}
					if (i == 1) {
						config.setNotificationStorageLimit(parsed);
					}
				}
			} catch (Exception e) {
				app.log(e);
			}
		}
	}

	@FXML
	private void forgetFavorite() {
		config.setFavoritePath(null);
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
		config.toggleAutoParseProductList();
	}

	@FXML
	private void setResultLabelFont(ActionEvent actionEvent) {
		int fontSize = 20;
		try {
			String text = ((Button) actionEvent.getSource()).getText();
			fontSize = Integer.parseInt(text);
		} catch (NumberFormatException ignored) {
		}
		setResultLabelFont(fontSize);
	}

	private void setResultLabelFont(int i) {
		Font font = new Font(resultLabel.getFont().getName(), i);
		resultLabel.setFont(font);
		config.setResultLabelFontSize(i);
	}

	@FXML
	private void click1(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(1, (Label) mouseEvent.getSource());
		}
	}

	@FXML
	private void click2(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(2, (Label) mouseEvent.getSource());
		}
	}

	@FXML
	private void click3(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(3, (Label) mouseEvent.getSource());
		}
	}

	@FXML
	private void click4(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(4, (Label) mouseEvent.getSource());
		}
	}

	@FXML
	private void click5(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(5, (Label) mouseEvent.getSource());
		}
	}

	@FXML
	private void click7(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(7, (Label) mouseEvent.getSource());
		}
	}

	@FXML
	private void click8(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			SummaryRedirectManager.setRedirectTo(8, (Label) mouseEvent.getSource());
		}
	}

	public Label[] getSummaryLabels() {
		Label[] labels = new Label[allSummaryLabelWrappers.length];
		for (int i = 0; i < labels.length; i++) {
			labels[i] = allSummaryLabelWrappers[i].getLabel();
		}
		return labels;
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
				favoriteDropSpace,
				summaryBuilderFileDropSpace,
				summaryBuilderFileCreatorDropSpace
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
				favoriteDropSpace,
				summaryBuilderFileDropSpace,
				summaryBuilderFileCreatorDropSpace
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
				showInfoButton1,
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
				font10Button,
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
				mSummaryAddButton2,
				clearSummaryBuilderDropSpaceButton,
				startNodeSeekerUtilButton,
				startPythonSteelFrameworkUtilButton,
				showInfoButton2,
				showInfoButton3,
				showInfoButton4
		};
		largeSizeText = new Text[]{
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
		littleSizeText = new Text[]{
				mSummaryHelpingText1,
				mSummaryHelpingText2,
				mSummaryHelpingText3,
				mSummaryHelpingText4,
				mSummaryHelpingText5,
				mSummaryHelpingText6,
				mSummaryHelpingText7,
				mSummaryHelpingText8,
				mSummaryHelpingText9,
				mSummaryHelpingText10,
				mSummaryHelpingText11,
				mSummaryHelpingText12
		};
	}

	private String getColorHexCode(Paint paint) {
		return "#" + paint.toString().substring(2, 8);
	}

	@SafeVarargs
	private void setupMSummaryContentRowChoiceBox(List<String> list, ChoiceBox<String>... choiceBoxes) {
		for (var v : choiceBoxes) {
			v.setItems(observableList(list));
			v.setValue(list.get(0));
		}
	}

	private void defaultSetupMSummaryContentRowChoiceBox() {
		setupMSummaryContentRowChoiceBox(
				Arrays.asList(getProperty("default_content_row").split("-"))
				, mSummaryChoiceBox1
				, mSummaryChoiceBox8
		);
	}

	private void setupMSummaryChoiceBox() {
		defaultSetupMSummaryContentRowChoiceBox();
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
		List<String> list2 = HotRolledSteelType.getAsStrings();
		mSummaryChoiceBox4.setItems(observableList(list2));
		mSummaryChoiceBox4.setValue(list2.get(0));
		mSummaryChoiceBox4.setOnAction(actionEvent -> {
			if (mSummaryChoiceBox4.getValue() == list2.get(0)) {
				setEqual();
			}
			if (mSummaryChoiceBox4.getValue() == list2.get(1)) {
				setUnequal();
			}
			if (mSummaryChoiceBox4.getValue() == list2.get(2)) {
				setSheet();
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
		choiceBoxWrapper3.setList(observableList(SteelComponentRepository.getSheetWidthList()));
	}

	private void setupMBackgroundChoiceBox() {
		mBackgroundChoiceBox.setItems(observableList(StandardsRepository.getPairsAsList()));
		mBackgroundChoiceBox.setValue(StandardsRepository.pairs[1]);
	}

	@FXML
	private void addManuallySummaryEntry() {
		ManuallyEntry.addManuallySummaryEntry(
				mSummaryChoiceBox1.getValue(), // Summary label
				mSummaryChoiceBox2.getValue(), // Diameter
				mSummaryChoiceBox3.getValue(), // RFClass
				mSummaryTextField.getText() // Mass by kg
		);
	}

	@FXML
	private void addManuallySummaryEntry2() {
		if (HotRolledSteelType.parseHotRolledSteelType(mSummaryChoiceBox4.getValue()) != HotRolledSteelType.SHEET) {
			ManuallyEntry.addSteelComponentEntry(
					mSummaryChoiceBox8.getValue(), // Summary label
					mSummaryChoiceBox5.getValue(), // Image
					mSummaryTextField2.getText() // Length by mm
			);
		} else {
			ManuallyEntry.addSteelSheet(
					mSummaryChoiceBox8.getValue(), // Summary label
					mSummaryChoiceBox6.getValue(), // Thickness
					mSummaryChoiceBox7.getValue(), // Width
					mSummaryTextField2.getText() // Length by mm
			);
		}
	}

	@FXML
	private void addBackgroundReinforcement() {
		ManuallyEntry.addBackgroundReinforcement(
				mBackgroundChoiceBox.getValue(), // PairDR
				mBackgroundTextField.getText() // Length by m
		);
	}

	@FXML
	private void restoreWindowSize() {
		Stages.primary.setHeight(Stages.mainStageDefaultHeight);
		Stages.primary.setWidth(Stages.mainStageDefaultWidth);
	}

	@FXML
	private void showReinforcementLinearMassList() {
		Stages.showReinforcementLinearMassList();
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

	public Font getFont() {
		return upperDropSpace.getFont();
	}

	/**
	 * @return user {@link Background} color
	 */
	public Background getUserBackgroundColor() {
		return new Background(new BackgroundFill(
				Color.valueOf(config.getBackgroundColor()),
				CornerRadii.EMPTY,
				Insets.EMPTY
		));
	}

	private Background getBackground(String color) {
		return new Background(new BackgroundFill(
				Paint.valueOf(color),
				CornerRadii.EMPTY,
				Insets.EMPTY
		));
	}

	@FXML
	private void showHotRolledSteelCode() throws IOException {
		HotRolledSteelCodeController.show();
	}

	private String getProperty(String key) {
		return app.getProperty(key);
	}

	@FXML
	private void startNodeSeekerUtil() throws IOException {
		NodeSeekerControllerWrapper.getInstance().show();
	}

	@FXML
	private void startPythonSteelFrameworkUtil() throws IOException {
		PythonSteelFrameworkParserControllerWrapper.getInstance().show();
	}

	@FXML
	private void printVarState() {
		if (Dev.isDevMode) {
			Dev.printVariableStates();
		}
	}

	@FXML
	private void doTest() throws Exception {
		if (Dev.isDevMode) {
			Test.test();
		}
	}

	private void localTest() throws Exception {
	}
}