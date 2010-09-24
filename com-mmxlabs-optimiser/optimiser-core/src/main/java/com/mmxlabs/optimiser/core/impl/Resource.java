package com.mmxlabs.optimiser.core.impl;

import com.mmxlabs.optimiser.core.IResource;

/**
 * Default implementation of a {@link IResource}
 * 
 * @author Simon Goodall
 * 
 */
public final class Resource implements IResource {

	private String name;

	public Resource() {

	}

	public Resource(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	
	public String toString() {
		return "Resource " + name;
	}
}
