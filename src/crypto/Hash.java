package crypto;

import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;

/**
 * Clase para generar checksums
 * @author Jordi Castelló
 */
public class Hash {
	
	/**
	 * Genera el hash md5 de un texto pasado como array de valores ascii (bytes)
	 * @param input texto pasado a array de valores ascii
	 * @return suma md5 en forma de array de bytes
	 */
	public byte [] md5Digest(byte [] input) {
		MD5Digest md5 = new MD5Digest();
		if (input != null) {
			byte[] result = digest(md5, input);
			return result;
		}
		return null;
	}
	
	/**
	 * Genera el hash sha1 de un texto pasado como array de valores ascii (bytes)
	 * @param input texto pasado a array de valores ascii
	 * @return suma sha1 en forma de array de bytes
	 */
	public byte [] sha1Digest(byte [] input) {
		SHA1Digest sha1 = new SHA1Digest();
		if (input != null) {
			byte[] result = digest(sha1, input);
			return result;
		}
		return null;
	}
	
	/**
	 * Genera el hash sha256 de un texto pasado como array de valores ascii (bytes)
	 * @param input texto pasado a array de valores ascii
	 * @return suma sha256 en forma de array de bytes
	 */
	public byte [] sha256Digest(byte [] input) {
		SHA256Digest sha256 = new SHA256Digest();
		if (input != null) {
			byte[] result = digest(sha256, input);
			return result;
		}
		return null;
	}
	
	/**
	 * Genera el hash sha512 de un texto pasado como array de valores ascii (bytes)
	 * @param input texto pasado a array de valores ascii
	 * @return suma sha512 en forma de array de bytes
	 */
	public byte [] sha512Digest(byte [] input) {
		SHA512Digest sha512 = new SHA512Digest();
		if (input != null) {
			byte[] result = digest(sha512, input);
			return result;
		}
		return null;
	}

	/**
	 * Realiza la suma en función de si es md5 o sha1 y devuelve el resultado
	 * @param digest tipo de función (md5 o sha1)
	 * @param input texto del cual generar la suma
	 * @return resultado de la suma
	 */
	private byte[] digest(GeneralDigest digest, byte [] input) {
		digest.update(input, 0, input.length);
		byte [] result = new byte[digest.getDigestSize()];
		digest.doFinal(result, 0);
		return result;
	}	

	/**
	 * Realiza la suma sha512 y devuelve el resultado
	 * @param digest tipo de función (md5 o sha1)
	 * @param input texto del cual generar la suma
	 * @return resultado de la suma
	 */
	private byte[] digest(SHA512Digest digest, byte[] input) {
		digest.update(input, 0, input.length);
		byte [] result = new byte[digest.getDigestSize()];
		digest.doFinal(result, 0);
		return result;
	}

}
