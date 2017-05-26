/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.validation;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.commercial.BallastBonusCharterContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.lng.spotmarkets.validation.internal.Activator;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class CharterInMarketConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		final List<IStatus> failures = new LinkedList<IStatus>();

		if (target instanceof CharterInMarket) {
			final CharterInMarket spotMarket = (CharterInMarket) target;
			if (spotMarket.getCharterContract() != null && spotMarket.getCharterContract() instanceof BallastBonusCharterContract) {
				BallastBonusCharterContract ballastBonusCharterContract = (BallastBonusCharterContract) spotMarket.getCharterContract();
				if (spotMarket.isNominal()) {
					//nominals
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("[Charter in market vessel | %s] has nominals enabled and a ballast bonus chartering contract.", spotMarket.getName())));
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterInMarkets());
					failures.add(dsd);
				}
				if (ballastBonusCharterContract.getEntity() == null) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("[Charter in market vessel | %s] uses a ballast bonus chartering contract without an entity set.", spotMarket.getName())));
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterInMarkets());
					failures.add(dsd);
				}
			}
		}
		final MultiStatus multi = new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR, null, null);
		for (final IStatus s : failures) {
			multi.add(s);
		}
		return multi;
	}
}
