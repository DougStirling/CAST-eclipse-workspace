package graphics3D;import java.awt.*;import javax.swing.*;public class RotateIcon implements Icon {	static final public int STANDARD = 0;	static final public int BOLD = 1;	static final public int DIM = 2;		static final private int kIconSize = 28;		static private Color kStandardTextColor = new Color(0x000099);	static private Color kHighlightTextColor = new Color(0x000033);	static private Color kDimTextColor = new Color(0x666666);		static public void drawX(Graphics g, int x, int y) {		g.drawLine(x, y, x, y+1);		g.drawLine(x, y+3, x, y+4);		g.drawLine(x+1, y+2, x+2, y+2);		g.drawLine(x+3, y, x+3, y+1);		g.drawLine(x+3, y+3, x+3, y+4);	}		static public void drawX2(Graphics g, int x, int y) {		g.drawLine(x, y, x, y+1);		g.drawLine(x, y+3, x, y+4);		g.drawLine(x+1, y+2, x+2, y+2);		g.drawLine(x+3, y, x+3, y+1);		g.drawLine(x+3, y+3, x+3, y+4);				g.drawLine(x+5, y-2, x+6, y-2);		g.drawLine(x+6, y-2, x+6, y);		g.drawLine(x+5, y+1, x+5, y);		g.drawLine(x+5, y+2, x+6, y+2);	}		static public void drawY(Graphics g, int x, int y) {		g.drawLine(x, y, x, y+2);		g.drawLine(x, y+5, x, y+5);		g.drawLine(x+1, y+3, x+2, y+3);		g.drawLine(x+3, y, x+3, y+5);		g.drawLine(x+1, y+6, x+2, y+6);	}		static public void drawZ(Graphics g, int x, int y) {		g.drawLine(x, y, x+3, y);		g.drawLine(x+3, y+1, x+3, y+1);		g.drawLine(x+1, y+2, x+2, y+2);		g.drawLine(x, y+3, x, y+3);		g.drawLine(x, y+4, x+3, y+4);	}		static private void draw2Axes(Graphics g, int x, int y) {		g.drawLine(x+3, y+6, x+3, y+24);		g.drawLine(x+3, y+24, x+22, y+24);	}		static private void draw3Axes(Graphics g, int x, int y) {		g.drawLine(x+3, y+2, x+3, y+18);		g.drawLine(x+3, y+18, x+18, y+11);		g.drawLine(x+3, y+18, x+18, y+25);	}		private int rotateType, enabling;		public RotateIcon(int rotateType, int enabling) {		this.rotateType = rotateType;		this.enabling = enabling;	}		private Color getTextColor() {		return (enabling == DIM) ? kDimTextColor									: (enabling == BOLD) ? kHighlightTextColor									: kStandardTextColor;	}	public void paintIcon(Component c, Graphics g, int x, int y) {		Color textColor = getTextColor();		g.setColor(textColor);		switch (rotateType) {			case RotateButton.YX_ROTATE:			case RotateButton.YX2_ROTATE:			case RotateButton.QUAD_YX_ROTATE:				draw2Axes(g, x, y);				drawY(g, x + 5, y + 2);				drawX(g, x + 22, y + 17);				break;			case RotateButton.YZ_ROTATE:				draw2Axes(g, x, y);				drawY(g, x + 5, y + 2);				drawZ(g, x + 22, y + 17);				break;			case RotateButton.XZ_ROTATE:				draw2Axes(g, x, y);				drawX(g, x + 5, y + 2);				drawZ(g, x + 22, y + 17);				break;			case RotateButton.QUAD_XX2_ROTATE:				draw2Axes(g, x, y);				drawX2(g, x + 5, y + 3);				drawX(g, x + 22, y + 17);				break;			case RotateButton.DX_ROTATE:				draw2Axes(g, x, y);				drawX(g, x + 22, y + 17);				break;			case RotateButton.DY_ROTATE:				draw2Axes(g, x, y);				drawY(g, x + 22, y + 15);				break;			case RotateButton.XYZ_ROTATE:				draw3Axes(g, x, y);				drawY(g, x + 5, y + 2);				drawX(g, x + 20, y + 8);				drawZ(g, x + 20, y + 21);				break;			case RotateButton.QUAD_XYX2_ROTATE:				draw3Axes(g, x, y);				drawY(g, x + 5, y + 2);				drawX2(g, x + 20, y + 8);				drawX(g, x + 20, y + 21);				break;			case RotateButton.YXD_ROTATE:				draw3Axes(g, x, y);				drawY(g, x + 20, y + 8);				drawX(g, x + 20, y + 21);				break;			case RotateButton.XZ_BLANK_ROTATE:			case RotateButton.YZ_BLANK_ROTATE:				draw2Axes(g, x, y);				break;			case RotateButton.XYZ_BLANK_ROTATE:				draw3Axes(g, x, y);				break;		}	}    	public int getIconWidth() {		return kIconSize;	}	public int getIconHeight() {		return kIconSize;	}}