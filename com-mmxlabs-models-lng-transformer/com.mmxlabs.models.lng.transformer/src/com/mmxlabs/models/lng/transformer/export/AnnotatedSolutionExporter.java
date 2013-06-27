/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import org.eclipse.emf.common.util.EList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
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
	private final ScheduleFactory factory = SchedulePackage.eINSTANCE.getScheduleFactory();

	@Inject
	private List<IExporterExtension> extensions = new LinkedList<IExporterExtension>();

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
		exporters.add(new GeneratedCharterOutEventExporter(visitExporter));
		exporters.add(visitExporter);
	}

	public void addExporterExtension(final IExporterExtension extension) {
		extensions.add(extension);
	}

	/**
	 * @since 3.0
	 */
	public Schedule exportAnnotatedSolution(final ModelEntityMap entities, final IAnnotatedSolution annotatedSolution) {
		final IOptimisationData data = annotatedSolution.getContext().getOptimisationData();
		final IVesselProvider vesselProvider = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		final IAnnotations elementAnnotations = annotatedSolution.getElementAnnotations();
		final IPortSlotProvider portSlotProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);
		final Schedule output = factory.createSchedule();

		// go through the annotated solution and build stuff for the EMF;

		for (final IExporterExtension extension : extensions) {
			extension.startExporting(output, entities, annotatedSolution);
		}

		// prepare exporters
		for (final IAnnotationExporter exporter : exporters) {
			exporter.setOutput(output);
			exporter.setModelEntityMap(entities);
			exporter.setAnnotatedSolution(annotatedSolution);

			exporter.init();
		}

		final List<Sequence> sequences = output.getSequences();
		final List<IResource> resources = annotatedSolution.getSequences().getResources();
		// Create sequences and run other exporters

		final Sequence fobSequence = factory.createSequence();
		final Sequence desSequence = factory.createSequence();
		final Map<String, Long> desFitnessMap = new LinkedHashMap<String, Long>();
		final Map<String, Long> fobFitnessMap = new LinkedHashMap<String, Long>();

		final Map<IVesselClass, AtomicInteger> counter = new HashMap<IVesselClass, AtomicInteger>();
		for (final IResource resource : resources) {
			final IVessel vessel = vesselProvider.getVessel(resource);

			final Sequence eSequence = factory.createSequence();

			boolean skipStartEndElements = false;
			boolean isFOBSequence = false;
			boolean isDESSequence = false;

			// TODO use spot rates correctly.
			final ICurve hireRate;
			switch (vessel.getVesselInstanceType()) {
			case SPOT_CHARTER:
				hireRate = vessel.getHourlyCharterInPrice();
				break;
			case TIME_CHARTER:
				hireRate = vessel.getHourlyCharterInPrice();
				break;
			default:
				hireRate = null;
				break;
			}

			final ISequence sequence = annotatedSolution.getSequences().getSequence(resource);
			switch (vessel.getVesselInstanceType()) {
			case TIME_CHARTER:
			case FLEET:
				eSequence.setSequenceType(SequenceType.VESSEL);
				eSequence.setVesselAvailability(entities.getModelObject(vessel, VesselAvailability.class));
				eSequence.unsetVesselClass();
				break;
			case FOB_SALE:
				fobSequence.setSequenceType(SequenceType.FOB_SALE);
				isFOBSequence = true;
				// Skip and process differently
				if (sequence.size() < 4) {
					continue;
				}

				skipStartEndElements = true;
				break;
			case DES_PURCHASE:
				desSequence.setSequenceType(SequenceType.DES_PURCHASE);
				isDESSequence = true;
				// Skip and process differently
				if (sequence.size() < 4) {
					continue;
				}

				skipStartEndElements = true;
				break;
			case SPOT_CHARTER:
				eSequence.setSequenceType(SequenceType.SPOT_VESSEL);
				if (sequence.size() < 2)
					continue;

				eSequence.setVesselClass(entities.getModelObject(vessel.getVesselClass(), VesselClass.class));
				eSequence.unsetVesselAvailability();
				final AtomicInteger ai = counter.get(vessel.getVesselClass());
				int ix = 0;

				if (ai == null) {
					counter.put(vessel.getVesselClass(), new AtomicInteger(ix));
				} else {
					ix = ai.incrementAndGet();
				}

				eSequence.setSpotIndex(ix);
				break;
			case CARGO_SHORTS:
				eSequence.setSequenceType(SequenceType.CARGO_SHORTS);
				if (sequence.size() < 2) {
					continue;
				}

				eSequence.unsetVesselAvailability();
				break;
			default:
				break;
			}

			if (vessel.getVesselInstanceType() != VesselInstanceType.FOB_SALE && vessel.getVesselInstanceType() != VesselInstanceType.DES_PURCHASE) {
				if (eSequence.getName().equals("<no vessel>") || (eSequence.getVesselAvailability() == null && eSequence.getVesselClass() == null)) {
					log.error("No vessel set on sequence!?");
				}
			}

			if (!(isDESSequence || isFOBSequence)) {
				sequences.add(eSequence);
			}
			{
				// set sequence fitness values
				@SuppressWarnings("unchecked")
				final Map<IResource, Map<String, Long>> sequenceFitnesses = annotatedSolution.getGeneralAnnotation(SchedulerConstants.G_AI_fitnessPerRoute, Map.class);

				if (sequenceFitnesses != null) {
					final EList<Fitness> eSequenceFitness = eSequence.getFitnesses();
					final Map<String, Long> sequenceFitness = sequenceFitnesses.get(resource);
					for (final Map.Entry<String, Long> e : sequenceFitness.entrySet()) {

						if (isDESSequence || isFOBSequence) {
							final Map<String, Long> m = isFOBSequence ? fobFitnessMap : desFitnessMap;
							long value = e.getValue();
							if (m.containsKey(e.getKey())) {
								value += m.get(e.getKey()).longValue();
							}
							m.put(e.getKey(), value);
						} else {
							final Fitness sf = ScheduleFactory.eINSTANCE.createFitness();
							sf.setName(e.getKey());
							sf.setFitnessValue(e.getValue());
							eSequenceFitness.add(sf);
						}
					}

				}
			}

			final EList<Event> events;
			if (isDESSequence) {
				events = desSequence.getEvents();
			} else if (isFOBSequence) {
				events = fobSequence.getEvents();
			} else {
				events = eSequence.getEvents();
			}
			
			// create a comparator to allow sorting of events
			final Comparator<Event> eventComparator = new Comparator<Event>() {
				@Override
				public int compare(final Event arg0, final Event arg1) {
					if (arg0.getStart().before(arg1.getStart())) {
						return -1;
					} else if (arg0.getStart().after(arg1.getStart())) {
						return 1;
					}

					// Sort by Journey -> idle -> PortVisit
					if (arg0 instanceof Journey) {
						if (arg1 instanceof Journey) {
							return 0;
						}
						if (arg1 instanceof Idle) {
							return -1;
						}
						if (arg1 instanceof PortVisit) {
							return -1;
						}
					}

					else if (arg0 instanceof Idle) {
						if (arg1 instanceof Journey) {
							return 1;
						}
						if (arg1 instanceof Idle) {
							return 0;
						}
						if (arg1 instanceof PortVisit) {
							return -1;
						}
					} else if (arg0 instanceof PortVisit) {
						if (arg1 instanceof Journey) {
							return 1;
						}
						if (arg1 instanceof Idle) {
							return 1;
						}
						if (arg1 instanceof PortVisit) {
							return 0;
						}
					}

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

			// TODO: this should be exported rather than recalculated.
			int startTime = entities.getHoursFromDate(events.get(0).getStart());

			if (hireRate != null) {
				int rate = (int) hireRate.getValueAtPoint(startTime);
				eSequence.setDailyHireRate((rate * 24) / 1000);
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

		if (!fobSequence.getEvents().isEmpty()) {
			sequences.add(fobSequence);

			final EList<Fitness> fobSequenceFitness = fobSequence.getFitnesses();
			for (final Map.Entry<String, Long> e : fobFitnessMap.entrySet()) {

				final Fitness sf = ScheduleFactory.eINSTANCE.createFitness();
				sf.setName(e.getKey());
				sf.setFitnessValue(e.getValue());
				fobSequenceFitness.add(sf);
			}
		}
		if (!desSequence.getEvents().isEmpty()) {
			sequences.add(desSequence);
			final EList<Fitness> desSequenceFitness = desSequence.getFitnesses();
			for (final Map.Entry<String, Long> e : desFitnessMap.entrySet()) {

				final Fitness sf = ScheduleFactory.eINSTANCE.createFitness();
				sf.setName(e.getKey());
				sf.setFitnessValue(e.getValue());
				desSequenceFitness.add(sf);
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
				if (event instanceof PortVisit) {
					if (event instanceof SlotVisit) {
						final SlotVisit visit = (SlotVisit) event;
						allocation = visit.getSlotAllocation().getCargoAllocation();
						allocation.setSequence(eSequence);
						allocation.getEvents().add(event);

					} else {
						allocation = null;
					}
				} else if (event instanceof Journey && allocation != null) {
					allocation.getEvents().add(event);
				} else if (event instanceof Idle && allocation != null) {
					allocation.getEvents().add(event);
				} else if (event instanceof Cooldown && allocation != null) {
					allocation.getEvents().add(event);
				}
			}
		}

		for (final ISequenceElement element : annotatedSolution.getSequences().getUnusedElements()) {
			final IPortSlot slot = portSlotProvider.getPortSlot(element);
			final Slot modelSlot = entities.getModelObject(slot, Slot.class);
			if (slot != null) {
				output.getUnusedElements().add(modelSlot);
			}
		}

		@SuppressWarnings("unchecked")
		final Map<String, Long> fitnesses = annotatedSolution.getGeneralAnnotation(OptimiserConstants.G_AI_fitnessComponents, Map.class);
		if (fitnesses != null) {
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
		}

		for (final IExporterExtension extension : extensions) {
			extension.finishExporting();
		}

		return output;
	}
}
