/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsConstraintChecker;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintChecker;

public interface ICargoVesselRestrictionsMatrixProducer {
	Map<List<IPortSlot>, List<IVesselAvailability>> getCargoVesselRestrictions(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, ResourceAllocationConstraintChecker resourceAllocationChecker, PortExclusionConstraintChecker portExclusionConstraintChecker);
	ArrayList<Set<Integer>> getIntegerCargoVesselRestrictions(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, Map<List<IPortSlot>, List<IVesselAvailability>> restrictions);
	ArrayList<Set<Integer>> getIntegerCargoVesselRestrictions(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, ResourceAllocationConstraintChecker resourceAllocationChecker, PortExclusionConstraintChecker portExclusionConstraintChecker);
}
