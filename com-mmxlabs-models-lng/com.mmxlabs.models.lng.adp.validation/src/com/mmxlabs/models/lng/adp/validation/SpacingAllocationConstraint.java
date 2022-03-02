/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.SpacingAllocation;
import com.mmxlabs.models.lng.adp.SpacingProfile;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SpacingAllocationConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof final SpacingAllocation spacingAllocation) {

			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("ADP profile", "Spacing Allocation") //
					.withTag(ValidationConstants.TAG_ADP);

			if (spacingAllocation.getContract() == null) {
				factory.copyName() //
						.withObjectAndFeature(spacingAllocation, ADPPackage.Literals.SPACING_ALLOCATION__CONTRACT) //
						.withMessage("No contract specified") //
						.make(ctx, statuses);
			}
		}
	}
}
