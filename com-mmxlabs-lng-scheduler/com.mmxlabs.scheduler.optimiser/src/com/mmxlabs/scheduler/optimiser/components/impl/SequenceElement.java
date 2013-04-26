/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.IndexedObject;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Default implementation of {@link ISequenceElement}.
 * 
 * @author Simon Goodall
 * 
 */
public final class SequenceElement extends IndexedObject implements ISequenceElement {

	private String name;

	public SequenceElement(final IIndexingContext context) {
		super(context);
	}

	public SequenceElement(final IIndexingContext context, final String name) {
		super(context);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getName();
	}
}
