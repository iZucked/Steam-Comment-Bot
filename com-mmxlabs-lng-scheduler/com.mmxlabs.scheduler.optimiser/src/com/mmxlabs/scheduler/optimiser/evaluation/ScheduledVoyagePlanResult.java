/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import com.mmxlabs.scheduler.optimiser.moves.util.MetricType;

public class ScheduledVoyagePlanResult {
	// Information needed to pass into the next voyage plan
	public int lastHeelPricePerMMBTU = 0;
	public int lastCV = 0;
	public long lastHeelVolumeInM3;
	public boolean forcedCooldown;

	// Actual times used
	public List<Integer> arrivalTimes;
	public int returnTime;

	// Metrics, e.g. P&L
	public long[] metrics;

	// The internal data calculated.
	public List<VoyagePlanRecord> voyagePlans = new LinkedList<>();

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
		if (forcedCooldown != other.forcedCooldown) {
			return false;
		}
		if (lastHeelPricePerMMBTU != other.lastHeelPricePerMMBTU) {
			return false;
		}
		if (lastCV != other.lastCV) {
			return false;
		}
		if (lastHeelVolumeInM3 != other.lastHeelVolumeInM3) {
			return false;
		}
		if (returnTime != other.returnTime) {
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
			if (voyagePlans.size() != other.voyagePlans.size()) {
				//
				int ii = 0;
			}
			int ii = 0;

			voyagePlans.equals(other.voyagePlans);
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