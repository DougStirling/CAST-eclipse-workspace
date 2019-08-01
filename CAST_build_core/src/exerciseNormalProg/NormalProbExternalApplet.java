package exerciseNormalProg;

import dataView.*;
import distn.NormalDistnVariable;
import exercise2.*;

import exerciseNormal.*;
import exerciseNormal.JdistnAreaLookup.*;


public class NormalProbExternalApplet extends NormalProbApplet {
		
//-----------------------------------------------------------
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("requiredDecimals", "int");
	}
	
	private int getRequiredDecimals() {
		return getIntParam("requiredDecimals");
	}
	
//-----------------------------------------------------------
	
	protected XPanel getWorkingPanels(DataSet data) {
		XPanel thePanel = super.getWorkingPanels(data);
		normalLookupPanel.disableEdit();
		return thePanel;
	}
	
	protected boolean singleNotMultipleDensities() {
		return NormalLookupPanel.SINGLE_DENSITY;
	}
	
	protected void setNormalDistnSelection(NormalDistnVariable normalDistn, double mean, double sd) {
		normalDistn.setMinSelection(mean);
		normalDistn.setMaxSelection(mean);
	}
	
//-----------------------------------------------------------
	
	protected void insertMessageContent(MessagePanel messagePanel) {
		IntervalLimits limits = getLimits();
		
		if (isCountQuestionType()) {
			int n = getN();
			switch (result) {
				case ANS_UNCHECKED:
					messagePanel.insertText("Find the expected number then type it into the text-edit box above.");
					break;
				case ANS_INVALID:
					if (resultPanel.isClear()) {
						messagePanel.insertRedHeading("Error!\n");
						messagePanel.insertText("You must type a value into the Answer box above.");
					}
					else {
						messagePanel.insertRedHeading("Wrong!\n");
						messagePanel.insertText("The expected number cannot be less than zero or more than the total.");
					}
					break;
				case ANS_TOLD:
					messagePanel.insertRedHeading("Answer\n");
					messagePanel.insertText("This probability is the " + limits.probAnswerString() + " (" + limits.areaAnswerString() + ").");
					messagePanel.insertText(" This expected number is " + n + " times this.");
					break;
				case ANS_CORRECT:
					messagePanel.insertRedHeading("Correct!\n");
					messagePanel.insertText("This probability is the " + limits.probAnswerString() + " (" + limits.areaAnswerString() + ").");
					messagePanel.insertText(" This expected number is " + n + " times this.");
					break;
				case ANS_CLOSE:
					messagePanel.insertRedHeading("Close!\n");
					messagePanel.insertText("However you should be able to find the answer more accurately -- to " + getRequiredDecimals() + " decimals.");
					break;
				case ANS_WRONG:
					messagePanel.insertRedHeading("Wrong!\n");
					messagePanel.insertText("The probability is the " + limits.probAnswerString() + ".");
					messagePanel.insertText(" Then use the template to multiply by " + n + ".");
					break;
			}
		}
		else
			switch (result) {
				case ANS_UNCHECKED:
					messagePanel.insertText("Find the probability then type it into the text-edit box above.");
					break;
				case ANS_INCOMPLETE:
					messagePanel.insertRedHeading("Error!\n");
					messagePanel.insertText("You must type a probability into the Answer box above.");
					break;
				case ANS_INVALID:
					messagePanel.insertRedHeading("Wrong!\n");
					messagePanel.insertText("Probabilities cannot be less than zero or more than one.");
					break;
				case ANS_TOLD:
					messagePanel.insertRedHeading("Answer\n");
					messagePanel.insertText("This probability is the " + limits.probAnswerString() + " (" + limits.areaAnswerString() + ").");
					break;
				case ANS_CORRECT:
					messagePanel.insertRedHeading("Correct!\n");
					messagePanel.insertText("This probability is the " + limits.probAnswerString() + " (" + limits.areaAnswerString() + ").");
					break;
				case ANS_CLOSE:
					messagePanel.insertRedHeading("Close!\n");
					messagePanel.insertText("However you should be able to find the answer more accurately -- to " + getRequiredDecimals() + " decimals.");
					break;
				case ANS_WRONG:
					messagePanel.insertRedHeading("Wrong!\n");
					messagePanel.insertText("The probability is the " + limits.probAnswerString() + ".");
					break;
			}
	}
	
//-----------------------------------------------------------
	
	protected int getMessageHeight() {
		return 150;
	}
	
	protected int assessAnswer() {
		if (resultPanel.isClear())
			return ANS_INCOMPLETE;
		
		double attempt = getAttempt();
		if (isCountQuestionType())
			attempt /= getN();
		
		if (attempt < 0.0 || attempt > 1.0)
			return ANS_INVALID;
		
		IntervalLimits limits = getLimits();
		double correct = evaluateProbability(limits);
		
		double factor = Math.pow(10.0, getRequiredDecimals());
		
		if (Math.round(attempt * factor) == Math.round(correct * factor))
			return ANS_CORRECT;
		else {
			factor /= 10.0;
			return (Math.round(attempt * factor) == Math.round(correct * factor)) ? ANS_CLOSE : ANS_WRONG;
		}
	}
}