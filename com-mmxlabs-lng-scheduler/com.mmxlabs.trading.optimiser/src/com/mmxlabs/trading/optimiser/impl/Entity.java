/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.impl;

import com.mmxlabs.trading.optimiser.IEntity;

/**
 * Basic entity implementation.
 * 
 * @author hinton
 * 
 */
public abstract class Entity implements IEntity {
	private final String name;

	public Entity(final String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
