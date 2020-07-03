/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DistributionModel;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.IDistributionModelFactory;

@NonNullByDefault
public class CargoSizeFactory implements IDistributionModelFactory {
	@Override
	public String getName() {
		return "Volume per cargo";
	}

	@Override
	public boolean isMatchForCurrent(SubContractProfile<?, ?> rule) {

		return ADPPackage.Literals.CARGO_SIZE_DISTRIBUTION_MODEL.isInstance(rule.getDistributionModel());
	}

	@Override
	public DistributionModel createInstance() {
		return ADPFactory.eINSTANCE.createCargoSizeDistributionModel();
	}
}
