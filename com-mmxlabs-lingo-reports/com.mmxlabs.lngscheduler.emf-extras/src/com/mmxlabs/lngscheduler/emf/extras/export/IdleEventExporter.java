/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.export;

import java.util.Map;

import scenario.fleet.VesselState;
import scenario.port.Port;
import scenario.schedule.events.Idle;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.fleetallocation.AllocatedVessel;

import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * Exports idleevent annotations.
 * 
 * @author hinton
 * 
 */
public class IdleEventExporter extends BaseAnnotationExporter {
	private final VisitEventExporter visitExporter;

	/**
	 * Take a reference to the visit exporter, as a hack to get the last visited
	 * port
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
	public ScheduledEvent export(final ISequenceElement element,
			final Map<String, Object> annotations, final AllocatedVessel v) {
		@SuppressWarnings("unchecked")
		final IIdleEvent<ISequenceElement> event = (IIdleEvent<ISequenceElement>) annotations
				.get(SchedulerConstants.AI_idleInfo);

		if (event == null)
			return null;

		Port ePort = entities.getModelObject(event.getPort(), Port.class);

		// TODO this is a bit of a kludge; the ANYWHERE port does not
		// have an EMF representation, but we do want idle time for it
		// so we assume if we hit a dubious port it's ANYWHERE and that
		// we are really where we used to be.

		ePort = visitExporter.getLastPortVisited();
		// if (ePort == null)
		// return null;

		final Idle idle = factory.createIdle();

		idle.setPort(ePort);
		idle.setStartTime(entities.getDateFromHours(event.getStartTime()));
		idle.setEndTime(entities.getDateFromHours(event.getEndTime()));

		switch (event.getVesselState()) {
		case Ballast:
			idle.setVesselState(VesselState.BALLAST);
			break;
		case Laden:
			idle.setVesselState(VesselState.LADEN);
			break;
		}

		for (final FuelComponent fc : FuelComponent.getIdleFuelComponents()) {
			addFuelQuantity(idle, fc,
					event.getFuelConsumption(fc, fc.getDefaultFuelUnit()),
					event.getFuelCost(fc));
		}

//		if (idle.getPort() == null) {
//			System.err.println("This shouldn't have happened");
//		}

		return idle;
	}
}
