/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.Arrays;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.impl.LongFastEnumMap;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * Implementation of {@link VoyagePlan}.
 * 
 * @author Simon Goodall
 * 
 */
public final class VoyagePlan implements Cloneable {

	private Object[] sequence;
	private final LongFastEnumMap<FuelComponent> fuelConsumptions;
	private final LongFastEnumMap<FuelComponent> routeAdditionalConsumption;
	private final LongFastEnumMap<FuelComponent> fuelCosts;
	private long lngFuelVolume;
	private int capacityViolations;

	public VoyagePlan() {
		fuelConsumptions = new LongFastEnumMap<FuelComponent>(FuelComponent.values().length);
		routeAdditionalConsumption = new LongFastEnumMap<FuelComponent>(FuelComponent.values().length);
		fuelCosts = new LongFastEnumMap<FuelComponent>(FuelComponent.values().length);
	}

	protected VoyagePlan(final Object[] sequence, final long fuelVolume, final LongFastEnumMap<FuelComponent> fuelConsumptions, final LongFastEnumMap<FuelComponent> routeAdditionalConsumption,
			final LongFastEnumMap<FuelComponent> fuelCosts, final int capacityViolations) {
		super();
		this.sequence = sequence;
		this.fuelConsumptions = fuelConsumptions;
		this.routeAdditionalConsumption = routeAdditionalConsumption;
		this.fuelCosts = fuelCosts;
		this.lngFuelVolume = fuelVolume;
		this.capacityViolations = capacityViolations;
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

	public final int getCapacityViolations() {
		return capacityViolations;
	}

	public final void setCapacityViolations(final int number) {
		capacityViolations = number;
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
			final VoyagePlan p = (VoyagePlan) obj;

			if (p.lngFuelVolume != lngFuelVolume) {
				return false;
			}

			if (!Equality.isEqual(sequence, p.sequence)) {
				return false;
			}
			if (!Equality.isEqual(capacityViolations, p.capacityViolations)) {
				return false;
			}
			if (!Equality.isEqual(fuelConsumptions, p.fuelConsumptions)) {
				return false;
			}
			if (!Equality.isEqual(routeAdditionalConsumption, p.routeAdditionalConsumption)) {
				return false;
			}
			if (!Equality.isEqual(fuelCosts, p.fuelCosts)) {
				return false;
			}
			return true;
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
		return new VoyagePlan(clonedSequence, lngFuelVolume, fuelConsumptions, routeAdditionalConsumption, fuelCosts, capacityViolations);
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
}
