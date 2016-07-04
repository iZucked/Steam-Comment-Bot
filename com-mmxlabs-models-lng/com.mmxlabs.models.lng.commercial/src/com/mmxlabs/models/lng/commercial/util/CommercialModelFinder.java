/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;

public class CommercialModelFinder {
	private final @NonNull CommercialModel commercialModel;

	public CommercialModelFinder(final @NonNull CommercialModel comercialModel) {
		this.commercialModel = comercialModel;
	}

	@NonNull
	public CommercialModel getCommercialModel() {
		return commercialModel;
	}

	@NonNull
	public BaseLegalEntity findEntity(@NonNull final String name) {
		for (final BaseLegalEntity entity : getCommercialModel().getEntities()) {
			if (name.equals(entity.getName())) {
				return entity;
			}
		}
		throw new IllegalArgumentException("Unknown entity");
	}

	@NonNull
	public PurchaseContract findPurchaseContract(@NonNull String name) {
		for (final PurchaseContract contract : getCommercialModel().getPurchaseContracts()) {
			if (name.equals(contract.getName())) {
				return contract;
			}
		}
		throw new IllegalArgumentException("Unknown contract " + name);
	}

	@NonNull
	public SalesContract findSalesContract(@NonNull String name) {
		for (final SalesContract contract : getCommercialModel().getSalesContracts()) {
			if (name.equals(contract.getName())) {
				return contract;
			}
		}
		throw new IllegalArgumentException("Unknown contract " + name);
	}
}
