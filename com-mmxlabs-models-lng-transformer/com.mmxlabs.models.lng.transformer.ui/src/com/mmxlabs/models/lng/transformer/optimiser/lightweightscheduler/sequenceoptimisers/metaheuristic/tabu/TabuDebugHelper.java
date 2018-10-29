/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.tabu;

import java.util.List;
import java.util.stream.Collectors;

public class TabuDebugHelper {
	public static List<TabuSolution> findMatchingSolutions(List<TabuSolution> tabuSolutions, int vessel, List<Integer> desiredSequence) {
		return tabuSolutions.stream().filter(s-> {
			if (s.schedule.get(vessel).size() != desiredSequence.size()) {
				return false;
			}
			for (int i = 0; i < desiredSequence.size(); i++) {
				if (s.schedule.get(vessel).get(i) != desiredSequence.get(i)) {
					return false;
				}
			}
			return true;
		}).collect(Collectors.toList());
	}
}
