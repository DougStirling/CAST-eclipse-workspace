package loess;import java.util.*;import dataView.*;import axis.*;public class TransAxisLabelEnumeration implements Enumeration {	private NumValue stepLabel;	private double step;	private double minPower, maxPower;	double nextPrintVal;		public TransAxisLabelEnumeration(int[] powerIndex, int[] stepIndex,										double minPower, double maxPower, int currentPowerIndex) {		this.minPower = minPower;		this.maxPower = maxPower;		int i = 0;		while (i < powerIndex.length - 1 && powerIndex[i+1] <= currentPowerIndex)			i++;				int currentStep = stepIndex[i];						int power = currentStep / 3;		int stepType = currentStep % 3;		if (stepType < 0) {			stepType += 3;			power --;		}		step = (stepType == 0) ? 1.0 : (stepType == 1) ? 2.0 : 5.0;		if (power > 0)			for (int j=0 ; j<power ; j++)				step *= 10.0;		else if (power < 0)			for (int j=0 ; j<-power ; j++)				step *= 0.1;		int stepDecimals = Math.max(-power, 0);				nextPrintVal = Math.floor(minPower / step) * step;		stepLabel = new NumValue(nextPrintVal, stepDecimals);	}		public boolean hasMoreLabels() {		return nextPrintVal <= maxPower;	}		public AxisLabel nextLabel() {		if (hasMoreLabels()) {			stepLabel.setValue(nextPrintVal);			double position = (nextPrintVal - minPower) / (maxPower - minPower);			AxisLabel nextLabel = new AxisLabel(stepLabel, position);			nextPrintVal += step;						return nextLabel;		}		else			return null;	}	public boolean hasMoreElements() {		return hasMoreLabels();	}	public Object nextElement() {		return nextLabel();	}}