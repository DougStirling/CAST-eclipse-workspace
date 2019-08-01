package exerciseNumGraphProg;

import java.awt.*;
import java.util.*;

import dataView.*;
import axis.*;
import utils.*;
import random.*;
import coreVariables.*;
import exercise2.*;
import coreGraphics.*;
import formula.*;


public class FindTransformApplet extends ExerciseApplet {

	static final private double kMinWidthPropn = 0.9;
	static final private String kDefaultAxis = "1 2 3 1";			//  does not show labels
	
	static final private String[] kPowerString = {"1/y#sup2#", "1/y", "1/#sqrt#y", "log(y)" , "#sqrt#y", "y#sup2#"};
	static final private double[] kInversePower = {-0.5, -1.0, -2.0, Double.POSITIVE_INFINITY, 2.0, 0.5};
	
	private RandomNormal generator;
	
	private XLabel rawTitleLabel, transformedTitleLabel;
	
	private XChoice transformChoice;
	
	private ExportDataButton exportButton;
	
//================================================
	
	protected void createDisplay() {
		setLayout(new BorderLayout(0, 10));
		
			questionPanel = new QuestionPanel(this);
		add("North", questionPanel);
		
		add("Center", getWorkingPanels(data));
				
			XPanel bottomPanel = new XPanel();
			bottomPanel.setLayout(new VerticalLayout(VerticalLayout.FILL, VerticalLayout.VERT_TOP, 4));
			
			bottomPanel.add(createMarkingPanel(NO_HINTS));
			
				XPanel messagePanel = new XPanel();
				messagePanel.setLayout(new FixedSizeLayout(100, getMessageHeight()));
					
					message = new ExerciseMessagePanel(this);
				messagePanel.add(message);
			bottomPanel.add(messagePanel);
		
		add("South", bottomPanel);
	}
	
//-----------------------------------------------------------
	protected void registerParameterTypes() {
		registerParameter("varName", "string");
		registerParameter("dataName", "string");
		registerParameter("count", "int");
		registerParameter("normalMeanSd", "string");			//  mean and sd for generating random normal (mean - 2sd should be at least 0.1)
		registerParameter("transformFactor", "const");				//  scaling applied to transformed random normal  
		registerParameter("powerIndex", "int");
		registerParameter("decimals", "int");
	}
	
	private int getCount() {
		return getIntParam("count");
	}
	
	private double getNormalMean() {
		String meanSdString =  getStringParam("normalMeanSd");
		StringTokenizer st = new StringTokenizer(meanSdString);
		return Double.valueOf(st.nextToken());
	}
	
	private double getNormalSd() {
		String meanSdString =  getStringParam("normalMeanSd");
		StringTokenizer st = new StringTokenizer(meanSdString);
		st.nextToken();
		return Double.valueOf(st.nextToken());
	}
	
	private double getTransformFactor() {
		return getDoubleParam("transformFactor");
	}
	
	private int getPowerIndex() {
		return getIntParam("powerIndex");
	}
	
	private double getInversePower() {				//  "power" is the power needed to make it symmetric; its inverse is what is used to generate the transformed variable
		return kInversePower[getPowerIndex()];
	}
	
	private String getVarName() {
		return getStringParam("varName");
	}
	
	private String getDataName() {
		return getStringParam("dataName");
	}
	
	private int getDecimals() {
		return getIntParam("decimals");
	}
	
	
//-----------------------------------------------------------
	
	protected XPanel getWorkingPanels(DataSet data) {
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new BorderLayout(0, 4));
		
			XPanel plotPanel = new XPanel();
			plotPanel.setLayout(new ProportionLayout(0.5, 10, ProportionLayout.VERTICAL));

				rawTitleLabel = new XLabel("", XLabel.LEFT, this);
			plotPanel.add(ProportionLayout.TOP, createPlot("yPowerPlot", rawTitleLabel));

				transformedTitleLabel = new XLabel("", XLabel.LEFT, this);
			plotPanel.add(ProportionLayout.BOTTOM, createPlot("yPlot", transformedTitleLabel));
		
		thePanel.add("Center", plotPanel);
		

