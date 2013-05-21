/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.Arrays;

import com.google.common.base.Objects;
import com.mmxlabs.common.impl.LongFastEnumMap;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * Implementation of {@link VoyagePlan}. A {@link VoyagePlan} is a collections of elements - {@link PortDetails} and {@link VoyageDetails} representing a related set of voyages. For example a
 * {@link VoyagePlan} may represent a single Cargo - A load, voyage, discharge, voyage, next destination sequence. Typically the last element of a {@link VoyagePlan} will be the start of the next
 * {@link VoyagePlan}. In such cases the last element is used to provide information, but costings etc are to be applied to the next {@link VoyagePlan}. However in some cases we will wish to include
 * the element. In such cases {@link #isIgnoreEnd()} will return false.
 * 
 * @author Simon Goodall
 * 
 */
public final class VoyagePlan implements Cloneable {

	/**
	 * An enum for use with remaining heel to denote it's allocation.
	 * 
	 * @since 3.1
	 * 
	 */
	public enum HeelType {

		/**
		 * No heel, nothing to do.
		 */
		NONE,

		/**
		 * Remaining heel is left at end of voyage. It should be included in total consumed voyage gas, then discarded
		 */
		END,
		/**
		 * Remaining heel will be discharged.
		 */
		DISCHARGE
	}

	private Object[] sequence;
	private final LongFastEnumMap<FuelComponent> fuelConsumptions;
	private final LongFastEnumMap<FuelComponent> routeAdditionalConsumption;
	private final LongFastEnumMap<FuelComponent> fuelCosts;
	private long lngFuelVolume;
	private int violationsCount;
	private boolean ignoreEnd;

	private long remainingHeelInM3;

	private HeelType remainingHeelType = HeelType.NONE;

	public VoyagePlan() {
		fuelConsumptions = new LongFastEnumMap<FuelComponent>(FuelComponent.values().length);
		routeAdditionalConsumption = new LongFastEnumMap<FuelComponent>(FuelComponent.values().length);
		fuelCosts = new LongFastEnumMap<FuelComponent>(FuelComponent.values().length);
		ignoreEnd = true;
	}

	protected VoyagePlan(final Object[] sequence, final long fuelVolume, final LongFastEnumMap<FuelComponent> fuelConsumptions, final LongFastEnumMap<FuelComponent> routeAdditionalConsumption,
			final LongFastEnumMap<FuelComponent> fuelCosts, final int violationsCount, final boolean ignoreEnd, final long remainingHeelInM3, final HeelType remainingHeelType) {
		super();
		this.sequence = sequence;
		this.fuelConsumptions = fuelConsumptions;
		this.routeAdditionalConsumption = routeAdditionalConsumption;
		this.fuelCosts = fuelCosts;
		this.lngFuelVolume = fuelVolume;
		this.violationsCount = violationsCount;
		this.ignoreEnd = ignoreEnd;
		this.remainingHeelInM3 = remainingHeelInM3;
		this.remainingHeelType = remainingHeelType;
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
	 * @since 5.0
	 */
	public final int getViolationsCount() {
		return violationsCount;
	}

	/**
	 * @since 5.0
	 */
	public final void setViolationsCount(final int violationsCount) {
		this.violationsCount = violationsCount;
	}

	public final Object[] getSequence() {
		return sequence;
	}

	public final void setSequence(final Object[] sequence) {
		this.sequence = sequence;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof VoyagePlan) {
			final VoyagePlan plan = (VoyagePlan) obj;

			// Ensure all fields are present here
			// @formatter:off
			return Objects.equal(lngFuelVolume, plan.lngFuelVolume)
					&& Objects.equal(sequence, plan.sequence)
					&& Objects.equal(violationsCount, plan.violationsCount)
					&& Objects.equal(fuelConsumptions, plan.fuelConsumptions)
					&& Objects.equal(routeAdditionalConsumption, plan.routeAdditionalConsumption)
					&& Objects.equal(fuelCosts, plan.fuelCosts)
					&& Objects.equal(remainingHeelInM3, plan.remainingHeelInM3)
					&& Objects.equal(remainingHeelType, plan.remainingHeelType)
					;
			// @formatter:on
		}

		return false;
	}

	@Override
	public String toString() {
		return "VoyagePlan [sequence=" + Arrays.toString(sequence) + ", fuelConsumptions=" + fuelConsumptions + ", routeAdditionalConsumption=" + routeAdditionalConsumption + ", fuelCosts="
				+ fuelCosts + "]";
	}

	@Override
	public final VoyagePlan clone() {
		final Object[] clonedSequence = new Object[sequence.length];
		int k = 0;
		for (final Object o : sequence) {
			if (o instanceof VoyageDetails) {
				clonedSequence[k++] = ((VoyageDetails) o).clone();
			} else if (o instanceof PortDetails) {
				clonedSequence[k++] = ((PortDetails) o).clone();
			} else {
				clonedSequence[k++] = o;
			}
		}
		return new VoyagePlan(clonedSequence, lngFuelVolume, fuelConsumptions, routeAdditionalConsumption, fuelCosts, violationsCount, ignoreEnd, remainingHeelInM3, remainingHeelType);
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

	private int totalRouteCost;

	public void setTotalRouteCost(final int routeCost) {
		totalRouteCost = routeCost;
	}

	public int getTotalRouteCost() {
		return totalRouteCost;
	}

	/**
	 * Returns true if the last element is a marker element and should be ignored in terms of it's time, fuel consumption, costs etc.
	 * 
	 * @return
	 * @since 2.0
	 */
	public boolean isIgnoreEnd() {
		return ignoreEnd;
	}

	public void setIgnoreEnd(final boolean ignoreEnd) {
		this.ignoreEnd = ignoreEnd;
	}

	/**
	 * @since 3.1
	 */
	public long getRemainingHeelInM3() {
		return remainingHeelInM3;
	}

	/**
	 * Set the heel that remains at the end of this voyage plan - typically (always...) due to the vessel class min heel. This heel may be discharged or discarded depending where in the voyage plan it
	 * occurred (where is not tracked explicitly).
	 * 
	 * @param remainingHeelInM3
	 * @param remainingHeelType
	 * @since 3.1
	 */
	public void setRemainingHeelInM3(final long remainingHeelInM3, final HeelType remainingHeelType) {
		this.remainingHeelInM3 = remainingHeelInM3;
		this.remainingHeelType = remainingHeelType;
	}

	/**
	 * @since 3.1
	 */
	public HeelType getRemainingHeelType() {
		return remainingHeelType;
	}
}
