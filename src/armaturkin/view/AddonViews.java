package armaturkin.view;

import armaturkin.core.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.net.URL;

public class AddonViews {

	public static Line redirectLine;
	public static Line arrowLine1;
	public static Line arrowLine2;
	public static Arrow arrow;

	public static void loadArrowLines() throws IOException {
		URL url = AddonViews.class.getResource("/fxml/Arrow_lines.fxml");
		var ap = (AnchorPane) new FXMLLoader(url).load();
		redirectLine = (Line) ap.getChildren().get(0);
		arrowLine1 = (Line) ap.getChildren().get(1);
		arrowLine2 = (Line) ap.getChildren().get(2);
		Main.app.getController().addArrowLines();
	}
}