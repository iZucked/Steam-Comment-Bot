package com.mmxlabs.scheduler.optimiser.fitness.impl;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.fitness.IPortVisitEvent;

/**
 * Implementation of {@link IPortVisitEvent}
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public final class PortVisitEventImpl<T> extends
		AbstractScheduledEventImpl<T> implements IPortVisitEvent<T> {

	private IPort port;

	@Override
	public IPort getPort() {
		return port;
	}

	public void setPort(final IPort port) {
		this.port = port;
	}
}
