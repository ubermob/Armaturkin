package armaturkin.utils;

import armaturkin.core.Main;
import armaturkin.core.Root;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Andrey Korneychuk on 09-Jan-22
 * @version 1.0
 */
public class PythonProvider {

	public String execute(String... strings) throws InterruptedException, IOException {
		ProcessBuilder processBuilder = new ProcessBuilder(strings);
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();
		String result = new String(process.getInputStream().readAllBytes());
		int exitCode = process.waitFor();
		result += "\nProcess finished with exit code " + exitCode;
		return result;
	}

	public String executePythonUtil(String pathToExcelFile) throws Exception {
		boolean isRunnable = checkPythonAndLibraries();
		if (isRunnable) {
			InputStream resourceAsStream = getClass().getResourceAsStream("/python/Steel_framework_parser.py");
			byte[] bytes = resourceAsStream.readAllBytes();
			Path path = Path.of(Root.programRootPath, Root.getProperty("python_file_name"));
			Files.write(path, bytes);
			String string = executePythonUtil(path.toString(), pathToExcelFile);
			Files.delete(path);
			return string;
		}
		return null;
	}

	public String executePythonUtil(String pathToPythonFile, String pathToExcelFile) throws Exception {
		return execute(
				"python"
				, pathToPythonFile
				, pathToExcelFile
				, "true"
		);
	}

	public boolean checkPythonAndLibraries() throws Exception {
		boolean pythonInterpreter = Main.app.getConfig().getPythonInterpreter();
		if (pythonInterpreter) {
			return true;
		} else {
			String string = "";
			string = execute("python", "--version");
			if (!string.contains("Python 3.")) {
				throw new Exception("You do not have Python 3");
			}
			string = execute("pip", "list");
			if (!string.contains("openpyxl")) {
				throw new Exception("You do not have installed 'openpyxl' library\nType 'pip install openpyxl'");
			}
			if (!string.contains("transliterate")) {
				throw new Exception("You do not have installed 'transliterate' library\nType 'pip install transliterate'");
			}
			Main.app.getConfig().setPythonInterpreter(true);
		}
		return true;
	}
}