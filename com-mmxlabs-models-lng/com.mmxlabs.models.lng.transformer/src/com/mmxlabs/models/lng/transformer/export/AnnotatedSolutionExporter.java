/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IAnnotations;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * A utility class for turning an annotated solution into some EMF representation, for the presentation layer.
 * 
 * At the moment this has the simplistic idea that every annotation corresponds to a scheduled event in the EMF output, which is almost certainly not true.
 * 
 * @author hinton
 * 
 */
public class AnnotatedSolutionExporter {
	private static final Logger log = LoggerFactory.getLogger(AnnotatedSolutionExporter.class);
	private final List<IAnnotationExporter> exporters = new LinkedList<IAnnotationExporter>();
	final ScheduleFactory factory = SchedulePackage.eINSTANCE.getScheduleFactory();
	final List<IExporterExtension> extensions = new LinkedList<IExporterExtension>();

	private boolean exportRuntimeAndFitness = false;

	public boolean isExportRuntimeAndFitness() {
		return exportRuntimeAndFitness;
	}

	public void setExportRuntimeAndFitness(final boolean exportRuntimeAndFitness) {
		this.exportRuntimeAndFitness = exportRuntimeAndFitness;
	}

	public AnnotatedSolutionExporter() {
		final VisitEventExporter visitExporter = new VisitEventExporter();
		exporters.add(new IdleEventExporter(visitExporter));
		exporters.add(new CooldownExporter(visitExporter));
		exporters.add(new JourneyEventExporter());
		exporters.add(visitExporter);
	}

	public boolean addPlatformExporterExtensions() {

		// TOOD: Peaberry

		if (Platform.getExtensionRegistry() == null) {
			log.warn("addPlatformExporterExtensions() called without a platform - skipping");
			return false;
		}
		final String EXTENSION_ID = "com.mmxlabs.lngscheduler.exporter";
		final IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_ID);

