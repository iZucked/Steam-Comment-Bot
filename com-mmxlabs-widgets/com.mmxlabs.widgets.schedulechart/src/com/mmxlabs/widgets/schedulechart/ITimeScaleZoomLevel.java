package com.mmxlabs.widgets.schedulechart;

import java.time.temporal.ChronoUnit;
import java.util.function.UnaryOperator;

public interface ITimeScaleZoomLevel {

	ITimeScaleZoomLevel nextIn();

	ITimeScaleZoomLevel nextOut();

	boolean isMaxZoom();

	boolean isMinZoom();

	TimeScaleView getView();

	ChronoUnit getWidthUnit();

	UnaryOperator<Integer> getScalingFunction();

}