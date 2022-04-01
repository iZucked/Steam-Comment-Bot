/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.scheduler.optimiser.moves.util.MetricType;

public final class ScheduledVoyagePlanResult {
	// Information needed to pass into the next voyage plan
	public final PreviousHeelRecord endHeelState;

	// Actual times used
	public final ImmutableList<Integer> arrivalTimes;
	public final int returnTime;

	// Metrics, e.g. P&L
	public final long[] metrics;

	// The internal data calculated.
	public final ImmutableList<VoyagePlanRecord> voyagePlans;

	public ScheduledVoyagePlanResult(@NonNull ImmutableList<@NonNull Integer> arrivalTimes,
			int returnTime, @NonNull ImmutableList<VoyagePlanRecord> voyagePlans, long[] metrics,
			@NonNull PreviousHeelRecord endHeelState) {
		this.arrivalTimes = arrivalTimes;
		this.returnTime = returnTime;
		this.voyagePlans = voyagePlans;
		this.metrics = metrics;
		this.endHeelState = endHeelState;
	}

	/**
	 * Equals method for cache test equality. Avoiding overriding {@link #equals(Object)} unless we really need it.
	 * 
	 * @param other
	 * @return
	 */
	public boolean isEqual(ScheduledVoyagePlanResult other) {

		if (this == other) {
			return true;
		}

		// Simple fields
		if (returnTime != other.returnTime) {
			return false;
		}

		if (!endHeelState.equals(other.endHeelState)) {
			return false;
		}
		// Arrays
		if (!Arrays.equals(metrics, other.metrics)) {
			return false;
		}
		if (!arrivalTimes.equals(other.arrivalTimes)) {
			return false;
		}

		// Complex data
		if (!voyagePlans.equals(other.voyagePlans)) {
			return false;
		}

		return true;
	}

	public static int compareTo(ScheduledVoyagePlanResult a, ScheduledVoyagePlanResult b) {

		int c = Long.compare(a.metrics[MetricType.LATENESS.ordinal()], b.metrics[MetricType.LATENESS.ordinal()]);
		if (c == 0) {
			c = Long.compare(a.metrics[MetricType.CAPACITY.ordinal()], b.metrics[MetricType.CAPACITY.ordinal()]);
		}
		if (c == 0) {
			c = -Long.compare(a.metrics[MetricType.PNL.ordinal()], b.metrics[MetricType.PNL.ordinal()]);
		}
		return c;
	}

}