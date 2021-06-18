import org.apache.commons.codec.digest.DigestUtils;

public interface FileHashCode {
	default String getFileHashCode(String fullPath) {
		return "@" + DigestUtils.sha256Hex(fullPath).substring(0, 8);
	}
}