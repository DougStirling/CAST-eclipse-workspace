package exerciseNormalProg;

import dataView.*;


public class NormalInverseExternalApplet extends NormalInverseApplet {
	
//-----------------------------------------------------------

	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("requiredDecimals", "int");
	}
	
	protected int getRequiredDecimals() {
		return getIntParam("requiredDecimals");
	}

//-----------------------------------------------------------
	
	protected XPanel getWorkingPanels(DataSet data) {
		XPanel thePanel = super.getWorkingPanels(data);
		normalLookupPanel.disableEdit();
		return thePanel;
	}
	
//-----------------------------------------------------------
	
	protected String areaAnswerString(NumValue percent) {
		NumValue quantile =  new NumValue(evaluatePercentile(percent, "distn"), getRequiredDecimals());
		String s;
		if (tailType() == GREATER_THAN)
			s = "The probability of being greater than  ";
		else
			s = "The probability of being less than ";
		return s + quantile.toString() + " is " + new NumValue(percent.toDouble() * 0.01, percent.decimals + 2) + ".";
	}
	
	protected String areaCloserString(NumValue percent) {
		String s = "However you should be able to find the answer more accurately -- to " + getRequiredDecimals() + " decimals. Find the value such than the probability of being ";
		if (tailType() == GREATER_THAN)
			s += "greater than";
		else
			s += "less than";
		return s + " it is " + new NumValue(percent.toDouble() * 0.01, percent.decimals + 2) + ".";
	}
	
	protected String areaHintString(NumValue percent) {
		return areaCloserString(percent);
	}
	
	protected int getMessageHeight() {
		return 100;
	}
	
//-----------------------------------------------------------
	
	protected int assessAnswer() {
		if (resultPanel.isClear())
			return ANS_INCOMPLETE;
		
		double attempt = getAttempt();

		NumValue percent = getPercent();
		double correct = evaluatePercentile(percent, "distn");
		
		double factor = Math.pow(10.0, getRequiredDecimals());
		
		if (Math.round(attempt * factor) == Math.round(correct * factor))
			return ANS_CORRECT;
		else {
			factor /= 10.0;
			return (Math.round(attempt * factor) == Math.round(correct * factor)) ? ANS_CLOSE : ANS_WRONG;
		}
	}
}