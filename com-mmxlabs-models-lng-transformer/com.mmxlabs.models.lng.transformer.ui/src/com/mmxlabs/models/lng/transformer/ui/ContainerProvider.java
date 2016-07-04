/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scenario.service.model.Container;

public class ContainerProvider {
	@Nullable
	private Container container;

	public ContainerProvider() {

	}

	public ContainerProvider(@Nullable final Container container) {
		this.container = container;
	}

	@Nullable
	public Container get() {
		return container;
	}

	public void set(@Nullable final Container container) {
		this.container = container;
	}
}
