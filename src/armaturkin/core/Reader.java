package armaturkin.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Reader {
	public static List<String> read(String path) throws IOException {
		return Files.readAllLines(Path.of(path));
    }

    public static List<String> read(InputStream inputStream) throws IOException {
	    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
	    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	    List<String> result = new ArrayList<>();
	    while (bufferedReader.ready()) {
	    	result.add(bufferedReader.readLine());
	    }
	    bufferedReader.close();
		return result;
    }
}