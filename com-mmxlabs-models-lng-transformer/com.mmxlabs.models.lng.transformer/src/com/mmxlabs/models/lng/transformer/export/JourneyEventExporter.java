/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.Map;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
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
	public Journey export(final ISequenceElement element, final Map<String, Object> annotations, final AllocatedVessel v) {

		final IJourneyEvent event = (IJourneyEvent) annotations.get(SchedulerConstants.AI_journeyInfo);

		if (event == null)
			return null;

		if (event.getDistance() == 0)
			return null; // filter out zero-length journeys

		final Port eFromPort = entities.getModelObject(event.getFromPort(), Port.class);
		final Port eToPort = entities.getModelObject(event.getToPort(), Port.class);

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

		for (final FuelComponent fc : FuelComponent.getTravelFuelComponents()) {
			final long consumption = event.getFuelConsumption(fc, fc.getDefaultFuelUnit());
			final long cost = event.getFuelCost(fc);

			addFuelQuantity(journey, fc, consumption, cost);
		}

		scaleFuelQuantities(journey);

		return journey;
	}
}
