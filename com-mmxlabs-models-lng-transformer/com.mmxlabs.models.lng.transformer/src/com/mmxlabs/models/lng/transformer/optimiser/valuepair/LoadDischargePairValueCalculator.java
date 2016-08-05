/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.valuepair;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
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
	private IOptimisationData optimisationData;

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

	private boolean isValidPair(final ILoadOption load, final IDischargeOption discharge, final IVesselAvailability vessel) {

		if (!(load instanceof ILoadSlot) && !(discharge instanceof IDischargeSlot)) {
			return false;
		}
		for (final IPairwiseConstraintChecker checker : constraintCheckers) {
			if (!checker.checkPairwiseConstraint(portSlotProvider.getElement(load), portSlotProvider.getElement(discharge), vesselProvider.getResource(vessel))) {
				return false;
			}
		}

		return true;
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
		for (final IResource resource : optimisationData.getResources()) {
			final IModifiableSequence modifiableSequence = sequences.getModifiableSequence(resource);

			modifiableSequence.add(startEndRequirementProvider.getStartElement(resource));
			if (resource == target_resource) {
				modifiableSequence.add(portSlotProvider.getElement(load));
				modifiableSequence.add(portSlotProvider.getElement(discharge));
			}
			modifiableSequence.add(startEndRequirementProvider.getEndElement(resource));
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

	public static List<ILoadOption> findPurchases(final IOptimisationData optimisationData, final IPortSlotProvider portSlotProvider) {

		return optimisationData.getSequenceElements().stream() //
				.map(e -> portSlotProvider.getPortSlot(e))//
				.filter(s -> s instanceof ILoadOption) //
				.map(s -> (ILoadOption) s)//
				.collect(Collectors.toList());
	}

	public static List<IDischargeOption> findSales(final IOptimisationData optimisationData, final IPortSlotProvider portSlotProvider) {

		return optimisationData.getSequenceElements().stream() //
				.map(e -> portSlotProvider.getPortSlot(e))//
				.filter(s -> s instanceof IDischargeOption) //
				.map(s -> (IDischargeOption) s)//
				.collect(Collectors.toList());
	}

	public void generate(final ILoadOption loadOption, final IDischargeOption dischargeOption, final IVesselAvailability nominalVessel, final ResultRecorder recorder) {

		final IVesselAvailability vesselAvailability = getCorrectVesselAvailability(loadOption, dischargeOption, nominalVessel);
		if (isValidPair(loadOption, dischargeOption, vesselAvailability)) {

			final Pair<IAnnotatedSolution, EvaluationState> result = evaluate(loadOption, dischargeOption, vesselAvailability);
			if (result != null) {
				recorder.record(loadOption, dischargeOption, vesselAvailability, result);
			} else {
				System.out.printf("Failed Pair %s -> %s\n", loadOption.getId(), dischargeOption.getId());
			}
		} else {
			System.out.printf("Invalid Pair %s -> %s\n", loadOption.getId(), dischargeOption.getId());
		}
	}
}
