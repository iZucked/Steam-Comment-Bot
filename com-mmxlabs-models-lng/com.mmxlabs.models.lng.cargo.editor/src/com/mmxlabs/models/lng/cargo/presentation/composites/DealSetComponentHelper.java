/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

public class DealSetComponentHelper extends DefaultComponentHelper {

	public DealSetComponentHelper() {
		super(CargoPackage.Literals.DEAL_SET);
		
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_INDIVIDUAL_EXPOSURES)) {
			ignoreFeatures.add(CargoPackage.eINSTANCE.getDealSet_AllowExposure());
			ignoreFeatures.add(CargoPackage.eINSTANCE.getDealSet_AllowHedging());
		}
	}

}
