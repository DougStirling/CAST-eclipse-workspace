package linMod;import dataView.*;public class OneResidualVariable extends NumSummaryVariable {	private String residKey;	private int residIndex;		public OneResidualVariable(String theName, String residKey, int residIndex) {		super(theName);		this.residKey = residKey;		this.residIndex = residIndex;	}		protected NumValue evaluateSummary(DataSet sourceData) {		NumVariable residVar = (NumVariable)sourceData.getVariable(residKey);		return (NumValue)residVar.valueAt(residIndex);	}}