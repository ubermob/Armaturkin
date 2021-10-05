package armaturkin.view;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

/**
 * @author Andrey Korneychuk on 28-Sep-21
 * @version 1.0
 */
public class ChoiceBoxWrapper {

	private ChoiceBox choiceBox;
	private final ObservableList defaultList;

	public ChoiceBoxWrapper(ChoiceBox choiceBox) {
		this.choiceBox = choiceBox;
		defaultList = choiceBox.getItems();
	}

	public void setList(ObservableList observableList) {
		choiceBox.setItems(observableList);
	}

	public void reset() {
		choiceBox.setItems(defaultList);
	}
}