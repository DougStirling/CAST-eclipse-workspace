package distn;public class BinomialTable {	static final private int kMaxMeanForSeries = 40;		static public double cumulative(int x, int n, double p) {																		//		prob of less than or equal to x		if (x * 2 > n)			return 1.0 - cumulative(n - x - 1, n, 1.0 - p);						if (x > kMaxMeanForSeries)			return NormalTable.cumulative((x + 0.5 - n * p) / Math.sqrt(n * p * (1.0 - p)));				if (x < 0)			return 0.0;				double term = Math.pow(1.0 - p, n);		double cumProb = term;		double theFactor = p / (1.0 - p);		for (int i=1 ; i<= x ; i++) {			term *= (n - i + 1) * theFactor / i;			cumProb += term;		}		return cumProb;	}}