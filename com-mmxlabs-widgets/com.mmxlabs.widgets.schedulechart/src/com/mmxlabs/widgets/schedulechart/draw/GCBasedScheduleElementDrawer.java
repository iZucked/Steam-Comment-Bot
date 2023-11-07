/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Image;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Line;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Padding;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Polygon;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Polyline;
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

				Pattern pattern = null;
				if(r.backgroundGradientColorStart() != null && r.backgroundGradientColorEnd() != null) {
					pattern = new Pattern(gc.getDevice(), r.x(), r.y(), r.x(), r.y() + r.height(), r.backgroundGradientColorStart(), r.backgroundGradientColorEnd());
					gc.setBackgroundPattern(pattern);
					gc.fillRectangle(r.x(), r.y(), r.width(), r.height());
				}
				else if (r.backgroundColour() != null) {
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
				
				if(pattern != null)
					pattern.dispose();
				
			} else if (b instanceof Polygon p) {
				int[] pointArray = p.points().stream().flatMapToInt(o -> List.of(o.x, o.y).stream().mapToInt(i -> i)).toArray();
				
				if (p.backgroundColour() != null) {
					gc.fillPolygon(pointArray);
				}

				if (p.borderColour() != null) {
					gc.drawPolygon(pointArray);
				}

			} else if (b instanceof Text t) {
				final Padding p = t.padding();
				final org.eclipse.swt.graphics.Rectangle bb = t.boundingBox();

				String s = t.text();
				Point extent = findSizeOfText(s, t.font(), t.fontSize());

				if (p.top() + extent.y + p.bottom() > bb.height) {
					//throw new IllegalArgumentException("The given text is too tall for its bounding box.");
				}
				
				if (p.left() + extent.x + p.right() > bb.width) {
					s = "...";
					extent = findSizeOfText(s, t.font(), t.fontSize());
				}
				
				int x = switch (t.horizontalAlignment()) {
					case SWT.LEFT -> bb.x + p.left();
					case SWT.RIGHT -> bb.x + bb.width - extent.x - p.right();
					case SWT.CENTER -> bb.x + bb.width / 2 - extent.x / 2; 
					default -> throw new IllegalArgumentException("Unexpected value for alignment: " + t.horizontalAlignment());
				};
				
				int y = switch (t.verticalAlignment()) {
					case SWT.TOP -> bb.y + p.top();
					case SWT.BOTTOM -> bb.y + bb.height - extent.y - p.bottom();
					case SWT.CENTER -> bb.y + bb.height / 2 - extent.y / 2;
					default -> throw new IllegalArgumentException("Unexpected value for alignment: " + t.verticalAlignment());
				};
					
				
				gc.setFont(t.font());
				
				// Create a Transform to scale the text to a font size
				final Transform transform = new Transform(Display.getCurrent());
				final float originalFontSize = t.font().getFontData()[0].height;
				final float fontScaling = t.fontSize() / originalFontSize;
				
				transform.scale(fontScaling, fontScaling);
				gc.setTransform(transform);

				gc.drawString(s, (int)(x / fontScaling), (int)(y / fontScaling), t.backgroundColour() == null);

				// Restore the original transform
				transform.identity();
				gc.setTransform(transform);
				transform.dispose();
			
			} else if (b instanceof Image i) {
				gc.drawImage(i.img(), i.x(), i.y());
			} else if(b instanceof Polyline polyline) {
				int[] pointArray = polyline.points().stream().flatMapToInt(o -> IntStream.of(o.x, o.y)).toArray();
				gc.drawPolyline(pointArray);
			} else {
				throw new UnsupportedOperationException("Got a BasicDrawableElement that cannot be drawn by this ScheduleElementDrawer");
			}
		} finally {
			gc.setAlpha(oldAlpha);
		}
	}

	@Override
	public Point findSizeOfText(String text, Font f, int fontSize) {
		gc.setFont(f);
		Point extent = gc.textExtent(text);
		final float originalFontSize = f.getFontData()[0].height;
		final float fontScaling = fontSize / originalFontSize;
		extent.x *= fontScaling;
		extent.y *= fontScaling;
		return extent;
	}

	@Override
	public Point findSizeOfText(Text text) {
		Point textExtent = findSizeOfText(text.text(), text.font(), text.fontSize());
		Padding p = text.padding();
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
