public enum RFClass {
	A240,
	A400,
	A500,
	A500S,
	A600,
	MISS_VALUE;

	static String toString(RFClass rfClass) {
		if (rfClass == A240) {
			return "А240"; // Russian letter
		}
		if (rfClass == A400) {
			return "А400"; // Russian letter
		}
		if (rfClass == A500) {
			return "А500"; // Russian letter
		}
		if (rfClass == A500S) {
			return "А500С"; // Russian letter
		}
		if (rfClass == A600) {
			return "А600"; // Russian letter
		}
		return "???";
	}
}