/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.pairing;

import java.util.List;
import java.util.Map;

public interface IPairingMatrixOptimiser {

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
