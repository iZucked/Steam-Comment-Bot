/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.ProfileVesselRestriction;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.ISubProfileConstraintFactory;

public class VesselRestrictionFactory implements ISubProfileConstraintFactory {

	@Override
	public String getName() {
		return "Vessel restriction";
	}

	@Override
	public boolean validFor(ContractProfile<?, ?> profile, SubContractProfile<?, ?> subContractProfile) {
		return true;
	}

	@Override
	public ProfileVesselRestriction createInstance() {
		return ADPFactory.eINSTANCE.createProfileVesselRestriction();
	}

}
