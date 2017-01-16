/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;

/**
 * Implementation of {@link IPortVisitEvent}
 * 
 * @author Simon Goodall
 * 
 */
public class PortVisitEventImpl extends AbstractFuelUsingEventImpl implements IPortVisitEvent {

	private IPortSlot portSlot;

	@Override
	public IPortSlot getPortSlot() {
		return portSlot;
	}

	public void setPortSlot(final IPortSlot portSlot) {
		this.portSlot = portSlot;
	}
}
