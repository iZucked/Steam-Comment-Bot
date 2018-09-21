package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.tabu;

import java.util.List;

public class TabuSolution {
	public List<List<Integer>> schedule;
	public List<Integer> tabu;

	public TabuSolution(List<List<Integer>> schedule, List<Integer> tabu) {
		this.schedule = schedule;
		this.tabu = tabu;
	}

}
