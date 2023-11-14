/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.time.temporal.ChronoUnit;
import java.util.function.UnaryOperator;

public class ScheduleTimeScaleSmoothZoomLevel implements ITimeScaleZoomLevel {

	private static final int MAX_D = 100;
	private static final int MIN_D = 0;

	/*
	 * Distance from the current zoom level. 0 = current, 100 = next.
	 */
	private int d = 0;
	private ScheduleTimeScaleZoomLevel zoomLevel;

	public ScheduleTimeScaleSmoothZoomLevel(ScheduleTimeScaleZoomLevel zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public final int ordinal() {
		return zoomLevel.ordinal();
	}

	@Override
	public boolean isMaxZoom() {
		return zoomLevel.isMaxZoom();
	}

	@Override
	public boolean isMinZoom() {
		return zoomLevel.isMinZoom() && d == MIN_D;
	}

	@Override
	public TimeScaleView getView() {
		return zoomLevel.getView();
	}

	@Override
	public ChronoUnit getWidthUnit() {
		return zoomLevel.getWidthUnit();
	}

	@Override
	public ITimeScaleZoomLevel nextIn() {
		return new ScheduleTimeScaleSmoothZoomLevel((ScheduleTimeScaleZoomLevel) zoomLevel.nextIn());
	}

	@Override
	public ITimeScaleZoomLevel nextOut() {
		return new ScheduleTimeScaleSmoothZoomLevel((ScheduleTimeScaleZoomLevel) zoomLevel.nextOut());
	}

	@Override
	public UnaryOperator<Integer> getScalingFunction() {
		if (isMaxZoom()) {
			return zoomLevel.getScalingFunction();
		}

		return w -> {
			int curr = zoomLevel.getScalingFunction().apply(w);
			int next = zoomLevel.nextIn().getScalingFunction().apply(w);
			return curr + d * (next - curr) / MAX_D;
		};
	}

	public ScheduleTimeScaleSmoothZoomLevel zoomBy(Integer count) {
		int levelsToZoom = Math.abs(count) / MAX_D;
		int newD = d + count % MAX_D;

		levelsToZoom += Math.abs((newD < 0) ? -1 : newD / MAX_D);
		boolean zoomIn = count > 0;
		// Simon's mouse wheel gets a count of 15, resulting in levelsToZoom being 0.
		// So make sure we zoom by one level. 
		// TODO: Is it different on a touchpad?
		if (levelsToZoom == 0 && count != 0) {
			levelsToZoom = zoomIn ? 1 : -1;
		}
		while (levelsToZoom > 0 && !(zoomIn && isMaxZoom())) {
			if (zoomIn) {
				zoomLevel = (ScheduleTimeScaleZoomLevel) zoomLevel.nextIn();
			} else if (zoomLevel.isMinZoom()) {
				d = 0;
				return this;
			} else {
				zoomLevel = (ScheduleTimeScaleZoomLevel) zoomLevel.nextOut();
			}
			levelsToZoom--;
		}

		d = isMaxZoom() ? 0 : Math.floorMod(newD, MAX_D);
		return this;
	}

}
