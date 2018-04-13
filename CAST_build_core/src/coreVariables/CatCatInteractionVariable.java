package coreVariables;import dataView.*;public class CatCatInteractionVariable extends CatFunctionVariable {	private String xKey, zKey;	private CatVariable xVar,  zVar;	private int nXCats, nZCats;		public CatCatInteractionVariable(String theName, DataSet data, String xKey, String zKey) {		super(theName);		this.xKey = xKey;		this.zKey = zKey;		xVar = (CatVariable)data.getVariable(xKey);		zVar = (CatVariable)data.getVariable(zKey);		nXCats = xVar.noOfCategories();		nZCats = zVar.noOfCategories();				LabelValue labels[] = new LabelValue[(nXCats - 1) * (nZCats - 1) + 1];		labels[0] = new LabelValue("Baseline");		for (int i=1 ; i<nXCats ; i++)			for (int j=1 ; j<nZCats ; j++)				labels[(i - 1) * (nXCats - 1) + j] = new LabelValue(xVar.getLabel(i).toString()																							+ " * " + zVar.getLabel(j).toString());		setLabels(labels);	}		public CatCatInteractionVariable(String theName, DataSet data, String xKey, String zKey,																								CatCatInteractionVariable labelsVar) {		super(theName);								//	uses exactly same cat labels		this.xKey = xKey;		this.zKey = zKey;		xVar = (CatVariable)data.getVariable(xKey);		zVar = (CatVariable)data.getVariable(zKey);		nXCats = xVar.noOfCategories();		nZCats = zVar.noOfCategories();				Value labels[] = new Value[(nXCats - 1) * (nZCats - 1) + 1];		for (int i=0 ; i<labels.length ; i++)			labels[i] = labelsVar.getLabel(i);		setLabels(labels);	}		public Value getLabel(int xCat, int zCat) {		if (xCat * zCat == 0)			return getLabel(0);		else			return getLabel((xCat - 1) * (nXCats - 1) + zCat);	}	//--------------------------------------------------------		public boolean noteVariableChange(String key) {		return xKey.equals(key) || zKey.equals(key);	}//--------------------------------------------------------		public int noOfValues() {		return xVar.noOfValues();	}		public Value valueAt(int index) {		nXCats = xVar.noOfCategories();		int xCat = xVar.getItemCategory(index);		int zCat = zVar.getItemCategory(index);		if (xCat * zCat == 0)			return getLabel(0);		else			return getLabel(xCat, zCat);	}}