/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.capacity.impl;

import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.ICapacityEntry;

/**
 * Implementation of {@link ICapacityEntry}
 * 
 * @author Simon Goodall
 * 
 */
public final class CapacityEntry implements ICapacityEntry {

	private final String type;

	private final long volume;

	public CapacityEntry(String type, long volume) {
		this.type = type;
		this.volume = volume;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public long getVolume() {
		return volume;
	}
}
