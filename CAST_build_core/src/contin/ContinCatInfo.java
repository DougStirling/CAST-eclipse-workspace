package contin;import dataView.*;public class ContinCatInfo implements PositionInfo {	public ContinCatInfo(int xIndex, int yIndex) {		this.xIndex = xIndex;		this.yIndex = yIndex;		zIndex = -1;	}		public ContinCatInfo(int zIndex, int xIndex, int yIndex) {		this.zIndex = zIndex;		this.xIndex = xIndex;		this.yIndex = yIndex;	}		public boolean equals(PositionInfo otherPos) {		if (otherPos == null || !(otherPos instanceof ContinCatInfo))			return false;		ContinCatInfo other = (ContinCatInfo)otherPos;		return (xIndex == other.xIndex) && (yIndex == other.yIndex)														&& (zIndex == other.zIndex);	}		public int xIndex, yIndex, zIndex;}