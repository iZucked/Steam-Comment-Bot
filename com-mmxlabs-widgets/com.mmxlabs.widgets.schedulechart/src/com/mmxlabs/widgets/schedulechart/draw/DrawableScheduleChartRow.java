package com.mmxlabs.widgets.schedulechart.draw;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleTimeScale;

public class DrawableScheduleChartRow extends DrawableElement {
	
	public static int SPACER = 2;
	public static int EVENT_HEIGHT = 20;
	public static int ROW_HEIGHT = EVENT_HEIGHT + 2 * SPACER;
	
	private static final Color ALT_BG_COLOUR = new Color(new RGB(230, 239, 249));
	
	private final ScheduleChartRow scr;
	private final ScheduleTimeScale sts;
	private final int rowNum;
	
	public DrawableScheduleChartRow(final ScheduleChartRow scr, final int rowNum, ScheduleTimeScale sts) {
		this.scr = scr;
		this.sts = sts;
		this.rowNum = rowNum;
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		List<BasicDrawableElement> res = new ArrayList<>();
		
		// Add background
		// res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x, bounds.y, bounds.width, ROW_HEIGHT).bgColour(rowNum % 2 == 0 ? null : ALT_BG_COLOUR).alpha(40).create());
		
		for (ScheduleEvent se: scr.getEvents()) {
			int startX = sts.getXForDateTime(se.getStart());
			int endX = sts.getXForDateTime(se.getEnd());
			Rectangle eventBounds = new Rectangle(startX, bounds.y + SPACER, endX - startX, EVENT_HEIGHT);
			res.addAll(new DrawableScheduleEvent(se, eventBounds).getBasicDrawableElements());
		}
		
		return res;
	}
	
}
