package com.mmxlabs.widgets.schedulechart.draw; 
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventStylingProvider;

public class DrawableScheduleEvent extends DrawableElement {
	
	private final ScheduleEvent se;
	private final IScheduleEventStylingProvider eventStylingProvider;
	private final boolean noneSelected;
	
	public DrawableScheduleEvent(ScheduleEvent se, Rectangle bounds, IScheduleEventStylingProvider eventStylingProvider, boolean noneSelected) {
		this.se = se;
		this.eventStylingProvider = eventStylingProvider;
		this.noneSelected = noneSelected;
		setBounds(bounds);
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		List<BasicDrawableElement> res = new ArrayList<>();

		if (!se.isVisible()) {
			return res;
		}
		
		res.add(BasicDrawableElements.Rectangle.withBounds(bounds).bgColour(eventStylingProvider.getBackgroundColour(se))
				.border(eventStylingProvider.getBorderColour(se), eventStylingProvider.getBorderThickness(se) + (se.isSelected() ? 2 : 0))
				.alpha(noneSelected || se.isSelected() ? BasicDrawableElements.MAX_ALPHA : 100).create());
		return res;
	}

	public ScheduleEvent getScheduleEvent() {
		return se;
	}

}
