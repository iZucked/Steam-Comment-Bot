/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.Assignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.optimiser.Constraint;
import com.mmxlabs.models.lng.optimiser.Objective;
import com.mmxlabs.models.lng.optimiser.OptimiserModel;
import com.mmxlabs.models.lng.optimiser.OptimiserSettings;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.AVesselClass;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.OptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.ConstrainedInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.IInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorUtil;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * Utility for taking an OptimisationSettings from the EMF and starting an optimiser accordingly. At the moment, it's pretty much just what was in TestUtils.
 * 
 * @author hinton
 * 
 */
public class OptimisationTransformer {
	private static final Logger log = LoggerFactory.getLogger(OptimisationTransformer.class);

	private MMXRootObject rootObject;

	private OptimiserSettings settings;

	@Inject
	private IFitnessFunctionRegistry fitnessFunctionRegistry;

	@Inject
	private IConstraintCheckerRegistry constraintCheckerRegistry;

	@Inject
	private IEvaluationProcessRegistry evaluationProcessRegistry;

	public OptimisationTransformer(final MMXRootObject rootObject) {
		this(rootObject, rootObject.getSubModel(OptimiserModel.class).getActiveSetting());
	}

	public OptimisationTransformer(final MMXRootObject root, final OptimiserSettings settings) {
		this.settings = settings;
		this.rootObject = root;
	}

	public IOptimisationContext createOptimisationContext(final IOptimisationData data, final ModelEntityMap mem) {
		final ISequences sequences = createInitialSequences(data, mem);

//		createConstraintCheckerRegistry();
//		createFitnessFunctionRegistry();
//		createEvaluationProcessRegistry();

		final List<String> components = getEnabledFitnessFunctionNames();
		log.debug("Desired components: " + components);
		components.retainAll(fitnessFunctionRegistry.getFitnessComponentNames());

		final List<String> checkers = getEnabledConstraintNames();
		log.debug("Available components: " + components);
		checkers.retainAll(constraintCheckerRegistry.getConstraintCheckerNames());

		// Enable all processes
		// final List<String> evaluationProcesses = getEnabledEvaluationProcessNames();
		// log.debug("Available evaluation processes: " + evaluationProcesses);
		// evaluationProcesses.retainAll(evaluationProcessRegistry.getEvaluationProcessNames());

		final List<String> evaluationProcesses = new ArrayList<String>(evaluationProcessRegistry.getEvaluationProcessNames());

		return new OptimisationContext(data, sequences, components, fitnessFunctionRegistry, checkers, constraintCheckerRegistry, evaluationProcesses, evaluationProcessRegistry);
	}

	public Pair<IOptimisationContext, LocalSearchOptimiser> createOptimiserAndContext(final IOptimisationData data, final ModelEntityMap mem) {
		final IOptimisationContext context = createOptimisationContext(data, mem);

		final LSOConstructor lsoConstructor = new LSOConstructor(settings);

		return new Pair<IOptimisationContext, LocalSearchOptimiser>(context, lsoConstructor.buildOptimiser(context, SequencesManipulatorUtil.createDefaultSequenceManipulators(data)));
	}

	private List<String> getEnabledConstraintNames() {
		final List<String> result = new ArrayList<String>();

		for (final Constraint c : settings.getConstraints()) {
			if (c.isEnabled()) {
				result.add(c.getName());
			}
		}

		return result;
	}

	private List<String> getEnabledFitnessFunctionNames() {
		final List<String> result = new ArrayList<String>();

		for (final Objective o : settings.getObjectives()) {
			if (o.isEnabled() && o.getWeight() > 0) {
				result.add(o.getName());
			}
		}

		return result;
	}

	public IEvaluationProcessRegistry createEvaluationProcessRegistry() {
		evaluationProcessRegistry = new EvaluationProcessRegistry();

		return evaluationProcessRegistry;
	}

