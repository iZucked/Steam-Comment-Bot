/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.jobcontroller.emf;

import java.util.List;
import java.util.Map;

import scenario.Scenario;
import scenario.cargo.Slot;
import scenario.fleet.Vessel;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFactory;
import scenario.schedule.SchedulePackage;
import scenario.schedule.Sequence;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IAnnotations;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * A utility class for turning an annotated solution into some EMF
 * representation, for the presentation layer.
 * 
 * @author hinton
 * 
 */
public class AnnotatedSolutionExporter {
	private Map<String, IAnnotationExporter> exporters;
	final ScheduleFactory factory = SchedulePackage.eINSTANCE
			.getScheduleFactory();

	public AnnotatedSolutionExporter() {
		exporters.put(
				SchedulerConstants.AI_idleInfo,
				new IdleEventExporter());
		exporters.put(
				SchedulerConstants.AI_journeyInfo,
				new JourneyEventExporter());
		exporters.put(
				SchedulerConstants.AI_visitInfo,
				new VisitEventExporter());
	}
	
	public Schedule exportAnnotatedSolution(final Scenario inputScenario,
			final ModelEntityMap entities,
			final IAnnotatedSolution<ISequenceElement> annotatedSolution) {
		final IOptimisationData<ISequenceElement> data = annotatedSolution
				.getContext().getOptimisationData();
		final IVesselProvider vesselProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		@SuppressWarnings("unchecked")
		final IPortSlotProvider<ISequenceElement> portSlotProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_portSlotsProvider,
						IPortSlotProvider.class);

		final IAnnotations<ISequenceElement> elementAnnotations = annotatedSolution
				.getElementAnnotations();

		final Schedule output = factory.createSchedule();
		
		// go through the annotated solution and build stuff for the EMF;
		entities.setScenario(inputScenario);
		
		// prepare exporters
		for (final IAnnotationExporter exporter : exporters.values()) {
			exporter.setOutput(output);
			exporter.setScenario(inputScenario);
			exporter.setModelEntityMap(entities);
			exporter.setAnnotatedSolution(annotatedSolution);
			
			exporter.init();
		}
		
		final List<Sequence> sequences = output.getSequences();
		
		// Create sequences and run other exporters
		for (final Map.Entry<IResource, ISequence<ISequenceElement>> resourceAndSequence : annotatedSolution
				.getSequences().getSequences().entrySet()) {
			final Sequence eSequence = factory.createSequence();
			sequences.add(eSequence);
			
			final IVessel vessel = vesselProvider.getVessel(resourceAndSequence
					.getKey());
			eSequence.setVessel(entities.getModelObject(vessel, Vessel.class));
			final List<Slot> slots = eSequence.getSlots();
			for (final ISequenceElement element : resourceAndSequence
					.getValue()) {
				final IPortSlot slot = portSlotProvider.getPortSlot(element);
				final Slot eSlot = entities.getModelObject(slot, Slot.class);
				
				if (eSlot != null)
					slots.add(eSlot);

				for (final String key : elementAnnotations
						.getAnnotations(element)) {
					final IAnnotationExporter exporter = exporters.get(key);
					if (exporter != null) {
						final Object annotation = elementAnnotations
								.getAnnotation(element, key, Object.class);
						assert annotation != null : "You oughtn't set a null annotation on anything";
						
						exporter.exportAnnotation(element, annotation);
					}
				}
			}
		}
		
		return output;
	}
}
