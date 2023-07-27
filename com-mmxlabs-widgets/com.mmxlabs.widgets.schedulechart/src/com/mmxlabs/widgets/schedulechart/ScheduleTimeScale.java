package com.mmxlabs.widgets.schedulechart;

import com.mmxlabs.common.Pair;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class ScheduleTimeScale {
	
	private LocalDateTime startTime;
	private int startPixelOffset = 0;

	private TimeScaleUnitWidths unitWidths;
	private Rectangle bounds;
	
	
	public ScheduleTimeScale() {
		this.startTime = ((LocalDateTime) TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY).adjustInto(LocalDateTime.now())).minusDays(1);
		// TODO: make this customisable with a settings object
		this.unitWidths = calculateUnitWidths(new ScheduleTimeScaleSmoothZoomLevel(ScheduleTimeScaleZoomLevel.TS_ZOOM_DAY_NORMAL), 16, 6, 3);
	}
	
	void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	Rectangle getBounds() {
		return bounds;
	}
	
	public void zoomIn() {
		zoomInBy(null, null);
	}
	
	public void zoomInBy(Point mouseLoc, Integer count) {
		if (this.unitWidths.zoomLevel().isMaxZoom()) {
			return;
		}
		
		Integer x = mouseLoc != null ? mouseLoc.x : bounds != null ? bounds.x + bounds.width / 2 : null;
		Pair<LocalDateTime, Integer> preZoom = x != null ? getDateTimeAtX(x) : null;
		var preZoomWidths = unitWidths;
		
		this.unitWidths = calculateUnitWidths(count == null ? (ScheduleTimeScaleSmoothZoomLevel) unitWidths.zoomLevel.nextIn() : unitWidths.zoomLevel.zoomBy(count), 16, 6, 3);
		
		if (preZoom != null) {
			internalSetDateTimeAtX(x, preZoom, preZoomWidths);
		}
	}
	
	public void zoomOut() {
		zoomOutBy(null, null);
	}
	
	public void zoomOutBy(Point mouseLoc, Integer count) {
		if (count == null && this.unitWidths.zoomLevel().isMinZoom()) {
			return;
		}

		Integer x = mouseLoc != null ? mouseLoc.x : bounds != null ? bounds.x + bounds.width / 2 : null;
		Pair<LocalDateTime, Integer> preZoom = x != null ? getDateTimeAtX(x) : null;
		var preZoomWidths = unitWidths;
		
		
		this.unitWidths = calculateUnitWidths(count == null ? (ScheduleTimeScaleSmoothZoomLevel) unitWidths.zoomLevel.nextOut() : unitWidths.zoomLevel.zoomBy(count), 16, 6, 3);
		

		if (preZoom != null) {
			internalSetDateTimeAtX(x, preZoom, preZoomWidths);
		}
	}
	
	// returns pixels moved
	public int scroll(int pixelOffset, boolean roundToBoundaries) {
		int unitWidth = getUnitWidthForCurrentZoomLevel();
		
		int unitsBetween = pixelOffset / unitWidth;
		int newOffset = startPixelOffset + pixelOffset % unitWidth;
		
		unitsBetween += (newOffset < 0) ? -1 : newOffset / unitWidth;
		Duration offsetDuration = Duration.of(unitsBetween, unitWidths.zoomLevel.getWidthUnit());

		int actualPixelsScrolled = pixelOffset;
		if (roundToBoundaries) {
			newOffset = 0;
			actualPixelsScrolled = unitsBetween * unitWidth + startPixelOffset;
		}

		startTime = startTime.plus(offsetDuration);
		startPixelOffset = Math.floorMod(newOffset, unitWidth);

		return actualPixelsScrolled;
	}
	
	public LocalDateTime getStartTime() {
		return startTime;
	}

	public TimeScaleUnitWidths getUnitWidths() {
		return unitWidths;
	}
	
	/**
	 * Calculates the x distance on the time scale for a given date time.
	 * Negative x distances are not allowed, so we return 0 for values before the startTime. Rounds to boundary by default.
	 * 
	 * @param d the date to get x for
	 * @return the calculated x value
	 */
	public int getXForDateTime(LocalDateTime d) {
		ChronoUnit widthUnit = unitWidths.zoomLevel.getWidthUnit();
		LocalDateTime truncatedStart = startTime.truncatedTo(widthUnit);

		if (d.isBefore(truncatedStart)) {
			return 0 - startPixelOffset;
		}
		
		long unitsBetween = widthUnit.between(truncatedStart, d);
		int unitWidth = getUnitWidthFromUnit(widthUnit);
		return (int) (unitWidth * unitsBetween) - startPixelOffset;
	}
	
	/**
	 * Calculates the date time on the time scale for a given x distance.
	 * Negative x distances are not allowed, so we return startTime for these values. 
	 * 
	 * @param x distance to get the date time for
	 * @return pair of calculated date time and remainder in pixels (includes startPixelOffset)
	 */
	public Pair<LocalDateTime, Integer> getDateTimeAtX(int x) {
		int offsetAdjustedX = x + startPixelOffset;
		ChronoUnit widthUnit = unitWidths.zoomLevel.getWidthUnit();
		LocalDateTime truncatedStart = startTime.truncatedTo(widthUnit);

		if (offsetAdjustedX <= 0) {
			return Pair.of(truncatedStart, 0);
		}
		
		// NOTE: integer division, round to boundary happens here
		int unitWidth = getUnitWidthFromUnit(widthUnit);
		int unitsBetween = offsetAdjustedX / unitWidth;
		int remainder = (offsetAdjustedX % unitWidth) + startPixelOffset;
		return Pair.of(truncatedStart.plus(Duration.of(unitsBetween, widthUnit)), remainder);
	}
	
	public int getUnitWidthForCurrentZoomLevel() {
		return getUnitWidthFromUnit(unitWidths.zoomLevel.getWidthUnit());
	}
	
	private void internalSetDateTimeAtX(int x, Pair<LocalDateTime, Integer> dateOffset, TimeScaleUnitWidths prevUnitWidths) {
		ChronoUnit currWidthUnit = unitWidths.zoomLevel().getWidthUnit();
		ChronoUnit prevWidthUnit = unitWidths.zoomLevel().getWidthUnit();

		Pair<LocalDateTime, Integer> dateOffsetAtX = getDateTimeAtX(x);

		long unitsBetween = dateOffset.getFirst().until(dateOffsetAtX.getFirst(), currWidthUnit);
		startTime = startTime.minus(unitsBetween, currWidthUnit);

		long pxFromStartTimeToOldDate = Period.between(startTime.toLocalDate(), dateOffset.getFirst().toLocalDate()).get(prevWidthUnit) * getUnitWidthFromUnit(prevWidthUnit);
		long pxFromStartTimeToCurrDate = Period.between(startTime.toLocalDate(), dateOffsetAtX.getFirst().toLocalDate()).get(currWidthUnit) * getUnitWidthFromUnit(currWidthUnit);
		startPixelOffset = (int) Math.floorDiv(pxFromStartTimeToCurrDate - pxFromStartTimeToOldDate + dateOffsetAtX.getSecond() - dateOffset.getSecond() + startPixelOffset, getUnitWidthForCurrentZoomLevel());
	}
	
