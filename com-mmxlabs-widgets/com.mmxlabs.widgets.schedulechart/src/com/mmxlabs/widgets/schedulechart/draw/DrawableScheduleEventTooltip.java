/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.ScheduleEventTooltip;

public abstract class DrawableScheduleEventTooltip extends DrawableElement {
	
	protected final ScheduleEventTooltip tooltip;
	protected Point anchor;
	protected int yOffset;
	
	protected DrawableScheduleEventTooltip(ScheduleEventTooltip tooltip) {
		this.tooltip = tooltip;
	}
	
	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver r) {
		final int totalHeight = getTotalHeight(r);

		// If the tooltip won't fit below the anchor, show it above the anchor
		if (anchor.y + yOffset + totalHeight > bounds.y + bounds.height) {
			anchor = new Point(anchor.x, anchor.y - totalHeight);
			yOffset *= -1;

			// If the tooltip won't fit above the anchor, there is no space for the tooltip so return
			if (anchor.y + yOffset - totalHeight < bounds.y) {
				return List.of();
			}
		}
		
		List<BasicDrawableElement> res = new ArrayList<>();
		int y = anchor.y + yOffset;
		int width = getWidth(r);
		
		final DrawableElement bg = getTooltipBackground();
		bg.setBounds(new Rectangle(anchor.x, y, width, totalHeight));
		res.addAll(bg.getBasicDrawableElements(r));
		
		final DrawableElement header = getTooltipHeader();
		if (header != null) {
			header.setBounds(new Rectangle(anchor.x, y, width, getHeaderHeight(r)));
			res.addAll(header.getBasicDrawableElements(r));
			y += getHeaderHeight(r);
		}
		
		final DrawableElement body = getTooltipBody();
		if (body != null) {
			body.setBounds(new Rectangle(anchor.x, y, width, getBodyHeight(r)));
			res.addAll(body.getBasicDrawableElements(r));
			y += getBodyHeight(r);
		}
		
		final DrawableElement footer = getTooltipFooter();
		if (footer != null) {
			footer.setBounds(new Rectangle(anchor.x, y, width, getFooterHeight(r)));
			res.addAll(footer.getBasicDrawableElements(r));
		}
		
		return res;
	}
	
	protected abstract int getWidth(DrawerQueryResolver resolvere);
	protected abstract int getHeaderHeight(DrawerQueryResolver resolver);
	protected abstract int getBodyHeight(DrawerQueryResolver resolver);
	protected abstract int getFooterHeight(DrawerQueryResolver resolver);
	protected int getTotalHeight(DrawerQueryResolver r) {
		return getHeaderHeight(r) + getBodyHeight(r) + getFooterHeight(r);
	}

	protected abstract DrawableElement getTooltipBackground();
	protected abstract DrawableElement getTooltipHeader();
	protected abstract DrawableElement getTooltipBody();
	protected abstract DrawableElement getTooltipFooter();

	public void setAnchor(Point anchor, int yOffset) {
		this.anchor = anchor;
		this.yOffset = yOffset;
	}

	protected abstract Color getBackgroundColour();
	protected abstract Color getStrokeColour();
}
