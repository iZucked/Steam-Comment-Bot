package com.mmxlabs.scheduler.optimiser.fitness.impl;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.fitness.IIdleEvent;

/**
 * Implementation of {@link IIdleEvent}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public final class IdleEventImpl<T> extends AbstractScheduledEventImpl<T>
		implements IIdleEvent<T> {

	private IPort port;

	@Override
	public IPort getPort() {
		return port;
	}

	public void setPort(final IPort port) {
		this.port = port;
	}
}
