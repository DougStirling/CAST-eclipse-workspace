package distn;import dataView.*;abstract public class ContinDistnVariable extends DistnVariable {	public ContinDistnVariable(String theName) {		super(theName);	}		abstract public double getDensityFactor();	abstract public double getMaxScaledDensity();	abstract public double getScaledDensity(double x);										//		density(x) = getScaledDensity(x) * getDensityFactor()	abstract public double getQuantile(double p);		abstract public DistnInfo getDistnInfo();	abstract public double xToZ(double x);	abstract public double zToX(double z);}