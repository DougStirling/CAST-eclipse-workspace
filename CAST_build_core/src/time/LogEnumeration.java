package time;import dataView.*;class LogEnumeration implements ValueEnumeration {	private ValueEnumeration e;		public LogEnumeration(ValueEnumeration e) {		this.e = e;	}		public boolean hasMoreValues() {		return e.hasMoreValues();	}		public double nextDouble() {		return Math.log(e.nextDouble());	}		public Value nextValue() {		return new NumValue(nextDouble());	}		public RepeatValue nextGroup() {		return new RepeatValue(nextValue(), 1);	}}