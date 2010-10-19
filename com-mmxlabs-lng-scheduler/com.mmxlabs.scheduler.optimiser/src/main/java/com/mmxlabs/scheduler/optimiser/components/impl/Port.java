/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.IndexedObject;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * Default implementation of {@link IPort}
 * 
 * @author Simon Goodall
 * 
 */
public final class Port extends IndexedObject implements IPort {

	private String name;

	public Port(final IIndexingContext context) {
		super(context);
	}

	public Port(final IIndexingContext context, final String name) {
		super(context);
		setName(name);
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof Port) {
			final Port p = (Port) obj;
			if (!Equality.isEqual(name, p.getName())) {
				return false;
			}
			return true;
		}

		return false;
	}
}
