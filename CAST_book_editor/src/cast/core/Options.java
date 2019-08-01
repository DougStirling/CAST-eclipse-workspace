package cast.core;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;
import org.xml.sax.*;

import cast.utils.*;


public class Options extends JDialog {
	static public boolean hasMultipleCollections;
	static public boolean isMasterCast;							//	Only true for when downloaded from GitHub
																								//	Must be true to allow editing of public e-books or sections
																								//	in public e-book folders.
																								//
																								//	Must be true to edit core variations of exercises.

	static public String kCastDownloadUrl = null;
	static public String kCastUploadServer = null;
	static public String kCastUploadPath = null;
	static public String kUploadServerType = null;
	
	static public String kCastInstallerUrl = null;
	static public String kHelpPath = null;
/*
	static private int kRelease = 0, kBeta = 1, kAlpha = 2;
	static private int kVersion = kRelease;
	
	static {
		if (kVersion == kRelease) {
			kCastInstallerUrl = "castAdmin2.massey.ac.nz/download";				//	for HTTP access
			kCastDownloadUrl = "cast.massey.ac.nz";												//	for HTTP access
			kCastUploadServer = "tur-www4.massey.ac.nz";			//	for FTP access
			kCastUploadPath = "public_html/CAST";					//	for FTP access
		} else if (kVersion == kBeta) {
			kCastInstallerUrl = "castAdmin2.massey.ac.nz/download_beta";				//	for HTTP access
			kCastDownloadUrl = "castBeta.massey.ac.nz";												//	for HTTP access
			kCastUploadServer = "tur-www4.massey.ac.nz";			//	for FTP access
			kCastUploadPath = "public_html/CASTbeta";			//	for FTP access
		} else if (kVersion == kAlpha) {
			kCastInstallerUrl = "castAdmin2.massey.ac.nz/download_alpha";			//	for HTTP access
			kCastDownloadUrl = "tur-www4.massey.ac.nz/~wwdstirl/CASTalpha";		//	for HTTP access
			kCastUploadServer = "tur-www4.massey.ac.nz";				//	for FTP access
			kCastUploadPath = "public_html/CASTalpha";			//	for FTP access
		}
		kHelpPath = "castAdmin2.massey.ac.nz/help";
	}
*/
	
