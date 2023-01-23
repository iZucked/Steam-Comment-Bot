/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.tabu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargoMap {
	public static class CargoCoordinates {
		int vessel;
		int position;
		
		public CargoCoordinates(int vessel, int position) {
			this.vessel = vessel;
			this.position = position;
		}
	}
	
	Map<Integer, CargoCoordinates> mapping = new HashMap<>();
	
	public CargoMap(List<List<Integer>> solution) {
		setMapping(solution);
	}
	
	public CargoCoordinates getCoordinates(int cargo) {
		return mapping.get(cargo);
	}
	
	void setMapping(List<List<Integer>> solution) {
		mapping.clear();
		for (List<Integer> vessel : solution) {
			for (Integer cargo : vessel) {
				mapping.put(cargo, new CargoCoordinates(solution.indexOf(vessel), vessel.indexOf(cargo)));
			}
		}
	}

}
