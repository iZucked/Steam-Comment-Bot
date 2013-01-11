/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.capacity;

import java.util.Collection;

/**
 * Annotation recording all the capacity violations occurring at a particular sequence element.
 * 
 * @author Simon Goodall
 * 
 */
public interface ICapacityAnnotation {
	/**
	 * Returns a bunch of {@link ICapacityEntry} instances, each pertaining to a capacity violation
	 * 
	 * @return
	 */
	Collection<ICapacityEntry> getEntries();
}
