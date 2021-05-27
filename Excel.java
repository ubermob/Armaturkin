import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;

public class Excel {
    static Workbook wb;
    static Sheet sheet;
    static Row row;
    static Cell cell;

    public static void initialize(String path) throws IOException {
        wb = WorkbookFactory.create(new FileInputStream(path));
        sheet = wb.getSheetAt(0);
    }

    public static void selectCell(int row, int column) {
        Excel.row = sheet.getRow(row);
        Excel.cell = Excel.row.getCell(column);
    }
}