/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.commercial.SalesContract;

public class SalesContractProfileMaker extends AbstractContractProfileMaker<SalesContractProfileMaker, SalesContractProfile, DischargeSlot, SalesContract> {

	public static @NonNull SalesContractProfileMaker make(final @NonNull ADPModelBuilder adpModelBuilder, final @NonNull SalesContract contract) {
		final SalesContractProfile contractProfile = ADPFactory.eINSTANCE.createSalesContractProfile();
		contractProfile.setContract(contract);
		contractProfile.setContractCode(contract.getCode());
		contractProfile.setEnabled(true);

		final SalesContractProfileMaker maker = new SalesContractProfileMaker(adpModelBuilder, contractProfile);

		return maker;
	}

	private SalesContractProfileMaker(final @NonNull ADPModelBuilder adpModelBuilder, final @NonNull SalesContractProfile contractProfile) {
		super(adpModelBuilder, contractProfile);
	}
}
