package exerciseNumGraphProg;

import java.awt.*;

import dataView.*;
import utils.*;
import exercise2.*;


public class InterpretShapeExternalApplet extends InterpretShapeApplet {
	
	private ExportDataButton exportButton;
	
//================================================
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("dataName", "string");
	}
	
	private String getDataName() {
		return getStringParam("dataName");
	}
	
	
//-----------------------------------------------------------
	
	protected XPanel getDataDisplayPanel() {
		XPanel buttonPanel = new XPanel();
		buttonPanel.setLayout(new VerticalLayout(VerticalLayout.CENTER, VerticalLayout.VERT_CENTER));
		
			String[] nullKeys = new String[0];
			exportButton = new ExportDataButton("Export Data", data, nullKeys, "", this);
		buttonPanel.add(exportButton);
		return buttonPanel;
	}
	
	protected void updateDataDisplayPanel() {
		String yKey = kShapeString[getShapeCode()];
		exportButton.setKey(yKey);
		exportButton.setDataName(getDataName());
	}
	
	
//-----------------------------------------------------------
	
	protected void addPlotToMessage(MessagePanel messagePanel) {
		XPanel plot = createPlot();
		updatePlotVariable();
		plot.setOpaque(false);
		theAxis.setOpaque(false);
		plot.setPreferredSize(new Dimension(getSize().width - 50, 150));
		messagePanel.insertGraph(plot);
		messagePanel.insertText("\n");
	}
	
	protected int getMessageHeight() {
		return 260;
	}
}