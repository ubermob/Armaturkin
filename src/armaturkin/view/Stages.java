package armaturkin.view;

import javafx.stage.Stage;

public class Stages {

	public static Stage primary;
	public static Stage infoStage;
	public static Stage reinforcementLinearMassListStage;
	public static double defaultHeight;
	public static double defaultWidth;

	public static void closeAll() {
		if (infoStage != null) {
			infoStage.close();
		}
		if (reinforcementLinearMassListStage != null) {
			reinforcementLinearMassListStage.close();
		}
	}
}