import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Content {

	private Double[][] table;
	private final List<Integer> possibleHash;

	public Content() {
		possibleHash = new ArrayList<>();
		try {
			InputStream resourceAsStream = this.getClass().getResourceAsStream("/RHashCodeList.txt");
			InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			while (bufferedReader.ready()) {
				possibleHash.add(Integer.parseInt(bufferedReader.readLine()));
			}
			bufferedReader.close();
			table = new Double[8][possibleHash.size()];
		} catch (Exception e) {
			Main.log.add(e);
		}
	}

	public void put(int labelID, int hashCode, double mass) {
		try {
			int column = possibleHash.indexOf(hashCode);
			if (table[labelID - 1][column] == null) {
				table[labelID - 1][column] = mass;
			} else {
				table[labelID - 1][column] += mass;
			}
		} catch (Exception e) {
			Main.log.add(e);
		}
	}

	public void compress(ContentHead contentHead, ContentRow contentRow) {
		int height = table.length;
		int width = table[0].length;
		boolean[] rowFullness = new boolean[height];
		boolean[] headFullness = new boolean[width];

		// Read row fullness
		for (int i = 0; i < height; i++) {
			boolean fullness = false;
			for (Double cell : table[i]) {
				if (cell != null) {
					fullness = true;
					break;
				}
			}
			rowFullness[i] = fullness;
		}
		// Read column fullness
		for (int i = 0; i < width; i++) {
			boolean fullness = false;
			for (int j = 0; j < height; j++) {
				if (table[j][i] != null) {
					fullness = true;
					break;
				}
			}
			headFullness[i] = fullness;
		}
		// Create new compressed table
		List<String> newContentRowList = new ArrayList<>();
		List<ReinforcementLiteInfo> newContentHeadList = new ArrayList<>();
		int compressedHeight = 0;
		for (int i = 0; i < height; i++) {
			if (rowFullness[i]) {
				compressedHeight++;
				newContentRowList.add(contentRow.get(i));
			}
		}
		int compressedWidth = 0;
		for (int i = 0; i < width; i++) {
			if (headFullness[i]) {
				compressedWidth++;
				newContentHeadList.add(contentHead.get(i));
			}
		}
		contentHead.newList(newContentHeadList);
		contentRow.newList(newContentRowList);
		Double[][] newTable = new Double[compressedHeight][compressedWidth];
		int heightPosition = 0;
		int widthPosition;
		for (int i = 0; i < height; i++) {
			if (rowFullness[i]) {
				widthPosition = 0;
				for (int j = 0; j < width; j++) {
					if (headFullness[j]) {
						newTable[heightPosition][widthPosition] = table[i][j];
						widthPosition++;
					}
				}
				heightPosition++;
			}
		}
		table = newTable;
	}

	@Override
	public String toString() {
		String result = "";
		for (Double[] row : table) {
			result += Arrays.toString(row) + "\n";
		}
		return result.substring(0, (result.length() - 1));
	}

	public String printCompact() {
		String result = "";
		for (Double[] row : table) {
			for (Double cell : row) {
				if (cell == null) {
					result += "░";
				} else {
					result += "█";
				}
			}
			result += "\n";
		}
		return result.substring(0, (result.length() - 1));
	}

	public int getHeight() {
		return table.length;
	}

	public Double getCell(int height, int width) {
		return table[height][width];
	}

	public Double[] getFinallyVerticalSummaryMass() {
		Double[] result = new Double[table.length + 1];
		MassCounter mass = new MassCounter();
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[0].length; j++) {
				mass.add(table[i][j]);
			}
			result[i] = mass.getValue();
			mass.reset();
		}
		for (int i = 0; i < result.length - 1; i++) {
			mass.add(result[i]);
		}
		result[result.length - 1] = mass.getValue();
		return result;
	}
}