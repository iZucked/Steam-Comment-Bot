/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.modelfactories;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;

/**
 */
public class PurchaseContractModelFactory extends ContractModelFactory {
	@Override
	public void initFromExtension(final String ID, final String label, final String prototype, final String replacementReference, final String replacementClass) {
		super.initFromExtension(ID, label, "com.mmxlabs.models.lng.commercial.PurchaseContract", replacementReference, replacementClass);
		this.priceInfoClassName = prototype;
	}

	@Override
	protected EObject constructInstance(final EClass eClass) {
		final EObject instance = super.constructInstance(eClass);

		if (instance instanceof PurchaseContract) {
			final PurchaseContract purchaseContract = (PurchaseContract) instance;
			purchaseContract.setPricingEvent(PricingEvent.START_LOAD);
		}

		return instance;
	}
}