//	private void internalSetDateTimeAtX(int x, Pair<LocalDateTime, Integer> dateOffset, ITimeScaleZoomLevel prevZoomLevel) {
//		ChronoUnit widthUnit = unitWidths.zoomLevel.getWidthUnit();
//		Pair<LocalDateTime, Integer> dateOffsetAtX = getDateTimeAtX(x);
//		long unitsBetween = dateOffset.getFirst().until(dateOffsetAtX.getFirst(), widthUnit);
//		startTime = startTime.minus(unitsBetween, widthUnit);
////		startPixelOffset = (int) Math.round(unitsBetween * getUnitWidthForCurrentZoomLevel() + (dateOffsetAtX.getSecond() - dateOffset.getSecond() * offsetScalingFactor) - x);
//		startPixelOffset = 0;
//		var offsetAtX = getDateTimeAtX(x);
//	}
//
//	private void internalSetDateTimeAtX(int x, Pair<LocalDateTime, Integer> dateOffset) {
//		ChronoUnit widthUnit = unitWidths.zoomLevel.getWidthUnit();
//		Pair<LocalDateTime, Integer> dateOffsetAtX = getDateTimeAtX(x);
//		long unitsBetween = dateOffset.getFirst().until(dateOffsetAtX.getFirst(), widthUnit);
//		startTime = startTime.minus(unitsBetween, widthUnit);
//	}

	private TimeScaleUnitWidths calculateUnitWidths(ScheduleTimeScaleSmoothZoomLevel zoomLevel, int defaultWeekDayWidth, int defaultMonthDayWidth, int defaultYearDayWidth) {
		int width = switch (zoomLevel.getView()) {
			case TS_VIEW_DAY, TS_VIEW_WEEK -> defaultWeekDayWidth;
			case TS_VIEW_MONTH -> defaultMonthDayWidth;
			case TS_VIEW_YEAR -> defaultYearDayWidth;
			default -> throw new IllegalArgumentException("Unexpected value: " + zoomLevel.getView());
		};
		
		int hourWidth = 0;
		int dayWidth = 0;
		if (zoomLevel.getWidthUnit() == ChronoUnit.HOURS) {
			hourWidth = zoomLevel.getScalingFunction().apply(width);
			dayWidth = hourWidth * 24;
		} else if (zoomLevel.getWidthUnit() == ChronoUnit.DAYS) {
			dayWidth = zoomLevel.getScalingFunction().apply(width);
			hourWidth = dayWidth / 24;
		} else {
			throw new IllegalArgumentException("Unexpected time unit " + zoomLevel.getWidthUnit() + " for calculating unit widths");
		}
		
		int weekWidth = dayWidth * 7;
		
		return new TimeScaleUnitWidths(zoomLevel, hourWidth, dayWidth, weekWidth);
	}
	
	private int getUnitWidthFromUnit(ChronoUnit unit) {
		return switch (unit) {
			case HOURS -> unitWidths.hourWidth();
			case DAYS -> unitWidths.dayWidth();
			case WEEKS -> unitWidths.weekWidth();
			default -> throw new IllegalArgumentException("Unexpected value: " + unit);
		};
	}
	
	public record TimeScaleUnitWidths(ScheduleTimeScaleSmoothZoomLevel zoomLevel, int hourWidth, int dayWidth, int weekWidth) {}
}
