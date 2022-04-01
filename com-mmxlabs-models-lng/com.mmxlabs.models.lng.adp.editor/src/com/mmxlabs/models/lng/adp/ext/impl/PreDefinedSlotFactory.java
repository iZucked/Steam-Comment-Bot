/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
public class PreDefinedSlotFactory implements IDistributionModelFactory {

	@Override
	public String getName() {
		return "Pre-defined";
	}

	@Override
	public boolean isMatchForCurrent(SubContractProfile<?, ?> rule) {

		return ADPPackage.Literals.PRE_DEFINED_DISTRIBUTION_MODEL.isInstance(rule.getDistributionModel());
	}

	@Override
	public DistributionModel createInstance() {
		return ADPFactory.eINSTANCE.createPreDefinedDistributionModel();
	}
}
