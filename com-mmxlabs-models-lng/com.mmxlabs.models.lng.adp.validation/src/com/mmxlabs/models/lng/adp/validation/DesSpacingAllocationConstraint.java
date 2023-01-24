/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DesSpacingAllocation;
import com.mmxlabs.models.lng.adp.FobSpacingAllocation;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class DesSpacingAllocationConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof final DesSpacingAllocation desSpacingAllocation) {

			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("ADP profile", "DES Spacing Allocation") //
					.withTag(ValidationConstants.TAG_ADP);

			final SalesContract contract = desSpacingAllocation.getContract();
			if (contract != null) {
				if (contract.getContractType() != ContractType.DES) {
					factory.copyName() //
							.withObjectAndFeature(desSpacingAllocation, ADPPackage.Literals.SPACING_ALLOCATION__CONTRACT) //
							.withMessage("Contract must be DES") //
							.make(ctx, statuses);
				}
			}
			if (desSpacingAllocation.getVesselCharter() == null) {
				factory.copyName() //
						.withObjectAndFeature(desSpacingAllocation, ADPPackage.Literals.DES_SPACING_ALLOCATION__VESSEL_CHARTER) //
						.withMessage("Vessel must be provided") //
						.make(ctx, statuses);
			}

			if (desSpacingAllocation.getDesSpacingRows().isEmpty()) {
				factory.copyName() //
				.withObjectAndFeature(desSpacingAllocation, ADPPackage.Literals.DES_SPACING_ALLOCATION__DES_SPACING_ROWS) //
				.withMessage("Cargo count must be greater than zero") //
				.make(ctx, statuses);
			}
		}
	}
}
