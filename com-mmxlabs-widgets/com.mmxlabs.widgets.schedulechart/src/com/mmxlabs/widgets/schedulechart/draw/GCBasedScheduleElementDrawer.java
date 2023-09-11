/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Line;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Padding;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Rectangle;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Text;

public class GCBasedScheduleElementDrawer implements ScheduleElementDrawer, DrawerQueryResolver {
	
	private final GC gc;
	
	public GCBasedScheduleElementDrawer(GC gc) {
		this.gc = gc;
	}

	@Override
	public void drawOne(BasicDrawableElement b) {
		int oldAlpha = gc.getAlpha();
		try {
			if (b.backgroundColour() != null) {
				gc.setBackground(b.backgroundColour());
			}

			if (b.borderColour() != null) {
				gc.setForeground(b.borderColour());
				gc.setLineWidth(b.borderThickness());
			}
			
			gc.setAlpha(b.alpha());

			if (b instanceof Line l) {
				gc.drawLine(l.x1(), l.y1(), l.x2(), l.y2());
			} else if (b instanceof Rectangle r) {

				if (r.borderColour() != null) {
					gc.drawRectangle(r.x(), r.y(), r.width(), r.height());
				}
				
				if (r.backgroundColour() != null) {
					gc.fillRectangle(r.x(), r.y(), r.width(), r.height());
				}

			} else if (b instanceof Text t) {
				final Padding p = t.p();
				final org.eclipse.swt.graphics.Rectangle bb = t.boundingBox();

				String s = t.text();
				Point extent = findSizeOfText(s, t.font());

				if (p.top() + extent.y + p.bottom() > bb.height) {
					throw new IllegalArgumentException("The given text is too tall for its bounding box.");
				}
				
				if (p.left() + extent.x + p.right() > bb.width) {
					s = "...";
					extent = findSizeOfText(s, t.font());
				}
				
				int x = switch (t.alignment()) {
					case SWT.LEFT -> bb.x + p.left();
					case SWT.RIGHT -> bb.x + bb.width - extent.x - p.right();
					case SWT.CENTER -> bb.x + bb.width / 2 - extent.x / 2;
					default -> throw new IllegalArgumentException("Unexpected value for alignment: " + t.alignment());
				};
				
				int y = bb.y + p.top();
				
				gc.setFont(t.font());
				gc.drawString(s, x, y, t.backgroundColour() == null);
			} else {
				throw new UnsupportedOperationException("Got a BasicDrawableElement that cannot be drawn by this ScheduleElementDrawer");
			}
		} finally {
			gc.setAlpha(oldAlpha);
		}
	}

	@Override
	public Point findSizeOfText(String text, Font f) {
		gc.setFont(f);
		return gc.textExtent(text);
	}

	@Override
	public Point findSizeOfText(Text text) {
		Point textExtent = findSizeOfText(text.text(), text.font());
		Padding p = text.p();
		return new Point(p.left() + textExtent.x + p.right(), p.top() + textExtent.y + p.bottom());
	}

}
