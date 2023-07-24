package com.mmxlabs.widgets.schedulechart.draw;

import org.eclipse.swt.graphics.GC;

import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Line;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Rectangle;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Text;

public class GCBasedScheduleElementDrawer implements ScheduleElementDrawer {
	
	private final GC gc;
	
	public GCBasedScheduleElementDrawer(GC gc) {
		this.gc = gc;
	}

	@Override
	public void drawOne(BasicDrawableElement b) {
		int oldAlpha = gc.getAlpha();
		try {
			if (b.getBackgroundColour() != null) {
				gc.setBackground(b.getBackgroundColour());
			}

			if (b.getStrokeColor() != null) {
				gc.setForeground(b.getStrokeColor());
			}

			if (b instanceof Line l) {
				gc.drawLine(l.x1(), l.y1(), l.x2(), l.y2());
			} else if (b instanceof Rectangle r) {

				if (r.getStrokeColor() != null) {
					gc.drawRectangle(r.x(), r.y(), r.width(), r.height());
				}
				
				if (r.getBackgroundColour() != null) {
					gc.fillRectangle(r.x(), r.y(), r.width(), r.height());
				}

			} else if (b instanceof Text t) {
				gc.drawString(t.text(), t.x(), t.y());
			} else {
				throw new UnsupportedOperationException("Got a BasicDrawableElement that cannot be drawn by this ScheduleElementDrawer");
			}
		} finally {
			gc.setAlpha(oldAlpha);
		}
	}

}
