package randomStat;import dataView.*;import distn.*;import random.RandomMultinomial;public class CatFitSampleVariable extends CatSampleVariable {	private String fitDistnKey, propnDistnKey, approxDistnKey;	private DataSet sampleData;	private SummaryDataSet summaryData;		public CatFitSampleVariable(String theName, RandomMultinomial generator,																	DataSet sampleData, String fitDistnKey) {		super(theName, generator);		this.sampleData = sampleData;		this.fitDistnKey = fitDistnKey;	}		public void setPropnDistn(SummaryDataSet summaryData, String propnDistnKey,																					String approxDistnKey) {		this.summaryData = summaryData;		this.propnDistnKey = propnDistnKey;		this.approxDistnKey = approxDistnKey;	}		private void adjustDistns() {		int counts[] = getCounts();		int total = 0;		for (int i=0 ; i<counts.length ; i++)			total += counts[i];		double propn[] = new double[counts.length];		for (int i=0 ; i<counts.length ; i++)			propn[i] = counts[i] / (double)total;				CatDistnVariable fit = (CatDistnVariable)sampleData.getVariable(fitDistnKey);		fit.setProbs(propn);				BinomialDistnVariable propnDistn = (BinomialDistnVariable)summaryData.getVariable(propnDistnKey);		propnDistn.setProb(propn[0]);		propnDistn.setCount(total);				NormalDistnVariable approxDistn = (NormalDistnVariable)summaryData.getVariable(approxDistnKey);		approxDistn.setMean(propn[0]);		approxDistn.setSD(Math.sqrt(propn[0] * (1.0 - propn[0]) / total));	}		public long generateNextSample() {		long seed = super.generateNextSample();				adjustDistns();				return seed;	}		public boolean setSampleFromSeed(long newSeed) {		if (super.setSampleFromSeed(newSeed)) {			adjustDistns();			return true;		}		else			return false;	}}