	static public void initialise(File castDir) {
		File coreDir = new File(castDir, "core");
		File settingsFile = new File(coreDir, "structure/servers.xml");
		
		hasMultipleCollections = hasMultipleCollections(castDir);
		
		File versionsFile = new File(castDir, "versions.text");
		isMasterCast = versionsFile.exists();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(true);
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.setErrorHandler(new ErrorHandler() {
													public void warning(SAXParseException exception) throws SAXException {
													}
													public void error(SAXParseException exception) throws SAXException {
													}
													public void fatalError(SAXParseException exception) throws SAXException {
													}
												} );
			Document settingsDomDocument = db.parse(settingsFile);
			
			Element documentElement = settingsDomDocument.getDocumentElement();
			
				Element castElement = getSelectedElement("serverPaths", "serverPath", documentElement);
			kCastDownloadUrl = castElement.getAttribute("httpAddress");
			kCastUploadServer = castElement.getAttribute("ftpServer");
			kCastUploadPath = castElement.getAttribute("ftpPath");
			kUploadServerType = castElement.getAttribute("type");
			
				Element installerElement = getSelectedElement("installersPaths", "path", documentElement);
			kCastInstallerUrl = installerElement.getAttribute("address");
				Element helpElement = getSelectedElement("helpPaths", "path", documentElement);
			kHelpPath = helpElement.getAttribute("address");
			
		} catch(ParserConfigurationException e) {
			System.err.println("Error opening settings file.\n" + e);
		} catch(IOException e) {
			System.err.println("Error opening settings file.\n" + e);
		} catch(SAXException e) {
			System.err.println("Error opening settings file.\n" + e);
		}
	}
	
	static private File[] findCollections(File castDir) {
		return castDir.listFiles( new FilenameFilter() {
																					public boolean accept(File dir, String name) {
																						return name.startsWith("collection_");
																					}
																			});
	}
	
	static public boolean hasMultipleCollections(File castDir) {
		File[] collectionFile = findCollections(castDir);
		return collectionFile.length > 1;
	}
	
	static private Element getSelectedElement(String pathType, String pathElementType, Element domElement) {
		NodeList nl = domElement.getElementsByTagName(pathType);
		Element pathElement = null;
		for (int i=0 ; i<nl.getLength() ; i++)
			if (nl.item(i).getNodeType() == Node.ELEMENT_NODE)
				pathElement = (Element)nl.item(i);
		
		if (pathElement != null) {		//		there should always be at least one tag of this type
			NodeList pathList = pathElement.getElementsByTagName(pathElementType);
			for (int i=0 ; i<pathList.getLength() ; i++) {
				Node n = pathList.item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element)n;
					boolean selected = Boolean.valueOf(e.getAttribute("selected"));
					if (selected)
						return e;
				}
			}
		}
		return null;		//		should never happen since one path should be selected
	}
	
	static public void editSettings(File coreDir) {
		Options settingsWindow = new Options(coreDir);
		settingsWindow.setVisible(true);
	}
	
	private File settingsFile;
	private Document settingsDomDocument;
	private Element[] servers;
	private Element[] installerPaths;
	private Element[] helpPaths;
	
	private JButton saveButton;
	
	public Options(File coreDir) {
		super((Frame)null, "External addresses", true);
		
		readPaths(coreDir);
		
		setLayout(new BorderLayout(0, 0));
			JTextArea instructions = new JTextArea("The settings in this dialog box can be used to change the external servers that CAST uses.\n");
			instructions.setLineWrap(true);
			instructions.setWrapStyleWord(true);
			instructions.setEditable(false);
			Border emptyBorder = BorderFactory.createEmptyBorder(5, 10, 5, 10);
			Border underlineBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black);
			instructions.setBorder(BorderFactory.createCompoundBorder(underlineBorder, emptyBorder));
		add("North", instructions);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setOpaque(true);
		bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			saveButton = new JButton("Save");
			saveButton.addActionListener(new ActionListener() {
													public void actionPerformed(ActionEvent e) {
														savePaths();
														saveButton.setEnabled(false);
													}
											});
			saveButton.setEnabled(false);
		bottomPanel.add(saveButton);
		Border overlineBorder = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black);
		bottomPanel.setBorder(BorderFactory.createCompoundBorder(overlineBorder, emptyBorder));
	add("South", bottomPanel);
		
			JPanel mainPanel = new JPanel();
			mainPanel.setOpaque(true);
			mainPanel.setLayout(new VerticalLayout(VerticalLayout.FILL, VerticalLayout.VERT_SPACED, 5));
			mainPanel.add(settingPanel(servers, "httpAddress", "type", "This choice affects the server from which CAST will look for updates. Administrators can also upload to this server.", "Update server:", saveButton));
			mainPanel.add(settingPanel(helpPaths, "address", null, "This choice determines where CAST will look for some HTML help files.", "Help files:", saveButton));
			if (hasMultipleCollections) {
				mainPanel.add(settingPanel(installerPaths, "address", null, "This choice affects where the web version of CAST will look for installers."
						+ " It will only be used after CAST the server version of CAST is updated.", "Installer location:", saveButton));
			}
			
		add("Center", mainPanel);
				
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
										public void windowClosing(WindowEvent e) {
											if (saveButton.isEnabled()) {
												Object[] options = {"Save", "Ignore changes", "Cancel"};
												int result = JOptionPane.showOptionDialog(Options.this, "You have changed the settings. Do you want to save them?", "Save settings?",
																									JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
												switch(result){
													case JOptionPane.CANCEL_OPTION:
														return;
													case JOptionPane.YES_OPTION:
														savePaths();
													case JOptionPane.NO_OPTION:
														;
												}
											}
											dispose();
										}
									});
		pack();
		
