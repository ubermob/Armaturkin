package armaturkin.core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

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

	public static void write(String path, ArrayList<String> inputList) throws IOException {
		Files.write(Path.of(path), inputList);
	}

	@Deprecated
	public static void write(String path, ArrayList<String> inputList, int bufferSize) throws IOException {
		FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter, bufferSize);
		for (String s : inputList) {
			bufferedWriter.write(s);
			bufferedWriter.newLine();
		}
		bufferedWriter.close();
	}
}