package com.mmxlabs.models.lng.transformer.extensions.fobsalerotations;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.NonShippedIdle;
import com.mmxlabs.models.lng.schedule.NonShippedJourney;
import com.mmxlabs.models.lng.schedule.NonShippedSequence;
import com.mmxlabs.models.lng.schedule.NonShippedSlotVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.NonShippedScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.scheduling.MinTravelTimeData;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

@NonNullByDefault
public class FobSaleRotationExporterExtension implements IExporterExtension {

	@Inject
	private IVesselProvider vesselProvider;

	@Nullable
	private Schedule output;

	@Nullable
	private ModelEntityMap modelEntityMap;

	@Nullable
	private IAnnotatedSolution annotatedSolution;

	@Override
	public void startExporting(final Schedule output, final ModelEntityMap modelEntityMap, final IAnnotatedSolution annotatedSolution) {
		this.output = output;
		this.modelEntityMap = modelEntityMap;
		this.annotatedSolution = annotatedSolution;
	}

	@Override
	public void finishExporting() {
		if (output == null //
				|| modelEntityMap == null //
				|| annotatedSolution == null) {
			throw new IllegalStateException("Exporter not initialised correctly");
		}
		final NonShippedScheduledSequences nonShippedSequences = annotatedSolution.getEvaluationState().getData(SchedulerEvaluationProcess.NONSHIPPED_SCHEDULED_SEQUENCES, NonShippedScheduledSequences.class);
		if (nonShippedSequences != null) {
			final Map<IResource, @Nullable Pair<MinTravelTimeData, List<IPortTimesRecord>>> map = nonShippedSequences.getSchedule();
			for (final Entry<IResource, @Nullable Pair<MinTravelTimeData, List<IPortTimesRecord>>> entry : map.entrySet()) {
				@SuppressWarnings("null")
				final IResource resource = entry.getKey();
				final MinTravelTimeData travelTimeData = entry.getValue().getFirst();
				final List<IPortTimesRecord> oSequence = entry.getValue().getSecond();
				int i = 0;
				if (!oSequence.isEmpty()) {
					final NonShippedSequence eSequence = ScheduleFactory.eINSTANCE.createNonShippedSequence();
					final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
					final IVessel oVessel = vesselCharter.getVessel();
					eSequence.setVessel(modelEntityMap.getModelObjectNullChecked(oVessel, Vessel.class));
					Event previousEvent = null;
					for (final IPortTimesRecord portTimesRecord : oSequence) {
						final List<IPortSlot> oSlots = portTimesRecord.getSlots();
						if (oSlots.size() == 1) {
							++i;
							continue;
						}
						final NonShippedSlotVisit eLoadVisit = ScheduleFactory.eINSTANCE.createNonShippedSlotVisit();
						final int startTime = portTimesRecord.getSlotTime(oSlots.get(0));
						eLoadVisit.setStart(modelEntityMap.getDateFromHours(startTime, oSlots.get(0).getPort()));
						final int loadDuration = portTimesRecord.getSlotDuration(oSlots.get(0));
						final int endTime = startTime + loadDuration;
						eLoadVisit.setEnd(modelEntityMap.getDateFromHours(endTime, oSlots.get(0).getPort()));
						eLoadVisit.setPort(modelEntityMap.getModelObjectNullChecked(oSlots.get(0).getPort(), Port.class));
						eLoadVisit.setSlot(modelEntityMap.getModelObjectNullChecked(oSlots.get(0), Slot.class));
						eSequence.getEvents().add(eLoadVisit);

						if (previousEvent != null) {
							eLoadVisit.setPreviousEvent(previousEvent);
							previousEvent.setNextEvent(eLoadVisit);
						}
						previousEvent = eLoadVisit;

						final int dischargeStart = portTimesRecord.getSlotTime(oSlots.get(1));
						final int dischargeDuration = portTimesRecord.getSlotDuration(oSlots.get(1));
						final int dischargeEnd = dischargeStart + dischargeDuration;
						final int minLadentTravelTime = travelTimeData.getMinTravelTime(i++) - loadDuration;
						final int earliestLadenEnd = endTime + minLadentTravelTime;
						final NonShippedJourney ladenJourney = ScheduleFactory.eINSTANCE.createNonShippedJourney();
						ladenJourney.setLaden(true);
						ladenJourney.setStart(modelEntityMap.getDateFromHours(endTime, oSlots.get(0).getPort()));
						IPort destPort = oSlots.get(1).getPort();
						if (destPort.getName().equalsIgnoreCase("ANYWHERE")) {
							destPort = oSlots.get(0).getPort();
						}
						ladenJourney.setPort(modelEntityMap.getModelObjectNullChecked(oSlots.get(0).getPort(), Port.class));
						ladenJourney.setDestination(modelEntityMap.getModelObjectNullChecked(destPort, Port.class));
						ladenJourney.setEnd(modelEntityMap.getDateFromHours(earliestLadenEnd, destPort));
						eSequence.getEvents().add(ladenJourney);
						if (previousEvent != null) {
							ladenJourney.setPreviousEvent(previousEvent);
							previousEvent.setNextEvent(ladenJourney);
						}
						previousEvent = ladenJourney;

						if (dischargeStart != earliestLadenEnd) {
							final NonShippedIdle ladenIdle = ScheduleFactory.eINSTANCE.createNonShippedIdle();
							ladenIdle.setLaden(true);
							ladenIdle.setStart(modelEntityMap.getDateFromHours(earliestLadenEnd, destPort));
							ladenIdle.setEnd(modelEntityMap.getDateFromHours(dischargeStart, destPort));
							ladenIdle.setPort(modelEntityMap.getModelObjectNullChecked(destPort, Port.class));
							eSequence.getEvents().add(ladenIdle);
							if (previousEvent != null) {
								ladenIdle.setPreviousEvent(previousEvent);
								previousEvent.setNextEvent(ladenIdle);
							}
							previousEvent = ladenIdle;
						}

						final NonShippedSlotVisit eDischargeVisit = ScheduleFactory.eINSTANCE.createNonShippedSlotVisit();
						eDischargeVisit.setStart(modelEntityMap.getDateFromHours(dischargeStart, destPort));
						eDischargeVisit.setEnd(modelEntityMap.getDateFromHours(dischargeEnd, destPort));
						eDischargeVisit.setPort(modelEntityMap.getModelObject(destPort, Port.class));
						eDischargeVisit.setSlot(modelEntityMap.getModelObject(oSlots.get(1), Slot.class));
						eSequence.getEvents().add(eDischargeVisit);

						if (previousEvent != null) {
							eDischargeVisit.setPreviousEvent(previousEvent);
							previousEvent.setNextEvent(eDischargeVisit);
						}
						previousEvent = eDischargeVisit;

						final IPortSlot returnSlot = portTimesRecord.getReturnSlot();
						if (returnSlot != null) {
							final int nextLoadStart = portTimesRecord.getSlotTime(returnSlot);
							final int minBallastTravelTime = travelTimeData.getMinTravelTime(i++) - dischargeDuration;
							final int earliestBallastEnd = dischargeEnd + minBallastTravelTime;
							final NonShippedJourney ballastJourney = ScheduleFactory.eINSTANCE.createNonShippedJourney();
							ballastJourney.setLaden(false);
							ballastJourney.setStart(modelEntityMap.getDateFromHours(dischargeEnd, destPort));
							ballastJourney.setEnd(modelEntityMap.getDateFromHours(earliestBallastEnd, returnSlot.getPort()));
							ballastJourney.setPort(modelEntityMap.getModelObjectNullChecked(destPort, Port.class));
							ballastJourney.setDestination(modelEntityMap.getModelObjectNullChecked(returnSlot.getPort(), Port.class));
							eSequence.getEvents().add(ballastJourney);
							if (previousEvent != null) {
								ballastJourney.setPreviousEvent(previousEvent);
								previousEvent.setNextEvent(ballastJourney);
							}
							previousEvent = ballastJourney;
							if (nextLoadStart != earliestBallastEnd) {
								final NonShippedIdle ballastIdle = ScheduleFactory.eINSTANCE.createNonShippedIdle();
								ballastIdle.setLaden(false);
								ballastIdle.setPort(modelEntityMap.getModelObjectNullChecked(returnSlot.getPort(), Port.class));
								ballastIdle.setStart(modelEntityMap.getDateFromHours(earliestBallastEnd, returnSlot.getPort()));
								ballastIdle.setEnd(modelEntityMap.getDateFromHours(nextLoadStart, returnSlot.getPort()));
								eSequence.getEvents().add(ballastIdle);
								if (previousEvent != null) {
									ballastIdle.setPreviousEvent(previousEvent);
									previousEvent.setNextEvent(ballastIdle);
								}
								previousEvent = ballastIdle;
							}
						}
					}
					output.getNonShippedSequences().add(eSequence);
				}
			}
		}
	}

}
