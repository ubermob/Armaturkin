package armaturkin.core;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Andrey Korneychuk on 10-Nov-22
 * @version 1.0
 */
public class ArgConfiguration {

	static boolean checkForUpdate;
	static String checkForUpdateHost;
	static volatile String checkResultForLog;
	static volatile String checkResultForNotification;

	static {
		checkForUpdate = true;
		InputStream stream = ArgConfiguration.class.getResourceAsStream("/Check_for_update_host.txt");
		try {
			checkForUpdateHost = new String(stream.readAllBytes());
		} catch (IOException exception) {
			checkForUpdateHost = "http://194.87.253.145/versions/armaturkin";
		}
	}
}