/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.MinCargoConstraint;
import com.mmxlabs.models.lng.adp.ext.IProfileConstraintFactory;

public class MinCargoProfileConstraintFactory implements IProfileConstraintFactory {

	@Override
	public String getName() {
		return "Min cargoes";
	}

	@Override
	public boolean validFor(ContractProfile<?, ?> profile) {
		return true;
	}

	@Override
	public MinCargoConstraint createInstance() {
		return ADPFactory.eINSTANCE.createMinCargoConstraint();
	}
}
