package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFileChooser;

import org.bouncycastle.util.encoders.Base64Encoder;
import org.bouncycastle.util.encoders.Hex;

import crypto.*;

/**
 * Clase que trabaja como controlador, se encarga de gestionar la GUI y manejar los ficheros.
 * @author Jordi Castelló
 *
 */
public class Cifrin implements ActionListener {

	MainWindow mainApp;
	FileHandler file;
	
	public Cifrin() {
		mainApp = new MainWindow();
		file = new FileHandler();
		addActionListeners();
		addActionCommands();
	}
	
	/**
	 * Agrega action listeners a todos los botones y opciones de menús
	 */
	public void addActionListeners() {
		mainApp.openMenuItem.addActionListener(this);
		mainApp.saveMenuItem.addActionListener(this);
		mainApp.saveAsMenuItem.addActionListener(this);
		mainApp.closeMenuItem.addActionListener(this);
		mainApp.desMenuItem.addActionListener(this);
		mainApp.tripleDesMenuItem.addActionListener(this);
		mainApp.aesMenuItem.addActionListener(this);
		mainApp.rsaMenuItem.addActionListener(this);
		mainApp.functionList.addActionListener(this);
		mainApp.operationList.addActionListener(this);
		mainApp.operationButton.addActionListener(this);	
	}
	
