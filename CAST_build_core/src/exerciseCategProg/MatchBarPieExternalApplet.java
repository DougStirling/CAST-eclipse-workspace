package exerciseCategProg;

import java.awt.*;

import javax.swing.BorderFactory;

import dataView.*;
import axis.*;
import utils.*;
import exercise2.*;

import exerciseCateg.*;
import exerciseNumGraph.MultipleDistnView;


public class MatchBarPieExternalApplet extends MatchBarPieApplet {
	
	private ExportDataButton exportDataButton;
	
//-----------------------------------------------------------
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("dataName", "string");
		registerParameter("groupNames", "string");
		registerParameter("variableName", "string");
		registerParameter("factorName", "string");
		registerParameter("groups", "boolean");
	}

	private String getDataName() {
		return getStringParam("dataName");
	}

	private String[] getGroupNames() {
		return getStringParam("groupNames").split("\\*");
	}
	
	private String getVariableName() {
		return getStringParam("variableName");
	}
	
	private String getFactorName() {
		return getStringParam("factorName");
	}
	
	public boolean hasOption(String s) {
		if (s.equals("stacked"))				//		can be different for different variations
			return getBooleanParam("groups");
		else
			return super.hasOption(s);
	}
	
	
//-----------------------------------------------------------
	

	protected XPanel getWorkingPanels(DataSet data) {
		setFixedLeftOrder(true);
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new BorderLayout(0, 10));
		
			XPanel buttonPanel = new XPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
			
			exportDataButton = new ExportDataButton("Export data", data, kYKeys, "", this);
			buttonPanel.add(exportDataButton);
		thePanel.add("North", buttonPanel);
		
		thePanel.add("Center", super.getWorkingPanels(data));
		return thePanel;
	}
	
	
	protected void setWorkingPanelLayout(XPanel thePanel) {
		thePanel.setLayout(new ProportionLayout(0.5, 20));
	}
	
	protected XPanel addLeftItems(XPanel thePanel, int[] leftOrder) {
		XPanel namePanel = new XPanel();
		namePanel.setLayout(new BorderLayout(0, 0));
		
			leftCharts = new MultipleDistnView(data, this, null, kYKeys, leftOrder, MultipleDistnView.DISTN_NAMES);
			((MultipleDistnView)leftCharts).setDrawGrid(false);
			leftCharts.setFixedOrder(true);
		
		namePanel.add("Center", leftCharts);
		namePanel.add("South", new Separator(0.0, 10));		//  actually of height 2 * 10 + 2 = 22
			
		thePanel.add(ProportionLayout.LEFT, namePanel);
		
		return leftCharts;
	}
	
	protected XPanel addRightItems(XPanel thePanel, int[] rightOrder) {
		if (hasOption("pie")) {
			rightCharts = new MultipleBarPieView(data, this, null, kYKeys, rightOrder,
																	MultipleBarPieView.PIE_CHART, pieDrawer);
			
			thePanel.add(ProportionLayout.RIGHT, rightCharts);
		}
		else {
			XPanel barPanel = new XPanel();
			barPanel.setLayout(new AxisLayout());
			
				catAxis = new HorizAxis(this);
			barPanel.add("Bottom", catAxis);
			
				rightCharts = new MultipleBarPieView(data, this, catAxis, kYKeys, rightOrder,
																								MultipleBarPieView.BAR_CHART, pieDrawer);
				rightCharts.lockBackground(Color.white);
			barPanel.add("Center", rightCharts);

			thePanel.add(ProportionLayout.RIGHT, barPanel);
		}
		registerStatusItem("chartPerm", rightCharts);
		
		return rightCharts;
	}
	
	protected void setDisplayForQuestion() {
		super.setDisplayForQuestion();
		
		exportDataButton.setDataName(getDataName());
		if (hasOption("stacked"))
			exportDataButton.setStackedNames(getVariableName(), getFactorName());
		if (hasOption("permuted"))
			exportDataButton.setPermuted();
	}
	
	protected void setDataForQuestion() {
		super.setDataForQuestion();
		
		String groupNames[] = getGroupNames();
		for (int i=0 ; i<kNDisplayedDistns ; i++)
			data.getVariable(kYKeys[i]).name = groupNames[i];
	}
	
	
//-----------------------------------------------------------
	
	protected void insertMessageContent(MessagePanel messagePanel) {
		String chartName = hasOption("pie") ? "pie charts" : "bar charts";
		switch (result) {
			case ANS_UNCHECKED:
				messagePanel.insertText("Reorder the " + chartName +  " by dragging so that each matches the name of the corresponding data set on its left.");
				break;
			case ANS_TOLD:
				messagePanel.insertRedHeading("Answer\n");
				messagePanel.insertText("The correct matching of " + chartName +  " is shown.");
				break;
			case ANS_CORRECT:
				messagePanel.insertRedHeading("Good!\n");
				messagePanel.insertText("You have correctly matched up the " + chartName +  " of the four data sets with their names.");
				break;
			case ANS_WRONG:
				messagePanel.insertRedHeading("Wrong!\n");
				messagePanel.insertText("The red arrows indicate " + chartName +  " that do not describe the data sets whose names are on their left.");
				break;
		}
	}
	
	protected int getMessageHeight() {
		return 120;
	}
	
}