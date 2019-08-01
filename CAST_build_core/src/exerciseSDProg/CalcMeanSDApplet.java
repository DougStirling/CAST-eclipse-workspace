package exerciseSDProg;

import java.awt.*;
import java.util.*;

import axis.*;
import coreGraphics.*;
import dataView.*;
import utils.*;
import exercise2.*;



public class CalcMeanSDApplet extends ExerciseApplet {
	static final private String DATA_NAME_PARAM = "dataName";
	static final protected String N_VARIABLES_PARAM = "nVariables";		//		x1, x2, ..., x<nVariables>
	
	static final private Color kLightRedColor = new Color(0xFF6666);
	
	private HorizAxis theAxis;
	private StackedDotPlotView theView;
	
	protected ResultValuePanel meanResultPanel, medianResultPanel, sdResultPanel;
	
	protected double exactMean, exactMedian, exactSd;
	private int meanResult, medianResult, sdResult;
	
//================================================
	
	protected void createDisplay() {
		setLayout(new BorderLayout(0, 10));
		
			questionPanel = new QuestionPanel(this);
		add("North", questionPanel);

		add("Center", getWorkingPanels(data));
				
			XPanel bottomPanel = new XPanel();
			bottomPanel.setLayout(new VerticalLayout(VerticalLayout.FILL, VerticalLayout.VERT_TOP, 4));
			
			addSummaryResultPanels(bottomPanel);
			
			bottomPanel.add(createMarkingPanel(NO_HINTS));
			
				XPanel messagePanel = new XPanel();
				messagePanel.setLayout(new FixedSizeLayout(100, getMessageHeight()));
					
					message = new ExerciseMessagePanel(this);
				messagePanel.add(message);
			bottomPanel.add(messagePanel);
		
		add("South", bottomPanel);
	}
	
	protected void addSummaryResultPanels(XPanel bottomPanel) {
		if (hasOption("mean")) {
			meanResultPanel = new ResultValuePanel(this, translate("Mean") + " =", 6);
			registerStatusItem("mean", meanResultPanel);
			bottomPanel.add(meanResultPanel);
		}
		
		if (hasOption("median")) {
			medianResultPanel = new ResultValuePanel(this, translate("Median") + " =", 6);
			registerStatusItem("median", medianResultPanel);
			bottomPanel.add(medianResultPanel);
		}
		if (hasOption("sd")) {
			sdResultPanel = new ResultValuePanel(this, translate("Standard deviation") + " =", 6);
			registerStatusItem("sd", sdResultPanel);
			bottomPanel.add(sdResultPanel);
		}
	}
	
//-----------------------------------------------------------
	
	protected void registerParameterTypes() {
//		registerParameter("index", "int");					//	always registered
		registerParameter("nValues", "int");
		registerParameter("variableIndex", "int");
		registerParameter("requiredDecimals", "int");
		registerParameter("varName", "string");
	}
	
	protected int getNValues() {
		return getIntParam("nValues");
	}
	
	protected int getVariableIndex() {
		return getIntParam("variableIndex");
	}
	
	protected String getVarName() {
		return getStringParam("varName");
	}
	
	protected int getRequiredDecimals() {
		return getIntParam("requiredDecimals");
	}
	
	protected String getAxisInfo() {
		int varIndex = getVariableIndex();
		return getParameter("x" + varIndex + "Axis");
	}

	
//-----------------------------------------------------------
	
	protected XPanel getWorkingPanels(DataSet data) {
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new BorderLayout(0,0));
			
			XPanel dotPlotPanel = new XPanel();
			dotPlotPanel.setLayout(new AxisLayout());
			
				theAxis = new HorizAxis(this);
			dotPlotPanel.add("Bottom", theAxis);
			
				theView = new StackedDotPlotView(data, this, theAxis);
				int nValues = ((NumVariable)data.getVariable("x0")).noOfValues();
				theView.setCrossSize(nValues < 250 ? DataView.LARGE_CROSS : DataView.MEDIUM_CROSS);
				theView.lockBackground(Color.white);
				theView.setCanDragCrosses(false);
				theView.setHiliteBackgroundColor(kLightRedColor);
			dotPlotPanel.add("Center", theView);
		
