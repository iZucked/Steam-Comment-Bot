/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint.PortShipSizeConstraintChecker;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ICargoVesselRestrictionsMatrixProducer;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintChecker;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IResourceElementConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class CargoVesselRestrictionsMatrixProducer implements ICargoVesselRestrictionsMatrixProducer {
	private static final Logger LOG = LoggerFactory.getLogger(CargoVesselRestrictionsMatrixProducer.class);
	/**
	 * Set of lightweight constraints types considered by this class.
	 */
	private static final Set<Class<?>> LIGHTWEIGHT_CONSTRAINTS = Set.of(PortExclusionConstraintChecker.class, ResourceAllocationConstraintChecker.class, AllowedVesselPermissionConstraintChecker.class, PortShipSizeConstraintChecker.class);
	
	@Inject
	private IVesselProvider vesselProvider;
	
	@Inject
	private IPortSlotProvider portSlotProvider;

	private LinkedList<IConstraintChecker> constraintCheckers;
	
	@Inject
	public void injectConstraintChecker(@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) final ISequences initialRawSequences, final List<IConstraintChecker> injectedConstraintCheckers) {
		this.constraintCheckers = new LinkedList<>();
		final List<@Nullable String> messages;
		if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES) {
			messages = new ArrayList<>();
			messages.add(String.format("%s: injectConstraintChecker", this.getClass().getName()));
		} else {
			messages = null;
		}
		for (final IConstraintChecker checker : injectedConstraintCheckers) {
			if (checker instanceof IPairwiseConstraintChecker) {
				final IPairwiseConstraintChecker constraintChecker = (IPairwiseConstraintChecker) checker;
				constraintCheckers.add(constraintChecker);

				// Prep with initial sequences.
				constraintChecker.checkConstraints(initialRawSequences, null, messages);
			}
		}
		if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES && !messages.isEmpty())
			messages.stream().forEach(LOG::debug);
	}

	@Override
	public Map<List<IPortSlot>, List<IVesselAvailability>> getCargoVesselRestrictions(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels) {
	
		List<IConstraintChecker> lightWeightConstraintCheckers = getLightWeightConstraintCheckers(constraintCheckers);

		Map<List<IPortSlot>, List<IVesselAvailability>> cargoMap = new HashMap<>();
		for (List<IPortSlot> cargo : cargoes) {
			List<IVesselAvailability> restrictedVessels = new LinkedList<>();
			for (IVesselAvailability vessel : vessels) {				
				if (!checkConstraints(lightWeightConstraintCheckers, cargo, vessel)) {
					restrictedVessels.add(vessel);
				}
			}

			cargoMap.put(cargo, restrictedVessels);
		}
		return cargoMap;
	}

	private boolean checkConstraints(List<IConstraintChecker> constraints, List<IPortSlot> cargo, IVesselAvailability vessel) {
		final List<@Nullable String> messages;
		if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES) {
			messages = new ArrayList<>();
			messages.add(String.format("%s: checkConstraints", this.getClass().getName()));
		} else {
			messages = null;
		}
		for (IConstraintChecker con : constraints) {
			if (con instanceof IResourceElementConstraintChecker) {
				IResourceElementConstraintChecker c = (IResourceElementConstraintChecker)con;
				for(IPortSlot slot : cargo) {
					if (!c.checkElement(portSlotProvider.getElement(slot), vesselProvider.getResource(vessel), messages)) {
						if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES && !messages.isEmpty())
							messages.stream().forEach(LOG::debug);
						return false;
					}
				}
			}
			else if (cargo.size() == 2 && con instanceof IPairwiseConstraintChecker) {
				IPairwiseConstraintChecker c = (IPairwiseConstraintChecker)con;
				if (!c.checkPairwiseConstraint(portSlotProvider.getElement(cargo.get(0)), portSlotProvider.getElement(cargo.get(1)), vesselProvider.getResource(vessel), messages)) {
					if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES && !messages.isEmpty())
						messages.stream().forEach(LOG::debug);
					return false;
				}
			}
		}
		return true;
	}	
	
	private List<IConstraintChecker> getLightWeightConstraintCheckers(List<IConstraintChecker> constraintCheckers) {
		List<IConstraintChecker> lightWeightConstraints = new ArrayList<>();
		Set<Class<?>> lightWeightConstraintTypes = getLightWeightConstraintClasses();
		constraintCheckers.forEach(c -> 
		{ 
			if (lightWeightConstraintTypes.contains(c.getClass())) {
				lightWeightConstraints.add(c); 
			}
		});
		return lightWeightConstraints;
	}

	private Set<Class<?>> getLightWeightConstraintClasses() {
		return LIGHTWEIGHT_CONSTRAINTS;
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
			set = restrictions.get(cargo).stream().map(v -> vesselMap.get(v)).collect(Collectors.toCollection(LinkedHashSet::new));
			cargoMap.add(set);
		}
		return cargoMap;
	}

	@Override
	public ArrayList<Set<Integer>> getIntegerCargoVesselRestrictions(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels) {
		return getIntegerCargoVesselRestrictions(cargoes, vessels, getCargoVesselRestrictions(cargoes, vessels));
	}
}
