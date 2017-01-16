/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.Map;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;

/**
 * Exports idleevent annotations.
 * 
 * @author hinton
 * 
 */
public class IdleEventExporter extends BaseAnnotationExporter {
	private final VisitEventExporter visitExporter;

	/**
	 * Take a reference to the visit exporter, as a hack to get the last visited port
	 * 
	 * @param visitExporter
	 */
	public IdleEventExporter(final VisitEventExporter visitExporter) {
		this.visitExporter = visitExporter;
	}

	@Override
	public void init() {
	}

	@Override
	public Event export(final ISequenceElement element, final Map<String, IElementAnnotation> annotations) {
		final IIdleEvent event = (IIdleEvent) annotations.get(SchedulerConstants.AI_idleInfo);

		if (event == null) {
			return null;
		}

		Port ePort = modelEntityMap.getModelObject(event.getPort(), Port.class);

		// TODO this is a bit of a kludge; the ANYWHERE port does not
		// have an EMF representation, but we do want idle time for it
		// so we assume if we hit a dubious port it's ANYWHERE and that
		// we are really where we used to be.
		if (ePort == null)
			ePort = visitExporter.getLastPortVisited();
		// if (ePort == null)
		// return null;

		final Idle idle = factory.createIdle();

		idle.setPort(ePort);
		idle.setStart(modelEntityMap.getDateFromHours(event.getStartTime(), event.getPort()));
		idle.setEnd(modelEntityMap.getDateFromHours(event.getEndTime(), event.getPort()));

		idle.setLaden(VesselState.Laden.equals(event.getVesselState()));

		idle.getFuels().addAll(super.createFuelQuantities(event));

		// set up hire cost
		// Note: Cooldown duration is zero - this will need to be changed if cooldown duration becomes non-zero again. This will likely need API support
		idle.setCharterCost(OptimiserUnitConvertor.convertToExternalFixedCost(event.getCharterCost()));
		idle.setHeelAtStart(OptimiserUnitConvertor.convertToExternalVolume(event.getStartHeelInM3()));
		idle.setHeelAtEnd(OptimiserUnitConvertor.convertToExternalVolume(event.getEndHeelInM3()));
		return idle;
	}
}
