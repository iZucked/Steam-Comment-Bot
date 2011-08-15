/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.IndexedObject;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ISimpleLoadPriceCalculator;

/**
 * Default implementation of {@link IPort}
 * 
 * @author Simon Goodall
 * 
 */
public final class Port extends IndexedObject implements IPort {
	/**
	 * The name of the port
	 */
	private String name;
	/**
	 * True if no cooldown is provided
	 */
	private boolean arriveCold;
	/**
	 * A calculator used to determine the price of cooldown LNG here.
	 */
	private ISimpleLoadPriceCalculator cooldownPriceCalculator;

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

	@Override
	public boolean shouldVesselsArriveCold() {
		return arriveCold;
	}
	
	public void setShouldVesselsArriveCold(final boolean arriveCold) {
		this.arriveCold = arriveCold;
	}

	@Override
	public ISimpleLoadPriceCalculator getCooldownPriceCalculator() {
		return cooldownPriceCalculator;
	}

	public void setCooldownPriceCalculator(
			ISimpleLoadPriceCalculator cooldownPriceCalculator) {
		this.cooldownPriceCalculator = cooldownPriceCalculator;
	}
}
