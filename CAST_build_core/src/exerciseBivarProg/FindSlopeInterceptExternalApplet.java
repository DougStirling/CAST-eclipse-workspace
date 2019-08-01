package exerciseBivarProg;

import java.awt.*;

import dataView.*;
import exercise2.*;
import exerciseBivar.*;
import formula.*;
import linMod.*;


public class FindSlopeInterceptExternalApplet extends GuessCorrApplet {
	
	private ExportDataButton exportButton;
	private LinearEquationPanel eqnView;
	
	private double lsIntercept, lsSlope;
	
	private int slopeResult = ANS_UNCHECKED;
	private int interceptResult = ANS_UNCHECKED;
	
//================================================
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("requiredInterceptDecimals", "int");
		registerParameter("requiredSlopeDecimals", "int");
		registerParameter("dataName", "string");
	}
	
	private String getDataName() {
		return getStringParam("dataName");
	}
	
	private int getRequiredInterceptDecimals() {
		return getIntParam("requiredInterceptDecimals");
	}
	
	private int getRequiredSlopeDecimals() {
		return getIntParam("requiredSlopeDecimals");
	}
	
	
//-----------------------------------------------------------
	
	protected void addResultPanel(XPanel thePanel) {
		FormulaContext stdContext = new FormulaContext(Color.black, getStandardFont(), this);
		eqnView = new LinearEquationPanel(stdContext);
		registerStatusItem("slopeIntercept", eqnView);
		thePanel.add(eqnView);
	}
	
	protected void addExportButton(DataSet data, XPanel thePanel) {
		XPanel buttonPanel = new XPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			String[] keys = {"x", "y"};
			exportButton = new ExportDataButton("Export Data", data, keys, null, this);
		buttonPanel.add(exportButton);
		
		thePanel.add("South", buttonPanel);
	}
	
	protected void updateExportDataName() {
		exportButton.setDataName(getDataName());
	}
	
	protected void clearResults() {
		eqnView.setClear();
		eqnView.setVarNames(getYVarName(), getXVarName());
	}
	
	protected void setDataForQuestion() {
		super.setDataForQuestion();
		
		LSEstimate lsParams = new LSEstimate(data, "x", "y");
		lsIntercept = lsParams.getIntercept();
		lsSlope = lsParams.getSlope();
	}
	
	
//-----------------------------------------------------------
	
	protected void insertMessageContent(MessagePanel messagePanel) {
		switch (result) {
			case ANS_INCOMPLETE:
				messagePanel.insertRedHeading("Error!\n");
				messagePanel.insertText("You have not typed values for the intercept and slope.");
				break;
			case ANS_TOLD:
				messagePanel.insertRedHeading("Answer\n");
				messagePanel.insertText("The exact values for the intercept and slope of the least squares line are shown above.");
				break;
			case ANS_UNCHECKED:
				messagePanel.insertText("Export the data and find the least squares line relating the response, " + getYVarName() + " to " + getXVarName()
				+ " in a statistics program. Type the intercept, correct to " + getRequiredInterceptDecimals() + " decimals, and the slope, correct to " + getRequiredSlopeDecimals() + " decimals,.");
				break;
			case ANS_CORRECT:
				messagePanel.insertRedHeading("Good!\n");
				messagePanel.insertText("You have correctly found the intercept to " + getRequiredInterceptDecimals() + " decimals and the slope to " + getRequiredSlopeDecimals() + ".");
				break;
			case ANS_CLOSE:
				messagePanel.insertRedHeading("Close!\n");
				messagePanel.insertText("Your answer is close but not exact to " + getRequiredInterceptDecimals() + " decimals.");
				break;
			case ANS_WRONG:
				messagePanel.insertRedHeading("Not close enough!\n");
				messagePanel.insertText("You have not correctly found the correlation coefficient between the variables");
				break;
		}
	}
	
	private int checkOneParam(double attempt, double correct, int decimals) {
		double factor = Math.pow(10.0, decimals);
		if (Math.round(attempt * factor) == Math.round(correct * factor))
			return ANS_CORRECT;
		else {
			factor /= 10;
			return (Math.round(attempt * factor) == Math.round(correct * factor)) ? ANS_CLOSE : ANS_WRONG;
		}
	}
	
	protected int assessAnswer() {
		if (eqnView.isClear())
			return ANS_INCOMPLETE;
		
		double slopeAttempt = eqnView.getSlope().toDouble();
		double interceptAttempt = eqnView.getIntercept().toDouble();
		
		slopeResult = checkOneParam(slopeAttempt, lsSlope, getRequiredSlopeDecimals());
		interceptResult = checkOneParam(interceptAttempt, lsIntercept, getRequiredInterceptDecimals());
		
		return Math.max(slopeResult, interceptResult);
	}
	
	protected void showCorrectWorking() {
		eqnView.setValues(new NumValue(lsIntercept, getRequiredInterceptDecimals()), new NumValue(lsSlope, getRequiredSlopeDecimals()));
	}
}