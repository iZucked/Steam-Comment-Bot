/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface ICargoVesselRestrictionsMatrixProducer {
	Map<List<IPortSlot>, List<IVesselAvailability>> getCargoVesselRestrictions(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels);
	ArrayList<Set<Integer>> getIntegerCargoVesselRestrictions(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, Map<List<IPortSlot>, List<IVesselAvailability>> restrictions);
	ArrayList<Set<Integer>> getIntegerCargoVesselRestrictions(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels);
}
