package cat;import java.awt.*;import dataView.*;import images.*;public class NumImageValue extends NumValue {		static final private int kMaxWait = 30000;		//		30 seconds		static final private int kDigitWidth = 8;	static final private int kDotWidth = 4;	static final private int kMinusWidth = 6;	static final private int kCommaWidth = 4;	static final public int kDigitAscent = 11;	static final public int kDigitDescent = 2;		static private Image digit[] = new Image[10];	static private Image comma, dot, minus, leading0;	static private boolean loadedImages = false;		private XApplet applet;		public NumImageValue(double theValue, int theDecimals, XApplet applet) {		super(theValue, theDecimals);		this.applet = applet;				if (!loadedImages) {			MediaTracker tracker = new MediaTracker(applet);			comma = CoreImageReader.getImage("tableDigits/comma.gif");			tracker.addImage(comma, 0);			dot = CoreImageReader.getImage("tableDigits/dot.gif");			tracker.addImage(dot, 0);			minus = CoreImageReader.getImage("tableDigits/minus.gif");			tracker.addImage(minus, 0);			leading0 = CoreImageReader.getImage("tableDigits/leading0.gif");			tracker.addImage(leading0, 0);			for (int i=0 ; i<10 ; i++) {				digit[i] = CoreImageReader.getImage("tableDigits/digit" + i + ".gif");				tracker.addImage(digit[i], 0);			}			try {				tracker.waitForAll(kMaxWait);			} catch (InterruptedException e) {			}			loadedImages = true;		}	}		public void drawLeft(Graphics g, int drawDecs, int x, int y) {		String st = getValueStringWithCommas();				for (int i=st.length()-1 ; i>=0 ;  i--) {			char ch = st.charAt(i);			if (ch == ',') {				x -= kCommaWidth;				g.drawImage(comma, x, y - kDigitAscent, applet);			}			else if (ch == '-') {				x -= kMinusWidth;				g.drawImage(minus, x, y - kDigitAscent, applet);			}			else if (ch == '.') {				x -= kDotWidth;				g.drawImage(dot, x, y - kDigitAscent, applet);			}			else if (ch == '0' && i == 0) {				x -= kDigitWidth;				g.drawImage(leading0, x, y - kDigitAscent, applet);			}			else {				x -= kDigitWidth;				g.drawImage(digit[ch - '0'], x, y - kDigitAscent, applet);			}		}	}		public void drawLeft(Graphics g, int x, int y) {		drawLeft(g, decimals, x, y);	}		public int stringWidth(Graphics g) {		String st = getValueStringWithCommas();		int width = 0;		for (int i=0 ; i<st.length() ;  i++) {			char ch = st.charAt(i);			if (ch == ',')				width += kCommaWidth;			else if (ch == '-')				width += kMinusWidth;			else if (ch == '.')				width += kDotWidth;			else				width += kDigitWidth;		}		return width;	}}