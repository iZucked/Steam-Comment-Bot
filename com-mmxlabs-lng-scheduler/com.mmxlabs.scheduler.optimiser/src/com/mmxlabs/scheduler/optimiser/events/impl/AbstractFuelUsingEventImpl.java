/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import java.util.EnumMap;

import com.mmxlabs.scheduler.optimiser.events.IFuelUsingEvent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * Extended implementation of {@link AbstractScheduledEventImpl} implementing {@link IFuelUsingEvent}
 * 
 * @author Simon Goodall
 * 
 */
public abstract class AbstractFuelUsingEventImpl extends AbstractScheduledEventImpl implements IFuelUsingEvent {

	private final EnumMap<FuelComponent, EnumMap<FuelUnit, Long>> fuelConsumption = new EnumMap<FuelComponent, EnumMap<FuelUnit, Long>>(FuelComponent.class);

	private final EnumMap<FuelComponent, Long> fuelCost = new EnumMap<FuelComponent, Long>(FuelComponent.class);

	private final EnumMap<FuelComponent, FuelUnit> fuelPriceUnit = new EnumMap<FuelComponent, FuelUnit>(FuelComponent.class);

	private final EnumMap<FuelComponent, Integer> fuelUnitPrice = new EnumMap<FuelComponent, Integer>(FuelComponent.class);

	private boolean cooldownPerformed;

	@Override
	public long getFuelConsumption(final FuelComponent fuel, final FuelUnit fuelUnit) {

		if (fuelConsumption.containsKey(fuel)) {

			final EnumMap<FuelUnit, Long> map = fuelConsumption.get(fuel);
			if (map.containsKey(fuelUnit)) {
				return map.get(fuelUnit);
			}
		}
		return 0L;
	}

	public void setFuelConsumption(final FuelComponent fuel, final FuelUnit fuelUnit, final long consumption) {
		final EnumMap<FuelUnit, Long> map;
		if (fuelConsumption.containsKey(fuel)) {
			map = fuelConsumption.get(fuel);
		} else {
			map = new EnumMap<FuelUnit, Long>(FuelUnit.class);
			fuelConsumption.put(fuel, map);
		}
		map.put(fuelUnit, consumption);
	}

	@Override
	public long getFuelCost(final FuelComponent fuel) {

		if (fuelCost.containsKey(fuel)) {
			return fuelCost.get(fuel);
		} else {
			return 0L;
		}
	}

	public void setFuelCost(final FuelComponent fuel, final long cost) {
		fuelCost.put(fuel, cost);
	}

	@Override
	public FuelUnit getFuelPriceUnit(final FuelComponent fuel) {
		if (fuelPriceUnit.containsKey(fuel)) {
			return fuelPriceUnit.get(fuel);
		} else {
			return null;
		}
	}

	@Override
	public int getFuelUnitPrice(final FuelComponent fuel) {
		if (fuelUnitPrice.containsKey(fuel)) {
			return fuelUnitPrice.get(fuel);
		} else {
			return 0;
		}
	}

	public void setFuelPriceUnit(final FuelComponent fuel, final FuelUnit unit) {
		fuelPriceUnit.put(fuel, unit);
	}

	public void setFuelUnitPrice(final FuelComponent fuel, final int pricePerUnit) {
		fuelUnitPrice.put(fuel, pricePerUnit);
	}

	@Override
	public boolean isCooldownPerformed() {
		return cooldownPerformed;
	}

	public void setCooldownPerformed(final boolean cooldownPerformed) {
		this.cooldownPerformed = cooldownPerformed;
	}

}
