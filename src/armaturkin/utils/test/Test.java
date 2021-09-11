package armaturkin.utils.test;

import armaturkin.summaryoutput.ContentContainer;
import armaturkin.core.Main;
import armaturkin.core.Reader;
import armaturkin.core.Specification;
import armaturkin.reinforcement.RFClass;
import armaturkin.steelcomponent.HotRolledSteel;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class Test {

	public static void test() throws Exception {

	}

	private static void saveAndLoad() throws IOException {
		try {
			Object[] sample = new Object[]{
					RFClass.A240,
					RFClass.A400,
					RFClass.A500,
					RFClass.A500S,
					RFClass.A600,
					HotRolledSteel.EQUAL_LEG_ANGLE,
					HotRolledSteel.UNEQUAL_LEG_ANGLE,
					HotRolledSteel.SHEET
			};

			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("J:\\abcdef"));
			oos.writeObject(sample);
			oos.close();

			Thread.sleep(250);

			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("J:\\abcdef"));
			Object o = ois.readObject();
			System.out.println(o);
			Object[] readSample = (Object[]) o;
			System.out.println(Arrays.toString(readSample));
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void localTest4() {
		printTestName(4);
		ContentContainer c = new ContentContainer();
		//c.put(1, 300, 1.0);
		System.out.println(c.maxHashCode());
		System.out.println(c.borderToString());
	}

	private static void localTest3() {
		printTestName(3);
		ContentContainer c = new ContentContainer();
		c.compress();
		System.out.println("=========");
		System.out.println(c.maxHashCode());
	}

	private static void localTest2() throws IOException {
		printTestName(2);
		List<Integer> possibleHash = Reader.readRfHashCode(Main.class.getResourceAsStream("/RF_hash_code_list.txt"));
		possibleHash.forEach(System.out::println);
	}

	private static void localTest1() {
		printTestName(1);
		System.out.println(Specification.getProperty("reinforcing_rolled"));
	}

	private static void printTestName(int number) {
		System.out.println("Local test " + number);
	}
}