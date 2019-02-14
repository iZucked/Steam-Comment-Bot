/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export.exporters;

import java.time.ZonedDateTime;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PanamaBookingPeriod;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.ExporterExtensionUtils;
import com.mmxlabs.models.lng.transformer.export.FuelExportHelper;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider.RouteOptionDirection;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PanamaPeriod;
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
					PanamaPeriod period = portTimesRecord.getSlotNextVoyagePanamaPeriod(voyageDetails.getOptions().getFromPortSlot());
					if (period != null) {
						switch (period) {
						case Beyond:
							exportPeriod = PanamaBookingPeriod.BEYOND;
							break;
						case Relaxed:
							exportPeriod = PanamaBookingPeriod.RELAXED;
							break;
						case Strict:
							exportPeriod = PanamaBookingPeriod.STRICT;
							break;
						default:
							break;
						}
					}
				}
				journey.setCanalBookingPeriod(exportPeriod);
			}
			final IPort canalEntry = distanceProvider.getRouteOptionEntryPort(options.getFromPortSlot().getPort(), options.getRoute());

			// set latest possible canal date
			if (canalEntry != null) {
				final Port eCanalPort = modelEntityMap.getModelObject(canalEntry, Port.class);
				final int fromCanalEntry = distanceProvider.getTravelTime(options.getRoute(), //
						voyageDetails.getOptions().getVessel(), //
						canalEntry, //
						voyageDetails.getOptions().getToPortSlot().getPort(), //
						voyageDetails.getOptions().getVessel().getMaxSpeed());

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
				if (southBound) {
					latestCanalEntry = endTime.minusHours(fromCanalEntry).minusHours(marginHours);
					journey.setLatestPossibleCanalDateTime(latestCanalEntry.toLocalDateTime());
					if (latestCanalEntry.toLocalDateTime().getHour() > CanalBookingSlot.BOOKING_HOURS_OFFSET && journey.getRouteOption() == RouteOption.PANAMA) {
						// slot can't be reached that day, set to previous day
						journey.setLatestPossibleCanalDateTime(latestCanalEntry.toLocalDateTime().withHour(CanalBookingSlot.BOOKING_HOURS_OFFSET));
					}
				} else {
					latestCanalEntry = endTime.minusHours(fromCanalEntry);
					journey.setLatestPossibleCanalDateTime(latestCanalEntry.toLocalDateTime());
				}

				// set canal arrival
				int toCanal = distanceProvider.getTravelTime(ERouteOption.DIRECT, //
						options.getVessel(), //
						options.getFromPortSlot().getPort(), //
						canalEntry, //
						toCanalSpeed);

				if (southBound) {
					toCanal = toCanal + marginHours;
				}

				estimatedArrival = modelEntityMap.getDateFromHours(currentTime + toCanal, canalEntry);
				journey.setCanalArrivalTime(estimatedArrival.toLocalDateTime());

				journey.setCanalEntrance(ExporterExtensionUtils.mapCanalEntry(distanceProvider.getRouteOptionCanalEntrance(options.getFromPortSlot().getPort(), options.getRoute())));
				journey.setCanalEntrancePort(eCanalPort);
			}

		}

		// set canal booking if present
		if (portTimesRecord.getRouteOptionBooking(fromPortSlot) != null) {
			final CanalBookingSlot canalBookingSlot = modelEntityMap.getModelObject(portTimesRecord.getRouteOptionBooking(fromPortSlot), CanalBookingSlot.class);
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
					journey.setCanalDateTime(estimatedArrival.toLocalDateTime());
				}
			}
		}
		return journey;
	}

	private List<FuelQuantity> exportFuelData(final VoyageDetails details) {

		return FuelExportHelper.exportFuelData(details, details.getOptions().getVessel(), FuelExportHelper.travelFuelComponentNames,
				(d, fk) -> d.getFuelConsumption(fk) + d.getRouteAdditionalConsumption(fk), VoyageDetails::getFuelUnitPrice, modelEntityMap);
	}
}
