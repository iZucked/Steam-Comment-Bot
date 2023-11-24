/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.annotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleEventAnnotation;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEventAnnotation;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;

public class NinetyDayCanalJourneyDefaultAnnotation extends DrawableScheduleEventAnnotation {

	private final DrawableScheduleEvent dse;
	
	public NinetyDayCanalJourneyDefaultAnnotation(ScheduleEventAnnotation annotation, DrawableScheduleEvent dse, IScheduleChartSettings settings, ToIntFunction<LocalDateTime> f) {
		super(annotation, dse, settings, f);
		this.dse = dse;
	}
	
	@Override
	protected Rectangle calculateAnnotationBounds(Rectangle scheduleEventBounds) {
		final int height = settings.getEventHeight() + 1; // For some reason rendered with height - 1
		final int width = 2;
		final int x = dateXCoords[0] - width / 2;
		final int y = scheduleEventBounds.y;
		return new Rectangle(x, y, width, height);
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		final List<BasicDrawableElement> res = new ArrayList<>();
		final Color bg = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		Rectangle lineOneBounds = new Rectangle(bounds.x , bounds.y, bounds.width, bounds.height);
		Rectangle lineTwoBounds = new Rectangle(bounds.x , bounds.y, bounds.width, bounds.height);
		
		lineOneBounds.x = lineOneBounds.x - bounds.width;
		lineTwoBounds.x = lineTwoBounds.x + bounds.width;
		
		res.add(BasicDrawableElements.Rectangle.withBounds(lineOneBounds).bgColour(bg).alpha(dse.getAlpha()).create());
		res.add(BasicDrawableElements.Rectangle.withBounds(lineTwoBounds).bgColour(bg).alpha(dse.getAlpha()).create());
		return res;
	}

}
