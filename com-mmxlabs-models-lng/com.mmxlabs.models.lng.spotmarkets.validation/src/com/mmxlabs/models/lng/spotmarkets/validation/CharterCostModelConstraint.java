/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class CharterCostModelConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(IValidationContext ctx, final IExtraValidationContext extraContext, List<IStatus> statuses) {
		final EObject object = ctx.getTarget();
		if (object instanceof CharterInMarket) {
			CharterInMarket ccm = (CharterInMarket) object;
			if (ccm.getVesselClass() == null) {
				final String failureMessage = "A charter cost model needs to be associated with at least one vessel class.";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
				dsd.addEObjectAndFeature(ccm, SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET__VESSEL_CLASS);
				statuses.add(dsd);
			}

			if (ccm.getCharterInPrice() == null) {
				final String failureMessage = "A charter in curve must be specified.";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
				dsd.addEObjectAndFeature(ccm, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__CHARTER_IN_PRICE);
				statuses.add(dsd);
			}

//			if (ccm.getSpotCharterCount() > 0) {
//				final String failureMessage = "A charter in curve must be specified if charter in count is greater than zero.";
//				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
//				dsd.addEObjectAndFeature(ccm, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT);
//				statuses.add(dsd);
//			}

		}
		if (object instanceof CharterOutMarket) {
			CharterOutMarket ccm = (CharterOutMarket) object;
			if (ccm.getVesselClass() == null) {
				final String failureMessage = "A charter cost model needs to be associated with at least one vessel class.";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
				dsd.addEObjectAndFeature(ccm, SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET__VESSEL_CLASS);
				statuses.add(dsd);
			}

			if (ccm.getCharterOutPrice() == null) {
				final String failureMessage = "A charter out curve must be specified.";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
				dsd.addEObjectAndFeature(ccm, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET__CHARTER_OUT_PRICE);
				statuses.add(dsd);
			}

			if (ccm.getMinCharterOutDuration() == 0) {
				final String failureMessage = "A minimum charter out duration must be specified.";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
				dsd.addEObjectAndFeature(ccm, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET__MIN_CHARTER_OUT_DURATION);
				statuses.add(dsd);
			}
		}
		return Activator.PLUGIN_ID;
	}
}