		thePanel.add("Center", dotPlotPanel);
			
			String[] keys = getExportKeys();
			
			XPanel buttonPanel = new XPanel();
			buttonPanel.setLayout(new VerticalLayout(VerticalLayout.CENTER, VerticalLayout.VERT_CENTER));
			
			ExportDataButton exportButton = new ExportDataButton("Export Data", data, keys, getParameter(DATA_NAME_PARAM), this);
			buttonPanel.add(exportButton);
		
		thePanel.add("South", buttonPanel);
		
		return thePanel;
	}
	
	protected String[] getExportKeys() {
		int nVars = Integer.parseInt(getParameter(N_VARIABLES_PARAM));
		String[] keys = new String[nVars];
		for (int i=0 ; i<nVars ; i++)
			keys[i] = "x" + i;
		return keys;
	}
	
	protected void setDisplayForQuestion() {
		updatePlotVariable();
		
		if (meanResultPanel != null)
			meanResultPanel.clear();
		if (medianResultPanel != null)
			medianResultPanel.clear();
		if (sdResultPanel != null)
			sdResultPanel.clear();
	}
	
	private void updatePlotVariable() {
		String xKey = "x" + getVariableIndex();
		
		theAxis.readNumLabels(getAxisInfo());
		theAxis.setAxisName(getVarName());
		theAxis.invalidate();
		
		theView.setActiveNumVariable(xKey);

		data.variableChanged(xKey);		//	clears selection
	}
	
	protected void setDataForQuestion() {
		int noOfValues = getNValues();
		
		RandomisedNumVariable firstVar = (RandomisedNumVariable)data.getVariable("x0");
		firstVar.setSampleSize(noOfValues);
		firstVar.setSampleFromSeed(nextSeed());		//		all variables use same generator
		int[] map = firstVar.getMap();
		
		int nVars = Integer.parseInt(getParameter(N_VARIABLES_PARAM));
		for (int i=1 ; i<nVars ; i++) {
			RandomisedNumVariable var = (RandomisedNumVariable)data.getVariable("x" + i);
			var.setSampleSize(noOfValues);
			var.setMap(map);
		}
		
		double sx = 0.0, sxx = 0.0;
		NumVariable selectedVar = (NumVariable)data.getVariable("x" + getVariableIndex());
		ValueEnumeration ve = selectedVar.values();
		double[] x = new double[noOfValues];
		int i = 0;
		while (ve.hasMoreValues()) {
			double v = ve.nextDouble();
			x[i++] = v;
			sx += v;
			sxx += v * v;
		}
		exactMean = hasOption("mean") ? sx / noOfValues : Double.NaN;
		exactSd = hasOption("sd") ? Math.sqrt((sxx - exactMean * sx) / (noOfValues - 1)) : Double.NaN;
		if (hasOption("median")) {
			Arrays.sort(x);
			if (noOfValues % 2 == 1)
				exactMedian = x[noOfValues / 2];
			else
				exactMedian = (x[noOfValues / 2] + x[noOfValues / 2 - 1]) / 2;
		}
		else
			exactMedian = Double.NaN;
	}
	
	
