package armaturkin.reinforcement;

import java.util.ArrayList;
import java.util.Arrays;

// Class contains organization standards for publish documentation
public class StandardsRepository {

	public static final int[] DIAMETERS = {6, 8, 10, 12, 14, 16, 18, 20, 22, 25, 28, 32, 36, 40}; // Size == 14
	static final String[] rfClass240 = {
			"а240", // Russian letter
			"а 240", // Russian letter
			"a240", // English letter
			"a 240" // English letter
	};
	static final String[] rfClass400 = {
			"а400", // Russian letter
			"а 400", // Russian letter
			"a400", // English letter
			"a 400" // English letter
	};
	static final String[] rfClass500 = {
			"а500", // Russian letter
			"а 500", // Russian letter
			"a500", // English letter
			"a 500" // English letter
	};
	static final String[] rfClass500S = {
			"а500с", // Russian letter
			"а 500с", // Russian letter
			"а500 с", // Russian letter
			"а 500 с", // Russian letter

			"a500c", // English letter
			"a 500c", // English letter
			"a500 c", // English letter
			"a 500 c", // English letter

			"a500с", // English + Russian letter
			"a 500с", // English + Russian letter
			"a500 с", // English + Russian letter
			"a 500 с", // English + Russian letter

			"а500c", // Russian + English letter
			"а 500c", // Russian + English letter
			"а500 c", // Russian + English letter
			"а 500 c" // Russian + English letter
	};
	static final String[] rfClass600 = {
			"а600", // Russian letter
			"а 600", // Russian letter
			"a600", // English letter
			"a 600" // English letter
	};

	public static final short MAX_LENGTH = 11_700;

	public static final double[] MASS_3_DIGIT = {0.222, 0.395, 0.617, 0.888, 1.208, 1.578, 1.998, 2.466, 2.984, 3.853, 4.834, 6.313, 7.990, 9.865};
	public static final double[] MASS_2_DIGIT_1 = {0.22, 0.40, 0.62, 0.89, 1.21, 1.58, 2.00, 2.47, 2.98, 3.85, 4.83, 6.31, 7.99, 9.87}; // implements math rules
	public static final double[] MASS_2_DIGIT_2 = {0.23, 0.39, 0.61, 0.88, 1.20, 1.57, 1.99, 2.46, 2.99, 3.86, 4.84, 6.32, 8.00, 9.86}; // alternative version

