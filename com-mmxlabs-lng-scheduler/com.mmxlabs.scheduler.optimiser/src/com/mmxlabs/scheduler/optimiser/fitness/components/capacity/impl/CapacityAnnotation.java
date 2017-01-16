/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.capacity.impl;

import java.util.Collection;

import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.ICapacityAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.ICapacityEntry;

/**
 * Implementation of {@link ICapacityAnnotation}
 * 
 * @author Simon Goodall
 */
public final class CapacityAnnotation implements ICapacityAnnotation {

	private final Collection<ICapacityEntry> entries;

	public CapacityAnnotation(Collection<ICapacityEntry> entries) {
		this.entries = entries;
	}

	@Override
	public Collection<ICapacityEntry> getEntries() {
		return entries;
	}
}
