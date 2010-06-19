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

	private EnumMap<FuelComponent, Long> fuelConsumption = new EnumMap<FuelComponent, Long>(
			FuelComponent.class);

	private long idleTime;

	private long travelTime;

	private int speed;

	@Override
	public long getFuelConsumption(FuelComponent fuel) {
		return fuelConsumption.get(fuel);
	}

	@Override
	public long getIdleTime() {
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
	public long getTravelTime() {
		return travelTime;
	}

	@Override
	public void setFuelConsumption(FuelComponent fuel, long consumption) {
		fuelConsumption.put(fuel, consumption);
	}

	@Override
	public void setIdleTime(long idleTime) {
		this.idleTime = idleTime;
	}

	@Override
	public void setOptions(IVoyageOptions options) {
		this.options = options;
	}

	@Override
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	@Override
	public void setTravelTime(long travelTime) {
		this.travelTime = travelTime;
	}

}
