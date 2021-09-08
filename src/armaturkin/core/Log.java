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

	public synchronized void add(String string) {
		log.add(string);
		System.out.println(string);
	}

	public synchronized void add(Exception exception) {
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
		StorageCleaner.clearStorage(Path.of(Root.programRootPath, Root.getProperty("log_storage_directory")));
		if (writeLog) {
			StorageCleaner.copyFile(
					Path.of(Root.programRootPath, Root.getProperty("log_file_name")),
					Path.of(Root.programRootPath, Root.getProperty("log_storage_directory"),
							Root.getProperty("numbered_log_file_name").formatted(
									StorageCleaner.getStorageSize(Root.programRootPath + Root.getProperty("log_storage_directory")) + 1)
					)
			);
			Writer.write(Root.programRootPath + Root.getProperty("log_file_name"), Main.log.getList());
		}
	}

	public static synchronized void log(String string) {
		Main.log.add(string);
	}

	public static synchronized void log(Exception exception) {
		Main.log.add(exception);
	}
}