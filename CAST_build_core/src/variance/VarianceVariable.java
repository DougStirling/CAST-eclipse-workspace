package variance;import dataView.*;public class VarianceVariable extends NumSummaryVariable {	private String sourceKey;	private int decimals;		public VarianceVariable(String theName, String sourceKey, int decimals) {		super(theName);		this.sourceKey = sourceKey;		this.decimals = decimals;	}		protected NumValue evaluateSummary(DataSet sourceData) {		int n = 0;		double sx = 0.0;		double sxx = 0.0;				NumVariable variable = (NumVariable)sourceData.getVariable(sourceKey);		ValueEnumeration e = variable.values();		while (e.hasMoreValues()) {			double x = e.nextDouble();			if (!Double.isNaN(x)) {				sx += x;				sxx += x * x;				n ++;			}		}		return new NumValue((sxx - sx * sx / n) / (n - 1), decimals);	}}