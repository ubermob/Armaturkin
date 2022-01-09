package armaturkin.view;

import javafx.scene.text.Text;

public class TextWrapper extends DefaultText {

	private final Text text;

	public TextWrapper(Text text) {
		super(text.getText());
		this.text = text;
	}

	public void setText(String text) {
		this.text.setText(text);
	}
}