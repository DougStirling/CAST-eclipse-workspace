package exerciseNormalProg;

import java.awt.*;
import java.util.*;

import dataView.*;
import distn.*;
import exercise2.*;
import formula.*;


import exerciseNormal.*;


public class NormalApproxExternalApplet extends CoreBinomialProbApplet {
	
//-----------------------------------------------------------
		
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("requiredDecimals", "int");
		registerParameter("meanDecimals", "int");
		registerParameter("sdDecimals", "int");
	}


	public int getRequiredDecimals() {
		return getIntParam("requiredDecimals");
	}

	public int getMeanDecimals() {
		return getIntParam("meanDecimals");
	}

	public int getSdDecimals() {
		return getIntParam("sdDecimals");
	}

	public String getAxisInfo() {
		return null;		//	standard normal distn axis is default
	}
	
	
	protected Dimension getMinMax(String minMaxString, int total) {				//  needed for generating random count
		double p = getPSuccess().toDouble();
		int n = getNTrials();
		
		double mean = n * p;
		double sd = Math.sqrt(n * p * (1 - p));
		
		double minCum = kMinCum;
		double maxCum = 1 - kMinCum;
		StringTokenizer st = new StringTokenizer(minMaxString, ":");
		if (st.hasMoreTokens())
			minCum = Double.parseDouble(st.nextToken());
		if (st.hasMoreTokens())
			maxCum = Double.parseDouble(st.nextToken());
		
		double zMin = NormalTable.quantile(minCum);
		double zMax = NormalTable.quantile(maxCum);
		
		double xMin = mean + zMin * sd;
		double xMax = mean + zMax * sd;
		
		return new Dimension((int)Math.round(Math.ceil(xMin)), (int)Math.round(Math.floor(xMax)));
	}
	
//-----------------------------------------------------------
	
	
	protected DataSet getData() {
		DataSet data = new DataSet();
			NormalDistnVariable normalDistn = new NormalDistnVariable("Normal");
														//	default is N(0,1)
		data.addVariable("z", normalDistn);
		
		return data;
	}
		
//-----------------------------------------------------------
	
	protected XPanel getWorkingPanels(DataSet data) {
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new BorderLayout(0, 20));
		return thePanel;
	}
	
	protected void setDisplayForQuestion() {
		resultPanel.clear();
	}
	
	protected void setDataForQuestion() {
		NormalDistnVariable zDistn = (NormalDistnVariable)data.getVariable("z");
		zDistn.setMinSelection(Double.NEGATIVE_INFINITY);
		zDistn.setMaxSelection(0.0);
	}
	
