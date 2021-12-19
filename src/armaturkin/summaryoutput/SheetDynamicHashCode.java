package armaturkin.summaryoutput;

import armaturkin.core.Main;
import armaturkin.manuallyentry.ManuallyEntry;
import armaturkin.steelcomponent.Image;
import armaturkin.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey Korneychuk on 10-Dec-21
 * @version 1.0
 */
public class SheetDynamicHashCode {

	private static final byte validMaxSize = 100;
	private static ArrayList<Image> sortedCurrentSheetList = new ArrayList<>();
	private static int effectiveSize = 0;

	public static ArrayList<Image> sortAndGetSortedCurrentSheetList(List<ManuallyEntry> manuallySummaryEntries) {
		sortedCurrentSheetList = new ArrayList<>();
		for (var v : manuallySummaryEntries) {
			if (v.isSheet()) {
				sortedCurrentSheetList.add(v.getLightInfoCastingToImage());
			}
		}
		effectiveSize = sortedCurrentSheetList.size();
		if (effectiveSize != 0) {
			deleteDuplicates();
			sort();
		} else {
			Main.log.add(Main.getProperty("empty_dynamic_hash_code_list"));
		}
		while (sortedCurrentSheetList.size() < validMaxSize) {
			sortedCurrentSheetList.add(null);
		}
		return sortedCurrentSheetList;
	}

	public static ArrayList<Image> getSortedCurrentSheetList() {
		return sortedCurrentSheetList;
	}

	public static int getNullFillingNumber() {
		return validMaxSize - effectiveSize;
	}

	public static int getDynamicHashCode(Image image) {
		ArrayList<Image> list = SheetDynamicHashCode.getSortedCurrentSheetList();
		for (var v : list) {
			if (v == null) {
				break;
				// break for loop. Filling with null objects at the end of the sheet is
				// guaranteed in SummarySheetSorter class.
			}
			if (v.getB().equals(image.getB()) && v.getC().equals(image.getC())) {
				return 500 + list.indexOf(v);
			}
		}
		return 0;
	}

	private static void deleteDuplicates() {
		ArrayList<Image> nonDuplicates = new ArrayList<>();
		for (var v : sortedCurrentSheetList) {
			boolean isUnique = true;
			for (var none : nonDuplicates) {
				if (v.sameImageOfSheet(none)) {
					isUnique = false;
				}
			}
			if (isUnique) {
				nonDuplicates.add(v);
			}
		}
		sortedCurrentSheetList = nonDuplicates;
		effectiveSize = sortedCurrentSheetList.size();
		if (effectiveSize > validMaxSize) {
			Main.log.add(Main.getProperty("invalid_size_of_dynamic_hash_code_list")
					.formatted(effectiveSize, validMaxSize));
		}
		Main.log.add(Main.getProperty("dynamic_non_duplicated_hash_code_list")
				.formatted(effectiveSize, buildString()));
	}

	private static void sort() {
		sortedCurrentSheetList.sort(Image::compareTo);
		Main.log.add(Main.getProperty("dynamic_sorted_hash_code_list")
				.formatted(effectiveSize, buildString()));
	}

	private static String buildString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < effectiveSize; i++) {
			builder.append(sortedCurrentSheetList.get(i).toString());
			builder.append(", ");
		}
		return StringUtil.cutEnd(builder.toString(), 2);
	}
}