package models;import dataView.*;abstract public class AdjustedSsqVariable extends NumFunctionVariable {	protected DataSet data;	protected String xKey, yKey;	protected double sourceExplSsq, sourceResidSsq;		private double rootTargetExplPropn, rootTargetResidPropn;	protected double yMean;		private int displayDecimals;	private boolean sourceInitialised = false;	//		initialisation when source variable changes		public AdjustedSsqVariable(String theName, DataSet data, String xKey, String yKey,																							int displayDecimals) {		super(theName);		this.data = data;		this.xKey = xKey;		this.yKey = yKey;		this.displayDecimals = displayDecimals;				initialiseXY();				rootTargetExplPropn = 1.0;		rootTargetResidPropn = 1.0;	}		protected boolean initialiseXY() {		if (sourceInitialised)			return false;				sourceInitialised = true;		return true;	}		public void setR2(double targetR2) {		double p1 = targetR2 * (sourceExplSsq + sourceResidSsq) / sourceExplSsq;		double p2 = (1.0 - targetR2) * (sourceExplSsq + sourceResidSsq) / sourceResidSsq;				setExplainedPropn(p1);		setResidPropn(p2);	}		public void setExplainedPropn(double p) {		rootTargetExplPropn = Math.sqrt(p);	}		public void setResidPropn(double p) {		rootTargetResidPropn = Math.sqrt(p);	}//--------------------------------------------------------		public boolean noteVariableChange(String key) {		if (xKey.equals(key) || yKey.equals(key)) {			sourceInitialised = false;			return true;		}		return false;	}		public int getMaxDecimals() {		return displayDecimals;	}		public int noOfValues() {		Variable xVar = (Variable)data.getVariable(xKey);		return xVar.noOfValues();	}		abstract protected double getFittedValue(Variable xVar, Value x);		public Value valueAt(int index) {		initialiseXY();				NumVariable yVariable = (NumVariable)data.getVariable(yKey);		NumValue yVal = (NumValue)yVariable.valueAt(index);		Variable xVariable = (Variable)data.getVariable(xKey);		Value xVal = xVariable.valueAt(index);				double fitted = getFittedValue(xVariable, xVal);		double resultVal = yMean + rootTargetExplPropn * (fitted - yMean)																		+ rootTargetResidPropn * (yVal.toDouble() - fitted);				NumValue result = new NumValue(resultVal, displayDecimals);		return result;	}}