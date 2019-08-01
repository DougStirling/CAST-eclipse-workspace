package exerciseSDProg;

import dataView.*;



public class CalcFactorMeanSDApplet extends CalcConditMeanSDApplet {
	static final protected String N_NUM_VARIABLES_PARAM = "nNumVariables";		//		x1, x2, ..., x<nNumVariables>
	static final protected String N_CAT_VARIABLES_PARAM = "nCatVariables";		//		xf1, xf2, ..., xf<nNumVariables>
	
	protected void registerParameterTypes() {
		super.registerParameterTypes();
		registerParameter("conditCat", "int");
		registerParameter("conditCatName", "string");
	}
	
	protected int getConditCat() {
		return getIntParam("conditCat");
	}
	
	protected String getConditCatName() {
		return getStringParam("conditCatName");
	}

	
//-----------------------------------------------------------
	
	protected String[] getExportKeys() {
		int nNumVars = Integer.parseInt(getParameter(N_NUM_VARIABLES_PARAM));
		int nCatVars = Integer.parseInt(getParameter(N_CAT_VARIABLES_PARAM));
		String[] keys = new String[nNumVars + nCatVars];
		for (int i=0 ; i<nNumVars ; i++)
			keys[i] = "x" + i;
		for (int i=0 ; i<nCatVars ; i++)
			keys[nNumVars + i] = "xf" + i;
		return keys;
	}
	
	
	protected String conditString() {
		return getConditVarName() + " is " + getConditCatName();
	}
	
	protected void setDataForQuestion() {
		NumVariable selectedVar = (NumVariable)data.getVariable("x" + getVariableIndex());
		CatVariable conditVar = (CatVariable)data.getVariable("xf" + getConditVariableIndex());
		int catCategory = getConditCat();
		
		double sx = 0.0, sxx = 0.0;
		int n = 0;
		selected = new boolean[selectedVar.noOfValues()];
		for (int i=0 ; i<selectedVar.noOfValues() ; i++) {
			double x = selectedVar.doubleValueAt(i);
			selected[i] = conditVar.getItemCategory(i) == catCategory;
			if (selected[i]) {
				n++;
				sx += x;
				sxx += x * x;
			}
		}
		exactMean = hasOption("mean") ? sx / n : Double.NaN;
		exactSd = hasOption("sd") ? Math.sqrt((sxx - exactMean * sx) / (n - 1)) : Double.NaN;
	}
	
	
//-----------------------------------------------------------
	
	protected DataSet getData() {
		DataSet data = new DataSet();
		
		int nNumVars = Integer.parseInt(getParameter(N_NUM_VARIABLES_PARAM));
		for (int i=0 ; i<nNumVars ; i++) {
			String varName = getParameter("x" + i + "VarName");
			String values = getParameter("x" + i + "Values");
			NumVariable xVar = new NumVariable(varName);
			xVar.readValues(values);
			data.addVariable("x" + i, xVar);
		}
		
		int nCatVars = Integer.parseInt(getParameter(N_CAT_VARIABLES_PARAM));
		for (int i=0 ; i<nCatVars ; i++) {
			String varName = getParameter("xf" + i + "VarName");
			String values = getParameter("xf" + i + "Values");
			CatVariable xfVar = new CatVariable(varName);
			xfVar.readLabels(getParameter("xf" + i + "Labels"));
			xfVar.readValues(values);
			data.addVariable("xf" + i, xfVar);
		}

		return data;
	}
}