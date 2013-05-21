/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
	 * @since 5.0
	 */
	public Cargo(final List<IPortSlot> portSlots) {
		this.portSlots = portSlots;
	}

	/**
	 * @since 5.0
	 */
	@Override
	public List<IPortSlot> getPortSlots() {
		return portSlots;
	}
}
