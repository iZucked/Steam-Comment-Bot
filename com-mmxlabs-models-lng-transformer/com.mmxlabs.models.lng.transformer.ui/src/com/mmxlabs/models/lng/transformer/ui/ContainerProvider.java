package com.mmxlabs.models.lng.transformer.ui;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scenario.service.model.Container;

public class ContainerProvider {

	private Container container;

	public ContainerProvider() {

	}

	public ContainerProvider(final Container container) {
		this.container = container;
	}

	@Nullable
	public Container get() {
		return container;
	}

	public void set(final Container container) {
		this.container = container;
	}

}
