package armaturkin.utils.test;

import armaturkin.summaryoutput.SummaryBuilderParser;

import java.io.IOException;

public class Test {

	public static void test() throws Exception {
		localTest8();
	}

	private static void localTest8() throws IOException {
		SummaryBuilderParser.parse("C:\\Users\\uber\\IdeaProjects\\Armaturkin\\stuff\\Big summary builder.txt");
	}
}