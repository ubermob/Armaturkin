package armaturkin.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Writer {

	private static ArrayList<String> list = new ArrayList<>();

	public static void write(String path, String[] inputArray) throws IOException {
		list = new ArrayList<>(Arrays.asList(inputArray));
		write(path, list);
	}

	public static void write(String path, String input) throws IOException {
		list = new ArrayList<>(1);
		list.add(input);
		write(path, list);
	}

	public static void write(String path, List<String> inputList) throws IOException {
		Files.write(Path.of(path), inputList);
	}
}