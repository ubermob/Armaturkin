package armaturkin.utils.test;

import armaturkin.core.Main;

public class Test {

	public static void test() throws Exception {
		localTest12();
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
	}
}