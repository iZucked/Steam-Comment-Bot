/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.common.impl.LongFastEnumEnumMap;
import com.mmxlabs.common.impl.LongFastEnumMap;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.events.IFuelUsingEvent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * Extended implementation of {@link AbstractScheduledEventImpl} implementing {@link IFuelUsingEvent}
 * 
 * @author Simon Goodall / NSteadman
 * 
 */
public abstract class AbstractFuelUsingEventImpl extends AbstractScheduledEventImpl implements IFuelUsingEvent {

	private final LongFastEnumEnumMap<FuelComponent, FuelUnit> fuelConsumption = new LongFastEnumEnumMap<>(FuelComponent.values().length, FuelUnit.values().length);

	private final LongFastEnumMap<FuelComponent> fuelCost = new LongFastEnumMap<>(FuelComponent.values().length);

	private final Map<IBaseFuel, Integer> fuelPricePerUnit = new HashMap<>();

	private Set<FuelKey> fuelKeys = new HashSet<>();

	private boolean cooldownPerformed;

	private long startHeelInM3;
	private long endHeelInM3;

	@Override
	public Set<FuelKey> getFuelKeys() {
		return fuelKeys;
	}

	@Override
	public long getFuelConsumption(final FuelKey fuelKey) {

		return fuelConsumption.get(fuelKey.getFuelComponent(), fuelKey.getFuelUnit());
	}

	public void setFuelConsumption(final FuelKey fuelKey, final long consumption) {
		fuelConsumption.put(fuelKey.getFuelComponent(), fuelKey.getFuelUnit(), consumption);
		fuelKeys.add(fuelKey);
	}

	@Override
	public long getFuelCost(final FuelKey fuelKey) {
		return fuelCost.get(fuelKey.getFuelComponent());

	}

	public void setFuelCost(final FuelKey fuelKey, final long cost) {
		fuelCost.put(fuelKey.getFuelComponent(), cost);
		fuelKeys.add(fuelKey);
	}

	@Override
	public int getBaseFuelUnitPrice(final IBaseFuel bf) {
		return fuelPricePerUnit.getOrDefault(bf, 0);

	}

	public void setBaseFuelUnitPrice(final IBaseFuel bf, final int price) {
		fuelPricePerUnit.put(bf, price);
	}

	@Override
	public boolean isCooldownPerformed() {
		return cooldownPerformed;
	}

	public void setCooldownPerformed(final boolean cooldownPerformed) {
		this.cooldownPerformed = cooldownPerformed;
	}

	@Override
	public long getStartHeelInM3() {
		return startHeelInM3;
	}

	@Override
	public long getEndHeelInM3() {
		return endHeelInM3;
	}

	public void setStartHeelInM3(final long startHeelInM3) {
		this.startHeelInM3 = startHeelInM3;
	}

	public void setEndHeelInM3(final long endHeelInM3) {
		this.endHeelInM3 = endHeelInM3;
	}
}
