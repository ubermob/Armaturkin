package armaturkin.core;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Andrey Korneychuk on 11-Jun-22
 * @version 1.0
 */
public class LogManager {

	private static boolean writeLog = false;

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
									StorageCleaner.getStorageSize(
											Root.programRootPath + Root.getProperty("log_storage_directory")
									) + 1)
					)
			);
			Writer.write(Root.programRootPath + Root.getProperty("log_file_name")
					, Main.app.getLogService().getList());
		}
	}
}