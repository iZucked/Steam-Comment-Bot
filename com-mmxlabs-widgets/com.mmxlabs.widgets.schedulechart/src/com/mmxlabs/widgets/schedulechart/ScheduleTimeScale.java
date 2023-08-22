/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.common.Pair;

public class ScheduleTimeScale {
	
	private LocalDateTime startTime;
	private int startPixelOffset = 0;

	private TimeScaleUnitWidths unitWidths;
	private Rectangle bounds;
	
	private final IScheduleChartContentBoundsProvider boundsProvider;
	private final IScheduleChartEventEmitter eventEmitter;
	
	public ScheduleTimeScale(IScheduleChartContentBoundsProvider boundsProvider, IScheduleChartEventEmitter eventEmitter, IScheduleChartSettings settings) {
		this.startTime = ((LocalDateTime) TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY).adjustInto(LocalDateTime.now())).minusDays(1).minusYears(1);
		// TODO: make this customisable with the settings object
		this.unitWidths = calculateUnitWidths(new ScheduleTimeScaleSmoothZoomLevel(ScheduleTimeScaleZoomLevel.TS_ZOOM_DAY_NORMAL), 3);
		this.boundsProvider = boundsProvider;
		this.eventEmitter = eventEmitter;
	}
	
	void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	Rectangle getBounds() {
		return bounds;
	}
	
	public void zoomIn() {
		zoomBy(null, null, true);
	}
	
	public void zoomOut() {
		zoomBy(null, null, false);
	}

	public void zoomBy(Point mouseLoc, Integer count, boolean isZoomIn) {
		internalZoomBy(mouseLoc, count, isZoomIn);
		eventEmitter.fireScheduleEvent(l -> l.timeScaleZoomLevelChanged(bounds, boundsProvider));
	}
	
	// returns pixels moved
	public int scroll(int pixelOffset, boolean roundToBoundaries) {
		return internalShiftByOffset(pixelOffset, roundToBoundaries);
	}
	
	public void pack() {
		var leftmost = boundsProvider.getLeftmostEvent();
		var rightmost = boundsProvider.getRightmostEvent();
		
		final int PADDING = 1;
		final int leftmostX = getXForDateTime(leftmost.getStart().minusDays(PADDING));
		final int rightmostX = getXForDateTime(rightmost.getStart().plusDays(PADDING));
		
		internalZoomToFitXBounds(leftmostX, rightmostX);
		eventEmitter.fireScheduleEvent(l -> l.timeScaleZoomLevelChanged(bounds, boundsProvider));
	}

	public void fitSelection(int startX, int endX) {
		internalZoomToFitXBounds(startX, endX);
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

		long unitsBetween = widthUnit.between(truncatedStart, d);
		int unitWidth = getUnitWidthFromRegularUnit(widthUnit).get();
		return bounds.x + (int) (unitWidth * unitsBetween) - startPixelOffset;
	}
	
	/**
	 * Calculates the date time on the time scale for a given x distance.
	 * Negative x distances are not allowed, so we return startTime for these values. 
	 * 
	 * @param x distance to get the date time for
	 * @return pair of calculated date time and remainder from the previous date boundary in pixels (not including offset).
	 */
	public Pair<LocalDateTime, Integer> getDateTimeAtX(int x) {
		int offsetAdjustedX = x + startPixelOffset - bounds.x;
		ChronoUnit widthUnit = unitWidths.zoomLevel.getWidthUnit();
		LocalDateTime truncatedStart = startTime.truncatedTo(widthUnit);

		// NOTE: integer division, round to boundary happens here
		int unitWidth = getUnitWidthFromRegularUnit(widthUnit).get();
		int unitsBetween = offsetAdjustedX / unitWidth;
		int remainder = offsetAdjustedX % unitWidth;
		return Pair.of(truncatedStart.plus(Duration.of(unitsBetween, widthUnit)), remainder);
	}

	public Optional<Integer> getUnitWidthFromRegularUnit(ChronoUnit unit) {
		return switch (unit) {
			case HOURS -> Optional.of(unitWidths.hourWidth());
			case DAYS -> Optional.of(unitWidths.dayWidth());
			case WEEKS -> Optional.of(unitWidths.weekWidth());
			default -> Optional.empty();
		};
	}
	
	public Pair<Integer, Integer> getXBoundsFromEvent(ScheduleEvent event) {
		return Pair.of(getXForDateTime(event.getStart()), getXForDateTime(event.getEnd()));
	}
	
	public int getTotalContentWidthInUnits() {
		return (int) boundsProvider.getLeftmostEvent().getStart().until(boundsProvider.getRightmostEvent().getEnd(), unitWidths.zoomLevel.getWidthUnit());
	}
	
	public int getScrollPositionInUnits() {
		return (int) boundsProvider.getLeftmostEvent().getStart().until(startTime, unitWidths.zoomLevel.getWidthUnit());
	}

	private int getUnitWidthForCurrentZoomLevel() {
		return getUnitWidthFromRegularUnit(unitWidths.zoomLevel.getWidthUnit()).get();
	}
	
	private int internalShiftByOffset(final int pixelOffset, final boolean roundToBoundaries) {
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

	private void internalZoomBy(Point mouseLoc, Integer count, boolean isZoomIn) {
		if (isZoomIn && this.unitWidths.zoomLevel().isMaxZoom()) {
			return;
		}
		
		if (!isZoomIn && count == null && this.unitWidths.zoomLevel().isMinZoom()) {
			return;
		}
		
		Integer x = mouseLoc != null ? mouseLoc.x : bounds != null ? bounds.x + bounds.width / 2 : null;
		Pair<LocalDateTime, Integer> preZoomPos = x != null ? getDateTimeAtX(x) : null;
		int preZoomUnitWidth = getUnitWidthForCurrentZoomLevel();

		var zoomLevel = unitWidths.zoomLevel();
		var nextZoomLevel = count != null ? zoomLevel.zoomBy(count) : isZoomIn ? zoomLevel.nextIn() : zoomLevel.nextOut();
		this.unitWidths = calculateUnitWidths((ScheduleTimeScaleSmoothZoomLevel) nextZoomLevel, 3);

		if (preZoomPos != null) {
			internalSetDateTimeAtX(x, preZoomPos, preZoomUnitWidth);
		}
	}
	
	private void internalSetDateTimeAtX(final int x, final Pair<LocalDateTime, Integer> dateOffset, Integer prevUnitWidth) {
		int currUnitWidth = getUnitWidthForCurrentZoomLevel();
		int currRemainder = (prevUnitWidth != null) ? (int) Math.round(dateOffset.getSecond() * (double) currUnitWidth / prevUnitWidth) : dateOffset.getSecond();
		int currXForDate = getXForDateTime(dateOffset.getFirst());
		internalShiftByOffset(currXForDate - x + currRemainder, false);
	}
	
	private void internalZoomToFitXBounds(int xStart, int xEnd) {
		final LocalDateTime xEndDate = getDateTimeAtX(xEnd).getFirst();
		final int maximumX = bounds.x + bounds.width;

		// Anchor the left at the start
		internalShiftByOffset(xStart - bounds.x, true);
		Point leftAnchor = new Point(bounds.x, bounds.y);
		
		// Find right level of zoom in binary search like fashion
		int r = getXForDateTime(xEndDate);
		final int minDeltaZoom = 1;
		int deltaZoom = r < maximumX ? minDeltaZoom : -minDeltaZoom;
		boolean startDirection = r < maximumX;
		
		// keep zooming until maximum x is crossed
		do {
			internalZoomBy(leftAnchor, deltaZoom, deltaZoom > 0);
			r = getXForDateTime(xEndDate);
			
			deltaZoom *= 2;
		} while (r < maximumX == startDirection && !(unitWidths.zoomLevel().isMaxZoom() || unitWidths.zoomLevel().isMinZoom()));
		
		// switch direction and take increasingly small jumps towards maximum x
		deltaZoom /= -2;
		boolean direction = !startDirection;
		boolean oscillating = false;
		int[] prev = new int[2];
		while (Math.abs(r - maximumX) > unitWidths.dayWidth() && !(oscillating && r < maximumX)) {
			internalZoomBy(leftAnchor, deltaZoom, deltaZoom > 0);
			r = getXForDateTime(xEndDate);
			
			// Update delta zoom
			deltaZoom = (Math.abs(deltaZoom) <= minDeltaZoom) ? minDeltaZoom  * Integer.signum(deltaZoom) : deltaZoom / 2;
			if (r < maximumX != direction) {
				deltaZoom *= -1;
				direction = !direction;
			}
			
			// Track last two values of r to check for oscillation
			if (r == prev[1]) {
				oscillating = true;
			} else {
				prev[1] = prev[0];
				prev[0] = r;
			}
		}
	}

	private TimeScaleUnitWidths calculateUnitWidths(ScheduleTimeScaleSmoothZoomLevel zoomLevel, int defaultYearDayWidth) {
		int width = defaultYearDayWidth;
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
	
	public record TimeScaleUnitWidths(ScheduleTimeScaleSmoothZoomLevel zoomLevel, int hourWidth, int dayWidth, int weekWidth) {}
}
