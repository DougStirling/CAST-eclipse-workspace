package exerciseBivarProg;

import java.awt.*;

import dataView.*;
import utils.*;
import exercise2.*;


public class LineariseTransformExternalApplet extends LineariseTransformApplet {
	
	private ExportDataButton exportButton;
	
//================================================
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("dataName", "string");
	}
	
	private String getDataName() {
		return "dataName";
	}
	
	
//-----------------------------------------------------------
	
	protected XPanel getWorkingPanels(DataSet data) {
		XPanel thePanel = new InsetPanel(60, 0);
		thePanel.setLayout(new BorderLayout(0, 0));
		
			XPanel topPanel = new XPanel();
			topPanel.setLayout(new VerticalLayout(VerticalLayout.FILL, VerticalLayout.VERT_TOP, 10));
			
				XPanel buttonPanel = new XPanel();
				buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
					String[] keys = {"x", "y"};
					exportButton = new ExportDataButton("Export Data", data, keys, null, this);
				buttonPanel.add(exportButton);
			topPanel.add(buttonPanel);
				
				yVarNameLabel = new XLabel("", XLabel.LEFT, this);
			topPanel.add(yVarNameLabel);
			
		thePanel.add("North", topPanel);

		thePanel.add("Center", getScatterPanel(data));
		
		return thePanel;
	}
	
	protected void setDisplayForQuestion() {
		super.setDisplayForQuestion();
		yVarNameLabel.setText("");
		xAxis.show(false);
		yAxis.show(false);
		theView.show(false);
		theView.setOpaque(false);

		exportButton.setDataName(getDataName());
		String keys[] = {getXKey(), getYKey()};
		exportButton.setKeys(keys);
	}
	
	protected void setDataForQuestion() {
		super.setDataForQuestion();
		
		NumVariable xVar = (NumVariable)data.getVariable(getXKey());
		xVar.name = getXVarName();
		xVar.setDecimals(4);
		
		NumVariable yVar = (NumVariable)data.getVariable(getYKey());
		yVar.name = getYVarName();
		yVar.setDecimals(4);
	}
	
	
//-----------------------------------------------------------
	
	protected int getMessageHeight() {
		return 120;
	}
	
	protected void giveFeedback() {
		super.giveFeedback();
		showScatterplot();
	}
	
	protected void showCorrectWorking() {
		super.showCorrectWorking();
		showScatterplot();
	}
	
	private void showScatterplot() {
		xAxis.show(true);
		yAxis.show(true);
		theView.show(true);
		theView.setOpaque(true);
		yVarNameLabel.setText(getYVarName());
	}
}