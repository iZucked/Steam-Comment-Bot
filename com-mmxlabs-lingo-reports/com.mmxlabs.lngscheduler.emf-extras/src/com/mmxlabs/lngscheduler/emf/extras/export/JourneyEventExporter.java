/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.lngscheduler.emf.extras.export;

import java.util.Map;

import org.eclipse.emf.common.util.EList;

import scenario.fleet.VesselState;
import scenario.port.Port;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.Journey;
import scenario.schedule.fleetallocation.AllocatedVessel;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * @author hinton
 * 
 */
public class JourneyEventExporter extends BaseAnnotationExporter {
	@Override
	public void init() {

	}

	@Override
	public Journey export(final ISequenceElement element,
			final Map<String, Object> annotations, final AllocatedVessel v) {

		@SuppressWarnings("unchecked")
		final IJourneyEvent<ISequenceElement> event = (IJourneyEvent<ISequenceElement>) annotations
				.get(SchedulerConstants.AI_journeyInfo);

		if (event == null) return null;
		
		final Port eFromPort = entities.getModelObject(event.getFromPort(),
				Port.class);
		final Port eToPort = entities.getModelObject(event.getToPort(),
				Port.class);

		if (eFromPort == null || eToPort == null)
			return null;

		final Journey journey = factory.createJourney();

		journey.setStartTime(entities.getDateFromHours(event.getStartTime()));
		journey.setEndTime(entities.getDateFromHours(event.getEndTime()));

		journey.setFromPort(eFromPort);
		journey.setToPort(eToPort);

		// journey.setDestination(entities.getModelObject(event.getToPort(),
		// Port.class));

		journey.setDistance(event.getDistance());
		journey.setRoute(event.getRoute());
		journey.setRouteCost(event.getRouteCost() / Calculator.ScaleFactor);
		
		switch (event.getVesselState()) {
		case Ballast:
			journey.setVesselState(VesselState.BALLAST);
			break;
		case Laden:
			journey.setVesselState(VesselState.LADEN);
			break;
		}

		journey.setSpeed(event.getSpeed() / (double) Calculator.ScaleFactor);

		final EList<FuelQuantity> fuelUsage = journey.getFuelUsage();
		for (final FuelComponent fc : FuelComponent.getTravelFuelComponents()) {
			final long consumption = event.getFuelConsumption(fc,
					fc.getDefaultFuelUnit());
			final long cost = event.getFuelCost(fc);
			final FuelQuantity fq = createFuelQuantity(fc, consumption, cost);

			fuelUsage.add(fq);
		}

		return journey;
	}
}
