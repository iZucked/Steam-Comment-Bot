/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.util.List;
import java.util.function.UnaryOperator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Image;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Line;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Padding;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Polygon;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Rectangle;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Text;

public class GCBasedScheduleElementDrawer implements ScheduleElementDrawer, DrawerQueryResolver {
	
	private final GC gc;
	private UnaryOperator<Color> colourFilter = UnaryOperator.identity();
	
	public GCBasedScheduleElementDrawer(GC gc) {
		this.gc = gc;
	}

	@Override
	public void drawOne(BasicDrawableElement b) {
		int oldAlpha = gc.getAlpha();
		try {
			if (b.backgroundColour() != null) {
				gc.setBackground(colourFilter.apply(b.backgroundColour()));
			}

			if (b.borderColour() != null) {
				gc.setForeground(colourFilter.apply(b.borderColour()));
				gc.setLineWidth(b.borderThickness());
			}
			
			gc.setAlpha(b.alpha());

			if (b instanceof Line l) {
				gc.drawLine(l.x1(), l.y1(), l.x2(), l.y2());
			} else if (b instanceof Rectangle r) {

				if (r.backgroundColour() != null) {
					gc.fillRectangle(r.x(), r.y(), r.width(), r.height());
				}

				if (r.borderColour() != null) {
					if (r.isBorderInner()) {
						gc.setLineWidth(1);
					}

					gc.drawRectangle(r.x(), r.y(), r.width(), r.height());

					if (r.isBorderInner()) {
						gc.setLineWidth(r.borderThickness() - 1);
						int innerBorderShift = r.borderThickness() / 2;
						gc.drawRectangle(r.x() + innerBorderShift, r.y() + innerBorderShift, r.width() - 2 * innerBorderShift, r.height() - 2 * innerBorderShift);
					}
				}
				
			} else if (b instanceof Polygon p) {
				int[] pointArray = p.points().stream().flatMapToInt(o -> List.of(o.x, o.y).stream().mapToInt(i -> i)).toArray();
				
				if (p.backgroundColour() != null) {
					gc.fillPolygon(pointArray);
				}

				if (p.borderColour() != null) {
					gc.drawPolygon(pointArray);
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
			
			} else if (b instanceof Image i) {
				gc.drawImage(i.img(), i.x(), i.y());
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

	@Override
	public void applyColourFilter(UnaryOperator<Color> filter) {
		this.colourFilter = filter;
	}

	@Override
	public void removeColourFilter() {
		this.colourFilter = UnaryOperator.identity();
	}
	
	@Override
	public UnaryOperator<Color> getColourFilter() {
		return colourFilter;
	}
	
}
