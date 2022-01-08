package armaturkin.utils;

import armaturkin.core.Main;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;

public class PcInformation {
	public static String getInformation() {
		try {
			return Main.app.getProperty("pc_information").formatted(
					System.getProperty("user.name"),
					InetAddress.getLocalHost().getHostName(),
					System.getProperty("os.name"),
					System.getProperty("os.version"),
					System.getProperty("os.arch"),
					System.getProperty("java.vendor"),
					System.getProperty("java.version"),
					ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors(),
					(((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreeMemorySize() / 1024 / 1024),
					(((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalMemorySize() / 1024 / 1024),
					Runtime.getRuntime().maxMemory() / 1024 / 1024
			);
		} catch (Exception e) {
			Main.app.log(e);
			return "no PC information";
		}
	}
}