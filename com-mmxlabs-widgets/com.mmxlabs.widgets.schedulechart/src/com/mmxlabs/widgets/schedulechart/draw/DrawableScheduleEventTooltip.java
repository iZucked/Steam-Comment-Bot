/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.widgets.schedulechart.ScheduleEventTooltip;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Text;

public abstract class DrawableScheduleEventTooltip extends DrawableElement {
	
	protected final ScheduleEventTooltip tooltip;
	protected Point anchor;
	protected int yOffset;
	protected int maxWidth;
	
	protected static final Font SYSTEM_FONT = Display.getDefault().getSystemFont();
	protected static final int SYSTEM_FONT_SIZE = SYSTEM_FONT.getFontData()[0].getHeight();
	protected static final int LINE_SPACING = 5;
	protected static final int TEXT_PADDING = 10;
	private int textExtent = -1;
	
	protected DrawableScheduleEventTooltip(ScheduleEventTooltip tooltip) {
		this.tooltip = tooltip;
		this.maxWidth = 0;
	}
	
	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver r) {
		final int totalHeight = getTotalHeight(r);

		/* If the tooltip won't fit below the anchor, show it above the anchor
		if (anchor.y + yOffset + totalHeight > bounds.y + bounds.height) {
			anchor = new Point(anchor.x, anchor.y - totalHeight);
			yOffset *= -1;

			// If the tooltip won't fit above the anchor, there is no space for the tooltip so return
			if (anchor.y + yOffset - totalHeight < bounds.y) {
				return List.of();
			}
		}*/
		
		// Get maximum width
		Rectangle dummyBounds = new Rectangle(anchor.x, anchor.y + yOffset, 100 ,100);
		for(BasicDrawableElement element : getTooltipBody().getBasicDrawableElements(dummyBounds, r)) {
			if(element instanceof Text text)
				maxWidth = Math.max(maxWidth, text.boundingBox().x - anchor.x + r.findSizeOfText(text).x);
		}
		for(BasicDrawableElement element : getTooltipFooter().getBasicDrawableElements(dummyBounds, r)) {
			if(element instanceof Text text)
				maxWidth = Math.max(maxWidth, text.boundingBox().x - anchor.x + r.findSizeOfText(text).x);
		}
		maxWidth += TEXT_PADDING;
		
		List<BasicDrawableElement> res = new ArrayList<>();
		int y = anchor.y + yOffset;
		int width = getWidth(r);
		
		final DrawableElement bg = getTooltipBackground();
		bg.setBounds(new Rectangle(anchor.x, y, width, totalHeight));
		res.addAll(bg.getBasicDrawableElements(r));
		
		final DrawableElement header = getTooltipHeader();
		if (header != null) {
			int headerHeight = getHeaderHeight(r);
			header.setBounds(new Rectangle(anchor.x, y, width, headerHeight));
			res.addAll(header.getBasicDrawableElements(r));
			y += headerHeight;
		}
		
		final DrawableElement body = getTooltipBody();
		if (body != null) {
			int bodyHeight = getBodyHeight(r);
			body.setBounds(new Rectangle(anchor.x, y, width, bodyHeight));
			res.addAll(body.getBasicDrawableElements(r));
			y += bodyHeight;
		}
		
		final DrawableElement footer = getTooltipFooter();
		if (footer != null) {
			footer.setBounds(new Rectangle(anchor.x, y, width, getFooterHeight(r)));
			res.addAll(footer.getBasicDrawableElements(r));
		}
		
		
		
		return res;
	}
	
	protected int getWidth(DrawerQueryResolver resolver) {
		return Math.max(200, maxWidth);
	}
	protected abstract int getHeaderHeight(DrawerQueryResolver resolver);
	protected int getBodyHeight(DrawerQueryResolver resolver) {
		textExtent = resolver.findSizeOfText("TEST", SYSTEM_FONT, SYSTEM_FONT_SIZE).y;
		int numFields = tooltip.bodyFields().size();
		return (numFields == 0) ? 0 : 2 * TEXT_PADDING + numFields * (textExtent + LINE_SPACING) - LINE_SPACING;
	}
	protected abstract int getFooterHeight(DrawerQueryResolver resolver);
	protected int getTotalHeight(DrawerQueryResolver r) {
		return getHeaderHeight(r) + getBodyHeight(r) + getFooterHeight(r);
	}

	protected abstract DrawableElement getTooltipBackground();
	protected abstract DrawableElement getTooltipHeader();
	protected  DrawableElement getTooltipBody() {
		return new DrawableElement() {
			
			@Override
			protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver r) {
				List<BasicDrawableElement> res = new ArrayList<>();
				
				if (tooltip.bodyFields().isEmpty()) {
					return res;
				}
				
				int maxKeyExtent = tooltip.bodyFields().keySet().stream().map(s -> r.findSizeOfText(s, SYSTEM_FONT, SYSTEM_FONT_SIZE).x).max(Integer::compare).get();
				final int minLineX = bounds.x + (int)(0.1 * bounds.width);
				final int lineX = Math.max(minLineX, bounds.x + TEXT_PADDING + maxKeyExtent + TEXT_PADDING);
				int y = bounds.y + TEXT_PADDING;
				
				// draw line separating keys and values
				res.add(new BasicDrawableElements.Line(lineX, y, lineX, y + getBodyHeight(r) - 2 * TEXT_PADDING, getStrokeColour(), 255, 1));
								
				for (var entry : tooltip.bodyFields().entrySet()) {					
					// add body field
					res.add(BasicDrawableElements.Text.from(lineX - TEXT_PADDING - maxKeyExtent, y, entry.getKey()).textColour(getTextColour()).create());
					// add value
					res.add(BasicDrawableElements.Text.from(lineX + TEXT_PADDING, y, entry.getValue()).textColour(getTextColour()).create());
					y += textExtent + LINE_SPACING;
				}
				
				return res;
			}
		};
	}
	
	protected DrawableElement getTooltipFooter() {
		return new DrawableElement() {
			
			@Override
			protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
				List<BasicDrawableElement> res = new ArrayList<>();
				
				if (tooltip.footerText().isEmpty()) {
					return res;
				}
				
				for(int i = 0; i < tooltip.footerText().size(); i++) {					
					res.add(BasicDrawableElements.Text.from(bounds.x + TEXT_PADDING, bounds.y + (15 * i), tooltip.footerText().get(i)).textColour(getTextColour()).create());
				}
				
				return res;
			}
		};
	}

	public void setAnchor(Point anchor, int yOffset) {
		this.anchor = anchor;
		this.yOffset = yOffset;
	}

	protected abstract Color getBackgroundColour();
	
	protected Color getStrokeColour() {
		return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	}
	
	protected Color getTextColour() {
		return Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	}
}
