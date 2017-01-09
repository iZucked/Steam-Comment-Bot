/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.IndexedObject;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;

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
	
	/**
	 * True if no cooldown is provided
	 */
	private boolean arriveCold;
	/**
	 * A calculator used to determine the price of cooldown LNG here.
	 */
	private ICooldownCalculator cooldownCalculator;

	private long minCvValue;

	private long maxCvValue;

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
	public String getTimeZoneId() {
		return timeZoneId;		
	}
	
	public void setTimeZoneId(String id) {
		this.timeZoneId = id;
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof Port) {
			final Port p = (Port) obj;
			if (!Equality.isEqual(name, p.getName())) {
				return false;
			}

			if (!Equality.isEqual(cooldownCalculator, p.getCooldownCalculator())) {
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
	public ICooldownCalculator getCooldownCalculator() {
		return cooldownCalculator;
	}

	public void setCooldownCalculator(final ICooldownCalculator cooldownCalculator) {
		this.cooldownCalculator = cooldownCalculator;
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public long getMinCvValue() {
		return minCvValue;
	}

	@Override
	public long getMaxCvValue() {
		return maxCvValue;
	}
	
	public void setMinCvValue(long minCvValue) {
		this.minCvValue = minCvValue;
	}

	public void setMaxCvValue(long maxCvValue) {
		this.maxCvValue = maxCvValue;
	}
}