//-----------------------------------------------------------
	
	protected void insertMessageContent(MessagePanel messagePanel) {
		DiscreteIntervalLimits limits = getLimits();
		
		switch (result) {
			case ANS_UNCHECKED:
				messagePanel.insertText("#bullet#  Find the parameters, #mu# and #sigma#, of the normal approximation.\n" 
										+ "#bullet#  Adjust the endpoint of the interval by #plusMinus#0.5 (continuity correction).\n"
										+ "#bullet#  Either find the probability using an external statistics program directly using this normal distribution, or\n"
										+ "#bullet#  Translate the endpoint of the interval into a z-score.\n"
										+ "#bullet#  Find the probability using a standard normal distribution and this z-score.");
				break;
			case ANS_INCOMPLETE:
				messagePanel.insertRedHeading("Error!\n");
				messagePanel.insertText("You must type a probability into the answer box above.");
				break;
			case ANS_INVALID:
				messagePanel.insertRedHeading("Error!\n");
				messagePanel.insertText("Probabilities cannot be less than zero or more than one.");
				break;
			case ANS_TOLD:
				messagePanel.insertRedHeading("Answer\n");
				insertWorking(messagePanel, limits, true);
				break;
			case ANS_CORRECT:
				messagePanel.insertRedHeading("Correct!\n");
				messagePanel.insertText("You have correctly evaluated the probability from the normal approximation.\n");
				insertWorking(messagePanel, limits, true);
				break;
			case ANS_CLOSE:
				messagePanel.insertRedHeading("Close!\n");
				messagePanel.insertText("Your answer is close but should be given exact to " + getRequiredDecimals() + " decimals.");
				insertWorking(messagePanel, limits, true);
				break;
			case ANS_WRONG:
				messagePanel.insertRedHeading("Not close enough!\n");
				insertWorking(messagePanel, limits, false);
				break;
		}
	}
	
	private MFormula meanFormula(boolean withAnswer) {
		FormulaContext stdContext = new FormulaContext(null, null, this);

		MFormula formula0 = new MText("#mu#", stdContext);
		MFormula formula1 = new MText("n#pi#", stdContext);
		MFormula leftFormula = new MBinary(MBinary.EQUALS, formula0, formula1, stdContext);
		if (withAnswer) {
			MFormula formula2 = new MBinary(MBinary.TIMES, new MConst(new NumValue(getNTrials(), 0), stdContext), new MConst(getPSuccess(), stdContext), stdContext);
			MFormula formula3 = new MConst(new NumValue(getNTrials() * getPSuccess().toDouble(), getMeanDecimals()), stdContext);
			MFormula rightFormula = new MBinary(MBinary.EQUALS, formula2, formula3, stdContext);
			return new MBinary(MBinary.EQUALS, leftFormula, rightFormula, stdContext);
		}
		else
			return leftFormula;
	}
	
	private MFormula sdFormula(boolean withAnswer) {
		FormulaContext stdContext = new FormulaContext(null, null, this);

		MFormula formula0 = new MText("#sigma#", stdContext);
		MFormula formula1 = new MRoot(new MText("n#pi#(1 #minus# #pi#)", stdContext), stdContext);
										NumValue pSuccess = getPSuccess();
										MFormula varFormula = new MBinary(MBinary.TIMES, new MBinary(MBinary.TIMES, new MConst(new NumValue(getNTrials(), 0), stdContext), new MConst(pSuccess, stdContext), stdContext),
																											new MConst(new NumValue((1 - pSuccess.toDouble()), pSuccess.decimals), stdContext), stdContext);
		MFormula leftFormula = new MBinary(MBinary.EQUALS, formula0, formula1, stdContext);
		
		if (withAnswer) {
			MFormula formula2 = new MRoot(varFormula, stdContext);
											double sd = Math.sqrt(getNTrials() * pSuccess.toDouble() * (1 - pSuccess.toDouble()));
			MFormula formula3 = new MConst(new NumValue(sd, getSdDecimals()), stdContext);
			
			MFormula rightFormula = new MBinary(MBinary.EQUALS, formula2, formula3, stdContext);
			return new MBinary(MBinary.EQUALS, leftFormula, rightFormula, stdContext);
		}
		else
			return leftFormula;
	}
	
	private MFormula zFormula(int sign, double limit, boolean withAnswer) {
		FormulaContext stdContext = new FormulaContext(null, null, this);

		MFormula formula0 = new MText("Z ", stdContext);
		MBinary diff = new MBinary(MBinary.MINUS, new MConst(new NumValue(limit, 1), stdContext), new MText("#mu#", stdContext),
				stdContext);
		MRatio formula1 = new MRatio(diff, new MText("#sigma#", stdContext), stdContext);
		MFormula leftFormula = new MBinary(sign, formula0, formula1, stdContext);

		if (withAnswer) {
			int n = getNTrials();
			double pSuccess = getPSuccess().toDouble();
			double mean = n * pSuccess;
			double sd = Math.sqrt(mean * (1 - pSuccess));
			double z = (limit - mean) / sd;
			
			MFormula formula2 = new MConst(new NumValue(z, 3), stdContext);
			return new MBinary(MBinary.EQUALS, leftFormula, formula2, stdContext);
		}
		else
			return leftFormula;
	}
	
	private void insertWorking(MessagePanel messagePanel, DiscreteIntervalLimits limits, boolean withAnswer) {
		messagePanel.insertText("#bullet#  This binomial distribution of X is approximately normal with\n");
		messagePanel.setAlignment(MessagePanel.CENTER_ALIGN);
		messagePanel.insertFormula(meanFormula(withAnswer));
		messagePanel.insertText("\n");
		messagePanel.insertFormula(sdFormula(withAnswer));
		messagePanel.insertText("\n");
		messagePanel.setAlignment(MessagePanel.LEFT_ALIGN);
		
		messagePanel.insertText("#bullet#  X being '" + limits + "' is equivalent to (X ");
		int first = limits.getFirst();
		int last = limits.getLast();
		String sign = (first <= 0) ? "< " : "> ";
		double limit = (first <= 0) ? (last + 0.5) : (first - 0.5);
		messagePanel.insertText(sign + new NumValue(limit, 1) + ")\n");
		
		messagePanel.insertText("#bullet#  This translates into\n");
		messagePanel.setAlignment(MessagePanel.CENTER_ALIGN);
		messagePanel.insertFormula(zFormula((first <= 0) ? MBinary.LESS_THAN : MBinary.GREATER_THAN, limit, withAnswer));
		messagePanel.insertText("\n");
		messagePanel.setAlignment(MessagePanel.LEFT_ALIGN);
		
		messagePanel.insertText("#bullet#  The probability for this is found using the standard normal distribution.");
	}
	
	protected int getMessageHeight() {
		return 300;
	}
	
//-----------------------------------------------------------
	
	protected double evaluateProbability(DiscreteIntervalLimits limits) {
		double p = getPSuccess().toDouble();
		int n = getNTrials();
		
		double mean = n * p;
		double sd = Math.sqrt(n * p * (1 - p));
		
		double first = limits.getFirst() - 0.5;
		double last = limits.getLast() + 0.5;
		
		double z1 = (first - mean) / sd;
		double z2 = (last - mean) / sd;
		
		return NormalTable.cumulative(z2) - NormalTable.cumulative(z1);
	}
	
	protected boolean isCorrect(double attempt, double correct, DiscreteIntervalLimits limits) {
		double factor = Math.pow(10.0,  getRequiredDecimals());
		return Math.round(attempt * factor) == Math.round(correct * factor);
	}
	
	protected boolean isClose(double attempt, double correct, DiscreteIntervalLimits limits) {
		double factor = Math.pow(10.0,  getRequiredDecimals() - 1);
		return Math.round(attempt * factor) == Math.round(correct * factor);
	}
	
	
	protected void showCorrectWorking() {
		DiscreteIntervalLimits limits = getLimits();
		double prob = evaluateProbability(limits);
		NumValue probValue = new NumValue(prob, 4);
		
		resultPanel.showAnswer(probValue);
	}
}