			XPanel buttonPanel = new XPanel();
			buttonPanel.setLayout(new VerticalLayout(VerticalLayout.CENTER, VerticalLayout.VERT_CENTER, 10));
			
//				String[] exportKeys = {"y", "yPlot", "yPower", "yPowerPlot", "yRaw"};
//				exportButton = new ExportDataButton("Export Data", data, exportKeys, "", this);
				exportButton = new ExportDataButton("Export Data", data, "yRaw", "", this);
			buttonPanel.add(exportButton);
			
				transformChoice = new XChoice("Transformation to make more symmetric:", XChoice.HORIZONTAL, this);
				for (int i=0 ; i<kPowerString.length ; i++)
					transformChoice.addItem(MText.expandText(kPowerString[i]));
				registerStatusItem("transformChoice", transformChoice);
			buttonPanel.add(transformChoice);
			
		thePanel.add("South", buttonPanel);
		
		return thePanel;
	}
	
	protected XPanel createPlot(String yScaledKey, XLabel titleLabel) {
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new BorderLayout(0, 0));
		
			titleLabel.setFont(getBigBoldFont());
		thePanel.add("North", titleLabel);
		
			XPanel dotPanel = new XPanel();
			dotPanel.setLayout(new AxisLayout());
			
				HorizAxis dotAxis = new HorizAxis(this);
				dotAxis.readNumLabels(kDefaultAxis);
			dotPanel.add("Bottom", dotAxis);
			
				StackedDotPlotView dotPlot = new StackedDotPlotView(data, this, dotAxis);
				dotPlot.setActiveNumVariable(yScaledKey);
				dotPlot.lockBackground(Color.white);
			dotPanel.add("Center", dotPlot);
		thePanel.add("Center", dotPanel);
			
		return thePanel;
	}
	
	protected void setDisplayForQuestion() {
		rawTitleLabel.setText("Distribution of " + getVarName());
		transformedTitleLabel.setText("Distribution of transformed " + getVarName());
		
		exportButton.setDataName(getDataName());
	}
	
	protected void setDataForQuestion() {
		generator.setMean(getNormalMean());
		generator.setSD(getNormalSd());
		NumSampleVariable yVar = (NumSampleVariable)data.getVariable("y");
		yVar.setSampleSize(getCount());
		yVar.generateNextSample();
		
		StringTokenizer st = new StringTokenizer(kDefaultAxis);
		double axisMin = Double.parseDouble(st.nextToken());
		double axisMax = Double.parseDouble(st.nextToken());
		
		Random uniformGenerator = new Random(nextSeed());
		double minWidth = kMinWidthPropn * (axisMax - axisMin);
		double targetMin = axisMin + uniformGenerator.nextDouble() * (axisMax - axisMin - minWidth);
		double targetMax = targetMin + minWidth + uniformGenerator.nextDouble() * (axisMax - targetMin - minWidth);
		
		ScaledVariable yScaled = (ScaledVariable)data.getVariable("yPlot");
		scaleToMinMax(yVar, yScaled, targetMin, targetMax);
		
		PowerVariable yPowerVar = (PowerVariable)data.getVariable("yPower");		//  This is what is presented to the user as the raw data
		yPowerVar.setPower(getInversePower(), getDecimals());
		
		ScaledVariable yPowerScaled = (ScaledVariable)data.getVariable("yPowerPlot");
		if (getPowerIndex() <= 2)
			scaleToMinMax(yPowerVar, yPowerScaled, targetMax, targetMin);
		else
			scaleToMinMax(yPowerVar, yPowerScaled, targetMin, targetMax);

		ScaledVariable yRaw = (ScaledVariable)data.getVariable("yRaw");
		yRaw.setScale(0.0, getTransformFactor(), 9);
		yRaw.name = getVarName();
		
		data.variableChanged("y");			//  Should update both dot plots
		data.variableChanged("yPlot");
		data.variableChanged("yPowerPlot");
	}

	
	private void scaleToMinMax(NumVariable baseVar, ScaledVariable scaledVar, double targetMin, double targetMax) {
//		NumValue sortedY[] = baseVar.getSortedData();
//		double dataMin = sortedY[0].toDouble();
//		double dataMax = sortedY[sortedY.length - 1].toDouble();
		
		double dataMin = Double.POSITIVE_INFINITY, dataMax = Double.NEGATIVE_INFINITY;
		ValueEnumeration ye = baseVar.values();
		while (ye.hasMoreValues()) {
			double y = ye.nextDouble();
			if (y < dataMin)
				dataMin = y;
			if (y > dataMax)
				dataMax = y;
		}
		
		double factor = (targetMax - targetMin) / (dataMax - dataMin);
		double shift = targetMin - dataMin * factor;
		
		scaledVar.setScale(shift, factor, 9);
//		scaledVar.clearSortedValues();
	}
	
	
