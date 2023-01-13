/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.Comparator;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.evaluation.PreviousHeelRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.ScheduledVoyagePlanResult;
import com.mmxlabs.scheduler.optimiser.moves.util.MetricType;

public final class ScheduledRecord {

	public final int sequenceStartTime;
	public final int currentEndTime;
	public final int maxDuration;
	public final int minDuration;

	public final long[] metrics = new long[MetricType.values().length];
	public final ScheduledVoyagePlanResult head;
	public final @NonNull ScheduledVoyagePlanResult result;

	public final @Nullable ScheduledRecord previous;
	public final PreviousHeelRecord previousHeelRecord;

	// These are not final for unit test
	public int maxDurationLateness;
	public int minDurationLateness;

	public ScheduledRecord(final @NonNull ScheduledVoyagePlanResult head, final PreviousHeelRecord previousHeelRecord, final int minDuration, final int maxDuration) {
		this(head, previousHeelRecord, null, minDuration, maxDuration);
	}

	public ScheduledRecord(final @NonNull ScheduledVoyagePlanResult tail, final PreviousHeelRecord previousHeelRecord, final ScheduledRecord previous) {
		this(tail, previousHeelRecord, previous, previous.minDuration, previous.maxDuration);
	}

	private ScheduledRecord(final @NonNull ScheduledVoyagePlanResult tail, final PreviousHeelRecord previousHeelRecord, @Nullable final ScheduledRecord previous, final int minDuration,
			final int maxDuration) {
		this.previousHeelRecord = previousHeelRecord;
		this.previous = previous;
		this.result = tail;
		this.maxDuration = maxDuration;
		this.minDuration = minDuration;

		if (previous == null) {
			this.head = tail;
			this.sequenceStartTime = result.arrivalTimes.get(0);
		} else {
			assert previous.currentEndTime == result.arrivalTimes.get(0);
			this.head = previous.head;
			this.sequenceStartTime = previous.sequenceStartTime;

		}

		for (final MetricType t : MetricType.values()) {
			metrics[t.ordinal()] += result.metrics[t.ordinal()];

			// Look back over the last states and accumulate metrics
			ScheduledRecord p = previous;
			while (p != null) {
				metrics[t.ordinal()] += p.result.metrics[t.ordinal()];
				p = p.previous;
			}
		}
		this.currentEndTime = result.returnTime;

		this.maxDurationLateness = maxDuration == Integer.MAX_VALUE ? 0 : Math.max(0, (this.currentEndTime - this.sequenceStartTime) - maxDuration);
		this.minDurationLateness = minDuration == 0 ? 0 : Math.max(0, minDuration - (this.currentEndTime - this.sequenceStartTime));
	}

	public static Comparator<ScheduledRecord> getComparator(final boolean withMinDuration) {
		return (a, b) -> {
			int c = withMinDuration ? Long.compare(a.minDurationLateness, b.minDurationLateness) : 0;
			if (c == 0) {
				c = Long.compare(a.maxDurationLateness, b.maxDurationLateness);
			}
			if (c == 0) {
				c = Long.compare(a.metrics[MetricType.LATENESS.ordinal()], b.metrics[MetricType.LATENESS.ordinal()]);
			}
			if (c == 0) {
				c = Long.compare(a.metrics[MetricType.CAPACITY.ordinal()], b.metrics[MetricType.CAPACITY.ordinal()]);
			}
			if (c == 0) {
				c = -Long.compare(a.metrics[MetricType.PNL.ordinal()], b.metrics[MetricType.PNL.ordinal()]);
			}
			return c;
		};
	}

	public boolean betterThan(@Nullable final ScheduledRecord other, final boolean withMinDuration) {
		if (other == null) {
			return true;
		} else {
			boolean minOk = true;
			if (withMinDuration) {
				if (this.minDurationLateness < other.maxDurationLateness) {
					return true;
				} else {
					minOk = this.minDurationLateness == other.maxDurationLateness;

				}
			}
			if (minOk) {
				if (this.maxDurationLateness < other.maxDurationLateness) {
					return true;
				} else if (this.maxDurationLateness == other.maxDurationLateness) {
					if (this.metrics[MetricType.LATENESS.ordinal()] < other.metrics[MetricType.LATENESS.ordinal()]) {
						return true;
					} else if (this.metrics[MetricType.LATENESS.ordinal()] == other.metrics[MetricType.LATENESS.ordinal()]) {
						if (this.metrics[MetricType.CAPACITY.ordinal()] < other.metrics[MetricType.CAPACITY.ordinal()]) {
							return true;
						} else if (this.metrics[MetricType.CAPACITY.ordinal()] == other.metrics[MetricType.CAPACITY.ordinal()]) {
							if (this.metrics[MetricType.PNL.ordinal()] > other.metrics[MetricType.PNL.ordinal()]) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
}