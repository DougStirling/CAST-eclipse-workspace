package sport;import java.util.*;import dataView.*;public class HandicapNormalVariable extends ScaledNormalDistnVariable {	private double targetVal, targetCumProb;	private NumValue maxHandicap;		private double lastTrueMean;		public HandicapNormalVariable(String theName, String scalingParam,																	String handicapParam) {		super(theName, scalingParam);				StringTokenizer st = new StringTokenizer(handicapParam);		targetVal = Double.parseDouble(st.nextToken());		targetCumProb = Double.parseDouble(st.nextToken());		maxHandicap = new NumValue(st.nextToken());	}		public void setMean(double newMean) {		lastTrueMean = newMean;		mean.setValue(targetVal);		setSD(findSD(newMean));		if (targetCumProb != 0.5)			mean.setValue(mean.toDouble() - stdQuantile(targetCumProb) * sd.toDouble());	}		public void setTargetCumProb(double targetCumProb) {		this.targetCumProb = targetCumProb;		setMean(lastTrueMean);	}		public double getHandicap() {		return lastTrueMean - mean.toDouble();	}		public NumValue getMaxHandicap() {		return maxHandicap;	}}