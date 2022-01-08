package armaturkin.utils;

import armaturkin.reinforcement.StandardsRepository;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.HashMap;

public class CellStyleRepository {

	private final HashMap<Integer, CellStyle> cellStyleHashMap = new HashMap<>();
	private final CellStyle headTableNameCellStyle;
	private final CellStyle headTableCellStyle;
	private final CellStyle headTableCellStyleWithTextWrap;
	private CellStyle cellStyle;
	private XSSFFont font;
	private final XSSFWorkbook workbook;

	public CellStyleRepository(XSSFWorkbook workbook) {
		this.workbook = workbook;
		// Build headTableNameCellStyle
		cellStyle = workbook.createCellStyle();

		font = workbook.createFont();
		font.setFontHeight((short) 170); // 8.5 * 20
		cellStyle.setFont(font);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		BorderStyle borderStyle = BorderStyle.THIN;
		cellStyle.setBorderBottom(borderStyle);
		cellStyle.setWrapText(true);

		headTableNameCellStyle = cellStyle;

		cellStyle = workbook.createCellStyle();
		cellStyle.cloneStyleFrom(headTableNameCellStyle);

		font = workbook.createFont();
		font.setFontHeight((short) 142); // 7.1 * 20
		cellStyle.setFont(font);
		cellStyle.setBorderTop(borderStyle);
		cellStyle.setBorderLeft(borderStyle);
		cellStyle.setBorderRight(borderStyle);
		cellStyle.setWrapText(false);

		headTableCellStyle = cellStyle;

		// Build headTableCellStyleWithTextWrap
		cellStyle = workbook.createCellStyle();
		cellStyle.cloneStyleFrom(headTableCellStyle);
		cellStyle.setWrapText(true);

		headTableCellStyleWithTextWrap = cellStyle;
	}

	public CellStyle getCellStyle(int diameter) {
		if (!cellStyleHashMap.containsKey(diameter)) {
			buildCellStyleHashMap(diameter);
		}
		return cellStyleHashMap.get(diameter);
	}

	public CellStyle getCellStyleWithFormat(int diameter, short dataFormat) {
		CellStyle sampleCellStyle = cellStyleHashMap.get(diameter);
		cellStyle = workbook.createCellStyle();
		cellStyle.cloneStyleFrom(sampleCellStyle);
		cellStyle.setDataFormat(dataFormat);
		return cellStyle;
	}

	public CellStyle getHeadTableNameCellStyle() {
		return headTableNameCellStyle;
	}

	public CellStyle getHeadTableCellStyle() {
		return headTableCellStyle;
	}

	public CellStyle getHeadTableCellStyleWithTextWrap() {
		return headTableCellStyleWithTextWrap;
	}

	private void buildCellStyleHashMap(int diameter) {
		cellStyle = workbook.createCellStyle();
		cellStyle.cloneStyleFrom(headTableCellStyle);

		font = workbook.createFont();
		font.setFontHeight((short) 142); // 7.1 * 20
		font.setColor(new XSSFColor(StandardsRepository.getRgb(diameter), null));
		cellStyle.setFont(font);

		cellStyleHashMap.put(diameter, cellStyle);
	}
}