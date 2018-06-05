/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm;

import java.util.List;
import java.util.Map;

public interface ILongTermMatrixOptimiser {

	/**
	 * Finds optimal pairings of slots given a value array
	 * @param values
	 * @param minDischargeSlotsConstraints TODO
	 * @param maxLoadSlotsConstraints TODO
	 * @param minLoadSlotsConstraints TODO
	 * @return
	 */
	boolean[][] findOptimalPairings(long[][] values, boolean[] optionalLoads, boolean[] optionalDischarges, boolean[][] valid,
			List<Map<String, List<Integer>>> maxDischargeSlotsConstraints, List<Map<String, List<Integer>>> minDischargeSlotsConstraints, List<Map<String, List<Integer>>> maxLoadSlotsConstraints, List<Map<String, List<Integer>>> minLoadSlotsConstraints);
}
