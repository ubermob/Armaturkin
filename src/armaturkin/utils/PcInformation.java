package armaturkin.utils;

import armaturkin.core.Main;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;

public class PcInformation {
	public static String getInformation() {
		try {
			return Main.properties.getProperty("pc_information").formatted(
					System.getProperty("user.name"),
					InetAddress.getLocalHost().getHostName(),
					ManagementFactory.getOperatingSystemMXBean().getVersion(),
					ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors(),
					(((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreeMemorySize() / 1024 / 1024),
					(((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalMemorySize() / 1024 / 1024)
			);
		} catch (Exception e) {
			Main.log.add(e);
			return "no PC information";
		}
	}
}