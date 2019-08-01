package exerciseSDProg;

import java.awt.*;
import javax.swing.*;

import dataView.*;
import exercise2.*;
import utils.*;



public class CalcConditMeanSDApplet extends CalcMeanSDApplet {
	
	static final private Color kDarkBlue = new Color(0x000099);
	
	protected XLabel conditLabel;
	private NumValue conditValue;
	private boolean highNotLow;
	
	protected boolean selected[];
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("conditVariableIndex", "int");
		registerParameter("conditPropn", "const");
		registerParameter("conditVarName", "string");
	}
	
	protected int getConditVariableIndex() {
		return getIntParam("conditVariableIndex");
	}
	
	protected double getConditPropn() {
		return getDoubleParam("conditPropn");
	}
	
	protected String getConditVarName() {
		return getStringParam("conditVarName");
	}
	
	
	public boolean hasOption(String optionName) {
		if (optionName.equals("median"))			//		does not allow median
			return false;
		else
			return super.hasOption(optionName);
	}

	
//-----------------------------------------------------------
	
	protected XPanel getWorkingPanels(DataSet data) {
		XPanel thePanel = super.getWorkingPanels(data);
		
			XLabel textLabel = new XLabel("Distribution for ALL individuals", XLabel.LEFT, this);
			textLabel.setFont(getBigBoldFont());
			textLabel.setForeground(kDarkBlue);
		thePanel.add("North", textLabel);
		
		return thePanel;
	}
	
	protected void addSummaryResultPanels(XPanel bottomPanel) {
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
		
			conditLabel = new XLabel("", XLabel.LEFT, this);
			conditLabel.setFont(getBigBoldFont());
			conditLabel.setForeground(kDarkBlue);
		bottomPanel.add(conditLabel);
		
		super.addSummaryResultPanels(bottomPanel);
	}
	
	protected void setDisplayForQuestion() {
		super.setDisplayForQuestion();
		
		String varName = getVarName();
		varName = varName.substring(0, 1).toUpperCase() + varName.substring(1);
		conditLabel.setText(varName + " of individuals whose " + conditString() + ":");
		data.setSelection(selected);
	}
	
	protected String conditString() {
		return getConditVarName() + " is " + conditValue + (highNotLow ? " or higher" : " or lower");
	}
	
	protected void setDataForQuestion() {
		NumVariable selectedVar = (NumVariable)data.getVariable("x" + getVariableIndex());
		NumVariable conditVar = (NumVariable)data.getVariable("x" + getConditVariableIndex());
		
		int sortedIndex[] = conditVar.getSortedIndex();
		double conditPropn = getConditPropn();
		highNotLow = conditPropn > 0;
		if (highNotLow)
			conditPropn = (1.0 - conditPropn);
		else
			conditPropn = -conditPropn;
		int cutoffIndex = (int)Math.round(sortedIndex.length * conditPropn);
		conditValue = (NumValue)conditVar.valueAt(sortedIndex[cutoffIndex]);
		
		double sx = 0.0, sxx = 0.0;
		int n = 0;
		selected = new boolean[selectedVar.noOfValues()];
		for (int i=0 ; i<selectedVar.noOfValues() ; i++) {
			double x = selectedVar.doubleValueAt(i);
			double conditX = conditVar.doubleValueAt(i);
			selected[i] = highNotLow ? (conditX >= conditValue.toDouble()) : (conditX <= conditValue.toDouble());
			if (selected[i]) {
				n++;
				sx += x;
				sxx += x * x;
			}
		}
		exactMean = hasOption("mean") ? sx / n : Double.NaN;
		exactSd = hasOption("sd") ? Math.sqrt((sxx - exactMean * sx) / (n - 1)) : Double.NaN;
	}
	
	
//-----------------------------------------------------------
	
	protected void insertMessageContent(MessagePanel messagePanel) {
		if (result == ANS_UNCHECKED)
			messagePanel.insertText("Find the summary statistics for the individuals whose " + conditString() + " using a statistical program.");
		else
			super.insertMessageContent(messagePanel);
	}
	
	protected int getMessageHeight() {
		return 120;
	}
	
//-----------------------------------------------------------
	
	protected DataSet getData() {
		DataSet data = new DataSet();
		
		int noOfVars = Integer.parseInt(getParameter(N_VARIABLES_PARAM));
		for (int i=0 ; i<noOfVars ; i++) {
			String varName = getParameter("x" + i + "VarName");
			String values = getParameter("x" + i + "Values");
			NumVariable xVar = new NumVariable(varName);
			xVar.readValues(values);
			data.addVariable("x" + i, xVar);
		}

		return data;
	}
}