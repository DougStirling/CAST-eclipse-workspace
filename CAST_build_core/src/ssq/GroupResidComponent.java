package ssq;import java.awt.*;import dataView.*;import models.*;public class GroupResidComponent extends CoreComponentVariable {		static final public Color kGroupColor[] = {Color.blue, Color.red, new Color(0x006600)};		private String xKey;	private int groupIndex;		private double groupMean;		public GroupResidComponent(String theName, DataSet data, String xKey, String yKey,													String modelKey, int groupIndex, int decimals) {		super(theName, data, yKey, modelKey, decimals);		this.groupIndex = groupIndex;		this.xKey = xKey;	}//--------------------------------------------------------		public void getGroupMean() {		if (!foundYMean) {			CatVariable xVar = (CatVariable)data.getVariable(xKey);			NumVariable yVar = (NumVariable)getVariable(yKey);			double sumY = 0.0;			int n = 0;			ValueEnumeration xe = xVar.values();			ValueEnumeration ye = yVar.values();			while (xe.hasMoreValues() && ye.hasMoreValues()) {				double y = ye.nextDouble();				if (xVar.labelIndex(xe.nextValue()) == groupIndex) {					sumY += y;					n ++;				}			}			groupMean = sumY / n;			foundYMean = true;		}	}		public int noOfValues() {		return Math.min(((Variable)data.getVariable(xKey)).noOfValues(),									super.noOfValues());	}		public boolean noteVariableChange(String key) {		boolean changed = super.noteVariableChange(key);				if (xKey.equals(key))			changed = true;		return changed;	}		public Value valueAt(int index) {		CatVariable xVar = (CatVariable)data.getVariable(xKey);		int group = xVar.getItemCategory(index);		if (group != groupIndex)			return new NumValue(Double.NaN, decimals);				if (!foundYMean)			getGroupMean();				NumVariable yVar = (NumVariable)data.getVariable(yKey);		double y = yVar.doubleValueAt(index);		return new NumValue(y - groupMean, decimals);	}		public int getDF() {		CatVariable xVar = (CatVariable)data.getVariable(xKey);		int[] counts = xVar.getCounts();		return counts[groupIndex] - 1;	}}