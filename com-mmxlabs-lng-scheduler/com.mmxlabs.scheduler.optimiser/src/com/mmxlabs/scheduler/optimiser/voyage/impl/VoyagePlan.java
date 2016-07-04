/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.Arrays;

import com.google.common.base.Objects;
import com.mmxlabs.common.impl.LongFastEnumMap;
import com.mmxlabs.scheduler.optimiser.fitness.impl.CachingVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * Implementation of {@link VoyagePlan}. A {@link VoyagePlan} is a collections of elements - {@link PortDetails} and {@link VoyageDetails} representing a related set of voyages. For example a
 * {@link VoyagePlan} may represent a single Cargo - A load, voyage, discharge, voyage, next destination sequence. Typically the last element of a {@link VoyagePlan} will be the start of the next
 * {@link VoyagePlan}. In such cases the last element is used to provide information, but costings etc are to be applied to the next {@link VoyagePlan}. However in some cases we will wish to include
 * the element. In such cases {@link #isIgnoreEnd()} will return false.
 * 
 * Note we do not include arrival times as the {@link VoyagePlan} is cached in the {@link CachingVoyagePlanOptimiser} in a time independent way.
 * 
 * @author Simon Goodall
 * 
 */
public final class VoyagePlan implements Cloneable {

	private IDetailsSequenceElement[] sequence;
	private long charterInRatePerDay;
	private final LongFastEnumMap<FuelComponent> fuelConsumptions;
	private final LongFastEnumMap<FuelComponent> routeAdditionalConsumption;
	private final LongFastEnumMap<FuelComponent> fuelCosts;
	private long lngFuelVolume;
	private int violationsCount;
	private boolean ignoreEnd;

	private long startingHeelInM3;
	private long remainingHeelInM3;

	public VoyagePlan() {
		fuelConsumptions = new LongFastEnumMap<FuelComponent>(FuelComponent.values().length);
		routeAdditionalConsumption = new LongFastEnumMap<FuelComponent>(FuelComponent.values().length);
		fuelCosts = new LongFastEnumMap<FuelComponent>(FuelComponent.values().length);
		ignoreEnd = true;
	}

	protected VoyagePlan(final IDetailsSequenceElement[] sequence, final long charterInRatePerDay, final long fuelVolume, final LongFastEnumMap<FuelComponent> fuelConsumptions,
			final LongFastEnumMap<FuelComponent> routeAdditionalConsumption, final LongFastEnumMap<FuelComponent> fuelCosts, final int violationsCount, final boolean ignoreEnd,
			final long startingHeelInM3, final long remainingHeelInM3) {
		super();
		this.sequence = sequence;
		this.charterInRatePerDay = charterInRatePerDay;
		this.fuelConsumptions = fuelConsumptions;
		this.routeAdditionalConsumption = routeAdditionalConsumption;
		this.fuelCosts = fuelCosts;
		this.lngFuelVolume = fuelVolume;
		this.violationsCount = violationsCount;
		this.ignoreEnd = ignoreEnd;
		this.startingHeelInM3 = startingHeelInM3;
		this.remainingHeelInM3 = remainingHeelInM3;
	}

	public final long getFuelConsumption(final FuelComponent fuel) {
		return fuelConsumptions.get(fuel);
	}

	public final void setFuelConsumption(final FuelComponent fuel, final long consumption) {
		fuelConsumptions.put(fuel, consumption);
	}

	public final long getRouteAdditionalConsumption(final FuelComponent fuel) {
		return routeAdditionalConsumption.get(fuel);
	}

	public final void setRouteAdditionalConsumption(final FuelComponent fuel, final long consumption) {
		routeAdditionalConsumption.put(fuel, consumption);
	}

	public final long getTotalFuelCost(final FuelComponent fuel) {
		return fuelCosts.get(fuel);
	}

	public final void setTotalFuelCost(final FuelComponent fuel, final long cost) {
		fuelCosts.put(fuel, cost);
	}

	/**
	 */
	public final int getViolationsCount() {
		return violationsCount;
	}

	/**
	 */
	public final void setViolationsCount(final int violationsCount) {
		this.violationsCount = violationsCount;
	}

	/**
	 */
	public final IDetailsSequenceElement[] getSequence() {
		return sequence;
	}

	/**
	 */
	public final void setSequence(final IDetailsSequenceElement[] sequence) {
		this.sequence = sequence;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof VoyagePlan) {
			final VoyagePlan plan = (VoyagePlan) obj;

			// Ensure all fields are present here
			// @formatter:off
			return Objects.equal(lngFuelVolume, plan.lngFuelVolume)
					&& Objects.equal(charterInRatePerDay, plan.charterInRatePerDay)
					&& Objects.equal(violationsCount, plan.violationsCount)
					&& Objects.equal(fuelConsumptions, plan.fuelConsumptions)
					&& Objects.equal(routeAdditionalConsumption, plan.routeAdditionalConsumption)
					&& Objects.equal(fuelCosts, plan.fuelCosts)
					&& Objects.equal(startingHeelInM3, plan.startingHeelInM3)
					&& Objects.equal(remainingHeelInM3, plan.remainingHeelInM3)
					&& Arrays.deepEquals(sequence, plan.sequence)

					;
			// @formatter:on
		}

		return false;
	}

	@Override
	public int hashCode() {
		// Simple hashCode implementation to fix findbugs warnings.
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "VoyagePlan [sequence=" + Arrays.toString(sequence) + ", charterInRatePerDay=" + charterInRatePerDay + ", fuelConsumptions=" + fuelConsumptions + ", routeAdditionalConsumption="
				+ routeAdditionalConsumption + ", fuelCosts=" + fuelCosts + "]";
	}

	@Override
	public final VoyagePlan clone() {
		final IDetailsSequenceElement[] clonedSequence = new IDetailsSequenceElement[sequence.length];
		int k = 0;
		for (final IDetailsSequenceElement o : sequence) {
			if (o instanceof VoyageDetails) {
				clonedSequence[k++] = ((VoyageDetails) o).clone();
			} else if (o instanceof PortDetails) {
				clonedSequence[k++] = ((PortDetails) o).clone();
			} else {
				clonedSequence[k++] = o;
			}
		}
		return new VoyagePlan(clonedSequence, charterInRatePerDay, lngFuelVolume, fuelConsumptions, routeAdditionalConsumption, fuelCosts, violationsCount, ignoreEnd, startingHeelInM3,
				remainingHeelInM3);
	}

	/**
	 * Set the total quantity in M3 of LNG used for fuel in this voyageplan
	 * 
	 * @param lngConsumed
	 */
	public void setLNGFuelVolume(final long lngConsumed) {
		this.lngFuelVolume = lngConsumed;
	}

	/**
	 * @return the total quantity in M3 of LNG used for fuel in this plan.
	 */
	public final long getLNGFuelVolume() {
		return lngFuelVolume;
	}

	private long totalRouteCost;

	public void setTotalRouteCost(final long routeCost) {
		totalRouteCost = routeCost;
	}

	public long getTotalRouteCost() {
		return totalRouteCost;
	}

	/**
	 * Returns true if the last element is a marker element and should be ignored in terms of it's time, fuel consumption, costs etc.
	 * 
	 * @return
	 */
	public boolean isIgnoreEnd() {
		return ignoreEnd;
	}

	public void setIgnoreEnd(final boolean ignoreEnd) {
		this.ignoreEnd = ignoreEnd;
	}

	/**
	 */
	public long getRemainingHeelInM3() {
		return remainingHeelInM3;
	}

	/**
	 * Set the heel that remains at the end of this voyage plan - typically (always...) due to the vessel class safety heel.
	 * 
	 * @param remainingHeelInM3
	 */
	public void setRemainingHeelInM3(final long remainingHeelInM3) {
		this.remainingHeelInM3 = remainingHeelInM3;
	}

	public long getStartingHeelInM3() {
		return startingHeelInM3;
	}

	public void setStartingHeelInM3(final long startingHeelInM3) {
		this.startingHeelInM3 = startingHeelInM3;
	}

	public long getCharterInRatePerDay() {
		return charterInRatePerDay;
	}

	public void setCharterInRatePerDay(final long charterInRatePerDay) {
		this.charterInRatePerDay = charterInRatePerDay;
	}
}
