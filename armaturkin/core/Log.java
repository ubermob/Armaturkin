package armaturkin.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Log {

	private final List<String> log;
	private static boolean writeLog = false;

	public Log() {
		log = new ArrayList<>();
	}

	public void add(String string) {
		log.add(string);
		System.out.println(string);
	}

	public void add(Exception exception) {
		// https://stackoverflow.com/questions/10120709/difference-between-printstacktrace-and-tostring
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		exception.printStackTrace(printStream);
		printStream.close();
		log.add(byteArrayOutputStream.toString());
		exception.printStackTrace();
	}

	public void merge(Log log) {
		ArrayList<String> list = log.getList();
		this.log.addAll(list);
	}

	public ArrayList<String> getList() {
		return (ArrayList<String>) log;
	}

	// S T A T I C
	public static void enable() {
		writeLog = true;
	}

	public static void saveLog() throws IOException {
		StorageCleaner.clearStorage(Path.of(Main.programRootPath, Main.logStorageDirectory));
		if (writeLog) {
			StorageCleaner.copyFile(
					Path.of(Main.programRootPath, Main.logFileName),
					Path.of(Main.programRootPath, Main.logStorageDirectory,
							Main.properties.getProperty("numbered_log_file_name").formatted(
									StorageCleaner.getStorageSize(Main.programRootPath + Main.logStorageDirectory) + 1)
					)
			);
			Writer.write(Main.programRootPath + Main.logFileName, Main.log.getList(), 1024 * 1024);
		}
	}
}