/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.importer;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 * Customise the contract imports by setting default pricing event values.
 * 
 * @author Simon Goodall
 * 
 */
public class ContractClassImporter extends DefaultClassImporter {
	@Override
	protected void importAttributes(final Map<String, String> row, final IMMXImportContext context, final EClass rowClass, final EObject instance) {

		// Set pricing event defaults
		if (instance instanceof PurchaseContract) {
			final PurchaseContract purchaseContract = (PurchaseContract) instance;
			purchaseContract.setPricingEvent(PricingEvent.START_LOAD);
		} else if (instance instanceof SalesContract) {
			final SalesContract salesContract = (SalesContract) instance;
			salesContract.setPricingEvent(PricingEvent.START_DISCHARGE);
		}

		super.importAttributes(row, context, rowClass, instance);
	}
}
