/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.vesselcharters;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IVesselCharterTransformer;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapEntityProviderEditor;

@NonNullByDefault
public class VesselCharterEntityTransformer implements IVesselCharterTransformer {

	@Inject
	private ModelEntityMap modelEntityMap;

	@Inject
	private HashMapEntityProviderEditor entityProvider;

	@Override
	public void vesselCharterTransformed(VesselCharter eVesselCharter, IVesselCharter vesselCharter) {
		final IEntity entity = modelEntityMap.getOptimiserObjectNullChecked(eVesselCharter.getCharterOrDelegateEntity(), IEntity.class);
		entityProvider.setEntityForVesselCharter(entity, vesselCharter);
	}

	@Override
	public void charterInVesselCharterTransformed(CharterInMarket charterInMarket, IVesselCharter vesselCharter) {
		if (charterInMarket.getEntity() != null) {
			final IEntity entity = modelEntityMap.getOptimiserObjectNullChecked(charterInMarket.getEntity(), IEntity.class);
			entityProvider.setEntityForVesselCharter(entity, vesselCharter);
		}
	}
}
