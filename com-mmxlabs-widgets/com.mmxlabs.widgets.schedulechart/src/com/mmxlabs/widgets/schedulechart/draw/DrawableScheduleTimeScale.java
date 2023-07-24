package com.mmxlabs.widgets.schedulechart.draw;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.widgets.schedulechart.ScheduleTimeScale;

public class DrawableScheduleTimeScale<T extends ScheduleTimeScale> extends DrawableElement {
	
	private final @NonNull T sts;
	
	public DrawableScheduleTimeScale(@NonNull T sts) {
		this.sts = sts;
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds) {
		List<BasicDrawableElement> res = new ArrayList<>();
		
		switch (sts.getUnitWidths().zoomLevel().getView()) {
		case TS_VIEW_DAY ->
			getDrawableElemsForDayView(res, bounds);
		case TS_VIEW_WEEK ->
			getDrawableElemsForWeekView(res, bounds);
		case TS_VIEW_MONTH ->
			getDrawableElemsForMonthView(res, bounds);
		case TS_VIEW_YEAR ->
			getDrawableElemsForYearView(res, bounds);
		default ->
		throw new IllegalArgumentException("Unexpected value: " + sts.getUnitWidths().zoomLevel().getView());
		}
		
		return res;
	}

	private void getDrawableElemsForDayView(List<BasicDrawableElement> res, Rectangle bounds) {
		// TODO Auto-generated method stub
	}

	private void getDrawableElemsForWeekView(List<BasicDrawableElement> res, Rectangle bounds) {
		final LocalDateTime startDate = sts.getStartTime();
		LocalDateTime date;

		int x = bounds.x;
		int y = bounds.y;
		int maxX = x + bounds.width;
		int maxY = y + bounds.height;
		
		// TODO: change to use a settings object
		int headerRowHeight = 24;
		
		Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		Color gray = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		Color bg = new Color(new RGB(230, 239, 249));
		Color white = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		
		// Draw top header
		date = (LocalDateTime) TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY).adjustInto(startDate);
		x = sts.getXForDateTime(date);
		res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x, y, x - bounds.x, headerRowHeight).bgColour(bg).create());
		while (x < maxX) {
			res.add(BasicDrawableElements.Rectangle.withBounds(x, y, sts.getUnitWidths().weekWidth(), headerRowHeight).bgColour(bg).create());
			res.add(new BasicDrawableElements.Text(x + 2, y, date.format(new DateTimeFormatterBuilder().appendPattern("MMM d, ''yy").toFormatter()), black));
			res.add(BasicDrawableElements.Rectangle.withBounds(x, y, sts.getUnitWidths().weekWidth(), headerRowHeight).strokeColour(gray).create());
			x += sts.getUnitWidths().weekWidth();
			date = date.plusWeeks(1);
		}
		
		y += headerRowHeight;
		date = startDate;
		x = sts.getXForDateTime(date);
		while (x < maxX) {
			res.add(BasicDrawableElements.Rectangle.withBounds(x, y, sts.getUnitWidths().dayWidth(), headerRowHeight).bgColour(bg).create());
			if (date.getDayOfWeek().ordinal() < 5) {
				res.add(new BasicDrawableElements.Text(x + 1, y, date.format(new DateTimeFormatterBuilder().appendPattern("EEEEE").toFormatter()), black));
			}
			res.add(BasicDrawableElements.Rectangle.withBounds(x, y, sts.getUnitWidths().dayWidth(), headerRowHeight).strokeColour(gray).create());
			x += sts.getUnitWidths().dayWidth();
			date = date.plusDays(1);
		}
	
		y += headerRowHeight;
		date = startDate;
		x = sts.getXForDateTime(date);
		while (x < maxX) {
			res.add(BasicDrawableElements.Rectangle.withBounds(x, y, sts.getUnitWidths().dayWidth(), maxY - y).bgColour(date.getDayOfWeek().ordinal() < 5 ? white : bg).create());
			res.add(BasicDrawableElements.Rectangle.withBounds(x, y, sts.getUnitWidths().dayWidth(), maxY - y).strokeColour(gray).create());
			x += sts.getUnitWidths().dayWidth();
			date = date.plusDays(1);
		}
	}

	private void getDrawableElemsForMonthView(List<BasicDrawableElement> res, Rectangle bounds) {
		// TODO Auto-generated method stub
	}

	private void getDrawableElemsForYearView(List<BasicDrawableElement> res, Rectangle bounds) {
		// TODO Auto-generated method stub
	}

}
