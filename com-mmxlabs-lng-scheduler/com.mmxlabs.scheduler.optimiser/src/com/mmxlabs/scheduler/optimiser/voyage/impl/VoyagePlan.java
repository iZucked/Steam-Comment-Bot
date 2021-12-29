/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.Arrays;
import java.util.Comparator;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.mmxlabs.scheduler.optimiser.cache.AbstractWriteLockable;

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
public final class VoyagePlan extends AbstractWriteLockable {

	public enum VoyagePlanMetrics {
		VOLUME_VIOLATION_COUNT, // Number of load, discharge or heel violations
		VOLUME_VIOLATION_QUANTITY, // Quantity over or under of the load or discharge violations
		COOLDOWN_COUNT // Number of cooldown violations
	}

	private static Comparator<long[]> SimpleMetricComparator = (a, b) -> {

		long aW = a[VoyagePlanMetrics.VOLUME_VIOLATION_COUNT.ordinal()] * 2 + a[VoyagePlanMetrics.COOLDOWN_COUNT.ordinal()];
		long bW = b[VoyagePlanMetrics.VOLUME_VIOLATION_COUNT.ordinal()] * 2 + b[VoyagePlanMetrics.COOLDOWN_COUNT.ordinal()];

		return Long.compare(aW, bW);
	};

	private static Comparator<long[]> VolumeBasedMetricComparator = (a, b) -> {

		int c = Long.compare(a[VoyagePlanMetrics.VOLUME_VIOLATION_COUNT.ordinal()], b[VoyagePlanMetrics.VOLUME_VIOLATION_COUNT.ordinal()]);
		if (c == 0) {
			c = Long.compare(a[VoyagePlanMetrics.COOLDOWN_COUNT.ordinal()], b[VoyagePlanMetrics.COOLDOWN_COUNT.ordinal()]);
		}
		if (c == 0) {
			c = Long.compare(a[VoyagePlanMetrics.VOLUME_VIOLATION_QUANTITY.ordinal()], b[VoyagePlanMetrics.VOLUME_VIOLATION_QUANTITY.ordinal()]);
		}
		return c;
	};

	public static Comparator<long[]> MetricComparator = SimpleMetricComparator;

	private IDetailsSequenceElement[] sequence;
	private long lngFuelVolume;
	private long lngFuelCost;
	private long cooldownCost;
	private long baseFuelCost;
	private long totalRouteCost;
	private long startHeelCost;

	private long[] voyagePlanMetrics = new long[VoyagePlanMetrics.values().length];
	private boolean ignoreEnd;

	private long startingHeelInM3;
	private long remainingHeelInM3;

	public VoyagePlan() {
		ignoreEnd = true;
	}

	protected VoyagePlan(final IDetailsSequenceElement[] sequence, final long fuelVolume, final long[] voyagePlanMetrics, final boolean ignoreEnd, final long startingHeelInM3,
			final long remainingHeelInM3) {
		super();
		this.sequence = sequence;
		this.lngFuelVolume = fuelVolume;
		this.voyagePlanMetrics = Arrays.copyOf(voyagePlanMetrics, voyagePlanMetrics.length);
		this.ignoreEnd = ignoreEnd;
		this.startingHeelInM3 = startingHeelInM3;
		this.remainingHeelInM3 = remainingHeelInM3;
	}

