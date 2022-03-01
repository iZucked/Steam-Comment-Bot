package com.mmxlabs.models.lng.adp.rateability.spacing.containers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.IntervalVar;

@NonNullByDefault
abstract class ShippedElementContainer {
	protected final IntVar start;
	protected final IntVar end;
	protected final IntVar durationVar;
	protected final IntervalVar interval;

	protected ShippedElementContainer(final IntVar start, final IntVar end, final IntVar durationVar, final IntervalVar interval) {
		this.start = start;
		this.end = end;
		this.durationVar = durationVar;
		this.interval = interval;
	}

	public IntVar getStart() {
		return this.start;
	}

	public IntVar getEnd() {
		return this.end;
	}

	public IntVar getDurationVar() {
		return this.durationVar;
	}

	public IntervalVar getInterval() {
		return this.interval;
	}
}
