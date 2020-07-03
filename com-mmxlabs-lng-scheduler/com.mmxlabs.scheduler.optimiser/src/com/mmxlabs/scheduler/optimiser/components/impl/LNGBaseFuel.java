/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;

/**
 * Default implementation of {@link IBaseFuel}
 * 
 * @author Nathan Steadman
 * 
 */
public class LNGBaseFuel implements IBaseFuel, IIndexedObject {
	/**
	 * The name of the base fuel
	 */
	private String name = "LNG";

	@Override
	public int getEquivalenceFactor() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setEquivalenceFactor(int factor) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getIndex() {
		// Hard-coded index - ensure indexing context is correct!
		return 0;
	}

	@Override
	public String getName() {
		return name;
	}

}
