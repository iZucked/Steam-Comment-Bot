/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
import com.mmxlabs.models.lng.transformer.export.exporters.CooldownExporter;
import com.mmxlabs.models.lng.transformer.export.exporters.GeneratedCharterOutEventExporter;
import com.mmxlabs.models.lng.transformer.export.exporters.IdleEventExporter;
import com.mmxlabs.models.lng.transformer.export.exporters.JourneyEventExporter;
import com.mmxlabs.models.lng.transformer.export.exporters.VisitEventExporter;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.optimiser.core.IElementAnnotationsMap;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.SplitCharterOutVesselEventEndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.SplitCharterOutVesselEventStartPortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence.HeelRecord;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence.HeelValueRecord;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.fitness.util.SequenceEvaluationUtils;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

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
	private VisitEventExporter portDetailsExporter;

	@Inject
	private JourneyEventExporter journeyDetailsExporter;

	@Inject
	private IdleEventExporter idleDetailsExporter;

	@Inject
	private GeneratedCharterOutEventExporter gcoDetailsExporter;

	@Inject
	private CooldownExporter cooldownDetailsExporter;

	@Inject
	private Injector injector;

	private boolean exportRuntimeAndFitness = false;

	public boolean isExportRuntimeAndFitness() {
		return exportRuntimeAndFitness;
	}

	public void setExportRuntimeAndFitness(final boolean exportRuntimeAndFitness) {
		this.exportRuntimeAndFitness = exportRuntimeAndFitness;
	}

	public void addExporterExtension(final IExporterExtension extension) {
		extensions.add(extension);
	}

	/**
	 */
	public Schedule exportAnnotatedSolution(final @NonNull ModelEntityMap modelEntityMap, final @NonNull IAnnotatedSolution annotatedSolution) {
		final IElementAnnotationsMap elementAnnotations = annotatedSolution.getElementAnnotations();
		final Schedule output = factory.createSchedule();
		assert output != null;
		// get domain level sequences
		final VolumeAllocatedSequences scheduledSequences = annotatedSolution.getEvaluationState().getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);

		// go through the annotated solution and build stuff for the EMF;

		for (final IExporterExtension extension : extensions) {
			extension.startExporting(output, modelEntityMap, annotatedSolution);
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

		for (final IResource resource : resources) {
			assert resource != null;
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

			final Sequence eSequence = factory.createSequence();

			boolean isFOBSequence = false;
			boolean isDESSequence = false;
			boolean isRoundTripSequence = false;
			final ISequence sequence = annotatedSolution.getFullSequences().getSequence(resource);
			final VolumeAllocatedSequence scheduledSequence = scheduledSequences.getScheduledSequenceForResource(resource);
			switch (vesselAvailability.getVesselInstanceType()) {
			case TIME_CHARTER:
			case FLEET:
				eSequence.setSequenceType(SequenceType.VESSEL);
				eSequence.setVesselAvailability(modelEntityMap.getModelObjectNullChecked(vesselAvailability, VesselAvailability.class));
				eSequence.unsetCharterInMarket();
				if (vesselAvailability.isOptional() && SequenceEvaluationUtils.shouldIgnoreSequence(sequence)) {
					continue;
				}
				break;
			case FOB_SALE:
				fobSequence.setSequenceType(SequenceType.FOB_SALE);
				isFOBSequence = true;
				// Skip and process differently
				if (SequenceEvaluationUtils.shouldIgnoreSequence(sequence)) {
					continue;
				}
				break;
			case DES_PURCHASE:
				desSequence.setSequenceType(SequenceType.DES_PURCHASE);
				isDESSequence = true;
				// Skip and process differently
				if (SequenceEvaluationUtils.shouldIgnoreSequence(sequence)) {
					continue;
				}
				break;
			case SPOT_CHARTER:
				eSequence.setSequenceType(SequenceType.SPOT_VESSEL);
				if (SequenceEvaluationUtils.shouldIgnoreSequence(sequence)) {
					continue;
				}

				eSequence.setCharterInMarket(modelEntityMap.getModelObjectNullChecked(vesselAvailability.getSpotCharterInMarket(), CharterInMarket.class));
				eSequence.unsetVesselAvailability();
				eSequence.setSpotIndex(vesselAvailability.getSpotIndex());
				break;
			case ROUND_TRIP:
				eSequence.setSequenceType(SequenceType.ROUND_TRIP);
				if (SequenceEvaluationUtils.shouldIgnoreSequence(sequence)) {
					continue;
				}
				isRoundTripSequence = true;
				eSequence.setCharterInMarket(modelEntityMap.getModelObjectNullChecked(vesselAvailability.getSpotCharterInMarket(), CharterInMarket.class));
				eSequence.unsetVesselAvailability();
				eSequence.setSpotIndex(vesselAvailability.getSpotIndex());
				break;
			default:
				break;
			}

			if (vesselAvailability.getVesselInstanceType() != VesselInstanceType.FOB_SALE && vesselAvailability.getVesselInstanceType() != VesselInstanceType.DES_PURCHASE) {
				if (eSequence.getName().equals("<no vessel>") || (eSequence.getVesselAvailability() == null && eSequence.getCharterInMarket() == null)) {
					log.error("No vessel set on sequence!?");
				}
			}

			if (!(isDESSequence || isFOBSequence || isRoundTripSequence)) {
				sequences.add(eSequence);
			}

			final boolean pIsRoundTripSequence = isRoundTripSequence;
			final boolean pIsFOBSequence = isFOBSequence;
			final boolean pIsDESSequence = isDESSequence;
			exportEvents(scheduledSequence, annotatedSolution, modelEntityMap, output, events -> {

				if (!events.isEmpty()) {

					// Setup next/prev events.
					Event prev = null;
					for (final Event event : events) {

						if (prev != null) {
							prev.setNextEvent(event);
							event.setPreviousEvent(prev);
						}
						prev = event;
					}

					if (pIsRoundTripSequence) {
						// Create new sequence, copying original data
						final Sequence thisSequence = factory.createSequence();

						thisSequence.setSequenceType(SequenceType.ROUND_TRIP);

						thisSequence.setCharterInMarket(eSequence.getCharterInMarket());
						thisSequence.unsetVesselAvailability();
						thisSequence.setSpotIndex(eSequence.getSpotIndex());
						thisSequence.getEvents().addAll(events);

						sequences.add(thisSequence);
					} else if (pIsFOBSequence) {
						fobSequence.getEvents().addAll(events);
					} else if (pIsDESSequence) {
						desSequence.getEvents().addAll(events);
					} else {
						eSequence.getEvents().addAll(events);
					}
				}
			});
		}

		if (!fobSequence.getEvents().isEmpty()) {
			sequences.add(fobSequence);
		}
		if (!desSequence.getEvents().isEmpty()) {
			sequences.add(desSequence);
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
						eventGrouping = (EndEvent) event;
						eventGrouping.getEvents().add(event);
					} else if (event instanceof VesselEventVisit) {
						eventGrouping = (VesselEventVisit) event;
						eventGrouping.getEvents().add(event);
					} else if (event instanceof GeneratedCharterOut) {
						eventGrouping = (EventGrouping) event;
						eventGrouping.getEvents().add(event);
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

	public void exportEvents(final VolumeAllocatedSequence scheduledSequence, final IAnnotatedSolution annotatedSolution, ModelEntityMap modelEntityMap, final Schedule output,
			final Consumer<List<Event>> eventsAction) {

		final List<Event> events = new LinkedList<>();

		final VoyagePlanIterator vpi = new VoyagePlanIterator(scheduledSequence);
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(scheduledSequence.getResource());
		final boolean isRoundTripSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;

		boolean exportingRedirectedCharterOut = false;
		PortDetails redirectedCharterOutStart = null;

		while (vpi.hasNextObject()) {

			final Object e = vpi.nextObject();
			final int currentTime = vpi.getCurrentTime();
			final VoyagePlan currentPlan = vpi.getCurrentPlan();

			final long charterRatePerDay = currentPlan.getCharterInRatePerDay();

			if (e instanceof PortDetails) {
				final PortDetails details = (PortDetails) e;
				final IPortSlot currentPortSlot = details.getOptions().getPortSlot();

				if (currentPortSlot instanceof SplitCharterOutVesselEventStartPortSlot) {
					redirectedCharterOutStart = details;
					exportingRedirectedCharterOut = true;
					continue;
				}
				boolean patchupRedirectCharterEvent = false;
				if (currentPortSlot instanceof SplitCharterOutVesselEventEndPortSlot) {
					assert exportingRedirectedCharterOut;
					exportingRedirectedCharterOut = false;
					// Found the end of the charter out. Export the event and then patch up start info
					patchupRedirectCharterEvent = true;
				}
				if (exportingRedirectedCharterOut) {
					// Not found the end yet, continue.
					continue;
				}
				final PortVisit event = portDetailsExporter.export(details, scheduledSequence, annotatedSolution, output);
				if (event != null) {
					// Heel tracking
					{
						final HeelValueRecord heelRecord = scheduledSequence.getPortHeelRecord(currentPortSlot);
						if (heelRecord != null) {
							event.setHeelAtStart(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtStartInM3()));
							event.setHeelAtEnd(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtEndInM3()));
							event.setHeelCost(OptimiserUnitConvertor.convertToExternalFixedCost(heelRecord.getHeelCost()));
							event.setHeelRevenue(OptimiserUnitConvertor.convertToExternalFixedCost(heelRecord.getHeelRevenue()));
						}
					}
					event.setCharterCost(OptimiserUnitConvertor.convertToExternalFixedCost(Calculator.quantityFromRateTime(charterRatePerDay, details.getOptions().getVisitDuration()) / 24L));

					if (patchupRedirectCharterEvent) {
						IPortSlot startSlot = redirectedCharterOutStart.getOptions().getPortSlot();
						final HeelValueRecord heelRecord = scheduledSequence.getPortHeelRecord(startSlot);
						if (heelRecord != null) {
							event.setHeelAtStart(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtStartInM3()));
							event.setHeelRevenue(OptimiserUnitConvertor.convertToExternalFixedCost(heelRecord.getHeelRevenue()));
						}

						((VesselEventVisit) event).setRedeliveryPort(event.getPort());
						event.setPort(modelEntityMap.getModelObject(startSlot.getPort(), Port.class));
						redirectedCharterOutStart = null;
						patchupRedirectCharterEvent = false;
					}

					events.add(event);

					final boolean thisEventIsSequenceEnd = currentPortSlot.getPortType() == PortType.Round_Trip_Cargo_End;
					if (isRoundTripSequence && thisEventIsSequenceEnd) {
						eventsAction.accept(events);
						events.clear();
					}
				}
			} else if (e instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) e;

				if (exportingRedirectedCharterOut) {
					continue;
				}

				int voyage_currentTime = currentTime;
				final Journey journey = journeyDetailsExporter.export(details, scheduledSequence, voyage_currentTime);
				voyage_currentTime += details.getTravelTime();

				Event lastEvent = null;
				if (journey != null) {
					events.add(journey);

					// Heel tracking
					final HeelRecord heelRecord = scheduledSequence.getNextTravelHeelRecord(details.getOptions().getFromPortSlot());
					if (heelRecord != null) {
						journey.setHeelAtStart(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtStartInM3()));
						journey.setHeelAtEnd(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtEndInM3()));
					}
					journey.setCharterCost(OptimiserUnitConvertor.convertToExternalFixedCost(Calculator.quantityFromRateTime(charterRatePerDay, details.getTravelTime()) / 24L));
					lastEvent = journey;
				}

				if (!details.getOptions().isCharterOutIdleTime()) {

					final Idle idle = idleDetailsExporter.export(details, scheduledSequence, voyage_currentTime);
					if (idle != null) {
						events.add(idle);

						// Heel tracking
						final HeelRecord heelRecord = scheduledSequence.getNextIdleHeelRecord(details.getOptions().getFromPortSlot());
						if (heelRecord != null) {
							idle.setHeelAtStart(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtStartInM3()));
							idle.setHeelAtEnd(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtEndInM3()));
						}
						idle.setCharterCost(OptimiserUnitConvertor.convertToExternalFixedCost(Calculator.quantityFromRateTime(charterRatePerDay, details.getIdleTime()) / 24L));
						lastEvent = idle;
					}

				} else {
					final GeneratedCharterOut event = gcoDetailsExporter.export(details, scheduledSequence, voyage_currentTime);
					if (event != null) {
						events.add(event);
						// Heel tracking
						final HeelRecord heelRecord = scheduledSequence.getNextIdleHeelRecord(details.getOptions().getFromPortSlot());
						if (heelRecord != null) {
							event.setHeelAtStart(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtStartInM3()));
							event.setHeelAtEnd(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtEndInM3()));
						}
						event.setCharterCost(OptimiserUnitConvertor.convertToExternalFixedCost(Calculator.quantityFromRateTime(charterRatePerDay, details.getIdleTime()) / 24L));
						event.setRevenue(
								OptimiserUnitConvertor.convertToExternalFixedCost(Calculator.quantityFromRateTime(details.getOptions().getCharterOutDailyRate(), details.getIdleTime()) / 24L));
						lastEvent = event;
					}
				}
				voyage_currentTime += details.getIdleTime();

				final Cooldown cooldown = cooldownDetailsExporter.export(details, scheduledSequence, voyage_currentTime);
				if (cooldown != null) {
					events.add(cooldown);
					if (lastEvent != null) {
						// Is this really needed - if we have a cooldown, then by definition heel should be zero.
						cooldown.setHeelAtStart(lastEvent.getHeelAtStart());
						cooldown.setHeelAtEnd(lastEvent.getHeelAtEnd());
					}
				}
			}
		}

		if (!events.isEmpty()) {
			eventsAction.accept(events);
		}
	}
}
