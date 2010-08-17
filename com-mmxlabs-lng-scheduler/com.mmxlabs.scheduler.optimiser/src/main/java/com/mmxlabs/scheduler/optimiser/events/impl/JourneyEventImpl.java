package com.mmxlabs.scheduler.optimiser.events.impl;

import java.util.EnumMap;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * Implementation of {@link IJourneyEvent}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public final class JourneyEventImpl<T> extends AbstractScheduledEventImpl<T>
		implements IJourneyEvent<T> {

	private int distance;

	private IPort fromPort;

	private IPort toPort;

	private VesselState vesselState;

	private int speed;

	private final EnumMap<FuelComponent, Long> fuelConsumption = new EnumMap<FuelComponent, Long>(
			FuelComponent.class);

	private final EnumMap<FuelComponent, Long> fuelCost = new EnumMap<FuelComponent, Long>(
			FuelComponent.class);

	private String route;
	
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
	public long getFuelConsumption(final FuelComponent fuel) {

		if (fuelConsumption.containsKey(fuel)) {
			return fuelConsumption.get(fuel);
		} else {
			return 0l;
		}
	}

	public void setFuelConsumption(final FuelComponent fuel,
			final long consumption) {
		fuelConsumption.put(fuel, consumption);
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
}
