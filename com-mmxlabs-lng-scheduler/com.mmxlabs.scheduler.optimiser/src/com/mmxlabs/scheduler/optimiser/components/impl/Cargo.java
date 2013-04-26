/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;

/**
 * Default implementation of {@link ICargo}.
 * 
 * @author Simon Goodall
 * 
 */
public final class Cargo implements ICargo {

	private ILoadOption loadSlot;

	private IDischargeOption dischargeSlot;

	private String id;

	@Override
	public ILoadOption getLoadOption() {
		return loadSlot;
	}

	public void setLoadOption(final ILoadOption loadOption) {
		this.loadSlot = loadOption;
	}

	@Override
	public IDischargeOption getDischargeOption() {
		return dischargeSlot;
	}

	public void setDischargeOption(final IDischargeOption dischargeOption) {
		this.dischargeSlot = dischargeOption;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

}
