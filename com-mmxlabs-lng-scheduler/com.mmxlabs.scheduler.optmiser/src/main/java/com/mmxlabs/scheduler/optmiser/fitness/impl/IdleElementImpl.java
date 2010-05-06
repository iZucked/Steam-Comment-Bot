package com.mmxlabs.scheduler.optmiser.fitness.impl;

import com.mmxlabs.scheduler.optmiser.components.IPort;
import com.mmxlabs.scheduler.optmiser.fitness.IIdleElement;

public class IdleElementImpl extends AbstractSequencedElementImpl implements
		IIdleElement {

	private IPort port;

	@Override
	public IPort getPort() {
		return port;
	}

	public void setPort(final IPort port) {
		this.port = port;
	}
}
