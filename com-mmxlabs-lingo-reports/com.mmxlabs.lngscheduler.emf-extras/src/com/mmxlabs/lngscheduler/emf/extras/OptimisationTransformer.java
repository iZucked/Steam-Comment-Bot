/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import scenario.fleet.CharterOut;
import scenario.optimiser.Constraint;
import scenario.optimiser.Objective;
import scenario.optimiser.OptimisationSettings;
import scenario.optimiser.lso.LSOSettings;
import scenario.schedule.Sequence;
import scenario.schedule.events.CharterOutVisit;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;
import scenario.schedule.fleetallocation.AllocatedVessel;
import scenario.schedule.fleetallocation.FleetVessel;
import scenario.schedule.fleetallocation.SpotVessel;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.OptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.ICharterOutPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
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
 * Utility for taking an OptimisationSettings from the EMF and starting an
 * optimiser accordingly. At the moment, it's pretty much just what was in
 * TestUtils.
 * 
 * @author hinton
 * 
 */
public class OptimisationTransformer {
	private OptimisationSettings settings;

	public OptimisationTransformer(OptimisationSettings settings) {
		this.settings = settings;
	}

	public IOptimisationContext<ISequenceElement> createOptimisationContext(
			final IOptimisationData<ISequenceElement> data,
			final ModelEntityMap mem) {
		ISequences<ISequenceElement> sequences = createInitialSequences(data,
				mem);
		IConstraintCheckerRegistry checkerRegistry = createConstraintCheckerRegistry();
		IFitnessFunctionRegistry componentRegistry = createFitnessFunctionRegistry();
		List<String> checkers = getEnabledConstraintNames();
		List<String> components = getEnabledFitnessFunctionNames();
		return new OptimisationContext<ISequenceElement>(data, sequences,
				components, componentRegistry, checkers, checkerRegistry);
	}

	public Pair<IOptimisationContext<ISequenceElement>, LocalSearchOptimiser<ISequenceElement>> createOptimiserAndContext(
			IOptimisationData<ISequenceElement> data, final ModelEntityMap mem) {
		IOptimisationContext<ISequenceElement> context = createOptimisationContext(
				data, mem);

		LSOConstructor lsoConstructor = new LSOConstructor(
				(LSOSettings) settings);

		return new Pair<IOptimisationContext<ISequenceElement>, LocalSearchOptimiser<ISequenceElement>>(
				context, lsoConstructor.buildOptimiser(context,
						SequencesManipulatorUtil
								.createDefaultSequenceManipulators(data)));
	}

	private List<String> getEnabledConstraintNames() {
		List<String> result = new ArrayList<String>();

		for (Constraint c : settings.getConstraints()) {
			if (c.isEnabled()) {
				result.add(c.getName());
			}
		}

		return result;
	}

	private List<String> getEnabledFitnessFunctionNames() {
		List<String> result = new ArrayList<String>();

		for (Objective o : settings.getObjectives()) {
			if (o.getWeight() > 0) {
				result.add(o.getName());
			}
		}

		return result;
	}

	/**
	 * Creates a fitness function registry
	 * 
	 * @param data
	 * @return
	 */
	public IFitnessFunctionRegistry createFitnessFunctionRegistry() {
		final FitnessFunctionRegistry fitnessRegistry = new FitnessFunctionRegistry();

		final CargoSchedulerFitnessCoreFactory factory = new CargoSchedulerFitnessCoreFactory();

		fitnessRegistry.registerFitnessCoreFactory(factory);
		return fitnessRegistry;
	}

	/**
	 * Creates a constraint checker registry
	 * 
	 * @param data
	 * @return
	 */
	public IConstraintCheckerRegistry createConstraintCheckerRegistry() {
		final IConstraintCheckerRegistry constraintRegistry = new ConstraintCheckerRegistry();
		{
			final OrderedSequenceElementsConstraintCheckerFactory constraintFactory = new OrderedSequenceElementsConstraintCheckerFactory(
					SchedulerConstants.DCP_orderedElementsProvider);
			constraintRegistry
					.registerConstraintCheckerFactory(constraintFactory);
		}
		{
			final ResourceAllocationConstraintCheckerFactory constraintFactory = new ResourceAllocationConstraintCheckerFactory(
					SchedulerConstants.DCP_resourceAllocationProvider);
			constraintRegistry
					.registerConstraintCheckerFactory(constraintFactory);
		}

		constraintRegistry
				.registerConstraintCheckerFactory(new PortTypeConstraintCheckerFactory(
						SchedulerConstants.DCP_portTypeProvider,
						SchedulerConstants.DCP_vesselProvider));

		constraintRegistry
				.registerConstraintCheckerFactory(new TravelTimeConstraintCheckerFactory());

		constraintRegistry
				.registerConstraintCheckerFactory(new PortExclusionConstraintCheckerFactory(
						SchedulerConstants.DCP_portExclusionProvider,
						SchedulerConstants.DCP_vesselProvider,
						SchedulerConstants.DCP_portProvider));

		return constraintRegistry;
	}

