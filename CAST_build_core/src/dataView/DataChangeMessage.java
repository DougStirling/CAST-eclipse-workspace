package dataView;public class DataChangeMessage {	public int messageType;	public Object object;	public int index;		public DataChangeMessage(int theMessageType, Object theObject) {		messageType = theMessageType;		object = theObject;	}		public DataChangeMessage(int theMessageType, int theIndex) {		messageType = theMessageType;		index = theIndex;	}		public static final int CHANGE_SELECTION = 0;				//		XOR selection during drag//	public static final int INVERT_ITEM = 1;	public static final int CHANGE_VALUE = 2;	public static final int CHANGE_VARIABLE = 3;	public static final int TRANSFORMED_AXIS = 4;	public static final int ADDED_VALUES = 5;}