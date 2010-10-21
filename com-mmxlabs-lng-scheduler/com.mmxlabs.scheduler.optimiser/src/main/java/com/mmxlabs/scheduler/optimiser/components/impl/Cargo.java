/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;

/**
 * Default implementation of {@link ICargo}.
 * 
 * @author Simon Goodall
 * 
 */
public final class Cargo implements ICargo {

	private ILoadSlot loadSlot;

	private IDischargeSlot dischargeSlot;

	private String id;

	@Override
	public ILoadSlot getLoadSlot() {
		return loadSlot;
	}

	public void setLoadSlot(final ILoadSlot loadSlot) {
		this.loadSlot = loadSlot;
	}

	@Override
	public IDischargeSlot getDischargeSlot() {
		return dischargeSlot;
	}

	public void setDischargeSlot(final IDischargeSlot dischargeSlot) {
		this.dischargeSlot = dischargeSlot;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

}
