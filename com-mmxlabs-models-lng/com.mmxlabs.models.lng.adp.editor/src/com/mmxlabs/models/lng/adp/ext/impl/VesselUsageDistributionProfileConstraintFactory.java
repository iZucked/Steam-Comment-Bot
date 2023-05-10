/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.VesselUsageDistributionProfileConstraint;
import com.mmxlabs.models.lng.adp.ext.IProfileConstraintFactory;

public class VesselUsageDistributionProfileConstraintFactory implements IProfileConstraintFactory {
	@Override
	public String getName() {
		return "By vessels";
	}

	@Override
	public boolean validFor(ContractProfile<?, ?> profile) {
		return profile instanceof SalesContractProfile;
	}

	@Override
	public VesselUsageDistributionProfileConstraint createInstance() {
		return ADPFactory.eINSTANCE.createVesselUsageDistributionProfileConstraint();
	}
}
