/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import com.mmxlabs.common.indexedobjects.IIndexedObject;

/**
 * Mock implementation of {@link IIndexedObject} for use in unit tests.
 * 
 * @author Simon Goodall
 * 
 */
class MockIndexedObject implements IIndexedObject {

	private final int index;

	public MockIndexedObject(final int index) {
		this.index = index;
	}

	@Override
	public int getIndex() {
		return index;
	}
}