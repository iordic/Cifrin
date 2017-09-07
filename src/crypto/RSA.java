package crypto;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.KeyPairGeneratorSpi;
import org.bouncycastle.util.encoders.Base64Encoder;


public class RSA {

	/**
	 * Genera par de claves RSA (pública y privada)
	 * @param priv fichero donde se guardará la clave privada
	 * @param pub fichero donde se guardará la clave pública
	 */
	public void generateKeys(File priv, File pub) {
		try {
			KeyPairGenerator gen = KeyPairGeneratorSpi.getInstance("RSA");
			gen.initialize(1024, generateSecureRamdom());
			//gen.initialize(2048, generateSecureRamdom());
			Base64Encoder b64 = new Base64Encoder();
			KeyPair pair = gen.generateKeyPair();
			Key pubKey = pair.getPublic();
			Key privKey = pair.getPrivate();
			BufferedOutputStream pubOut = new BufferedOutputStream(new FileOutputStream(pub));
			BufferedOutputStream privOut = new BufferedOutputStream(new FileOutputStream(priv));
			b64.encode(pubKey.getEncoded(), 0, pubKey.getEncoded().length, pubOut);
			b64.encode(privKey.getEncoded(), 0, privKey.getEncoded().length, privOut);
			privOut.flush();
			privOut.close();
			pubOut.flush();
			pubOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Cifra el texto en RSA con la clave pública (cifrado asimétrico)
	 * @param inputData texto a cifrar pasado como array de bytes (valores ascii)
	 * @param keyBytes valores de bytes de la clave para cifrar
	 * @return texto cifrado
	 */
	public byte[] encrypt(byte[] inputData, byte[] keyBytes) {
		try {
			AsymmetricKeyParameter publicKey = (AsymmetricKeyParameter) PublicKeyFactory.createKey(keyBytes);
			AsymmetricBlockCipher e = new RSAEngine();
			// http://www.emc.com/collateral/white-papers/h11300-pkcs-1v2-2-rsa-cryptography-standard-wp.pdf
			e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
			e.init(true, publicKey);
			byte[] hexEncodedCipher = e.processBlock(inputData, 0, inputData.length);
			return hexEncodedCipher;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Descifra el texto con el método RSA y la clave privada
	 * @param encryptedData valores en bytes del texto cifrado
	 * @param keyBytes valores en bytes de la clave privada
	 * @return texto descifrado
	 */
	public byte[] decrypt(byte[] encryptedData, byte[] keyBytes) {
		try {
			AsymmetricKeyParameter privateKey = (AsymmetricKeyParameter) PrivateKeyFactory.createKey(keyBytes);
			AsymmetricBlockCipher e = new RSAEngine();
			e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
			e.init(false, privateKey);
			byte[] hexEncodedCipher = e.processBlock(encryptedData, 0, encryptedData.length);
			return hexEncodedCipher;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private SecureRandom generateSecureRamdom() {
		SecureRandom sr = null;
		try {
			sr = new SecureRandom();
			sr.setSeed("UCTresM.".getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return sr;
	}
}
