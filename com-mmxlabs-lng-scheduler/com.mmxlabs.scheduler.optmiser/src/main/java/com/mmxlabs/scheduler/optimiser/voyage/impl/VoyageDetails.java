package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.EnumMap;

import com.mmxlabs.common.Equality;
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

	private final EnumMap<FuelComponent, Integer> fuelUnitPrices = new EnumMap<FuelComponent, Integer>(
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

	@Override
	public int getFuelUnitPrice(final FuelComponent fuel) {

		if (fuelUnitPrices.containsKey(fuel)) {
			return fuelUnitPrices.get(fuel);
		} else {
			return 0;
		}
	}

	@Override
	public void setFuelUnitPrice(final FuelComponent fuel, final int unitPrice) {
		fuelUnitPrices.put(fuel, unitPrice);
	}
	
	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof VoyageDetails) {
			@SuppressWarnings("rawtypes")
			final VoyageDetails d = (VoyageDetails) obj;

			// Ensure all fields are present here

			if (speed != d.speed) {
				return false;
			}
			if (idleTime != d.idleTime) {
				return false;
			}
			if (travelTime != d.travelTime) {
				return false;
			}
			if (startTime != d.startTime) {
				return false;
			}
			if (!Equality.isEqual(options, d.options)) {
				return false;
			}

			if (!Equality.isEqual(fuelConsumption, d.fuelConsumption)) {
				return false;
			}
			if (!Equality.isEqual(fuelUnitPrices, d.fuelUnitPrices)) {
				return false;
			}
			
			return true;
		}
		return false;
	}
}
