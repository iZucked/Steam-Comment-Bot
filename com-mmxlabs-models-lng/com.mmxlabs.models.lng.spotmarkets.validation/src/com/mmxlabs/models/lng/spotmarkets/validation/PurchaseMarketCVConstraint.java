/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class PurchaseMarketCVConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof DESPurchaseMarket) {
			final DESPurchaseMarket market = (DESPurchaseMarket) target;

			if (market.getCv() < 1.0) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("CV value it too low."));
				dsd.addEObjectAndFeature(market, SpotMarketsPackage.eINSTANCE.getDESPurchaseMarket_Cv());
				return dsd;
			} else if (market.getCv() > 40.0) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("CV value is too high."));
				dsd.addEObjectAndFeature(market, SpotMarketsPackage.eINSTANCE.getDESPurchaseMarket_Cv());
				return dsd;
			}

		} else if (target instanceof FOBPurchasesMarket) {
			final FOBPurchasesMarket market = (FOBPurchasesMarket) target;

			if (market.getCv() < 1.0) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("CV value it too low."));
				dsd.addEObjectAndFeature(market, SpotMarketsPackage.eINSTANCE.getFOBPurchasesMarket_Cv());
				return dsd;
			} else if (market.getCv() > 40.0) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("CV value is too high."));
				dsd.addEObjectAndFeature(market, SpotMarketsPackage.eINSTANCE.getFOBPurchasesMarket_Cv());
				return dsd;
			}

		}
		return ctx.createSuccessStatus();
	}
}