//-----------------------------------------------------------
	
	protected void insertMessageContent(MessagePanel messagePanel) {
		String varName = getVarName();
		switch (result) {
			case ANS_UNCHECKED:
				messagePanel.insertText("Find the summary statistics of " + varName + " using a statistical program.");
				break;
			case ANS_INCOMPLETE:
				messagePanel.insertRedHeading("Error!\n");
				messagePanel.insertText("You must type values for the summary statistics of " + varName + ".");
				break;
			case ANS_INVALID:
				messagePanel.insertRedHeading("Wrong!\n");
				messagePanel.insertText("Standard deviations can never be negative.");
				break;
			case ANS_TOLD:
				messagePanel.insertRedHeading("Answer\n");
				insertAnswer(messagePanel, varName);
				break;
			case ANS_CORRECT:
				messagePanel.insertRedHeading("Good!\n");
				messagePanel.insertText("You have correctly found the summary statistics to " + getRequiredDecimals() + " decimal places.\n");
				break;
			case ANS_CLOSE:
			case ANS_WRONG:
				if (result == ANS_CLOSE)
					messagePanel.insertRedHeading("Close!\n");
				else
					messagePanel.insertRedHeading("Wrong!\n");
				if (meanResult == ANS_CORRECT) {
					if (hasOption("mean"))
						messagePanel.insertText("Your value for the mean is correct to " + getRequiredDecimals() + " decimal places.\n");
				}
				else {
					if (meanResult == ANS_CLOSE)
						messagePanel.insertRedText("Your value for the mean is close but not exact to " + getRequiredDecimals() + " decimal places.");
					else		//	ANS_WRONG
						messagePanel.insertRedText("Your value for the mean is wrong!");
					messagePanel.insertText(" The exact mean to " + getRequiredDecimals() + " decimals is " + new NumValue(exactMean, getRequiredDecimals()) + ".\n");
				}

				if (medianResult == ANS_CORRECT) {
					if (hasOption("median"))
						messagePanel.insertText("Your value for the median is correct to " + getRequiredDecimals() + " decimal places.\n");
				}
				else {
					if (medianResult == ANS_CLOSE)
						messagePanel.insertRedText("Your value for the median is close but not exact to " + getRequiredDecimals() + " decimal places.");
					else		//	ANS_WRONG
						messagePanel.insertRedText("Your value for the median is wrong!");
					messagePanel.insertText(" The exact median to " + getRequiredDecimals() + " decimals is " + new NumValue(exactMedian, getRequiredDecimals()) + ".\n");
				}

				if (sdResult == ANS_CORRECT) {
					if (hasOption("sd"))
						messagePanel.insertText("Your value for the standard deviation is correct to " + getRequiredDecimals() + " decimal places.\n");
				}
				else {
					if (sdResult == ANS_CLOSE)
						messagePanel.insertRedText("Your value for the standard deviation is close but not exact to " + getRequiredDecimals() + " decimal places.");
					else		//	ANS_WRONG
						messagePanel.insertRedText("Your value for the standard deviation is wrong!");
					messagePanel.insertText(" The exact standard deviation to " + getRequiredDecimals() + " decimals is " + new NumValue(exactSd, getRequiredDecimals()) + ".");
				}
				break;
		}
	}
	
	protected void insertAnswer(MessagePanel messagePanel, String varName) {
		if (hasOption("mean"))
			messagePanel.insertText("The exact mean of " + varName + " to " + getRequiredDecimals() + " decimals is " + new NumValue(exactMean, getRequiredDecimals()) + ".\n");
		if (hasOption("median"))
			messagePanel.insertText("The exact median of " + varName + " to " + getRequiredDecimals() + " decimals is " + new NumValue(exactMedian, getRequiredDecimals()) + ".\n");
		if (hasOption("sd"))
			messagePanel.insertText("The exact standard deviation of " + varName + " to " + getRequiredDecimals() + " decimals is " + new NumValue(exactSd, getRequiredDecimals()) + ".\n");
	}
	
	protected int getMessageHeight() {
		return 180;
	}
	
//-----------------------------------------------------------
	
	protected DataSet getData() {
		DataSet data = new DataSet();
		
		int noOfVars = Integer.parseInt(getParameter(N_VARIABLES_PARAM));
		Random generator = null;		//		All variables share the same random generator
		for (int i=0 ; i<noOfVars ; i++) {
			String varName = getParameter("x" + i + "VarName");
			String values = getParameter("x" + i + "Values");
			RandomisedNumVariable xVar = new RandomisedNumVariable(varName);
			xVar.readValues(values);
			data.addVariable("x" + i, xVar);
			if (generator == null)
				generator = xVar.getGenerator();
			else
				xVar.setGenerator(generator);
		}
		
		return data;
	}
	
	protected double getMeanAttempt() {
		return hasOption("mean") ? meanResultPanel.getAttempt().toDouble() : Double.NaN;
	}
	
	protected double getMedianAttempt() {
		return hasOption("median") ? medianResultPanel.getAttempt().toDouble() : Double.NaN;
	}
	
	protected double getSdAttempt() {
		return hasOption("sd") ? sdResultPanel.getAttempt().toDouble() : Double.NaN;
	}
	
	
