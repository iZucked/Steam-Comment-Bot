/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import java.util.EnumMap;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * Implementation of {@link IJourneyEvent}.
 * 
 * @author Simon Goodall
 * 
 */
public final class JourneyEventImpl extends AbstractScheduledEventImpl implements IJourneyEvent {

	private int distance;

	private IPort fromPort;

	private IPort toPort;

	private VesselState vesselState;

	private int speed;

	private final EnumMap<FuelComponent, EnumMap<FuelUnit, Long>> fuelConsumption = new EnumMap<FuelComponent, EnumMap<FuelUnit, Long>>(FuelComponent.class);

	private final EnumMap<FuelComponent, Long> fuelCost = new EnumMap<FuelComponent, Long>(FuelComponent.class);

	private String route;

	private long routeCost;

	@Override
	public int getDistance() {
		return distance;
	}

	@Override
	public IPort getFromPort() {
		return fromPort;
	}

	@Override
	public IPort getToPort() {
		return toPort;
	}

	public void setDistance(final int distance) {
		this.distance = distance;
	}

	public void setFromPort(final IPort fromPort) {
		this.fromPort = fromPort;
	}

	public void setToPort(final IPort toPort) {
		this.toPort = toPort;
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

	@Override
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(final int speed) {
		this.speed = speed;
	}

	@Override
	public String getRoute() {
		return route;
	}

	public void setRoute(final String route) {
		this.route = route;
	}

	@Override
	public long getRouteCost() {
		return routeCost;
	}

	public void setRouteCost(final long routeCost) {
		this.routeCost = routeCost;
	}
}
