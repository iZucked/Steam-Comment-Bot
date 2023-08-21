package com.mmxlabs.lingo.reports.views.ninetydayschedule.events;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.widgets.schedulechart.ScheduleEventTooltip;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements;
import com.mmxlabs.widgets.schedulechart.draw.DrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEventTooltip;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;

public abstract class AbstractNinetyDayEventTooltip extends DrawableScheduleEventTooltip {
	
	protected AbstractNinetyDayEventTooltip(ScheduleEventTooltip tooltip) {
		super(tooltip);
	}
	
	@Override
	protected int getWidth(DrawerQueryResolver r) {
		return 200;
	}

	@Override
	protected DrawableElement getTooltipBackground() {
		return new DrawableElement() {
			
			@Override
			protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver r) {
				List<BasicDrawableElement> res = new ArrayList<>();
				int shadowOffset = 2;
				res.add(BasicDrawableElements.Rectangle.withBounds(anchor.x + shadowOffset, anchor.y + shadowOffset + yOffset, getWidth(r), getTotalHeight(r)).bgColour(getBackgroundShadowColour()).border(getBackgroundShadowColour(), 2).alpha(150).create());
				res.add(BasicDrawableElements.Rectangle.withBounds(anchor.x, anchor.y + yOffset, getWidth(r), getTotalHeight(r)).bgColour(getBackgroundColour()).border(getStrokeColour(), 2).create());
				return res;
			}
		};
	}

	protected Color getBackgroundColour() {
		return new Color(new RGB(255, 255, 202));
	}
	
	protected Color getBackgroundShadowColour() {
		return new Color(new RGB(64, 64, 64));
	}

	protected Color getStrokeColour() {
		return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	}
	
	protected Color getTextColour() {
		return Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	}
}
