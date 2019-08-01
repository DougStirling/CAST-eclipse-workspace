package exerciseCategProg;

import java.awt.*;

import javax.swing.*;

import dataView.*;
import utils.*;
import exercise2.*;



public class FindContinPropnExternalApplet extends FindContinPropnApplet {
	
	static final private String kKeys[] = {"x", "y"}; 
	
	private ExportDataButton exportDataButton;
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("dataName", "string");
		registerParameter("requiredDecimals", "int");
	}
	
	private String getDataName() {
		return getStringParam("dataName");
	}
	
	protected int getRequiredDecimals() {
		return getIntParam("requiredDecimals");
	}

//-----------------------------------------------------------
	
	protected XPanel getWorkingPanels(DataSet data) {
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new VerticalLayout(VerticalLayout.CENTER, VerticalLayout.VERT_CENTER, 0));
		
			XPanel buttonPanel = new XPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
			
			exportDataButton = new ExportDataButton("Export data", data, kKeys, "", this);
			exportDataButton.setPermuted();
			buttonPanel.add(exportDataButton);
		thePanel.add(buttonPanel);
		
		thePanel.add(super.getWorkingPanels(data));
		
		return thePanel;
	}
	
	protected void setDisplayForQuestion() {
		super.setDisplayForQuestion();
		
		exportDataButton.setDataName(getDataName());
		
		showTable(false);
	}
	
	private void showTable(boolean showNotHide) {
		theTable.show(showNotHide);
		xNameLabel.show(showNotHide);
		yNameLabel.show(showNotHide);
	}
	
//-----------------------------------------------------------

	protected void insertSolution(MessagePanel messagePanel) {
		super.insertSolution(messagePanel);
		
		showTable(true);
	}
	
	protected void showCorrectWorking() {
		super.showCorrectWorking();
		showTable(true);
	}
	
}