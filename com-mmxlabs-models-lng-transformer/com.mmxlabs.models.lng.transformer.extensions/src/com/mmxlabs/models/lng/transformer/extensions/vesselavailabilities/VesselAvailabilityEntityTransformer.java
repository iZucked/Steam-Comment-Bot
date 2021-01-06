/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.vesselavailabilities;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IVesselAvailabilityTransformer;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapEntityProviderEditor;

public class VesselAvailabilityEntityTransformer implements IVesselAvailabilityTransformer {

	ModelEntityMap modelEntityMap;
	
	@Inject
	private HashMapEntityProviderEditor entityProvider;

	@Override
	public void startTransforming(LNGScenarioModel rootObject, ModelEntityMap modelEntityMap, ISchedulerBuilder builder) {
		this.modelEntityMap = modelEntityMap;
	}

	@Override
	public void finishTransforming() {
		// TODO Auto-generated method stub

	}

	@Override
	public void vesselAvailabilityTransformed(@NonNull VesselAvailability eVesselAvailability, @NonNull IVesselAvailability vesselAvailability) {
		final IEntity entity = modelEntityMap.getOptimiserObjectNullChecked(eVesselAvailability.getCharterOrDelegateEntity(), IEntity.class);
		entityProvider.setEntityForVesselAvailability(entity, vesselAvailability);
	}

	@Override
	public void charterInVesselAvailabilityTransformed(@NonNull CharterInMarket charterInMarket, @NonNull IVesselAvailability vesselAvailability) {
		if (charterInMarket.getEntity() != null) {
			final IEntity entity = modelEntityMap.getOptimiserObjectNullChecked(charterInMarket.getEntity(), IEntity.class);
			entityProvider.setEntityForVesselAvailability(entity, vesselAvailability);
		}
	}
}
