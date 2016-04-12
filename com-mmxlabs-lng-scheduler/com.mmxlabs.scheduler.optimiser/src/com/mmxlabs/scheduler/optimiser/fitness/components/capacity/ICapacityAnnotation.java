/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.capacity;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IElementAnnotation;

/**
 * Annotation recording all the capacity violations occurring at a particular sequence element.
 * 
 * @author Simon Goodall
 * 
 */
public interface ICapacityAnnotation extends IElementAnnotation {
	/**
	 * Returns a bunch of {@link ICapacityEntry} instances, each pertaining to a capacity violation
	 * 
	 * @return
	 */
	@NonNull
	Collection<@NonNull ICapacityEntry> getEntries();
}
