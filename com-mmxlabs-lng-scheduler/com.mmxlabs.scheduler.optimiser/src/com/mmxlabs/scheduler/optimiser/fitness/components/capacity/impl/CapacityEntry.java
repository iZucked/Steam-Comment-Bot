/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.capacity.impl;

import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.ICapacityEntry;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;

/**
 * Implementation of {@link ICapacityEntry}
 * 
 * @author Simon Goodall
 * 
 */
public final class CapacityEntry implements ICapacityEntry {

	private final CapacityViolationType type;

	private final long volume;

	public CapacityEntry(CapacityViolationType type, long volume) {
		this.type = type;
		this.volume = volume;
	}

	@Override
	public CapacityViolationType getType() {
		return type;
	}

	@Override
	public long getVolume() {
		return volume;
	}
}
