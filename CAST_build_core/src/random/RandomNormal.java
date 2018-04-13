package random;import java.util.*;import distn.*;public class RandomNormal extends RandomContinuous {	protected double mean, sd, truncation;		public RandomNormal(String randomInfo) {		StringTokenizer st = new StringTokenizer(randomInfo);		String countString = st.nextToken();		count = Integer.parseInt(countString);				String meanString = st.nextToken();		mean = Double.parseDouble(meanString);				String sdString = st.nextToken();		sd = Double.parseDouble(sdString);				if (st.hasMoreTokens()) {			String seedString = st.nextToken();			setSeed((Long.valueOf(seedString)).longValue());		}				if (st.hasMoreTokens()) {			String truncString = st.nextToken();			truncation = Double.parseDouble(truncString);		}		else			truncation = 5.0;		setTruncation();	}			public RandomNormal(int count, double mean, double sd, double truncation) {		super(count);		this.mean = mean;		this.sd = sd;		this.truncation = truncation;		setTruncation();	}		public void setParameters(double mean, double sd) {		this.mean = mean;		this.sd = sd;	}		public void setMean(double mean) {		this.mean = mean;	}		public void setSD(double sd) {		this.sd = sd;	}		private void setTruncation() {		double highP = NormalTable.cumulative(truncation);		double lowP = 1.0 - highP;		setPTruncation(lowP, highP);	}		protected double getQuantile(double p) {		return mean + sd * NormalTable.quantile(p);	}	//	public double generateOne() {//		double nextVal = nextGaussian();//		while (nextVal > truncation || nextVal < -truncation)//			nextVal = nextGaussian();//		return mean + sd * nextVal;//	}}