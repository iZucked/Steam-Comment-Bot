/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.IndexedObject;
import com.mmxlabs.optimiser.core.IResource;

/**
 * Default implementation of a {@link IResource}
 * 
 * @author Simon Goodall
 * 
 */
public final class Resource extends IndexedObject implements IResource {

	@NonNull
	private String name;

	public Resource(@NonNull final IIndexingContext context, @NonNull final String name) {
		super(context);
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Resource " + name;
	}
}
