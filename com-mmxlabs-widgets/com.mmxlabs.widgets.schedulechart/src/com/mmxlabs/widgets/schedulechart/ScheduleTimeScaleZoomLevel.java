package com.mmxlabs.widgets.schedulechart;

import java.time.temporal.ChronoUnit;
import java.util.function.UnaryOperator;

/**
 * Responsible for storing the different zoom levels available.
 * Each level is associated with its TimeScaleView, and a scaling function that calculates the scaled width for the given unit time from the default width.
 * 
 * @author Ashvin
 */
public enum ScheduleTimeScaleZoomLevel implements ITimeScaleZoomLevel {
	
	TS_ZOOM_DAY_MAX(TimeScaleView.TS_VIEW_WEEK, ChronoUnit.DAYS, w -> w * 3),
	TS_ZOOM_DAY_MEDIUM(TimeScaleView.TS_VIEW_WEEK, ChronoUnit.DAYS, w -> w * 2),
	TS_ZOOM_DAY_NORMAL(TimeScaleView.TS_VIEW_WEEK, ChronoUnit.DAYS, w -> w),
	TS_ZOOM_MONTH_MAX(TimeScaleView.TS_VIEW_MONTH, ChronoUnit.DAYS, w -> w + 6),
	TS_ZOOM_MONTH_MEDIUM(TimeScaleView.TS_VIEW_MONTH, ChronoUnit.DAYS, w -> w + 3),
	TS_ZOOM_MONTH_NORMAL(TimeScaleView.TS_VIEW_MONTH, ChronoUnit.DAYS, w -> w),
	TS_ZOOM_YEAR_MAX(TimeScaleView.TS_VIEW_YEAR, ChronoUnit.DAYS, w -> w + 2);
	
	private static final ITimeScaleZoomLevel[] vals = values();
	
	public final TimeScaleView view;
	private final ChronoUnit unit;
	private final UnaryOperator<Integer> scalingFunction;
	
	private ScheduleTimeScaleZoomLevel(TimeScaleView view, ChronoUnit unit, UnaryOperator<Integer> scalingFunction) {
		this.view = view;
		this.unit = unit;
		this.scalingFunction = scalingFunction;
	}
	
	@Override
	public ITimeScaleZoomLevel nextIn() {
		return vals[(this.ordinal() + vals.length - 1) % vals.length];
	}

	@Override
	public ITimeScaleZoomLevel nextOut() {
		return vals[(this.ordinal() + 1) % vals.length];
	}
	
	@Override
	public boolean isMaxZoom() {
		return this.ordinal() == 0;
	}
	
	@Override
	public boolean isMinZoom() {
		return this.ordinal() == vals.length - 1;
	}
	
	@Override
	public TimeScaleView getView() {
		return view;
	}
	
	@Override
	public ChronoUnit getWidthUnit() {
		return unit;
	}
	
	@Override
	public UnaryOperator<Integer> getScalingFunction() {
		return scalingFunction;
	}
	
}

/**
 * 
 * 
 * private void updateZoomLevel() {
		final int originalDayWidth = _settings.getDayWidth();
		final int originalMonthWeekWidth = _settings.getMonthDayWidth();
		final int originalYearMonthDayWidth = _settings.getYearMonthDayWidth();

		boolean dDay = false;
		if (_currentView == ISettings.VIEW_D_DAY) {
			dDay = true;
		}

		switch (_zoomLevel) {
		// hour
		case ISettings.ZOOM_HOURS_MAX:
			_currentView = ISettings.VIEW_DAY;
			_dayWidth = originalDayWidth * 6;
			break;
		case ISettings.ZOOM_HOURS_MEDIUM:
			_currentView = ISettings.VIEW_DAY;
			_dayWidth = originalDayWidth * 4;
			break;
		case ISettings.ZOOM_HOURS_NORMAL:
			_currentView = ISettings.VIEW_DAY;
			_dayWidth = originalDayWidth * 2;
			break;
		// mDay
		case ISettings.ZOOM_DAY_MAX:
			_currentView = ISettings.VIEW_WEEK;
			_dayWidth = originalDayWidth * 3;
			break;
		case ISettings.ZOOM_DAY_MEDIUM:
			_currentView = ISettings.VIEW_WEEK;
			_dayWidth = originalDayWidth * 2;
			break;
		case ISettings.ZOOM_DAY_NORMAL:
			_currentView = ISettings.VIEW_WEEK;
			_dayWidth = originalDayWidth;
			break;
		case ISettings.ZOOM_MONTH_MAX:
			_currentView = ISettings.VIEW_MONTH;
			_monthDayWidth = originalMonthWeekWidth + 6;
			break;
		case ISettings.ZOOM_MONTH_MEDIUM:
			_currentView = ISettings.VIEW_MONTH;
			_monthDayWidth = originalMonthWeekWidth + 3;
			break;
		case ISettings.ZOOM_MONTH_NORMAL:
			_currentView = ISettings.VIEW_MONTH;
			_monthDayWidth = originalMonthWeekWidth;
			break;
		case ISettings.ZOOM_YEAR_MAX:
			_currentView = ISettings.VIEW_YEAR;
			_yearDayWidth = originalYearMonthDayWidth + 2;
			break;
		case ISettings.ZOOM_YEAR_MEDIUM:
			_currentView = ISettings.VIEW_YEAR;
			_yearDayWidth = originalYearMonthDayWidth + 1;
			break;
		case ISettings.ZOOM_YEAR_NORMAL:
			_currentView = ISettings.VIEW_YEAR;
			_yearDayWidth = originalYearMonthDayWidth;
			break;
		case ISettings.ZOOM_YEAR_SMALL:
			_currentView = ISettings.VIEW_YEAR;
			_yearDayWidth = originalYearMonthDayWidth - 1;
			break;
		case ISettings.ZOOM_YEAR_VERY_SMALL:
			_currentView = ISettings.VIEW_YEAR;
			_yearDayWidth = originalYearMonthDayWidth - 2;
			break;
		default:
			break;
		}

		_weekWidth = _dayWidth * 7;
		_monthWeekWidth = _monthDayWidth * 7;

		// mYearMonthWeekWidth = mYearDayWidth * 7;
		// mYearMonthWidth = mYearMonthWeekWidth * 4;

		// not a hack, we just re-use the same parameter name but for a
		// different purpose than the name itself, not exactly great logic
		// but it saves some recoding
		if (_zoomLevel == ISettings.ZOOM_HOURS_MAX || _zoomLevel == ISettings.ZOOM_HOURS_MEDIUM || _zoomLevel == ISettings.ZOOM_HOURS_NORMAL) {
			// how many hours are there really in our work day? we don't show
			// anything else!
			_weekWidth = _dayWidth * 24;
		}

		if (dDay) {
			_currentView = ISettings.VIEW_D_DAY;
		}
	}
		
*/
