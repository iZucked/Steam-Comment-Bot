/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities.impl;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.entities.EntityBookType;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.entities.IEntityBook;

/**
 * Basic entity book which has a tax curve and and does transfer pricing by adding a fixed offset per MMBTU
 * 
 * @author Simon Goodall, hinton
 * 
 */
public class DefaultEntityBook implements IEntityBook {

	private final IEntity entity;
	private final ICurve taxCurve;
	private final EntityBookType entityBookType;

	public DefaultEntityBook(final IEntity entity, final EntityBookType entityBookType, final ICurve taxCurve) {
		this.entity = entity;
		this.entityBookType = entityBookType;
		this.taxCurve = taxCurve;
	}

	@Override
	public IEntity getEntity() {
		return entity;
	}

	@Override
	public EntityBookType getBookType() {
		return entityBookType;
	}

	/**
	 * For this case, taxed profit is just pretax * taxrate(time)
	 */
	@Override
	public long getTaxedProfit(final long pretax, final int time) {
		final int taxRate = taxCurve.getValueAtPoint(time);
		final int flip = Calculator.ScaleFactor - taxRate;
		final long taxedValue = Calculator.multiply(pretax, flip);
		return taxedValue;
	}

}
