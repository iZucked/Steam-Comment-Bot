/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.tabu;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TabuDebugHelper {
	public static List<TabuSolution> findMatchingSolutions(List<TabuSolution> tabuSolutions, int vessel, List<Integer> desiredSequence) {
		return tabuSolutions.stream().filter(s -> {
			if (s.schedule.get(vessel).size() != desiredSequence.size()) {
				return false;
			}
			for (int i = 0; i < desiredSequence.size(); i++) {
				if (!s.schedule.get(vessel).get(i).equals(desiredSequence.get(i))) {
					return false;
				}
			}
			return true;
		}).collect(Collectors.toList());
	}

	public static List<TabuSolution> findMatchingSolutionsSet(List<TabuSolution> tabuSolutions, Set<Integer> desiredSequence) {
		return tabuSolutions.stream().filter(s -> {
			Set<Integer> collect = s.schedule.stream().flatMap(Collection::stream).collect(Collectors.toSet());
			if (collect.containsAll(desiredSequence)) {
				return true;
			}
			return false;
		}).collect(Collectors.toList());
	}

}
