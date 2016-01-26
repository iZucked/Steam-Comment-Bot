/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects.impl;

import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.common.indexedobjects.IIndexingContext;

public class IndexedObject implements IIndexedObject {
	public final int index;

	public IndexedObject(final IIndexingContext provider) {
		index = provider.assignIndex(this);
	}

	@Override
	public final int getIndex() {
		return index;
	}
}
