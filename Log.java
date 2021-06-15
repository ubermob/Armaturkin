import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Log {

	private final List<String> log;

	private static boolean writeLog = false;
	private static int logStorageLimit = 10;
	private static int currentLogStorage;

	public Log() {
		log = new ArrayList<>();
	}

	public void add(String string) {
		if (writeLog) {
			log.add(string);
		}
		System.out.println(string);
	}

	public void add(Exception exception) {
		// https://stackoverflow.com/questions/10120709/difference-between-printstacktrace-and-tostring
		if (writeLog) {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			PrintStream printStream = new PrintStream(byteArrayOutputStream);
			exception.printStackTrace(printStream);
			printStream.close();
			log.add(byteArrayOutputStream.toString());
		}
		exception.printStackTrace();
	}

	public void merge(Log log) {
		if (writeLog) {
			ArrayList<String> list = log.getList();
			for (String string : list) {
				this.log.add(string);
			}
		}
	}

	public ArrayList<String> getList() {
		return (ArrayList<String>) log;
	}
	// S T A T I C
	public static void enable() {
		writeLog = true;
	}

	public static void setLogStorageLimit(int limit) {
		logStorageLimit = limit;
	}

	public static void saveLog() throws IOException {
		clearStorage(Path.of(Main.programRootPath, Main.logStorageDirectory));
		try {
			Files.copy(Path.of(Main.programRootPath, Main.logFileName),
					Path.of(Main.programRootPath, Main.logStorageDirectory,
							Main.properties.getProperty("numberedLogFileName").formatted(currentLogStorage + 1))
			);
		} catch (Exception e) {
			Main.log.add(e);
		}
		if (writeLog) {
			Writer.write(Main.programRootPath + Main.logFileName, Main.log.getList(), 1024 * 1024);
		}
	}

	private static void clearStorage(Path path) throws IOException {
		List<Path> storageList = Files.list(path).collect(Collectors.toList());
		Path[] pathArray = new Path[storageList.size()];
		storageList.forEach(x -> {
			int fileNumber = parseFileName(x.getFileName().toString());
			if (fileNumber > 0) {
				pathArray[fileNumber - 1] = x;
			}
		});
		currentLogStorage = storageList.size();
		if (pathArray.length >= logStorageLimit) {
			int deleteNumber = pathArray.length - (logStorageLimit - 1);
			for (int i = 0; i < deleteNumber; i++) {
				try {
					Files.delete(pathArray[i]);
				} catch (Exception e) {
					Main.log.add(e);
				}
			}
			int fileNumber = 1;
			for (int i = deleteNumber; i < pathArray.length; i++) {
				try {
					Files.move(pathArray[i],
							Path.of(Main.programRootPath, Main.logStorageDirectory,
									Main.properties.getProperty("numberedLogFileName").formatted(fileNumber))
					);
				} catch (Exception e) {
					Main.log.add(e);
				}
				fileNumber++;
			}
			currentLogStorage = logStorageLimit - 1;
		}
	}

	private static int parseFileName(String fileName) {
		try {
			return Integer.parseInt(fileName.split("log")[1].split("\\.txt")[0]);
		} catch (Exception e) {
			Main.log.add(e);
			return -1;
		}
	}
}