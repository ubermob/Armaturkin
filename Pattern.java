public class Pattern {

	static int[] diameter = {6, 8, 10, 12, 14, 16, 18, 20, 22, 25, 28, 32, 36, 40};
	static String[] rfClass240 = {
			"а240", // Russian letter
			"а 240", // Russian letter
			"a240", // English letter
			"a 240" // English letter
	};
	static String[] rfClass400 = {
			"а400", // Russian letter
			"а 400", // Russian letter
			"a400", // English letter
			"a 400" // English letter
	};
	static String[] rfClass500 = {
			"а500", // Russian letter
			"а 500", // Russian letter
			"a500", // English letter
			"a 500" // English letter
	};
	static String[] rfClass500S = {
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
	static String[] rfClass600 = {
			"а600", // Russian letter
			"а 600", // Russian letter
			"a600", // English letter
			"a 600" // English letter
	};
	static int maxProductionLength = 11700;
	static double[] mass3Digit = {0.222, 0.395, 0.617, 0.888, 1.208, 1.578, 1.998, 2.466, 2.984, 3.853 ,4.834, 6.313, 7.990, 9.865};
	static double[] mass2Digit1 = {0.22, 0.40, 0.62, 0.89, 1.21, 1.58, 2.00, 2.47, 2.98, 3.85 ,4.83, 6.31, 7.99, 9.87}; // implements math rules
	static double[] mass2Digit2 = {0.23, 0.39, 0.61, 0.88, 1.20, 1.57, 1.99, 2.46, 2.99, 3.86 ,4.84, 6.32, 8.00, 9.86}; // alternative version
}