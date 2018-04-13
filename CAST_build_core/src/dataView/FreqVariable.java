package dataView;import random.RandomInteger;public class FreqVariable extends Variable {	static final public boolean WITH_REPLACEMENT = true;	static final public boolean WITHOUT_REPLACEMENT = false;		private RandomInteger generator;		public FreqVariable(String theName, int noOfValues) {		super(theName);		generator = new RandomInteger(0, noOfValues - 1, 1);	}		public FreqVariable(String theName, int noOfValues, long randomSeed) {		super(theName);		generator = new RandomInteger(0, noOfValues - 1, 1, randomSeed);	}		protected Value valFromString(String s) {		return new FreqValue(s);	}		public void setValues(int values[]) {		setNoOfGroups(values.length);		for (int i=0 ; i<values.length ; i++)			setValueAt(new FreqValue(values[i]), i);	}		public void sample(int n, boolean withReplacement) {		if (withReplacement) {			clearCounts();			generator.setSampleSize(n);			int sample[] = generator.generate();			for (int i=0 ; i<sample.length ; i++)				((FreqValue)valueAt(sample[i])).intValue ++;		}		else {			int sampleLeft = n;			int popnLeft = noOfValues();			for (int i=0 ; i<noOfValues() ; i++) {				if ((sampleLeft > 0) && (sampleLeft >= popnLeft * generator.nextDouble())) {					((FreqValue)valueAt(i)).intValue = 1;					sampleLeft --;				}				else					((FreqValue)valueAt(i)).intValue = 0;				popnLeft --;			}		}			}		public void clearCounts() {		ValueEnumeration e = values();		while (e.hasMoreValues()) {			((FreqValue)e.nextValue()).intValue = 0;		}	}}