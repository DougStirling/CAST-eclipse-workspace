package random;import java.util.*;abstract public class RandomDiscrete extends Random {	protected int count;		public RandomDiscrete() {	}		public RandomDiscrete(int count) {		this.count = count;	}		abstract public int generateOne();		public int[] generate() {		int vals[] = new int[count];		for (int i=0 ; i<count ; i++)			vals[i] = generateOne();		return vals;	}		public void setSampleSize(int count) {		this.count = count;	}		public int getSampleSize() {		return count;	}}