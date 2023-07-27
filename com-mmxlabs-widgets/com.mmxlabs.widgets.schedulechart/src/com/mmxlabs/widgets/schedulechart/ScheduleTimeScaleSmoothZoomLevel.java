package com.mmxlabs.widgets.schedulechart;

import java.time.temporal.ChronoUnit;
import java.util.function.UnaryOperator;

public class ScheduleTimeScaleSmoothZoomLevel implements ITimeScaleZoomLevel {
	
	private static final int MAX_D = 100;
	private static final int MIN_D = 0;

	/*
	 * Distance from the current zoom level.
	 * 0 = current, 100 = next.
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
		this.zoomLevel = (ScheduleTimeScaleZoomLevel) zoomLevel.nextIn();
		return this;
	}

	@Override
	public ITimeScaleZoomLevel nextOut() {
		this.zoomLevel = (ScheduleTimeScaleZoomLevel) zoomLevel.nextOut();
		return this;
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
