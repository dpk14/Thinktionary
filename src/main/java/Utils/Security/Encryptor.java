package Utils.Security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {

    private static final String ENCRYPTING_ALGORITHM = "MD5";

    public static String encryptMD5(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance(ENCRYPTING_ALGORITHM);
            md.update(string.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i=0; i< bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
