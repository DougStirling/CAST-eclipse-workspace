package sampDesign;import dataView.*;public class StratifiedMeanVariable extends NumSummaryVariable {	private String yKey;	private int decimals;		public StratifiedMeanVariable(String theName, String yKey, int decimals) {		super(theName);		this.yKey = yKey;		this.decimals = decimals;	}		protected NumValue evaluateSummary(DataSet sourceData) {		StratifiedSampleVariable y = (StratifiedSampleVariable)sourceData.getVariable(yKey);				return new NumValue(y.getPrediction(), decimals);	}}