package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * Default implementation of {@link IVessel}
 * 
 * @author Simon Goodall
 * 
 */
public final class Vessel implements IVessel {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
