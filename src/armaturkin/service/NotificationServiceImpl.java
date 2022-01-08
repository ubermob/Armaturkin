package armaturkin.service;

import armaturkin.controller.Controller;
import armaturkin.core.*;
import armaturkin.utils.StringUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Andrey Korneychuk on 06-Jan-22
 * @version 1.0
 */
public class NotificationServiceImpl implements NotificationService {

	private Controller controller;
	private volatile String notificationString;
	private final Log notificationLog;

	public NotificationServiceImpl() {
		notificationString = "";
		notificationLog = new Log();
	}

	@Override
	public String getNotification() {
		return notificationString;
	}

	@Override
	public synchronized void addNotification(String notification) {
		notificationString += notification + "\n";
		controller.setNotificationOpacity(1);
	}

	@Override
	public Log getStoredNotifications() {
		return notificationLog;
	}

	@Override
	public List<String> getAllNotifications() {
		List<String> list = new ArrayList<>();
		for (var multiString : getStoredNotifications().getList()) {
			String[] split = multiString.split("\n");
			list.addAll(Arrays.asList(split));
		}
		list.addAll(Arrays.asList(getNotification().split("\n")));
		return list;
	}

	@Override
	public synchronized void clearNotification() {
		notificationLog.addSkipConsole(notificationString);
		notificationString = "";
	}

	@Override
	public void saveNotification() throws IOException {
		saveNotificationToFile(StringUtil.replaceNewLine(notificationString));
	}

	@Override
	public void saveAllNotifications() throws IOException {
		StringBuilder builder = new StringBuilder();
		for (var v : notificationLog.getList()) {
			builder.append(v);
		}
		builder.append(StringUtil.replaceNewLine(notificationString));
		saveNotificationToFile(builder.toString());
	}

	@Override
	public void saveNotificationToFile(String string) throws IOException {
		StorageCleaner.clearStorage(Path.of(Root.programRootPath, Root.getProperty("notification_storage_directory")));
		if (Main.app.getConfig().getWriteNotification() && string.length() > 0) {
			StorageCleaner.copyFile(
					Path.of(Root.programRootPath, Root.getProperty("notification_file_name")),
					Path.of(Root.programRootPath,
							Root.getProperty("notification_storage_directory"),
							Root.getProperty("numbered_notification_file_name").formatted(
									StorageCleaner.getStorageSize(Root.programRootPath +
											Root.getProperty("notification_storage_directory")) + 1)
					)
			);
			Writer.write(Root.programRootPath + Root.getProperty("notification_file_name"), string);
		}
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
}