	public static final int[] RESERVED_POSITIONS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18}; // Size == 18
	public static final int[] RESERVED_DIAMETERS = {8, 10, 12, 14, 16, 18, 20, 22, 25, 28, 32, 36, 40, 6, 8, 10, 12, 14}; // Size == 18
	public static final PairDR[] PAIRS;

	public static final byte[][] DIAMETERS_COLORS_RGB = {
			{(byte) 255, (byte) 0, (byte) 255},     // d6
			{(byte) 127, (byte) 127, (byte) 0},     // d8
			{(byte) 127, (byte) 102, (byte) 204},   // d10
			{(byte) 255, (byte) 127, (byte) 0},     // d12
			{(byte) 223, (byte) 127, (byte) 255},   // d14
			{(byte) 0, (byte) 191, (byte) 255},     // d16
			{(byte) 204, (byte) 0, (byte) 204},     // d18
			{(byte) 38, (byte) 153, (byte) 0},      // d20
			{(byte) 255, (byte) 0, (byte) 0},       // d22
			{(byte) 0, (byte) 0, (byte) 0},         // d25
			{(byte) 127, (byte) 0, (byte) 0},       // d28
			{(byte) 102, (byte) 153, (byte) 204},   // d32
			{(byte) 153, (byte) 153, (byte) 0},     // d36
			{(byte) 0, (byte) 127, (byte) 63},      // d40
	};

	static {
		PAIRS = new PairDR[RESERVED_DIAMETERS.length];
		for (int i = 0; i < RESERVED_DIAMETERS.length; i++) {
			PAIRS[i] = new PairDR(RESERVED_DIAMETERS[i], getReservedRFClass(RESERVED_POSITIONS[i]));
		}
	}

	public static int getReservedPositionIndex(int position) {
		for (int i = 0; i < RESERVED_POSITIONS.length; i++) {
			if (position == RESERVED_POSITIONS[i]) {
				return i;
			}
		}
		return -1; // Do not contains
	}

	public static int getReservedDiameterIndex(int diameter) {
		for (int i = 0; i < RESERVED_DIAMETERS.length; i++) {
			if (diameter == RESERVED_DIAMETERS[i]) {
				return i;
			}
		}
		return -1; // Do not contains
	}

	public static RFClass getReservedRFClass(int reservedPosition) {
		if (reservedPosition <= 13) {
			return RFClass.A500S;
		}
		return RFClass.A240;
	}

	public static double getMass(int diameter) {
		for (int i = 0; i < DIAMETERS.length; i++) {
			if (diameter == DIAMETERS[i]) {
				return MASS_3_DIGIT[i];
			}
		}
		return 0.0;
	}

	public static boolean contains(int[] pattern, int value) {
		for (int i : pattern) {
			if (i == value) {
				return true;
			}
		}
		return false;
	}

	public static byte[] getRgb(int diameter) {
		for (int i = 0; i < DIAMETERS.length; i++) {
			if (diameter == DIAMETERS[i]) {
				return DIAMETERS_COLORS_RGB[i];
			}
		}
		return DIAMETERS_COLORS_RGB[9]; // Black color
	}

	public static ArrayList<Integer> getDiametersAsList() {
		ArrayList<Integer> list = new ArrayList<>();
		for (int i : DIAMETERS) {
			list.add(i);
		}
		return list;
	}

	public static ArrayList<PairDR> getPairsAsList() {
		return new ArrayList<>(Arrays.asList(PAIRS));
	}

	/**
	 * @author Andrey Korneychuk on 10-Jun-22
	 * @version 1.0
	 */
	public static class NotifiableReinforcementList {

		public static short[] list;

		static {
			list = new short[]{
					(short) RfHashCode.getHashCode(8, RFClass.A400)
					, (short) RfHashCode.getHashCode(8, RFClass.A500)
					, (short) RfHashCode.getHashCode(8, RFClass.A500S)
					, (short) RfHashCode.getHashCode(8, RFClass.A600)
					, (short) RfHashCode.getHashCode(10, RFClass.A400)
					, (short) RfHashCode.getHashCode(10, RFClass.A500)
					, (short) RfHashCode.getHashCode(10, RFClass.A500S)
					, (short) RfHashCode.getHashCode(10, RFClass.A600)
					, (short) RfHashCode.getHashCode(12, RFClass.A240)
					, (short) RfHashCode.getHashCode(14, RFClass.A240)
					, (short) RfHashCode.getHashCode(16, RFClass.A240)
					, (short) RfHashCode.getHashCode(18, RFClass.A240)
					, (short) RfHashCode.getHashCode(20, RFClass.A240)
					, (short) RfHashCode.getHashCode(22, RFClass.A240)
					, (short) RfHashCode.getHashCode(25, RFClass.A240)
					, (short) RfHashCode.getHashCode(28, RFClass.A240)
					, (short) RfHashCode.getHashCode(32, RFClass.A240)
					// Any 36 and 40
					, (short) RfHashCode.getHashCode(36, RFClass.A240)
					, (short) RfHashCode.getHashCode(36, RFClass.A400)
					, (short) RfHashCode.getHashCode(36, RFClass.A500)
					, (short) RfHashCode.getHashCode(36, RFClass.A500S)
					, (short) RfHashCode.getHashCode(36, RFClass.A600)
					, (short) RfHashCode.getHashCode(40, RFClass.A240)
					, (short) RfHashCode.getHashCode(40, RFClass.A400)
					, (short) RfHashCode.getHashCode(40, RFClass.A500)
					, (short) RfHashCode.getHashCode(40, RFClass.A500S)
					, (short) RfHashCode.getHashCode(40, RFClass.A600)
			};
		}

		public static boolean isContain(int rfHashCode) {
			for (short s : list) {
				if (s == rfHashCode) {
					return true;
				}
			}
			return false;
		}
	}
}