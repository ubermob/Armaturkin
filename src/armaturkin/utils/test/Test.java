package armaturkin.utils.test;

import armaturkin.core.DesignCode;
import armaturkin.core.Main;
import armaturkin.core.Reader;
import armaturkin.reinforcement.RFClass;
import armaturkin.steelcomponent.HotRolledSteelType;
import armaturkin.summaryoutput.ContentContainer;
import armaturkin.summaryoutput.ContentHeadPlacement;
import armaturkin.utils.HttpServerUtil;
import armaturkin.utils.StringUtil;
import utools.stopwatch.Stopwatch;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class Test {

	public static void test() throws Exception {
		/*loadBlocks();
		ContentContainer contentContainer = new ContentContainer();
		contentContainer.put(1, 300, 1);
		System.out.println(contentContainer.compactContentToString());*/

		//contentHeadPlacementTest();

		localTest7();
	}

	private static void contentHeadTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		ContentContainer head = ContentContainer.class.getDeclaredConstructor().newInstance();
	}

	private static void contentHeadPlacementTest() {
		ContentHeadPlacement c = new ContentHeadPlacement();
		c.getHashes().forEach(System.out::println);
		System.out.println("Size: " + c.getHashes().size());
		System.out.println(Arrays.toString(c.getBlocks()));
	}

	private static void testArtifact() throws InterruptedException {
		Stopwatch stopwatch = new Stopwatch();
		Thread.sleep(500);
		stopwatch.print();
	}

	private static void stringUtil() {
		System.out.println(StringUtil.replaceNewLine("ABC\n\n\n"));
		System.out.println(StringUtil.cutEnd("123", 0));
		System.out.println(StringUtil.cutEnd("123", 1));
		System.out.println(StringUtil.cutEnd("123", 2));
		System.out.println(StringUtil.cutEnd("123", 3));
	}

	private static void loadBlocks() {
		System.out.println("Load blocks test");
		ContentHeadPlacement contentHeadPlacement = new ContentHeadPlacement();
		System.out.println(contentHeadPlacement.getHashes());
		System.out.println(Arrays.toString(contentHeadPlacement.getBlocks()));
	}

	private static void saveAndLoad() throws IOException {
		try {
			Object[] sample = new Object[]{
					RFClass.A240,
					RFClass.A400,
					RFClass.A500,
					RFClass.A500S,
					RFClass.A600,
					HotRolledSteelType.EQUAL_LEG_ANGLE,
					HotRolledSteelType.UNEQUAL_LEG_ANGLE,
					HotRolledSteelType.SHEET
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

	private static void localTest7() throws Exception {
		String string = HttpServerUtil.getString(Test.class.getResourceAsStream("/http/index.html"));
		System.out.println(string);
	}

	private static void localTest6() throws Exception {
		Main.addNotification("Notification 1");
		Main.addNotification("Notification 2");
		Main.clearNotification();
		Main.addNotification("Notification 3");
	}

	private static void localTest5() throws Exception {
		String s = "1234,,";
		System.out.println(s);
		System.out.println(StringUtil.cutEndIfLastCharIs(s, ','));
		s = "1234,";
		System.out.println(s);
		System.out.println(StringUtil.cutEndIfLastCharIs(s, ','));
		s = "1234";
		System.out.println(s);
		System.out.println(StringUtil.cutEndIfLastCharIs(s, ','));
	}

	private static void localTest4() throws Exception {
/*		printTestName(4);
		ContentContainer c = new ContentContainer();
		//c.put(1, 300, 1.0);
		System.out.println(c.maxHashCode());
		System.out.println(c.borderToString());*/
	}

	private static void localTest3() throws Exception {
/*		printTestName(3);
		ContentContainer c = new ContentContainer();
		c.compress();
		System.out.println("=========");
		System.out.println(c.maxHashCode());*/
	}

	private static void localTest2() throws IOException {
		printTestName(2);
		List<Integer> possibleHash = Reader.readRfHashCode(Main.class.getResourceAsStream("/RF_hash_code_list.txt"));
		possibleHash.forEach(System.out::println);
	}

	private static void localTest1() {
		printTestName(1);
		System.out.println(DesignCode.getProperty("reinforcing_rolled"));
	}

	private static void printTestName(int number) {
		System.out.println("Local test " + number);
	}
}