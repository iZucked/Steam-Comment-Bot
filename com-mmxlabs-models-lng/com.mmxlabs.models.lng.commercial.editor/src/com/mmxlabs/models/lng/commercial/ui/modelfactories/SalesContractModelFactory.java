/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.modelfactories;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.SalesContract;

/**
 */
public class SalesContractModelFactory extends ContractModelFactory {
	@Override
	public void initFromExtension(final String ID, final String label, final String prototype, String replacementReference, String replacementClass) {
		super.initFromExtension(ID, label, "com.mmxlabs.models.lng.commercial.SalesContract", replacementReference, replacementClass);
		this.priceInfoClassName = prototype;
	}

	@Override
	protected EObject constructInstance(final EClass eClass) {
		final EObject instance = super.constructInstance(eClass);

		if (instance instanceof SalesContract) {
			final SalesContract salesContract = (SalesContract) instance;
			salesContract.setPricingEvent(PricingEvent.START_DISCHARGE);
			{
				EClass priceInfoClass = getEClassFromName(priceInfoClassName, null);
				// return a price info object of the specified type
				if (priceInfoClass != null) {
					salesContract.setPriceInfo((LNGPriceCalculatorParameters) priceInfoClass.getEPackage().getEFactoryInstance().create(priceInfoClass));
				} else {

					salesContract.setPriceInfo((LNGPriceCalculatorParameters) CommercialPackage.eINSTANCE.getEFactoryInstance().create(CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS));
				}
			}
		}

		return instance;
	}
}
