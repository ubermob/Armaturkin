package armaturkin.utils;

import armaturkin.core.Main;
import armaturkin.core.Reader;
import armaturkin.reinforcement.StandardsRepository;

import java.io.IOException;
import java.util.List;

public class ReinforcementLinearMassInfo {

	public static String getText() {
		List<String> read = null;
		try {
			read = Reader.readFromInternalSource("/RLMI_style.txt");
		} catch (IOException e) {
			Main.app.log(e);
		}
		assert read != null && read.size() == 4;
		String string = read.get(0) + "\n";
		String pattern = read.get(1);
		int range = StandardsRepository.DIAMETERS.length;
		for (int i = 0; i < range; i++) {
			string += fill(StandardsRepository.DIAMETERS[i], StandardsRepository.MASS_3_DIGIT[i], pattern) + "\n";
			if (i != range - 1) {
				string += read.get(2) + "\n";
			}
		}
		string = string.replace(".", ",");
		return string + read.get(3);
	}

	private static String fill(int diameter, double mass, String pattern) {
		String first = String.valueOf(diameter);
		if (first.length() == 1) {
			first = " " + first;
		}
		String second = String.valueOf(mass);
		if (second.length() == 4) {
			second = second + "0";
		}
		return pattern.formatted(first, second);
	}
}