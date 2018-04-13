package exerciseNumGraph;import java.awt.*;import exercise2.*;import exerciseNumGraphProg.*;public class ShapeChoicePanel extends MultichoicePanel{	static final private int kOptions = 5;		private String optionString[];	//================================================		private class ShapeOptionInfo extends OptionInformation {		private int shape;				ShapeOptionInfo(int shape, int correctShape) {			super(shape == correctShape);			this.shape = shape;		}				public boolean equals(OptionInformation a) {			ShapeOptionInfo oa = (ShapeOptionInfo)a;			return (shape == oa.shape);		}				public boolean lessThan(OptionInformation a) {			return true;		}				public String getOptionString() {			return optionString[shape];		}				public String getMessageString() {			switch (shape) {				case InterpretShapeApplet.NORMAL:					return "The distribution is fairly symmetric with no outliers or clusters.";				case InterpretShapeApplet.SKEW_POS:					return "The distribution has a long tail towards the high values.";				case InterpretShapeApplet.SKEW_NEG:					return "The distribution has a long tail towards the low values.";				case InterpretShapeApplet.OUTLIER_POS:				case InterpretShapeApplet.OUTLIER_NEG:					return "The distribution is fairly symmetric other than a single outlier.";				case InterpretShapeApplet.CLUSTERS1:				case InterpretShapeApplet.CLUSTERS2:					return "There seem to be two separate clusters of values.";			}			return null;		}	}	//================================================		public ShapeChoicePanel(ExerciseApplet exerciseApplet, int correctShape, String[] optionString) {		super(exerciseApplet, kOptions);		optionInfo = new ShapeOptionInfo[kOptions];		this.optionString = optionString;		setupChoices(correctShape);		setupPanel();	}		public void setupChoices(int correctShape) {		if (correctShape == InterpretShapeApplet.OUTLIER_NEG)			correctShape = InterpretShapeApplet.OUTLIER_POS;		else if (correctShape == InterpretShapeApplet.CLUSTERS2)			correctShape = InterpretShapeApplet.CLUSTERS1;	//	don't distinguish different types of cluster or outlier				for (int i=0 ; i<5 ; i++)			optionInfo[i] = new ShapeOptionInfo(i, correctShape);				randomiseOptions();		findCorrectOption();	}		protected Component createOptionPanel(int optionIndex, ExerciseApplet exerciseApplet) {		return new OptionLongTextPanel(optionInfo, optionIndex, exerciseApplet);	}		public void changeOptions(int correctShape, String[] optionString) {		this.optionString = optionString;				setupChoices(correctShape);		for (int i=0 ; i<option.length ; i++)			((OptionLongTextPanel)option[i]).changeContent();	}	//	public String getBiggestMessage(String nullHypoth) {//		return "If " + nullHypoth + ", the probability of getting a p-value as low as 9.9999 is 9.9999. A p-value between 0.05 and 0.1 is fairly low, but not particularly unusual."; //	}}