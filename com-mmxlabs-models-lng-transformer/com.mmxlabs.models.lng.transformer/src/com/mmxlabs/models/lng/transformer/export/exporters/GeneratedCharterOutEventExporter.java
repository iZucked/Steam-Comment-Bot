/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export.exporters;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

/**
 * Exports idleevent annotations.
 * 
 * @author hinton
 * 
 */
public class GeneratedCharterOutEventExporter {

	@Inject
	private ModelEntityMap modelEntityMap;

	public GeneratedCharterOut export(final VoyageDetails voyageDetails, final VolumeAllocatedSequence volumeAllocatedSequence, final int currentTime) {
		@NonNull
		final VoyageOptions options = voyageDetails.getOptions();
		Port ePort = modelEntityMap.getModelObject(options.getToPortSlot().getPort(), Port.class);

		// TODO this is a bit of a kludge; the ANYWHERE port does not
		// have an EMF representation, but we do want idle time for it
		// so we assume if we hit a dubious port it's ANYWHERE and that
		// we are really where we used to be.
		if (ePort == null) {
			ePort = modelEntityMap.getModelObject(options.getFromPortSlot().getPort(), Port.class);
		}

		final GeneratedCharterOut event = ScheduleFactory.eINSTANCE.createGeneratedCharterOut();
		event.setPort(ePort);
		event.setStart(modelEntityMap.getDateFromHours(currentTime, options.getToPortSlot().getPort()));
		event.setEnd(modelEntityMap.getDateFromHours(currentTime + voyageDetails.getIdleTime(), options.getToPortSlot().getPort()));

		return event;
	}
}
