package qnUtils;import java.awt.*;import javax.swing.*;import java.io.Serializable;import utils.*;public class QuestionLayout implements LayoutManager, Serializable {	static final public String MEAN = "Mean";	static final public String SD = "SD";	static final public String X1_VALUE = "X1";	static final public String X2_VALUE = "X2";	static final public String XBAR = "XBAR";	static final public String S = "S";	static final public String N = "N";	static final public String PI = "PI";	static final public String MU1 = "Mu1";	static final public String MU2 = "Mu2";	static final public String SIGMA1 = "Sigma1";	static final public String SIGMA2 = "Sigma2";	static final public String D = "D";	static final public String D2 = "D2";	static final public String S1 = "S1";		//		successes in group 1	static final public String S2 = "S2";	static final public String N1 = "N1";	static final public String N2 = "N2";		static final private String[] compName = {MEAN, SD, X1_VALUE, X2_VALUE, XBAR, S, N, PI,																MU1, MU2, SIGMA1, SIGMA2, D, D2, S1, S2, N1, N2};		static final private int kMinimumWidth = 50;	static final private int kMinimumHeight = 50;	static final private int kLineGap = 2;		private String questionText;		private Component comp[] = new Component[compName.length];	private int lineHeight;		public QuestionLayout(String questionText) {		this.questionText = questionText;	}	public void addLayoutComponent(String name, Component newComp) {		for (int i=0 ; i<compName.length ; i++)			if (compName[i].equals(name)) {				comp[i] = newComp;				break;			}	}	public void removeLayoutComponent(Component comp) {	}		public Dimension minimumLayoutSize(Container parent) {		Insets insets = parent.getInsets();		return new Dimension(kMinimumWidth + insets.left + insets.right,																	kMinimumHeight + insets.top + insets.bottom);	}		public Dimension preferredLayoutSize(Container parent) {		return minimumLayoutSize(parent);	}		public int getBaselineFromTop(Graphics g) {		int editHeight = firstComp().getMinimumSize().height;		FontMetrics fm = g.getFontMetrics();		int ascent = fm.getAscent();		int descent = fm.getDescent();		return (editHeight + ascent - descent) / 2;	}		public QuestionTokenizer getTokenizer(Container parent, Graphics g, Insets insets) {		int editHeight = firstComp().getMinimumSize().height;//		FontMetrics fm = g.getFontMetrics();//		int ascent = fm.getAscent();//		int descent = fm.getDescent();		lineHeight = editHeight + kLineGap;				int availableWidth = parent.getSize().width - (insets.left + insets.right);				return new QuestionTokenizer(questionText, g, availableWidth, lineHeight,														insets.top + kLineGap, comp);	}		public void layoutContainer(Container parent) {		Insets insets = parent.getInsets();		int editHeight = firstComp().getMinimumSize().height;				boolean componentUsed[] = new boolean[comp.length];				QuestionTokenizer wordTokenizer = getTokenizer(parent, parent.getGraphics(), insets);				while (wordTokenizer.hasMoreWords()) {			TextWord word = wordTokenizer.nextWord();			if (word != null && word.theEdit != null) {				word.theEdit.setBounds(insets.left + word.horizPos + word.pictureWidth,									word.lineTop, word.editWidth, editHeight);				for (int i=0 ; i<comp.length ; i++)					if (comp[i] == word.theEdit)						componentUsed[i] = true;			}		}				for (int i=0 ; i<comp.length ; i++)			if (comp[i] != null && !componentUsed[i])				comp[i].setBounds(-100, -100, 20, 20);	//		shift unused components off panel.																	//		used when changing from 'between' to 'less'	}		private Component firstComp() {		for (int i=0 ; i<comp.length ; i++)			if (comp[i] != null)				return comp[i];		return null;	}		public void changeQuestionText(String questionText, String[] valueString) {		this.questionText = questionText;		for (int i=0 ; i<valueString.length ; i++)			if (valueString[i] != null && comp[i] != null) {				if (comp[i] instanceof XLabel)					((XLabel)comp[i]).setText(valueString[i]);				else					((JTextField)comp[i]).setText(valueString[i]);			}	}		public Component[] getComponents() {		return comp;	}		public String getValueString(String key) {		for (int i=0 ; i<comp.length ; i++)			if (comp[i] != null && compName[i].equals(key)) {				if (comp[i] instanceof XLabel)					return ((XLabel)comp[i]).getText();				else					return ((JTextField)comp[i]).getText();			}		return null;	}		public String[] getValueStrings() {		String s[] = new String[comp.length];		for (int i=0 ; i<comp.length ; i++) {			if (comp[i] != null) {				if (comp[i] instanceof XLabel)					s[i] = ((XLabel)comp[i]).getText();				else					s[i] = ((JTextField)comp[i]).getText();			}		}		return s;	}		public String toString() {		return getClass().getName();	}}