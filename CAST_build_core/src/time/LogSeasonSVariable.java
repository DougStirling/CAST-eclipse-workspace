package time;import dataView.*;public class LogSeasonSVariable extends SeasonSmoothVariable {	public LogSeasonSVariable(String theName, DataSet data, String sourceKey) {		super(theName, data, sourceKey);	}		protected ValueEnumeration getEnumeration() {		return new ExpEnumeration(super.getEnumeration());	}		protected ValueEnumeration sourceEnumeration(NumVariable sourceVar) {		return new LogEnumeration(super.sourceEnumeration(sourceVar));	}}