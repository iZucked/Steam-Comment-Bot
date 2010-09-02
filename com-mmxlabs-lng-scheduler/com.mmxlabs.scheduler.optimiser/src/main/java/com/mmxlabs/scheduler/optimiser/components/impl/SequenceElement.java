package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;

/**
 * Default implementation of {@link ISequenceElement}.
 * 
 * @author Simon Goodall
 * 
 */
public final class SequenceElement implements ISequenceElement {

	private String name;

	private IPortSlot portSlot;

	public SequenceElement() {

	}

	public SequenceElement(final String name, IPortSlot portSlot) {
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
