package multiRegn;import dataView.*;import models.*;public class CoeffSEVariable extends NumSummaryVariable {	private String lsKey;	private int coeffIndex;	private int decimals;	private double fixedB[];		public CoeffSEVariable(String theName, String lsKey, String yKey, int coeffIndex,																										double[] fixedB, int decimals) {		super(theName);		this.lsKey = lsKey;		this.coeffIndex = coeffIndex;		this.fixedB = fixedB;		this.decimals = decimals;	}		public CoeffSEVariable(String theName, String lsKey, String yKey, int coeffIndex,																																			int decimals) {		this(theName, lsKey, yKey, coeffIndex, null, decimals);	}		protected NumValue evaluateSummary(DataSet sourceData) {		MultipleRegnModel ls = (MultipleRegnModel)sourceData.getVariable(lsKey);				double bVar[] = ls.getCoeffVariances("y", fixedB, false, 0.0);		double coeffSE = Math.sqrt(bVar[(coeffIndex + 1) * (coeffIndex + 2) / 2 - 1]);				return new NumValue(coeffSE, decimals);	}}