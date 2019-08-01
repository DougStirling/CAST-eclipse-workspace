package exerciseBivarProg;

import java.awt.*;

import dataView.*;
import exercise2.*;


public class GuessCorrExternalApplet extends GuessCorrApplet {
	
	private ExportDataButton exportButton;
	
//================================================
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("requiredDecimals", "int");
		registerParameter("dataName", "string");
	}
	
	private String getDataName() {
		return getStringParam("dataName");
	}
	
	private int getRequiredDecimals() {
		return getIntParam("requiredDecimals");
	}
	
	
//-----------------------------------------------------------
	
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
	
	protected void randomiseCorr(NumValue corr) {
		double corrVal = corr.toDouble();
		double maxDiff = Math.min(1 - corrVal, 1 + corrVal);
		double slop = maxDiff * (randomDouble() - 0.5);
		corr.setValue(corrVal + slop);
		corr.decimals = getRequiredDecimals();
	}
	
	
//-----------------------------------------------------------
	
	protected void insertMessageContent(MessagePanel messagePanel) {
		if (result == ANS_INCOMPLETE || result == ANS_INVALID || result == ANS_TOLD)
			super.insertMessageContent(messagePanel);
		else {
			switch (result) {
				case ANS_UNCHECKED:
					messagePanel.insertText("Export the data and find the correlation coefficient between the variables in a statistics program. Type it into the text-edit box above, correct to " + getRequiredDecimals() + " decimals.");
					break;
				case ANS_CORRECT:
					messagePanel.insertRedHeading("Good!\n");
					messagePanel.insertText("You have correctly found the correlation coefficient to " + getRequiredDecimals() + " decimals.");
					break;
				case ANS_CLOSE:
					messagePanel.insertRedHeading("Close!\n");
					messagePanel.insertText("Your answer is close but not exact to " + getRequiredDecimals() + " decimals.");
					break;
				case ANS_WRONG:
					messagePanel.insertRedHeading("Not close enough!\n");
					messagePanel.insertText("You have not correctly found the correlation coefficient between the variables");
					break;
			}
		}
	}
	
	protected int assessAnswer() {
		double attempt = getAttempt();
		
		if (corrResultPanel.isClear())
			return ANS_INCOMPLETE;
		else if (attempt > 1.0 || attempt < -1.0)
			return ANS_INVALID;
		else {
			double correct = corr.toDouble();
			double factor = Math.pow(10.0, getRequiredDecimals());
			if (Math.round(attempt * factor) == Math.round(correct * factor))
				return ANS_CORRECT;
			else {
				factor /= 10;
				return (Math.round(attempt * factor) == Math.round(correct * factor)) ? ANS_CLOSE : ANS_WRONG;
			}
		}
	}
}