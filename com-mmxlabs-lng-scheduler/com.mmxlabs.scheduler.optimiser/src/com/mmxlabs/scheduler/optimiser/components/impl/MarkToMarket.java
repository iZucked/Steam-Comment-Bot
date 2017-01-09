/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;

/**
 * 
 * Default implementation of {@link IMarkToMarket}
 * 
 * @author Simon Goodall
 */
public class MarkToMarket implements IMarkToMarket {

	private final ILoadPriceCalculator loadPriceCalculator;

	private final ISalesPriceCalculator salesPriceCalculator;

	private final int cv;

	private final IEntity entity;

	public MarkToMarket(@NonNull final ISalesPriceCalculator salesPriceCalculator, final IEntity entity) {
		this(null, 0, salesPriceCalculator, entity);
	}

	public MarkToMarket(final ILoadPriceCalculator loadPriceCalculator, final int cv, final ISalesPriceCalculator salesPriceCalculator, final IEntity entity) {
		this.loadPriceCalculator = loadPriceCalculator;
		this.salesPriceCalculator = salesPriceCalculator;
		this.cv = cv;
		this.entity = entity;
	}

	public MarkToMarket(@NonNull final ILoadPriceCalculator loadPriceCalculator, final int cv, final IEntity entity) {
		this(loadPriceCalculator, cv, null, entity);
	}

	@Override
	@Nullable
	public ILoadPriceCalculator getLoadPriceCalculator() {
		return loadPriceCalculator;
	}

	@Override
	@Nullable
	public ISalesPriceCalculator getSalesPriceCalculator() {
		return salesPriceCalculator;
	}

	@Override
	public int getCVValue() {
		return cv;
	}

	@Override
	public IEntity getEntity() {
		return entity;
	}
}
