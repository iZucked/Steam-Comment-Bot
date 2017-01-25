/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.actionset;

import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * The current state of a particular track of search in the LSO.
 * 
 * @author Alex
 *
 */
public class ActionSetJobState {
	public enum Status {
		Fail, Pass
	}

	private List<ISequenceElement> usedElements = new LinkedList<ISequenceElement>();
	public List<ISequenceElement> getUsedElements() {
		return usedElements;
	}

	public void addUsedElements(ISequenceElement e) {
		this.usedElements.add(e);
	}

	private transient ISequences rawSequences = null;
	// private transient ISequences fullSequences = null;
	// private long fitness;
	private long seed;
	// private IEvaluationState evaluationState;
	private Status status;

	private long[] metrics;

	public long[] getMetrics() {
		return metrics;
	}

	public Status getStatus() {
		return status;
	}

	private String note;
	private ActionSetJobState parent;
	private long fitness;

	public long getFitness() {
		return fitness;
	}

	public ActionSetJobState(final ISequences rawSequences, long fitness, long[] metrics, Status status, long seed, String note, ActionSetJobState parent) {
		this.rawSequences = rawSequences;
		this.fitness = fitness;
		this.metrics = metrics;
		// this.fullSequences = fullSequences;
		// this.fitness = fitness;
		this.status = status;
		this.seed = seed;
		this.note = note;
		// this.setNote(note);
		// this.setSeed(seed);
		this.parent = parent;
	}
	// public ActionSetJobState(final ISequences rawSequences, final ISequences fullSequences, long fitness, Status status, IEvaluationState evaluationState, long seed, String note) {
	// this.rawSequences = rawSequences;
	// this.fullSequences = fullSequences;
	// this.fitness = fitness;
	// this.status = status;
	// this.setNote(note);
	// this.setSeed(seed);
	// }

	public ISequences getRawSequences() {
		return rawSequences;
	}

	// public void setRawSequences(ISequences rawSequences) {
	// this.rawSequences = rawSequences;
	// }

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public long getSeed() {
		return seed;
	}

	public ActionSetJobState getParent() {
		return parent;
	}

	// public void setSeed(long seed) {
	// this.seed = seed;
	// }

	@Override
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}
		if (obj instanceof ActionSetJobState) {
			ActionSetJobState other = (ActionSetJobState) obj;
			return rawSequences.equals(other.rawSequences);

		}

		return false;
	}
}