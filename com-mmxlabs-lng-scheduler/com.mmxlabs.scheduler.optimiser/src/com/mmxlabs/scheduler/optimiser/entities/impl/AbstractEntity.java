/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities.impl;

import org.eclipse.jdt.annotation.NonNullByDefault;

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
@NonNullByDefault
public abstract class AbstractEntity implements IEntity {
	@Inject
	private IEntityProvider enityProvider;

	private final String name;

	private final boolean thirdparty;

	public AbstractEntity(final String name, final boolean thirdparty) {
		super();
		this.name = name;
		this.thirdparty = thirdparty;
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

	@Override
	public boolean isThirdparty() {
		return thirdparty;
	}
}
