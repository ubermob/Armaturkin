import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Reader {
	public static List<String> read(String path) throws IOException {
		List<String> list = Files.readAllLines(Path.of(path));
        return list;
    }
}