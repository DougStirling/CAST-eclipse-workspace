package distn;import dataView.*;public class SkewedDistnVariable extends ContinDistnVariable {	static final private double kSDMax = 3.0;		private double maxValue;	private double changeP, mean, lowSD, highSD;		public SkewedDistnVariable(String theName) {		super(theName);	}		public void setParams(String s) {		try {			maxValue = (new NumValue(s)).toDouble();			setChangeP(0.5);		} catch (Exception e) {			System.err.println("Bad parameters for skewed normal distn: " + s);		}				setMinSelection(mean);		setMaxSelection(mean);	}		public void setChangeP(double changeP) {		this.changeP = changeP;		mean = maxValue * changeP;		lowSD = mean / kSDMax;		highSD = (maxValue - mean) / kSDMax;	}	//-------------------------------------------------------------//	These functions do not work correctly//-------------------------------------------------------------		public NumValue getMean() {		return new NumValue(1.0);	}		public NumValue getSD() {		return new NumValue(1.0);	}		public void setMean(double newMean) {	}		public void setSD(double newSD) {	}	//-------------------------------------------------------------		public double getDensityFactor() {		return Math.sqrt(2.0 / Math.PI) / maxValue * kSDMax * 2.0;	}		public double getMaxScaledDensity() {		return 1.0;	}		public double getScaledDensity(double x) {		double z = (x < mean) ? (x - mean) / lowSD : (x - mean) / highSD;		return Math.exp(-0.5 * z * z);	}		public double getCumulativeProb(double v) {		if (v < mean) {			double z = (v - mean) / lowSD;			return NormalDistnVariable.stdCumProb(z) * changeP / 0.5;		}		else {			double z = (v - mean) / highSD;			return 1.0 + (1.0 - NormalDistnVariable.stdCumProb(z)) * (1.0 - changeP) / 0.5;		}	}		public double getQuantile(double prob) {		if (prob < changeP)			return mean + lowSD * NormalDistnVariable.stdQuantile(0.5 / changeP * prob);		else			return mean + highSD * NormalDistnVariable.stdQuantile(0.5 + 0.5 / (1.0 - changeP)																									* (prob - changeP));	}		public DistnInfo getDistnInfo() {		return new NormalInfo();	}		public double xToZ(double x) {		if (x < mean)			return (x - mean) / lowSD;		else			return (x - mean) / highSD;	}		public double zToX(double z) {		if (z < 0.0)			return mean + z * lowSD;		else			return mean + z * highSD;	}}