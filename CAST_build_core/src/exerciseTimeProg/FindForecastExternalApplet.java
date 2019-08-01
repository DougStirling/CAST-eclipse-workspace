package exerciseTimeProg;

import java.awt.*;

import dataView.*;
import utils.*;
import exercise2.*;
import models.*;
import formula.*;


public class FindForecastExternalApplet extends FindForecastApplet {
	private ResultValuePanel lsResultPanel, esResultPanel;
	
	private ExportDataButton exportButton;
	
	private int lsResult, esResult;
	
//================================================
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("dataName", "string");
	}
	
	private String getDataName() {
		return getStringParam("dataName");
	}
	
	
//-----------------------------------------------------------
	
	protected XPanel listPanel(DataSet data) {
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new BorderLayout(0, 0));
		return thePanel;
	}
	
	protected XPanel tablePanel(DataSet data) {
		XPanel thePanel = new InsetPanel(20, 7);
		thePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			exportButton = new ExportDataButton("Export Data", data, "y", "", this);
			thePanel.add(exportButton);
		return thePanel;
	}

	protected XPanel resultPanels() {
		XPanel eqnsPanel = new InsetPanel(2, 2);
		eqnsPanel.setLayout(new VerticalLayout(VerticalLayout.CENTER, VerticalLayout.VERT_CENTER, 10));

			lsResultPanel = new ResultValuePanel(this, "Least squares forecast =", 6);
			registerStatusItem("regn", lsResultPanel);
		eqnsPanel.add(lsResultPanel);
		
			esResultPanel = new ResultValuePanel(this, "Exponential smoothing forecast =", 6);
			registerStatusItem("es", esResultPanel);
		eqnsPanel.add(esResultPanel);
		
		return eqnsPanel;
	}
	
	protected void setTablesForQuestion() {
		lsResultPanel.clear();
		esResultPanel.clear();
		
		exportButton.setDataName(getDataName());
	}
	
//-----------------------------------------------------------
	
	
	protected void insertMessageContent(MessagePanel messagePanel) {
		switch (result) {
			case ANS_INCOMPLETE:
			case ANS_TOLD:
			case ANS_CORRECT:
			case ANS_WRONG:
				super.insertMessageContent(messagePanel);
				break;
			case ANS_UNCHECKED:
				messagePanel.insertText("Type the two forecasts in the edit boxes above.\n(They should be correct to " + getForecastDecimals() + " decimals.)");
				break;
			case ANS_CLOSE:
				switch (esResult) {
					case ANS_CORRECT:
						messagePanel.insertBoldRedText("Your exponentially smoothed prediction is correct.");
						break;
					case ANS_CLOSE:
						messagePanel.insertBoldRedText("Your exponentially smoothed prediction is close but not exact to " + getForecastDecimals() + " decimals.");
						break;
					case ANS_WRONG:
						messagePanel.insertBoldRedText("Your exponentially smoothed prediction is wrong!");
						break;
				}
				insertCorrectEsMessage(messagePanel);
				
				switch (lsResult) {
					case ANS_CORRECT:
						messagePanel.insertBoldRedText("\nYour least squares prediction is correct.");
						break;
					case ANS_CLOSE:
						messagePanel.insertBoldRedText("\nYour least squares prediction is close but not exact to " + getForecastDecimals() + " decimals.");
						break;
					case ANS_WRONG:
						messagePanel.insertBoldRedText("\nYour least squares prediction is wrong!");
						break;
				}
				insertCorrectRegnMessage(messagePanel);
				break;
		}
	}
	
	protected void insertCorrectEsMessage(MessagePanel messagePanel) {
		NumValue esConst = getEsConst();
		NumValue esConst2 = new NumValue(1.0 - esConst.toDouble(), esConst.decimals);
		messagePanel.insertText("\nEach year's exponentially smoothed value is " + esConst
				+ " times its actual value plus " + esConst2 + " times is the previous exponentially smoothed value.");
		
		super.insertCorrectEsMessage(messagePanel);
	}
	
	protected void insertCorrectRegnMessage(MessagePanel messagePanel) {
		MultipleRegnModel ls = (MultipleRegnModel)data.getVariable("ls");
		NumValue intercept = ls.getParameter(0);
		NumValue slope = ls.getParameter(1);
		int baseYear = getBaseYear();

		messagePanel.insertText("\nIn terms of the base year, "
																	+ baseYear + ", the least squares line is\n");
		messagePanel.setAlignment(MessagePanel.CENTER_ALIGN);

		messagePanel.insertText(MText.expandText(getVarName() + " = " + intercept
												+ " + " + slope + " #times# (coded year)."));
		messagePanel.setAlignment(MessagePanel.LEFT_ALIGN);
		
		super.insertCorrectRegnMessage(messagePanel);
	}
	
	protected int getMessageHeight() {
		return 220;
	}
	
	
//-----------------------------------------------------------
	
	private int checkAttempt(double attempt, double exact) {
		double factor = Math.pow(10.0,  getForecastDecimals());
		if (Math.round(attempt * factor) == Math.round(exact * factor))
			return ANS_CORRECT;
		factor /= 10.0;
		return (Math.round(attempt * factor) == Math.round(exact * factor)) ? ANS_CLOSE : ANS_WRONG;
	}
	
	private double correctEsValue() {
		NumVariable esVar = (NumVariable)data.getVariable("es");
		return esVar.doubleValueAt(esVar.noOfValues() - 1);
	}
	
	private double correctLsValue() {
		int forecastIndex = getForecastYear() - getBaseYear();
		
		MultipleRegnModel ls = (MultipleRegnModel)data.getVariable("ls");
		double x[] = {forecastIndex};
		return ls.evaluateMean(x);
	}
	
	protected int assessAnswer() {
		if (lsResultPanel.isClear() || esResultPanel.isClear())
			return ANS_INCOMPLETE;
		
		esResult = checkAttempt(esResultPanel.getAttempt().toDouble(), correctEsValue());
		lsResult = checkAttempt(lsResultPanel.getAttempt().toDouble(), correctLsValue());
		
		if (esResult == ANS_CORRECT && lsResult == ANS_CORRECT)
			return ANS_CORRECT;
		
		if (esResult != ANS_WRONG && lsResult != ANS_WRONG)
			return ANS_CLOSE;
		
		return ANS_WRONG;
	}
	
	protected void giveFeedback() {
	}
	
	protected void showCorrectWorking() {
		NumVariable esVar = (NumVariable)data.getVariable("es");
		NumValue esCorrect = ((NumValue)esVar.valueAt(esVar.noOfValues() - 1));
		esCorrect.decimals = getForecastDecimals();
		esResultPanel.showAnswer(esCorrect);
		
		int forecastIndex = getForecastYear() - getBaseYear();
		MultipleRegnModel ls = (MultipleRegnModel)data.getVariable("ls");
		double x[] = {forecastIndex};
		double forecast = ls.evaluateMean(x);
		
		NumValue regnCorrect = new NumValue(forecast, getForecastDecimals());
		lsResultPanel.showAnswer(regnCorrect);
	}
	
	protected double getMark() {
		double mark = 0.0;
		if (esResult == ANS_CORRECT)
			mark += 0.5;
		else if (esResult == ANS_CLOSE)
			mark += 0.2;
		if (lsResult == ANS_CORRECT)
			mark += 0.5;
		else if (lsResult == ANS_CLOSE)
			mark += 0.2;
		return mark;
	}
}
