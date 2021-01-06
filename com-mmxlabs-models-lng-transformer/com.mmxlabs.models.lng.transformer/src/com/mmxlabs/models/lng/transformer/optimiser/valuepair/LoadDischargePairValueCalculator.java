/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.valuepair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenIdleTimeConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenLegLimitConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintChecker;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

@NonNullByDefault
public class LoadDischargePairValueCalculator {
	protected static final Logger LOG = LoggerFactory.getLogger(LoadDischargePairValueCalculator.class);

	private List<IPairwiseConstraintChecker> constraintCheckers = new LinkedList<>();

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IVirtualVesselSlotProvider virtualVesselSlotProvider;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private Injector injector;

	@Inject
	private IPhaseOptimisationData optimisationData;

	@Inject
	public void injectConstraintChecker(@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) final ISequences initialRawSequences, final List<IConstraintChecker> injectedConstraintCheckers) {
		this.constraintCheckers = new LinkedList<>();
		final List<String> messages = new ArrayList<>();
		messages.add(String.format("%s: injectConstraintChecker", this.getClass().getName()));
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
		modifyConstraintCheckers(constraintCheckers);
	}

	private void modifyConstraintCheckers(@NonNull List<@NonNull IPairwiseConstraintChecker> constraints) {
		for (IPairwiseConstraintChecker checker : constraints) {
			if (checker instanceof TravelTimeConstraintChecker) {
				((TravelTimeConstraintChecker) checker).setMaxLateness(0);
			}
			if (checker instanceof LadenIdleTimeConstraintChecker) {
				((LadenIdleTimeConstraintChecker) checker).setMaxIdleTimeInHours(8*24);
			}
			if (checker instanceof LadenLegLimitConstraintChecker) {
				((LadenLegLimitConstraintChecker) checker).setMaxLadenDuration(32 * 24);
			}
//
		}
	}

	private boolean isValidPair(final ILoadOption load, final IDischargeOption discharge, final IVesselAvailability vessel) {

		if (!(load instanceof ILoadSlot) && !(discharge instanceof IDischargeSlot)) {
			return false;
		}
		final List<String> messages = new ArrayList<>();
		messages.add(String.format("%s: isValidPair", this.getClass().getName()));
		for (final IPairwiseConstraintChecker checker : constraintCheckers) {
			if (!checker.checkPairwiseConstraint(portSlotProvider.getElement(load), portSlotProvider.getElement(discharge), vesselProvider.getResource(vessel), messages)) {
				if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES && !messages.isEmpty())
					messages.stream().forEach(LOG::debug);
				return false;
			}
		}

		return true;
	}
	
	/**
	 * Check whether the load and discharge are compatible for at least one of the vessels (apart from the nominal vessel)
	 * NB: reverted isValidPair as the "updated" code appeared to load+discharge require all the constraints to be satisfied for all 
	 * vessels in the scenario. This prevented ADP optimisation from being possible with contracts with vessel constraints present on
	 * the contracts.
	 * @param load
	 * @param discharge
	 * @param vessel
	 * @param vessels
	 * @return true, if compatible, false, otherwise.
	 */
	private boolean isValidPair(final ILoadOption load, final IDischargeOption discharge, final IVesselAvailability vessel, Collection<IVesselAvailability> vessels) {

		if (!(load instanceof ILoadSlot) && !(discharge instanceof IDischargeSlot)) {
			return false;
		}
		final List<String> messages = new ArrayList<>();
		messages.add(String.format("%s: isValidPair", this.getClass().getName()));
		for (final IVesselAvailability v : vessels) {
			if (v == vessel) continue;
			boolean isValid = true;
			for (final IPairwiseConstraintChecker checker : constraintCheckers) {
				if (!checker.checkPairwiseConstraint(portSlotProvider.getElement(load), portSlotProvider.getElement(discharge), vesselProvider.getResource(v), messages)) {
					if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES && !messages.isEmpty())
						messages.stream().forEach(LOG::debug);
					//checker.checkPairwiseConstraint(portSlotProvider.getElement(load), portSlotProvider.getElement(discharge), vesselProvider.getResource(v));
					isValid = false;//return false;
				}
			}
			if (isValid) {
				return true;
			}
		}

		return false;
	}

	private Pair<IAnnotatedSolution, EvaluationState> evaluate(final ILoadOption load, final IDischargeOption discharge, final IVesselAvailability vesselAvailability) {

		// Create the sequences object
		final ISequences sequences = createSequences(load, discharge, vesselAvailability);

		// Run through the sequences manipulator of things such as start/end port replacement

		final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);
		// this will set the return elements to the right places, and remove the start elements.
		final ISequences mSequences = manipulator.createManipulatedSequences(sequences);

		final EvaluationState state = new EvaluationState();

		final AnnotatedSolution solution = new AnnotatedSolution(mSequences, state);

		final IEvaluationProcess process = injector.getInstance(SchedulerEvaluationProcess.class);

		process.annotate(mSequences, state, solution);

		return new Pair<>(solution, state);

	}

	private ISequences createSequences(final ILoadOption load, final IDischargeOption discharge, final IVesselAvailability vesselAvailability) {
		final ModifiableSequences sequences = new ModifiableSequences(optimisationData.getResources());
		final IResource target_resource = vesselProvider.getResource(vesselAvailability);

		boolean foundTarget = false;
		for (final IResource resource : optimisationData.getResources()) {
			final IModifiableSequence modifiableSequence = sequences.getModifiableSequence(resource);

			modifiableSequence.add(startEndRequirementProvider.getStartElement(resource));
			if (resource == target_resource) {
				foundTarget = true;
				modifiableSequence.add(portSlotProvider.getElement(load));
				modifiableSequence.add(portSlotProvider.getElement(discharge));
			}
			modifiableSequence.add(startEndRequirementProvider.getEndElement(resource));
		}
		if (!foundTarget) {
			throw new IllegalStateException();
		}

		return sequences;
	}

	private IVesselAvailability getCorrectVesselAvailability(final ILoadOption load, final IDischargeOption discharge, final IVesselAvailability vesselAvailability) {
		if (!(load instanceof ILoadSlot)) {
			// DES Purchase, return DES vessel availability
			final ISequenceElement element = portSlotProvider.getElement(load);
			return virtualVesselSlotProvider.getVesselAvailabilityForElement(element);
		}
		if (!(discharge instanceof IDischargeSlot)) {
			// FOB Sale , return FOBvessel availability
			final ISequenceElement element = portSlotProvider.getElement(discharge);
			return virtualVesselSlotProvider.getVesselAvailabilityForElement(element);
		}
		return vesselAvailability;
	}

	public static List<ILoadOption> findPurchases(final IPhaseOptimisationData optimisationData, final IPortSlotProvider portSlotProvider) {

		return optimisationData.getSequenceElements().stream() //
				.map(portSlotProvider::getPortSlot)//
				.filter(ILoadOption.class::isInstance) //
				.map(ILoadOption.class::cast)//
				.collect(Collectors.toList());
	}

	public static List<IDischargeOption> findSales(final IPhaseOptimisationData optimisationData, final IPortSlotProvider portSlotProvider) {

		return optimisationData.getSequenceElements().stream() //
				.map(portSlotProvider::getPortSlot)//
				.filter(IDischargeOption.class::isInstance) //
				.map(IDischargeOption.class::cast)//
				.collect(Collectors.toList());
	}
	
	public static List<IVesselAvailability> findVessels(final IPhaseOptimisationData optimisationData, final IVesselProvider vesselProvider) {

		return optimisationData.getResources().stream()
				.map(vesselProvider::getVesselAvailability)//
				.collect(Collectors.toList());
	}

	public void generate(final ILoadOption loadOption, final IDischargeOption dischargeOption, //
			final IVesselAvailability nominalVessel, final ResultRecorder recorder, //
			final List<IVesselAvailability> vessels) {

		final IVesselAvailability vesselAvailability = getCorrectVesselAvailability(loadOption, dischargeOption, nominalVessel);
		if (isValidPair(loadOption, dischargeOption, vesselAvailability)) {
			if ((nominalVessel != vesselAvailability) || (isValidPair(loadOption, dischargeOption, vesselAvailability, vessels))) {
				final Pair<IAnnotatedSolution, EvaluationState> result = evaluate(loadOption, dischargeOption, vesselAvailability);
				if (result != null) {
					IResource resource = vesselProvider.getResource(vesselAvailability);
					recorder.record(loadOption, dischargeOption, resource, result);
				} else {
					// System.out.printf("Failed Pair %s -> %s\n", loadOption.getId(), dischargeOption.getId());
				}
			}
		} else {
			// System.out.printf("Invalid Pair %s -> %s\n", loadOption.getId(), dischargeOption.getId());
		}
	}
}
