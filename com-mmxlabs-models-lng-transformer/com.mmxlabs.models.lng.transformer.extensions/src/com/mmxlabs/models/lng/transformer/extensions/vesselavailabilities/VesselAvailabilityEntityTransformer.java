/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.vesselavailabilities;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IVesselAvailabilityTransformer;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapEntityProviderEditor;

@NonNullByDefault
public class VesselAvailabilityEntityTransformer implements IVesselAvailabilityTransformer {

	@Inject
	private ModelEntityMap modelEntityMap;

	@Inject
	private HashMapEntityProviderEditor entityProvider;

	@Override
	public void vesselAvailabilityTransformed(VesselAvailability eVesselAvailability, IVesselAvailability vesselAvailability) {
		final IEntity entity = modelEntityMap.getOptimiserObjectNullChecked(eVesselAvailability.getCharterOrDelegateEntity(), IEntity.class);
		entityProvider.setEntityForVesselAvailability(entity, vesselAvailability);
	}

	@Override
	public void charterInVesselAvailabilityTransformed(CharterInMarket charterInMarket, IVesselAvailability vesselAvailability) {
		if (charterInMarket.getEntity() != null) {
			final IEntity entity = modelEntityMap.getOptimiserObjectNullChecked(charterInMarket.getEntity(), IEntity.class);
			entityProvider.setEntityForVesselAvailability(entity, vesselAvailability);
		}
	}
}
