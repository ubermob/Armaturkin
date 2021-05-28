import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.*;

public class ProductFileWorker implements Runnable {

	private final String path;
	private final ArrayList<ReinforcementProduct> reinforcementProductArrayList; // Do change final to volatile?
	private Workbook workbook;
	private Sheet sheet;
	private Row row;
	private int rowInt;
	private ArrayList<Integer> positionList = new ArrayList<>();
	private int diameter;
	private int diameterArrayIndex;
	private boolean diameterMatch;
	private int length;

	public ProductFileWorker(String path, ArrayList<ReinforcementProduct> reinforcementProductArrayList) {
		this.reinforcementProductArrayList = reinforcementProductArrayList;
		this.path = path;
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
			row = sheet.getRow(rowInt);
			new ReinforcementProduct(
					checkPosition(parseInt(row.getCell(0))),
					checkDiameter(parseInt(row.getCell(2))),
					checkRFClass(row.getCell(3).getStringCellValue()),
					checkMaxLength(parseInt(row.getCell(4))),
					checkMass(row.getCell(5).getNumericCellValue())
			);
			System.out.println(rowInt);
			rowInt++;
		}
		Main.addNotification("☻ Прочитано до " + rowInt);
	}

	boolean isCellEmpty(Cell cell) {
		// https://stackoverflow.com/questions/15764417/how-to-check-if-an-excel-cell-is-empty-using-apache-poi/15779444
		if (cell == null) {
			return true;
		}
		if (cell.getCellType() == CellType.BLANK) {
			return true;
		}
		return false;
	}

	int checkPosition(int position) {
		if (positionList.contains(position)) {
			Main.addNotification("В строчке " + (rowInt + 1) + " продублированна позиция: " + position);
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
		Main.addNotification("В строчке " + (rowInt + 1) + " диаметр равен: " + diameter);
		this.diameter = diameter;
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
		Main.addNotification("В строчке " + (rowInt + 1) + " класс арматуры: " + string);
		return RFClass.MISS_VALUE;
	}

	int checkMaxLength(int length) {
		if (length > Pattern.maxProductionLength) {
			Main.addNotification("В строчке " + (rowInt + 1) + " длина изделия: " + length);
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
				Main.addNotification("В строчке " + (rowInt + 1) + " масса изделия: " + mass + ". Я думаю, должно быть: " + mass1);
				//Main.addNotification("Log: " + mass1 + "\n" + mass2 + "\n" + mass3);
			}
		}
		return mass;
	}

	int parseInt(Cell cell) {
		return (int) Math.round(cell.getNumericCellValue());
	}
}