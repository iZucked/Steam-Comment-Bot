/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.FlowType;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.ISubProfileConstraintFactory;

public class SupplyFromProfileFlowFactory implements ISubProfileConstraintFactory {

	@Override
	public String getName() {
		return "Supply From Profile";
	}

	@Override
	public boolean validFor(ContractProfile<?, ?> profile, SubContractProfile<?, ?> subContractProfile) {
		return profile instanceof SalesContractProfile;
	}

	@Override
	public FlowType createInstance() {
		return ADPFactory.eINSTANCE.createSupplyFromProfileFlow();
	}
}
