package distn;import java.util.*;import dataView.*;public class RectangularDistnVariable extends ContinDistnVariable {	static final private double kRootTwelfth = Math.sqrt(1.0 / 12.0);	protected NumValue minVal = new NumValue(0.0, 0);	protected NumValue maxVal = new NumValue(1.0, 0);		public RectangularDistnVariable(String theName) {		super(theName);	}		public void setParams(String s) {		try {			StringTokenizer params = new StringTokenizer(s);			minVal = new NumValue(params.nextToken());			maxVal = new NumValue(params.nextToken());			if (minVal.toDouble() >= maxVal.toDouble())				throw new Exception();		} catch (Exception e) {			System.err.println("Bad parameters for rectangular distn: " + minVal.toDouble()																												 + ", " + maxVal.toDouble());		}				setMinSelection(minVal.toDouble());		setMaxSelection(minVal.toDouble());	}		public NumValue getMean() {		return new NumValue((minVal.toDouble() + maxVal.toDouble()) * 0.5,																				Math.max(minVal.decimals, maxVal.decimals) + 1);	}		public NumValue getSD() {		return new NumValue((maxVal.toDouble() - minVal.toDouble()) * kRootTwelfth,																				Math.max(minVal.decimals, maxVal.decimals) + 8);	}		public NumValue getMin() {		return minVal;	}		public NumValue getMax() {		return maxVal;	}		public void setLimits(double newMin, double newMax) {		minVal.setValue(newMin);		maxVal.setValue(newMax);		setMinSelection(minVal.toDouble());		setMaxSelection(minVal.toDouble());	}		public void setDecimals(int decimals) {		minVal.decimals = decimals;		minVal.decimals = decimals;	}		public double getDensityFactor() {		return 1.0 / (maxVal.toDouble() - minVal.toDouble());	}		public double getMaxScaledDensity() {		return 1.0;	}		public double getScaledDensity(double x) {		return (x < minVal.toDouble() || x > maxVal.toDouble()) ? 0.0 : 1.0;	}		public double getCumulativeProb(double v) {		if (v < minVal.toDouble())			return 0.0;		else if (v > maxVal.toDouble())			return 1.0;		else			return (v - minVal.toDouble()) / (maxVal.toDouble() - minVal.toDouble());	}		public double getQuantile(double prob) {		return minVal.toDouble() + prob * (maxVal.toDouble() - minVal.toDouble());	}		public DistnInfo getDistnInfo() {		return new RectangularInfo();	}		public double xToZ(double x) {		return (x - minVal.toDouble()) / (maxVal.toDouble() - minVal.toDouble());	}		public double zToX(double z) {		return z * (maxVal.toDouble() - minVal.toDouble()) + minVal.toDouble();	}}