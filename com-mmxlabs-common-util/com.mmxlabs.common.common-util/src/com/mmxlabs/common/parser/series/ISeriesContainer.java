/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

public interface ISeriesContainer {
	
	String getName();
	
	SeriesType getType();
	
	ISeries get();

	boolean canGet();
}
