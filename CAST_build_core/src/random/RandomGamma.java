package random;import java.util.*;import distn.*;public class RandomGamma extends RandomContinuous {	private double shape, scale;		public RandomGamma(String randomInfo) {		StringTokenizer st = new StringTokenizer(randomInfo);		String countString = st.nextToken();		count = Integer.parseInt(countString);				String shapeString = st.nextToken();		shape = Double.parseDouble(shapeString);				String scaleString = st.nextToken();		scale = Double.parseDouble(scaleString);				if (st.hasMoreTokens()) {			String seedString = st.nextToken();			setSeed((Long.valueOf(seedString)).longValue());		}				if (st.hasMoreTokens()) {			String truncString = st.nextToken();			setTruncation(0.0, Double.parseDouble(truncString));		}		else			setTruncation(0.0, Double.POSITIVE_INFINITY);	}			public RandomGamma(int count, double shape, double scale, double highTruncation) {		super(count);		this.shape = shape;		this.scale = scale;		setTruncation(0.0, highTruncation);	}		public void setParameters(double shape, double scale) {		this.shape = shape;		this.scale = scale;	}		public void setShape(double shape) {		this.shape = shape;	}		public void setScale(double scale) {		this.scale = scale;	}		public void setTruncation(double lowTruncation, double highTruncation) {		double lowZTrunc = lowTruncation / scale;		double highZTrunc = highTruncation / scale;		double lowPTrunc = GammaDistnVariable.gammaProb(lowZTrunc, shape);		double highPTrunc = (highZTrunc < Double.POSITIVE_INFINITY)																? GammaDistnVariable.gammaProb(highZTrunc, shape) : 1.0;		lowPTrunc = Math.max(0.000002, lowPTrunc);		highPTrunc = Math.min(0.999998, highPTrunc);		setPTruncation(lowPTrunc, highPTrunc);	}		protected double getQuantile(double p) {		return GammaDistnVariable.gammaQuant(p, shape) * scale;	}	//	public double generateOne() {//		double nextP = nextDouble();//		double nextVal = GammaDistnVariable.gammaQuant(nextP, shape) * scale;//		while (nextVal > highTruncation || nextVal < lowTruncation) {//			do {			//	algorithm only works for 0.000002 < nextP < 0.999998//				nextP = nextDouble();//			} while (nextP <= 0.000002 || nextP >= 0.999998);//			nextVal = GammaDistnVariable.gammaQuant(nextP, shape) * scale;//		}//		return nextVal;//	}}