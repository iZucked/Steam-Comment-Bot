/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.components;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Interface defining a window of time bounded by {@link #getStart()} and {@link #getEnd()}.
 * 
 * @author Simon Goodall
 * 
 */
public interface ITimeWindow {

	int getInclusiveStart();

	int getExclusiveEnd();

	int getExclusiveEndFlex();

	int getExclusiveEndWithoutFlex();

	/**
	 * Return true if the non-flex interval overlaps
	 * 
	 * @param other
	 * @return
	 */
	boolean overlaps(@NonNull ITimeWindow other);

	/**
	 * Return true if t is within the time window bounds
	 * @param t
	 * @return
	 */
	default boolean contains(int t) {
		return t >= getInclusiveStart() && t < getExclusiveEnd();
	}
}
