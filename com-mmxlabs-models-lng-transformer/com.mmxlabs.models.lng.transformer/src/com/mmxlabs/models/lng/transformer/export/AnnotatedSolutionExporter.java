/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.optimiser.core.IElementAnnotationsMap;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
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

	@NonNull
	private final List<IAnnotationExporter> exporters = new LinkedList<IAnnotationExporter>();

	private final ScheduleFactory factory = SchedulePackage.eINSTANCE.getScheduleFactory();

	@Inject
	private IVesselProvider vesselProvider;
	@Inject
	@NonNull
	private IPortSlotProvider portSlotProvider;

	@Inject
	@NonNull
	private List<IExporterExtension> extensions;

	@Inject
	@NonNull
	private Injector injector;

	private boolean exportRuntimeAndFitness = false;

	public boolean isExportRuntimeAndFitness() {
		return exportRuntimeAndFitness;
	}

	public void setExportRuntimeAndFitness(final boolean exportRuntimeAndFitness) {
		this.exportRuntimeAndFitness = exportRuntimeAndFitness;
	}

	public AnnotatedSolutionExporter() {
	}

	@Inject
	public void init() {
		final VisitEventExporter visitExporter = new VisitEventExporter();
		exporters.add(new IdleEventExporter(visitExporter));
		exporters.add(new CooldownExporter(visitExporter));
		exporters.add(new JourneyEventExporter());
		exporters.add(new GeneratedCharterOutEventExporter(visitExporter));
		exporters.add(visitExporter);

		for (final IAnnotationExporter ext : exporters) {
			injector.injectMembers(ext);
		}
	}

	public void addExporterExtension(final IExporterExtension extension) {
		extensions.add(extension);
	}

	/**
	 */
	public Schedule exportAnnotatedSolution(final @NonNull ModelEntityMap modelEntityMap, final @NonNull IAnnotatedSolution annotatedSolution) {
		final IElementAnnotationsMap elementAnnotations = annotatedSolution.getElementAnnotations();
		final Schedule output = factory.createSchedule();
		// get domain level sequences
		final VolumeAllocatedSequences scheduledSequences = annotatedSolution.getEvaluationState().getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);

		// go through the annotated solution and build stuff for the EMF;

		for (final IExporterExtension extension : extensions) {
			extension.startExporting(output, modelEntityMap, annotatedSolution);
		}

		// prepare exporters
		for (final IAnnotationExporter exporter : exporters) {
			// injector.injectMembers(exporter);
			exporter.setOutput(output);
			exporter.setModelEntityMap(modelEntityMap);
			exporter.setAnnotatedSolution(annotatedSolution);

			exporter.init();
		}

		// TODO: Generate an unused element exporter interface etc.
		final OpenSlotExporter openSlotExporter = new OpenSlotExporter();
		{
			injector.injectMembers(openSlotExporter);
			openSlotExporter.setOutput(output);
			openSlotExporter.setModelEntityMap(modelEntityMap);
			openSlotExporter.setAnnotatedSolution(annotatedSolution);
		}
		final MarkToMarketExporter mtmExporter = new MarkToMarketExporter();
		{
			injector.injectMembers(mtmExporter);
			mtmExporter.setOutput(output);
			mtmExporter.setModelEntityMap(modelEntityMap);
			mtmExporter.setAnnotatedSolution(annotatedSolution);
		}

		final List<Sequence> sequences = output.getSequences();
		final Iterable<IResource> resources = annotatedSolution.getFullSequences().getResources();
		// Create sequences and run other exporters

		final Sequence fobSequence = factory.createSequence();
		final Sequence desSequence = factory.createSequence();
		final Map<String, Long> desFitnessMap = new LinkedHashMap<String, Long>();
		final Map<String, Long> fobFitnessMap = new LinkedHashMap<String, Long>();

		for (final IResource resource : resources) {
			assert resource != null;
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

			final Sequence eSequence = factory.createSequence();

			boolean isFOBSequence = false;
			boolean isDESSequence = false;

			final ISequence sequence = annotatedSolution.getFullSequences().getSequence(resource);
			final VolumeAllocatedSequence scheduledSequence = scheduledSequences.getScheduledSequenceForResource(resource);
			switch (vesselAvailability.getVesselInstanceType()) {
			case TIME_CHARTER:
			case FLEET:
				eSequence.setSequenceType(SequenceType.VESSEL);
				eSequence.setVesselAvailability(modelEntityMap.getModelObjectNullChecked(vesselAvailability, VesselAvailability.class));
				eSequence.unsetCharterInMarket();
				break;
			case FOB_SALE:
				fobSequence.setSequenceType(SequenceType.FOB_SALE);
				isFOBSequence = true;
				// Skip and process differently
				if (sequence.size() < 2) {
					continue;
				}
				break;
			case DES_PURCHASE:
				desSequence.setSequenceType(SequenceType.DES_PURCHASE);
				isDESSequence = true;
				// Skip and process differently
				if (sequence.size() < 2) {
					continue;
				}
				break;
			case SPOT_CHARTER:
				eSequence.setSequenceType(SequenceType.SPOT_VESSEL);
				if (sequence.size() < 2)
					continue;

				eSequence.setCharterInMarket(modelEntityMap.getModelObjectNullChecked(vesselAvailability.getSpotCharterInMarket(), CharterInMarket.class));
				eSequence.unsetVesselAvailability();
				eSequence.setSpotIndex(vesselAvailability.getSpotIndex());
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

			if (vesselAvailability.getVesselInstanceType() != VesselInstanceType.FOB_SALE && vesselAvailability.getVesselInstanceType() != VesselInstanceType.DES_PURCHASE) {
				if (eSequence.getName().equals("<no vessel>") || (eSequence.getVesselAvailability() == null && eSequence.getCharterInMarket() == null)) {
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
					if (arg0 instanceof StartEvent) {
						return -1;
					} else if (arg1 instanceof StartEvent) {
						return 1;
					}
					
					if (arg0 instanceof EndEvent) {
						return 1;
					} else if (arg1 instanceof EndEvent) {
						return -1;
					}
					if (arg0.getStart().isBefore(arg1.getStart())) {
						return -1;
					} else if (arg0.getStart().isAfter(arg1.getStart())) {
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
			final List<IPortSlot> sequencePortSlots = scheduledSequence.getSequenceSlots();
			for (int i = 0; i < sequencePortSlots.size(); ++i) {
				final IPortSlot scheduledSlot = sequencePortSlots.get(i);
				assert scheduledSlot != null;
				final ISequenceElement element = portSlotProvider.getElement(scheduledSlot);
				// get annotations for this element
				final Map<String, IElementAnnotation> annotations = elementAnnotations.getAnnotations(element);

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

		// Fix up start events with no port.
		fixUpStartEventPorts(output);
		// patch up idle events with no port
		fixUpFirstIdleEventPorts(output);
		// Fix up end events with no port.
		fixUpEndEventPorts(output);
		// Correct the timezone of event dates
		fixUpEventTimezones(output);

		// now patch up laden/ballast journey references in the cargoes
		for (final Sequence eSequence : output.getSequences()) {
			EventGrouping eventGrouping = null;
			for (final Event event : eSequence.getEvents()) {
				if (event instanceof PortVisit) {
					if (event instanceof SlotVisit) {
						final SlotVisit visit = (SlotVisit) event;
						final CargoAllocation allocation = visit.getSlotAllocation().getCargoAllocation();
						allocation.setSequence(eSequence);
						eventGrouping = allocation;
						eventGrouping.getEvents().add(event);
					} else if (event instanceof StartEvent) {
						eventGrouping = (StartEvent) event;
						eventGrouping.getEvents().add(event);
					} else if (event instanceof EndEvent) {
						eventGrouping = null;
					} else if (event instanceof VesselEventVisit) {
						eventGrouping = (VesselEventVisit) event;
						eventGrouping.getEvents().add(event);
					} else if (event instanceof GeneratedCharterOut) {
						eventGrouping = (EventGrouping) event;
					} else {
						eventGrouping = null;
					}
				} else if (event instanceof Journey && eventGrouping != null) {
					eventGrouping.getEvents().add(event);
				} else if (event instanceof Idle && eventGrouping != null) {
					eventGrouping.getEvents().add(event);
				} else if (event instanceof Cooldown && eventGrouping != null) {
					eventGrouping.getEvents().add(event);
				}
			}
		}

		for (final ISequenceElement element : annotatedSolution.getFullSequences().getUnusedElements()) {
			assert element != null;
			final IPortSlot slot = portSlotProvider.getPortSlot(element);
			final Slot modelSlot = modelEntityMap.getModelObject(slot, Slot.class);
			if (slot != null) {
				output.getUnusedElements().add(modelSlot);
			}
			final Map<String, IElementAnnotation> annotations = elementAnnotations.getAnnotations(element);
			openSlotExporter.export(element, annotations);
			// mtmExporter.export(element, annotations);
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

	protected void fixUpEventTimezones(final Schedule output) {
		for (final Sequence eSequence : output.getSequences()) {
			// Idle firstIdle = null;
			for (final Event event : eSequence.getEvents()) {
				final Port port = event.getPort();
				if (port != null) {
					final ZoneId zone = ZoneId.of(port.getTimeZone());
					event.setStart(event.getStart().withZoneSameInstant(zone));
					if (event instanceof Journey) {
						final Journey journey = (Journey) event;
						final ZoneId eZone = ZoneId.of(journey.getDestination().getTimeZone());
						journey.setEnd(journey.getEnd().withZoneSameInstant(eZone));
					} else {
						event.setEnd(event.getEnd().withZoneSameInstant(zone));
					}
				}
			}
		}
	}

	/**
	 * Fix up first idle events missing a port - typically a result of missing start event ports.
	 * 
	 * @param output
	 */
	private void fixUpFirstIdleEventPorts(final Schedule output) {
		for (final Sequence eSequence : output.getSequences()) {
			Idle firstIdle = null;
			for (final Event event : eSequence.getEvents()) {
				if (firstIdle != null && firstIdle.getPort() != null) {
					break;
				}
				if (event instanceof Idle) {
					firstIdle = (Idle) event;
				}
				final Port port = event.getPort();
				if (firstIdle != null && firstIdle.getPort() == null && port != null) {
					firstIdle.setPort(port);
				}
			}
		}
	}

	/**
	 * Fix up {@link StartEvent}s with no start port.
	 * 
	 * @param schedule
	 */
	private void fixUpStartEventPorts(final Schedule schedule) {
		for (final Sequence sequence : schedule.getSequences()) {
			if (sequence.getEvents().isEmpty()) {
				continue;
			}
			final Event event = sequence.getEvents().get(0);
			if (event instanceof StartEvent) {
				final StartEvent startEvent = (StartEvent) event;
				if (startEvent.getPort() == null) {
					final Event nextEvent = startEvent.getNextEvent();
					final Port port = nextEvent.getPort();
					if (port != null) {
						startEvent.setPort(port);
					}
				}
			}
		}
	}

	/**
	 * Fix up {@link EndEvent}s with no start port.
	 * 
	 * @param schedule
	 */
	private void fixUpEndEventPorts(final Schedule schedule) {
		for (final Sequence sequence : schedule.getSequences()) {
			if (sequence.getEvents().isEmpty()) {
				continue;
			}
			final Event event = sequence.getEvents().get(sequence.getEvents().size() - 1);
			if (event instanceof EndEvent) {
				final EndEvent endEvent = (EndEvent) event;
				if (endEvent.getPort() == null) {
					final Event prevEvent = endEvent.getPreviousEvent();
					final Port port = prevEvent.getPort();
					if (port != null) {
						endEvent.setPort(port);
					}
				}
			}
		}
	}
}
