package armaturkin.summaryoutput;

import armaturkin.core.Main;
import armaturkin.reinforcement.RFClass;
import armaturkin.reinforcement.ReinforcementLiteInfo;
import armaturkin.reinforcement.StandardsRepository;
import armaturkin.steelcomponent.HotRolledSteelType;
import armaturkin.steelcomponent.Image;
import armaturkin.steelcomponent.SteelComponentRepository;
import armaturkin.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ContentHead {

	private List<ContentHeadEntry> list;
	private final Object[] blocks;

	public ContentHead(Object[] blocks, int hashesSize, ArrayList<Image> sortedSheets) throws Exception {
		this.blocks = blocks;
		list = new ArrayList<>();
		RFClass[] rfClasses = RFClass.values();
		// exclude "UNKNOWN" value
		for (int j = 0; j < (rfClasses.length - 1); j++) {
			for (int i : StandardsRepository.diameters) {
				list.add(new ContentHeadEntry(new ReinforcementLiteInfo(i, rfClasses[j])));
			}
		}
		for (var v : SteelComponentRepository.getFullEqualAnglesImages()) {
			list.add(new ContentHeadEntry(v));
		}
		for (var v : SteelComponentRepository.getFullUnequalAnglesImages()) {
			list.add(new ContentHeadEntry(v));
		}
		for (var v : sortedSheets) {
			list.add(new ContentHeadEntry(v));
		}
		if (list.size() != hashesSize) {
			throw new Exception("\"ContentHead.list.size\"=" + list.size()
					+ " must be equal \"ContentHeadPlacement.hashes.size\"=" + hashesSize);
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
			String string = entry.getPrettyString();
			if (string.equals("-")) {
				break;
			}
			result += Main.getProperty("content_head_pretty_string").formatted(string);
		}
		int nullCounter = SheetDynamicHashCode.getNullFillingNumber();
		if (nullCounter != 0) {
			result += Main.getProperty("content_head_pretty_string").formatted("-");
			result = StringUtil.cutEndIfLastCharIs(result, ',');
			result += "x" + nullCounter;
		} else {
			result = StringUtil.cutEndIfLastCharIs(result, ',');
		}
		return result;
	}

	/**
	 * @return Возвращает массив заполнености блоков для сводной ведомости
	 */
	public boolean[] getBlockFullness() {
		// Посмотреть в файл RF_hash_code_list.txt
		boolean[] result = new boolean[blocks.length];
		for (int i = 0; i < result.length; i++) {
			for (var entry : list) {
				if (entry.getType() == blocks[i]) {
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

	public HotRolledSteelType getHotRolledSteelType(int i) {
		return (HotRolledSteelType) blocks[i];
	}

	public int[] getDiameters(RFClass rfClass) {
		List<Integer> tmpList = new ArrayList<>();
		for (var entry : list) {
			if (entry.isInstanceOfLiteInfo() && entry.getRfClass() == rfClass) {
				tmpList.add(entry.getDiameter());
			}
		}
		Object[] objects = tmpList.toArray();
		int[] result = new int[objects.length];
		for (int i = 0; i < objects.length; i++) {
			result[i] = (Integer) objects[i];
		}
		return result;
	}

	public int[] getIndexes(RFClass rfClass) {
		List<Integer> tmpList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isInstanceOfLiteInfo() && list.get(i).getRfClass() == rfClass) {
				tmpList.add(i);
			}
		}
		Object[] objects = tmpList.toArray();
		int[] result = new int[objects.length];
		for (int i = 0; i < objects.length; i++) {
			result[i] = (Integer) objects[i];
		}
		return result;
	}

	public List<Image> getImages(HotRolledSteelType hotRolledSteelType) {
		List<Image> resultList = new ArrayList<>();
		for (var entry : list) {
			if (entry.isInstanceOfImage() && entry.getImage().getHotRolledSteelType() == hotRolledSteelType) {
				resultList.add(entry.getImage());
			}
		}
		return resultList;
	}

	public int[] getIndexes(HotRolledSteelType hotRolledSteelType) {
		List<Integer> tmpList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isInstanceOfImage() && list.get(i).getImage().getHotRolledSteelType() == hotRolledSteelType) {
				tmpList.add(i);
			}
		}
		Object[] objects = tmpList.toArray();
		int[] result = new int[objects.length];
		for (int i = 0; i < objects.length; i++) {
			result[i] = (Integer) objects[i];
		}
		return result;
	}
}