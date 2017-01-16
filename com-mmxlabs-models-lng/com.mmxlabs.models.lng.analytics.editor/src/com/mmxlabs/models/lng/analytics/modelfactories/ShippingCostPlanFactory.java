/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.modelfactories;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.DestinationType;
import com.mmxlabs.models.lng.analytics.ShippingCostPlan;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.ui.modelfactories.DefaultModelFactory;

public class ShippingCostPlanFactory extends DefaultModelFactory {

	@Override
	protected void postprocess(final EObject top) {
		super.postprocess(top);

		if (top instanceof ShippingCostPlan) {
			final ShippingCostPlan shippingCostPlan = (ShippingCostPlan) top;

			final ShippingCostRow startRow = (ShippingCostRow) constructInstance(AnalyticsPackage.eINSTANCE.getShippingCostRow());
			postprocess(startRow);
			startRow.setDestinationType(DestinationType.START);
			shippingCostPlan.getRows().add(startRow);

			final ShippingCostRow endRow = (ShippingCostRow) constructInstance(AnalyticsPackage.eINSTANCE.getShippingCostRow());
			postprocess(endRow);
			endRow.setDestinationType(DestinationType.END);
			shippingCostPlan.getRows().add(endRow);
		}
	}
}
