package armaturkin.interfaces;

import armaturkin.core.Main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface FileNameCreator {
	default String createFileName(String fileName) {
		if (fileName.length() == 0) {
			LocalDateTime localDateTime = LocalDateTime.now();
			fileName = localDateTime.format(DateTimeFormatter.ofPattern(Main.properties.getProperty("date_time_pattern"))) + ".xlsx";
		} else {
			fileName += ".xlsx";
		}
		return fileName;
	}
}