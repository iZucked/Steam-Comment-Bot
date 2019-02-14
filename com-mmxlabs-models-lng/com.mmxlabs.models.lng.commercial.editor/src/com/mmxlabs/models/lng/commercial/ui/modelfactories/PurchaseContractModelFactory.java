/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.modelfactories;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
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
			{
				EClass priceInfoClass = getEClassFromName(priceInfoClassName, null);
				// return a price info object of the specified type
				if (priceInfoClass != null) {
					purchaseContract.setPriceInfo((LNGPriceCalculatorParameters) priceInfoClass.getEPackage().getEFactoryInstance().create(priceInfoClass));
				} else {

					purchaseContract.setPriceInfo((LNGPriceCalculatorParameters) CommercialPackage.eINSTANCE.getEFactoryInstance().create(CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS));
				}
			}
		}

		return instance;
	}
}
