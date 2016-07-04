/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.util;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;

public class FleetModelBuilder {
	private @NonNull final FleetModel fleetModel;

	public FleetModelBuilder(@NonNull final FleetModel fleetModel) {
		this.fleetModel = fleetModel;
	}

	/**
	 * Creates a heel options with default settings.
	 * 
	 * @return
	 */
	public @NonNull HeelOptions createHeelOptions(@Nullable final Double startHeelVolumeInM3, final double cv, final double pricePerMMBTu) {
		final HeelOptions result = FleetFactory.eINSTANCE.createHeelOptions();
		result.setCvValue(cv);
		result.setPricePerMMBTU(pricePerMMBTu);
		if (startHeelVolumeInM3 != null) {
			result.setVolumeAvailable(startHeelVolumeInM3);
		}
		return result;
	}

	@NonNull
	public Vessel createVessel(@NonNull final String name, @NonNull final VesselClass vesselClass) {
		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		vessel.setName(name);
		vessel.setVesselClass(vesselClass);

		fleetModel.getVessels().add(vessel);

		return vessel;
	}

	@NonNull
	public Vessel createVessel(@NonNull final String name, @NonNull final VesselClass vesselClass, final int capacityInM3, final double fillPercent) {
		final Vessel vessel = createVessel(name, vesselClass);
		vessel.setCapacity(capacityInM3);
		vessel.setFillCapacity(fillPercent);
		return vessel;
	}

	@NonNull
	public BaseFuel createBaseFuel(@NonNull final String name, final double equivalenceFactor) {
		final BaseFuel baseFuel = FleetFactory.eINSTANCE.createBaseFuel();
		baseFuel.setName(name);
		baseFuel.setEquivalenceFactor(equivalenceFactor);

		fleetModel.getBaseFuels().add(baseFuel);
		return baseFuel;
	}
}
