package exerciseTimeProg;

import java.awt.*;

import javax.swing.BorderFactory;

import dataView.*;
import exercise2.*;


public class TimeComponentsExternalApplet extends TimeComponentsApplet {

	static final private LabelValue[] kQuarterNames = {new LabelValue("Q1"), new LabelValue("Q2"), new LabelValue("Q3"), new LabelValue("Q4")};
	static final private LabelValue[] kMonthNames = {new LabelValue("Jan"), new LabelValue("Feb"), new LabelValue("Mar"), new LabelValue("Apr"), new LabelValue("May"), new LabelValue("Jun"), new LabelValue("Jul"), new LabelValue("Aug"), new LabelValue("Sep"), new LabelValue("Oct"), new LabelValue("Nov"), new LabelValue("Dec")};
	
	private ExportDataButton exportDataButton;
	
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();

		registerParameter("dataName", "string");
	}
	
	private String getDataName() {
		return getStringParam("dataName");
	}
	
	
//-----------------------------------------------------------
	
	protected XPanel getWorkingPanels(DataSet data) {
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new BorderLayout(0, 10));
		
			XPanel buttonPanel = new XPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
			
			exportDataButton = new ExportDataButton("Export data", data, this);
			buttonPanel.add(exportDataButton);
		thePanel.add("North", buttonPanel);
		
//		thePanel.add("Center", timeSeriesPanel(data));
		thePanel.add("South", choicePanel(data));
		
		return thePanel;
	}
	
/*
	protected XPanel timeSeriesPanel(DataSet data) {
		XPanel timePanel = super.timeSeriesPanel(data);
		
		timePanel.add("blank", new XPanel());
		
		return timePanel;
	}
*/
	
	protected void setDisplayForQuestion() {
//		super.setDisplayForQuestion();
			
		for (int i=0 ; i<2 ; i++)
			componentChoice[i].select(0);
		
		validate();
		
//		timePanelLayout.show(timePanel, "blank");
		
		exportDataButton.setDataName(getDataName());

		int nSeasons = getNoOfSeasons();
		if (nSeasons == 1) {
			String[] keys = {"year", "y"};
			exportDataButton.setKeys(keys);
		}
		else {
			String[] keys = {"season", "year", "y"};
			exportDataButton.setKeys(keys);
		}
	}
	
//-----------------------------------------------------------
	
	protected DataSet getData() {
		DataSet data = super.getData();
			NumVariable yearVar = new NumVariable("Year");
		data.addVariable("year", yearVar);
		
			CatVariable seasonVar = new CatVariable("Season");
		data.addVariable("season", seasonVar);
		
		return data;
	}
	
	
//-----------------------------------------------------------
	
	protected void setDataForQuestion() {
		super.setDataForQuestion();
		int nSeasons = getNoOfSeasons();
		int year0 = getYear0();
		int nValues = getCount();
		double year[] = new double[nValues];
		for (int i=0 ; i<nValues ; i++)
			year[i] = year0 + i / nSeasons;
		
		NumVariable yearVar = (NumVariable)data.getVariable("year");
		yearVar.setValues(year);
		yearVar.setDecimals(0);
		
		if (nSeasons > 1) {
			int season[] = new int[nValues];
			for (int i=0 ; i<nValues ; i++)
				season[i] = i % nSeasons;
			CatVariable seasonVar = (CatVariable)data.getVariable("season");
			seasonVar.setLabels(nSeasons == 4 ? kQuarterNames : kMonthNames);
			seasonVar.setValues(season);
		}
	}
	
	
//-----------------------------------------------------------
	
	protected void insertGraph(MessagePanel messagePanel) {
		messagePanel.insertText("\n");
		if (getNoOfSeasons() == 1) {
			XPanel plotPanel = indexPanel(data);
			updateIndexPlotForQuestion();
			indexTimeAxis.setOpaque(false);
			yIndexAxis.setOpaque(false);
			plotPanel.setOpaque(false);
			messagePanel.insertGraph(plotPanel);
		}
		else {
			XPanel plotPanel = seasonalPanel(data);
			updateSeasonalPlotForQuestion();
			seasonTimeAxis.setOpaque(false);
			ySeasonAxis.setOpaque(false);
			plotPanel.setOpaque(false);
			messagePanel.insertGraph(plotPanel);
		}
		messagePanel.insertText("\n");
	}
	
	protected int getMessageHeight() {
		return 400;
	}
	
/*
	protected void showCorrectWorking() {
		super.showCorrectWorking();
		
		
		if (getNoOfSeasons() == 1)
			timePanelLayout.show(timePanel, "index");
		else
			timePanelLayout.show(timePanel, "seasonal");
	}
*/
}
