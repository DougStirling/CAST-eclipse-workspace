package qnUtils;import java.awt.*;import java.util.*;import imageGroups.*;class TextWord {	Component theEdit;	String word;	Image picture;	int editWidth, stringWidth, pictureWidth;	int lineTop, horizPos;		TextWord(Component theEdit, String word, Image picture, int editWidth, int stringWidth,														int pictureWidth, int lineTop, int horizPos) {		this.theEdit = theEdit;		this.word = word;		this.picture = picture;		this.editWidth = editWidth;		this.stringWidth = stringWidth;		this.pictureWidth = pictureWidth;		this.lineTop = lineTop;		this.horizPos = horizPos;	}}public class QuestionTokenizer extends StringTokenizer {	static final private String tokenString[] = {"#mu#", "#sig#", "#x1#", "#x2#", "#xBar#", "#s#", "#n#", "#pi#", "#mu1#", "#mu2#", "#sig1#", "#sig2#", "#d#", "#d2#", "#suc1#", "#suc2#", "#n1#", "#n2#"};		static final private int kPictureEditGap = 3;		private Image image[] = {	MeanSDImages.popnMean,										MeanSDImages.popnSD,										MeanSDImages.x,										MeanSDImages.x,										MeanSDImages.sampMean,										MeanSDImages.sampSD,										MeanSDImages.sampN,										MeanSDImages.popnProp,										GroupsEqualsImages.mu1,										GroupsEqualsImages.mu2,										GroupsEqualsImages.sigma1,										GroupsEqualsImages.sigma2,										GroupsEqualsImages.d,										GroupsEqualsImages.d,										GroupsEqualsImages.suc1,										GroupsEqualsImages.suc2,										GroupsEqualsImages.n1,										GroupsEqualsImages.n2};	private Component comp[];	private int pixWidth;	private FontMetrics fm;	private int spaceLength;	private int lineHeight;		private int horizPos = 0;	private int lineTop = 0;		public QuestionTokenizer(String s, Graphics g, int pixWidth, int lineHeight, int line0Top,																										Component comp[]) {		super(s);		this.comp = comp;		this.pixWidth = pixWidth;		this.lineHeight = lineHeight;		lineTop = line0Top;		fm = g.getFontMetrics();		spaceLength = fm.stringWidth(" ");	}		public TextWord nextWord() {		if (!hasMoreTokens())			return null;				String rawWord = nextToken();		Component theEdit = null;		Image picture = null;		if (rawWord.charAt(0) == '#') {			for (int i=0 ; i<tokenString.length ; i++)				if (rawWord.startsWith(tokenString[i])) {					theEdit = comp[i];					rawWord = rawWord.substring(tokenString[i].length());					picture = image[i];					break;				}		}				int editLength = (theEdit == null) ? 0 : theEdit.getPreferredSize().width;		int textLength = fm.stringWidth(rawWord);		int pictureLength = (picture == null) ? 0 : (MeanSDImages.kParamWidth + kPictureEditGap);				int itemStart = 0;		if (horizPos > 0) {			if (horizPos + spaceLength + pictureLength + editLength + textLength > pixWidth)				lineTop += lineHeight;			else				itemStart = horizPos + spaceLength;		}		horizPos = itemStart + pictureLength + editLength + textLength;				return new TextWord(theEdit, rawWord, picture, editLength, textLength, pictureLength,																									lineTop, itemStart);	}		public boolean hasMoreWords() {		return hasMoreTokens();	}}