package matrixProg;import dataView.*;import matrix.*;public class FactorLinearPartApplet extends GenericLinearPartApplet {	protected CoreMatrixView createXMatrixView(DataSet data, ModelTerm[] xTerms) {		CoreMatrixView matrix = super.createXMatrixView(data, xTerms);		matrix.setGroupKey("x1");				return matrix;	}		protected ModelTerm[] findXTerms(DataSet data, String paramSymbol) {		return findOneFactorXTerms(data, paramSymbol);	}}