package armaturkin.summaryoutput;

import armaturkin.core.Main;
import armaturkin.utils.StringUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey Korneychuk on 05-Jan-22
 * @version 1.0
 */
public class SummaryBuilderParser {

	public static List<SummaryBuilder> parse(String path) throws IOException {
		List<String> file = Files.readAllLines(Path.of(path), StandardCharsets.UTF_8);
		List<SummaryBuilder> summaryBuilderList = new ArrayList<>();
		SummaryBuilder current;
		int firstLine = 0;
		for (int i = 0; i < file.size(); i++) {
			if (file.get(i).endsWith(" {")) {
				firstLine = i;
			}
			if (file.get(i).endsWith("}")) {
				List<String> block = new ArrayList<>();
				for (int j = firstLine; j <= i; j++) {
					block.add(file.get(j));
				}
				current = parseBlock(block);
				summaryBuilderList.add(current);
				Main.app.log(SummaryBuilderParser.class + " parse: " + current.toString());
			}
		}
		return summaryBuilderList;
	}

	private static SummaryBuilder parseBlock(List<String> list) {
		SummaryBuilder summaryBuilder = new SummaryBuilder(StringUtil.cutEnd(list.get(0), 2));
		for (String line : list) {
			if (line.endsWith(" {")) {
				// skip open bracket
				continue;
			} else if (line.contains("type = ")) {
				String type = line.split("type = ")[1];
				summaryBuilder.setType(SummaryThreadPool.getSet(type));
			} else if (line.contains("repeat = ")) {
				String repeat = line.split("repeat = ")[1];
				summaryBuilder.setRepeat(Integer.parseInt(repeat));
			} else if (line.contains("all in ")) {
				String pathToDirectory = line.split("all in ")[1];
				summaryBuilder.addPathToDirectory(pathToDirectory);
			} else if (line.contains(" * ")) {
				String[] splitted = line.split(" \\* ");
				String pathToFile = splitted[0].trim();
				int localRepeat = Integer.parseInt(splitted[1]);
				for (int i = 0; i < localRepeat; i++) {
					summaryBuilder.addPathToFile(pathToFile);
				}
			} else if (line.contains("}")) {
				// skip close bracket
				continue;
			} else {
				// regular path
				summaryBuilder.addPathToFile(line.trim());
			}
		}
		return summaryBuilder;
	}
}