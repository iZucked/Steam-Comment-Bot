/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.tabu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TabuSolution {
	public List<List<Integer>> schedule;
	public List<Integer> tabu;
	long fitness;

	public TabuSolution(List<List<Integer>> schedule, List<Integer> tabu) {
		this.schedule = schedule;
		this.tabu = tabu;
		this.fitness = Long.MIN_VALUE;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TabuSolution) {
			TabuSolution other = (TabuSolution) obj;
			if (this.fitness != other.fitness) {
				return false;
			}
			// same size
			if (schedule.size() == other.schedule.size()) {
				for (int sequenceIndex = 0; sequenceIndex < schedule.size(); sequenceIndex++) {
					if (this.schedule.get(sequenceIndex).size() != other.schedule.get(sequenceIndex).size()) {
						return false;
					}
				}
				for (int sequenceIndex = 0; sequenceIndex < schedule.size(); sequenceIndex++) {
					for (int elementIndex = 0; elementIndex < this.schedule.get(sequenceIndex).size(); elementIndex++) {
						if (this.schedule.get(sequenceIndex).get(elementIndex) !=
								other.schedule.get(sequenceIndex).get(elementIndex)) {
							return false;
						}
					}
				}
			} else {
				return false;
			}
		}
		return true;
	}
	
	public TabuSolution copy() {
		TabuSolution copy = new TabuSolution(
				schedule.stream()
				.map(s->new ArrayList<>(s))
				.collect(Collectors.toCollection(ArrayList::new))
				, new ArrayList<>(tabu));
		copy.fitness = this.fitness;
		return copy;
	}
}