	public long getCharterCost() {
		long charterCost = 0;
		final Object[] sequence = this.getSequence();
		final int offset = this.isIgnoreEnd() ? 1 : 0;
		final int k = sequence.length - offset;
		for (int i = 0; i < k; i++) {
			final Object o = sequence[i];
			if (o instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) o;
				charterCost += voyageDetails.getTravelCharterCost();
				charterCost += voyageDetails.getIdleCharterCost();
				charterCost += voyageDetails.getPurgeCharterCost();
			} else {
				charterCost += ((PortDetails) o).getCharterCost();
			}
		}
		return charterCost;
	}

	public long getLngFuelCost() {
		return lngFuelCost;
	}

	public void setLngFuelCost(final long lngFuelCost) {
		checkWritable();

		this.lngFuelCost = lngFuelCost;
	}

	public long getCooldownCost() {
		return cooldownCost;
	}

	public void setCooldownCost(final long cooldownCost) {
		checkWritable();
		this.cooldownCost = cooldownCost;
	}

	public long getBaseFuelCost() {
		return baseFuelCost;
	}

	public void setBaseFuelCost(final long baseFuelCost) {
		checkWritable();
		this.baseFuelCost = baseFuelCost;
	}

	/**
	 */
	public final long[] getVoyagePlanMetrics() {
		return voyagePlanMetrics;
	}

	public final long getWeightedVoyagePlanMetrics() {
		// Note: This was the LNGVoyageCalculator weighting
		return getVoyagePlanMetrics()[VoyagePlan.VoyagePlanMetrics.VOLUME_VIOLATION_COUNT.ordinal()] * 2 + getVoyagePlanMetrics()[VoyagePlan.VoyagePlanMetrics.COOLDOWN_COUNT.ordinal()];
	}

	/**
	 */
	public final void setVoyagePlanMetrics(final long[] voyagePlanMetrics) {
		checkWritable();
		this.voyagePlanMetrics = voyagePlanMetrics;
	}

	/**
	 */
	public final IDetailsSequenceElement[] getSequence() {
		return sequence;
	}

	/**
	 */
	public final void setSequence(final IDetailsSequenceElement[] sequence) {
		checkWritable();
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
					&& Objects.equal(lngFuelCost, plan.lngFuelCost)
					&& Objects.equal(cooldownCost, plan.cooldownCost)
					&& Objects.equal(baseFuelCost, plan.baseFuelCost)
					&& Objects.equal(startingHeelInM3, plan.startingHeelInM3)
					&& Objects.equal(totalRouteCost, plan.totalRouteCost)
					&& Objects.equal(remainingHeelInM3, plan.remainingHeelInM3)
					&& Objects.equal(startHeelCost, plan.startHeelCost)
					&& Arrays.equals(voyagePlanMetrics, plan.voyagePlanMetrics)
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
				.add("totalRouteCost", totalRouteCost) //
				.add("lngFuelVolume", lngFuelVolume) //
				.add("lngFuelCost", lngFuelCost) //
				.add("cooldownCost", cooldownCost) //
				.add("baseFuelCost", baseFuelCost) //
				.add("voyagePlanMetrics", voyagePlanMetrics) //
				.add("startingHeelInM3", startingHeelInM3) //
				.add("remainingHeelInM3", remainingHeelInM3) //
				.add("ignoreEnd", ignoreEnd) //
				.toString();
	}

	public final VoyagePlan copy() {
		final IDetailsSequenceElement[] clonedSequence = new IDetailsSequenceElement[sequence.length];
		int k = 0;
		for (final IDetailsSequenceElement o : sequence) {
			if (o instanceof VoyageDetails) {
				clonedSequence[k++] = ((VoyageDetails) o).copy();
			} else if (o instanceof PortDetails) {
				clonedSequence[k++] = ((PortDetails) o).copy();
			} else {
				clonedSequence[k++] = o;
			}
		}
		return new VoyagePlan(clonedSequence, lngFuelVolume, voyagePlanMetrics, ignoreEnd, startingHeelInM3, remainingHeelInM3);
	}

	/**
	 * Set the total quantity in M3 of LNG used for fuel in this voyageplan
	 * 
	 * @param lngConsumed
	 */
	public void setLNGFuelVolume(final long lngConsumed) {
		checkWritable();
		this.lngFuelVolume = lngConsumed;
	}

	/**
	 * @return the total quantity in M3 of LNG used for fuel in this plan.
	 */
	public final long getLNGFuelVolume() {
		return lngFuelVolume;
	}

	public void setTotalRouteCost(final long routeCost) {
		checkWritable();
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
		checkWritable();
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
		checkWritable();
		this.remainingHeelInM3 = remainingHeelInM3;
	}

	public long getStartingHeelInM3() {
		return startingHeelInM3;
	}

	public void setStartingHeelInM3(final long startingHeelInM3) {
		checkWritable();
		this.startingHeelInM3 = startingHeelInM3;
	}

	public void setStartHeelCost(long startHeelCost) {
		checkWritable();
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
}
