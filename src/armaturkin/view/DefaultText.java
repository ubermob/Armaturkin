package armaturkin.view;

public abstract class DefaultText {

	protected String defaultText;

	public DefaultText() {
	}

	public DefaultText(String string) {
		defaultText = string;
	}

	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	public String getDefaultText() {
		return defaultText;
	}
}