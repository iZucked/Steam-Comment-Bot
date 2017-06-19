/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export.exporters;

import java.security.cert.PKIXRevocationChecker.Option;
import java.time.ZoneId;
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
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.FuelExportHelper;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
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
	IPanamaBookingsProvider panamaSlotsProvider;

	public Journey export(final VoyageDetails voyageDetails, final VolumeAllocatedSequence volumeAllocatedSequence, final int currentTime) {

		if (voyageDetails.getTravelTime() == 0 && voyageDetails.getOptions().getDistance() == 0) {
			return null; // filter out zero-length journeys
		}
		@NonNull
		final VoyageOptions options = voyageDetails.getOptions();

		@NonNull
		IPortSlot fromPortSlot = options.getFromPortSlot();

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

		IPortTimesRecord portTimesRecord = volumeAllocatedSequence.getPortTimesRecord(fromPortSlot);

		// set latest possible canal date
		if (journey.getRoute().isCanal()){
			final IPort canalEntry = distanceProvider.getRouteOptionEntry(options.getFromPortSlot().getPort(), options.getRoute());
			
			if (canalEntry != null){
				
				int fromCanalEntry = distanceProvider.getTravelTime(options.getRoute(), //
						voyageDetails.getOptions().getVessel(), //
						canalEntry, //
						voyageDetails.getOptions().getToPortSlot().getPort(), //
						voyageDetails.getOptions().getVessel().getVesselClass().getMaxSpeed());
				
				ZonedDateTime endTime = modelEntityMap.getDateFromHours(currentTime + options.getAvailableTime(), canalEntry);
				
				ZonedDateTime latestCanalEntry = endTime.minusHours(fromCanalEntry).minusHours(panamaSlotsProvider.getMargin());
				
				journey.setLatestPossibleCanalDate(latestCanalEntry.toLocalDate());
				if (latestCanalEntry.getHour()*60 + latestCanalEntry.getMinute() > IPanamaBookingsProvider.BOOKING_OFFSET_FROM_MIDNIGHT_MINUTES
						&& journey.getRoute().getRouteOption() == RouteOption.PANAMA){
					// slot can't be reached that day, set to previous day
					journey.setLatestPossibleCanalDate(latestCanalEntry.minusDays(1).toLocalDate());
				}
			}
		}

		// set canal booking if present
		if (portTimesRecord.getRouteOptionBooking(fromPortSlot) != null) {
			final CanalBookingSlot canalBookingSlot = modelEntityMap.getModelObject(portTimesRecord.getRouteOptionBooking(fromPortSlot), CanalBookingSlot.class);
			journey.setCanalBooking(canalBookingSlot);
			journey.setCanalEntry(canalBookingSlot.getEntryPoint());
			journey.setCanalDate(canalBookingSlot.getBookingDate());
		} else if (journey.getRoute().isCanal()) {

			final IPort canalEntry = distanceProvider.getRouteOptionEntry(voyageDetails.getOptions().getFromPortSlot().getPort(), voyageDetails.getOptions().getRoute());
			if (canalEntry != null) {
				int toCanal = distanceProvider.getTravelTime(ERouteOption.DIRECT, //
						options.getVessel(), //
						options.getFromPortSlot().getPort(), // 
						canalEntry, //
						Math.min(panamaSlotsProvider.getSpeedToCanal(), options.getVessel().getVesselClass().getMaxSpeed()))
						+ panamaSlotsProvider.getMargin();

				int fromCanal = distanceProvider.getTravelTime(voyageDetails.getOptions().getRoute(), voyageDetails.getOptions().getVessel(), canalEntry,
						voyageDetails.getOptions().getToPortSlot().getPort(), voyageDetails.getOptions().getVessel().getVesselClass().getMaxSpeed());
				
				int departureTime = portTimesRecord.getSlotTime(voyageDetails.getOptions().getFromPortSlot()) + portTimesRecord.getSlotDuration(voyageDetails.getOptions().getFromPortSlot());
				ZonedDateTime estimatedArrival = modelEntityMap.getDateFromHours(departureTime + toCanal, canalEntry);
				// Add 200 years if we would not make out arrival times based on canal rules

				if (departureTime + toCanal + fromCanal > portTimesRecord.getSlotTime(portTimesRecord.getReturnSlot())) {
					estimatedArrival = estimatedArrival.plusYears(200);
				}
				journey.setCanalDate(estimatedArrival.toLocalDate());
				if (estimatedArrival.getHour()*60 + estimatedArrival.getMinute() > IPanamaBookingsProvider.BOOKING_OFFSET_FROM_MIDNIGHT_MINUTES 
						&& journey.getRoute().getRouteOption() == RouteOption.PANAMA){
					// slot can't be reached that day, set arrival to next day
					journey.setCanalDate(estimatedArrival.plusDays(1).toLocalDate());
				}

				if (canalEntry != null) {
					@NonNull
					final Port expectedEntryPort = modelEntityMap.getModelObjectNullChecked(canalEntry, Port.class);
					if (journey.getRoute().getEntryA() != null && journey.getRoute().getEntryA().getPort() == expectedEntryPort) {
						journey.setCanalEntry(journey.getRoute().getEntryA());
					}
					if (journey.getRoute().getEntryB() != null && journey.getRoute().getEntryB().getPort() == expectedEntryPort) {
						journey.setCanalEntry(journey.getRoute().getEntryB());
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
