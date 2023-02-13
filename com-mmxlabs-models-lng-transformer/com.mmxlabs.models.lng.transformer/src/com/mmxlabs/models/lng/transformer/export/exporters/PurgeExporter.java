/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export.exporters;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Purge;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.voyage.ExplicitIdleTime;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

public class PurgeExporter {

	@Inject
	private ModelEntityMap modelEntityMap;

	public @Nullable Purge export(final VoyageDetails voyageDetails, final VolumeAllocatedSequence volumeAllocatedSequence, final int currentTime) {

		if (voyageDetails.isPurgePerformed()) {

			Port ePort = modelEntityMap.getModelObject(voyageDetails.getOptions().getToPortSlot().getPort(), Port.class);

			// TODO this is a bit of a kludge; the ANYWHERE port does not
			// have an EMF representation, but we do want idle time for it
			// so we assume if we hit a dubious port it's ANYWHERE and that
			// we are really where we used to be.
			if (ePort == null) {
				ePort = modelEntityMap.getModelObject(voyageDetails.getOptions().getFromPortSlot().getPort(), Port.class);
			}

			final Purge purge = ScheduleFactory.eINSTANCE.createPurge();
			purge.setPort(ePort);

			int purgeDuration = voyageDetails.getOptions().getExtraIdleTime(ExplicitIdleTime.PURGE);
			purge.setStart(modelEntityMap.getDateFromHours(currentTime, ePort));
			purge.setEnd(modelEntityMap.getDateFromHours(currentTime + purgeDuration, ePort));

			return purge;
		}

		return null;
	}
}
