package utils;import java.awt.*;public class VerticalLayout extends GridBagLayout {//public class VerticalLayout implements LayoutManager, Serializable {	static public final int LEFT = 0;	static public final int CENTER = 1;	static public final int RIGHT = 2;	static public final int FILL = 3;		static public final int VERT_TOP = 0;	static public final int VERT_CENTER = 1;	static public final int VERT_SPACED = 2;	static public final int VERT_BOTTOM = 3;		//	private int align = LEFT;	private int vertSpacing = VERT_TOP;	private int vgap = 0;		private GridBagConstraints constraints;	private GridBagConstraints previousConstraints = null;		public VerticalLayout() {		this(LEFT, VERT_TOP, 0);	}		public VerticalLayout(int align) {		this(align, VERT_TOP, 0);	}		public VerticalLayout(int align, int vgap) {		this(align, VERT_TOP, vgap);	}		public VerticalLayout(int align, int vertSpacing, int vgap) {//		this.align = align;		this.vertSpacing = vertSpacing;		this.vgap = vgap;				constraints = new GridBagConstraints();		constraints.gridx = 0;		constraints.gridy = 0;		constraints.insets = new Insets(0,0,0,0);		constraints.gridheight = constraints.gridwidth = 1;		constraints.ipadx = constraints.ipady = 0;		constraints.weightx = 1.0;				constraints.fill = (align == FILL) ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;				constraints.weighty = (vertSpacing == VERT_SPACED) ? 1.0 : 0.0;				constraints.anchor = GridBagConstraints.CENTER;		if (align == LEFT || align == FILL)			constraints.anchor = (vertSpacing == VERT_TOP) ? GridBagConstraints.NORTHWEST														: (vertSpacing == VERT_BOTTOM) ? GridBagConstraints.SOUTHWEST														: GridBagConstraints.WEST;		else if (align == RIGHT)			constraints.anchor = (vertSpacing == VERT_TOP) ? GridBagConstraints.NORTHEAST														: (vertSpacing == VERT_BOTTOM) ? GridBagConstraints.SOUTHEAST														: GridBagConstraints.EAST;		else // align == CENTER			constraints.anchor = (vertSpacing == VERT_TOP) ? GridBagConstraints.NORTH														: (vertSpacing == VERT_BOTTOM) ? GridBagConstraints.SOUTH														: GridBagConstraints.CENTER;	}	public void addLayoutComponent(Component comp, Object dummy) {		super.addLayoutComponent(comp, constraints);		constraints.gridy ++;		if (vertSpacing == VERT_TOP || vertSpacing == VERT_BOTTOM) {			int nComp = (rowWeights == null) ? 1 : (rowWeights.length + 1);			rowWeights = new double[nComp];			if (vertSpacing == VERT_TOP)				rowWeights[rowWeights.length - 1] = 1.0;			else				rowWeights[0] = 1.0;		}				if (previousConstraints != null)		//	make sure that vgap is applied to all except last component			previousConstraints.insets.bottom = vgap;		previousConstraints = lookupConstraints(comp);	}	public void addLayoutComponent(String name, Component comp) {		addLayoutComponent(comp, null);	}/*		private int align = LEFT;	private int vertSpacing = VERT_TOP;	private int vgap;	private int minWidth = 0, minHeight = 0;	private int preferredWidth = 0, preferredHeight = 0;	private boolean initialised = false;		public VerticalLayout() {		this(LEFT, VERT_TOP, 0);	}		public VerticalLayout(int align) {		this(align, VERT_TOP, 0);	}		public VerticalLayout(int align, int vgap) {		this(align, VERT_TOP, vgap);	}		public VerticalLayout(int align, int vertSpacing, int vgap) {		this.align = align;		this.vgap = vgap;		this.vertSpacing = vertSpacing;	}	public void addLayoutComponent(String name, Component comp) {		initialised = false;	}	public void removeLayoutComponent(Component comp) {		initialised = false;	}	private void setSizes(Container parent) {		if (initialised)			return;		int nComps = parent.countComponents();		Dimension d = null;		preferredWidth = 0;		preferredHeight = 0;		minWidth = 0;		minHeight = 0;				int visibleComps = 0;		for (int i=0; i<nComps; i++) {			Component c = parent.getComponent(i);			if (c.isVisible()) {				d = c.getPreferredSize();								preferredWidth = Math.max(d.width, preferredWidth);				preferredHeight += d.height;				d = c.getMinimumSize();				minWidth = Math.max(d.width, minWidth);				minHeight += d.height;				visibleComps ++;			}		}		if (visibleComps > 1) {			minHeight += (visibleComps - 1) * vgap;			preferredHeight += (visibleComps - 1) * vgap;		}		initialised = true;	}		public Dimension preferredLayoutSize(Container parent) {		setSizes(parent);		Insets insets = parent.insets();		return new Dimension(preferredWidth + insets.left + insets.right,																	preferredHeight + insets.top + insets.bottom);	}		public Dimension minimumLayoutSize(Container parent) {		return preferredLayoutSize(parent);	}		public void layoutContainer(Container parent) {		setSizes(parent);				Insets insets = parent.insets();		int availableWidth = parent.getSize().width - (insets.left + insets.right);		int availableHeight = parent.getSize().height - (insets.top + insets.bottom);		int tempTop = insets.top;		int nComps = parent.countComponents();		int localVGap = vgap;				if (vertSpacing != VERT_TOP) {			int used = 0;			int visibleComps = 0;			for (int i=0; i<nComps; i++) {				Component c = parent.getComponent(i);				if (c.isVisible()) {					Dimension d = c.getPreferredSize();					used += Math.min(d.height, availableHeight);					visibleComps ++;				}			}			if (visibleComps > 1)				used += (visibleComps - 1) * vgap;						if (vertSpacing == VERT_CENTER)				tempTop += (availableHeight - used) / 2;			else if (vertSpacing == VERT_BOTTOM)				tempTop += availableHeight - used;			else {			//		VERT_SPACED				int extraGap = (availableHeight - used) / nComps;				tempTop += extraGap / 2;				localVGap += extraGap;			}		}				for (int i=0; i<nComps; i++) {			Component c = parent.getComponent(i);			if (c.isVisible()) {				Dimension d = c.getPreferredSize();				int leftPos = (align == LEFT || align == FILL) ? insets.left										: (align == RIGHT) ? parent.getSize().width - insets.right - d.width										: insets.left + (availableWidth - d.width) / 2;				int width = (align == FILL) ? parent.getSize().width - insets.left - insets.right										: d.width;				int height = Math.min(d.height, availableHeight);				c.reshape(leftPos, tempTop, width, height);				tempTop += height + localVGap;			}		}	}		public String toString() {		return getClass().getName();	}*/}