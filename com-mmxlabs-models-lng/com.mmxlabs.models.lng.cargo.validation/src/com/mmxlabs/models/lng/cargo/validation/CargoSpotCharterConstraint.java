/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * @author Simon Goodall
 * 
 */
public class CargoSpotCharterConstraint extends AbstractModelMultiConstraint {

	private boolean isMatchingSpotMarketExist(final MMXRootObject rootObject, final CharterInMarket vc) {
//		if (rootObject instanceof LNGScenarioModel) {
//			final SpotMarketsModel spotModel = ((LNGScenarioModel) rootObject).getSpotMarketsModel();
//			if (spotModel == null) {
//				return false;
//			}
//			for (final CharterInMarket market : spotModel.getCharterInMarkets()) {
//				if (!market.isEnabled()) {
//					continue;
//				}
//
//				if (market.getVesselClass().equals(vc)) {
//					return true;
//				}
//			}
//		}
		return true;
	}

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof Cargo) {
			final Cargo cargo = (Cargo) target;
			final VesselAssignmentType vesselAssignmentType = cargo.getVesselAssignmentType();
			if (vesselAssignmentType instanceof CharterInMarket) {
				final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
				// Commented out as part of BugzId:  1112 - spot market should always exist as it is part of the assignment. However we may wish to check the spot count.
//				if (isMatchingSpotMarketExist(extraContext.getRootObject(), charterInMarket) == false) {
//					final String message = String.format("No charter market provides %s vessels as required by cargo '%s'", charterInMarket.getName(), cargo.getName());
//					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
//					dcsd.addEObjectAndFeature(target, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
//					statuses.add(dcsd);
//				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}
