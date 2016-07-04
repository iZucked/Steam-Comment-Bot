/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.modelfactories;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.ui.modelfactories.DefaultModelFactory;

/**
 */
public class ContractModelFactory extends DefaultModelFactory {
	protected String priceInfoClassName;
	
	@Override
	protected EObject createSubInstance(final EObject top, final EReference reference) {
		if (reference.equals(CommercialPackage.Literals.CONTRACT__PRICE_INFO)) {
			EClass priceInfoClass = getEClassFromName(priceInfoClassName, null);
			// return a price info object of the specified type
			if (priceInfoClass != null) {
				return priceInfoClass.getEPackage().getEFactoryInstance().create(priceInfoClass);
			}
			
			return CommercialPackage.eINSTANCE.getEFactoryInstance().create(CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS);
			
		}
		return super.createSubInstance(top, reference);
	}
	

}
