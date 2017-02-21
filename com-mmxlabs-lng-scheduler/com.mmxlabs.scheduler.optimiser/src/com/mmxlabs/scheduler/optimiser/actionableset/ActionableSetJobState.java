/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.actionableset;

import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;

public class ActionableSetJobState {
	public enum Status {
		Fail, Pass
	}

	private final List<ISequenceElement> usedElements = new LinkedList<ISequenceElement>();

	public List<ISequenceElement> getUsedElements() {
		return usedElements;
	}

	public void addUsedElements(final ISequenceElement e) {
		this.usedElements.add(e);
	}

	private transient ISequences rawSequences = null;
	private final long seed;
	private final Status status;

	private final long[] metrics;

	public long[] getMetrics() {
		return metrics;
	}

	public Status getStatus() {
		return status;
	}

	private String note;
	private final ActionableSetJobState parent;
	private final long fitness;

	public long getFitness() {
		return fitness;
	}

	public ActionableSetJobState(final ISequences rawSequences, final long fitness, final long[] metrics, final Status status, final long seed, final String note, final ActionableSetJobState parent) {
		this.rawSequences = rawSequences;
		this.fitness = fitness;
		this.metrics = metrics;
		this.status = status;
		this.seed = seed;
		this.note = note;
		this.parent = parent;
	}

	public ISequences getRawSequences() {
		return rawSequences;
	}

	public String getNote() {
		return note;
	}

	public void setNote(final String note) {
		this.note = note;
	}

	public long getSeed() {
		return seed;
	}

	public ActionableSetJobState getParent() {
		return parent;
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj == this) {
			return true;
		}
		if (obj instanceof ActionableSetJobState) {
			final ActionableSetJobState other = (ActionableSetJobState) obj;
			return rawSequences.equals(other.rawSequences);
		}

		return false;
	}
}