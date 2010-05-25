package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;

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

	public SequenceElement() {

	}

	public SequenceElement(final String name, final IPort port,
			final ICargo cargo) {
		setName(name);
		setPort(port);
		setCargo(cargo);
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setCargo(final ICargo cargo) {
		this.cargo = cargo;
	}

	public void setPort(final IPort port) {
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
