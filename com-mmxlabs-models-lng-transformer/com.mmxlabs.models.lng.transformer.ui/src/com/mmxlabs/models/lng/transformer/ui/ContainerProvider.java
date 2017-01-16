/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scenario.service.model.Container;

/**
 * The {@link ContainerProvider} is used in the chain builders as a placeholder for a container than will be created as part of the chain,
 * 
 * @author sg
 *
 */
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
