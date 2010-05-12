package com.mmxlabs.scheduler.optmiser.components.impl;

import com.mmxlabs.scheduler.optmiser.components.IPort;

public class Port implements IPort {

	private String name;

	public Port() {

	}

	public Port(final String name) {
		setName(name);
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
