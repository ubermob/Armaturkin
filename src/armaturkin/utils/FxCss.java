package armaturkin.utils;

import armaturkin.core.Main;
import armaturkin.core.Root;
import javafx.scene.Scene;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Andrey Korneychuk on 13-Jan-22
 * @version 1.0
 */
public class FxCss {

	public static void setCss(Scene scene) throws IOException {
		String string = fileToString(FxCss.class.getResourceAsStream("/fxml/css_template/List_view_css_template.txt"));
		string = string.formatted(Main.app.getConfig().getBackgroundColor(), Main.app.getConfig().getTextColor());
		Path path = Files.writeString(Path.of(Root.programRootPath, Root.getProperty("fx_css_file_name")), string);
		scene.getStylesheets().add("file:/" + path.toString().replace("\\", "/"));
	}

	private static String fileToString(InputStream inputStream) throws IOException {
		return HttpServerUtil.getString(inputStream);
	}
}