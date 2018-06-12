/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.Arrays;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.mmxlabs.scheduler.optimiser.fitness.impl.CachingVoyagePlanOptimiser;

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
	private boolean locked = false;

	private IDetailsSequenceElement[] sequence;
	private long charterInRatePerDay;
	private long lngFuelVolume;
	private long lngFuelCost;
	private long cooldownCost;
	private long baseFuelCost;
	private long totalRouteCost;
	private long startHeelCost;

	private int violationsCount;
	private boolean ignoreEnd;

	private long startingHeelInM3;
	private long remainingHeelInM3;

	public VoyagePlan() {
		ignoreEnd = true;
	}

	protected VoyagePlan(final IDetailsSequenceElement[] sequence, final long charterInRatePerDay, final long fuelVolume, final int violationsCount, final boolean ignoreEnd,
			final long startingHeelInM3, final long remainingHeelInM3) {
		super();
		this.sequence = sequence;
		this.charterInRatePerDay = charterInRatePerDay;
		this.lngFuelVolume = fuelVolume;
		this.violationsCount = violationsCount;
		this.ignoreEnd = ignoreEnd;
		this.startingHeelInM3 = startingHeelInM3;
		this.remainingHeelInM3 = remainingHeelInM3;
	}

	public long getLngFuelCost() {
		return lngFuelCost;
	}

	public void setLngFuelCost(final long lngFuelCost) {
		assert !locked;

		this.lngFuelCost = lngFuelCost;
	}

	public long getCooldownCost() {
		return cooldownCost;
	}

	public void setCooldownCost(final long cooldownCost) {
		assert !locked;
		this.cooldownCost = cooldownCost;
	}

	public long getBaseFuelCost() {
		return baseFuelCost;
	}

	public void setBaseFuelCost(final long baseFuelCost) {
		assert !locked;
		this.baseFuelCost = baseFuelCost;
	}

	/**
	 */
	public final int getViolationsCount() {
		return violationsCount;
	}

	/**
	 */
	public final void setViolationsCount(final int violationsCount) {
		assert !locked;
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
		assert !locked;
		this.sequence = sequence;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof VoyagePlan) {
			final VoyagePlan plan = (VoyagePlan) obj;

			// Ensure all fields are present here
			// @formatter:off
			return Objects.equal(lngFuelVolume, plan.lngFuelVolume)
					&& Objects.equal(ignoreEnd, plan.ignoreEnd)
					&& Objects.equal(charterInRatePerDay, plan.charterInRatePerDay)
					&& Objects.equal(lngFuelCost, plan.lngFuelCost)
					&& Objects.equal(cooldownCost, plan.cooldownCost)
					&& Objects.equal(baseFuelCost, plan.baseFuelCost)
					&& Objects.equal(violationsCount, plan.violationsCount)
					&& Objects.equal(startingHeelInM3, plan.startingHeelInM3)
					&& Objects.equal(totalRouteCost, plan.totalRouteCost)
					&& Objects.equal(remainingHeelInM3, plan.remainingHeelInM3)
					&& Objects.equal(startHeelCost, plan.startHeelCost)
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
		return MoreObjects.toStringHelper(VoyagePlan.class) //
				.add("sequence", Arrays.toString(sequence)) //
				.add("charterInRatePerDay", charterInRatePerDay) //
				.add("totalRouteCost", totalRouteCost) //
				.add("lngFuelVolume", lngFuelVolume) //
				.add("lngFuelCost", lngFuelCost) //
				.add("cooldownCost", cooldownCost) //
				.add("baseFuelCost", baseFuelCost) //
				.add("violationsCount", violationsCount) //
				.add("startingHeelInM3", startingHeelInM3) //
				.add("remainingHeelInM3", remainingHeelInM3) //
				.add("ignoreEnd", ignoreEnd) //
				.toString();
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
		return new VoyagePlan(clonedSequence, charterInRatePerDay, lngFuelVolume, violationsCount, ignoreEnd, startingHeelInM3, remainingHeelInM3);
	}

	/**
	 * Set the total quantity in M3 of LNG used for fuel in this voyageplan
	 * 
	 * @param lngConsumed
	 */
	public void setLNGFuelVolume(final long lngConsumed) {
		assert !locked;
		this.lngFuelVolume = lngConsumed;
	}

	/**
	 * @return the total quantity in M3 of LNG used for fuel in this plan.
	 */
	public final long getLNGFuelVolume() {
		return lngFuelVolume;
	}

	public void setTotalRouteCost(final long routeCost) {
		assert !locked;
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
		// assert !locked;
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
		assert !locked;
		this.remainingHeelInM3 = remainingHeelInM3;
	}

	public long getStartingHeelInM3() {
		return startingHeelInM3;
	}

	public void setStartingHeelInM3(final long startingHeelInM3) {
		assert !locked;
		this.startingHeelInM3 = startingHeelInM3;
	}

	public long getCharterInRatePerDay() {
		return charterInRatePerDay;
	}

	public void setCharterInRatePerDay(final long charterInRatePerDay) {
		assert !locked;
		this.charterInRatePerDay = charterInRatePerDay;
	}

	public void setStartHeelCost(long startHeelCost) {
		assert !locked;
		this.startHeelCost = startHeelCost;
	}

	/**
	 * Return the cost for *just* the start heel of the plan. There may be other heel costs or revenue contained within the plan.
	 * 
	 * @return
	 */
	public long getStartHeelCost() {
		return startHeelCost;
	}

	public boolean isCacheLocked() {
		return locked;
	}

	public void setCacheLocked(boolean locked) {
		assert !this.locked;
		this.locked = locked;
	}
}
