/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.FlowType;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.ISubProfileConstraintFactory;

public class DeliverToProfileFlowFactory implements ISubProfileConstraintFactory {

	@Override
	public String getName() {
		return "Deliver To Profile";
	}

	@Override
	public boolean validFor(ContractProfile<?, ?> profile, SubContractProfile<?, ?> subContractProfile) {
		return profile instanceof PurchaseContractProfile;
	}

	@Override
	public FlowType createInstance() {
		return ADPFactory.eINSTANCE.createDeliverToProfileFlow();
	}
}
