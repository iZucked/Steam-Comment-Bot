package com.mmxlabs.widgets.schedulechart.draw;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.IScheduleChartColourScheme;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleTimeScale;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventStylingProvider;

public class DrawableScheduleChartRow extends DrawableElement {

	public static final int SPACER = 3;
	public static final int EVENT_HEIGHT = 20;
	public static final int ROW_HEIGHT = EVENT_HEIGHT + 2 * SPACER;

	private final IScheduleChartColourScheme colourScheme;
	private final IScheduleEventStylingProvider eventStylingProvider;

	private final ScheduleChartRow scr;
	private final ScheduleTimeScale sts;
	private final int rowNum;

	public DrawableScheduleChartRow(final ScheduleChartRow scr, final int rowNum, ScheduleTimeScale sts, IScheduleChartColourScheme colourScheme, IScheduleEventStylingProvider eventStylingProvider) {
		this.scr = scr;
		this.sts = sts;
		this.rowNum = rowNum;
		this.colourScheme = colourScheme;
		this.eventStylingProvider = eventStylingProvider;
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		List<BasicDrawableElement> res = new ArrayList<>();

		// Add background
		res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x, bounds.y, bounds.width, ROW_HEIGHT).bgColour(rowNum % 2 == 0 ? null : colourScheme.getRowBgColour(rowNum))
				.borderColour(colourScheme.getGridStrokeColour()).alpha(160).create());

		for (ScheduleEvent se : scr.getEvents()) {
			int startX = sts.getXForDateTime(se.getStart());
			int endX = sts.getXForDateTime(se.getEnd());
			Rectangle eventBounds = new Rectangle(startX, bounds.y + SPACER, endX - startX, EVENT_HEIGHT);
			res.addAll(new DrawableScheduleEvent(se, eventBounds, eventStylingProvider).getBasicDrawableElements());
		}

		return res;
	}

}
