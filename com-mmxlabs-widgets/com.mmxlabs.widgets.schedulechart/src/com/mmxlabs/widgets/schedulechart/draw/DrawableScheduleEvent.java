package com.mmxlabs.widgets.schedulechart.draw; 
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEventSelectionState;

public abstract class DrawableScheduleEvent extends DrawableElement {
	
	private final ScheduleEvent se;
	private ScheduleEventSelectionState selectionState;
	
	protected DrawableScheduleEvent(ScheduleEvent se, Rectangle bounds, boolean noneSelected) {
		this.se = se;
		this.selectionState = noneSelected ? ScheduleEventSelectionState.SELECTED : se.getSelectionState();
		setBounds(bounds);
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		List<BasicDrawableElement> res = new ArrayList<>();

		if (!se.isVisible()) {
			return res;
		}
		
		res.add(BasicDrawableElements.Rectangle.withBounds(bounds)
				.bgColour(getBackgroundColour())
				.border(getBorderColour(), getBorderThickness(selectionState))
				.alpha(getAlpha(selectionState))
				.create());

		return res;
	}

	public ScheduleEvent getScheduleEvent() {
		return se;
	}

	protected abstract Color getBackgroundColour();
	protected abstract Color getBorderColour();
	protected abstract int getBorderThickness(ScheduleEventSelectionState s);
	protected abstract int getAlpha(ScheduleEventSelectionState s);
}
