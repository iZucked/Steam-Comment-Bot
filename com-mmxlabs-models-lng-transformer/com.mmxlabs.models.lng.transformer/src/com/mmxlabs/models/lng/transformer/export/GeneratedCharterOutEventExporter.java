/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.Map;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.events.IGeneratedCharterOutEvent;

/**
 * Exports idleevent annotations.
 * 
 * @author hinton
 * 
 */
public class GeneratedCharterOutEventExporter extends BaseAnnotationExporter {
	private final VisitEventExporter visitExporter;

	/**
	 * Take a reference to the visit exporter, as a hack to get the last visited port
	 * 
	 * @param visitExporter
	 */
	public GeneratedCharterOutEventExporter(final VisitEventExporter visitExporter) {
		this.visitExporter = visitExporter;
	}

	@Override
	public void init() {
	}

	@Override
	public Event export(final ISequenceElement element, final Map<String, IElementAnnotation> annotations) {
		final IGeneratedCharterOutEvent event = (IGeneratedCharterOutEvent) annotations.get(SchedulerConstants.AI_generatedCharterOutInfo);

		if (event == null) {
			return null;
		}

		Port ePort = modelEntityMap.getModelObject(event.getPort(), Port.class);

		// TODO this is a bit of a kludge; the ANYWHERE port does not
		// have an EMF representation, but we do want idle time for it
		// so we assume if we hit a dubious port it's ANYWHERE and that
		// we are really where we used to be.
		if (ePort == null) {
			ePort = visitExporter.getLastPortVisited();
		}
		// if (ePort == null)
		// return null;

		final GeneratedCharterOut generatedCharterOutEvent = factory.createGeneratedCharterOut();
		generatedCharterOutEvent.setPort(ePort);
		generatedCharterOutEvent.setStart(modelEntityMap.getDateFromHours(event.getStartTime(), event.getPort()));
		generatedCharterOutEvent.setEnd(modelEntityMap.getDateFromHours(event.getEndTime(), event.getPort()));
		generatedCharterOutEvent.setRevenue(OptimiserUnitConvertor.convertToExternalFixedCost(event.getCharterOutRevenue()));

		generatedCharterOutEvent.setCharterCost(OptimiserUnitConvertor.convertToExternalFixedCost(event.getCharterCost()));

		generatedCharterOutEvent.setHeelAtStart(OptimiserUnitConvertor.convertToExternalVolume(event.getStartHeelInM3()));
		generatedCharterOutEvent.setHeelAtEnd(OptimiserUnitConvertor.convertToExternalVolume(event.getEndHeelInM3()));

		return generatedCharterOutEvent;
	}
}
