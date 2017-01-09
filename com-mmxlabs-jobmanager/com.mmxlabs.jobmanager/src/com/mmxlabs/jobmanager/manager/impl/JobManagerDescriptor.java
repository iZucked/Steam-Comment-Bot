/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.manager.impl;

import com.mmxlabs.jobmanager.manager.IJobManagerDescriptor;

public final class JobManagerDescriptor implements IJobManagerDescriptor {

	private final String name;

	private String description;

	private Object capabilities;

	public JobManagerDescriptor(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Object getCapabilities() {
		return capabilities;
	}

	public final void setDescription(final String description) {
		this.description = description;
	}

	public final void setCapabilities(final Object capabilities) {
		this.capabilities = capabilities;
	}

}