//		selectPath(installerPaths, 0);
//		saveButton.setEnabled(true);
	}
	
	private JPanel settingPanel(final Element[] paths, String addressKey, String typeKey, String choiceDescription, String choiceLabel, final JButton saveButton) {
		JPanel thePanel = new JPanel();
		thePanel.setLayout(new BorderLayout(0, 0));
		
			JLabel description = new JLabel(choiceDescription, JLabel.LEFT);
		thePanel.add("North", description);
		
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
				JLabel label = new JLabel(choiceLabel, JLabel.LEFT);
			mainPanel.add(label);
				final JComboBox choice = new JComboBox();
				int selectedItem = 0;
				for (int i=0 ; i<paths.length ; i++) {
					String path = paths[i].getAttribute(addressKey);
					if (typeKey != null)
						try {
							String versionType = paths[i].getAttribute(typeKey);
							String server = "http://" + path + "/" + "versions.text";
							URL versionUrl = new URL(server);
							BufferedReader in = new BufferedReader(new InputStreamReader(versionUrl.openStream()));

							String versionString = in.readLine();
							int versionNo = Integer.valueOf(versionString);
							int v1 = versionNo / 1000000;
							int v2 = (versionNo % 1000000) / 10000;
							int v3 = versionNo % 1000;
							String versionNumberString = v1 + "." + v2;
							if (v3 > 0)
								versionNumberString += "." + v3;
							path += " (" + versionType + " " + versionNumberString + ")";
							in.close();
						} catch (MalformedURLException e) {
							System.err.println("MalformedURLException " + e);
						} catch (IOException e) {
							System.err.println("IOException " + e);
						}
					choice.addItem(path);
					if (Boolean.valueOf(paths[i].getAttribute("selected")))
						selectedItem = i;
				}
				choice.addActionListener(new ActionListener() {
														public void actionPerformed(ActionEvent e) {
															selectPath(paths, choice.getSelectedIndex());
															saveButton.setEnabled(true);
														}
												});
				choice.setSelectedIndex(selectedItem);
			mainPanel.add(choice);
		thePanel.add("Center", mainPanel);
		thePanel.setPreferredSize(new Dimension(500, 110));
		return thePanel;
	}
	
	private void readPaths(File coreDir) {
		settingsFile = new File(coreDir, "structure/servers.xml");
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(true);
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.setErrorHandler(new ErrorHandler() {
													public void warning(SAXParseException exception) throws SAXException {
													}
													public void error(SAXParseException exception) throws SAXException {
													}
													public void fatalError(SAXParseException exception) throws SAXException {
													}
												} );
			settingsDomDocument = db.parse(settingsFile);
			
			Element documentElement = settingsDomDocument.getDocumentElement();
			
			servers = readPaths("serverPaths", "serverPath", documentElement);
			installerPaths = readPaths("installersPaths", "path", documentElement);
			helpPaths = readPaths("helpPaths", "path", documentElement);
			
		} catch(ParserConfigurationException e) {
			System.err.println("Error opening settings file.\n" + e);
      e.printStackTrace();
		} catch(IOException e) {
			System.err.println("Error opening settings file.\n" + e);
      e.printStackTrace();
		} catch(SAXException e) {
			System.err.println("Error opening settings file.\n" + e);
      e.printStackTrace();
		}
	}
	
	private Element[] readPaths(String pathType, String pathElementType, Element domElement) {
		Element pathElement = XmlHelper.getUniqueTag(domElement, pathType);

		NodeList pathList = pathElement.getElementsByTagName(pathElementType);
		int nPaths = 0;
		for (int i=0 ; i<pathList.getLength() ; i++) {
			if (pathList.item(i).getNodeType() == Node.ELEMENT_NODE)
				nPaths ++;
		}
		
		Element[] paths = new Element[nPaths];
		nPaths = 0;
		for (int i=0 ; i<pathList.getLength() ; i++) {
			Node n = pathList.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE)
				paths[nPaths ++] = (Element)n;
		}
		return paths;
	}
	
	private void selectPath(Element[] paths, int index) {
		for (int i=0 ; i<paths.length ; i++) {
	    Node value = paths[i].getAttributes().getNamedItem("selected");
	    value.setNodeValue(i == index ? "true" : "false");
		}
	}
	
	private Element getSelectedElement(Element[] elements) {
		for (int i=0 ; i<elements.length ; i++) {
			String selectedString = elements[i].getAttribute("selected");
			if (Boolean.valueOf(selectedString))
				return elements[i];
		}
		return null;		//		should never happen
	}
	
	private void savePaths() {
		Element selectedServer = getSelectedElement(servers);
		kCastDownloadUrl = selectedServer.getAttribute("httpAddress");
		kCastUploadServer = selectedServer.getAttribute("ftpServer");
		kCastUploadPath = selectedServer.getAttribute("ftpPath");
		kUploadServerType = selectedServer.getAttribute("type");

		Element selectedInstallerPath = getSelectedElement(installerPaths);
		kCastInstallerUrl = selectedInstallerPath.getAttribute("address");

		Element selectedHelpPath = getSelectedElement(helpPaths);
		kCastInstallerUrl = selectedHelpPath.getAttribute("address");
		
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
      
			DocumentType doctype = settingsDomDocument.getDoctype();
			if (doctype != null)
				transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());

			DOMSource source = new DOMSource(settingsDomDocument);
			File tempOutputFile = new File(settingsFile.getParentFile(), "tempSettings.xml");
			StreamResult result = new StreamResult(tempOutputFile.getPath());
			transformer.transform(source, result);
			
			settingsFile.delete();
			tempOutputFile.renameTo(settingsFile);
		} catch (TransformerConfigurationException e) {
			System.err.println("Error saving settings file.\n" + e);
      e.printStackTrace();
		} catch (TransformerException e) {
			System.err.println("Error saving settings file.\n" + e);
      e.printStackTrace();
		}
	} 
}