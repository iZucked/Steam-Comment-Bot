/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.pairing;

import java.util.List;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.providers.ConstraintInfo;

public interface IPairingMatrixOptimiser<P,C> {

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


	/**
	 * Find the minimum number of violated constraints.
	 * @param optimiserRecorder 
	 * @param values
	 * @param optionalLoads
	 * @param optionalDischarges
	 * @param valid
	 * @param maxDischargeSlotsConstraints
	 * @param minDischargeSlotsConstraints
	 * @param maxLoadSlotsConstraints
	 * @param minLoadSlotsConstraints
	 * @return a list of ConstraintInfo objects with details of constraints which are violated.
	 */
	List<ConstraintInfo<P, C, ?>> findMinViolatedConstraints(PairingOptimisationData<?, ?> optimiserRecorder, long[][] values, boolean[] optionalLoads, boolean[] optionalDischarges, boolean[][] valid,
			List<ConstraintInfo<P,C,IDischargeOption>> maxDischargeSlotsConstraints, List<ConstraintInfo<P,C,IDischargeOption>> minDischargeSlotsConstraints, 
			List<ConstraintInfo<P,C,ILoadOption>> maxLoadSlotsConstraints, List<ConstraintInfo<P,C,ILoadOption>> minLoadSlotsConstraints);
}
