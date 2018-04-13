package map;import java.awt.*;import java.util.*;import dataView.*;public class RegionValue extends Value {	private Polygon p = new Polygon();	private Polygon tempP;		public RegionValue(String coordinates) {		StringTokenizer st = new StringTokenizer(coordinates);		while (st.hasMoreTokens()) {			String pt = st.nextToken();			int commaIndex = pt.indexOf(',');			int x = Integer.parseInt(pt.substring(0, commaIndex));			int y = Integer.parseInt(pt.substring(commaIndex + 1, pt.length()));			p.addPoint(x, y);		}				p.addPoint(p.xpoints[0], p.ypoints[0]);	}		public void drawAtPoint(Graphics g, int x, int y) {	}		public void fillInMap(Graphics g, RegionVariable regionVar) {		tempP = regionVar.rescalePoly(p, tempP);		if (tempP == null)			g.fillPolygon(p.xpoints, p.ypoints, p.npoints);		else			g.fillPolygon(tempP.xpoints, tempP.ypoints, tempP.npoints);	}		public void getRegionBounds(Rectangle r) {		getRegionBounds(r, p);	}		private void getRegionBounds(Rectangle r, Polygon poly) {		int minLeft = Integer.MAX_VALUE;		int minTop = Integer.MAX_VALUE;		int maxRight = Integer.MIN_VALUE;		int maxBottom = Integer.MIN_VALUE;		for (int i=0 ; i<poly.npoints ; i++) {			int xi = poly.xpoints[i];			int yi = poly.ypoints[i];			if (xi < minLeft)				minLeft = xi;			if (xi > maxRight)				maxRight = xi;			if (yi < minTop)				minTop = yi;			if (yi > maxBottom)				maxBottom = yi;		}		r.x = minLeft;		r.y = minTop;		r.width = (maxRight - minLeft);		r.height = (maxBottom - minTop);	}		public void drawCircle(Graphics g, int radius, Color fillColor,																				Color outlineColor, RegionVariable regionVar) {		tempP = regionVar.rescalePoly(p, tempP);		Rectangle r = new Rectangle(0, 0, 0, 0);		if (tempP == null)			getRegionBounds(r, p);		else			getRegionBounds(r, tempP);			int xCenter = r.x + r.width / 2;		int yCenter = r.y + r.height / 2;				if (fillColor != null) {			g.setColor(fillColor);			g.fillOval(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius);		}				if (outlineColor != null) {			g.setColor(outlineColor);			g.drawOval(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius);		}	}		public void drawPie(Graphics g, int radius, double[] y, Color[] c, Color[] cOutline,																												RegionVariable regionVar) {		tempP = regionVar.rescalePoly(p, tempP);		Rectangle r = new Rectangle(0, 0, 0, 0);		if (tempP == null)			getRegionBounds(r, p);		else			getRegionBounds(r, tempP);			int xCenter = r.x + r.width / 2;		int yCenter = r.y + r.height / 2;				g.setColor(c[c.length - 1]);		//	last category//		g.fillOval(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius);				double endCum = 0.25;		for (int i=0 ; i<c.length ; i++) {			if (endCum < 0.0)				endCum += 1.0;			double startCum = endCum;			endCum -= y[i];						int startDegrees = (int)Math.round(360 * startCum);			int endDegrees = (int)Math.round(360 * endCum);			g.setColor(c[i]);			g.fillArc(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius, startDegrees, (endDegrees - startDegrees));			if (cOutline != null) {				g.setColor(cOutline[i]);				g.drawArc(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius, startDegrees, (endDegrees - startDegrees));			}		}	}		public void drawUnknown(Graphics g, Color c, RegionVariable regionVar) {		tempP = regionVar.rescalePoly(p, tempP);		Rectangle r = new Rectangle(0, 0, 0, 0);		if (tempP == null)			getRegionBounds(r, p);		else			getRegionBounds(r, tempP);			int xCenter = r.x + r.width / 2;		int yCenter = r.y + r.height / 2;				int fontSize = regionVar.rescaleRadius(14);		Font f = new Font(XApplet.FONT, Font.BOLD, fontSize);		g.setFont(f);		FontMetrics fm = g.getFontMetrics();		int ascent = fm.getAscent();		int qnWidth = fm.stringWidth("?");				g.setColor(c);				g.drawString("?", xCenter - qnWidth / 2, yCenter + ascent / 2);	}		public String toString() {		StringBuffer s = new StringBuffer(p.npoints + ": ");		for (int i=0 ; i<p.npoints ; i++)			s.append(" (" + p.xpoints[i] + "," + p.ypoints[i] + ")");		return s.toString();	}		public boolean hitInRegion(int xInMap, int yInMap) {		return p.contains(xInMap, yInMap);	}}