	/**
	 * Establece los comandos de los botones y las opciones de menús
	 */
	public void addActionCommands() {
		mainApp.openMenuItem.setActionCommand("openFile");
		mainApp.saveMenuItem.setActionCommand("saveFile");
		mainApp.saveAsMenuItem.setActionCommand("saveFileAs");
		mainApp.closeMenuItem.setActionCommand("closeFile");
		mainApp.desMenuItem.setActionCommand("desKeyGenerate");
		mainApp.tripleDesMenuItem.setActionCommand("tripleDesKeyGenerate");
		mainApp.aesMenuItem.setActionCommand("aesKeyGenerate");
		mainApp.rsaMenuItem.setActionCommand("rsaKeysGenerate");
		mainApp.functionList.setActionCommand("updateSublist");
		mainApp.operationList.setActionCommand("setOperation");		
		mainApp.operationButton.setActionCommand("desEncrypt");
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		// Aquí realizamos todas las operaciones según el comando que entre
		if (event.getActionCommand().equals("updateSublist")) {
			mainApp.generateSublist();
		}
		if (event.getActionCommand().equals("setOperation")) {
			mainApp.updateButtonLabel();
			updateOperationButton();
		}
		// Operaciones sobre ficheros
		if (event.getActionCommand().equals("openFile")) {
			file.selectFile(mainApp.text);
			mainApp.setTitle("Cifrín - " + file.getFile().getPath());
		}
		if (event.getActionCommand().equals("closeFile")) {
			file.closeFile(mainApp.text);
			mainApp.text.setText(null);
			mainApp.setTitle("Cifrín");
		}
		if (event.getActionCommand().equals("saveFile")) {
			try {
				file.saveFile(mainApp.text);
				mainApp.setTitle("Cifrín - " + file.getFile().getPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (event.getActionCommand().equals("saveFileAs")) {
			try {
				file.saveFileAs(mainApp.text);
				mainApp.setTitle("Cifrín - " + file.getFile().getPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Operaciones sobre cifrado
		if (event.getActionCommand().equals("aesEncrypt")) {
			byte [] key = getKey();	// Coge valores de la clave, contiene IV y la clave
			AES aes = new AES();
			// Sacamos el vector de inicialización
			byte [] iv = Arrays.copyOfRange(key, 24, 24 + aes.blockSize);
			byte [] subkey = Arrays.copyOfRange(key, 0, 24);
			byte [] result = aes.encrypt(mainApp.text.getText().getBytes(), subkey, iv);
			mainApp.text.setText(null);
			mainApp.text.setText(new String(Hex.encode(result)));
		}
		if (event.getActionCommand().equals("aesDecrypt")) {
			byte [] key = getKey();
			AES aes = new AES();
			byte [] iv = Arrays.copyOfRange(key, 24, 24 + aes.blockSize);
			byte [] subkey = Arrays.copyOfRange(key, 0, 24);
			byte [] result = aes.decrypt(Hex.decode(mainApp.text.getText()), subkey, iv);
			mainApp.text.setText(null);
			mainApp.text.append(new String(result));
		}
		if (event.getActionCommand().equals("desEncrypt")) {
			byte [] key = getKey();
			DES des = new DES();
			byte [] result = des.encrypt(key, mainApp.text.getText().getBytes());
			mainApp.text.setText(null);
			mainApp.text.append(new String(Hex.encode(result)));
		}
		if (event.getActionCommand().equals("desDecrypt")) {
			byte [] key = getKey();
			DES des = new DES();
			byte [] result = des.decrypt(key, Hex.decode(mainApp.text.getText()));
			mainApp.text.setText(null);
			mainApp.text.append(new String(result));
		}
		if (event.getActionCommand().equals("tripleDesEncrypt")) {	
			byte [] key = getKey();
			TripleDES tripleDes = new TripleDES();
			byte [] result = tripleDes.encrypt(key, mainApp.text.getText().getBytes());
			mainApp.text.setText(null);
			mainApp.text.append(new String(Hex.encode(result)));
		}		
		if (event.getActionCommand().equals("tripleDesDecrypt")) {
			byte [] key = getKey();
			TripleDES tripleDes = new TripleDES();
			byte [] result = tripleDes.decrypt(key, Hex.decode(mainApp.text.getText()));
			mainApp.text.setText(null);
			mainApp.text.append(new String(result));
		}
		if (event.getActionCommand().equals("rsaEncrypt")) {
			byte [] key = getRSAKey("publica");
			RSA rsa = new RSA();
			if (key != null) {
				Base64Encoder b64 = new Base64Encoder();
				ByteArrayOutputStream keyBytes = new ByteArrayOutputStream();
				BufferedOutputStream bKey = new BufferedOutputStream(keyBytes);
				try {
					b64.decode(key, 0, key.length, bKey);
					bKey.flush();
					bKey.close();
					byte[] result = rsa.encrypt(mainApp.text.getText().getBytes(), keyBytes.toByteArray());
					mainApp.text.setText(null);
					mainApp.text.append(new String(Hex.encode(result)));
				} 
				catch (IOException e) {
					e.printStackTrace();
				}			
			}			
		}
		if (event.getActionCommand().equals("rsaDecrypt")) {
			byte [] key = getRSAKey("privada");
			RSA rsa = new RSA();			
			if (key != null) {
				Base64Encoder b64 = new Base64Encoder();
				ByteArrayOutputStream keyBytes = new ByteArrayOutputStream();
				BufferedOutputStream bKey = new BufferedOutputStream(keyBytes);
				try {
					b64.decode(key, 0, key.length, bKey);
					bKey.flush();
					bKey.close();
					// Decrypt Operation
					byte[] result = rsa.decrypt(Hex.decode(mainApp.text.getText()), keyBytes.toByteArray());
					mainApp.text.setText(null);
					mainApp.text.append(new String(result));
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (event.getActionCommand().equals("md5Generate")) {
			Hash md5 = new Hash();				
			byte [] md5Sum = md5.md5Digest(mainApp.text.getText().getBytes());
			mainApp.text.setText(null);
			mainApp.text.setText(new String(Hex.encode(md5Sum)));
		}
		if (event.getActionCommand().equals("sha1Generate")) {
			Hash sha1 = new Hash();			
			byte [] sha1Sum = sha1.sha1Digest(mainApp.text.getText().getBytes());
			mainApp.text.setText(null);
			mainApp.text.setText(new String(Hex.encode(sha1Sum)));
		}
		if (event.getActionCommand().equals("sha256Generate")) {
			Hash sha256 = new Hash();			
			byte [] sha256Sum = sha256.sha256Digest(mainApp.text.getText().getBytes());
			mainApp.text.setText(null);
			mainApp.text.setText(new String(Hex.encode(sha256Sum)));
		}
		if (event.getActionCommand().equals("sha512Generate")) {
			Hash sha512 = new Hash();			
			byte [] sha512Sum = sha512.sha512Digest(mainApp.text.getText().getBytes());
			mainApp.text.setText(null);
			mainApp.text.setText(new String(Hex.encode(sha512Sum)));
		}
		// Generar claves
		if (event.getActionCommand().equals("desKeyGenerate")) {
			DES des = new DES();
			byte [] key = Hex.encode(des.generateKey());
			String keyText = new String(key);		
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter(file));
					writer.write(keyText);
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}
		}
		if (event.getActionCommand().equals("tripleDesKeyGenerate")) {
			TripleDES tripleDes = new TripleDES();
			byte [] key = Hex.encode(tripleDes.generateKey());
			String keyText = new String(key);		
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter(file));
					writer.write(keyText);
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}
		}
		if (event.getActionCommand().equals("aesKeyGenerate")) {
			AES aes = new AES();
			byte [] key = Hex.encode(aes.generateKey());
			String keyText = new String(key);		
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter(file));
					writer.write(keyText);
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}
		}
		if (event.getActionCommand().equals("rsaKeysGenerate")) {
			RSA rsa = new RSA();
			File pubKey = null;
			File privKey = null;
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Guardar clave pública");
			if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {				
				pubKey = fileChooser.getSelectedFile();			
			}
			fileChooser.setDialogTitle("Guardar clave privada");
			if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {			
				privKey = fileChooser.getSelectedFile();			
			}
			if (pubKey != null && privKey != null) {
				rsa.generateKeys(privKey, pubKey);
			}
		}
	}	
	
	/**
	 * Establece el comando del botón según la opción que hayamos seleccionado
	 */
	private void updateOperationButton() {
		int selectedFunction = mainApp.functionList.getSelectedIndex();
		int selectedOperation = mainApp.operationList.getSelectedIndex();
		switch(selectedFunction) {
		case Constants.AES:
			if (selectedOperation == Constants.CIPHER_OPERATION) mainApp.operationButton.setActionCommand("aesEncrypt");
			else if (selectedOperation == Constants.DECIPHER_OPERATION) mainApp.operationButton.setActionCommand("aesDecrypt");
			break;
		case Constants.DES:
			if (selectedOperation == Constants.CIPHER_OPERATION) mainApp.operationButton.setActionCommand("desEncrypt");
			else if (selectedOperation == Constants.DECIPHER_OPERATION) mainApp.operationButton.setActionCommand("desDecrypt");
			break;
		case Constants.TRIPLE_DES:
			if (selectedOperation == Constants.CIPHER_OPERATION) mainApp.operationButton.setActionCommand("tripleDesEncrypt");
			else if (selectedOperation == Constants.DECIPHER_OPERATION) mainApp.operationButton.setActionCommand("tripleDesDecrypt");
			break;
		case Constants.RSA:
			if (selectedOperation == Constants.CIPHER_OPERATION) mainApp.operationButton.setActionCommand("rsaEncrypt");
			else if (selectedOperation == Constants.DECIPHER_OPERATION) mainApp.operationButton.setActionCommand("rsaDecrypt");
			break;
		case Constants.HASH:
			if (selectedOperation == Constants.MD5) mainApp.operationButton.setActionCommand("md5Generate");
			else if (selectedOperation == Constants.SHA1) mainApp.operationButton.setActionCommand("sha1Generate");
			else if (selectedOperation == Constants.SHA256) mainApp.operationButton.setActionCommand("sha256Generate");
			else if (selectedOperation == Constants.SHA512) mainApp.operationButton.setActionCommand("sha512Generate");
			break;
		}
	}
	
	/**
	 * Recoge el valor hexadecimal de una clave guardado en un fichero de texto
	 * @return clave con valores hexadecimales
	 */
	private byte [] getKey() {
		File keyFile;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Abrir fichero de clave");
		if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
			keyFile = fileChooser.getSelectedFile();			
			try {
				BufferedReader in = new BufferedReader(new FileReader(keyFile));
				String value = "";
				try {
					String line = in.readLine();
					while(line != null) {
						value += line;
						line = in.readLine();
					}
					in.close();
					byte [] key = Hex.decode(value);
					return key;
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}		
		}
		return null;
	}
	
	/**
	 * Recoge el valor de una clave guardado en un fichero de texto
	 * (cuando no es en hexadecimal, como es el caso de RSA)
	 * @param tipo "publica" o "privada" para el encabezado del diálogo para abrir el fichero
	 * @return array de valores en bytes del código ascii de la clave
	 */
	private byte [] getRSAKey(String tipo) {
		JFileChooser fileChooser = new JFileChooser();
		File keyFile;
		if (tipo.equals("privada")) fileChooser.setDialogTitle("Seleccionar clave privada");
		else if (tipo.equals("publica")) fileChooser.setDialogTitle("Seleccionar clave pública");
		if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
			keyFile = fileChooser.getSelectedFile();
			try {
				BufferedInputStream keystream = new BufferedInputStream(new FileInputStream(keyFile));
				int len = keystream.available();
				byte[] keyhex = new byte[len];
				keystream.read(keyhex, 0, len);
				keystream.close();
				return keyhex; 
			} 
			catch (Exception e) {
				e.printStackTrace();
			}	
		}
		return null;
	}
	
	public static void main(String[] args) {
		new Cifrin();
	}

}
