/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.common.Equality;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;

/**
 * Default implementation of {@link IBaseFuel}
 * 
 * @author achurchill
 * 
 */
public class BaseFuel implements IBaseFuel {
	/**
	 * The name of the base fuel
	 */
	private String name;

	/**
	 * Equivalence factor for converting base fuel to lng
	 */
	private int equivalenceFactor;

	public BaseFuel(final String name) {
		setName(name);
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof BaseFuel) {
			final BaseFuel p = (BaseFuel) obj;
			if (!Equality.isEqual(name, p.getName())) {
				return false;
			}

			if (!Equality.isEqual(getEquivalenceFactor(), p.getEquivalenceFactor())) {
				return false;
			}
			return true;
		}

		return false;
	}

	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int getEquivalenceFactor() {
		return equivalenceFactor;
	}

	@Override
	public void setEquivalenceFactor(int factor) {
		equivalenceFactor = factor;
	}
	
}
