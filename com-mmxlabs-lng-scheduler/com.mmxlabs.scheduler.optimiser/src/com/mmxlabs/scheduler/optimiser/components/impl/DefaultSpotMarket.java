/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.ISpotMarket;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;

/**
 * 
 * Default implementation of {@link ISpotMarket}
 * 
 * @author Simon Goodall
 */
@NonNullByDefault
public class DefaultSpotMarket implements ISpotMarket {

	private final String name;

	private final IEntity entity;

	public DefaultSpotMarket(final String name, final IEntity entity) {
		this.name = name;
		this.entity = entity;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IEntity getEntity() {
		return entity;
	}
}
