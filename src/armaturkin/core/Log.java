package armaturkin.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Log {

	private final List<String> log;

	public Log() {
		log = new ArrayList<>();
	}

	public synchronized void add(String string) {
		log.add(string);
		System.out.println(string);
	}

	public synchronized void addSkipConsole(String string) {
		log.add(string);
	}

	public synchronized void add(Exception exception) {
		// https://stackoverflow.com/questions/10120709/difference-between-printstacktrace-and-tostring
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		exception.printStackTrace(printStream);
		printStream.close();
		log.add(byteArrayOutputStream.toString());
		exception.printStackTrace();
	}

	public synchronized void merge(Log log) {
		this.log.addAll(log.getList());
	}

	public synchronized List<String> getList() {
		return log;
	}
}