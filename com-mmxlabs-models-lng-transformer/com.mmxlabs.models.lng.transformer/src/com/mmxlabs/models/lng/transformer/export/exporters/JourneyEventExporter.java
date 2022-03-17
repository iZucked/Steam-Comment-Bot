/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export.exporters;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.schedule.CanalJourneyEvent;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PanamaBookingPeriod;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.ExporterExtensionUtils;
import com.mmxlabs.models.lng.transformer.export.FuelExportHelper;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider.RouteOptionDirection;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

/**
 * @author hinton
 * 
 */
public class JourneyEventExporter {

	@Inject
	private ModelEntityMap modelEntityMap;

	@Inject
	private IDistanceProvider distanceProvider;
	
	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPanamaBookingsProvider panamaSlotsProvider;

	public Journey export(final VoyageDetails voyageDetails, final VolumeAllocatedSequence volumeAllocatedSequence, final int currentTime) {

		if (voyageDetails.getTravelTime() == 0 && voyageDetails.getOptions().getDistance() == 0) {
			return null; // filter out zero-length journeys
		}
		@NonNull
		final VoyageOptions options = voyageDetails.getOptions();

		@NonNull
		final IPortSlot fromPortSlot = options.getFromPortSlot();

		final Port eFromPort = modelEntityMap.getModelObject(fromPortSlot.getPort(), Port.class);
		final Port eToPort = modelEntityMap.getModelObject(options.getToPortSlot().getPort(), Port.class);

		if (eFromPort == null || eToPort == null) {
			return null;
		}

		final Journey journey = ScheduleFactory.eINSTANCE.createJourney();

		journey.setStart(modelEntityMap.getDateFromHours(currentTime, eFromPort));
		journey.setEnd(modelEntityMap.getDateFromHours(currentTime + voyageDetails.getTravelTime(), eToPort));

		journey.setPort(eFromPort);
		journey.setDestination(eToPort);

		journey.setDistance(options.getDistance());
		journey.setRouteOption(ExporterExtensionUtils.mapRouteOption(options.getRoute()));
		journey.setToll(OptimiserUnitConvertor.convertToExternalFixedCost(options.getRouteCost()));

		journey.setLaden(VesselState.Laden.equals(options.getVesselState()));

		journey.setSpeed(OptimiserUnitConvertor.convertToExternalSpeed(voyageDetails.getSpeed()));

		journey.getFuels().addAll(exportFuelData(voyageDetails));

		final IPortTimesRecord portTimesRecord = volumeAllocatedSequence.getPortTimesRecord(fromPortSlot);

		ZonedDateTime estimatedArrival = null;
		if (journey.getRouteOption() != RouteOption.DIRECT) {
			if (journey.getRouteOption() == RouteOption.PANAMA) {
				PanamaBookingPeriod exportPeriod = PanamaBookingPeriod.BEYOND;
				if (vesselProvider.getVesselAvailability(volumeAllocatedSequence.getResource()).getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
					exportPeriod = PanamaBookingPeriod.NOMINAL;
				} else {
					//PanamaPeriod period = portTimesRecord.getSlotNextVoyagePanamaPeriod(voyageDetails.getOptions().getFromPortSlot());
					//if (period != null) {
						//Only relaxed period now, others removed.
							exportPeriod = PanamaBookingPeriod.RELAXED;
				}
				journey.setCanalBookingPeriod(exportPeriod);
			}
			final IPort canalEntry = distanceProvider.getRouteOptionEntryPort(options.getFromPortSlot().getPort(), options.getRoute());

			// set latest possible canal date
			if (canalEntry != null) {
				final Port eCanalPort = modelEntityMap.getModelObject(canalEntry, Port.class);
				final int fromCanalEntry = distanceProvider.getTravelTime(ERouteOption.DIRECT, //
						voyageDetails.getOptions().getVessel(), //
						canalEntry, //
						voyageDetails.getOptions().getToPortSlot().getPort(), //
						voyageDetails.getOptions().getVessel().getMaxSpeed()) + // Add transit time (includes panama margin, it seems).
						routeCostProvider.getRouteTransitTime(voyageDetails.getOptions().getRoute(), voyageDetails.getOptions().getVessel());

				final ZonedDateTime endTime = modelEntityMap.getDateFromHours(currentTime + options.getAvailableTime(), canalEntry);
				final int marginHours;
				final int toCanalSpeed;
				if (journey.getRouteOption() == RouteOption.PANAMA) {
					toCanalSpeed = Math.min(panamaSlotsProvider.getSpeedToCanal(), voyageDetails.getOptions().getVessel().getMaxSpeed());
					marginHours = panamaSlotsProvider.getMarginInHours();
				} else {
					// Suez - use voyage speed
					toCanalSpeed = voyageDetails.getSpeed();
					marginHours = 0;
				}

				boolean southBound = distanceProvider.getRouteOptionDirection(options.getFromPortSlot().getPort(), ERouteOption.PANAMA) == RouteOptionDirection.SOUTHBOUND;
				ZonedDateTime latestCanalEntry;
				{
					latestCanalEntry = endTime.minusHours(fromCanalEntry);
					journey.setLatestPossibleCanalDateTime(latestCanalEntry.toLocalDateTime());
				}

				// set canal arrival - 1st leg of panama journey.
				int toCanal = distanceProvider.getTravelTime(ERouteOption.DIRECT, //
						options.getVessel(), //
						options.getFromPortSlot().getPort(), //
						canalEntry, //
						toCanalSpeed);

				//We should take into account margin hours for both Northbound and Southbound journeys.
				toCanal = toCanal + marginHours;

				estimatedArrival = modelEntityMap.getDateFromHours(currentTime + toCanal, canalEntry);
				journey.setCanalArrivalTime(estimatedArrival.toLocalDateTime());

				journey.setCanalEntrance(ExporterExtensionUtils.mapCanalEntry(distanceProvider.getRouteOptionCanalEntrance(options.getFromPortSlot().getPort(), options.getRoute())));
				journey.setCanalEntrancePort(eCanalPort);
			}

		}

		// set canal booking if present
		IRouteOptionBooking routeOptionBooking = portTimesRecord.getRouteOptionBooking(fromPortSlot);
		if (routeOptionBooking != null) {
			final CanalBookingSlot canalBookingSlot = modelEntityMap.getModelObject(routeOptionBooking, CanalBookingSlot.class);
			final ECanalEntry canalEntry = distanceProvider.getRouteOptionCanalEntrance(fromPortSlot.getPort(), options.getRoute());

			final IPort canalEntryPort = distanceProvider.getRouteOptionEntryPort(options.getRoute(), canalEntry);
			assert canalEntryPort != null;
			final Port eCanalPort = modelEntityMap.getModelObject(canalEntryPort, Port.class);

			journey.setCanalBooking(canalBookingSlot);
			journey.setCanalEntrance(canalBookingSlot.getCanalEntrance());
			journey.setCanalEntrancePort(eCanalPort);
			journey.setCanalDateTime(canalBookingSlot.getBookingDate().atTime(CanalBookingSlot.BOOKING_HOURS_OFFSET, 0));	
		} else if (journey.getRouteOption() != RouteOption.DIRECT) {
			final ECanalEntry canalEntry = distanceProvider.getRouteOptionCanalEntrance(fromPortSlot.getPort(), voyageDetails.getOptions().getRoute());
			if (canalEntry != null) {

				final IPort canalEntryPort = distanceProvider.getRouteOptionEntryPort(options.getRoute(), canalEntry);
				assert canalEntryPort != null;
				final Port eCanalPort = modelEntityMap.getModelObject(canalEntryPort, Port.class);

				journey.setCanalEntrance(ExporterExtensionUtils.mapCanalEntry(canalEntry));
				journey.setCanalEntrancePort(eCanalPort);
				journey.setCanalDateTime(journey.getCanalArrivalTime());
				if (estimatedArrival != null && estimatedArrival.getHour() > CanalBookingSlot.BOOKING_HOURS_OFFSET && journey.getRouteOption() == RouteOption.PANAMA) {
					// slot can't be reached that day, set arrival to next day
					journey.setCanalDateTime(estimatedArrival.plusDays(1).toLocalDateTime());
				}
			}
		}
		
		if (journey.getRouteOption() != RouteOption.DIRECT) {
			final CanalJourneyEvent canalJourneyEvent = generateCanalJourneyEvent(journey, routeOptionBooking, options, portTimesRecord, fromPortSlot);
			journey.setCanalJourneyEvent(canalJourneyEvent);
		}

		return journey;
	}

