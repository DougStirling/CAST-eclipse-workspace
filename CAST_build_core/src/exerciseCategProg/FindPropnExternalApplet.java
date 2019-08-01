package exerciseCategProg;

import java.awt.*;

import javax.swing.BorderFactory;

import dataView.*;
import exercise2.ExportDataButton;
import utils.*;


public class FindPropnExternalApplet extends FindPropnApplet {
	
	private ExportDataButton exportDataButton;
	
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();

		registerParameter("dataName", "string");
	}
	
	private String getDataName() {
		return getStringParam("dataName");
	}
	
//-----------------------------------------------------------
	
	protected void createDisplay() {
		setLayout(new BorderLayout(0, 10));
		
			XPanel topPanel = new XPanel();
			topPanel.setLayout(new BorderLayout(0, 10));
			topPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
			
				questionPanel = new QuestionPanel(this);
			topPanel.add("North", questionPanel);
			
			topPanel.add("Center", getWorkingPanels(data));
		
		add("North", topPanel);
		
		add("Center", getBottomPanel());
	}
	
//-----------------------------------------------------------
	
	
	protected XPanel getWorkingPanels(DataSet data) {
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new BorderLayout());
		
			XPanel buttonPanel = new XPanel();
			buttonPanel.setLayout(new VerticalLayout(VerticalLayout.CENTER, VerticalLayout.VERT_CENTER, 0));

			exportDataButton = new ExportDataButton("Export data", data, "y", "", this);
			buttonPanel.add(exportDataButton);
				
		thePanel.add("Center", buttonPanel);
		
		thePanel.add("East", getPropnTemplatePanel());
		
		return thePanel;
	}
	
	
	protected void setDisplayForQuestion() {
		super.setDisplayForQuestion();
		exportDataButton.setDataName(getDataName());
	}
		
//-----------------------------------------------------------
	
	protected String getCategoriesString() {
		return "categories";
	}
	
	protected boolean usesCumulative() {
		return false;
	}
		
	protected void selectCorrectCounts() {
	}
	
}