/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Mock implementation of {@link ISequenceElement} for use in unit tests.
 * 
 * @author Simon Goodall
 * 
 */
class MockSequenceElement implements ISequenceElement {

	private final int index;

	public MockSequenceElement(final int index) {
		this.index = index;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public String getName() {
		return "" + index;
	}
}