package exerciseBivarProg;

import java.awt.*;

import javax.swing.BorderFactory;

import dataView.*;
import utils.*;
import exercise2.*;


public class LsProblemsExternalApplet extends LsProblemsApplet {
	
	private ExportDataButton exportButton;
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("dataName", "string");
		registerParameter("xDecimals", "int");
		registerParameter("yDecimals", "int");
	}
	
	protected String getDataName() {
		return getStringParam("dataName");
	}
	
	protected int getXDecimals() {
		return getIntParam("xDecimals");
	}
	
	protected int getYDecimals() {
		return getIntParam("yDecimals");
	}
	
//-----------------------------------------------------------
	
	protected XPanel getWorkingPanels(DataSet data) {
		XPanel workingPanel = new XPanel();
		workingPanel.setLayout(new BorderLayout(0, 0));
		
			XPanel topPanel = new XPanel();
			topPanel.setLayout(new VerticalLayout(VerticalLayout.FILL));
		
				XPanel buttonPanel = new XPanel();
				buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
				buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 12, 0));
		
					String[] keys = {"x", "y"};
					exportButton = new ExportDataButton("Export Data", data, keys, null, this);
				buttonPanel.add(exportButton);
				
			topPanel.add(buttonPanel);
			
			topPanel.add(getCheckboxPanel());
		
			workingPanel.add("North", topPanel);
		
		return workingPanel;
	}
	
	protected void setDisplayForQuestion() {
		super.setDisplayForQuestion();
		exportButton.setDataName(getDataName());
	}
	
	protected void setScatterplotForQuestion() {
	}
	
	
	protected void setDataForQuestion() {
		super.setDataForQuestion();
		NumVariable xVar = (NumVariable)data.getVariable("x");
		xVar.setDecimals(getXDecimals());
		NumVariable yVar = (NumVariable)data.getVariable("y");
		yVar.setDecimals(getYDecimals());
	}
	
	
//-----------------------------------------------------------

	
	protected void showScatterplot(MessagePanel messagePanel) {
		XPanel theGraph = getScatterPanel(data);
		theGraph.remove(yNameLabel);
		theGraph.setOpaque(false);
		xAxis.setOpaque(false);
		yAxis.setOpaque(false);
		updateScatterplotInfo();
		
		messagePanel.insertText("\n                     " + getYVarName() + "\n");
		messagePanel.insertGraph(theGraph);
	}
	
	protected int getMessageHeight() {
		return 370;
	}
	
}