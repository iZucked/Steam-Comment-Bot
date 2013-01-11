/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import com.google.common.base.Objects;
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

	private final LongFastEnumEnumMap<FuelComponent, FuelUnit> routeAdditionalConsumption;

	private final LongFastEnumMap<FuelComponent> fuelUnitPrices;

	private int idleTime;

	private int travelTime;

	private int speed;

	private int startTime;

	private long routeCost = 0;

	private boolean charterOutIdleTime;

	public VoyageDetails() {
		fuelConsumption = new LongFastEnumEnumMap<FuelComponent, FuelUnit>(FuelComponent.values().length, FuelUnit.values().length);
		routeAdditionalConsumption = new LongFastEnumEnumMap<FuelComponent, FuelUnit>(FuelComponent.values().length, FuelUnit.values().length);
		fuelUnitPrices = new LongFastEnumMap<FuelComponent>(FuelComponent.values().length);
	}

	public VoyageDetails(final int idleTime2, final int travelTime2, final int speed2, final int startTime2, final long routeCost2, final VoyageOptions options,
			final LongFastEnumEnumMap<FuelComponent, FuelUnit> fuelConsumption2, final LongFastEnumEnumMap<FuelComponent, FuelUnit> routeAdditionalConsumption2,
			final LongFastEnumMap<FuelComponent> fuelUnitPrices2) {
		this.idleTime = idleTime2;
		this.travelTime = travelTime2;
		this.speed = speed2;
		this.startTime = startTime2;
		this.routeCost = routeCost2;
		this.options = options;
		this.fuelConsumption = fuelConsumption2;
		this.fuelUnitPrices = fuelUnitPrices2;
		this.routeAdditionalConsumption = routeAdditionalConsumption2;
	}

	@Override
	public VoyageDetails clone() {
		return new VoyageDetails(idleTime, travelTime, speed, startTime, routeCost, new VoyageOptions(options), fuelConsumption, routeAdditionalConsumption, fuelUnitPrices);
	}

	public final long getFuelConsumption(final FuelComponent fuel, final FuelUnit fuelUnit) {

		return fuelConsumption.get(fuel, fuelUnit);
	}

	public final long getRouteAdditionalConsumption(final FuelComponent fuel, final FuelUnit fuelUnit) {
		return routeAdditionalConsumption.get(fuel, fuelUnit);
	}

	public final void setRouteAdditionalConsumption(final FuelComponent fuel, final FuelUnit fuelUnit, final long consumption) {
		routeAdditionalConsumption.put(fuel, fuelUnit, consumption);
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

	public final int getFuelUnitPrice(final FuelComponent fuel) {

		return (int) fuelUnitPrices.get(fuel);
	}

	public final void setFuelUnitPrice(final FuelComponent fuel, final int unitPrice) {
		fuelUnitPrices.put(fuel, unitPrice);
	}

	public void setRouteCost(final long price) {
		routeCost = price;
	}

	public long getRouteCost() {
		return routeCost;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof VoyageDetails) {
			final VoyageDetails d = (VoyageDetails) obj;

			// Ensure all fields are present here
			// @formatter:off
			return Objects.equal(speed,  d.speed)
				&& Objects.equal(idleTime,  d.idleTime)
				&& Objects.equal(travelTime,  d.travelTime)
				&& Objects.equal(startTime,  d.startTime)
				&& Objects.equal(charterOutIdleTime,  d.charterOutIdleTime)
				&& Objects.equal(routeCost,  d.routeCost)
				&& Objects.equal(options,  d.options)
				&& Objects.equal(fuelConsumption,  d.fuelConsumption)
				&& Objects.equal(routeAdditionalConsumption,  d.routeAdditionalConsumption)
				&& Objects.equal(fuelUnitPrices,  d.fuelUnitPrices)
				;
				// @formatter:on
		}
		return false;
	}

	@Override
	public String toString() {
		// @formatter:off
		return Objects.toStringHelper(VoyageDetails.class)
				.add("options", options)
				.add("fuelConsumption", fuelConsumption)
				.add("routeAdditionalConsumption", routeAdditionalConsumption)
				.add("fuelUnitPrices", fuelUnitPrices)
				.add("idleTime", idleTime)
				.add("travelTime", travelTime)
				.add("speed", speed)
				.add("startTime", startTime)
				.add("routeCost", routeCost)
				.toString();
		// @formatter:on
	}
}
