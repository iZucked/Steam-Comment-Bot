/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters;
import com.mmxlabs.models.lng.commercial.VolumeTierSlotParams;

public class VolumeTierContractCommandProvider extends AbstractSlotContractCommandProvider<VolumeTierPriceParameters, VolumeTierSlotParams> {

	public VolumeTierContractCommandProvider() {
		super(VolumeTierPriceParameters.class, VolumeTierSlotParams.class, CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS, CommercialFactory.eINSTANCE,
				AbstractSlotContractCommandProvider.FILTER_NonSpotSlotFilter);
	}
}
