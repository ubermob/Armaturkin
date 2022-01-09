package armaturkin.utils;

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
		if (isHavePythonAndOpenpyxlLibrary()) {
			InputStream resourceAsStream = getClass().getResourceAsStream("/python/Steel_framework_parser.py");
			byte[] bytes = resourceAsStream.readAllBytes();
			Path path = Path.of(Root.programRootPath, "tmp_parser.py");
			Files.write(path, bytes);
			String string = executePythonUtil(path.toString(), pathToExcelFile);
			Files.delete(path);
			return string;
		}
		return "";
	}

	public String executePythonUtil(String pathToPythonFile, String pathToExcelFile) throws Exception {
		PythonProvider pythonProvider = new PythonProvider();
		return pythonProvider.execute(
				"python"
				, pathToPythonFile
				, pathToExcelFile
		);
	}

	private boolean isHavePythonAndOpenpyxlLibrary() throws Exception {
		PythonProvider pythonProvider = new PythonProvider();
//		String string = pythonProvider.execute("python");
/*		if (!string.startsWith("Python 3.")) {
			throw new Exception("You do not have installed Python");
		}*/
		String string = pythonProvider.execute("pip", "list");
		if (!string.contains("openpyxl")) {
			throw new Exception("You do not have installed 'openpyxl' library\nType 'pip install openpyxl'");
		}
		return true;
	}
}