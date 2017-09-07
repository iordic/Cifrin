package main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

/**
 * Clase que contiene la ventana principal
 * @author Jordi Castelló
 *
 */
public class MainWindow extends JFrame {
	
	static final long serialVersionUID = -4761970858383259972L;
	JMenuBar menuBar;
	JMenu fileMenu, keyMenu;
	JMenuItem openMenuItem, saveMenuItem, saveAsMenuItem, closeMenuItem;
	JMenuItem desMenuItem, tripleDesMenuItem, aesMenuItem, rsaMenuItem;
	JLabel functionTitle, operationTitle;
	JComboBox<String> functionList, operationList;
	JTextArea text;
	JScrollPane jScrollPane;
	JButton operationButton;
	
	public MainWindow() {
		menuBar = new JMenuBar();
		fileMenu = new JMenu("Fichero");
		keyMenu = new JMenu("Claves");
		functionTitle = new JLabel();
		operationTitle = new JLabel();
		functionList = new JComboBox<String>();
		operationList = new JComboBox<String>();
		text = new JTextArea();
		operationButton = new JButton("Cifrar");
		jScrollPane = new JScrollPane(text);	
		// Crea los elementos del menú "fichero"
		openMenuItem = new JMenuItem("Abrir");
		saveMenuItem = new JMenuItem("Guardar");
		saveAsMenuItem = new JMenuItem("Guardar como...");
		closeMenuItem = new JMenuItem("Cerrar y/o limpiar");
		// Añade elementos al menú "fichero"
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);
		fileMenu.add(closeMenuItem);
		// Crea los elementos del menú "claves"
		desMenuItem = new JMenuItem("Generar clave DES");
		tripleDesMenuItem = new JMenuItem("Generar clave Triple DES");
		aesMenuItem = new JMenuItem("Generar clave AES");
		rsaMenuItem = new JMenuItem("Generar par de claves RSA");
		// Añade elementos al menú "claves"
		keyMenu.add(desMenuItem);
		keyMenu.add(tripleDesMenuItem);
		keyMenu.add(aesMenuItem);
		keyMenu.add(rsaMenuItem);
		// Añade elementos a la lista de funciones (mismo orden que las constantes)
		functionList.addItem("DES");
		functionList.addItem("Triple DES");
		functionList.addItem("AES");
		functionList.addItem("HASH");
		functionList.addItem("RSA");		
		generateSublist();
		// Establece propiedades de los objetos
		menuBar.add(fileMenu);
		menuBar.add(keyMenu);		
		functionTitle.setText("Algoritmo:");
		operationTitle.setText("Operación:");
		text.setLineWrap(true);
        text.setRows(5);
        text.setWrapStyleWord(true);
        text.setEditable(true);
        // Cambiemos la fuente por defecto (era muy fea y pequeña) ;D
        Font textFont = new Font("SansSerif", Font.PLAIN, 16);
        text.setFont(textFont);
		jScrollPane = new JScrollPane(text);
		jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		// Posiciona (posición absoluta en X e Y)
		setLayout(null);
		menuBar.setBounds(0, 0, 800, 30);
		functionTitle.setBounds(20, 40, 100, 20);
		operationTitle.setBounds(140, 40, 100, 20);
		functionList.setBounds(20, 60, 100, 20);
		operationList.setBounds(140, 60, 200, 20);
		jScrollPane.setBounds(20, 100, 750, 400);
		operationButton.setBounds(20, 510, 100, 30);
		// Añade elementos
		add(menuBar);
		add(functionTitle);
		add(operationTitle);
		add(operationList);
		add(functionList);
		add(jScrollPane);
		add(operationButton);
		// Propiedades de la ventana
		setTitle("Cifrín");
		setSize(800, 600);
		centerFrame();
		setVisible(true);
		this.setMinimumSize(getSize());
		addComponentListener(new ComponentListener() {	
			// Redimensiona el area de texto y la barra de menú cuando redimensionamos la ventana.
			// También reubica el botón de debajo.
		    public void componentResized(ComponentEvent e) {
		    	Rectangle r = getBounds();
		    	int height = r.height;
		    	int width = r.width;
		        jScrollPane.setSize(width - 60, height - 220);
		        text.setSize(jScrollPane.getSize());  // Necesario para que se redimensione bien el textarea
		        menuBar.setSize(width, 30);
		        // Coloca el botón
		        r = jScrollPane.getBounds();
		        height = r.height + r.y;
		        width = r.width;
		        operationButton.setBounds(20, height + 20, 100, 30);
		    }
			@Override
			public void componentHidden(ComponentEvent e) {}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentShown(ComponentEvent e) {}
		});
		// Finalizar el programa si lo cerramos
		// (en caso de no hacerlo, dejaremos java ejecutando en segundo plano)
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	/**
	 * Centrar la ventana en la pantalla. 
	 */
	private void centerFrame() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();	// Tamaño de la pantalla en px
        Dimension window = getSize();										// Tamaño de la ventana en px     
        setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2); // Centra la ventana
	}
	
	/**
	 * Actualiza la lista de operaciones que se pueden realizar según el algoritmo que 
	 * hayamos seleccionado en la primera lista.
	 */
	public void generateSublist() {
		operationList.removeAllItems();
		int selection = functionList.getSelectedIndex();
		switch(selection) {
		case Constants.DES:
			operationList.addItem("Cifrar texto");
			operationList.addItem("Descifrar texto");
			break;
		case Constants.TRIPLE_DES:
			operationList.addItem("Cifrar texto");
			operationList.addItem("Descifrar texto");
			break;
		case Constants.AES:
			operationList.addItem("Cifrar texto");
			operationList.addItem("Descifrar texto");
			break;
		case Constants.HASH:
			operationList.addItem("MD5");
			operationList.addItem("SHA1");
			operationList.addItem("SHA256");
			operationList.addItem("SHA512");
			break;
		case Constants.RSA:
			operationList.addItem("Cifrar texto");
			operationList.addItem("Descifrar texto");
		}	
		updateButtonLabel();
	}
	
	/**
	 * Cambia la etiqueta del botón  según la operación que hayamos seleccionado
	 */
	public void updateButtonLabel() {
		int selectedFunction = functionList.getSelectedIndex();
		int selectedOperation = operationList.getSelectedIndex();
		switch(selectedFunction) {
		case Constants.DES:
			if(selectedOperation == Constants.CIPHER_OPERATION) {
				operationButton.setText("Cifrar");
			}
			else if(selectedOperation == Constants.DECIPHER_OPERATION) {
				operationButton.setText("Descifrar");
			}	
			break;
		case Constants.TRIPLE_DES:
			if(selectedOperation == Constants.CIPHER_OPERATION) {
				operationButton.setText("Cifrar");
			}
			else if(selectedOperation == Constants.DECIPHER_OPERATION) {
				operationButton.setText("Descifrar");
			}	
			break;
		case Constants.AES:
			if(selectedOperation == Constants.CIPHER_OPERATION) {
				operationButton.setText("Cifrar");
			}
			else if(selectedOperation == Constants.DECIPHER_OPERATION) {
				operationButton.setText("Descifrar");
			}	
			break;
		case Constants.HASH:
			operationButton.setText("Generar");
			break;	
		case Constants.RSA:
			if(selectedOperation == Constants.CIPHER_OPERATION) {
				operationButton.setText("Cifrar");
			}
			else if(selectedOperation == Constants.DECIPHER_OPERATION) {
				operationButton.setText("Descifrar");
			}
		}		
	}
}
