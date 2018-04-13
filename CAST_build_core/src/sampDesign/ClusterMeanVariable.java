package sampDesign;import dataView.*;public class ClusterMeanVariable extends NumSummaryVariable {	private String yKey;	private int decimals;		private double sampleValues[];		public ClusterMeanVariable(String theName, String yKey, int decimals) {		super(theName);		this.yKey = yKey;		this.decimals = decimals;	}		protected NumValue evaluateSummary(DataSet sourceData) {		ClusterSampleVariable y = (ClusterSampleVariable)sourceData.getVariable(yKey);				sampleValues = y.extractLastSample(sampleValues);				double sy = 0.0;		for (int i=0 ; i<sampleValues.length ; i++)			sy += sampleValues[i];				return new NumValue(sy / sampleValues.length, decimals);	}		public void recalculateMeans(DataSet sourceData) {		ClusterSampleVariable y = (ClusterSampleVariable)sourceData.getVariable(yKey);		for (int i=0 ; i<y.getNoOfClusters() ; i+=y.getClustersPerSample()) {			sampleValues = y.extractSample(i, sampleValues);						double sy = 0.0;			for (int j=0 ; j<sampleValues.length ; j++)				sy += sampleValues[j];						NumValue v = (NumValue)valueAt(i / y.getClustersPerSample());			v.setValue(sy / sampleValues.length);		}	}}