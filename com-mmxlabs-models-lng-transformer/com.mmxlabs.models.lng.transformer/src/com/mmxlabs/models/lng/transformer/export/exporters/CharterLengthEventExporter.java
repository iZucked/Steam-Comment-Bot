/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export.exporters;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.FuelExportHelper;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.voyage.ExplicitIdleTime;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

/**
 * Exports idleevent annotations.
 * 
 * @author hinton
 * 
 */
public class CharterLengthEventExporter {

	@Inject
	private ModelEntityMap modelEntityMap;

	public void update(CharterLengthEvent event, final VoyageDetails voyageDetails, VolumeAllocatedSequence volumeAllocatedSequence, int currentTime) {
		final @NonNull VoyageOptions options = voyageDetails.getOptions();
		Port ePort = modelEntityMap.getModelObject(options.getToPortSlot().getPort(), Port.class);

		// TODO this is a bit of a kludge; the ANYWHERE port does not
		// have an EMF representation, but we do want idle time for it
		// so we assume if we hit a dubious port it's ANYWHERE and that
		// we are really where we used to be.
		if (ePort == null) {
			ePort = modelEntityMap.getModelObject(options.getFromPortSlot().getPort(), Port.class);
		}

		event.setPort(ePort);
		event.setStart(modelEntityMap.getDateFromHours(currentTime, options.getToPortSlot().getPort()));
		event.setEnd(modelEntityMap.getDateFromHours(currentTime + voyageDetails.getIdleTime(), options.getToPortSlot().getPort()));
		event.setDuration(voyageDetails.getIdleTime());

		event.setLaden(VesselState.Laden.equals(options.getVesselState()));

		event.getFuels().addAll(exportFuelData(voyageDetails));
		
		event.setContingencyHours(voyageDetails.getOptions().getExtraIdleTime(ExplicitIdleTime.CONTINGENCY));
	}

	private List<FuelQuantity> exportFuelData(VoyageDetails details) {
		return FuelExportHelper.exportFuelData(details, details.getOptions().getVessel(), FuelExportHelper.idleFuelComponentNames, VoyageDetails::getFuelConsumption, VoyageDetails::getFuelUnitPrice,
				modelEntityMap);
	}
}
