package contin;import java.awt.*;import dataView.*;public class BlankTableView extends BufferedCanvas {	static final private int kHorizLabelGap = 6;	static final private int kHorizMarginGap = 6;	static final private int kHalfMinColGap = 10;		static final private int kVertLabelGap = 4;	static final private int kVertMarginGap = 3;	static final private int kHalfMinRowGap = 2;		static final private int kCrossSize = 4;		private int maxRow, maxCol;	private int nRow, nCol;	private String rowPrefix, colPrefix;	private Color rowLabelColor, colLabelColor;		private boolean initialised = false;		private int ascent, descent, maxRowTitleWidth, maxColTitleWidth, minRowHeight, minColWidth;	private int minWidth, minHeight;	private LabelValue tempTitle = new LabelValue("");		public BlankTableView(XApplet applet, int maxRow, int maxCol, int nRow, int nCol,																	String rowPrefix, String colPrefix, Color rowLabelColor,																	Color colLabelColor) {		super(applet);				this.maxRow = maxRow;		this.maxCol = maxCol;		this.nRow = nRow;		this.nCol = nCol;		this.rowPrefix = rowPrefix;		this.colPrefix = colPrefix;		this.rowLabelColor = rowLabelColor;		this.colLabelColor = colLabelColor;	}		public void setTableSize(int nRow, int nCol) {		this.nRow = nRow;		this.nCol = nCol;		repaint();	}//---------------------------------------------------------------------------------		private boolean initialise(Graphics g) {		if (initialised)			return false;		else {			tempTitle.label = rowPrefix + maxRow;			maxRowTitleWidth = tempTitle.stringWidth(g);			tempTitle.label = colPrefix + maxCol;			maxColTitleWidth = tempTitle.stringWidth(g);						FontMetrics fm = g.getFontMetrics();			ascent = fm.getAscent();			descent = fm.getDescent();			minRowHeight = Math.max(ascent + descent, 2 * kCrossSize) + 2 * kHalfMinRowGap;			minColWidth = Math.max(maxColTitleWidth, 2 * kCrossSize) + 2 * kHalfMinColGap;						minWidth = maxRowTitleWidth + kHorizLabelGap + 2 * kHorizMarginGap + maxCol * minColWidth;			minHeight = ascent + descent + kVertLabelGap + 2 * kVertMarginGap + maxRow * minRowHeight;						return true;		}	}		public void corePaint(Graphics g) {		initialise(g);				int horizOffset = (getSize().width - minWidth) / 2;		int vertOffset = (getSize().height - minHeight) / 2;				int tableTop = vertOffset + ascent + descent + kVertLabelGap;		int tableLeft = horizOffset + maxRowTitleWidth + kHorizLabelGap;		int tableHeight = 2 * kVertMarginGap + maxRow * minRowHeight;		int tableWidth = 2 * kHorizMarginGap + maxCol * minColWidth;				g.setColor(Color.white);		g.fillRect(tableLeft, tableTop, tableWidth, tableHeight);		g.setColor(Color.black);		g.drawRect(tableLeft, tableTop, tableWidth-1, tableHeight-1);				int colDiff = (tableWidth - 2 * kHorizMarginGap) / nCol;		int colCentre = tableLeft + kHorizMarginGap + colDiff / 2;				int rowDiff = (tableHeight - 2 * kVertMarginGap) / nRow;		int rowCentre = tableTop + kVertMarginGap + rowDiff / 2;		int rowBaseline = rowCentre + (ascent - descent) / 2;				g.setColor(rowLabelColor);		for (int r=0 ; r<nRow ; r++) {			tempTitle.label = rowPrefix + (r+1);			tempTitle.drawLeft(g, tableLeft - kHorizLabelGap, rowBaseline + r * rowDiff);		}				for (int c=0 ; c<nCol ; c++) {			g.setColor(colLabelColor);			tempTitle.label = colPrefix + (c+1);			int horiz = colCentre + c * colDiff;			tempTitle.drawCentred(g, horiz, tableTop - kVertLabelGap);						g.setColor(getForeground());			for (int r=0 ; r<nRow ; r++) {				int vert = rowCentre + r * rowDiff;				g.drawLine(horiz - kCrossSize, vert - kCrossSize, horiz + kCrossSize, vert + kCrossSize);				g.drawLine(horiz + kCrossSize, vert - kCrossSize, horiz - kCrossSize, vert + kCrossSize);			}		}	}		public Dimension getMinimumSize() {		Graphics g = getGraphics();		initialise(g);				return new Dimension(minWidth, minHeight);	}		public Dimension getPreferredSize() {		return getMinimumSize();	}//-----------------------------------------------------------------------------------	protected boolean needsHitToDrag() {		return true;	}		protected boolean canDrag() {		return false;	}		protected boolean startDrag(PositionInfo startInfo) {		return false;	}		protected void doDrag(PositionInfo fromPos, PositionInfo toPos) {	}		protected void endDrag(PositionInfo startPos, PositionInfo endPos) {	}}