//-----------------------------------------------------------
	
	protected void insertMessageContent(MessagePanel messagePanel) {
		switch (result) {
			case ANS_UNCHECKED:
				messagePanel.insertText("Export the data and try different power transformations (including log) to find one that gives a fairly symmetric distribution matching the one shown above.");
				break;
//			case ANS_INCOMPLETE:
//				break;
			case ANS_TOLD:
				messagePanel.insertRedHeading("Answer\n");
				messagePanel.insertText("You need the transformation " + MText.expandText(kPowerString[getPowerIndex()]) + " to make the distribution fairly symmetric (as shown above).");
				break;
			case ANS_CORRECT:
				messagePanel.insertRedHeading("Good!\n");
				messagePanel.insertText("Your answer is correct.");
				break;
			case ANS_WRONG:
				messagePanel.insertRedHeading("Wrong!\n");
				messagePanel.insertRedText("No. You need the transformation " + MText.expandText(kPowerString[getPowerIndex()]) + " to make the distribution fairly symmetric (as shown above).");
				break;
		}
	}
	
	protected int getMessageHeight() {
		return 100;
	}
	
//-----------------------------------------------------------
	
	protected DataSet getData() {
		DataSet data = new DataSet();

			generator = new RandomNormal(1, 0, 1.0, 2.0);		//		Mean and SD are changed for each new exercise
			generator.setNeatening(0.3);
			generator.setSeed(nextSeed());
		
			NumSampleVariable baseVar = new NumSampleVariable("y", generator, 9);
			baseVar.generateNextSample();
		data.addVariable("y", baseVar);
		
			ScaledVariable yPlotVar = new ScaledVariable("yPlot", baseVar, "y", 0.0, 1.0, 9);		//  Scaled version is only for plotting without axis
		data.addVariable("yPlot", yPlotVar);
		
			PowerVariable yPower = new PowerVariable("yPower", baseVar, 1.0, 9);		//  initially identity power
		data.addVariable("yPower", yPower);
		
			ScaledVariable yPowerPlot = new ScaledVariable("yPowerPlot", yPower, "yPower", 0.0, 1.0, 9);		//  Scaled version is only for plotting without axis
		data.addVariable("yPowerPlot", yPowerPlot);
		
		ScaledVariable yRaw = new ScaledVariable("yRaw", yPower, "yPower", 0.0, 1.0, 9);		//  Displayed to user as "raw" data
	data.addVariable("yRaw", yRaw);
		
		return data;
	}
	
	public void setFixedQuestionSeed(long seed) {
		super.setFixedQuestionSeed(seed);
		generator.setSeed(nextSeed());
	}
	
	
//-----------------------------------------------------------
	
	protected int assessAnswer() {
		if (transformChoice.getSelectedIndex() == getPowerIndex())
			return ANS_CORRECT;
		else
			return ANS_WRONG;
	}
	
	protected void giveFeedback() {
	}
	
	protected void showCorrectWorking() {
		transformChoice.select(getPowerIndex());
	}
	
	protected double getMark() {
		return (result == ANS_CORRECT) ? 1 : 0;
	}
	
	
//-----------------------------------------------------------
	
	@SuppressWarnings("deprecation")
	public boolean action(Event evt, Object what) {
		if (evt.target == transformChoice) {
			noteChangedWorking();
			return false;
		}
		else
			return super.action(evt, what);
	}
}