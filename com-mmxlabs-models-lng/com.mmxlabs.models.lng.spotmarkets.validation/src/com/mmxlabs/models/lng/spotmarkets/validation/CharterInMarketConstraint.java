/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.validation;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.commercial.BallastBonusCharterContract;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.validation.internal.Activator;
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
					// nominals
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx
							.createFailureStatus(String.format("[Charter in market vessel | %s] has nominals enabled and a ballast bonus chartering contract.", spotMarket.getName())));
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterInMarkets());
					failures.add(dsd);
				}
				if (ballastBonusCharterContract.getEntity() == null) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx
							.createFailureStatus(String.format("[Charter in market vessel | %s] uses a ballast bonus chartering contract without an entity set.", spotMarket.getName())));
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterInMarkets());
					failures.add(dsd);
				}
			}

			int minDurationInHours = spotMarket.getMarketOrContractMinDuration();
			int maxDurationInHours = spotMarket.getMarketOrContractMaxDuration();

			if (minDurationInHours != 0 || maxDurationInHours != 0) {
				if (spotMarket.isNominal()) {
					// nominals
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("[Charter in market vessel | %s] has nominals enabled and a min or max duration.", spotMarket.getName())));
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterInMarkets());
					failures.add(dsd);
				}
			}
			if (minDurationInHours != 0 && maxDurationInHours != 0) {
				if (minDurationInHours > maxDurationInHours) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("[Charter in market vessel | %s] has min duration superior to the max duration.", spotMarket.getName())));
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterInMarkets());
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getCharterInMarket_MinDuration());
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getCharterInMarket_MaxDuration());
					failures.add(dsd);
				}
			}
			if (spotMarket.isMtm()) {
				if (!spotMarket.isNominal()) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("[Charter in market vessel | %s] is used for MTM but not Nominal.", spotMarket.getName())));
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterInMarkets());
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getCharterInMarket_Mtm());
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getCharterInMarket_Nominal());
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
