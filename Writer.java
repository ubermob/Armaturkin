import java.io.IOException;
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
}