package dataView;import java.util.*;class RandomisedEnumeration implements ValueEnumeration {	private Vector v;	private int map[];	private int nextIndex = 0;		public RandomisedEnumeration(Vector v, int[] map) {		this.v = v;		this.map = map;	}		public boolean hasMoreValues() {		return nextIndex < map.length;	}		public Value nextValue() {		return (Value)v.elementAt(map[nextIndex ++]);	}		public RepeatValue nextGroup() {		return new RepeatValue((Value)v.elementAt(map[nextIndex ++]), 1);	}		public double nextDouble() {		return ((NumValue)v.elementAt(map[nextIndex ++])).toDouble();	}}