package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JTextArea;

/**
 * Clase para el manejo de ficheros desde la ventana.
 * @author Jordi Castelló
 *
 */
public class FileHandler {
	private File file;
	
	/**
	 * Selecciona un fichero y lo carga en el cuadro de texto.
	 * @param textArea cuadro de texto donde se carga el contenido.
	 */
	public void selectFile(JTextArea textArea) {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
			// Comprobamos que haya un fichero cargado y que no tenga el mismo nombre que el que queremos abrir.
			if (file != null && !file.getName().equals(fileChooser.getSelectedFile().getName())) {
				file = fileChooser.getSelectedFile();	
			}
			else {
				file = fileChooser.getSelectedFile();
			}
			try {	// Leer el contenido del fichero y cargarlo en el cuadro de texto
				BufferedReader in = new BufferedReader(new FileReader(file));
				try {
					textArea.setText(null);
					String line = in.readLine();
					while(line != null) {
						textArea.append(line + "\n");
						line = in.readLine();
					}
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Cierra el fichero y vacia el cuadro de texto.
	 * @param textArea cuadro de texto a limpiar
	 */
	public void closeFile(JTextArea textArea) {
		if(file != null) {
			file = null;
			textArea.setText(null);
		}
	}
	
	/**
	 * Coge el texto de un cuadro de texto y lo guarda en un fichero plano,
	 * el que previamente hemos seleccionado, lo sobreescribe.
	 * @param textArea cuadro de texto de donde cargar el texto
	 * @throws IOException en caso de que no pueda abrir el fichero
	 */
	public void saveFile(JTextArea textArea) throws IOException {
		if(file != null) {	// Si tenemos el fichero establecido
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			textArea.write(writer);
			writer.close();
		}
		else {
			saveFileAs(textArea);
		}
	}
	
	/**
	 * Carga el texto del cuadro y lo guarda en un fichero plano, en la ruta que seleccionemos
	 * @param textArea cuadro de donde cargar el texto
	 * @throws IOException en caso de no poder crear el fichero
	 */
	public void saveFileAs(JTextArea textArea) throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			textArea.write(writer);		// Escribir lo del cuadro en el fichero
			writer.close();
		}
	}
	/**
	 * Devuelve el fichero que tenemos seleccionado
	 */
	public File getFile() {
		return file;
	}
}
