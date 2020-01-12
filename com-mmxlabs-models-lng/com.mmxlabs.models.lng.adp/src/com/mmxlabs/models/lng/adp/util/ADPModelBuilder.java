/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.util;

import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.FleetProfile;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;

public class ADPModelBuilder {
	private final @NonNull ADPModel adpModel;

	public static @NonNull ADPModel initialise(final @NonNull YearMonth startInclusive, final @NonNull YearMonth endExclusive) {
		final ADPModel adpModel = ADPFactory.eINSTANCE.createADPModel();
		final FleetProfile fleetProfile = ADPFactory.eINSTANCE.createFleetProfile();
		adpModel.setFleetProfile(fleetProfile);

		adpModel.setYearStart(startInclusive);
		adpModel.setYearEnd(endExclusive);

		return adpModel;
	}

	public ADPModelBuilder(final @NonNull ADPModel adpModel) {
		this.adpModel = adpModel;
	}

	@NonNull
	public ADPModel getADPModel() {
		return adpModel;
	}

	public void setADPYear(final YearMonth startInclusive, final YearMonth endExclusive) {
		adpModel.setYearStart(startInclusive);
		adpModel.setYearEnd(endExclusive);
	}

	public @NonNull PurchaseContractProfileMaker withPurchaseContractProfile(final @NonNull PurchaseContract contract) {
		return PurchaseContractProfileMaker.make(this, contract);
	}

	public @NonNull SalesContractProfileMaker withSalesContractProfile(final @NonNull SalesContract contract) {
		return SalesContractProfileMaker.make(this, contract);
	}

}
