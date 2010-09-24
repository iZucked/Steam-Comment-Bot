package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.EnumMap;
import java.util.Map.Entry;

import com.mmxlabs.common.Equality;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * Implementation of {@link VoyageDetails}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public final class VoyageDetails<T> implements Cloneable {

	private VoyageOptions options;

	private final EnumMap<FuelComponent, EnumMap<FuelUnit, Long>> fuelConsumption;

	private final EnumMap<FuelComponent, Integer> fuelUnitPrices;

	private int idleTime;

	private int travelTime;

	private int speed;

	private int startTime;

	public VoyageDetails() {
		fuelConsumption = new EnumMap<FuelComponent, EnumMap<FuelUnit, Long>>(
				FuelComponent.class);
		fuelUnitPrices = new EnumMap<FuelComponent, Integer>(
				FuelComponent.class);
		for (FuelComponent fuel : FuelComponent.values()) {
			fuelConsumption.put(fuel, new EnumMap<FuelUnit, Long>(FuelUnit.class));
		}
	}
	
	public VoyageDetails(int idleTime2, int travelTime2, int speed2,
			int startTime2, VoyageOptions options,
			EnumMap<FuelComponent, EnumMap<FuelUnit, Long>> fuelConsumption2,
			EnumMap<FuelComponent, Integer> fuelUnitPrices2) {
		this.idleTime = idleTime2;
		this.travelTime = travelTime2;
		this.speed = speed2;
		this.startTime = startTime2;
		this.options = options;
		this.fuelConsumption = fuelConsumption2;
		this.fuelUnitPrices = fuelUnitPrices2;
	}

	public VoyageDetails clone() {
		return new VoyageDetails(idleTime, travelTime, speed, startTime, new VoyageOptions(options), fuelConsumption, fuelUnitPrices);
	}

	public final long getFuelConsumption(final FuelComponent fuel,
			final FuelUnit fuelUnit) {

//		if (fuelConsumption.containsKey(fuel)) {

			final EnumMap<FuelUnit, Long> map = fuelConsumption.get(fuel);
			if (map.containsKey(fuelUnit)) {
				return map.get(fuelUnit);
			}
//		}
		return 0l;
	}

	public final int getIdleTime() {
		return idleTime;
	}

	public final VoyageOptions getOptions() {
		return options;
	}

	public final int getSpeed() {
		return speed;
	}

	public final int getTravelTime() {
		return travelTime;
	}

	public final void setFuelConsumption(final FuelComponent fuel,
			final FuelUnit fuelUnit, final long consumption) {
//		final EnumMap<FuelUnit, Long> map;
//		if (fuelConsumption.containsKey(fuel)) {
//			map = fuelConsumption.get(fuel);
//		} else {
//			map = new EnumMap<FuelUnit, Long>(FuelUnit.class);
//			fuelConsumption.put(fuel, map);
//		}
//		map.put(fuelUnit, consumption);
		fuelConsumption.get(fuel).put(fuelUnit, consumption);
	}

	public final void setIdleTime(final int idleTime) {
		this.idleTime = idleTime;
	}

	public final void setOptions(final VoyageOptions options) {
		this.options = options;
	}

	public final void setSpeed(final int speed) {
		this.speed = speed;
	}

	public final void setTravelTime(final int travelTime) {
		this.travelTime = travelTime;
	}

	public final int getStartTime() {
		return startTime;
	}

	public final void setStartTime(final int startTime) {
		this.startTime = startTime;
	}

	public final int getFuelUnitPrice(final FuelComponent fuel) {

		if (fuelUnitPrices.containsKey(fuel)) {
			return fuelUnitPrices.get(fuel);
		} else {
			return 0;
		}
	}

	public final void setFuelUnitPrice(final FuelComponent fuel,
			final int unitPrice) {
		fuelUnitPrices.put(fuel, unitPrice);
	}

	@Override
	public final boolean equals(final Object obj) {

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

	@Override
	public String toString() {
		return "VoyageDetails [options=" + options + ", fuelConsumption="
				+ fuelConsumption + ", fuelUnitPrices=" + fuelUnitPrices
				+ ", idleTime=" + idleTime + ", travelTime=" + travelTime
				+ ", speed=" + speed + ", startTime=" + startTime + "]";
	}
}
