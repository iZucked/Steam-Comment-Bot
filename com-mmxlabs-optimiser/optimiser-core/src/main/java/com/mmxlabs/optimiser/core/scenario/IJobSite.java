/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core.scenario;

/**
 * A job at a site that requires sequencing.
 * 
 * @author proshun
 * @param <T> A Comparable to be used for lookup.
 */
public interface IJobSite<T> {

	Comparable<T> getComparableID();
}
