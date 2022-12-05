/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.util;

public enum MetricType {
	PNL, LATENESS, CAPACITY, COMPULSARY_SLOT;

	public static boolean betterThan(final long[] toCheck, final long[] currentBest) {

		if (toCheck[MetricType.LATENESS.ordinal()] < currentBest[MetricType.LATENESS.ordinal()]) {
			return true;
		} else if (toCheck[MetricType.LATENESS.ordinal()] == currentBest[MetricType.LATENESS.ordinal()]) {
			if (toCheck[MetricType.CAPACITY.ordinal()] < currentBest[MetricType.CAPACITY.ordinal()]) {
				return true;
			} else if (toCheck[MetricType.CAPACITY.ordinal()] == currentBest[MetricType.CAPACITY.ordinal()]) {
				if (toCheck[MetricType.PNL.ordinal()] > currentBest[MetricType.PNL.ordinal()]) {
					return true;
				}
			}
		}
		return false;
	}

	public static void increment(long[] metrics, MetricType type, long value) {
		metrics[type.ordinal()] = Math.addExact(metrics[type.ordinal()], value);
	}
}
