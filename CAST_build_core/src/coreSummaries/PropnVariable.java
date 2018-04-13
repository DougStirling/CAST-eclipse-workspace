package coreSummaries;import dataView.*;public class PropnVariable extends NumSummaryVariable {	private String sourceKey;	private int decimals;		public PropnVariable(String theName, String sourceKey, int decimals) {		super(theName);		this.sourceKey = sourceKey;		this.decimals = decimals;	}		protected NumValue evaluateSummary(DataSet sourceData) {		CatVariable variable = (CatVariable)sourceData.getVariable(sourceKey);		int count[] = variable.getCounts();		int total = 0;		for (int i=0 ; i<count.length ; i++)			total += count[i];				NumValue result = new NumValue(count[0] / (double)total, decimals);		return result;	}}