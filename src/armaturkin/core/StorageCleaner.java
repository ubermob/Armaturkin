package armaturkin.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class StorageCleaner {

	public static void clearStorage(Path path) throws IOException {
		List<Path> storageList;
		int storageLimit = 10;
		String beginningOfName = "";
		String numberedFileName = "";
		if (path.equals(Path.of(Root.programRootPath, Root.getProperty("log_storage_directory")))) {
			storageLimit = Main.app.getConfig().getLogStorageLimit();
			beginningOfName = Root.getProperty("log_file_name").split("\\.txt")[0];
			numberedFileName = Root.getProperty("numbered_log_file_name");
		}
		if (path.equals(Path.of(Root.programRootPath, Root.getProperty("notification_storage_directory")))) {
			storageLimit = Main.app.getConfig().getNotificationStorageLimit();
			beginningOfName = Root.getProperty("notification_file_name").split("\\.txt")[0];
			numberedFileName = Root.getProperty("numbered_notification_file_name");
		}
		storageList = Files.list(path).collect(Collectors.toList());
		if (storageList.size() >= storageLimit) {
			Path[] numbers = new Path[storageList.size()];
			String finalBeginningOfName = beginningOfName;
			storageList.forEach(x -> {
				int fileNumber = parseFileName(x.getFileName().toString(), finalBeginningOfName);
				if (fileNumber > 0) {
					numbers[fileNumber - 1] = x;
				}
			});
			int deleteNumber = numbers.length - (storageLimit - 1);
			for (int i = 0; i < deleteNumber; i++) {
				try {
					Files.delete(numbers[i]);
				} catch (Exception e) {
					Main.app.log(e);
				}
			}
			int fileNumber = 1;
			for (int i = deleteNumber; i < numbers.length; i++) {
				try {
					Files.move(numbers[i], Path.of(path.toString(), numberedFileName.formatted(fileNumber)));
				} catch (Exception e) {
					Main.app.log(e);
				}
				fileNumber++;
			}
		}
	}

	public static void copyFile(Path from, Path to) {
		try {
			if (Files.exists(from)) {
				Files.copy(from, to);
			}
		} catch (Exception e) {
			Main.app.log(e);
		}
	}

	public static int getStorageSize(String path) throws IOException {
		return (int) Files.list(Path.of(path)).count();
	}

	public static long getSize(String path) {
		long size = 0;
		try {
			List<Path> collect = Files.list(Path.of(path)).collect(Collectors.toList());
			for (Path file : collect) {
				size += Files.size(file);
			}
		} catch (IOException e) {
			Main.app.log(e);
		}
		if (size == 0) {
			return 0;
		}
		if (size <= 1024) {
			return 1;
		}
		return size / 1024 + 1;
	}

	private static int parseFileName(String currentFileName, String beginningOfName) {
		try {
			return Integer.parseInt(currentFileName.split(beginningOfName)[1].split("\\.txt")[0]);
		} catch (Exception e) {
			Main.app.log(e);
			return -1;
		}
	}
}