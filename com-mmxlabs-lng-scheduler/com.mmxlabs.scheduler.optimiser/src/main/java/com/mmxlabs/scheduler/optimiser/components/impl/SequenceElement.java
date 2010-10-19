/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.IndexedObject;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;

/**
 * Default implementation of {@link ISequenceElement}.
 * 
 * @author Simon Goodall
 * 
 */
public final class SequenceElement extends IndexedObject implements ISequenceElement {

	private String name;

	private IPortSlot portSlot;

	public SequenceElement(final IIndexingContext context) {
		super(context);
	}

	public SequenceElement(final IIndexingContext context, final String name, IPortSlot portSlot) {
		super(context);
		this.name = name;
		this.portSlot = portSlot;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public IPortSlot getPortSlot() {
		return portSlot;
	}

	public void setPortSlot(final IPortSlot portSlot) {
		this.portSlot = portSlot;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
