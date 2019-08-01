package exerciseBivarProg;

import java.awt.*;

import coreVariables.ScaledVariable;
import dataView.*;
import models.*;
import exercise2.*;


public class FindPredictionExternalApplet extends FindResidualApplet {
	
	private ExportDataButton exportButton;
	
	protected String getResultName() {
		return "Prediction =";
	}
	
	protected String getResultUnits() {
		return "";
	}
	
//-----------------------------------------------------------
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("xValue", "const");
		registerParameter("xDecimals", "int");
		registerParameter("dataName", "string");
	}
	
	private NumValue getXValue() {
		return getNumValueParam("xValue");
	}
	
	private String getDataName() {
		return getStringParam("dataName");
	}
	
	private int getXDecimals() {
		return getIntParam("xDecimals");
	}
	
//-----------------------------------------------------------
	
	protected XPanel getWorkingPanels(DataSet data) {
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new BorderLayout(0, 0));
		
		thePanel.add("Center", getScatterPanel(data));
		
			XPanel buttonPanel = new XPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

				String[] keys = {"x", "y"};
				exportButton = new ExportDataButton("Export Data", data, keys, null, this);
			buttonPanel.add(exportButton);
			
		thePanel.add("South", buttonPanel);
		
		return thePanel;
	}
	
	
	protected void setDataForQuestion() {
		super.setDataForQuestion();

		ScaledVariable xVar = (ScaledVariable)data.getVariable("x");
		xVar.setDecimals(getXDecimals());
	}
	
	
	protected void doSelection(int n) {
		data.variableChanged("y");
	}
	
	protected void setDisplayForQuestion() {
		theView.setPredictionX(getXValue().toDouble());
		
		super.setDisplayForQuestion();

		exportButton.setDataName(getDataName());
	}
	
//-----------------------------------------------------------
	
	protected void insertMessageContent(MessagePanel messagePanel) {
//		NumVariable xVar = (NumVariable)data.getVariable("x");
		LinearModel ls = (LinearModel)data.getVariable("ls");
		
		switch (result) {
			case ANS_UNCHECKED:
				messagePanel.insertText("Export the data then use the least squares line to find the value of the prediction (correct to " + getDecimals() + " decimals.");
				break;
			case ANS_INCOMPLETE:
				messagePanel.insertRedHeading("Error\n");
				messagePanel.insertText("You must type a value for the prediction.");
				break;
			case ANS_TOLD:
				messagePanel.insertRedHeading("Answer\n");
				messagePanel.insertText("Using the least squares equation, the predicted value of " + getYVarName() + " is\n");
				messagePanel.setAlignment(MessagePanel.CENTER_ALIGN);
				messagePanel.insertText(ls.getIntercept().toString());
				NumValue slope = ls.getSlope();
				NumValue x = getXValue();
				if (slope.toDouble() > 0)
					messagePanel.insertText(" + " + slope);
				else
					messagePanel.insertText(" - " + new NumValue(-slope.toDouble(), slope.decimals));
				messagePanel.insertText(" x " + x);
				messagePanel.insertText(" = " + new NumValue(getCorrect(), getDecimals()));
				
				break;
			case ANS_CORRECT:
				messagePanel.insertRedHeading("Good!\n");
				messagePanel.insertText("You have given the correct prediction.");
				break;
			case ANS_CLOSE:
				messagePanel.insertRedHeading("Close!\n");
				messagePanel.insertRedText("The value that you have given is close but not exact to " + getDecimals() + " decimals.");
				break;
			case ANS_WRONG:
				messagePanel.insertRedHeading("Not close enough!\n");
				messagePanel.insertRedText("Use the equation of the least squares line to find the prediction.");
				break;
		}
	}
	
	protected int getMessageHeight() {
		return 120;
	}
	
	
//-----------------------------------------------------------
	
	
	protected double getCorrect() {
		LinearModel ls = (LinearModel)data.getVariable("ls");
		
		double x = getXValue().toDouble();
		double prediction = ls.evaluateMean(x);
		
		return prediction;
	}
	
	protected int assessAnswer() {
		if (resultPanel.isClear())
			return ANS_INCOMPLETE;
		else {
			double factor = Math.pow(10.0,  getDecimals());
			
			double correct = getCorrect();
			double attempt = getAttempt();
			
			if (Math.round(correct * factor) == Math.round(attempt * factor))
				return ANS_CORRECT;
			else {
				factor *= 10.0;
				if (Math.round(correct * factor) == Math.round(attempt * factor))
					return ANS_CLOSE;
				else
					return ANS_WRONG;
			}
		}
	}
}