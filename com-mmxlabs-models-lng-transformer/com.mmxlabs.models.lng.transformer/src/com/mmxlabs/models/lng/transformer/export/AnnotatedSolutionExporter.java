/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Purge;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.IOutputScheduleProcessor;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.exporters.CharterLengthEventExporter;
import com.mmxlabs.models.lng.transformer.export.exporters.CooldownExporter;
import com.mmxlabs.models.lng.transformer.export.exporters.IdleEventExporter;
import com.mmxlabs.models.lng.transformer.export.exporters.JourneyEventExporter;
import com.mmxlabs.models.lng.transformer.export.exporters.PurgeExporter;
import com.mmxlabs.models.lng.transformer.export.exporters.VisitEventExporter;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.optimiser.core.IElementAnnotationsMap;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.SplitCharterOutVesselEventEndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.SplitCharterOutVesselEventStartPortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.HeelRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.HeelValueRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.fitness.util.SequenceEvaluationUtils;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.ExplicitIdleTime;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

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
	private static final Logger LOG = LoggerFactory.getLogger(AnnotatedSolutionExporter.class);

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
	private CharterLengthEventExporter charterLengthEventExporter;

	@Inject
	private CooldownExporter cooldownDetailsExporter;

	@Inject
	private PurgeExporter purgeDetailsExporter;

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
		final ProfitAndLossSequences profitAndLossSequences = annotatedSolution.getEvaluationState().getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);

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
		assert sequences.isEmpty();
		final Iterable<IResource> resources = annotatedSolution.getFullSequences().getResources();
		// Create sequences and run other exporters

		final Sequence fobSequence = factory.createSequence();
		final Sequence desSequence = factory.createSequence();

		for (final IResource resource : resources) {
			assert resource != null;
			final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);

			final Sequence eSequence = factory.createSequence();

			boolean isFOBSequence = false;
			boolean isDESSequence = false;
			boolean isRoundTripSequence = false;
			final ISequence sequence = annotatedSolution.getFullSequences().getSequence(resource);
			final VolumeAllocatedSequence scheduledSequence = profitAndLossSequences.getScheduledSequenceForResource(resource);
			assert scheduledSequence != null;

			switch (vesselCharter.getVesselInstanceType()) {
			case TIME_CHARTER, FLEET:
				eSequence.setSequenceType(SequenceType.VESSEL);
				eSequence.setVesselCharter(modelEntityMap.getModelObjectNullChecked(vesselCharter, VesselCharter.class));
				eSequence.unsetCharterInMarket();
				if (SequenceEvaluationUtils.shouldIgnoreSequence(sequence, vesselCharter)) {
					continue;
				}
				break;
			case FOB_SALE:
				fobSequence.setSequenceType(SequenceType.FOB_SALE);
				isFOBSequence = true;
				// Skip and process differently
				if (SequenceEvaluationUtils.shouldIgnoreSequence(sequence, vesselCharter)) {
					continue;
				}
				break;
			case DES_PURCHASE:
				desSequence.setSequenceType(SequenceType.DES_PURCHASE);
				isDESSequence = true;
				// Skip and process differently
				if (SequenceEvaluationUtils.shouldIgnoreSequence(sequence, vesselCharter)) {
					continue;
				}
				break;
			case SPOT_CHARTER:
				eSequence.setSequenceType(SequenceType.SPOT_VESSEL);
				if (SequenceEvaluationUtils.shouldIgnoreSequence(sequence, vesselCharter)) {
					continue;
				}

				eSequence.setCharterInMarket(modelEntityMap.getModelObjectNullChecked(vesselCharter.getSpotCharterInMarket(), CharterInMarket.class));
				eSequence.setCharterInMarketOverride(modelEntityMap.getModelObject(vesselCharter, CharterInMarketOverride.class));
				eSequence.unsetVesselCharter();
				eSequence.setSpotIndex(vesselCharter.getSpotIndex());
				break;
			case ROUND_TRIP:
				eSequence.setSequenceType(SequenceType.ROUND_TRIP);
				if (SequenceEvaluationUtils.shouldIgnoreSequence(sequence, vesselCharter)) {
					continue;
				}
				isRoundTripSequence = true;
				eSequence.setCharterInMarket(modelEntityMap.getModelObjectNullChecked(vesselCharter.getSpotCharterInMarket(), CharterInMarket.class));
				eSequence.unsetVesselCharter();
				eSequence.setSpotIndex(vesselCharter.getSpotIndex());
				break;
			default:
				break;
			}

			if (vesselCharter.getVesselInstanceType() != VesselInstanceType.FOB_SALE && vesselCharter.getVesselInstanceType() != VesselInstanceType.DES_PURCHASE) {
				if (eSequence.getName().equals("<no vessel>") || (eSequence.getVesselCharter() == null && eSequence.getCharterInMarket() == null)) {
					LOG.error("No vessel set on sequence!?");
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
						thisSequence.unsetVesselCharter();
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
					if (event instanceof final SlotVisit visit) {
						final CargoAllocation allocation = visit.getSlotAllocation().getCargoAllocation();
						allocation.setSequence(eSequence);
						eventGrouping = allocation;
						eventGrouping.getEvents().add(event);
					} else if (event instanceof final StartEvent evt) {
						eventGrouping = evt;
						eventGrouping.getEvents().add(event);
					} else if (event instanceof final EndEvent evt) {
						eventGrouping = evt;
						eventGrouping.getEvents().add(event);
					} else if (event instanceof final VesselEventVisit evt) {
						eventGrouping = evt;
						eventGrouping.getEvents().add(event);
					} else if (event instanceof final GeneratedCharterOut evt) {
						eventGrouping = evt;
						eventGrouping.getEvents().add(event);
					} else if (event instanceof final GeneratedCharterLengthEvent evt) {
						eventGrouping = evt;
						eventGrouping.getEvents().add(event);
					} else {
						eventGrouping = null;
					}
				} else if (event instanceof Journey && eventGrouping != null) {
					eventGrouping.getEvents().add(event);
				} else if (event instanceof Idle && eventGrouping != null) {
					eventGrouping.getEvents().add(event);
				} else if (event instanceof Purge && eventGrouping != null) {
					eventGrouping.getEvents().add(event);
				} else if (event instanceof Cooldown && eventGrouping != null) {
					eventGrouping.getEvents().add(event);
				}
			}
		}

		for (final ISequenceElement element : annotatedSolution.getFullSequences().getUnusedElements()) {
			assert element != null;
			final IPortSlot slot = portSlotProvider.getPortSlot(element);

			if (slot.getPortType() == PortType.Load || slot.getPortType() == PortType.Discharge) {
				final Slot<?> modelSlot = modelEntityMap.getModelObject(slot, Slot.class);
				if (slot != null) {
					output.getUnusedElements().add(modelSlot);
				}
				final Map<String, IElementAnnotation> annotations = elementAnnotations.getAnnotations(element);
				openSlotExporter.export(element, annotations);
			} else if (slot.getPortType() == PortType.CharterOut) {
				final CharterOutEvent modelSlot = modelEntityMap.getModelObject(slot, CharterOutEvent.class);
				output.getUnusedElements().add(modelSlot);
				final Map<String, IElementAnnotation> annotations = elementAnnotations.getAnnotations(element);
				openSlotExporter.export(element, annotations);
			}
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

//		exportNonShippedSchedules(modelEntityMap, annotatedSolution, output);

		for (final IExporterExtension extension : extensions) {
			extension.finishExporting();
		}

		final Key<List<IOutputScheduleProcessor>> key = Key.get(new TypeLiteral<List<IOutputScheduleProcessor>>() {
		});

		try {
			final Iterable<IOutputScheduleProcessor> scheduleProcessors = injector.getInstance(key);
			for (final IOutputScheduleProcessor processor : scheduleProcessors) {
				processor.process(output);
			}
		} catch (final ConfigurationException e) {
		}

		return output;
	}

	protected void fixUpEventTimezones(final @NonNull Schedule output) {
		for (final Sequence eSequence : output.getSequences()) {
			for (final Event event : eSequence.getEvents()) {
				final Port port = event.getPort();
				if (port != null) {
					final ZoneId zone = port.getZoneId();
					event.setStart(event.getStart().withZoneSameInstant(zone));
					if (event instanceof final Journey journey) {
						final ZoneId eZone = journey.getDestination().getZoneId();
						journey.setEnd(journey.getEnd().withZoneSameInstant(eZone));
					} else if (event instanceof final VesselEventVisit vesselEventVisit) {
						final Port redeliveryPort = vesselEventVisit.getRedeliveryPort();
						if (redeliveryPort != null) {
							final ZoneId eZone = redeliveryPort.getZoneId();
							vesselEventVisit.setEnd(vesselEventVisit.getEnd().withZoneSameInstant(eZone));
						} else {
							event.setEnd(event.getEnd().withZoneSameInstant(zone));
						}
					} else {
						event.setEnd(event.getEnd().withZoneSameInstant(zone));
					}
				}
			}
		}
	}

	/**
	 * Fix up first idle events missing a port - typically a result of missing start
	 * event ports.
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
				if (event instanceof final Idle idle) {
					firstIdle = idle;
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
			if (event instanceof final StartEvent startEvent) {
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
			if (event instanceof final EndEvent endEvent) {
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

	public void exportEvents(final VolumeAllocatedSequence scheduledSequence, final IAnnotatedSolution annotatedSolution, final ModelEntityMap modelEntityMap, final Schedule output,
			final Consumer<List<Event>> eventsAction) {

		final List<Event> events = new LinkedList<>();

		final VoyagePlanIterator vpi = new VoyagePlanIterator(scheduledSequence);
		final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(scheduledSequence.getResource());
		final boolean isRoundTripSequence = vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;
		final boolean isNonShipped = vesselCharter.getVesselInstanceType() == VesselInstanceType.FOB_SALE //
				|| vesselCharter.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE;

		boolean exportingRedirectedCharterOut = false;
		PortDetails redirectedCharterOutStart = null;
		GeneratedCharterLengthEvent charterLengthEvent = null;
		VoyagePlanRecord previous = null;

		while (vpi.hasNextObject()) {

			final Object e = vpi.nextObject();
			final int currentTime = vpi.getCurrentTime();
			final VoyagePlanRecord vpr = vpi.getCurrentPlanRecord();

			if (e instanceof final PortDetails details) {
				final IPortSlot currentPortSlot = details.getOptions().getPortSlot();

				charterLengthEvent = null;

				if (currentPortSlot instanceof SplitCharterOutVesselEventStartPortSlot) {
					redirectedCharterOutStart = details;
					exportingRedirectedCharterOut = true;
					continue;
				}
				boolean patchupRedirectCharterEvent = false;
				if (currentPortSlot instanceof SplitCharterOutVesselEventEndPortSlot) {
					assert exportingRedirectedCharterOut;
					exportingRedirectedCharterOut = false;
					// Found the end of the charter out. Export the event and then patch up start
					// info
					patchupRedirectCharterEvent = true;
				}
				if (exportingRedirectedCharterOut) {
					// Not found the end yet, continue.
					previous = vpr;
					continue;
				}

				final PortVisit event = portDetailsExporter.export(details, scheduledSequence, annotatedSolution, output);
				if (event instanceof final GeneratedCharterLengthEvent cle) {
					charterLengthEvent = cle;
				}
				if (event != null) {
					// Heel tracking
					if (!isNonShipped) {
						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(currentPortSlot).portHeelRecord;
						if (heelRecord != null) {
							event.setHeelAtStart(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtStartInM3()));
							event.setHeelAtEnd(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtEndInM3()));
						}
						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(currentPortSlot);
						if (heelValueRecord != null) {
							event.setHeelCost(OptimiserUnitConvertor.convertToExternalFixedCost(heelValueRecord.getHeelCost()));
							event.setHeelRevenue(OptimiserUnitConvertor.convertToExternalFixedCost(heelValueRecord.getHeelRevenue()));
							event.setHeelCostUnitPrice(OptimiserUnitConvertor.convertToExternalPrice(heelValueRecord.getCostUnitPrice()));
							event.setHeelRevenueUnitPrice(OptimiserUnitConvertor.convertToExternalPrice(heelValueRecord.getRevenueUnitPrice()));
						}
					}
					event.setCharterCost(OptimiserUnitConvertor.convertToExternalFixedCost(((PortDetails) e).getCharterCost()));

					if (patchupRedirectCharterEvent) {
						final IPortSlot startSlot = redirectedCharterOutStart.getOptions().getPortSlot();
						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(currentPortSlot).portHeelRecord;

						if (heelRecord != null) {
							event.setHeelAtStart(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtStartInM3()));
						}
						final HeelValueRecord heelValueRecord = previous != null ? previous.getHeelValueRecord(startSlot) : vpr.getHeelValueRecord(startSlot);

						if (heelValueRecord != null) {
							event.setHeelRevenueUnitPrice(OptimiserUnitConvertor.convertToExternalPrice(heelValueRecord.getRevenueUnitPrice()));
							event.setHeelRevenue(OptimiserUnitConvertor.convertToExternalFixedCost(heelValueRecord.getHeelRevenue()));
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
			} else if (e instanceof final VoyageDetails details) {

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
					final HeelRecord heelRecord = vpr.getNextTravelHeelRecord(details.getOptions().getFromPortSlot());
					if (heelRecord != null) {
						journey.setHeelAtStart(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtStartInM3()));
						journey.setHeelAtEnd(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtEndInM3()));
					}
					journey.setCharterCost(OptimiserUnitConvertor.convertToExternalFixedCost(details.getTravelCharterCost()));
					lastEvent = journey;
				}

				if (charterLengthEvent != null) {
					charterLengthEventExporter.update(charterLengthEvent, details, scheduledSequence, voyage_currentTime);
					// Heel tracking
					final HeelRecord heelRecord = vpr.getNextIdleHeelRecord(details.getOptions().getFromPortSlot());
					if (heelRecord != null) {
						charterLengthEvent.setHeelAtStart(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtStartInM3()));
						charterLengthEvent.setHeelAtEnd(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtEndInM3()));
					}
					charterLengthEvent.setCharterCost(OptimiserUnitConvertor.convertToExternalFixedCost(details.getIdleCharterCost()));
					lastEvent = charterLengthEvent;

				} else {
					final Idle idle = idleDetailsExporter.export(details, scheduledSequence, voyage_currentTime);
					if (idle != null) {
						events.add(idle);

						// Heel tracking
						final HeelRecord heelRecord = vpr.getNextIdleHeelRecord(details.getOptions().getFromPortSlot());
						if (heelRecord != null) {
							idle.setHeelAtStart(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtStartInM3()));
							idle.setHeelAtEnd(OptimiserUnitConvertor.convertToExternalVolume(heelRecord.getHeelAtEndInM3()));
						}
						idle.setCharterCost(OptimiserUnitConvertor.convertToExternalFixedCost(details.getIdleCharterCost()));
						lastEvent = idle;
					}
				}
				// Purge time taken out
				voyage_currentTime += details.getIdleTime();

				final Purge purge = purgeDetailsExporter.export(details, scheduledSequence, voyage_currentTime);
				if (purge != null) {
					events.add(purge);
					if (lastEvent != null) {
						purge.setHeelAtStart(lastEvent.getHeelAtEnd());
						purge.setHeelAtEnd(0);
					}
					purge.setCharterCost(OptimiserUnitConvertor.convertToExternalFixedCost(details.getPurgeCharterCost()));

					final int purgeDuration = details.getOptions().getExtraIdleTime(ExplicitIdleTime.PURGE);
					voyage_currentTime += purgeDuration;
					lastEvent = purge;
				}
				final Cooldown cooldown = cooldownDetailsExporter.export(details, scheduledSequence, voyage_currentTime);
				if (cooldown != null) {
					events.add(cooldown);
					if (lastEvent != null) {
						assert lastEvent.getHeelAtEnd() == 0;
						cooldown.setHeelAtStart(0);
						cooldown.setHeelAtEnd(0);
					}
					lastEvent = cooldown;
				}
			}
			if (!isRoundTripSequence)
				previous = vpr;
		}

		if (!events.isEmpty()) {
			eventsAction.accept(events);
		}
	}
}
