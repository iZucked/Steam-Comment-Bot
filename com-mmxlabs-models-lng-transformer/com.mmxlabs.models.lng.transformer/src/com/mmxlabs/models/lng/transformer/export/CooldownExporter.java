/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.Map;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

public class CooldownExporter extends BaseAnnotationExporter {
	final VisitEventExporter visitExporter;

	public CooldownExporter(final VisitEventExporter e) {
		this.visitExporter = e;
	}

	@Override
	public void init() {

	}

	@Override
	public Event export(ISequenceElement element, Map<String, Object> annotations) {
		final IIdleEvent event = (IIdleEvent) annotations.get(SchedulerConstants.AI_idleInfo);

		if (event == null) {
			return null;
		}

		final long cooldownVolume = event.getFuelConsumption(FuelComponent.Cooldown, FuelUnit.M3);
		if (cooldownVolume > 0) {
			Port ePort = entities.getModelObject(event.getPort(), Port.class);
			if (ePort == null)
				ePort = visitExporter.getLastPortVisited();

			final Cooldown cooldown = factory.createCooldown();
			cooldown.setPort(ePort);
			cooldown.setVolume(OptimiserUnitConvertor.convertToExternalVolume(cooldownVolume));
			cooldown.setCost(OptimiserUnitConvertor.convertToExternalFixedCost(event.getFuelCost(FuelComponent.Cooldown)));
			cooldown.setStart(entities.getDateFromHours(event.getEndTime()));
			cooldown.setEnd(entities.getDateFromHours(event.getEndTime()));

			// Cooldown duration is zero - this will need to be changed if cooldown duration becomes non-zero again. This will likely need API support
			// cooldown.setCharterCost(OptimiserUnitConvertor.convertToExternalFixedCost(event.getCharterCost()));

			return cooldown;
		}

		return null;
	}

}
