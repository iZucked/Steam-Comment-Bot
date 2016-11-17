/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.eclipse.jdt.annotation.NonNull;

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
public final class VoyageDetails implements IDetailsSequenceElement, Cloneable {

	private @NonNull VoyageOptions options;

	private final @NonNull LongFastEnumEnumMap<FuelComponent, FuelUnit> fuelConsumption = new LongFastEnumEnumMap<FuelComponent, FuelUnit>(FuelComponent.values().length, FuelUnit.values().length);

	private final @NonNull LongFastEnumEnumMap<FuelComponent, FuelUnit> routeAdditionalConsumption = new LongFastEnumEnumMap<FuelComponent, FuelUnit>(FuelComponent.values().length,
			FuelUnit.values().length);

	private final @NonNull LongFastEnumMap<FuelComponent> fuelUnitPrices = new LongFastEnumMap<FuelComponent>(FuelComponent.values().length);

	private int idleTime;

	private int travelTime;

	private int speed;

	private int startTime;

	private boolean cooldownPerformed;

	public VoyageDetails(@NonNull final VoyageOptions options) {
		this.options = options;
	}

	private VoyageDetails(final int idleTime2, final int travelTime2, final int speed2, final int startTime2, final @NonNull VoyageOptions options,
			final @NonNull LongFastEnumEnumMap<FuelComponent, FuelUnit> fuelConsumption2, final @NonNull LongFastEnumEnumMap<FuelComponent, FuelUnit> routeAdditionalConsumption2,
			final @NonNull LongFastEnumMap<FuelComponent> fuelUnitPrices2, final boolean cooldownPerformed) {
		this.idleTime = idleTime2;
		this.travelTime = travelTime2;
		this.speed = speed2;
		this.startTime = startTime2;
		this.options = options;
		this.fuelConsumption.putAll(fuelConsumption2);
		this.fuelUnitPrices.putAll(fuelUnitPrices2);
		this.routeAdditionalConsumption.putAll(routeAdditionalConsumption2);
		this.cooldownPerformed = cooldownPerformed;
	}

	@Override
	public VoyageDetails clone() {
		return new VoyageDetails(idleTime, travelTime, speed, startTime, new VoyageOptions(options), fuelConsumption, routeAdditionalConsumption, fuelUnitPrices, cooldownPerformed);
	}

	public final long getFuelConsumption(final @NonNull FuelComponent fuel, final @NonNull FuelUnit fuelUnit) {

		return fuelConsumption.get(fuel, fuelUnit);
	}

	public final long getRouteAdditionalConsumption(final @NonNull FuelComponent fuel, final @NonNull FuelUnit fuelUnit) {
		return routeAdditionalConsumption.get(fuel, fuelUnit);
	}

	public final void setRouteAdditionalConsumption(final @NonNull FuelComponent fuel, final @NonNull FuelUnit fuelUnit, final long consumption) {
		routeAdditionalConsumption.put(fuel, fuelUnit, consumption);
	}

	public final int getIdleTime() {
		return idleTime;
	}

	public final @NonNull VoyageOptions getOptions() {
		return options;
	}

	public final int getSpeed() {
		return speed;
	}

	public final int getTravelTime() {
		return travelTime;
	}

	public final void setFuelConsumption(final @NonNull FuelComponent fuel, final @NonNull FuelUnit fuelUnit, final long consumption) {
		fuelConsumption.put(fuel, fuelUnit, consumption);
	}

	public final void setIdleTime(final int idleTime) {
		this.idleTime = idleTime;
	}

	public final void setOptions(final @NonNull VoyageOptions options) {
		this.options = options;
	}

	public final void setSpeed(final int speed) {
		this.speed = speed;
	}

	public final void setTravelTime(final int travelTime) {
		this.travelTime = travelTime;
	}

	public final int getFuelUnitPrice(final @NonNull FuelComponent fuel) {

		// This cast is ok as #setFuelUnitPrice takes the input as an int
		return (int) fuelUnitPrices.get(fuel);
	}

	public final void setFuelUnitPrice(final @NonNull FuelComponent fuel, final int unitPrice) {
		fuelUnitPrices.put(fuel, unitPrice);
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
				&& Objects.equal(options,  d.options)
				&& Objects.equal(fuelConsumption,  d.fuelConsumption)
				&& Objects.equal(routeAdditionalConsumption,  d.routeAdditionalConsumption)
				&& Objects.equal(fuelUnitPrices,  d.fuelUnitPrices)
				&& Objects.equal(cooldownPerformed,  d.cooldownPerformed)
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
				.add("cooldownPerformed", cooldownPerformed)
				.add("idleTime", idleTime)
				.add("travelTime", travelTime)
				.add("speed", speed)
				.add("startTime", startTime)
				.toString();
		// @formatter:on
	}

	public boolean isCooldownPerformed() {
		return cooldownPerformed;
	}

	public void setCooldownPerformed(final boolean cooldownPerformed) {
		this.cooldownPerformed = cooldownPerformed;
	}
}
