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
import com.mmxlabs.models.lng.adp.FobSpacingAllocation;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class FobSpacingAllocationConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof final FobSpacingAllocation fobSpacingAllocation) {

			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("ADP profile", "FOB Spacing Allocation") //
					.withTag(ValidationConstants.TAG_ADP);

			final SalesContract contract = fobSpacingAllocation.getContract();
			if (contract != null) {
				if (contract.getContractType() != ContractType.FOB) {
					factory.copyName() //
							.withObjectAndFeature(fobSpacingAllocation, ADPPackage.Literals.SPACING_ALLOCATION__CONTRACT) //
							.withMessage("Contract must be FOB") //
							.make(ctx, statuses);
				}
			}
			if (fobSpacingAllocation.isSetMinSpacing() && fobSpacingAllocation.isSetMaxSpacing()) {
				final int minSpacing = fobSpacingAllocation.getMinSpacing();
				final int maxSpacing = fobSpacingAllocation.getMaxSpacing();
				if (minSpacing > maxSpacing) {
					factory.copyName() //
							.withObjectAndFeature(fobSpacingAllocation, ADPPackage.Literals.FOB_SPACING_ALLOCATION__MIN_SPACING) //
							.withObjectAndFeature(fobSpacingAllocation, ADPPackage.Literals.FOB_SPACING_ALLOCATION__MAX_SPACING) //
							.withMessage("Min spacing must be less or equal to max spacing") //
							.make(ctx, statuses);
				}
			}
			if (fobSpacingAllocation.getCargoCount() <= 0) {
				factory.copyName() //
				.withObjectAndFeature(fobSpacingAllocation, ADPPackage.Literals.FOB_SPACING_ALLOCATION__CARGO_COUNT) //
				.withMessage("Cargo count must be greater than zero") //
				.make(ctx, statuses);
			}
		}
	}
}
