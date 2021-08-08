package armaturkin.view;

import armaturkin.core.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.DepthTest;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.net.URL;

public class AddonViews {

	public static Label infoLabel;
	public static Line redirectLine;
	public static Line arrowLine1;
	public static Line arrowLine2;
	public static Arrow arrow;

	public static void loadInfoLabel(URL url) throws IOException {
		infoLabel = new FXMLLoader(url).load();
		Main.controller.addInfoLabel(infoLabel);
		AddonViews.setupInfoLabel();
	}

	public static void loadArrowLines(URL url) throws IOException {
		var ap = (AnchorPane) new FXMLLoader(url).load();
		redirectLine = (Line) ap.getChildren().get(0);
		arrowLine1 = (Line) ap.getChildren().get(1);
		arrowLine2 = (Line) ap.getChildren().get(2);
		Main.controller.addArrowLines();
	}

	private static void setupInfoLabel() {
		infoLabel.setTranslateX(50);
		infoLabel.setTranslateY(60);
	}
}