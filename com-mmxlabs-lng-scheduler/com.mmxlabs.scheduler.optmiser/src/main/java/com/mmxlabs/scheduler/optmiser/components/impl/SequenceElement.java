package com.mmxlabs.scheduler.optmiser.components.impl;

import com.mmxlabs.scheduler.optmiser.components.ICargo;
import com.mmxlabs.scheduler.optmiser.components.IPort;
import com.mmxlabs.scheduler.optmiser.components.ISequenceElement;

/**
 * Default implementation of {@link ISequenceElement}.
 * 
 * @author Simon Goodall
 * 
 */
public final class SequenceElement implements ISequenceElement {

	private String name;

	private ICargo cargo;

	private IPort port;

	public void setName(String name) {
		this.name = name;
	}

	public void setCargo(ICargo cargo) {
		this.cargo = cargo;
	}

	public void setPort(IPort port) {
		this.port = port;
	}

	@Override
	public ICargo getCargo() {
		return cargo;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IPort getPort() {
		return port;
	}

}