	/**
	 * Create initial sequences; starts with the advice sequences (if there are any) and then uses the {@link ConstrainedInitialSequenceBuilder} to sort out any unsequenced elements.
	 * 
	 * @param data
	 * @return
	 */
	public ISequences createInitialSequences(final IOptimisationData data, final ModelEntityMap mem) {
		/**
		 * This sequences is passed into the initial sequence builder as a starting point. Extra elements may be added to the sequence in any position, but the existing elements will not be removed or
		 * reordered
		 */
		final IModifiableSequences advice;
		/**
		 * This map will be used to try and place elements which aren't in the advice above onto particular resources, if possible.
		 */
		final Map<ISequenceElement, IResource> resourceAdvice = new HashMap<ISequenceElement, IResource>();
		final InputModel inputModel = rootObject.getSubModel(InputModel.class);

		if (!inputModel.getAssignments().isEmpty()) {
			log.debug("Creating advice for sequence builder");
			advice = new ModifiableSequences(data.getResources());
			final IVesselProvider vp = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
			final IPortSlotProvider psp = data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);
			final IStartEndRequirementProvider serp = data.getDataComponentProvider(SchedulerConstants.DCP_startEndRequirementProvider, IStartEndRequirementProvider.class);

			for (final Entry<IResource, IModifiableSequence> sequence : advice.getModifiableSequences().entrySet()) {
				sequence.getValue().add(serp.getStartElement(sequence.getKey()));
			}

			final Map<IVesselClass, List<IVessel>> spotVesselsByClass = new HashMap<IVesselClass, List<IVessel>>();
			for (final IResource resource : data.getResources()) {
				final IVessel vessel = vp.getVessel(resource);
				if (!vessel.getVesselInstanceType().equals(VesselInstanceType.SPOT_CHARTER))
					continue;

				final IVesselClass vesselClass = vessel.getVesselClass();

				List<IVessel> vesselsOfClass = spotVesselsByClass.get(vesselClass);
				if (vesselsOfClass == null) {
					vesselsOfClass = new LinkedList<IVessel>();
					spotVesselsByClass.put(vesselClass, vesselsOfClass);
				}

				vesselsOfClass.add(vessel);
			}

			assignments: for (final Assignment assignment : inputModel.getAssignments()) {
				if (assignment.getVessels().isEmpty()) continue assignments;
				IVessel vessel = null;
				;
				if (assignment.isAssignToSpot()) {
					AVesselClass modelVesselClass;
					for (final AVesselSet set : assignment.getVessels()) {
						if (set instanceof AVesselClass) {
							modelVesselClass = (AVesselClass) set;
							final IVesselClass vesselClass = mem.getOptimiserObject(modelVesselClass, IVesselClass.class);

							final List<IVessel> vesselsOfClass = spotVesselsByClass.get(vesselClass);
							if (vesselsOfClass == null || vesselsOfClass.isEmpty())
								continue assignments;

							vessel = vesselsOfClass.get(0);
							vesselsOfClass.remove(0);

							break;
						}
					}
				} else {
					final AVessel modelVessel = SetUtils.getVessels(assignment.getVessels()).iterator().next();
					vessel = mem.getOptimiserObject(modelVessel, IVessel.class);
				}
				if (vessel == null) {
					log.warn("Vessel from assignments not found");
					continue assignments;
				}
				final IResource resource = vp.getResource(vessel);
				if (assignment.getAssignedObjects().size() == 1) {
					for (final ISequenceElement element : getElements(assignment.getAssignedObjects().get(0), psp, mem)) {
						resourceAdvice.put(element, resource);
					}
				} else {
					final IModifiableSequence sequence = advice.getModifiableSequence(resource);
					for (final UUIDObject assignedObject : assignment.getAssignedObjects()) {
						for (final ISequenceElement element : getElements(assignedObject, psp, mem)) {
							sequence.add(element);
						}
					}
				}
			}

			for (final Entry<IResource, IModifiableSequence> sequence : advice.getModifiableSequences().entrySet()) {
				sequence.getValue().add(serp.getEndElement(sequence.getKey()));
			}
		} else {
			advice = null;
		}

		// final IConstraintCheckerRegistry registry = createConstraintCheckerRegistry();

		final IInitialSequenceBuilder builder = new ConstrainedInitialSequenceBuilder(constraintCheckerRegistry.getConstraintCheckerFactories(getEnabledConstraintNames()));

		return builder.createInitialSequences(data, advice, resourceAdvice);
	}

	private ISequenceElement[] getElements(final UUIDObject modelObject, final IPortSlotProvider psp, final ModelEntityMap mem) {
		if (modelObject instanceof VesselEvent) {
			final VesselEvent event = (VesselEvent) modelObject;
			final IVesselEventPortSlot eventSlot = mem.getOptimiserObject(event, IVesselEventPortSlot.class);
			if (eventSlot != null) {
				return new ISequenceElement[] { psp.getElement(eventSlot) };
			}
		} else if (modelObject instanceof Cargo) {
			final Cargo cargo = (Cargo) modelObject;
			final Slot loadSlot = cargo.getLoadSlot();
			final Slot dischargeSlot = cargo.getDischargeSlot();
			final IPortSlot loadPortSlot = mem.getOptimiserObject(loadSlot, IPortSlot.class);
			final IPortSlot dischargePortSlot = mem.getOptimiserObject(dischargeSlot, IPortSlot.class);
			if (loadPortSlot != null && dischargePortSlot != null) {
				return new ISequenceElement[] { psp.getElement(loadPortSlot), psp.getElement(dischargePortSlot) };
			}
		} else if (modelObject instanceof Slot) {
			final Slot slot = (Slot) modelObject;
			final IPortSlot portSlot = mem.getOptimiserObject(slot, IPortSlot.class);
			if (portSlot != null) {
				return new ISequenceElement[] { psp.getElement(portSlot) };
			}
		}
		return new ISequenceElement[0];
	}
}
