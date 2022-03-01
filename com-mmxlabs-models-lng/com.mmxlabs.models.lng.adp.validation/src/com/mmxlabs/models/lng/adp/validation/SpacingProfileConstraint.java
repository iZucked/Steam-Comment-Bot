/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.SpacingProfile;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SpacingProfileConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof final SpacingProfile spacingProfile) {

			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("ADP profile", "Spacing Profile") //
					.withTag(ValidationConstants.TAG_ADP);

			if (spacingProfile.getDefaultPort() == null) {
				factory.copyName() //
						.withObjectAndFeature(spacingProfile, ADPPackage.Literals.SPACING_PROFILE__DEFAULT_PORT) //
						.withMessage("No default port selected") //
						.make(ctx, statuses);
			}

			if (spacingProfile.getDesSpacingAllocations().isEmpty() && spacingProfile.getFobSpacingAllocations().isEmpty()) {
				factory.copyName() //
						.withObjectAndFeature(spacingProfile, ADPPackage.Literals.SPACING_PROFILE__FOB_SPACING_ALLOCATIONS) //
						.withObjectAndFeature(spacingProfile, ADPPackage.Literals.SPACING_PROFILE__DES_SPACING_ALLOCATIONS) //
						.withMessage("No cargo data provided") //
						.make(ctx, statuses);
			}
		}
	}
}
