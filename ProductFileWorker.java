import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.*;

public class ProductFileWorker implements Runnable {

	//private final FileInputStream productFileInputStream; // Do change final to volatile?
	private final String path;
	private final ArrayList<ReinforcementProduct> reinforcementProductArrayList; // Do change final to volatile?
	Workbook workbook;
	Sheet sheet;
	Row row;
	int rowInt;
	Cell cell;

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
			/*new ReinforcementProduct(
					parseInt(row.getCell(0)),
					checkDiameter(parseInt(row.getCell(2))),

			);*/
			rowInt++;
		}
		System.out.println(rowInt);
		reinforcementProductArrayList.add(new ReinforcementProduct(1, 1, RFClass.A500, 2 , 3));
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

	int checkDiameter(int diameter) {
		boolean match = false;
		for (int i = 0; i < Sample.diameter.length; i++) {
			if (diameter == Sample.diameter[i]) {
				match = true;
			}
		}
		if (!match) {
			Main.addNotification("В строчке " + (rowInt + 1) + " диаметр равен " + diameter);
		}
		return diameter;
	}

	int parseInt(Cell cell) {
		return (int) Math.round(cell.getNumericCellValue());
	}
}