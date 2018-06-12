/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsConstraintChecker;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class CargoVesselRestrictionsMatrixProducer implements ICargoVesselRestrictionsMatrixProducer {

	@Inject
	private IVesselProvider vesselProvider;
	
	@Inject
	private IPortSlotProvider portSlotProvider;
	@Override
	public Map<List<IPortSlot>, List<IVesselAvailability>> getCargoVesselRestrictions(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, ResourceAllocationConstraintChecker resourceAllocationChecker, PortExclusionConstraintChecker portExclusionConstraintChecker) {
		Map<List<IPortSlot>, List<IVesselAvailability>> cargoMap = new HashMap<>();
		for (List<IPortSlot> cargo : cargoes) {
			List<IVesselAvailability> restrictedVessels = new LinkedList<>();
			for (IVesselAvailability vessel : vessels) {
				boolean valid = true;
				for(IPortSlot slot : cargo) {
					if (!resourceAllocationChecker.checkElement(portSlotProvider.getElement(slot), vesselProvider.getResource(vessel))) {
						valid = false;
						break;
					}
				}
				if (cargo.size() == 2) {
					if (!portExclusionConstraintChecker.checkPairwiseConstraint(portSlotProvider.getElement(cargo.get(0)), portSlotProvider.getElement(cargo.get(1)), vesselProvider.getResource(vessel))) {
						valid = false;
					}
				}
				if (!valid) {
					restrictedVessels.add(vessel);
				}
			}

			cargoMap.put(cargo, restrictedVessels);
		}
		return cargoMap;
	}

	@Override
	public ArrayList<Set<Integer>> getIntegerCargoVesselRestrictions(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, Map<List<IPortSlot>, List<IVesselAvailability>> restrictions) {
		Map<IVesselAvailability, Integer> vesselMap = new HashMap<>();
		for (int i = 0; i < vessels.size(); i++) {
			vesselMap.put(vessels.get(i), i);
		}
		
		ArrayList<Set<Integer>> cargoMap = new ArrayList<>();
		for (List<IPortSlot> cargo : cargoes) {
			Set<Integer> set = new LinkedHashSet<Integer>(vessels.size());
//			for (IPortSlot slot : cargo) {
//			set.addAll(restrictions.get(cargo).stream().map(v -> vesselMap.get(v)).collect(Collectors.toList()));
			set = restrictions.get(cargo).stream().map(v -> vesselMap.get(v)).collect(Collectors.toCollection(LinkedHashSet::new));
//			}
			cargoMap.add(set);
		}
		return cargoMap;
	}

	@Override
	public ArrayList<Set<Integer>> getIntegerCargoVesselRestrictions(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, ResourceAllocationConstraintChecker resourceAllocationConstraintChecker, PortExclusionConstraintChecker portExclusionConstraintChecker) {
		return getIntegerCargoVesselRestrictions(cargoes, vessels, getCargoVesselRestrictions(cargoes, vessels, resourceAllocationConstraintChecker, portExclusionConstraintChecker));
	}

}
