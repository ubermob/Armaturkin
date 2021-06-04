import java.io.IOException;
import java.util.ArrayList;

public class Log {

	private static ArrayList<String> log;
	private static boolean writeLog = false;

	public static void enable() {
		writeLog = true;
		log = new ArrayList<>();
	}

	public static void add(String string) {
		if (writeLog) {
			log.add(string);
		}
		System.out.println(string);
	}

	public static void saveLog() throws IOException {
		if (writeLog) {
			Writer.write(Main.programRootPath + Main.logFileName, log);
		}
	}
}