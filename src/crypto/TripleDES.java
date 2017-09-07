package crypto;

import java.security.SecureRandom;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.generators.DESedeKeyGenerator;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.DESedeParameters;
import org.bouncycastle.crypto.params.KeyParameter;

public class TripleDES {

	BlockCipher engine = new DESedeEngine();
	
	/**
	 * Genera una clave de cifrado y descifrado para TripleDES (cifrado simétrico)
	 * @return clave generado como array de bytes
	 */
	public byte[] generateKey() {
		SecureRandom sr = null;
		try {
			sr = new SecureRandom();
			sr.setSeed("UCTresM.".getBytes());	// Semilla para generar
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		KeyGenerationParameters kgp = new KeyGenerationParameters(sr, (DESedeParameters.DES_EDE_KEY_LENGTH) * 8);
		DESedeKeyGenerator kg = new DESedeKeyGenerator();
		kg.init(kgp);
		byte[] key = kg.generateKey();
		return key;
	}

	/**
	 * Cifra el texto pasado como array de bytes (valores ascii) en TripleDES
	 * @param key clave de cifrado TripleDES como array de bytes
	 * @param ptBytes mensaje a cifrar
	 * @return
	 */
	public byte[] encrypt(byte[] key, byte[] ptBytes) {
		BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
		cipher.init(true, new KeyParameter(key));
		byte[] rv = new byte[cipher.getOutputSize(ptBytes.length)];
		int tam = cipher.processBytes(ptBytes, 0, ptBytes.length, rv, 0);
		try {
			cipher.doFinal(rv, tam);
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return rv;
	}
	
	/**
	 * Descifra el texto pasado con TripleDES a un array de bytes que representan los valores ascii
	 * @param key clave de cifrado TripleDES
	 * @param cipherText texto cifrado
	 * @return texto plano con valores ascii en forma de array de bytes
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
