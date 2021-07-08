package armaturkin.core;

public class Spammer implements Runnable {
	@Override
	public void run() {
		try {
			Thread.sleep(500);
			for (int i = 0; i < 30; i++) {
				String string = "";
				for (int j = 0; j < 15; j++) {
					string += "Hello ";
				}
				Main.addNotification(string);
			}
		} catch (InterruptedException e) {
			Main.log.add(e);
		}
	}
}