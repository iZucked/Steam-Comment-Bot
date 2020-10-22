/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

public class DefaultSpotCharterOutMarket implements ISpotCharterOutMarket {

	private final @NonNull String name;
	
	public DefaultSpotCharterOutMarket(final @NonNull String name) {
		this.name = name;
	}
	
	@Override
	public @NonNull String getName() {
		return this.name;
	}

}