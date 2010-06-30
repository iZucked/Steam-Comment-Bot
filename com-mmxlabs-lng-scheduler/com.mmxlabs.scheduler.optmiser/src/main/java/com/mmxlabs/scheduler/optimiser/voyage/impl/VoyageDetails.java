package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.EnumMap;

import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageOptions;

/**
 * Implementation of {@link IVoyageDetails}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public final class VoyageDetails<T> implements IVoyageDetails<T> {

	private IVoyageOptions options;

	private final EnumMap<FuelComponent, Long> fuelConsumption = new EnumMap<FuelComponent, Long>(
			FuelComponent.class);

	private int idleTime;

	private int travelTime;

	private int speed;

	private int startTime;

	@Override
	public long getFuelConsumption(final FuelComponent fuel) {

		if (fuelConsumption.containsKey(fuel)) {
			return fuelConsumption.get(fuel);
		} else {
			return 0l;
		}
	}

	@Override
	public int getIdleTime() {
		return idleTime;
	}

	@Override
	public IVoyageOptions getOptions() {
		return options;
	}

	@Override
	public int getSpeed() {
		return speed;
	}

	@Override
	public int getTravelTime() {
		return travelTime;
	}

	@Override
	public void setFuelConsumption(final FuelComponent fuel,
			final long consumption) {
		fuelConsumption.put(fuel, consumption);
	}

	@Override
	public void setIdleTime(final int idleTime) {
		this.idleTime = idleTime;
	}

	@Override
	public void setOptions(final IVoyageOptions options) {
		this.options = options;
	}

	@Override
	public void setSpeed(final int speed) {
		this.speed = speed;
	}

	@Override
	public void setTravelTime(final int travelTime) {
		this.travelTime = travelTime;
	}

	@Override
	public int getStartTime() {
		return startTime;
	}

	@Override
	public void setStartTime(final int startTime) {
		this.startTime = startTime;
	}
}
