public enum RFClass {
	A240,
	A400,
	A500,
	A500S,
	A600,
	UNKNOWN;

	static String toString(RFClass rfClass) {
		return switch (rfClass) {
			case A240 -> "А240"; // Russian letter
			case A400 -> "А400"; // Russian letter
			case A500 -> "А500"; // Russian letter
			case A500S -> "А500С"; // Russian letter
			case A600 -> "А600"; // Russian letter
			default -> "???";
		};
	}

	static synchronized int getIntegerValue(RFClass rfClass) {
		return switch (rfClass) {
			case A240 -> 50;
			case A400 -> 100;
			case A500 -> 150;
			case A500S -> 200;
			case A600 -> 250;
			default -> 0;
		};
	}

	static synchronized RFClass parseRFClass(String string) {
		//Sorted by often use
		for (int i = 0; i < StandardsRepository.rfClass500S.length; i++) {
			if (string.equalsIgnoreCase(StandardsRepository.rfClass500S[i])) {
				return A500S;
			}
		}
		for (int i = 0; i < StandardsRepository.rfClass240.length; i++) {
			if (string.equalsIgnoreCase(StandardsRepository.rfClass240[i])) {
				return A240;
			}
		}
		for (int i = 0; i < StandardsRepository.rfClass500.length; i++) {
			if (string.equalsIgnoreCase(StandardsRepository.rfClass500[i])) {
				return A500;
			}
		}
		for (int i = 0; i < StandardsRepository.rfClass400.length; i++) {
			if (string.equalsIgnoreCase(StandardsRepository.rfClass400[i])) {
				return A400;
			}
		}
		for (int i = 0; i < StandardsRepository.rfClass600.length; i++) {
			if (string.equalsIgnoreCase(StandardsRepository.rfClass600[i])) {
				return A600;
			}
		}
		return UNKNOWN;
	}
}