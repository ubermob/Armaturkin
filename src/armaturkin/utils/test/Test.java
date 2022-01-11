package armaturkin.utils.test;

import armaturkin.utils.PythonProvider;
import utools.stopwatch.Stopwatch;

public class Test {

	public static void test() throws Exception {
		localTest11();
	}

	private static void localTest11() throws Exception {
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.appendBefore("python in ");
		PythonProvider pythonProvider = new PythonProvider();
		String string = pythonProvider.executePythonUtil("T:/p.xlsx");
		System.out.println(string);
		stopwatch.print();
	}
}