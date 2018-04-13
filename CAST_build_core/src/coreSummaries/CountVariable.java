package coreSummaries;import dataView.*;public class CountVariable extends NumSummaryVariable {	private String sourceKey;		public CountVariable(String theName, String sourceKey) {		super(theName);		this.sourceKey = sourceKey;	}		protected NumValue evaluateSummary(DataSet sourceData) {		CatVariable variable = (CatVariable)sourceData.getVariable(sourceKey);		int count[] = variable.getCounts();				NumValue result = new NumValue(count[0], 0);		return result;	}}