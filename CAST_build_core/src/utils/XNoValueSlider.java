package utils;import java.awt.*;import dataView.*;public class XNoValueSlider extends XSlider {	public XNoValueSlider(String minText, String maxText, String titleText, int minVal,							int maxVal, int startVal, int orientation, XApplet applet) {		super(minText, maxText, titleText, minVal, maxVal, startVal, orientation, applet);	}		public XNoValueSlider(String minText, String maxText, String titleText, int minVal,													int maxVal, int startVal, XApplet applet) {		super(minText, maxText, titleText, minVal, maxVal, startVal, applet);	}		protected Value translateValue(int val) {		return null;	}		protected int getMaxValueWidth(Graphics g) {		return 0;	}}