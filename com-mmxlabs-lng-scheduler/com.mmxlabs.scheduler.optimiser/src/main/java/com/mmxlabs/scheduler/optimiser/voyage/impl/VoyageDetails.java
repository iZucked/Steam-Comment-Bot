/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.impl.LongFastEnumEnumMap;
import com.mmxlabs.common.impl.LongFastEnumMap;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * Implementation of {@link VoyageDetails}.
 * 
 * @author Simon Goodall
 * 
 */
public final class VoyageDetails implements Cloneable {

	private VoyageOptions options;

	private final LongFastEnumEnumMap<FuelComponent, FuelUnit> fuelConsumption;

	private final LongFastEnumMap<FuelComponent> fuelUnitPrices;

	private int idleTime;

	private int travelTime;

	private int speed;

	private int startTime;

	public VoyageDetails() {
		fuelConsumption = new LongFastEnumEnumMap<FuelComponent, FuelUnit>(FuelComponent.values().length, FuelUnit.values().length);
		fuelUnitPrices = new LongFastEnumMap<FuelComponent>(FuelComponent.values().length);
	}

	public VoyageDetails(final int idleTime2, final int travelTime2, final int speed2, final int startTime2, final VoyageOptions options,
			final LongFastEnumEnumMap<FuelComponent, FuelUnit> fuelConsumption2, final LongFastEnumMap<FuelComponent> fuelUnitPrices2) {
		this.idleTime = idleTime2;
		this.travelTime = travelTime2;
		this.speed = speed2;
		this.startTime = startTime2;
		this.options = options;
		this.fuelConsumption = fuelConsumption2;
		this.fuelUnitPrices = fuelUnitPrices2;
	}

	@Override
	public VoyageDetails clone() {
		return new VoyageDetails(idleTime, travelTime, speed, startTime, new VoyageOptions(options), fuelConsumption, fuelUnitPrices);
	}

	public final long getFuelConsumption(final FuelComponent fuel, final FuelUnit fuelUnit) {

		return fuelConsumption.get(fuel, fuelUnit);
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

	public final void setFuelConsumption(final FuelComponent fuel, final FuelUnit fuelUnit, final long consumption) {
		fuelConsumption.put(fuel, fuelUnit, consumption);
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

	// public final int getStartTime() {
	// return startTime;
	// }
	//
	// public final void setStartTime(final int startTime) {
	// this.startTime = startTime;
	// }

	public final int getFuelUnitPrice(final FuelComponent fuel) {

		// if (fuelUnitPrices.containsKey(fuel)) {
		return (int) fuelUnitPrices.get(fuel);
		// } else {
		// return 0;
		// }
	}

	public final void setFuelUnitPrice(final FuelComponent fuel, final int unitPrice) {
		fuelUnitPrices.put(fuel, unitPrice);
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof VoyageDetails) {
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
		return "VoyageDetails [options=" + options + ", fuelConsumption=" + fuelConsumption + ", fuelUnitPrices=" + fuelUnitPrices + ", idleTime=" + idleTime + ", travelTime=" + travelTime
				+ ", speed=" + speed + ", startTime=" + startTime + ", route cost = " + routeCost + "]";
	}

	private long routeCost = 0;

	public void setRouteCost(final long price) {
		routeCost = price;
	}

	public long getRouteCost() {
		return routeCost;
	}
}
