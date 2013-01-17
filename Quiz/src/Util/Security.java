/**
 * @author vrolijkx
 */
package Util;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hulp klassen voor encriptie en salt generatie.
 * @author vrolijkx
 *
 */
public class Security {
	private static java.util.Random random = new java.util.Random();
	private static final String saltChars = "abcdefghijklmonpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&@#?./+=:!-)(][";
	private static MessageDigest encoder;
	
	public static String getRandomSalt() {
		StringBuilder builder = new StringBuilder();
		int max = saltChars.length();
		
		
		builder.setLength(40);
		for(int i = 0; i<40; i++) {
			builder.append(saltChars.charAt(random.nextInt(max)));
		}
		
		return builder.toString();
	}
	
	public static String encrypt(String pass, String salt) {
		 try {
			encoder = MessageDigest.getInstance("sha-256");
			return hash(pass + hash(salt));
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	public static boolean checkPassword(String password,String salt,String hash) {
		if(encrypt(password,salt).equals(hash)) {
			return true;
		} else {
			return false;
		}
	}
	
	private static String hash(String toHash) {
		encoder.update(toHash.getBytes(Charset.forName("Unicode")));
		String hash;
		hash = new BigInteger(1, encoder.digest()).toString(36);
		return hash;
	}
	
	
	public static void main(String[] args) {
		String salt;
		String pass = "kristof123";
		for(int i = 0; i<20; i++) {
			salt = getRandomSalt();
			System.out.println("salt :  " + salt);
			System.out.println(encrypt(pass, salt));
			System.out.println(encrypt(pass, salt));
			pass = encrypt(pass, salt);
			System.out.printf("salt lengt: %d  hash lengt: %d\n",salt.length(),pass.length());
		}
	}
}
