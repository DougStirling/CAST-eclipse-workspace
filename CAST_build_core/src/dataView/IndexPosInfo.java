package dataView;public class IndexPosInfo implements PositionInfo {	public IndexPosInfo(int theItemIndex) {		itemIndex = theItemIndex;	}		public boolean equals(PositionInfo otherPos) {		if (otherPos == null || !(otherPos instanceof IndexPosInfo))			return false;		IndexPosInfo other = (IndexPosInfo)otherPos;		return itemIndex == other.itemIndex;	}		public int itemIndex;}