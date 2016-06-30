/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.IndexedObject;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Default implementation of {@link ISequenceElement}.
 * 
 * @author Simon Goodall
 * 
 */
public class SequenceElement extends IndexedObject implements ISequenceElement {
	@NonNull
	private String name;

	public SequenceElement(@NonNull final IIndexingContext context, @NonNull final String name) {
		super(context);
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	public void setName(@NonNull final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getName();
	}
}
