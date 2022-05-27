package armaturkin.interfaces;

import armaturkin.core.Main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface FileNameCreator {

	default String createFileName(String fileName, String extension) {
		if (fileName.length() == 0) {
			LocalDateTime localDateTime = LocalDateTime.now();
			fileName = localDateTime.format(DateTimeFormatter.ofPattern(Main.app.getProperty("date_time_pattern"))) + extension;
		} else {
			fileName += extension;
		}
		return fileName;
	}
}