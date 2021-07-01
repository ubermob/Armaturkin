package armaturkin.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Reader {
	public static List<String> read(String path) throws IOException {
		return Files.readAllLines(Path.of(path));
    }
}