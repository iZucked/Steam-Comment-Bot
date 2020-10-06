/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public class DefaultADPProfileProvider extends AbstractADPProfileProvider {

	@Override
	public @Nullable PurchaseContractProfile createProfile(final LNGScenarioModel scenarioModel, final CommercialModel commercialModel, final ADPModel existingModel, PurchaseContract contract) {
		return createGenericModel(contract);
	}

	@Override
	public @Nullable SalesContractProfile createProfile(final LNGScenarioModel scenarioModel, final CommercialModel commercialModel, final ADPModel existingModel, SalesContract contract) {
		return createGenericModel(contract);
	}

	protected PurchaseContractProfile createGenericModel(final PurchaseContract contract) {

		LNGVolumeUnit unit = mapVolumeUnits(contract.getVolumeLimitsUnit());
		int volume = contract.getMaxQuantity();
		if (volume == 0) {
			volume = (unit == LNGVolumeUnit.MMBTU) ? 3_000_000 : 150_000;
		}

		// Base
		final PurchaseContractProfile profile = createGenericModel(contract, volume * 12, unit);

		final SubContractProfile<LoadSlot, PurchaseContract> subProfile = createCargoNumberSubProfile(contract, contract.getContractType(), "Volume", 12, volume, unit);
		subProfile.setPort(contract.getPreferredPort());
		
		profile.getSubProfiles().add(subProfile);

		return profile;
	}

	protected SalesContractProfile createGenericModel(final SalesContract contract) {

		LNGVolumeUnit unit = mapVolumeUnits(contract.getVolumeLimitsUnit());
		int volume = contract.getMaxQuantity();
		if (volume == 0) {
			volume = (unit == LNGVolumeUnit.MMBTU) ? 3_000_000 : 150_000;
		}

		// Base
		final SalesContractProfile profile = createGenericModel(contract, volume * 12, unit);

		final SubContractProfile<DischargeSlot, SalesContract> subProfile = createCargoNumberSubProfile(contract, contract.getContractType(), "Volume", 12, volume, unit);
		subProfile.setPort(contract.getPreferredPort());
		
		profile.getSubProfiles().add(subProfile);

		return profile;
	}

}
