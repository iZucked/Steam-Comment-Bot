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
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsConstraintChecker;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintChecker;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
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

	private LinkedList<IConstraintChecker> constraintCheckers;
	
	@Inject
	public void injectConstraintChecker(@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) final ISequences initialRawSequences, final List<IConstraintChecker> injectedConstraintCheckers) {
		this.constraintCheckers = new LinkedList<>();
		for (final IConstraintChecker checker : injectedConstraintCheckers) {
			if (checker instanceof IPairwiseConstraintChecker) {
				final IPairwiseConstraintChecker constraintChecker = (IPairwiseConstraintChecker) checker;
				constraintCheckers.add(constraintChecker);

				// Prep with initial sequences.
				constraintChecker.checkConstraints(initialRawSequences, null);
			}
		}
	}

	@Override
	public Map<List<IPortSlot>, List<IVesselAvailability>> getCargoVesselRestrictions(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels) {
		ResourceAllocationConstraintChecker resourceAllocationConstraintChecker = getResourceAllocationConstraintChecker(constraintCheckers);
		PortExclusionConstraintChecker portExclusionConstraintChecker = getPortExclusionConstraintChecker(constraintCheckers);
		Map<List<IPortSlot>, List<IVesselAvailability>> cargoMap = new HashMap<>();
		for (List<IPortSlot> cargo : cargoes) {
			List<IVesselAvailability> restrictedVessels = new LinkedList<>();
			for (IVesselAvailability vessel : vessels) {
				boolean valid = true;
				for(IPortSlot slot : cargo) {
					if (resourceAllocationConstraintChecker != null && !resourceAllocationConstraintChecker.checkElement(portSlotProvider.getElement(slot), vesselProvider.getResource(vessel))) {
						valid = false;
						break;
					}
				}
				if (cargo.size() == 2) {
					if (portExclusionConstraintChecker != null && !portExclusionConstraintChecker.checkPairwiseConstraint(portSlotProvider.getElement(cargo.get(0)), portSlotProvider.getElement(cargo.get(1)), vesselProvider.getResource(vessel))) {
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
	
	private ResourceAllocationConstraintChecker getResourceAllocationConstraintChecker(List<IConstraintChecker> constraintCheckers) {
		return constraintCheckers.parallelStream().filter(c -> (c instanceof ResourceAllocationConstraintChecker)).map(c -> (ResourceAllocationConstraintChecker) c).findFirst().get();
	}

	private PortExclusionConstraintChecker getPortExclusionConstraintChecker(List<IConstraintChecker> constraintCheckers) {
		return constraintCheckers.parallelStream().filter(c -> (c instanceof PortExclusionConstraintChecker)).map(c -> (PortExclusionConstraintChecker) c).findFirst().get();
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
	public ArrayList<Set<Integer>> getIntegerCargoVesselRestrictions(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels) {
		return getIntegerCargoVesselRestrictions(cargoes, vessels, getCargoVesselRestrictions(cargoes, vessels));
	}

}
