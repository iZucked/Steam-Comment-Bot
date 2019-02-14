/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.mmxlabs.common.impl.LongFastEnumEnumMap;
import com.mmxlabs.common.impl.LongFastEnumMap;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * Implementation of {@link VoyageDetails}.
 * 
 * @author Simon Goodall
 * 
 */
public final class VoyageDetails implements IDetailsSequenceElement, Cloneable {

	private @NonNull VoyageOptions options;

	private final LongFastEnumEnumMap<FuelComponent, FuelUnit> fuelConsumption = new LongFastEnumEnumMap<>(FuelComponent.values().length, FuelUnit.values().length);

	private final LongFastEnumEnumMap<FuelComponent, FuelUnit> routeAdditionalConsumption = new LongFastEnumEnumMap<>(FuelComponent.values().length, FuelUnit.values().length);

	private final LongFastEnumMap<FuelComponent> fuelUnitPrices = new LongFastEnumMap<>(FuelComponent.values().length);

	private int idleTime;
	private int travelTime;
	private int speed;
	private int startTime;
	private boolean cooldownPerformed;
	
	/*
	 * NBO travel times
	 */
	private int idleNBOHours;
	private int travelNBOHours;

	public VoyageDetails(@NonNull final VoyageOptions options) {
		this.options = options;
	}

	private VoyageDetails(final int idleTime2, final int travelTime2, final int speed2, final int startTime2, final @NonNull VoyageOptions options,
			final LongFastEnumEnumMap<FuelComponent, FuelUnit> fuelConsumption2, final @NonNull LongFastEnumEnumMap<FuelComponent, FuelUnit> routeAdditionalConsumption2,
			final @NonNull LongFastEnumMap<FuelComponent> fuelUnitPrices2, final boolean cooldownPerformed, int idleNBOHours, int travelNBOHours) {
		this.idleTime = idleTime2;
		this.travelTime = travelTime2;
		this.speed = speed2;
		this.startTime = startTime2;
		this.options = options;
		this.setIdleNBOHours(idleNBOHours);
		this.setTravelNBOHours(travelNBOHours);
		this.fuelConsumption.putAll(fuelConsumption2);
		this.fuelUnitPrices.putAll(fuelUnitPrices2);
		this.routeAdditionalConsumption.putAll(routeAdditionalConsumption2);
		this.cooldownPerformed = cooldownPerformed;
	}

	@Override
	public VoyageDetails clone() {
		return new VoyageDetails(idleTime, travelTime, speed, startTime, new VoyageOptions(options), fuelConsumption,
				routeAdditionalConsumption, fuelUnitPrices, cooldownPerformed, getIdleNBOHours(), getTravelNBOHours());
	}

	public final long getFuelConsumption(final @NonNull FuelKey fuelKey) {
		return fuelConsumption.get(fuelKey.getFuelComponent(), fuelKey.getFuelUnit());
	}

	public final long getRouteAdditionalConsumption(final @NonNull FuelKey fuelKey) {
		return routeAdditionalConsumption.get(fuelKey.getFuelComponent(), fuelKey.getFuelUnit());
	}

	public void setFuelConsumption(final @NonNull FuelKey fuelKey, final long consumption) {
		fuelConsumption.put(fuelKey.getFuelComponent(), fuelKey.getFuelUnit(), consumption);
	}

	public void setRouteAdditionalConsumption(final @NonNull FuelKey fuelKey, final long consumption) {
		routeAdditionalConsumption.put(fuelKey.getFuelComponent(), fuelKey.getFuelUnit(), consumption);
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

	public final void resetFuelConsumption() {
		fuelConsumption.clear();
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

	public final int getFuelUnitPrice(final @NonNull FuelComponent baseFuel) {
		return (int) fuelUnitPrices.get(baseFuel);
	}

	public final void setFuelUnitPrice(final @NonNull FuelComponent baseFuel, final int unitPrice) {
		fuelUnitPrices.put(baseFuel, unitPrice);
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj == this) {
			return true;
		}

		if (obj instanceof VoyageDetails) {
			final VoyageDetails d = (VoyageDetails) obj;

			// Ensure all fields are present here
			// @formatter:off
			return speed == d.speed
				&& cooldownPerformed == d.cooldownPerformed
				&& idleTime ==  d.idleTime
				&& travelTime ==  d.travelTime
				&& startTime ==  d.startTime
				&& Objects.equal(options,  d.options)
				&& Objects.equal(fuelUnitPrices,  d.fuelUnitPrices)
				&& Objects.equal(fuelConsumption,  d.fuelConsumption)
				&& Objects.equal(routeAdditionalConsumption,  d.routeAdditionalConsumption)
				&& Objects.equal(getIdleNBOHours(),  d.getIdleNBOHours())
				&& Objects.equal(getTravelNBOHours(),  d.getTravelNBOHours())
				;
				// @formatter:on
		}
		return false;
	}

	@Override
	public String toString() {
		// @formatter:off
		return MoreObjects.toStringHelper(VoyageDetails.class)
				.add("options", options)
				.add("fuelConsumption", fuelConsumption)
				.add("routeAdditionalConsumption", routeAdditionalConsumption)
				.add("fuelUnitPrices", fuelUnitPrices)
				.add("cooldownPerformed", cooldownPerformed)
				.add("idleTime", idleTime)
				.add("travelTime", travelTime)
				.add("speed", speed)
				.add("startTime", startTime)
				.add("idleNBOHours", getIdleNBOHours())
				.add("travelNBOHours", getTravelNBOHours())
				.toString();
		// @formatter:on
	}

	public boolean isCooldownPerformed() {
		return cooldownPerformed;
	}

	public void setCooldownPerformed(final boolean cooldownPerformed) {
		this.cooldownPerformed = cooldownPerformed;
	}

	public int getIdleNBOHours() {
		return idleNBOHours;
	}

	public void setIdleNBOHours(int idleNBOHours) {
		this.idleNBOHours = idleNBOHours;
	}

	public int getTravelNBOHours() {
		return travelNBOHours;
	}

	public void setTravelNBOHours(int travelNBOHours) {
		this.travelNBOHours = travelNBOHours;
	}
}
