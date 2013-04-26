/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import java.util.EnumMap;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * Implementation of {@link IPortVisitEvent}
 * 
 * @author Simon Goodall
 * 
 */
public class PortVisitEventImpl extends AbstractScheduledEventImpl implements IPortVisitEvent {

	private IPortSlot portSlot;

	private final EnumMap<FuelComponent, EnumMap<FuelUnit, Long>> fuelConsumption = new EnumMap<FuelComponent, EnumMap<FuelUnit, Long>>(FuelComponent.class);

	private final EnumMap<FuelComponent, Long> fuelCost = new EnumMap<FuelComponent, Long>(FuelComponent.class);

	@Override
	public IPortSlot getPortSlot() {
		return portSlot;
	}

	public void setPortSlot(final IPortSlot portSlot) {
		this.portSlot = portSlot;
	}

	@Override
	public long getFuelConsumption(final FuelComponent fuel, final FuelUnit fuelUnit) {

		if (fuelConsumption.containsKey(fuel)) {

			final EnumMap<FuelUnit, Long> map = fuelConsumption.get(fuel);
			if (map.containsKey(fuelUnit)) {
				return map.get(fuelUnit);
			}
		}
		return 0l;
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
			return 0l;
		}
	}

	public void setFuelCost(final FuelComponent fuel, final long cost) {
		fuelCost.put(fuel, cost);
	}
}
