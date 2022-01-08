package armaturkin.service;

import armaturkin.core.Log;

import java.io.IOException;
import java.util.List;

/**
 * @author Andrey Korneychuk on 06-Jan-22
 * @version 1.0
 */
public interface NotificationService {

	String getNotification();

	void addNotification(String notification);

	Log getStoredNotifications();

	List<String> getAllNotifications();

	void clearNotification();

	void saveNotification() throws IOException;

	void saveAllNotifications() throws IOException;

	void saveNotificationToFile(String string) throws IOException;
}