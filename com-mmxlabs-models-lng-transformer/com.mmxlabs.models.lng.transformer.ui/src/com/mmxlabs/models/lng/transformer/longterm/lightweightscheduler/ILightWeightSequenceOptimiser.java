/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

/**
 * Optimiser for lightweight cargo scheduling
 * @author alex
 *
 */
public interface ILightWeightSequenceOptimiser {
	List<List<Integer>> optimise(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, long[] cargoPNL, Long[][][] cargoToCargoCostsOnAvailability, ArrayList<Set<Integer>> cargoVesselRestrictions, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes);
}
