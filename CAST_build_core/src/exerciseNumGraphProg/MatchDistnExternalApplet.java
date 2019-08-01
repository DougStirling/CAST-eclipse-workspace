package exerciseNumGraphProg;

import java.awt.*;

import javax.swing.*;

import dataView.*;
import utils.*;
import exercise2.*;

import exerciseNumGraph.*;


public class MatchDistnExternalApplet extends MatchDistnApplet {
	
	private ExportDataButton exportDataButton;
	
//-----------------------------------------------------------
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("factorName", "string");
		registerParameter("groupNames", "string");
		registerParameter("dataName", "string");
	}
	
	protected String getFactorName() {
		return getStringParam("factorName");
	}
	
	protected String getDataName() {
		return getStringParam("dataName");
	}
	
	protected String[] getGroupNames() {
		String namesString = getStringParam("groupNames");
		if (namesString == null)
			return null;
		else
			return namesString.split("\\*");
	}
	
	
//-----------------------------------------------------------
	

	protected XPanel getWorkingPanels(DataSet data) {
		setFixedLeftOrder(true);
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new BorderLayout(0, 10));
		
			XPanel buttonPanel = new XPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
			
			exportDataButton = new ExportDataButton("Export data", data, this);
			buttonPanel.add(exportDataButton);
		thePanel.add("North", buttonPanel);
		
		thePanel.add("Center", super.getWorkingPanels(data));
		return thePanel;
	}
	
	protected void setWorkingPanelLayout(XPanel thePanel) {
		thePanel.setLayout(new ProportionLayout(0.5, 20));
	}
	
	protected MultipleDistnView createLeftDisplay(int[] leftOrder) {
		MultipleDistnView leftPlots = new MultipleDistnView(data, this, leftAxis, null, leftOrder, MultipleDistnView.DISTN_NAMES);
		leftPlots.setDrawGrid(false);
		leftPlots.setFixedOrder(true);
		leftAxis.show(false);
		return leftPlots;
	}
	
	protected int getRightDisplayType() {
		return hasOption("boxplot") ? MultipleDistnView.BOX_PLOT : hasOption("histogram") ? MultipleDistnView.HISTOGRAM
				: MultipleDistnView.STACKED_DOT_PLOT;
	}
	
	
//-----------------------------------------------------------
	
	protected void setDisplayForQuestion() {
		super.setDisplayForQuestion();
		
		String[] groupNames = getGroupNames();
		for (int i=0 ; i<groupNames.length ; i++)
			data.getVariable(yKeys[i]).name = groupNames[i];
		
		exportDataButton.setDataName(getDataName());
		exportDataButton.setKeys(yKeys);
		if (hasOption("stacked"))
			exportDataButton.setStackedNames(getVarName(), getFactorName());
	}
	
//-----------------------------------------------------------
	
	protected String leftDisplayString() {
		return "data set name";
	}
	
	protected String rightDisplayString() {
		return hasOption("boxplot") ? "box plot" : hasOption("histogram") ? "histogram" : "stacked dot plot";
	}
	
	protected int getMessageHeight() {
		return 100;
	}
	
}