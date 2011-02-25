/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.lngscheduler.emf.extras.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.common.util.EList;

import scenario.Scenario;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFactory;
import scenario.schedule.ScheduleFitness;
import scenario.schedule.SchedulePackage;
import scenario.schedule.Sequence;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.fleetallocation.AllocatedVessel;
import scenario.schedule.fleetallocation.FleetVessel;
import scenario.schedule.fleetallocation.SpotVessel;

import com.mmxlabs.lngscheduler.emf.extras.ModelEntityMap;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IAnnotations;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * A utility class for turning an annotated solution into some EMF
 * representation, for the presentation layer.
 * 
 * At the moment this has the simplistic idea that every annotation corresponds
 * to a scheduled event in the EMF output, which is almost certainly not true.
 * 
 * @author hinton
 * 
 */
public class AnnotatedSolutionExporter {
	private List<IAnnotationExporter> exporters = new LinkedList<IAnnotationExporter>();
	final ScheduleFactory factory = SchedulePackage.eINSTANCE
			.getScheduleFactory();

	public AnnotatedSolutionExporter() {
		exporters.add(new IdleEventExporter());
		exporters.add(new JourneyEventExporter());
		exporters.add(new VisitEventExporter());
	}

	public Schedule exportAnnotatedSolution(final Scenario inputScenario,
			final ModelEntityMap entities,
			final IAnnotatedSolution<ISequenceElement> annotatedSolution) {
		final IOptimisationData<ISequenceElement> data = annotatedSolution
				.getContext().getOptimisationData();
		final IVesselProvider vesselProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		final IAnnotations<ISequenceElement> elementAnnotations = annotatedSolution
				.getElementAnnotations();

		final Schedule output = factory.createSchedule();

		// go through the annotated solution and build stuff for the EMF;
		entities.setScenario(inputScenario);

		// prepare exporters
		for (final IAnnotationExporter exporter : exporters) {
			exporter.setOutput(output);
			exporter.setScenario(inputScenario);
			exporter.setModelEntityMap(entities);
			exporter.setAnnotatedSolution(annotatedSolution);

			exporter.init();
		}

		final List<Sequence> sequences = output.getSequences();
		final List<IResource> resources = annotatedSolution.getSequences()
				.getResources();
		// Create sequences and run other exporters

		final Map<IVesselClass, AtomicInteger> counter = new HashMap<IVesselClass, AtomicInteger>();
		@SuppressWarnings("unchecked")
		final Map<IResource, Map<String, Long>> sequenceFitnesses = annotatedSolution
				.getGeneralAnnotation(SchedulerConstants.G_AI_fitnessPerRoute,
						Map.class);
		for (final IResource resource : resources) {
			final IVessel vessel = vesselProvider.getVessel(resource);
			final AllocatedVessel outputVessel;
			switch (vessel.getVesselInstanceType()) {
			case TIME_CHARTER:
			case FLEET:
				final FleetVessel fv = scenario.schedule.fleetallocation.FleetallocationPackage.eINSTANCE
						.getFleetallocationFactory().createFleetVessel();

				fv.setVessel(entities.getModelObject(vessel, Vessel.class));

				outputVessel = fv;
				break;
			case SPOT_CHARTER:
				if (annotatedSolution.getSequences().getSequence(resource)
						.size() < 2)
					continue;
				final SpotVessel sv = scenario.schedule.fleetallocation.FleetallocationPackage.eINSTANCE
						.getFleetallocationFactory().createSpotVessel();

				final AtomicInteger ai = counter.get(vessel.getVesselClass());
				int ix = 0;

				if (ai == null) {
					counter.put(vessel.getVesselClass(), new AtomicInteger(ix));
				} else {
					ix = ai.incrementAndGet();
				}

				sv.setVesselClass(entities.getModelObject(
						vessel.getVesselClass(), VesselClass.class));

				sv.setIndex(ix);

				outputVessel = sv;
				break;
			default:
				outputVessel = null;
				break;
			}

			final Sequence eSequence = factory.createSequence();
			sequences.add(eSequence);

			{
				// set sequence fitness values
				final EList<ScheduleFitness> eSequenceFitness = eSequence
						.getFitness();
				final Map<String, Long> sequenceFitness = sequenceFitnesses.get(resource);
				for (final Map.Entry<String, Long> e : sequenceFitness.entrySet()) {
					final ScheduleFitness sf = ScheduleFactory.eINSTANCE.createScheduleFitness();
					sf.setName(e.getKey());
					sf.setValue(e.getValue());
					eSequenceFitness.add(sf);
				}
			}
			output.getFleet().add(outputVessel);
			eSequence.setVessel(outputVessel);

			final EList<ScheduledEvent> events = eSequence.getEvents();

			Comparator<ScheduledEvent> eventComparator = new Comparator<ScheduledEvent>() {
				@Override
				public int compare(ScheduledEvent arg0, ScheduledEvent arg1) {
					if (arg0.getStartTime().before(arg1.getStartTime())) {
						return -1;
					} else if (arg0.getStartTime().after(arg1.getStartTime())) {
						return 1;
					}
					return 0;
				}
			};

			final List<ScheduledEvent> eventsForElement = new ArrayList<ScheduledEvent>();
			for (final ISequenceElement element : annotatedSolution
					.getSequences().getSequence(resource)) {
				// get annotations for this element
				final Map<String, Object> annotations = elementAnnotations
						.getAnnotations(element);

				for (final IAnnotationExporter exporter : exporters) {
					final ScheduledEvent result = exporter.export(element,
							annotations, outputVessel);
					if (result != null)
						eventsForElement.add(result);
				}

				// this is messy, but we want to be sure stuff is in the right
				// order or it won't make any sense.
				Collections.sort(eventsForElement, eventComparator);
				events.addAll(eventsForElement);
				eventsForElement.clear();
			}
		}

		@SuppressWarnings("unchecked")
		final Map<String, Long> fitnesses = annotatedSolution
				.getGeneralAnnotation(
						OptimiserConstants.G_AI_fitnessComponents, Map.class);

		final Long runtime = annotatedSolution.getGeneralAnnotation(
				OptimiserConstants.G_AI_runtime, Long.class);
		final Integer iterations = annotatedSolution.getGeneralAnnotation(
				OptimiserConstants.G_AI_iterations, Integer.class);

		final EList<ScheduleFitness> outputFitnesses = output.getFitness();

		for (final Map.Entry<String, Long> entry : fitnesses.entrySet()) {
			final ScheduleFitness fitness = factory.createScheduleFitness();
			fitness.setName(entry.getKey());
			fitness.setValue(entry.getValue());
			outputFitnesses.add(fitness);
		}

		final ScheduleFitness eRuntime = factory.createScheduleFitness();
		eRuntime.setName("runtime");
		eRuntime.setValue(runtime);

		outputFitnesses.add(eRuntime);

		final ScheduleFitness eIters = factory.createScheduleFitness();
		eIters.setName("iterations");
		eIters.setValue(iterations.longValue());

		outputFitnesses.add(eIters);

		return output;
	}
}
