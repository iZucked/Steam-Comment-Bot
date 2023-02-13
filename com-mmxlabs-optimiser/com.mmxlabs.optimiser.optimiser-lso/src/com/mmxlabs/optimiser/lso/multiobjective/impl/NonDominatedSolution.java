/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.multiobjective.impl;

import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.ISequences;

public class NonDominatedSolution {
	private ISequences sequences = null;
	private long[] fitnesses = null;
	private ILookupManager manager = null;
	
	public NonDominatedSolution(ISequences sequences, long[] fitnesses, ILookupManager manager) {
		this.setSequences(sequences);
		this.setFitnesses(fitnesses);
		this.setManager(manager);
	}

	public ISequences getSequences() {
		return sequences;
	}

	public void setSequences(ISequences sequences) {
		this.sequences = sequences;
	}

	public long[] getFitnesses() {
		return fitnesses;
	}

	public void setFitnesses(long[] fitnesses) {
		this.fitnesses = fitnesses;
	}

	public ILookupManager getManager() {
		return manager;
	}

	public void setManager(ILookupManager manager) {
		this.manager = manager;
	}
}
