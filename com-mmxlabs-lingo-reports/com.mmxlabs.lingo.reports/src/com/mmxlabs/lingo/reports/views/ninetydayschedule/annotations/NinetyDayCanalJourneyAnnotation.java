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

public class NinetyDayCanalJourneyAnnotation extends DrawableScheduleEventAnnotation {
	
	private final DrawableScheduleEvent dse;
	
	public NinetyDayCanalJourneyAnnotation(ScheduleEventAnnotation annotation, DrawableScheduleEvent dse, IScheduleChartSettings settings, ToIntFunction<LocalDateTime> f) {
		super(annotation, dse, settings, f);
		this.dse = dse;
	}

	@Override
	protected Rectangle calculateAnnotationBounds(Rectangle scheduleEventBounds) {
		int height = settings.getBottomAnnotationHeight() + settings.getSpacerWidth();
		int width = height;
		int x = dateXCoords[0] - width / 2;
		int y = scheduleEventBounds.y + settings.getEventHeight();
		return new Rectangle(x, y, width, height);
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		List<BasicDrawableElement> res = new ArrayList<>();
		int x = bounds.x;
		int y = bounds.y;
		int maxX = x + bounds.width;
		int maxY = y + bounds.height;
		
		Color bg = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		Color text = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		
		res.add(BasicDrawableElements.Polygon.fromTriangle(x, maxY, dateXCoords[0], y, maxX, maxY).bgColour(bg).alpha(dse.getAlpha()).create());
//		res.add(BasicDrawableElements.Text.from(x, y, "P").textColour(text).create());
		return res;
	}

}
