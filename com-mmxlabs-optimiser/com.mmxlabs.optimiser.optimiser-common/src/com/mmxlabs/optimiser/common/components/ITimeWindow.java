/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.components;

/**
 * Interface defining a window of time bounded by {@link #getStart()} and {@link #getEnd()}.
 * 
 * @author Simon Goodall
 * 
 */
public interface ITimeWindow {

	int getInclusiveStart();

	// TODO: Should the end be inclusive or exclusive?
	int getExclusiveEnd();
	
	int getExclusiveEndFlex();
	
	int getExclusiveEndWithoutFlex();
}
