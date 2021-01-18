/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.MaxCargoConstraint;
import com.mmxlabs.models.lng.adp.ext.IProfileConstraintFactory;

public class MaxCargoProfileConstraintFactory implements IProfileConstraintFactory {

	@Override
	public String getName() {
		return "Max cargoes";
	}

	@Override
	public boolean validFor(ContractProfile<?, ?> profile) {
		return true;
	}

	@Override
	public MaxCargoConstraint createInstance() {
		return ADPFactory.eINSTANCE.createMaxCargoConstraint();
	}
}
