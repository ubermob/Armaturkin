import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Log {

	private static ArrayList<String> log;
	private static boolean writeLog = false;
	private static int logStorageLimit = 10;
	private static int currentLogStorage;

	public static void enable() {
		writeLog = true;
		log = new ArrayList<>();
	}

	public static void setLogStorageLimit(int limit) {
		logStorageLimit = limit;
	}

	public static void add(String string) {
		if (writeLog) {
			log.add(string);
		}
		System.out.println(string);
	}

	public static void add(Exception exception) {
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

	public static void saveLog() throws IOException {
		clearStorage(Path.of(Main.programRootPath, Main.logStorageDirectory));
		try {
			Files.copy(Path.of(Main.programRootPath, Main.logFileName), Path.of(Main.programRootPath,
					Main.logStorageDirectory, "log" + (currentLogStorage + 1) + ".txt"));
		} catch (Exception e) {
			Log.add(e);
		}
		if (writeLog) {
			Writer.write(Main.programRootPath + Main.logFileName, log, 1024 * 1024);
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
					Log.add(e);
				}
			}
			int fileNumber = 1;
			for (int i = deleteNumber; i < pathArray.length; i++) {
				try {
					Files.move(pathArray[i], Path.of(Main.programRootPath, Main.logStorageDirectory, "log" + fileNumber + ".txt"));
				} catch (Exception e) {
					Log.add(e);
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
			Log.add(e);
			return -1;
		}
	}
}