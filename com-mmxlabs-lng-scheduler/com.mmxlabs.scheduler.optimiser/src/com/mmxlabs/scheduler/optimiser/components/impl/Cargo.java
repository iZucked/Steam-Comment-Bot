/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * Default implementation of {@link ICargo}.
 * 
 * @author Simon Goodall
 * 
 */
public final class Cargo implements ICargo {

	private final List<IPortSlot> portSlots;

	/**
	 */
	public Cargo(final List<IPortSlot> portSlots) {
		this.portSlots = portSlots;
	}

	/**
	 */
	@Override
	public List<IPortSlot> getPortSlots() {
		return portSlots;
	}
}
