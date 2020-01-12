/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.FleetProfile;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class FleetProfileConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> statuses) {

		final EObject target = ctx.getTarget();

		if (target instanceof FleetProfile) {
			final FleetProfile profile = (FleetProfile) target;

			DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withName("ADP Fleet profile") //
					.withTag(ValidationConstants.TAG_ADP);

			if (profile.getDefaultNominalMarket() == null) {
				factory.copyName() //
						.withObjectAndFeature(profile, ADPPackage.Literals.FLEET_PROFILE__DEFAULT_NOMINAL_MARKET) //
						.withMessage("No default vessel specified") //
						.make(ctx, statuses);
			}
		}
	}
}
