package linMod;import dataView.*;import distn.*;public class SlopeDistnVariable extends NormalDistnVariable {	private DataSet sourceData;	private String xKey, yKey;	private int decimals;	private boolean zeroMean;		private boolean initialised = false;		private NumValue mean = null;	private NumValue sd = null;	private int n;		public SlopeDistnVariable(String theName, DataSet sourceData, String xKey, String yKey,																				int decimals, boolean zeroMean) {		super(theName);		this.sourceData = sourceData;		this.xKey = xKey;		this.yKey = yKey;		this.decimals = decimals;		this.zeroMean = zeroMean;	}		public SlopeDistnVariable(String theName, DataSet sourceData, String xKey, String yKey,																										int decimals) {		this(theName, sourceData, xKey, yKey, decimals, false);	}		private void initialiseMeanSD() {		if (!initialised) {			NumVariable xVar = (NumVariable)sourceData.getVariable(xKey);			NumVariable yVar = (NumVariable)sourceData.getVariable(yKey);			ValueEnumeration xe = xVar.values();			ValueEnumeration ye = yVar.values();			double sxx = 0.0;			double sxy = 0.0;			double syy = 0.0;			double sx = 0.0;			double sy = 0.0;			n = 0;			while (xe.hasMoreValues() && ye.hasMoreValues()) {				double x = xe.nextDouble();				double y = ye.nextDouble();				sx += x;				sy += y;				sxx += x * x;				sxy += x * y;				syy += y * y;				n++;			}			double xMean = sx / n;			sxx -= sx * xMean;			double yMean = sy / n;			sxy -= sx * yMean;			syy -= sy * yMean;						double errorSsq = syy - sxy * sxy / sxx;						if (!zeroMean)				mean = new NumValue(sxy / sxx, decimals);			sd = new NumValue(Math.sqrt(errorSsq / ((n - 2) * sxx)), decimals);						initialised = true;		}	}		public NumValue getMean() {		initialiseMeanSD();		return (mean == null) ? new NumValue(0.0, decimals) : mean;	}		public NumValue getSD() {		initialiseMeanSD();		return sd;	}		public int getN() {		initialiseMeanSD();		return n;	}		public void resetSource() {		initialised = false;	}}