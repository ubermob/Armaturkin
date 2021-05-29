import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.*;

public class ProductFileWorker implements Runnable, CellEmptyChecker, ParseInt {

	private final String path;
	private final ArrayList<ReinforcementProduct> reinforcementProductArrayList;
	private Workbook workbook;
	private Sheet sheet;
	private Row row;
	private int rowInt;
	private final ArrayList<Integer> positionList = new ArrayList<>();
	private int diameterArrayIndex;
	private boolean diameterMatch;
	private int length;

	public ProductFileWorker(String path, ArrayList<ReinforcementProduct> reinforcementProductArrayList) {
		this.path = path;
		this.reinforcementProductArrayList = reinforcementProductArrayList;
	}

	@Override
	public void run() {
		try {
			workbook = WorkbookFactory.create(Files.newInputStream(Path.of(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = workbook.getSheetAt(0);
		rowInt = 2;
		while (!isCellEmpty(sheet.getRow(rowInt).getCell(0))) {
			readRow();
			rowInt++;
		}
		System.out.println(getClass() + ": Thread complete");
		Main.addNotification("☻ Список изделий прочитан до строки с номером: " + rowInt + " (включительно)");
	}

	void readRow() {
		row = sheet.getRow(rowInt);
		reinforcementProductArrayList.add(new ReinforcementProduct(
				checkPosition(parseIntFromNumber(row.getCell(0))),
				checkDiameter(parseIntFromNumber(row.getCell(2))),
				checkRFClass(row.getCell(3).getStringCellValue()),
				checkMaxLength(parseIntFromNumber(row.getCell(4))),
				checkMass(row.getCell(5).getNumericCellValue())
		));
		System.out.println(getClass() + ": [rowInt: " + rowInt + "]");
		System.out.println(reinforcementProductArrayList.get(reinforcementProductArrayList.size() - 1).toString());
	}

	int checkPosition(int position) {
		if (positionList.contains(position)) {
			Main.addNotification("В строке " + (rowInt + 1) + " продублированна позиция: " + position);
		}
		positionList.add(position);
		return  position;
	}

	int checkDiameter(int diameter) {
		diameterMatch = false;
		for (int i = 0; i < Pattern.diameter.length; i++) {
			if (diameter == Pattern.diameter[i]) {
				diameterMatch = true;
				diameterArrayIndex = i;
				return diameter;
			}
		}
		Main.addNotification("В строке " + (rowInt + 1) + " диаметр равен: " + diameter);
		return diameter;
	}

	RFClass checkRFClass(String string) {
		for (int i = 0; i < Pattern.rfClass500S.length; i++) {
			if (string.equalsIgnoreCase(Pattern.rfClass500S[i])) {
				return RFClass.A500S;
			}
		}
		for (int i = 0; i < Pattern.rfClass240.length; i++) {
			if (string.equalsIgnoreCase(Pattern.rfClass240[i])) {
				return RFClass.A240;
			}
		}
		for (int i = 0; i < Pattern.rfClass500.length; i++) {
			if (string.equalsIgnoreCase(Pattern.rfClass500[i])) {
				return RFClass.A500;
			}
		}
		for (int i = 0; i < Pattern.rfClass400.length; i++) {
			if (string.equalsIgnoreCase(Pattern.rfClass400[i])) {
				return RFClass.A400;
			}
		}
		for (int i = 0; i < Pattern.rfClass600.length; i++) {
			if (string.equalsIgnoreCase(Pattern.rfClass600[i])) {
				return RFClass.A600;
			}
		}
		Main.addNotification("В строке " + (rowInt + 1) + " класс арматуры: " + string);
		return RFClass.MISS_VALUE;
	}

	int checkMaxLength(int length) {
		if (length > Pattern.maxProductionLength) {
			Main.addNotification("В строке " + (rowInt + 1) + " длина изделия: " + length);
		}
		this.length = length;
		return length;
	}

	double checkMass(double mass) {
		if (diameterMatch) {
			double mass1 = length / 1000.0 * Pattern.mass3Digit[diameterArrayIndex];
			double mass2 = length / 1000.0 * Pattern.mass2Digit1[diameterArrayIndex];
			double mass3 = length / 1000.0 * Pattern.mass2Digit2[diameterArrayIndex];
			boolean match1 = mass1 - mass < 0.01 || mass1 - mass < -0.01;
			boolean match2 = mass2 - mass < 0.01 || mass2 - mass < -0.01;
			boolean match3 = mass2 - mass < 0.01 || mass3 - mass < -0.01;
			if (!match1 && !match2 && !match3) {
				Main.addNotification("В строке " + (rowInt + 1) + " масса изделия: " + mass + ". Я думаю, должно быть: " + mass1);
			}
		}
		return mass;
	}
}