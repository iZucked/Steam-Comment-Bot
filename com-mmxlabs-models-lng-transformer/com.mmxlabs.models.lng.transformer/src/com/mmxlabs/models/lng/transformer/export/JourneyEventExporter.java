/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.Map;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;

/**
 * @author hinton
 * 
 */
public class JourneyEventExporter extends BaseAnnotationExporter {
	@Override
	public void init() {

	}

	@Override
	public Journey export(final ISequenceElement element, final Map<String, IElementAnnotation> annotations) {

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

		journey.setStart(entities.getDateFromHours(event.getStartTime()));
		journey.setEnd(entities.getDateFromHours(event.getEndTime()));

		journey.setPort(eFromPort);
		journey.setDestination(eToPort);

		journey.setDistance(event.getDistance());
		journey.setRoute(entities.getModelObject(event.getRoute(), Route.class));
		journey.setToll(OptimiserUnitConvertor.convertToExternalFixedCost(event.getRouteCost()));

		journey.setLaden(VesselState.Laden.equals(event.getVesselState()));

		journey.setSpeed(OptimiserUnitConvertor.convertToExternalSpeed(event.getSpeed()));

		journey.getFuels().addAll(super.createFuelQuantities(event));

		// set up hire cost
		journey.setCharterCost(OptimiserUnitConvertor.convertToExternalFixedCost(event.getCharterCost()));
		
		return journey;
	}
}
