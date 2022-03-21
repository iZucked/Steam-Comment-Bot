/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SpotMarketsModelConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> failures) {

		final EObject target = ctx.getTarget();

		if (target instanceof SpotMarketsModel spotMarketsModel) {
			final Map<String, SpotMarket> spotMarketNames = new HashMap<>();
			checkMarketGroup(ctx, failures, spotMarketNames, spotMarketsModel.getDesPurchaseSpotMarket());
			checkMarketGroup(ctx, failures, spotMarketNames, spotMarketsModel.getDesSalesSpotMarket());
			checkMarketGroup(ctx, failures, spotMarketNames, spotMarketsModel.getFobPurchasesSpotMarket());
			checkMarketGroup(ctx, failures, spotMarketNames, spotMarketsModel.getFobSalesSpotMarket());
		}
	}

	private void checkMarketGroup(final IValidationContext ctx, final List<IStatus> failures, final Map<String, SpotMarket> spotMarketNames, final SpotMarketGroup smg) {
		for (final SpotMarket sm : smg.getMarkets()) {
			if (spotMarketNames.get(sm.getName()) != null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("Market %s has a non-unique name", sm.getName())));

				dsd.addEObjectAndFeature(sm, MMXCorePackage.eINSTANCE.getNamedObject_Name());
				dsd.addEObjectAndFeature(spotMarketNames.get(sm.getName()), MMXCorePackage.eINSTANCE.getNamedObject_Name());
				failures.add(dsd);
			}
			spotMarketNames.put(sm.getName(), sm);
		}
	}
}
