package dataView;public class CatPosInfo implements PositionInfo {	public CatPosInfo(int catIndex, boolean highNotLow) {		this.catIndex = catIndex;		this.highNotLow = highNotLow;	}		public boolean equals(PositionInfo otherPos) {		if (otherPos == null || !(otherPos instanceof CatPosInfo))			return false;		CatPosInfo other = (CatPosInfo)otherPos;		return (catIndex == other.catIndex) && (highNotLow == other.highNotLow);	}		public int catIndex;	public boolean highNotLow;}