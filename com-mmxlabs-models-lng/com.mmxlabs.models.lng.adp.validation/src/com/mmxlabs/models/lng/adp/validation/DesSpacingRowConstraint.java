/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DesSpacingAllocation;
import com.mmxlabs.models.lng.adp.DesSpacingRow;
import com.mmxlabs.models.lng.adp.FobSpacingAllocation;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class DesSpacingRowConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof final DesSpacingRow desSpacingRow) {

			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("ADP profile", "DES Cargo Row") //
					.withTag(ValidationConstants.TAG_ADP);

			final ADPModel adpModel = (ADPModel) desSpacingRow.eContainer().eContainer().eContainer();
			final YearMonth adpStart = adpModel.getYearStart();
			final YearMonth adpEnd = adpModel.getYearEnd();
			if (desSpacingRow.isSetMinDischargeDate()) {
				final LocalDateTime minDischargeDate = desSpacingRow.getMinDischargeDate();
				if (YearMonth.from(minDischargeDate).isAfter(adpEnd)) {
					factory.copyName() //
					.withObjectAndFeature(desSpacingRow, ADPPackage.Literals.DES_SPACING_ROW__MIN_DISCHARGE_DATE) //
					.withMessage("Discharge dates must intersect ADP year") //
					.make(ctx, statuses);
				}
				if (desSpacingRow.isSetMaxDischargeDate()) {
					final LocalDateTime maxDischargeDate = desSpacingRow.getMaxDischargeDate();
					if (maxDischargeDate.isBefore(minDischargeDate)) {
						factory.copyName() //
						.withObjectAndFeature(desSpacingRow, ADPPackage.Literals.DES_SPACING_ROW__MIN_DISCHARGE_DATE) //
						.withObjectAndFeature(desSpacingRow, ADPPackage.Literals.DES_SPACING_ROW__MAX_DISCHARGE_DATE) //
						.withMessage("Min discharge date must be before max discharge date") //
						.make(ctx, statuses);
					}
				}
			}
			if (desSpacingRow.isSetMaxDischargeDate()) {
				final LocalDateTime maxDischargeDate = desSpacingRow.getMaxDischargeDate();
				if (YearMonth.from(maxDischargeDate).isBefore(adpStart)) {
					factory.copyName() //
					.withObjectAndFeature(desSpacingRow, ADPPackage.Literals.DES_SPACING_ROW__MAX_DISCHARGE_DATE) //
					.withMessage("Discharge dates must intersect ADP year") //
					.make(ctx, statuses);
				}
			}
		}
	}
}
