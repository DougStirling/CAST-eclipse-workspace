package contin;import dataView.*;public class Chi2Variable extends NumSummaryVariable {	private ObsExpTableView oeView;	private int decimals;		public Chi2Variable(String theName, ObsExpTableView oeView, int decimals) {		super(theName);		this.oeView = oeView;		this.decimals = decimals;	}		protected NumValue evaluateSummary(DataSet sourceData) {		int[][] obs = oeView.getObservedArray();		double[][] exp = oeView.getExpectedArray();				double result = 0.0;		for (int j=0 ; j<obs.length ; j++)			for (int i=0 ; i<obs[0].length ; i++) {				double eij = exp[j][i];				double oij = obs[j][i];				double component = (oij - eij) * (oij - eij) / eij;				result += component;			}		return new NumValue(result, decimals);	}}