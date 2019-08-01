package exercise2;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import dataView.*;
import utils.*;

public class ExportDataButton extends XButton {
	
	private DataSet data;
	private String dataName;
	private Variable[][] variables;
	private String factorName = null;
	private String variableName[];
	private String factorLevelName[] = null;
	private boolean permuted = false;
	private boolean outputIndex = true;
	
	public ExportDataButton(String label, DataSet data, XApplet applet) {		// must be followed by setKeys() and setDataName()
		super(label, applet);
		this.data = data;
	}
	
	public ExportDataButton(String label, DataSet data, String[][] keys, String dataName, XApplet applet) {
		super(label, applet);
		this.data = data;
		this.dataName = dataName;
		setKeys(keys);
	}
	
	public ExportDataButton(String label, DataSet data, String[] keys, String dataName, XApplet applet) {
		super(label, applet);
		this.data = data;
		this.dataName = dataName;
		setKeys(keys);
	}
	
	public ExportDataButton(String label, DataSet data, String key, String dataName, XApplet applet) {
		super(label, applet);
		this.data = data;
		this.dataName = dataName;
		setKey(key);
	}
	
	public void setKeys(String[][] keys) {
		variables = new Variable[keys.length][];
		for (int i=0 ; i<keys.length ; i++) {
			variables[i] = new Variable[keys[i].length];
			for (int j=0 ; j<keys[i].length ; j++) {
				variables[i][j] = (Variable)data.getVariable(keys[i][j]);
			}
		}
	}
	
	public void setKeys(String[] keys) {
		variables = new Variable[1][keys.length];
		for (int i=0 ; i<keys.length ; i++)
			variables[0][i] = (Variable)data.getVariable(keys[i]);
	}
	
	public void setKey(String key) {
		variables = new Variable[1][1];
		variables[0][0] = (Variable)data.getVariable(key);
	}
	
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	
	public void setStackedNames(String variableName, String factorName) {
		this.factorName = factorName;
		this.variableName = new String[1];
		this.variableName[0] = variableName;
	}
	
	public void setStackedNames(String[] variableName, String factorName) {
		this.factorName = factorName;
		this.variableName = variableName;
	}
	
	public void setFactorLevelNames(String[] factorLevelName) {
		this.factorLevelName = factorLevelName;
	}
	
	public void setPermuted() {
		permuted = true;
	}
	
	public void setOutputIndex(boolean outputIndex) {
		this.outputIndex = outputIndex;
	}
	
	public void actionPerformed(ActionEvent e) {
		File f = selectCsvFile();
		if (f != null) {
			try {
				PrintWriter writer = new PrintWriter(f, "UTF-8");
				if (variableName == null && factorName == null)
					writeUnstacked(writer);
				else
					writeStacked(writer);
				writer.close();
			} catch (IOException ex) {
				System.out.println("Could not save CSV file");
			}
		}
	}
	
	private void writeUnstacked(PrintWriter writer) {
		if (outputIndex)
			writer.print("index,");
		for (int i=0 ; i<variables.length ; i++)
			for (int j=0 ; j<variables[i].length ; j++) {
				if (i > 0 || j > 0)
					writer.print(",");
				writer.print(quoteCommas(variables[i][j].name));
			}
		writer.print("\n");
		
		int nValues = 0;
		for (int i=0 ; i<variables.length ; i++)
			for (int j=0 ; j<variables[i].length ; j++)
				nValues = Math.max(nValues, variables[i][j].noOfValues());
		
		int permutation[] = permuted ? ((ExerciseApplet)getApplet()).createPermutation(nValues) : null;
		for (int i=0 ; i<nValues ; i++) {
			if (outputIndex)
				writer.print((i + 1) + ",");
			int itemIndex = permuted ? permutation[i] : i;
			for (int j=0 ; j<variables.length ; j++)
				for (int k=0 ; k<variables[j].length ; k++) {
					if (j > 0 || k > 0)
						writer.print(",");
					Value v = variables[j][k].valueAt(itemIndex);
					if (v != null)
						writer.print(v.toString());
				}
			writer.print("\n");
		}
	}
	
	private void writeStacked(PrintWriter writer) {
		if (outputIndex)
			writer.print("index,");
		for (int i=0 ; i<variableName.length ; i++)
			writer.print(quoteCommas(variableName[i]) + ",");
		writer.println(quoteCommas(factorName));

		int nValues = 0;
		for (int i=0 ; i<variables.length ; i++)
			for (int j=0 ; j<variables[i].length ; j++)
				nValues = Math.max(nValues, variables[i][j].noOfValues());		//  maximum length of variables
		
		int permutation[] = permuted ? ((ExerciseApplet)getApplet()).createPermutation(nValues * variables[0].length) : null;
		
		int index = 0;
		for (int i=0 ; i<variables[0].length * nValues ; i++) {
			int itemIndex = permuted ? permutation[i] : i;
			int valueIndex = itemIndex % nValues;
			int groupIndex = itemIndex / nValues;
			boolean hasAllValues = true;
			for (int j=0 ; j<variables.length ; j++)
				if (valueIndex >= variables[j][groupIndex].noOfValues())
					hasAllValues = false;
			if (hasAllValues) {
				if (outputIndex)
					writer.print((++index) + ",");
				for (int j=0 ; j<variables.length ; j++) {
					Variable y = variables[j][groupIndex];
					Value yVal = y.valueAt(valueIndex);
					if (yVal != null)			//  some variables might not have nValues values
						writer.print(y.valueAt(valueIndex) + ",");
				}
				String factorLevel = (factorLevelName != null) ? factorLevelName[groupIndex] : variables[0][groupIndex].name;
				writer.println(factorLevel);
			}
		}
	}
	
	private String quoteCommas(String s) {
		if (s.contains(","))
			return "\"" + s + "\"";
		else
			return s;
	}
	

	private File selectCsvFile() {
		JFileChooser fileChooser = new JFileChooser() {
											public void approveSelection() {
												File f = getSelectedFile();
												if (f.exists()) {
													int result = JOptionPane.showConfirmDialog(this, "A file with this name already exists. Overwrite it?",
																												"Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
													switch (result) {
														case JOptionPane.YES_OPTION:
															super.approveSelection();
															return;
														case JOptionPane.CANCEL_OPTION:
															cancelSelection();
															return;
														case JOptionPane.NO_OPTION:
														case JOptionPane.CLOSED_OPTION:
														default:
															return;
													}
												}
												String fileName = f.getName();
												if (fileName.indexOf(".csv") != (fileName.length() - 4)) {
													JOptionPane.showMessageDialog(this, "Error!", "The file must have extension \".csv\".",
																																												JOptionPane.ERROR_MESSAGE);
													cancelSelection();
													return;
												}
												super.approveSelection();
											}
										};
		fileChooser.setDialogTitle("Specify a CSV file where the attempt will be saved.");
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		fileChooser.setSelectedFile(new File(dataName + ".csv"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("csv file","csv"));
		
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			return fileToSave;
		}
		return null;
	}
}