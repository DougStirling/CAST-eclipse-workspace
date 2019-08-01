package exerciseSDProg;

import dataView.*;

import java.awt.*;

import axis.*;
import coreGraphics.*;
import utils.*;
import exercise2.*;

import exerciseSD.*;


public class ScaledMeanSdExternalApplet extends ScaledMeanSdApplet {
	
	private ExportDataButton exportButton;
	
//================================================
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("dataName", "string");
	}
	
	private String getDataName() {
		return getStringParam("dataName");
	}
	
	
//-----------------------------------------------------------
	
	protected DataView createDotPlot(DataSet data, HorizAxis theAxis) {
		DataView theView = new StackedDotPlotView(data, this, theAxis, null, false);
		theView.setActiveNumVariable("y");
		return theView;
	}
	
	protected XPanel createTemplates() {
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new BorderLayout(0,0));
		
			XPanel buttonPanel = new XPanel();
			buttonPanel.setLayout(new VerticalLayout(VerticalLayout.CENTER, VerticalLayout.VERT_CENTER));
	
			exportButton = new ExportDataButton("Export Data", data, "y", "", this);
			buttonPanel.add(exportButton);
		thePanel.add("Center", buttonPanel);
	
			XPanel meanSdPanel = new XPanel();
			meanSdPanel.setLayout(new ProportionLayout(0.5, 0));
			
			meanSdPanel.add(ProportionLayout.LEFT, createMeanPanel(false));
			meanSdPanel.add(ProportionLayout.RIGHT, createSdPanel(false));
		thePanel.add("South", meanSdPanel);
		
		return thePanel;
	}
	
	protected void setTemplatesForQuestion() {
		exportButton.setDataName(getDataName());
	}
	
	
//-----------------------------------------------------------
	
	protected void insertMessageContent(MessagePanel messagePanel) {
		switch (result) {
			case ANS_UNCHECKED:
				messagePanel.insertText("Export the data then find the transformed mean and sd.\n(You can either transform the data before finding the mean and sd or find the mean and sd of the original data and transform them.)");
				break;
			case ANS_TOLD:
				messagePanel.insertRedHeading("Answer\n");
				messagePanel.insertText("The correct mean and standard deviation are shown above.");
				break;
			case ANS_CORRECT:
				messagePanel.insertRedHeading("Good!\n");
				messagePanel.insertRedText("You have found the correct mean and standard deviation.");
				break;
			case ANS_INCOMPLETE:
			case ANS_WRONG:
				messagePanel.insertRedHeading("Wrong!\n");
				messagePanel.insertBoldText("Mean: ");
				if (meanResult == ANS_INCOMPLETE)
					messagePanel.insertRedText("You have not typed a value for the mean.");
				else if (meanResult == ANS_CORRECT)
					messagePanel.insertText("You have given the correct value for the mean.");
				else {		//		meanResult == ANS_WRONG or ANS_CLOSE
					if (meanResult == ANS_CLOSE)
						messagePanel.insertRedText("Your answer is not accurate enough. ");
					messagePanel.insertRedText("If you found the mean of the original data, the mean of the transformed variable can be found with the formula  ");
					if (getDirection() == ScaleXYTemplatePanel.X_TO_Y)
						messagePanel.insertFormula(xToYFormula("mean"));
					else
						messagePanel.insertFormula(yToXFormula("mean"));
				}
				
				messagePanel.insertBoldText("\nSt devn: ");
				if (sdResult == ANS_INCOMPLETE)
					messagePanel.insertRedText("You have not typed a value for the standard deviation.");
				else if (sdResult == ANS_INVALID)
					messagePanel.insertRedText("The standard deviation cannot be negative.");
				else if (sdResult == ANS_CORRECT)
					messagePanel.insertText("You have given the correct standard deviation.");
				else {		//		sdResult == ANS_WRONG or ANS_CLOSE
					if (sdResult == ANS_CLOSE)
						messagePanel.insertRedText("Your answer is not accurate enough. ");
					messagePanel.insertRedText("If you found the sd of the original data, the sd of the transformed variable can be found with the formula  ");
					if (getDirection() == ScaleXYTemplatePanel.X_TO_Y)
						messagePanel.insertFormula(xToYSdFormula());
					else
						messagePanel.insertFormula(yToXSdFormula());
				}
				break;
		}
	}
	
	protected int getMessageHeight() {
		return 200;
	}
	
	protected void findSourceMeanSd() {
		ValueEnumeration ye = ((NumVariable)data.getVariable("y")).values();
		int n = 0;
		double sy = 0.0;
		double syy = 0.0;
		while (ye.hasMoreValues()) {
			double y = ye.nextDouble();
			n += 1;
			sy += y;
			syy += y * y;
		}
		sourceMean = sy / n;
		sourceSd = Math.sqrt((syy - sy * sourceMean) / (n - 1));
	}
	
	
//-----------------------------------------------------------
	
	protected void showCorrectWorking() {
		meanResult = sdResult = ANS_TOLD;
		
		findSourceMeanSd();
		
		NumValue interceptVal = getIntercept();
		NumValue slopeVal = getSlope();
		double intercept = interceptVal.toDouble();
		double slope = slopeVal.toDouble();
		
		double mean, sd;
		if (getDirection() == ScaleXYTemplatePanel.X_TO_Y) {
			mean = intercept + slope * sourceMean;
			sd = slope * sourceSd;
		}
		else {
			mean = (sourceMean - intercept) / slope;
			sd = sourceSd / slope;
		}
		
		int resultDecimals = getMaxResult().decimals;
		meanResultPanel.showAnswer(new NumValue(mean, resultDecimals));
		sdResultPanel.showAnswer(new NumValue(sd, resultDecimals));
	}
}