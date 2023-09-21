/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.time.temporal.ChronoUnit;
import java.util.function.UnaryOperator;

public interface ITimeScaleZoomLevel {

	ITimeScaleZoomLevel nextIn();

	ITimeScaleZoomLevel nextOut();

	boolean isMaxZoom();

	boolean isMinZoom();
	
	default boolean isMinOrMax() {
		return isMinZoom() || isMaxZoom();
	}

	TimeScaleView getView();

	ChronoUnit getWidthUnit();

	UnaryOperator<Integer> getScalingFunction();

}