/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.events;

import java.util.List;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell.BuySellEventTooltip;
import com.mmxlabs.widgets.schedulechart.ScheduleEventTooltip;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.DrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;

/**
 * @deprecated
 * TODO: find what is actually inherited by its successor - {@link BuySellEventTooltip},
 * move the methods that are missing to buysell and delete the class below.
 * This class is not completed wip.
 */
public abstract class AbstractTextContentNinetyDayEventTooltip extends AbstractNinetyDayEventTooltip {
	
	protected static final Font SYSTEM_FONT = Display.getDefault().getSystemFont();
	protected static final int SYSTEM_FONT_SIZE = SYSTEM_FONT.getFontData()[0].getHeight();
	protected static final int LINE_SPACING = 5;
	protected static final int TEXT_PADDING = 10;
	private int textExtent = -1;
	
	protected AbstractTextContentNinetyDayEventTooltip(final ScheduleEventTooltip tooltip) {
		super(tooltip);
	}
	
	@Override
	protected int getHeaderHeight(DrawerQueryResolver r) {
		return 40;
	}

	@Override
	protected int getBodyHeight(final DrawerQueryResolver resolver) {
		textExtent = (textExtent < 0) ? resolver.findSizeOfText("Jg", SYSTEM_FONT, SYSTEM_FONT_SIZE).y : textExtent;
		int numFields = tooltip.bodyFields().size();
		return (numFields == 0) ? 0 : 2 * TEXT_PADDING + numFields * (textExtent + LINE_SPACING) - LINE_SPACING;
	}
	
	@Override
	protected int getFooterHeight(final DrawerQueryResolver resolver) {
		return 0;
	}

	@Override
	protected DrawableElement getTooltipFooter() {
		return new DrawableElement() {
			
			@Override
			protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
				return List.of();
			}
		};
	}
	
	public int getTextExtent() {
		return textExtent;
	}
}
