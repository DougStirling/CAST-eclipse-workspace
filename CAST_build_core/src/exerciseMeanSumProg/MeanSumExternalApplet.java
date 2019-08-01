package exerciseMeanSumProg;

import java.awt.*;

import axis.AxisLayout;
import axis.HorizAxis;
import axis.VertAxis;
import coreGraphics.DistnDensityView;
import dataView.*;
import distn.*;
import exercise2.*;
import formula.*;
import exerciseNormal.*;
import exerciseNormalProg.*;


public class MeanSumExternalApplet extends CoreNormalProbApplet {
	static final private int MEAN_VALUE = 0;
	static final private int SUM_VALUE = 1;
	static final private int SINGLE_VALUE = 2;
	
	static final private Color kHighlightColor = new Color(0x3366FF);
	static final private Color kDimColor = new Color(0xCCCCCC);
	
	static final private String kZAxisInfo = "-3.5 3.5 -3 1";
	
	
//-----------------------------------------------------------
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("sumMeanType", "choice");
		registerParameter("requiredDecimals", "int");
		registerParameter("zDecimals", "int");
		registerParameter("decimalsForSd", "int");
	}
	
	private int getMeanSumType() {
		return getIntParam("sumMeanType");
	}
	
	private int getRequiredDecimals() {
		return getIntParam("requiredDecimals");
	}
	
	private int getZDecimals() {
		return getIntParam("zDecimals");
	}
	
	private int getSdDecimals() {
		return getIntParam("decimalsForSd");
	}
	
	
	protected NumValue getMean() {
		int sumMeanType = getMeanSumType();
		
		NumValue popnMean = super.getMean();
		if (sumMeanType == SUM_VALUE) {
			int n = getN();
			return new NumValue(popnMean.toDouble() * n, popnMean.decimals);
		}
		else
			return popnMean;
	}
	
	protected NumValue getSD() {
		int sumMeanType = getMeanSumType();
		
		NumValue popnSd = super.getSD();
		
		if (sumMeanType == SUM_VALUE) {
			int n = getN();
			double rootN = Math.sqrt(n);
			double sd = popnSd.toDouble() * rootN;
			return new NumValue(sd, getSdDecimals());
		}
		else if (sumMeanType == MEAN_VALUE) {
			int n = getN();
			double rootN = Math.sqrt(n);
			double sd = popnSd.toDouble() / rootN;
			return new NumValue(sd, getSdDecimals());
		}
		else
			return popnSd;
	}
	
//-----------------------------------------------------------
	
	protected XPanel getWorkingPanels(DataSet data) {
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new BorderLayout(0, 0));
		return thePanel;
	}
	
	protected void setDisplayForQuestion() {
		resultPanel.clear();
	}
	
	protected void setDataForQuestion() {
		NumValue mean = getMean();
		NumValue sd = getSD();
		
		NormalDistnVariable normalDistn = (NormalDistnVariable)data.getVariable("distn");
		normalDistn.setMean(mean.toDouble());
		normalDistn.setSD(sd.toDouble());
		normalDistn.setDecimals(mean.decimals, sd.decimals);
		
		NormalDistnVariable zDistn = (NormalDistnVariable)data.getVariable("z");
		NumValue xLowValue = getLimits().startVal;
		double xLow = (xLowValue == null) ? Double.NEGATIVE_INFINITY : xLowValue.toDouble();
		zDistn.setMinSelection((xLow - mean.toDouble()) / sd.toDouble());
		
		NumValue xHighValue = getLimits().endVal;
		double xHigh = (xHighValue == null) ? Double.POSITIVE_INFINITY : xHighValue.toDouble();
		zDistn.setMaxSelection((xHigh - mean.toDouble()) / sd.toDouble());
	}
	
	protected DataSet getData() {
		DataSet data = super.getData();
		
			NormalDistnVariable zDistn = new NormalDistnVariable("Z");
			zDistn.setMean(0.0);
			zDistn.setSD(1.0);
		data.addVariable("z", zDistn);
		
		return data;
	}
	
//-----------------------------------------------------------
	
	protected void insertMessageContent(MessagePanel messagePanel) {
		IntervalLimits limits = getLimits();
		NumValue lowLimit = limits.startVal;
		NumValue highLimit = limits.endVal;
		
		switch (result) {
			case ANS_UNCHECKED:
				messagePanel.insertText("Find the probability and give it to " + getRequiredDecimals() + " decimals.");
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
				insertAnswer(messagePanel, lowLimit, highLimit, true);
				break;
				
			case ANS_CORRECT:
				messagePanel.insertRedHeading("Good!\n");
				messagePanel.insertText("You have found the correct probability.");
				insertAnswer(messagePanel, lowLimit, highLimit, true);
				break;
				
			case ANS_CLOSE:
				messagePanel.insertRedHeading("Close!\n");
				messagePanel.insertText("However your answer is not exact to " + getRequiredDecimals() + " decimals.");
				insertAnswer(messagePanel, lowLimit, highLimit, true);
				break;
				
			case ANS_WRONG:
				messagePanel.insertRedHeading("Wrong!\n");
				insertAnswer(messagePanel, lowLimit, highLimit, false);
				break;
		}
	}
	
	protected int getMessageHeight() {
		return 480;
	}
	