	private CanalJourneyEvent generateCanalJourneyEvent(Journey journey, IRouteOptionBooking routeOptionBooking, 
			final VoyageOptions options, final IPortTimesRecord ptr, final IPortSlot fromPortSlot) {
		if (journey.getCanalDateTime() != null) {
			if (journey.getRouteOption() == RouteOption.PANAMA) {
				//Can we replace this check for charter in or spot vessel some other way?
				//At any rate, seems irrelevant, if there is a panama crossing we need a booking or we need to wait.
				//if (sequence.getCharterInMarket() == null || sequence.getSpotIndex() != -1) {
					final CanalJourneyEvent canal = ScheduleFactory.eINSTANCE.createCanalJourneyEvent();
					canal.setLinkedSequence(journey.getSequence());
					canal.setLinkedJourney(journey);
					canal.setStart(journey.getCanalDateTime().atZone(ZoneId.of(journey.getTimeZone(SchedulePackage.Literals.JOURNEY__CANAL_DATE_TIME))));
					canal.setEnd(canal.getStart().plusDays(1));
					if (journey.getCanalBooking() == null) {
						if (journey.getLatestPossibleCanalDateTime() != null) {
							ZonedDateTime atStartOfDay = journey.getLatestPossibleCanalDateTime()
									.atZone(ZoneId.of(journey.getTimeZone(SchedulePackage.Literals.JOURNEY__CANAL_DATE_TIME)));
							int hours = Math.max(1, Hours.between(canal.getStart(), atStartOfDay));
							canal.setEnd(canal.getStart().plusHours(hours));
						}
					}

					canal.setPort(journey.getCanalEntrancePort());
					canal.setPanamaWaitingTimeHours(ptr.getSlotAdditionaPanamaIdleHours(fromPortSlot));
					canal.setMaxAvailablePanamaWaitingTimeHours(ptr.getSlotMaxAdditionaPanamaIdleHours(fromPortSlot));
					
					return canal;
				//}

			}
		}
		return null;
	}
	
	private List<FuelQuantity> exportFuelData(final VoyageDetails details) {
		return FuelExportHelper.exportFuelData(details, details.getOptions().getVessel(), FuelExportHelper.travelFuelComponentNames,
				(d, fk) -> d.getFuelConsumption(fk) + d.getRouteAdditionalConsumption(fk), VoyageDetails::getFuelUnitPrice, modelEntityMap);
	}
}
