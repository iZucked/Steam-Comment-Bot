/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

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
public class Port extends IndexedObject implements IPort {
	/**
	 * The name of the port
	 */
	private String name;

	/**
	 * The time zone id of the port
	 */
	private String timeZoneId = "";

	public Port(final IIndexingContext context) {
		super(context);
	}

	public Port(final IIndexingContext context, final @NonNull String name) {
		super(context);
		setName(name);
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final @NonNull String name) {
		this.name = name;
	}

	@Override
	public String getTimeZoneId() {
		return timeZoneId;
	}

	public void setTimeZoneId(final @NonNull String id) {
		this.timeZoneId = id;
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

	@Override
	public String toString() {
		return getName();
	}
}
