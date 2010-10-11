package com.mmxlabs.optimiser.core.impl;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.IndexedObject;
import com.mmxlabs.optimiser.core.IResource;

/**
 * Default implementation of a {@link IResource}
 * 
 * @author Simon Goodall
 * 
 */
public final class Resource extends IndexedObject implements IResource {

	private String name;

	public Resource(final IIndexingContext context) {
		super(context);
	}

	public Resource(final IIndexingContext context, final String name) {
		super(context);
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
