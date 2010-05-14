package com.mmxlabs.scheduler.optmiser.fitness.impl;

import com.mmxlabs.scheduler.optmiser.components.IPort;
import com.mmxlabs.scheduler.optmiser.fitness.IIdleElement;

public class IdleElementImpl<T> extends AbstractSequencedElementImpl<T> implements
		IIdleElement<T> {

	private IPort port;

	@Override
	public IPort getPort() {
		return port;
	}

	public void setPort(final IPort port) {
		this.port = port;
	}
}
