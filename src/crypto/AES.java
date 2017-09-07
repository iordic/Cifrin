package crypto;

import java.security.SecureRandom;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class AES {
	
	public final int blockSize = 16;
	
	/**
	 * Genera clave AES para cifrado y descifrado
	 * @return array de valores de la clave
	 */
	public byte [] generateKey() {
		SecureRandom sr = null;
		try {
			sr = new SecureRandom();
			sr.setSeed("UCTresM.".getBytes()); // ¿Necesario? No lo parece.
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		byte[] key = sr.generateSeed(24 + blockSize + 10);
		return key;
	}
	
	/**
	 * Cifra el texto con la clave y el vector de inicialización
	 * @param plain array de valores ascii en bytes
	 * @param key clave de cifrado AES
	 * @param iv vector de inicialización, necesario en AES
	 * @return array de bytes del texto cifrado
	 */
	public byte[] encrypt(byte[] plain, byte[] key, byte[] iv) {
		try {
			PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
			//PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(new RijndaelEngine())); // Apartado H
			CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(key), iv);
			aes.init(true, ivAndKey);
			return cipherData(aes, plain);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Descifra el texto cifrado en AES
	 * @param ciphered texto cifrado
	 * @param key clave para cifrado y descifrado AES
	 * @param iv vector de inicialización
	 * @return texto plano descifrado en valores ascii
	 */
	public byte[] decrypt(byte[] ciphered, byte[] key, byte[] iv) {
		try {
			PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
			CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(key), iv);
			aes.init(false, ivAndKey);
			return cipherData(aes, ciphered);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private byte[] cipherData(PaddedBufferedBlockCipher aes, byte[] data) throws Exception {
		// Creamos un array de bytes del tamaño estimado de descifrado
		int minSize = aes.getOutputSize(data.length);
		byte[] outBuf = new byte[minSize];
		// Procesamos todos los bytes de los datos
		int length1 = aes.processBytes(data, 0, data.length, outBuf, 0);
		// Realizamos el procesamiento final (conceptualmente, es como el flush de los streams)
		int length2 = aes.doFinal(outBuf, length1);
		int actualLength = length1 + length2;
		byte[] result = new byte[actualLength];
		// Copiamos el resultado y lo devolvemos
		System.arraycopy(outBuf, 0, result, 0, result.length);
		return result;
	}
}