//-----------------------------------------------------------
	
	protected int assessAnswer() {
		int requiredDecimals = getRequiredDecimals();
		double factor = 1.0;
		for (int i=0 ; i<requiredDecimals ; i++)
			factor *= 10.0;
		
		double meanAttempt = getMeanAttempt();
		double medianAttempt = getMedianAttempt();
		double sdAttempt = getSdAttempt();

		if (meanResultPanel != null && meanResultPanel.isClear())
			return ANS_INCOMPLETE;
		else if (sdResultPanel != null && sdResultPanel.isClear())
			return ANS_INCOMPLETE;
		else if (medianResultPanel != null && medianResultPanel.isClear())
			return ANS_INCOMPLETE;
		else if (sdAttempt < 0.0)
			return ANS_INVALID;
		else {
			if (hasOption("mean")) {
				if (Double.isNaN(meanAttempt) || Math.round(meanAttempt * factor) == Math.round(exactMean * factor))
					meanResult = ANS_CORRECT;
				else if (Math.round(meanAttempt * factor * 0.1) == Math.round(exactMean * factor * 0.1))
					meanResult = ANS_CLOSE;
				else
					meanResult = ANS_WRONG;
			}
			else
				meanResult = ANS_CORRECT;
			
			if (hasOption("median")) {
				if (Double.isNaN(medianAttempt) || Math.round(medianAttempt * factor) == Math.round(exactMedian * factor))
					medianResult = ANS_CORRECT;
				else if (Math.round(medianAttempt * factor * 0.1) == Math.round(exactMedian * factor * 0.1))
					medianResult = ANS_CLOSE;
				else
					medianResult = ANS_WRONG;
			}
			else
				medianResult = ANS_CORRECT;
			
			if (hasOption("sd")) {
				if (Double.isNaN(sdAttempt) || Math.round(sdAttempt * factor) == Math.round(exactSd * factor))
					sdResult = ANS_CORRECT;
				else if (Math.round(sdAttempt * factor * 0.1) == Math.round(exactSd * factor * 0.1))
					sdResult = ANS_CLOSE;
				else
					sdResult = ANS_WRONG;
			}
			else
				sdResult = ANS_CORRECT;
			
			if (meanResult == ANS_CORRECT && medianResult == ANS_CORRECT && sdResult == ANS_CORRECT)
				return ANS_CORRECT;
			else if (meanResult != ANS_WRONG && medianResult != ANS_WRONG && sdResult != ANS_WRONG)
					return ANS_CLOSE;
			else
				return ANS_WRONG;
		}
	}
	
	protected void giveFeedback() {
/*
		if (result == ANS_CORRECT || result == ANS_WRONG) {
			getCurrentView().setShow4s(true);
			getCurrentView().repaint();
		}
*/
	}
	
	protected void showCorrectWorking() {
		int requiredDecimals = getRequiredDecimals();
		if (hasOption("mean")) {
			NumValue correctMean = new NumValue(exactMean, requiredDecimals);
			meanResultPanel.showAnswer(correctMean);
		}
		if (hasOption("median")) {
			NumValue correctMedian = new NumValue(exactMedian, requiredDecimals);
			medianResultPanel.showAnswer(correctMedian);
		}
		if (hasOption("sd")) {
			NumValue correctSD = new NumValue(exactSd, requiredDecimals);
			sdResultPanel.showAnswer(correctSD);
		}
		
//		getCurrentView().setShow4s(true);
//		getCurrentView().repaint();
	}
	
	protected double getMark() {
		int ans = assessAnswer();
		return (ans == ANS_CORRECT) ? 1 : (ans == ANS_CLOSE) ? 0.5 : 0;
	}
}