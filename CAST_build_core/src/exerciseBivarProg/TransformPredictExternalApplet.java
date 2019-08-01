package exerciseBivarProg;

import java.awt.*;

import dataView.*;
import utils.*;
import exercise2.*;
import models.*;

import regn.*;


public class TransformPredictExternalApplet extends TransformPredictApplet {
	
	private ExportDataButton exportButton;
	
//-----------------------------------------------------------
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("dataName", "string");
		registerParameter("xDecimals", "int");
		registerParameter("yDecimals", "int");
		registerParameter("requiredDecimals", "int");
	}
	
	private String getDataName() {
		return getStringParam("dataName");
	}
	
	private int getXDecimals() {
		return getIntParam("xDecimals");
	}
	
	private int getYDecimals() {
		return getIntParam("yDecimals");
	}
	
	private int getRequiredDecimals() {
		return getIntParam("requiredDecimals");
	}
	
	
//-----------------------------------------------------------
	
	protected XPanel getWorkingPanels(DataSet data) {
		XPanel thePanel = new XPanel();
		thePanel.setLayout(new BorderLayout(0, 0));
			
		thePanel.add("Center", getDataPanels(data));
		
			XPanel bottomPanel = new XPanel();
			bottomPanel.setLayout(new VerticalLayout(VerticalLayout.CENTER, VerticalLayout.VERT_CENTER, 10));
			
				exportButton = new ExportDataButton("Export Data", data, this);
			bottomPanel.add(exportButton);
			
				lsEqn = new LinearEquationView(data, this, "ls", "", "", null, null, null, null);
			bottomPanel.add(lsEqn);
		
		thePanel.add("South", bottomPanel);
		
		return thePanel;
	}
	
	protected void setDisplayForQuestion() {
		super.setDisplayForQuestion();
		lsEqn.show(false);
//		lsEqn.setOpaque(false);
		
		exportButton.setDataName(getDataName());
			String[] keys = {getXKey(), getYKey()};
		exportButton.setKeys(keys);
	}
	
	protected void clearTemplates() {
	}
	
	private String getXKey() {
		int xTransform = getXTransformType();
		return (xTransform == IDENTITY_TRANS) ? "x" : "exp(x)";
	}
	
	private String getYKey() {
		int yTransform = getYTransformType();
		return (yTransform == IDENTITY_TRANS) ? "y" : "exp(y)";
	}
	
	protected void setDataForQuestion() {
		super.setDataForQuestion();
		NumVariable yVar = (NumVariable)data.getVariable(getYKey());
		yVar.setDecimals(getYDecimals());
		yVar.name = getYVarName();
		NumVariable xVar = (NumVariable)data.getVariable(getXKey());
		xVar.setDecimals(getXDecimals());
		xVar.name = getXVarName();
	}
	
	
//-----------------------------------------------------------
	
	protected void insertMessageContent(MessagePanel messagePanel) {
		switch (result) {
			case ANS_UNCHECKED:
				messagePanel.insertText("Find the least squares line for the linearised relationship and use it to predict the "
										+ getYVarName() + ". Type your prediction into the text-edid box above, accurate to " + getRequiredDecimals() + " decimals.");
				break;
			case ANS_INCOMPLETE:
				messagePanel.insertRedHeading("Error!\n");
				messagePanel.insertRedText("You have not typed a value for the prediction.");
				break;
			case ANS_TOLD:
				messagePanel.insertRedHeading("Answer\n");
				double modelX = getXValue().toDouble();
				if (getXTransformType() == IDENTITY_TRANS)
					messagePanel.insertText("Since the explanatory variable in the LS equation is "
																	+ getXVarName() + ", there is no need to transform the value "
																	+ getXValue() + " before putting its value into the equation.");
				else {
					messagePanel.insertText("Since the explanatory variable in the LS equation is ln("
																	+ getXVarName() + "), the value ln(" + getXValue()
																	+ ") must be used in the equation.");
					modelX = Math.log(modelX);
				}
				
				LinearModel lsModel = (LinearModel)data.getVariable("ls");
				double prediction = lsModel.evaluateMean(modelX);
				int predictionDecimals = (getYTransformType() == LOG_TRANS) ? 4 : (getRequiredDecimals() + 2);
				NumValue predictionVal = new NumValue(prediction, predictionDecimals);
				messagePanel.insertText("\nFrom this, the LS equation gives the value " + predictionVal + ". ");
				
				if (getYTransformType() == IDENTITY_TRANS)
					messagePanel.insertText("Since the response appears untransformed in the LS equation, this is our final prediction.");
				else
					messagePanel.insertText("Since the response in this LS equation is ln(" + getYVarName()
								+ "), the predicted " + getYVarName() + " should be exp(" + predictionVal + ").");
				
				break;
			case ANS_CORRECT:
				messagePanel.insertRedHeading("Good!\n");
				messagePanel.insertText("You have correctly evaluated the prediction.");
				break;
			case ANS_CLOSE:
				messagePanel.insertRedHeading("Not close enough!\n");
				messagePanel.insertRedText("Your prediction is close but not exact to " + getRequiredDecimals() + " decimals.");
				insertHelpMessage(messagePanel);
				break;
			case ANS_WRONG:
				messagePanel.insertRedHeading("Wrong!\n");
				messagePanel.insertRedText("You have not correctly predicted " + getYVarName() + ".");
				insertHelpMessage(messagePanel);
				break;
		}
	}
	
	protected int getMessageHeight() {
		return 160;
	}
	
//-----------------------------------------------------------
	
	private double getExact() {
		LinearModel lsModel = (LinearModel)data.getVariable("ls");
		double intercept = Double.parseDouble(lsModel.getIntercept().toString());
		double slope = Double.parseDouble(lsModel.getSlope().toString());
		
		double x = getXValue().toDouble();
		if (getXTransformType() == LOG_TRANS)
			x = Math.log(x);
		
		double prediction = intercept + slope * x;
		
		if (getYTransformType() == LOG_TRANS)
			prediction = Math.exp(prediction);
		
		return prediction;
	}
	
	protected int assessAnswer() {
		if (resultPanel.isClear())
			return ANS_INCOMPLETE;
		else {
			double attempt = getAttempt();
			double exact = getExact();
			double factor = Math.pow(10.0,  getRequiredDecimals());
			if (Math.round(attempt * factor) == Math.round(exact * factor))
				return ANS_CORRECT;
			else {
				factor = factor / 10.0;
				if (Math.round(attempt * factor) == Math.round(exact * factor))
					return ANS_CLOSE;
				else
					return ANS_WRONG;
			}
		}
	}
	
	protected void giveFeedback() {
	}
	
	protected void showCorrectWorking() {
		lsEqn.show(true);
		
		NumValue exactVal = new NumValue(getExact(), getRequiredDecimals());
		resultPanel.showAnswer(exactVal);
	}
	
}