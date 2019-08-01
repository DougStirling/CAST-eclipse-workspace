package exerciseBivarProg;

import java.awt.*;

import javax.swing.BorderFactory;

import dataView.*;
import utils.*;
import exercise2.*;


public class InterpretScatterExternalApplet extends InterpretScatterApplet {
	
	private ExportDataButton stackedExportButton;
	
	private ExportDataButton exportButton[] = new ExportDataButton[4];
	
	
//-----------------------------------------------------------
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("dataName", "string");
		registerParameter("factorName", "string");
		registerParameter("factorLevelNames", "string");
	}
	
	private String getDataName() {
		return getStringParam("dataName");
	}
	
	private String getFactorName() {
		return getStringParam("factorName");
	}
	
	private String[] getFactorLevelNames() {
		String factorLevelString = getStringParam("factorLevelNames");
		String[] factorLevels = factorLevelString.split("\\*");
		return factorLevels;
	}
	
//-----------------------------------------------------------
	
	
	protected XPanel getWorkingPanels(DataSet data) {
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new BorderLayout(0, 0));
		
		if (hasOption("stacked")) {
			XPanel buttonPanel = new XPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
			stackedExportButton = new ExportDataButton("Export Data", data, this);
			buttonPanel.add(stackedExportButton);
			thePanel.add("North", buttonPanel);
		}
		
		thePanel.add("Center", super.getWorkingPanels(data));
		
		return thePanel;
	}
	
	
	protected XPanel getScatterPanel(DataSet data, int index) {
		String dataName = getDataName() + " for " + getFactorLevelNames()[index];
		
		XPanel exportPanel = new XPanel();
		exportPanel.setLayout(new VerticalLayout(VerticalLayout.CENTER, VerticalLayout.VERT_BOTTOM, 7));
			XLabel dataNameLabel = new XLabel(dataName, XLabel.LEFT, this);
			dataNameLabel.setFont(getBigBoldFont());
		exportPanel.add(dataNameLabel);
		
		if (!hasOption("stacked")) {
			exportButton[index] = new ExportDataButton("Export " + dataName, data, this);
			exportButton[index].setDataName(dataName);
			exportPanel.add(exportButton[index]);
		}
		return exportPanel;
	}
	
	protected void setDisplayForQuestion() {
		super.setDisplayForQuestion();
		
		if (hasOption("stacked")) {
			stackedExportButton.setDataName(getDataName());
			stackedExportButton.setFactorLevelNames(getFactorLevelNames());
			String variableNames[] = {data.getVariable(kXKeys[0]).name, data.getVariable(kYKeys[0]).name};
			stackedExportButton.setStackedNames(variableNames, getFactorName());
			String[][] exportKeys = new String[2][4];
			for (int i=0 ; i<4 ; i++) {
				int keyIndex = type[i];
				exportKeys[0][i] = kXKeys[keyIndex];
				exportKeys[1][i] = kYKeys[keyIndex];
			}
			stackedExportButton.setKeys(exportKeys);
		}
	}
	
	protected void setScatterplotForQuestion(int index) {
		if (!hasOption("stacked")) {
			int displayIndex = type[index];
			String[] exportKeys = new String[2];
			exportKeys[0] = kXKeys[displayIndex];
			exportKeys[1] = kYKeys[displayIndex];
			exportButton[index].setKeys(exportKeys);
		}
	}
	
	
	protected String getScatterplotsName() {
		return "data sets";
	}
	
	protected int getMessageHeight() {
		return hasOption("stacked") ? 170 : 120;
	}
}