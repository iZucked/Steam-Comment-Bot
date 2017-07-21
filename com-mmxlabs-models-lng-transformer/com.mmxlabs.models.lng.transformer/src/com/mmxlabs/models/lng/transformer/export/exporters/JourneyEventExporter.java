/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export.exporters;

import java.time.ZonedDateTime;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PanamaBookingPeriod;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.FuelExportHelper;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
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
		final String name = options.getRoute().name();
		@Nullable
		final Route modelObject = modelEntityMap.getModelObject(name, Route.class);
		journey.setRoute(modelObject);
		journey.setToll(OptimiserUnitConvertor.convertToExternalFixedCost(options.getRouteCost()));

		journey.setLaden(VesselState.Laden.equals(options.getVesselState()));

		journey.setSpeed(OptimiserUnitConvertor.convertToExternalSpeed(voyageDetails.getSpeed()));

		journey.getFuels().addAll(exportFuelData(voyageDetails));

		final IPortTimesRecord portTimesRecord = volumeAllocatedSequence.getPortTimesRecord(fromPortSlot);

		final Route journeyRoute = journey.getRoute();
		ZonedDateTime estimatedArrival = null;
		if (journey.getRoute().isCanal()) {
			if (journey.getRoute().getRouteOption() == RouteOption.PANAMA) {
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

				final int fromCanalEntry = distanceProvider.getTravelTime(options.getRoute(), //
						voyageDetails.getOptions().getVessel(), //
						canalEntry, //
						voyageDetails.getOptions().getToPortSlot().getPort(), //
						voyageDetails.getOptions().getVessel().getVesselClass().getMaxSpeed());

				final ZonedDateTime endTime = modelEntityMap.getDateFromHours(currentTime + options.getAvailableTime(), canalEntry);
				final int marginHours;
				final int toCanalSpeed;
				if (journey.getRoute().getRouteOption() == RouteOption.PANAMA) {
					toCanalSpeed = Math.min(panamaSlotsProvider.getSpeedToCanal(), voyageDetails.getOptions().getVessel().getVesselClass().getMaxSpeed());
					marginHours = panamaSlotsProvider.getMargin();
				} else {
					// Suez - use voyage speed
					toCanalSpeed = voyageDetails.getSpeed();
					marginHours = 0;
				}

				ZonedDateTime latestCanalEntry = endTime.minusHours(fromCanalEntry).minusHours(marginHours);
				journey.setLatestPossibleCanalDate(latestCanalEntry.toLocalDate());

				if (latestCanalEntry.getHour() > CanalBookingSlot.BOOKING_HOURS_OFFSET && journey.getRoute().getRouteOption() == RouteOption.PANAMA) {
					// slot can't be reached that day, set to previous day
					journey.setLatestPossibleCanalDate(latestCanalEntry.minusDays(1).toLocalDate());
				}

				// set canal arrival
				int toCanal = distanceProvider.getTravelTime(ERouteOption.DIRECT, //
						options.getVessel(), //
						options.getFromPortSlot().getPort(), //
						canalEntry, //
						toCanalSpeed) + marginHours;

				final int departureTime = portTimesRecord.getSlotTime(fromPortSlot) + portTimesRecord.getSlotDuration(fromPortSlot);
				estimatedArrival = modelEntityMap.getDateFromHours(departureTime + toCanal, canalEntry);
				journey.setCanalArrival(estimatedArrival.toLocalDate());
			}

		}

		// set canal booking if present
		if (portTimesRecord.getRouteOptionBooking(fromPortSlot) != null) {
			final CanalBookingSlot canalBookingSlot = modelEntityMap.getModelObject(portTimesRecord.getRouteOptionBooking(fromPortSlot), CanalBookingSlot.class);
			journey.setCanalBooking(canalBookingSlot);
			journey.setCanalEntry(canalBookingSlot.getEntryPoint());
			journey.setCanalDate(canalBookingSlot.getBookingDate());
		} else if (journeyRoute.isCanal()) {
			final IPort canalEntry = distanceProvider.getRouteOptionEntryPort(fromPortSlot.getPort(), voyageDetails.getOptions().getRoute());
			if (canalEntry != null) {
				journey.setCanalDate(journey.getCanalArrival());
				if (estimatedArrival != null && estimatedArrival.getHour() > CanalBookingSlot.BOOKING_HOURS_OFFSET && journey.getRoute().getRouteOption() == RouteOption.PANAMA) {
					// slot can't be reached that day, set arrival to next day
					journey.setCanalDate(estimatedArrival.plusDays(1).toLocalDate());
				}

				if (canalEntry != null) {
					@NonNull
					final Port expectedEntryPort = modelEntityMap.getModelObjectNullChecked(canalEntry, Port.class);
					if (journeyRoute.getNorthEntrance() != null && journeyRoute.getNorthEntrance().getPort() == expectedEntryPort) {
						journey.setCanalEntry(journeyRoute.getNorthEntrance());
					}
					if (journeyRoute.getSouthEntrance() != null && journeyRoute.getSouthEntrance().getPort() == expectedEntryPort) {
						journey.setCanalEntry(journeyRoute.getSouthEntrance());
					}
				}
			}
		}
		return journey;
	}

	private List<FuelQuantity> exportFuelData(final VoyageDetails details) {

		return FuelExportHelper.exportFuelData(details, FuelExportHelper.travelFuelComponentNames, (d, f, u) -> {
			return d.getFuelConsumption(f, u) + d.getRouteAdditionalConsumption(f, u);
		}, VoyageDetails::getFuelUnitPrice);
	}
}
