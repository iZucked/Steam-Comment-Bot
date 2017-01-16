/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities.impl;

import com.google.inject.Inject;
import com.mmxlabs.scheduler.optimiser.entities.EntityBookType;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.entities.IEntityBook;
import com.mmxlabs.scheduler.optimiser.providers.IEntityProvider;

/**
 * Basic entity implementation.
 * 
 * @author hinton
 * 
 */
public abstract class AbstractEntity implements IEntity {
	@Inject
	private IEntityProvider enityProvider;

	private final String name;

	public AbstractEntity(final String name) {
		super();
		this.name = name;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IEntityBook getShippingBook() {
		return enityProvider.getEntityBook(this, EntityBookType.Shipping);
	}

	@Override
	public IEntityBook getTradingBook() {
		return enityProvider.getEntityBook(this, EntityBookType.Trading);
	}

	@Override
	public IEntityBook getUpstreamBook() {
		return enityProvider.getEntityBook(this, EntityBookType.Upstream);
	}
}
