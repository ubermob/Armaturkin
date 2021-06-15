public interface FileNameHashCode {
	default String getFileNameHashCode(String fullPath) {
		String[] strings = fullPath.split("\\\\");
		char[] chars = strings[strings.length - 1].toCharArray();
		long sum = 1;
		for (char c : chars) {
			sum *= c;
		}
		String hashCode = Long.toHexString(sum);
		int end = 0;
		for (int i = (hashCode.length() - 1); i >= 0; i--) {
			if (hashCode.charAt(i) == '0') {
				end = i;
			}
		}
		return "@" + hashCode.substring(0, end);
	}
}