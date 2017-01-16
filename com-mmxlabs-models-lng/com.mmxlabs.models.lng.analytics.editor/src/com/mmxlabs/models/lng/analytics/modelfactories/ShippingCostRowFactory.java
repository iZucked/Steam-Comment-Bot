/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.modelfactories;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.analytics.DestinationType;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.ui.modelfactories.DefaultModelFactory;

public class ShippingCostRowFactory extends DefaultModelFactory {

	@Override
	protected void postprocess(final EObject top) {
		super.postprocess(top);

		if (top instanceof ShippingCostRow) {
			final ShippingCostRow shippingCostRow = (ShippingCostRow) top;
			shippingCostRow.setDestinationType(DestinationType.OTHER);
			shippingCostRow.setIncludePortCosts(true);
		}
	}
}
