/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface ISeriesContainer {

	String getName();

	SeriesType getType();

	ISeries get();

	boolean canGet();
}
