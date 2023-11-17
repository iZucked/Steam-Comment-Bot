/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.events;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.widgets.schedulechart.ScheduleEventTooltip;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements;
import com.mmxlabs.widgets.schedulechart.draw.DrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;

public class JourneyEventTooltip extends AbstractNinetyDayEventTooltip {
	
	public JourneyEventTooltip(ScheduleEventTooltip tooltip) {
		super(tooltip);
	}

	@Override
	protected int getHeaderHeight(DrawerQueryResolver r) {
		return 40;
	}

	@Override
	protected int getFooterHeight(DrawerQueryResolver r) {
		return 40;
	}

	@Override
	protected DrawableElement getTooltipHeader() {
		return new DrawableElement() {
			
			@Override
			protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver resolver) {
				List<BasicDrawableElement> res = new ArrayList<>();
				
				if(tooltip.headerNames().isEmpty()) {
					return res;
				}
				
				final String from = tooltip.headerNames().get(0);
				final String to = tooltip.headerNames().get(1);
				final Point fromExtent = resolver.findSizeOfText(from, SYSTEM_FONT, SYSTEM_FONT_SIZE);
				final Point toExtent = resolver.findSizeOfText(to, SYSTEM_FONT, SYSTEM_FONT_SIZE);
				final int toStartPos = bounds.x + bounds.width - toExtent.x - TEXT_PADDING;
				final int textHeight = Math.max(fromExtent.y, toExtent.y);

				res.add(BasicDrawableElements.Text.from(bounds.x + TEXT_PADDING, bounds.y + TEXT_PADDING, from).textColour(getTextColour()).create());
				res.add(BasicDrawableElements.Text.from(toStartPos, bounds.y + TEXT_PADDING, to).textColour(getTextColour()).create());
				
				final int arrowStart = bounds.x + fromExtent.x + 2 * TEXT_PADDING;
				final int arrowWidth = toStartPos - TEXT_PADDING - arrowStart;
				
				if (arrowWidth > textHeight) {
					addElementsForArrow(res, new Rectangle(arrowStart, bounds.y + TEXT_PADDING, arrowWidth, textHeight));
				}

				return res;
			}
			
			private void addElementsForArrow(List<BasicDrawableElement> res, Rectangle b) {
				final int center = b.y + b.height / 2 + 2;
				final int maxX = b.x + b.width;
				final int headSize = b.height / 2 - 4;
				res.add(new BasicDrawableElements.Line(b.x, center, maxX, center, getStrokeColour(), 255, 2));
				res.add(new BasicDrawableElements.Line(maxX - headSize, center - headSize, maxX, center, getStrokeColour(), 255, 2));
				res.add(new BasicDrawableElements.Line(maxX - headSize, center + headSize, maxX, center, getStrokeColour(), 255, 2));
			}
		};
	}


}
