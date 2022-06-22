package armaturkin.utils.test;

import armaturkin.core.Log;
import armaturkin.core.Main;
import armaturkin.utils.PythonProvider;
import utools.stopwatch.Stopwatch;

public class Test {

	public static void test() throws Exception {
		localTest13();
	}

	private static void localTest11() throws Exception {
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.appendBefore("python in ");
		PythonProvider pythonProvider = new PythonProvider();
		String string = pythonProvider.executePythonUtil("T:/p.xlsx");
		System.out.println(string);
		stopwatch.print();
	}

	private static void localTest12() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Main.app.setWorkerExceptionMessage("1234");
			}
		});
		thread.start();
	}

	private static void localTest13() {
		Log log = new Log();
		log.add("localTest13");
		Main.app.getLogService().merge(log);
	}
}