package distn;public class NormalTable {	static final private double cc0 = 1.0, cc1 = 0.0498673470, cc2 = 0.0211410061, cc3 = 0.0032776263,								cc4 = 0.0000380036, cc5 = 0.0000488906, cc6 = 0.0000053830;		static public double alNorm(double x, boolean topTail) {		if (x < -10.0)			return topTail ? 1.0 : 0.0;		else if (x > 10.0)			return topTail ? 0.0 : 1.0;				boolean neg = x < 0.0;		double z = neg ? -x : x;				double tailArea = (((((z * cc6 + cc5) * z + cc4) * z + cc3) * z + cc2) * z + cc1) * z + cc0;		tailArea = Math.pow(tailArea, -16.0) / 2.0;				return (neg == topTail) ? (1.0 - tailArea) : tailArea;	}		static public double cumulative(double x) {			//		prob of less than or equal to x		return alNorm(x, false);	}	static final private double split = 0.42;	static final private double a0 = 2.50662823884, a1 = -18.61500062529, a2 = 41.39119773534,																	a3 = -25.44106049637;	static final private double b1 = -8.47351093090, b2 = 23.08336743743, b3 = -21.06224101826,																	b4 = 3.13082909833;	static final private double c0 = -2.78718931138, c1 = -2.29796479134, c2 = 4.85014127135,																	c3 = 2.32121276858;	static final private double d1 = 3.54388924762, d2 = 1.63706781897;		static public double quantile(double p) {		//	AS 111		double q = p - 0.5;		if (Math.abs(q) <= split) {			double r = q * q;			return q * (((a3*r + a2)*r + a1)*r + a0) / ((((b4*r + b3)*r + b2)*r																					+ b1)*r + 1.0);		}		else {			double r = (q > 0) ? (1.0 - p) : p;			if (r <= 0.0)				return Double.NaN;			r = Math.sqrt(-Math.log(r));			double result = (((c3*r + c2)*r + c1)*r + c0) / ((d2*r + d1)*r + 1.0);			return (q < 0.0) ? -result : result;		}	}}