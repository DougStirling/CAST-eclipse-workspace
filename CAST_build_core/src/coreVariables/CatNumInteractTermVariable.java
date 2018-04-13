package coreVariables;import dataView.*;public class CatNumInteractTermVariable extends NumFunctionVariable {	static final private NumValue kZero = new NumValue(0.0, 0);		static public void addInteractions(DataSet data, String catKey, String numKey) {		String keys[] = getInteractionKeys(data, catKey, numKey);		for (int i=0 ; i<keys.length ; i++)			data.addVariable(keys[i], new CatNumInteractTermVariable(keys[i], data,																																catKey, numKey, i + 1));	}		static public String[] getInteractionKeys(DataSet data, String catKey, String numKey) {		String prefix = data.getVariable(numKey).name + "*";		CatVariable catVar = (CatVariable)data.getVariable(catKey);		int nCats = catVar.noOfCategories();		String keys[] = new String[nCats - 1];		for (int i=1 ; i<nCats ; i++)			keys[i - 1] = prefix + catVar.getLabel(i).toString();		return keys;	}		private String catKey, numKey;	private int catLevel;	private CatVariable catVar;	private NumVariable numVar;		public CatNumInteractTermVariable(String theName, DataSet data, String catKey, String numKey,																																	int catLevel) {		super(theName);		this.catKey = catKey;		this.numKey = numKey;		this.catLevel = catLevel;		catVar = (CatVariable)data.getVariable(catKey);		numVar = (NumVariable)data.getVariable(numKey);	}	//--------------------------------------------------------		public boolean noteVariableChange(String key) {		return catKey.equals(key) || numKey.equals(key);	}//--------------------------------------------------------	public int getMaxDecimals() {		return numVar.getMaxDecimals();	}		public int noOfValues() {		return catVar.noOfValues();	}		public Value valueAt(int index) {		int xCat = catVar.getItemCategory(index);		return (xCat == catLevel) ? numVar.valueAt(index) : kZero;	}}