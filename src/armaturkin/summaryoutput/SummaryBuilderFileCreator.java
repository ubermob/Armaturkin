package armaturkin.summaryoutput;

import armaturkin.core.Main;
import utools.stopwatch.Stopwatch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Andrey Korneychuk on 27-May-22
 * @version 1.0
 */
public class SummaryBuilderFileCreator {

	public void create(Path input, String relative, String blockName, Path saving) {
		try {
			Stopwatch stopwatch = new Stopwatch();
			final List<String> list = new LinkedList<>();
			final boolean isRelative = relative.equals("rel");
			Stream<Path> pathStream = Files.list(input);
			list.add(blockName + " {");
			pathStream.forEach(path1 -> {
				if (isRelative) {
					Path path2 = path1.getParent().getParent();
					path1 = path2.relativize(path1);
				}
				String replace = path1.toString().replace("\\", "/");
				list.add("    " + replace);
			});
			list.add("    type = pretty");
			list.add("}");
			Files.write(saving, list, StandardCharsets.UTF_8);
			Main.app.addNotification(Main.app.getProperty("summary_builder_file_creator_notification").formatted(
					input.toString()
			));
			Main.app.log(Main.app.getProperty("summary_builder_file_creator_log").formatted(
					input.toString()
					, relative
					, saving.toString()
					, stopwatch.getElapsedTime()
			));
		} catch (IOException exception) {
			Main.app.log(this.getClass().toString());
			Main.app.log(exception);
		}
	}
}