package designProg;import dataView.*;import exper.*;public class RandomisedApplet extends ExperDesignApplet {//	static final private String RANDOMISATION_PARAM = "randomisation";		protected FieldPlotsView getFieldView(DataSet data) {		FieldPlotsView theView = new FieldPlotsView(data, this, getParameter(FIELD_PICT_PARAM),								getParameter(TREAT_PICT_PARAM), FieldPlotsView.NO_EDIT, "treatment", "plotEffect");		theView.setShowPicture(true);		return theView;	}		protected TreatmentEffectView getEffectView(DataSet data) {		return new TreatmentEffectView(data, this, "treatment", "response",																				maxMean, TreatmentEffectView.SHOW_INTERVAL);	}		protected TreatmentBiasView getBiasView(DataSet data) {		return null;	}		protected void generateResponses() {		randomiseTreatments(0, plotsPerTreat);		data.variableChanged("treatment");				super.generateResponses();	}}