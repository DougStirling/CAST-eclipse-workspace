package histo;public class HighlightLimits {	private double minHilite, maxHilite;		public HighlightLimits(double minHilite, double maxHilite) {		this.minHilite = minHilite;		this.maxHilite = maxHilite;	}		public boolean classHilited(double classMin, double classMax) {		double classMiddle = (classMin + classMax) / 2.0;		return (classMiddle > minHilite) && (classMiddle < maxHilite);	}}