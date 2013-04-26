/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import java.util.EnumMap;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * Implementation of {@link IIdleEvent}.
 * 
 * @author Simon Goodall
 */
public final class IdleEventImpl extends AbstractScheduledEventImpl implements IIdleEvent {

	private IPort port;

	private VesselState vesselState;

	private final EnumMap<FuelComponent, EnumMap<FuelUnit, Long>> fuelConsumption = new EnumMap<FuelComponent, EnumMap<FuelUnit, Long>>(FuelComponent.class);

	private final EnumMap<FuelComponent, Long> fuelCost = new EnumMap<FuelComponent, Long>(FuelComponent.class);

	private int cooldownDuration;

	@Override
	public IPort getPort() {
		return port;
	}

	public void setPort(final IPort port) {
		this.port = port;
	}

	@Override
	public VesselState getVesselState() {
		return vesselState;
	}

	public void setVesselState(final VesselState vesselState) {
		this.vesselState = vesselState;
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

	public void setCooldownDuration(final int cooldownDuration) {
		this.cooldownDuration = cooldownDuration;
	}
	
	@Override
	public int getCooldownDuration() {
		return cooldownDuration;
	}
}
