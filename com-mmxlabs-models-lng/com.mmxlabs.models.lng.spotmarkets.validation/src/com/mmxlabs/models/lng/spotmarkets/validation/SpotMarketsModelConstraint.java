/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.validation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotAvailability;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.lng.spotmarkets.validation.internal.Activator;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class SpotMarketsModelConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		final List<IStatus> failures = new LinkedList<>();
		
		if (target instanceof SpotMarketsModel) {
			final SpotMarketsModel spotMarketsModel = (SpotMarketsModel) target;
			final Map<String, SpotMarket> spotMarketNames = new HashMap<>();
			checkMarketGroup(ctx, failures, spotMarketNames, spotMarketsModel.getDesPurchaseSpotMarket());
			checkMarketGroup(ctx, failures, spotMarketNames, spotMarketsModel.getDesSalesSpotMarket());
			checkMarketGroup(ctx, failures, spotMarketNames, spotMarketsModel.getFobPurchasesSpotMarket());
			checkMarketGroup(ctx, failures, spotMarketNames, spotMarketsModel.getFobSalesSpotMarket());
		}

		if (failures.isEmpty()) {
			return ctx.createSuccessStatus();
		} else if (failures.size() == 1) {
			return failures.get(0);
		} else {
			final MultiStatus multi = new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR, null, null);
			for (final IStatus s : failures) {
				multi.add(s);
			}
			return multi;
		}
	}

	private void checkMarketGroup(final IValidationContext ctx, final List<IStatus> failures, final Map<String, SpotMarket> spotMarketNames, final SpotMarketGroup smg) {
		for (final SpotMarket sm : smg.getMarkets()) {
			if (spotMarketNames.get(sm.getName())!=null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) 
						ctx.createFailureStatus(String.format("Market %s has a non-unique name", sm.getName())));
				
				dsd.addEObjectAndFeature(sm, MMXCorePackage.eINSTANCE.getNamedObject_Name());
				dsd.addEObjectAndFeature(spotMarketNames.get(sm.getName()), MMXCorePackage.eINSTANCE.getNamedObject_Name());
				failures.add(dsd);
			}
			spotMarketNames.put(sm.getName(), sm);
		}
	}
}
