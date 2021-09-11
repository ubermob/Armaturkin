package armaturkin.summaryoutput;

import armaturkin.core.Main;
import armaturkin.reinforcement.RFClass;
import armaturkin.reinforcement.ReinforcementLiteInfo;
import armaturkin.reinforcement.StandardsRepository;

import java.util.ArrayList;
import java.util.List;

public class ContentHead {

	private List<ContentHeadEntry> list;
	private final Object[] blocks;

	public ContentHead(Object[] blocks) {
		this.blocks = blocks;
		list = new ArrayList<>();
		RFClass[] rfClasses = RFClass.values();
		for (int j = 0; j < (rfClasses.length - 1); j++) {
			for (int i : StandardsRepository.diameters) {
				list.add(new ContentHeadEntry(new ReinforcementLiteInfo(i, rfClasses[j], 0.0)));
			}
		}
	}

	public ContentHeadEntry get(int index) {
		return list.get(index);
	}

	public void newList(List<ContentHeadEntry> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return list.toString();
	}

	public String toPrettyString() {
		String result = "";
		for (var entry : list) {
			result += Main.properties.getProperty("content_head_pretty_string").formatted(entry.getPrettyString());
		}
		return result.substring(0, (result.length() - 1));
	}

	/**
	 * @return Возвращает массив заполнености блоков для сводной ведомости
	 */
	public boolean[] getBlockFullness() {
		// Посмотреть в файл RF_hash_code_list.txt
		boolean[] result = new boolean[RFClass.values().length + 3];
		for (int i = 0; i < result.length; i++) {
			for (var entry : list) {
				if (entry.getRfClass() == blocks[i]) {
					result[i] = true;
					break;
				}
			}
		}
		return result;
	}

	public RFClass getRFClass(int i) {
		return (RFClass) blocks[i];
	}

	public int[] getDiameters(RFClass rfClass) {
		int counter = 0;
		for (var entry : list) {
			if (entry.getRfClass() == rfClass) {
				counter++;
			}
		}
		int[] result = new int[counter];
		int resultIndex = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getRfClass() == rfClass) {
				result[resultIndex++] = list.get(i).getDiameter();
			}
		}
		return result;
	}

	public int[] getIndexes(RFClass rfClass) {
		int counter = 0;
		for (var entry : list) {
			if (entry.getRfClass() == rfClass) {
				counter++;
			}
		}
		int[] result = new int[counter];
		int resultIndex = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getRfClass() == rfClass) {
				result[resultIndex++] = i;
			}
		}
		return result;
	}
}