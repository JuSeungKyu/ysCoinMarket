package ysCoin.shahash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaHash {

	private String originalString;
	private String hashedString;

	public ShaHash(String originalString) {
		this.originalString = originalString;
		this.hashedString = hashingFunction(originalString);
	}

	private String hashingFunction(String originalString) {

		MessageDigest md;

		try {
			md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(originalString.getBytes(StandardCharsets.UTF_8));
			return byteToHex(hash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String byteToHex(byte[] bytes) {
    	StringBuilder builder = new StringBuilder();
    	
    	for (byte data : bytes) {
    		builder.append(String.format("%02x", data));
		}
    	
    	return builder.toString();
	}

	public String getOriginalString() {
		return originalString;
	}

	public void setOriginalString(String originalString) {
		this.originalString = originalString;
	}

	public String getHashedString() {
		return hashedString;
	}

	public void setHashedString(String hashedString) {
		this.hashedString = hashedString;
	}

}
