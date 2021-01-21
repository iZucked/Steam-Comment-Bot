/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.PurchaseContract;

public class PurchaseContractProfileMaker extends AbstractContractProfileMaker<PurchaseContractProfileMaker, PurchaseContractProfile, LoadSlot, PurchaseContract> {

	public static @NonNull PurchaseContractProfileMaker make(final @NonNull ADPModelBuilder adpModelBuilder, final @NonNull PurchaseContract contract) {
		final PurchaseContractProfile contractProfile = ADPFactory.eINSTANCE.createPurchaseContractProfile();
		contractProfile.setContract(contract);
		contractProfile.setContractCode(contract.getCode());
		contractProfile.setEnabled(true);

		final PurchaseContractProfileMaker maker = new PurchaseContractProfileMaker(adpModelBuilder, contractProfile);

		return maker;
	}

	private PurchaseContractProfileMaker(final @NonNull ADPModelBuilder adpModelBuilder, final @NonNull PurchaseContractProfile contractProfile) {
		super(adpModelBuilder, contractProfile);
	}
}
