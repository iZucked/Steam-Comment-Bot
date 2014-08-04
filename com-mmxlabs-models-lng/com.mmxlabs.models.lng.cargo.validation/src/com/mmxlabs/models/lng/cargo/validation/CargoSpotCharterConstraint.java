/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterCostModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * @author Simon Goodall
 * 
 */
public class CargoSpotCharterConstraint extends AbstractModelMultiConstraint {

	private boolean isMatchingSpotMarketExist(final MMXRootObject rootObject, final VesselClass vc) {
		if (rootObject instanceof LNGScenarioModel) {
			final SpotMarketsModel spotModel = ((LNGScenarioModel) rootObject).getSpotMarketsModel();
			if (spotModel == null) {
				return false;
			}
			for (final CharterCostModel market : spotModel.getCharteringSpotMarkets()) {
				if (!market.isEnabled()) {
					continue;
				}

				if (market.getVesselClasses().contains(vc)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof Cargo) {
			final Cargo cargo = (Cargo) target;
			final AVesselSet<? extends Vessel> assignment = cargo.getAssignment();
			if (assignment instanceof VesselClass && (isMatchingSpotMarketExist(extraContext.getRootObject(), (VesselClass) assignment) == false)) {
				final String message = String.format("No charter market provides %s vessels as required by cargo '%s'", assignment.getName(), cargo.getName());
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dcsd.addEObjectAndFeature(target, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT);
				statuses.add(dcsd);
			}
		}
		return Activator.PLUGIN_ID;
	}
}
