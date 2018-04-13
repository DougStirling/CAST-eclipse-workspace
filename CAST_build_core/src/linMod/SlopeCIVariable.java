package linMod;import dataView.*;import distn.TTable;import coreSummaries.*;public class SlopeCIVariable extends IntervalSummaryVariable {	private SummaryDataSet summaryData;	private String slopeDistnKey;	private int decimals;	private double tValue;		public SlopeCIVariable(String theName, double level, int df, SummaryDataSet summaryData,																			String slopeDistnKey, int decimals) {		super(theName);		this.tValue = TTable.quantile(0.5 + level * 0.5, df);		this.summaryData = summaryData;		this.slopeDistnKey = slopeDistnKey;		this.decimals = decimals;	}		protected IntervalValue evaluateSummary(DataSet sourceData) {		SlopeDistnVariable slopeDistn = (SlopeDistnVariable)summaryData.getVariable(slopeDistnKey);		//		int n = slopeDistn.getN();		double slope = slopeDistn.getMean().toDouble();		double slopeSD = slopeDistn.getSD().toDouble();				return new IntervalValue(slope, slopeSD, tValue, decimals);	}}