//-----------------------------------------------------------
	
	private void insertAnswer(MessagePanel messagePanel, NumValue lowLimit, NumValue highLimit, boolean showAnswer) {
		FormulaContext stdContext = new FormulaContext(null, null, this);
		int meanSumType = getMeanSumType();
		switch (meanSumType) {
			case MEAN_VALUE:
				messagePanel.insertText("The sample mean of n = " + getN() + " values from a distribution with mean #mu# is approx normal with mean\n");
				break;
			case SUM_VALUE:
				messagePanel.insertText("The total of " + getN() + " sample values from a distribution with mean #mu# is approx normal with mean\n");
				break;
			case SINGLE_VALUE:
				messagePanel.insertText("A single value from a normal population from a distribution with mean #mu# is normal with mean\n");
				break;
		}
		messagePanel.setAlignment(MessagePanel.CENTER_ALIGN);
		messagePanel.insertFormula(getMeanFormula(stdContext, showAnswer));
		messagePanel.insertText("\n");
		messagePanel.setAlignment(MessagePanel.LEFT_ALIGN);

		messagePanel.insertText("and standard deviation\n");
		messagePanel.setAlignment(MessagePanel.CENTER_ALIGN);
		messagePanel.insertFormula(getSdFormula(stdContext, showAnswer));
		messagePanel.insertText("\n");
		messagePanel.setAlignment(MessagePanel.LEFT_ALIGN);
		
		if (showAnswer) {
			if (lowLimit != null && highLimit != null) {
				messagePanel.insertText("The answer is the probability between the z-scores\n");
				messagePanel.setAlignment(MessagePanel.CENTER_ALIGN);
				messagePanel.insertFormula(getZFormula(lowLimit, "#sub1#", stdContext));
				messagePanel.insertText("\n");
				messagePanel.setAlignment(MessagePanel.LEFT_ALIGN);
				messagePanel.insertText("and\n");
				messagePanel.setAlignment(MessagePanel.CENTER_ALIGN);
				messagePanel.insertFormula(getZFormula(highLimit, "#sub2#", stdContext));
				messagePanel.insertText("\n");
				messagePanel.setAlignment(MessagePanel.LEFT_ALIGN);
			}
			else if (lowLimit == null) {
				messagePanel.insertText("The answer is the probability below the z-score\n");
				messagePanel.setAlignment(MessagePanel.CENTER_ALIGN);
				messagePanel.insertFormula(getZFormula(highLimit, "", stdContext));
				messagePanel.insertText("\n");
				messagePanel.setAlignment(MessagePanel.LEFT_ALIGN);
			}
			else {			//  highLimit == null
				messagePanel.insertText("The answer is the probability above the z-score\n");
				messagePanel.setAlignment(MessagePanel.CENTER_ALIGN);
				messagePanel.insertFormula(getZFormula(lowLimit, "", stdContext));
				messagePanel.insertText("\n");
				messagePanel.setAlignment(MessagePanel.LEFT_ALIGN);
			}
			messagePanel.insertText("This probability is the highlighted area below:\n");
			messagePanel.insertGraph(displayPanel(data));
		}
		else {
			if (lowLimit != null && highLimit != null)
				messagePanel.insertText("Translate the cutoff values " + lowLimit + " and " + highLimit + " into z-scores using this mean and standard deviation then find the answer using standard normal probabilities.\n");
			else if (lowLimit == null)
				messagePanel.insertText("Translate the cutoff value " + highLimit + " into a z-score using this mean and standard deviation then find the answer using standard normal probabilities.\n");
			else
				messagePanel.insertText("Translate the cutoff value " + lowLimit + " into a z-score using this mean and standard deviation then find the answer using standard normal probabilities.\n");
		}
	}
	
	private XPanel displayPanel(DataSet data) {
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new AxisLayout());
		thePanel.setOpaque(false);
		
			HorizAxis theHorizAxis = new HorizAxis(this);
			theHorizAxis.readNumLabels(kZAxisInfo);
			theHorizAxis.setAxisName("Z");
			theHorizAxis.setOpaque(false);
		thePanel.add("Bottom", theHorizAxis);
		
			VertAxis theProbAxis = new VertAxis(this);			//  Unfortunately DistnDensityView needs a vertical axis
			theProbAxis.readNumLabels("0 0.46 7 0.1");
			theProbAxis.setOpaque(false);
			theProbAxis.show(false);
		thePanel.add("Left", theProbAxis);
		
			DistnDensityView theView = new DistnDensityView(data, this, theHorizAxis, theProbAxis, "z");
			theView.setColors(kHighlightColor, kDimColor);
			theView.lockBackground(Color.white);
		thePanel.add("Center", theView);
		
		thePanel.setPreferredSize(new Dimension(getSize().width - 100, 200));
		
		return thePanel;
	}
	
	private MFormula getMeanFormula(FormulaContext context, boolean showAnswer) {
		NumValue popnMeanVal = super.getMean();
		int sumMeanType = getMeanSumType();
		MFormula leftFormula;
		if (sumMeanType == SUM_VALUE) {
			NumValue nVal = new NumValue(getN(), 0);
			MFormula nMu = new MBinary(MBinary.TIMES, new MConst(nVal, context), new MConst(popnMeanVal, context), context);
			leftFormula = new MBinary(MBinary.EQUALS, new MText("n#mu#", context), nMu, context);
		}
		else
			leftFormula = new MText("#mu#", context);
		
		if (showAnswer)
			return new MBinary(MBinary.EQUALS, leftFormula, new MConst(getMean(), context), context);
		else
			return leftFormula;
	}
	
	private MFormula getSdFormula(FormulaContext context, boolean showAnswer) {
		NumValue popnSdVal = super.getSD();
		int sumMeanType = getMeanSumType();
		MFormula leftFormula;
		if (sumMeanType == SUM_VALUE) {
			NumValue nVal = new NumValue(getN(), 0);
			MFormula rootNSigma = new MBinary(MBinary.TIMES, new MRoot(new MConst(nVal, context), context), new MConst(popnSdVal, context), context);
			leftFormula = new MBinary(MBinary.EQUALS, new MText("#sqrt#n#times##sigma#", context), rootNSigma, context);
		}
		else if (sumMeanType == MEAN_VALUE) {
			NumValue nVal = new NumValue(getN(), 0);
			MFormula sigmaOverRootN = new MRatio(new MConst(popnSdVal, context), new MRoot(new MConst(nVal, context), context), context);
			leftFormula = new MBinary(MBinary.EQUALS, new MRatio(new MText("#sigma#", context), new MText("#sqrt#n", context), context), sigmaOverRootN, context);
		}
		else			//  sumMeanType == SINGLE_VALUE
			leftFormula = new MText("#sigma#", context);
		
		if (showAnswer)
			return new MBinary(MBinary.EQUALS, leftFormula, new MConst(getSD(), context), context);
		else
			return leftFormula;
	}
	
	private MFormula getZFormula(NumValue x, String subscript, FormulaContext context) {
		MFormula leftFormula = new MText("z" + subscript, context);
		MFormula numerator = new MBinary(MBinary.MINUS, new MConst(x, context), new MConst(getMean(), context), context);
		MFormula middleFormula = new MRatio(numerator, new MConst(getSD(), context), context);
		
		NumValue zValue = new NumValue((x.toDouble() - getMean().toDouble()) / getSD().toDouble(), getZDecimals());
		return new MBinary(MBinary.EQUALS, new MBinary(MBinary.EQUALS, leftFormula, middleFormula, context), new MConst(zValue, context), context);
	}
	
//-----------------------------------------------------------
	
	protected int assessAnswer() {
		double attempt = getAttempt();
		
		if (resultPanel.isClear())
			return ANS_INCOMPLETE;
		else if(attempt < 0.0 || attempt > 1.0)
			return ANS_INVALID;
		else {
			IntervalLimits limits = getLimits();
			double correct = evaluateProbability(limits);
			
			double factor = Math.pow(10.0, getRequiredDecimals());
			if (Math.round(attempt * factor) == Math.round(correct * factor))
				return ANS_CORRECT;
			else {
				factor /= 10.0;
				if (Math.round(attempt * factor) == Math.round(correct * factor))
					return ANS_CLOSE;
				else
					return ANS_WRONG;
			}
		}
	}
	
	protected void giveFeedback() {
	}
	
	protected void showCorrectWorking() {
		IntervalLimits limits = getLimits();
		double prob = evaluateProbability(limits);
		NumValue probValue = new NumValue(prob, getRequiredDecimals());
		
		resultPanel.showAnswer(probValue);
	}
	
	protected double getMark() {
		int ans = assessAnswer();
		return (ans == ANS_CORRECT) ? 1 : (ans == ANS_CLOSE) ? 0.7 : 0;
	}
}