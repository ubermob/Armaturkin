import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Writer {

    private static final ArrayList<String> list = new ArrayList<>();

    public static void write(String path, String[] inputList) throws IOException {
	    list.clear();
	    for (int i = 0; i < inputList.length; i++) {
		    list.add(i, inputList[i]);
	    }
	    write(path, list);
	    list.clear();
    }

    public static void write(String path, ArrayList<String> inputList) throws IOException {
	    Files.write(Path.of(path), inputList);
    }

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