/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.dynamic;

import java.util.HashMap;
import java.util.Map;

public class SolutionState {

	private Map<String, Long> fitnesses = new HashMap<>();

	public Map<String, Long> getFitnesses() {
		return fitnesses;
	}

	public void setFitnesses(Map<String, Long> fitnesses) {
		this.fitnesses = fitnesses;
	}

	public void addFitnesses(String fitness, long value) {
		this.fitnesses.put(fitness, value);
	}

	public boolean isEquivalent(SolutionState other) {
		if (other == null) {
			return false;
		}
		return this.fitnesses.equals(other.fitnesses);
	}
}