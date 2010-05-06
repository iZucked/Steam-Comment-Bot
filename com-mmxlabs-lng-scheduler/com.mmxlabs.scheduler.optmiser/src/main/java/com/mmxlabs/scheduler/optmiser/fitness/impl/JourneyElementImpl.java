package com.mmxlabs.scheduler.optmiser.fitness.impl;

import com.mmxlabs.scheduler.optmiser.components.IPort;
import com.mmxlabs.scheduler.optmiser.fitness.IJourneyElement;

public class JourneyElementImpl extends AbstractSequencedElementImpl implements
		IJourneyElement {

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
