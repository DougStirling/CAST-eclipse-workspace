package exerciseNormalProg;

import java.awt.*;
import java.util.*;

import axis.*;
import dataView.*;
import distn.*;
import distribution.*;
import exercise2.*;

import exerciseNormal.*;


public class BinomialProbExternalApplet extends CoreBinomialProbApplet {
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("requiredDecimals", "int");
	}
	
	private int getRequiredDecimals() {
		return getIntParam("requiredDecimals");
	}
	
	
//-----------------------------------------------------------
	
	protected Dimension getMinMax(String minMaxString, int total) {				//		Used for creating random count
		double minCum = kMinCum;
		double maxCum = 1 - kMinCum;
		StringTokenizer st = new StringTokenizer(minMaxString, ":");
		if (st.hasMoreTokens())
			minCum = Double.parseDouble(st.nextToken());
		if (st.hasMoreTokens())
			maxCum = Double.parseDouble(st.nextToken());
		
		BinomialDistnVariable tempBin = createTempBinomial();
		double cum = 0;
		int xMin = -1;
		while (cum < minCum) {
			xMin ++;
			cum += tempBin.getScaledProb(xMin);
		}
		cum = 1;
		int xMax = total + 1;
		while (cum > maxCum) {
			xMax --;
			cum -= tempBin.getScaledProb(xMax);
		}
		return new Dimension(xMin, xMax);
	}
	
	private BinomialDistnVariable createTempBinomial() {
		BinomialDistnVariable tempBin = new BinomialDistnVariable("");
		tempBin.setCount(getNTrials());
		tempBin.setProb(getPSuccess().toDouble());
		return tempBin;
	}
	
//-----------------------------------------------------------
	
	
	protected DataSet getData() {
		DataSet data = new DataSet();
			BinomialDistnVariable binomialDistn = new BinomialDistnVariable("Binomial");
		data.addVariable("binomial", binomialDistn);
		
		return data;
	}
		
//-----------------------------------------------------------
	
	protected XPanel getWorkingPanels(DataSet data) {
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new BorderLayout(0, 0));
		return thePanel;
	}
	
	protected void setDisplayForQuestion() {
	}
	
	protected void setDataForQuestion() {
		BinomialDistnVariable binomialDistn = (BinomialDistnVariable)data.getVariable("binomial");
		binomialDistn.setCount(getNTrials());
		binomialDistn.setProb(getPSuccess().toDouble());

		DiscreteIntervalLimits limits = getLimits();
		double first = limits.getFirst() - 0.5;
		double last = limits.getLast() + 0.5;
		binomialDistn.setMinSelection(first);
		binomialDistn.setMaxSelection(last);
	}
	
//-----------------------------------------------------------
	
	protected void insertMessageContent(MessagePanel messagePanel) {
		DiscreteIntervalLimits limits = getLimits();
		
		switch (result) {
			case ANS_UNCHECKED:
				messagePanel.insertText("Use an external statistics program to find the required probability then type it into the text-edit box above.");
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
				messagePanel.insertText("The answer is " + limits.probAnswerString() + ", as highlighted in the bar chart.\n");
				addBinomialBarChart(messagePanel);
				break;
			case ANS_CORRECT:
				messagePanel.insertRedHeading("Correct!\n");
				messagePanel.insertText("This is " + limits.probAnswerString() + ".\n");
				addBinomialBarChart(messagePanel);
				break;
			case ANS_CLOSE:
				messagePanel.insertRedHeading("Close!\n");
				messagePanel.insertText("Your answer is close, but you should be able to get it exact to " + getRequiredDecimals() + " decimals.\n");
				addBinomialBarChart(messagePanel);
				break;
			case ANS_WRONG:
				messagePanel.insertRedHeading("Wrong!\n");
				messagePanel.insertRedText("You have given the wrong probability. Have you correctly included (or excluded) the extreme values of the interval?\n");
				addBinomialBarChart(messagePanel);
				break;
		}
	}
	
	private void addBinomialBarChart(MessagePanel messagePanel) {
		messagePanel.insertGraph(barchartPanel(data, this));
	}
	
	public String getAxisInfo() {
		int n = getNTrials();
		int step = (n <= 10) ? 1 : (n <= 20) ? 2 : (n <= 50) ? 5 : (n <= 100) ? 10 : (n <= 200) ? 20 : (n <= 500) ? 50 : 100;
		
		String axisInfo = "-0.5 " + n + ".5 0 " + step;
		if (n % step != 0)
			axisInfo += " " + n;
		
		return axisInfo;
	}
	
	private XPanel barchartPanel(DataSet data, XApplet applet) {
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new AxisLayout());
		thePanel.setOpaque(false);
		
		HorizAxis nAxis = new HorizAxis(this);
		nAxis.setAxisName("Count");
		nAxis.readNumLabels(getAxisInfo());
		nAxis.setOpaque(false);
		thePanel.add("Bottom", nAxis);
		
		thePanel.add("Center", barChartView(data, applet, nAxis));
		
		return thePanel;
	}
	
	protected DataView barChartView(DataSet data, XApplet applet, HorizAxis nAxis) {
		DiscreteProbView barChart = new DiscreteProbView(data, applet, "binomial", null, null, nAxis, DiscreteProbView.NO_DRAG);
		barChart.lockBackground(Color.white);
		return barChart;
	}
	
	protected int getMessageHeight() {
		return 420;
	}
	
//-----------------------------------------------------------
	
	
	protected double evaluateProbability(DiscreteIntervalLimits limits) {
		BinomialDistnVariable binomial = (BinomialDistnVariable)data.getVariable("binomial");
		int first = limits.getFirst();
		int last = limits.getLast();
		return binomial.getCumulativeProb(last + 0.5) - binomial.getCumulativeProb(first - 0.5);
	}
	
	protected boolean isClose(double attempt, double correct, DiscreteIntervalLimits limits) {
		double factor = Math.pow(10.0, getRequiredDecimals() - 1);
		return Math.round(correct * factor) == Math.round(attempt * factor);
	}
	
	protected boolean isCorrect(double attempt, double correct, DiscreteIntervalLimits limits) {
		double factor = Math.pow(10.0, getRequiredDecimals());
		return Math.round(correct * factor) == Math.round(attempt * factor);
	}
	
	protected void showCorrectWorking() {
		DiscreteIntervalLimits limits = getLimits();
		double prob = evaluateProbability(limits);
		NumValue probValue = new NumValue(prob, 4);
		
		resultPanel.showAnswer(probValue);
	}
}