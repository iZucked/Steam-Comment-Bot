/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint;
import com.mmxlabs.models.lng.adp.ext.IProfileConstraintFactory;

public class PeriodDistributionProfileConstraintFactory implements IProfileConstraintFactory {
	@Override
	public String getName() {
		return "By period";
	}

	@Override
	public boolean validFor(ContractProfile<?, ?> profile) {
		return true;
	}

	@Override
	public PeriodDistributionProfileConstraint createInstance() {
		return ADPFactory.eINSTANCE.createPeriodDistributionProfileConstraint();
	}
}
