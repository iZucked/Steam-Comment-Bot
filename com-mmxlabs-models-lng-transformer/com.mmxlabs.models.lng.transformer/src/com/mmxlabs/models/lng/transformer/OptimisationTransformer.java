/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scenario.fleet.CharterOut;
import scenario.fleet.VesselEvent;
import scenario.optimiser.Constraint;
import scenario.optimiser.Objective;
import scenario.optimiser.OptimisationSettings;
import scenario.optimiser.lso.LSOSettings;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Sequence;
import scenario.schedule.events.CharterOutVisit;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;
import scenario.schedule.events.VesselEventVisit;
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
import com.mmxlabs.optimiser.core.ISequenceElement;
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
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
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
	private final OptimisationSettings settings;

	public OptimisationTransformer(final OptimisationSettings settings) {
		this.settings = settings;
	}

	public IOptimisationContext createOptimisationContext(final IOptimisationData data, final ModelEntityMap mem) {
		final ISequences sequences = createInitialSequences(data, mem);
		final IConstraintCheckerRegistry checkerRegistry = createConstraintCheckerRegistry();
		final IFitnessFunctionRegistry componentRegistry = createFitnessFunctionRegistry();
		final List<String> checkers = getEnabledConstraintNames();
		final List<String> components = getEnabledFitnessFunctionNames();
		log.debug("Desired components: " + components);
		components.retainAll(componentRegistry.getFitnessComponentNames());
		log.debug("Available components: " + components);
		checkers.retainAll(checkerRegistry.getConstraintCheckerNames());
		return new OptimisationContext(data, sequences, components, componentRegistry, checkers, checkerRegistry);
	}

	public Pair<IOptimisationContext, LocalSearchOptimiser> createOptimiserAndContext(final IOptimisationData data, final ModelEntityMap mem) {
		final IOptimisationContext context = createOptimisationContext(data, mem);

		final LSOConstructor lsoConstructor = new LSOConstructor((LSOSettings) settings);

		return new Pair<IOptimisationContext, LocalSearchOptimiser>(context, lsoConstructor.buildOptimiser(context,
				SequencesManipulatorUtil.createDefaultSequenceManipulators(data)));
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
			final OrderedSequenceElementsConstraintCheckerFactory constraintFactory = new OrderedSequenceElementsConstraintCheckerFactory(SchedulerConstants.DCP_orderedElementsProvider);
			constraintRegistry.registerConstraintCheckerFactory(constraintFactory);
		}
		{
			final ResourceAllocationConstraintCheckerFactory constraintFactory = new ResourceAllocationConstraintCheckerFactory(SchedulerConstants.DCP_resourceAllocationProvider);
			constraintRegistry.registerConstraintCheckerFactory(constraintFactory);
		}

		constraintRegistry.registerConstraintCheckerFactory(new PortTypeConstraintCheckerFactory(SchedulerConstants.DCP_portTypeProvider, SchedulerConstants.DCP_vesselProvider));

		constraintRegistry.registerConstraintCheckerFactory(new TravelTimeConstraintCheckerFactory());

		constraintRegistry.registerConstraintCheckerFactory(new PortExclusionConstraintCheckerFactory(SchedulerConstants.DCP_portExclusionProvider, SchedulerConstants.DCP_vesselProvider,
				SchedulerConstants.DCP_portProvider));

		return constraintRegistry;
	}

	/**
	 * Create initial sequences; starts with the advice sequences (if there are any) and then uses the {@link ConstrainedInitialSequenceBuilder} to sort out any unsequenced elements.
	 * 
	 * @param data
	 * @return
	 */
	public ISequences createInitialSequences(final IOptimisationData data, final ModelEntityMap mem) {
		// Create the sequenced constraint checkers here
		final IModifiableSequences advice;
		final Map<ISequenceElement, IResource> resourceAdvice = new HashMap<ISequenceElement, IResource>();
		if (settings.getInitialSchedule() != null) {
			advice = new ModifiableSequences(data.getResources());

//			final Map<IVesselClass, Set<IVessel>> spotVesselsByClass = new LinkedHashMap<IVesselClass, Set<IVessel>>();

			final IVesselProvider vp = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);

			final IPortSlotProvider psp = data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);

			final IStartEndRequirementProvider serp = data.getDataComponentProvider(SchedulerConstants.DCP_startEndRequirementProvider, IStartEndRequirementProvider.class);

			// // collect spot vessels
			// for (final IResource resource : data.getResources()) {
			// final IVessel vessel = vp.getVessel(resource);
			// if (vessel.getVesselInstanceType().equals(
			// VesselInstanceType.SPOT_CHARTER)) {
			// if (spotVesselsByClass.containsKey(vessel.getVesselClass())) {
			// spotVesselsByClass.get(vessel.getVesselClass()).add(
			// vessel);
			// } else {
			// final HashSet<IVessel> hs = new LinkedHashSet<IVessel>();
			// hs.add(vessel);
			// spotVesselsByClass.put(vessel.getVesselClass(), hs);
			// }
			// }
			// }

			for (final Sequence sequence : settings.getInitialSchedule().getSequences()) {

				final AllocatedVessel av = sequence.getVessel();
				final IVessel vessel;

				if (av instanceof SpotVessel) {
					// final IVesselClass vesselClass = mem.getOptimiserObject(
					// ((SpotVessel) av).getVesselClass(),
					// IVesselClass.class);
					// // match vesselClass with a suitable vessel
					// vessel = spotVesselsByClass.get(vesselClass).iterator()
					// .next();
					// spotVesselsByClass.get(vesselClass).remove(vessel);

					/**
					 * This works because {@link LNGScenarioTransformer} previously allocated all SpotVessels in the initial schedule to IVessels
					 */

					vessel = mem.getOptimiserObject(av, IVessel.class);
				} else {
					vessel = mem.getOptimiserObject(((FleetVessel) av).getVessel(), IVessel.class);
				}

				// get the sequence for the chosen vessel
				final IModifiableSequence ms = advice.getModifiableSequence(vp.getResource(vessel));
				final IResource r = vp.getResource(vessel);

				ms.add(serp.getStartElement(r));
				for (final ScheduledEvent event : sequence.getEvents()) {
					if (event instanceof SlotVisit) {
						final SlotVisit v = (SlotVisit) event;
						final IPortSlot portSlot = mem.getOptimiserObject(((SlotVisit) event).getSlot(), IPortSlot.class);
						if (portSlot == null) {
							log.debug("Slot " + v.getSlot().getId() + " is missing from the optimisation input, but contained in the initial sequences. It will be ignored.");
							continue;
						}
						ms.add(psp.getElement(portSlot));
					} else if (event instanceof CharterOutVisit) {
						final CharterOut co = ((CharterOutVisit) event).getCharterOut();
						final IVesselEventPortSlot coSlot = mem.getOptimiserObject(co, IVesselEventPortSlot.class);
						ms.add(psp.getElement(coSlot));
					} else if (event instanceof VesselEventVisit) {
						final VesselEvent ve = ((VesselEventVisit) event).getVesselEvent();
						final IVesselEventPortSlot veSlot = mem.getOptimiserObject(ve, IVesselEventPortSlot.class);
						ms.add(psp.getElement(veSlot));
					}
				}
				ms.add(serp.getEndElement(vp.getResource(vessel)));
			}

			// create resource advice (map load and discharge slots of cargo
			// allocations to resources)

			// TODO similar thing for drydocks & charter outs.
			for (final CargoAllocation allocation : settings.getInitialSchedule().getCargoAllocations()) {
				if (allocation.getLoadSlotVisit() == null && allocation.getVessel() != null) {
					final ISequenceElement loadElement = psp.getElement(mem.getOptimiserObject(allocation.getLoadSlot(), IPortSlot.class));
					final ISequenceElement dischargeElement = psp.getElement(mem.getOptimiserObject(allocation.getDischargeSlot(), IPortSlot.class));
					final IVessel vessel = (allocation.getVessel() instanceof SpotVessel) ? mem.getOptimiserObject(allocation.getVessel(), IVessel.class) : mem.getOptimiserObject(
							((FleetVessel) allocation.getVessel()).getVessel(), IVessel.class);

					final IResource resource = vp.getResource(vessel);

					resourceAdvice.put(loadElement, resource);
					resourceAdvice.put(dischargeElement, resource);
				}
			}
		} else {
			advice = null;
		}

		final IConstraintCheckerRegistry registry = createConstraintCheckerRegistry();

		final IInitialSequenceBuilder builder = new ConstrainedInitialSequenceBuilder(registry.getConstraintCheckerFactories(getEnabledConstraintNames()));

		return builder.createInitialSequences(data, advice, resourceAdvice);
	}
}
