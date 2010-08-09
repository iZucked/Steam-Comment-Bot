package com.mmxlabs.scheduler.optimiser.events.impl;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;

/**
 * Implementation of {@link IPortVisitEvent}
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public class PortVisitEventImpl<T> extends AbstractScheduledEventImpl<T>
		implements IPortVisitEvent<T> {

	private IPortSlot portSlot;

	@Override
	public IPortSlot getPortSlot() {
		return portSlot;
	}

	public void setPortSlot(final IPortSlot portSlot) {
		this.portSlot = portSlot;
	}
}
