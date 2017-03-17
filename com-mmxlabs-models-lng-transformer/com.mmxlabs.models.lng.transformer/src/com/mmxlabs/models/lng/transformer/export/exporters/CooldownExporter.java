/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export.exporters;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class CooldownExporter {

	@Inject
	private ModelEntityMap modelEntityMap;

	public @Nullable Cooldown export(final VoyageDetails voyageDetails, final VolumeAllocatedSequence volumeAllocatedSequence, final int currentTime) {

		if (voyageDetails.isCooldownPerformed()) {
			final long cooldownVolume = voyageDetails.getFuelConsumption(FuelComponent.Cooldown, FuelUnit.M3);

			Port ePort = modelEntityMap.getModelObject(voyageDetails.getOptions().getToPortSlot().getPort(), Port.class);

			// TODO this is a bit of a kludge; the ANYWHERE port does not
			// have an EMF representation, but we do want idle time for it
			// so we assume if we hit a dubious port it's ANYWHERE and that
			// we are really where we used to be.
			if (ePort == null) {
				ePort = modelEntityMap.getModelObject(voyageDetails.getOptions().getFromPortSlot().getPort(), Port.class);
			}

			final Cooldown cooldown = ScheduleFactory.eINSTANCE.createCooldown();
			cooldown.setPort(ePort);
			cooldown.setVolume(OptimiserUnitConvertor.convertToExternalVolume(cooldownVolume));

			final VoyagePlan voyagePlan = volumeAllocatedSequence.getVoyagePlan(voyageDetails.getOptions().getFromPortSlot());

			cooldown.setCost(OptimiserUnitConvertor.convertToExternalFixedCost(voyagePlan.getTotalFuelCost(FuelComponent.Cooldown)));
			cooldown.setStart(modelEntityMap.getDateFromHours(currentTime, ePort));
			cooldown.setEnd(modelEntityMap.getDateFromHours(currentTime, ePort));

			return cooldown;
		}

		return null;
	}
}