		for (final IConfigurationElement e : config) {
			try {
				final Object object = e.createExecutableExtension("exporter");
				if (object instanceof IExporterExtension) {
					addExporterExtension((IExporterExtension) object);
				}
			} catch (final Exception ex) {
				log.error("Exception caught when adding platform exporter extensions", ex);
			}
		}
		return true;
	}

	public void addExporterExtension(final IExporterExtension extension) {
		extensions.add(extension);
	}

	public Schedule exportAnnotatedSolution(final MMXRootObject inputScenario, final ModelEntityMap entities, final IAnnotatedSolution annotatedSolution) {
		final IOptimisationData data = annotatedSolution.getContext().getOptimisationData();
		final IVesselProvider vesselProvider = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		final IAnnotations elementAnnotations = annotatedSolution.getElementAnnotations();

		final Schedule output = factory.createSchedule();

		// go through the annotated solution and build stuff for the EMF;
		entities.setScenario(inputScenario);

		for (final IExporterExtension extension : extensions) {
			extension.startExporting(inputScenario, output, entities, annotatedSolution);
		}

		// prepare exporters
		for (final IAnnotationExporter exporter : exporters) {
			exporter.setOutput(output);
			exporter.setScenario(inputScenario);
			exporter.setModelEntityMap(entities);
			exporter.setAnnotatedSolution(annotatedSolution);

			exporter.init();
		}

		final List<Sequence> sequences = output.getSequences();
		final List<IResource> resources = annotatedSolution.getSequences().getResources();
		// Create sequences and run other exporters

		final Map<IVesselClass, AtomicInteger> counter = new HashMap<IVesselClass, AtomicInteger>();
		@SuppressWarnings("unchecked")
		final Map<IResource, Map<String, Long>> sequenceFitnesses = annotatedSolution.getGeneralAnnotation(SchedulerConstants.G_AI_fitnessPerRoute, Map.class);
		for (final IResource resource : resources) {
			final IVessel vessel = vesselProvider.getVessel(resource);

			final Sequence eSequence = factory.createSequence();

			boolean skipStartEndElements = false;

			// TODO use spot rates correctly.
			final int hireRate;
			switch (vessel.getVesselInstanceType()) {
			case SPOT_CHARTER:
				hireRate = vessel.getHourlyCharterInPrice();
				break;
			case TIME_CHARTER:
				hireRate = vessel.getHourlyCharterInPrice();
				break;
			default:
				hireRate = 0;
				break;
			}

			eSequence.setDailyHireRate((hireRate * 24) / 1000);

			final ISequence sequence = annotatedSolution.getSequences().getSequence(resource);
			switch (vessel.getVesselInstanceType()) {
			case TIME_CHARTER:
			case FLEET:
				eSequence.setVessel(entities.getModelObject(vessel, Vessel.class));
				eSequence.unsetVesselClass();
				break;
			case VIRTUAL:
				// oops should do something here
				// Skip and process differently
				if (sequence.size() < 4) {
					continue;
				}
				skipStartEndElements = true;
				break;
			case SPOT_CHARTER:
				if (sequence.size() < 2)
					continue;

				eSequence.setVesselClass(entities.getModelObject(vessel.getVesselClass(), VesselClass.class));
				eSequence.unsetVessel();
				final AtomicInteger ai = counter.get(vessel.getVesselClass());
				int ix = 0;

				if (ai == null) {
					counter.put(vessel.getVesselClass(), new AtomicInteger(ix));
				} else {
					ix = ai.incrementAndGet();
				}

				eSequence.setSpotIndex(ix);
				break;
			default:
				break;
			}

			if (vessel.getVesselInstanceType() != VesselInstanceType.VIRTUAL) {
				if (eSequence.getName().equals("<no vessel>") || (eSequence.getVessel() == null && eSequence.getVesselClass() == null)) {
					log.error("No vessel set on sequence!?");
				}
			}

			sequences.add(eSequence);

			{
				// set sequence fitness values
				final EList<Fitness> eSequenceFitness = eSequence.getFitnesses();
				final Map<String, Long> sequenceFitness = sequenceFitnesses.get(resource);
				for (final Map.Entry<String, Long> e : sequenceFitness.entrySet()) {
					final Fitness sf = ScheduleFactory.eINSTANCE.createFitness();
					sf.setName(e.getKey());
					sf.setFitnessValue(e.getValue());
					eSequenceFitness.add(sf);
				}
			}

			final EList<Event> events = eSequence.getEvents();

			final Comparator<Event> eventComparator = new Comparator<Event>() {
				@Override
				public int compare(final Event arg0, final Event arg1) {
					if (arg0.getStart().before(arg1.getStart())) {
						return -1;
					} else if (arg0.getStart().after(arg1.getStart())) {
						return 1;
					}

					// idle must come after journey
					if (arg0 instanceof Idle)
						return (arg1 instanceof Idle) ? 0 : -1;
					if (arg1 instanceof Idle)
						return 1;

					if (arg0 instanceof Journey)
						return (arg1 instanceof Journey) ? 0 : -1;
					if (arg1 instanceof Journey)
						return 1;

					return 0;
				}
			};

			final List<Event> eventsForElement = new ArrayList<Event>();
			for (int i = 0; i < sequence.size(); ++i) {

				final ISequenceElement element = sequence.get(i);
				if (skipStartEndElements && (i == 0 || i == sequence.size() - 1)) {
					continue;
				}
				// get annotations for this element
				final Map<String, Object> annotations = elementAnnotations.getAnnotations(element);

				// filter virtual ports out here?

				for (final IAnnotationExporter exporter : exporters) {
					final Event result = exporter.export(element, annotations);
					if (result != null) {
						eventsForElement.add(result);
					}
				}

				// this is messy, but we want to be sure stuff is in the right
				// order or it won't make any sense.
				Collections.sort(eventsForElement, eventComparator);
				events.addAll(eventsForElement);
				eventsForElement.clear();
			}

			// Setup next/prev events.
			Event prev = null;
			for (final Event event : events) {
				if (prev != null) {
					prev.setNextEvent(event);
					event.setPreviousEvent(prev);
				}
				prev = event;
			}
		}

		// patch up idle events with no port
		for (final Sequence eSequence : output.getSequences()) {
			Idle firstIdle = null;
			for (final Event event : eSequence.getEvents()) {
				if (firstIdle != null && firstIdle.getPort() != null)
					break;
				if (event instanceof Idle) {
					firstIdle = (Idle) event;
				}
				if (firstIdle != null && firstIdle.getPort() == null && event.getPort() != null) {
					firstIdle.setPort(event.getPort());
				}
			}
		}

		// now patch up laden/ballast journey references in the cargoes
		for (final Sequence eSequence : output.getSequences()) {
			CargoAllocation allocation = null;
			for (final Event event : eSequence.getEvents()) {
				if (event instanceof SlotVisit) {
					final SlotVisit visit = (SlotVisit) event;
					allocation = visit.getSlotAllocation().getCargoAllocation();
					allocation.setSequence(eSequence);
				} else if (event instanceof Journey && allocation != null) {
					if (allocation.getLadenLeg() == null) {
						allocation.setLadenLeg((Journey) event);
					} else if (allocation.getBallastLeg() == null) {
						allocation.setBallastLeg((Journey) event);
					}
				} else if (event instanceof Idle && allocation != null) {
					if (allocation.getLadenIdle() == null) {
						allocation.setLadenIdle((Idle) event);
					} else if (allocation.getBallastIdle() == null) {
						allocation.setBallastIdle((Idle) event);
					}
				}
				if (allocation != null && allocation.getBallastLeg() != null && allocation.getLadenLeg() != null && allocation.getLadenIdle() != null && allocation.getBallastIdle() != null)
					allocation = null;
			}
		}

		// connect back-references to input cargoes.
		final CargoModel cargoModel = inputScenario.getSubModel(CargoModel.class);
		for (final Cargo cargo : cargoModel.getCargoes()) {
			for (final CargoAllocation allocation : output.getCargoAllocations()) {
				if (allocation.getLoadAllocation().isSetSlot() && allocation.getDischargeAllocation().isSetSlot() && allocation.getLoadAllocation().getSlot() == cargo.getLoadSlot()
						&& allocation.getDischargeAllocation().getSlot() == cargo.getDischargeSlot()) {
					allocation.setInputCargo(cargo);
				}
			}
		}

		final IPortSlotProvider portSlotProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);
		for (final ISequenceElement element : annotatedSolution.getSequences().getUnusedElements()) {
			final IPortSlot slot = portSlotProvider.getPortSlot(element);
			final Slot modelSlot = entities.getModelObject(slot, Slot.class);
			if (slot != null) {
				output.getUnusedElements().add(modelSlot);
			}
		}

		@SuppressWarnings("unchecked")
		final Map<String, Long> fitnesses = annotatedSolution.getGeneralAnnotation(OptimiserConstants.G_AI_fitnessComponents, Map.class);

		final Long runtime = annotatedSolution.getGeneralAnnotation(OptimiserConstants.G_AI_runtime, Long.class);
		final Integer iterations = annotatedSolution.getGeneralAnnotation(OptimiserConstants.G_AI_iterations, Integer.class);

		final EList<Fitness> outputFitnesses = output.getFitnesses();

		for (final Map.Entry<String, Long> entry : fitnesses.entrySet()) {
			final Fitness fitness = factory.createFitness();
			fitness.setName(entry.getKey());
			fitness.setFitnessValue(entry.getValue());
			outputFitnesses.add(fitness);
		}

		if (isExportRuntimeAndFitness()) {
			final Fitness eRuntime = factory.createFitness();
			eRuntime.setName("runtime");
			eRuntime.setFitnessValue(runtime);

			outputFitnesses.add(eRuntime);

			final Fitness eIters = factory.createFitness();
			eIters.setName("iterations");
			eIters.setFitnessValue(iterations.longValue());

			outputFitnesses.add(eIters);
		}

		for (final IExporterExtension extension : extensions) {
			extension.finishExporting();
		}

		return output;
	}
}
