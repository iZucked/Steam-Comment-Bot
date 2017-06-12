/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export.exporters;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.FuelExportHelper;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 * 
 */
public class JourneyEventExporter {

	@Inject
	private ModelEntityMap modelEntityMap;

	public Journey export(final VoyageDetails voyageDetails, final VolumeAllocatedSequence volumeAllocatedSequence, int currentTime) {

		if (voyageDetails.getTravelTime() == 0 && voyageDetails.getOptions().getDistance() == 0) {
			return null; // filter out zero-length journeys
		}
		@NonNull
		VoyageOptions options = voyageDetails.getOptions();

		final Port eFromPort = modelEntityMap.getModelObject(options.getFromPortSlot().getPort(), Port.class);
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
		String name = options.getRoute().name();
		@Nullable
		Route modelObject = modelEntityMap.getModelObject(name, Route.class);
		journey.setRoute(modelObject);
		journey.setToll(OptimiserUnitConvertor.convertToExternalFixedCost(options.getRouteCost()));

		journey.setLaden(VesselState.Laden.equals(options.getVesselState()));

		journey.setSpeed(OptimiserUnitConvertor.convertToExternalSpeed(voyageDetails.getSpeed()));

		journey.getFuels().addAll(exportFuelData(voyageDetails));
		
		
		
		
		// set canal booking if present
		Optional<Pair<VoyagePlan, IPortTimesRecord>> potential = volumeAllocatedSequence.getVoyagePlans().stream().filter(pair -> {
			return pair.getSecond().getFirstSlot().getPort().equals(options.getFromPortSlot().getPort());
		}).findFirst();
		if (potential.isPresent() && potential.get().getSecond().getRouteOptionSlot() != null) {
			CanalBookingSlot canalBookingSlot = modelEntityMap.getModelObject(potential.get().getSecond().getRouteOptionSlot(), CanalBookingSlot.class);
			journey.setCanalBooking(canalBookingSlot);
			journey.setCanalEntry(canalBookingSlot.getEntryPoint());
			journey.setCanalDate(canalBookingSlot.getSlotDate());
		}

		return journey;
	}

	private List<FuelQuantity> exportFuelData(VoyageDetails details) {

		return FuelExportHelper.exportFuelData(details, FuelExportHelper.travelFuelComponentNames, (d, f, u) -> {
			return d.getFuelConsumption(f, u) + d.getRouteAdditionalConsumption(f, u);
		}, VoyageDetails::getFuelUnitPrice);
	}
}
