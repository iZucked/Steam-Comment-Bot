/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

public interface ICargoVesselRestrictionsMatrixProducer {
	Map<List<IPortSlot>, List<IVesselCharter>> getCargoVesselRestrictions(List<List<IPortSlot>> cargoes, List<IVesselCharter> vessels);
	ArrayList<Set<Integer>> getIntegerCargoVesselRestrictions(List<List<IPortSlot>> cargoes, List<IVesselCharter> vessels, Map<List<IPortSlot>, List<IVesselCharter>> restrictions);
	ArrayList<Set<Integer>> getIntegerCargoVesselRestrictions(List<List<IPortSlot>> cargoes, List<IVesselCharter> vessels);
}
