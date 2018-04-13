package dataView;import java.util.*;import java.awt.Graphics;public class NumVariable extends Variable {	public NumVariable(String theName) {		super(theName);	}		public NumVariable(String theName, boolean usesGroups) {		super(theName, usesGroups);	}		protected Value valFromString(String s) {		return new NumValue(s);	}		public void clearSortedValues() {		sortedData = null;		sortedIndex = null;	}		public void setValues(double values[]) {		setNoOfGroups(values.length);		for (int i=0 ; i<values.length ; i++)			setValueAt(new NumValue(values[i]), i);		clearSortedValues();	}		public void setDecimals(int decimals) {		ValueEnumeration e = values();		while (e.hasMoreValues()) {			((NumValue)e.nextValue()).decimals = decimals;		}	}		public double doubleValueAt(int index) {		return ((NumValue)valueAt(index)).toDouble();	}		public void scrollAddValues(double newVal[]) {		int noOfNewVals = newVal.length;		int noOfVals = noOfValues();		for (int i=0 ; i<noOfVals-noOfNewVals ; i++)			setValueAt(valueAt(i+noOfNewVals), i);		for (int i=0 ; i<noOfNewVals ; i++)			setValueAt(new NumValue(newVal[i]), noOfVals-noOfNewVals + i);		clearSortedValues();	}		public int getMaxDecimals() {		int maxDecimals = 0;		ValueEnumeration e = values();		while (e.hasMoreValues()) {			NumValue nextVal = (NumValue)e.nextValue();			if (nextVal.decimals > maxDecimals)				maxDecimals = nextVal.decimals;		}		return maxDecimals;	}		public void setMaxDecimals() {		setDecimals(getMaxDecimals());	}		public int getMaxLeftDigits() {		int maxLeft = 0;		ValueEnumeration e = values();		while (e.hasMoreValues()) {			NumValue nextVal = (NumValue)e.nextValue();			if (nextVal.leftDigits > maxLeft)				maxLeft = nextVal.leftDigits;		}		return maxLeft;	}		public int getMaxAlignedWidth(Graphics g, int decimals) {		int maxWidth = 0;		ValueEnumeration e = values();		while (e.hasMoreValues()) {			NumValue v = (NumValue)e.nextValue();			int valWidth = v.stringWidth(g, decimals);			if (valWidth > maxWidth)				maxWidth = valWidth;		}		return maxWidth;	}		private NumValue sortedData[];	private int sortedIndex[];					//	value with rank i has index sortedIndex[i]		public void addValue(Value v) {		super.addValue(v);		if (sortedData != null) {			int oldCount = sortedData.length;			NumValue[] oldSorted = sortedData;			int[] oldIndex = sortedIndex;			sortedData = new NumValue[oldCount + 1];			sortedIndex = new int[oldCount + 1];						double newDouble = ((NumValue)v).toDouble();			int countBelow = 0;			while (countBelow < oldCount && oldSorted[countBelow].toDouble() < newDouble)				countBelow ++;						if (countBelow > 0) {				System.arraycopy(oldSorted, 0, sortedData, 0, countBelow);				System.arraycopy(oldIndex, 0, sortedIndex, 0, countBelow);			}			sortedData[countBelow] = (NumValue)v;			sortedIndex[countBelow] = oldIndex.length;			if (countBelow < oldCount) {				System.arraycopy(oldSorted, countBelow, sortedData, countBelow + 1,																							oldCount - countBelow);				System.arraycopy(oldIndex, countBelow, sortedIndex, countBelow + 1,																							oldCount - countBelow);			}		}	}		public void clearData() {		super.clearData();		clearSortedValues();	}		public void readSequence(String sequenceParam) {		StringTokenizer st = new StringTokenizer(sequenceParam);		try {			NumValue minVal = new NumValue(st.nextToken());			NumValue stepVal = new NumValue(st.nextToken());			int nVals = Integer.parseInt(st.nextToken());			readSequence(minVal, stepVal, nVals);		} catch (Exception e) {			System.err.println("Error: bad format for sequence");		}	}		public void readSequence(NumValue minVal, NumValue stepVal, int nVals) {		int decimals = Math.max(minVal.decimals, stepVal.decimals);		double val = minVal.toDouble();		double step = stepVal.toDouble();		for (int i=0 ; i<nVals ; i++) {			addValue(new NumValue(val, decimals));			val += step;		}	}		private void doSort() {		int nVals = noOfValues();		sortedData = new NumValue[nVals];		sortedIndex = new int[nVals];		ValueEnumeration e = values();		int valIndex = 0;		int i = 0;		while (e.hasMoreValues()) {			NumValue y = (NumValue)e.nextValue();			if (Double.isNaN(y.toDouble())) {				nVals --;				sortedData[nVals] = y;				sortedIndex[nVals] = valIndex;			}			else {				sortedData[i] = y;				sortedIndex[i] = valIndex;				i++;			}			valIndex ++;		}		quickSort(0, nVals - 1);	}		synchronized public NumValue[] getSortedData() {		if (sortedData == null)			doSort();		return sortedData;	}		synchronized public int[] getSortedIndex() {		if (sortedIndex == null)			doSort();		return sortedIndex;	}		synchronized public void reSortData() {		if (sortedData != null) {			int iEnd = sortedData.length - 1;			int iStart = 0;			while (iStart < iEnd) {				while (iEnd >= 0 && Double.isNaN(sortedData[iEnd].toDouble()))					iEnd --;				while (iStart < iEnd && !Double.isNaN(sortedData[iStart].toDouble()))					iStart ++;				if (iStart < iEnd) {               NumValue temp = sortedData[iStart];               sortedData[iStart] = sortedData[iEnd];               sortedData[iEnd] = temp;               int iTemp = sortedIndex[iStart];               sortedIndex[iStart] = sortedIndex[iEnd];               sortedIndex[iEnd] = iTemp;               iStart ++;               iEnd --;				}			}						quickSort(0, iEnd);		}	}		public int indexToRank(int index) {		int si[] = getSortedIndex();		for (int i=0 ; i<si.length ; i++)			if (si[i] == index)				return i;		throw new NoSuchElementException("Can't find ranked value");	}		public int rankToIndex(int rank) {		int si[] = getSortedIndex();		return si[rank];	}		   private void quickSort(int lo0, int hi0) {      int lo = lo0;      int hi = hi0;      if (hi0 > lo0) {         double mid = sortedData[(lo0 + hi0) / 2].toDouble();         while(lo <= hi) {            while((lo < hi0) && (sortedData[lo].toDouble() < mid))               ++lo;            while((hi > lo0) && (sortedData[hi].toDouble() > mid))               --hi;            if(lo <= hi) {               NumValue temp = sortedData[lo];               sortedData[lo] = sortedData[hi];               sortedData[hi] = temp;               int iTemp = sortedIndex[lo];               sortedIndex[lo] = sortedIndex[hi];               sortedIndex[hi] = iTemp;               ++lo;               --hi;            }         }         if(lo0 < hi)            quickSort(lo0, hi);         if(lo < hi0)            quickSort(lo, hi0);      }   }}