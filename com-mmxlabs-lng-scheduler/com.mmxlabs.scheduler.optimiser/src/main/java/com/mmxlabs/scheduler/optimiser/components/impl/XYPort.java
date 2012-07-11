/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.IndexedObject;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator;

/**
 * Default implementation of {@link IXYPort}
 * 
 * @author Simon Goodall
 * 
 */
public final class XYPort extends IndexedObject implements IXYPort {

	private String name;

	/**
	 * A calculator used to determine the price of cooldown LNG here.
	 */
	private ICooldownPriceCalculator cooldownPriceCalculator;

	private float x;

	private float y;

	private boolean arriveCold;

	public XYPort(final IIndexingContext context) {
		super(context);
	}

	public XYPort(final IIndexingContext context, final String name, final float x, final float y) {
		super(context);
		this.name = name;
		this.x = x;
		this.y = y;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public float getX() {
		return x;
	}

	public void setX(final float x) {
		this.x = x;
	}

	@Override
	public float getY() {
		return y;
	}

	public final void setY(final float y) {
		this.y = y;
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof XYPort) {
			final XYPort p = (XYPort) obj;

			if (x != p.x) {
				return false;
			}
			if (y != p.y) {
				return false;
			}
			if (!Equality.isEqual(name, p.name)) {
				return false;
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean shouldVesselsArriveCold() {
		return arriveCold;
	}

	public void setShouldVesselsArriveCold(final boolean arriveCold) {
		this.arriveCold = arriveCold;
	}

	@Override
	public ICooldownPriceCalculator getCooldownPriceCalculator() {
		return cooldownPriceCalculator;
	}

	public void setCooldownPriceCalculator(final ICooldownPriceCalculator cooldownPriceCalculator) {
		this.cooldownPriceCalculator = cooldownPriceCalculator;
	}
}
