package com.mmxlabs.scheduler.optimiser.fitness.impl;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.fitness.IJourneyEvent;

/**
 * Implementation of {@link IJourneyEvent}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public final class JourneyEventImpl<T> extends AbstractScheduledEventImpl<T>
		implements IJourneyEvent<T> {

	private int distance;

	private IPort fromPort;

	private IPort toPort;

	@Override
	public int getDistance() {
		return distance;
	}

	@Override
	public IPort getFromPort() {
		return fromPort;
	}

	@Override
	public IPort getToPort() {
		return toPort;
	}

	public void setDistance(final int distance) {
		this.distance = distance;
	}

	public void setFromPort(final IPort fromPort) {
		this.fromPort = fromPort;
	}

	public void setToPort(final IPort toPort) {
		this.toPort = toPort;
	}
}
