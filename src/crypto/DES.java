package crypto;

import java.security.SecureRandom;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.generators.DESKeyGenerator;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.DESParameters;
import org.bouncycastle.crypto.params.KeyParameter;

public class DES {
	
	BlockCipher engine = new DESEngine();
	
	/**
	 * Genera clave DES de 8 bytes y la devuelve como array
	 * @return array de bytes que contiene los valores de la clave
	 */
	public byte[] generateKey() {
		SecureRandom sr = null;
		try {
			sr = new SecureRandom();
			sr.setSeed("UCTresM.".getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		KeyGenerationParameters kgp = new KeyGenerationParameters(sr, (DESParameters.DES_KEY_LENGTH) * 8);
		DESKeyGenerator kg = new DESKeyGenerator();
		kg.init(kgp);
		byte[] key = kg.generateKey();
		return key;
	}
	
	/**
	 * Cifra el mensaje con la clave DES que le pasamos por parámetro
	 * @param key clave DES en forma de array de bytes
	 * @param ptBytes array de bytes con los valores ascii del texto a cifrar
	 * @return
	 */
	public byte[] encrypt(byte[] key, byte[] ptBytes) {
		BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
		cipher.init(true, new KeyParameter(key));
		byte[] rv = new byte[cipher.getOutputSize(ptBytes.length)];
		int tam = cipher.processBytes(ptBytes, 0, ptBytes.length, rv, 0);
		try {
			cipher.doFinal(rv, tam);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return rv;
	}
	
	/**
	 * Descifra el texto con la clave DES que se ha usado para cifrar
	 * @param key clave DES para descifrar como array de valores
	 * @param cipherText array de valores con el texto a descifrar
	 * @return array de valores ascii con el texto descifrado
	 */
	public byte[] decrypt(byte[] key, byte[] cipherText) {
		BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
		cipher.init(false, new KeyParameter(key));
		byte[] rv = new byte[cipher.getOutputSize(cipherText.length)];
		int tam = cipher.processBytes(cipherText, 0, cipherText.length, rv, 0);
		try {
			cipher.doFinal(rv, tam);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return rv;
	}
}