	/**
	 * Create initial sequences; currently random, ideally will use the
	 * optimisation settings.
	 * 
	 * @param data
	 * @return
	 */
	public ISequences<ISequenceElement> createInitialSequences(
			IOptimisationData<ISequenceElement> data, final ModelEntityMap mem) {
		// Create the sequenced constraint checkers here
		final IModifiableSequences<ISequenceElement> advice;
		if (settings.getInitialSchedule() != null) {
			advice = new ModifiableSequences<ISequenceElement>(
					data.getResources());

			final Map<IVesselClass, Set<IVessel>> spotVesselsByClass = new LinkedHashMap<IVesselClass, Set<IVessel>>();

			final IVesselProvider vp = data.getDataComponentProvider(
					SchedulerConstants.DCP_vesselProvider,
					IVesselProvider.class);

			final IPortSlotProvider<ISequenceElement> psp = data
					.getDataComponentProvider(
							SchedulerConstants.DCP_portSlotsProvider,
							IPortSlotProvider.class);

			final IStartEndRequirementProvider<ISequenceElement> serp = data
					.getDataComponentProvider(
							SchedulerConstants.DCP_startEndRequirementProvider,
							IStartEndRequirementProvider.class);

			// collect spot vessels
			for (final IResource resource : data.getResources()) {
				final IVessel vessel = vp.getVessel(resource);
				if (vessel.getVesselInstanceType().equals(
						VesselInstanceType.SPOT_CHARTER)) {
					if (spotVesselsByClass.containsKey(vessel.getVesselClass())) {
						spotVesselsByClass.get(vessel.getVesselClass()).add(
								vessel);
					} else {
						final HashSet<IVessel> hs = new LinkedHashSet<IVessel>();
						hs.add(vessel);
						spotVesselsByClass.put(vessel.getVesselClass(), hs);
					}
				}
			}

			for (final Sequence sequence : settings.getInitialSchedule()
					.getSequences()) {

				final AllocatedVessel av = sequence.getVessel();
				final IVessel vessel;

				if (av instanceof SpotVessel) {
					final IVesselClass vesselClass = mem.getOptimiserObject(
							((SpotVessel) av).getVesselClass(),
							IVesselClass.class);
					// match vesselClass with a suitable vessel
					vessel = spotVesselsByClass.get(vesselClass).iterator()
							.next();
					spotVesselsByClass.get(vesselClass).remove(vessel);
				} else {
					vessel = mem.getOptimiserObject(
							((FleetVessel) av).getVessel(), IVessel.class);
				}

				// get the sequence for the chosen vessel
				final IModifiableSequence<ISequenceElement> ms = advice
						.getModifiableSequence(vp.getResource(vessel));
				ms.add(serp.getStartElement(vp.getResource(vessel)));
				for (final ScheduledEvent event : sequence.getEvents()) {
					if (event instanceof SlotVisit) {
						final SlotVisit v = (SlotVisit) event;
						final IPortSlot portSlot = mem.getOptimiserObject(
								((SlotVisit) event).getSlot(), IPortSlot.class);
						if (portSlot == null) {
							System.err.println("Slot " + v.getSlot().getId() + " is missing from the optimisation - perhaps you changed its cargo type but didn't delete it from the initial solution");
							continue;
						}
						ms.add(psp.getElement(portSlot));
					} else if (event instanceof CharterOutVisit) {
						final CharterOut co = ((CharterOutVisit)event).getCharterOut();
						final ICharterOutPortSlot coSlot = mem.getOptimiserObject(co, ICharterOutPortSlot.class);
						ms.add(psp.getElement(coSlot));
					}
				}
				ms.add(serp.getEndElement(vp.getResource(vessel)));
			}
		} else {
			advice = null;
		}

		final IConstraintCheckerRegistry registry = createConstraintCheckerRegistry();

		final IInitialSequenceBuilder<ISequenceElement> builder = new ConstrainedInitialSequenceBuilder<ISequenceElement>(
				registry.getConstraintCheckerFactories(getEnabledConstraintNames()));

		return builder.createInitialSequences(data, advice);
	}
}
