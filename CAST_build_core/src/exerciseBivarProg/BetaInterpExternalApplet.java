package exerciseBivarProg;

import java.awt.*;

import dataView.*;
import exercise2.*;
import models.*;
import random.*;
import linMod.*;


public class BetaInterpExternalApplet extends BetaInterpInterpApplet {
	
	private ExportDataButton exportButton;
	
	private NumValue lsIntercept, lsSlope;
	
//-----------------------------------------------------------

	protected void registerExtraParameterTypes() {
		registerParameter("count", "int");
		registerParameter("xMean", "const");
		registerParameter("xSd", "const");
		registerParameter("xDecimals", "int");
		registerParameter("yDecimals", "int");
		registerParameter("modelSlope", "const");
		registerParameter("modelIntercept", "const");
		registerParameter("modelErrorSd", "const");
		registerParameter("dataName", "string");
		registerParameter("interceptDecimals", "int");
		registerParameter("slopeDecimals", "int");
	}
	
	protected NumValue getSlope() {
		return lsSlope;
	}
	
	protected NumValue getIntercept() {
		return lsIntercept;
	}
	
	private double getModelSlope() {
		return getDoubleParam("modelSlope");
	}
	
	private double getModelIntercept() {
		return getDoubleParam("modelIntercept");
	}
	
	private double getErrorSd() {
		return getDoubleParam("modelErrorSd");
	}
	
	private String getDataName() {
		return getStringParam("dataName");
	}
	
	private int getCount() {
		return getIntParam("count");
	}
	
	private double getXMean() {
		return getDoubleParam("xMean");
	}
	
	private double getXSd() {
		return getDoubleParam("xSd");
	}
	
	private int getXDecimals() {
		return getIntParam("xDecimals");
	}
	
	private int getYDecimals() {
		return getIntParam("yDecimals");
	}
	
	private int getInterceptDecimals() {
		return getIntParam("interceptDecimals");
	}
	
	private int getSlopeDecimals() {
		return getIntParam("slopeDecimals");
	}
	
	
//-----------------------------------------------------------

	
	protected XPanel equationPanel(DataSet data) {
		XPanel buttonPanel = new XPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

			String[] keys = {"x", "y"};
			exportButton = new ExportDataButton("Export Data", data, keys, null, this);
		buttonPanel.add(exportButton);
		return buttonPanel;
	}
	
	protected void setEquationForQuestion() {
		exportButton.setDataName(getDataName());
	}
	
	protected void setDataForQuestion() {
		RandomNormal xGenerator = new RandomNormal(getCount(), getXMean(), getXSd(), 2.0);
		double vals[] = xGenerator.generate();
		double factor = Math.pow(10.0,  getXDecimals());
		for (int i=0 ; i<vals.length ; i++)
			vals[i] = Math.round(vals[i] * factor) / factor;
		NumVariable xVar = (NumVariable)data.getVariable("x");
		xVar.setValues(vals);
		xVar.setDecimals(getXDecimals());
		xVar.name = getXVarName();
		
		LinearModel yModel = (LinearModel)data.getVariable("model");
		yModel.setIntercept(new NumValue(getModelIntercept()));
		yModel.setSlope(new NumValue(getModelSlope()));

		RandomNormal errorGenerator = new RandomNormal(getCount(), 0.0, getErrorSd(), 2.0);
		double errorVals[] = errorGenerator.generate();
		NumVariable error = (NumVariable)data.getVariable("error");
		error.setValues(errorVals);
		
		ResponseVariable yData = (ResponseVariable)data.getVariable("y");
		yData.name = getYVarName();
		yData.setDecimals(getYDecimals());
		
		LSEstimate lsParams = new LSEstimate(data, "x", "y");
		lsIntercept = new NumValue(lsParams.getIntercept(), getInterceptDecimals());
		lsSlope = new NumValue(lsParams.getSlope(), getSlopeDecimals());
	}
	
	protected DataSet getData() {
		DataSet data = new DataSet();
		
			NumVariable xVar = new NumVariable("x");
		data.addVariable("x", xVar);
		
			LinearModel yModel = new LinearModel("model", data, "x");
		data.addVariable("model", yModel);
			
			NumVariable error = new NumVariable("error");
		data.addVariable("error", error);
			
			ResponseVariable yData = new ResponseVariable("y", data, "x", "error", "model", 10);
			yData.setRoundValues(true);
		data.addVariable("y", yData);
		return data;
	}
	
//-----------------------------------------------------------
	
	protected void insertLSEquation(MessagePanel messagePanel) {
		messagePanel.insertBoldBlueText(getLsEquation() + "\n");
	}
}