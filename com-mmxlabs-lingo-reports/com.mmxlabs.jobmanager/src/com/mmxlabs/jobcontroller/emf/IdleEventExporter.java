/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.jobcontroller.emf;

import java.util.Map;

import org.eclipse.emf.common.util.EList;

import scenario.fleet.VesselState;
import scenario.port.Port;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.Idle;
import scenario.schedule.events.ScheduledEvent;

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
	@Override
	public void init() {
	}

	@Override
	public ScheduledEvent export(final ISequenceElement element,
			final Map<String, Object> annotations) {
		@SuppressWarnings("unchecked")
		final IIdleEvent<ISequenceElement> event = (IIdleEvent<ISequenceElement>) annotations
				.get(SchedulerConstants.AI_idleInfo);

		if (event == null) return null;
		
		final Port ePort = entities.getModelObject(event.getPort(), Port.class);

		if (ePort == null)
			return null;

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

		final EList<FuelQuantity> fuelUsage = idle.getFuelUsage();

		for (final FuelComponent fc : FuelComponent.getIdleFuelComponents()) {
			fuelUsage.add(createFuelQuantity(fc,
					event.getFuelConsumption(fc, fc.getDefaultFuelUnit()),
					event.getFuelCost(fc)));
		}

		return idle;
	}
}
