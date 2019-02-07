/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PaperDealConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof PaperDeal) {
			final PaperDeal paperDeal = (PaperDeal) target;
			String name = paperDeal.getName();
			if (name == null || name.trim().isEmpty()) {
				name = "<no name>";
			}

			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus().withTypedName("Paper deal", name);

			final LocalDate startDate = paperDeal.getStartDate();
			if (startDate == null) {
				factory.copyName() //
						.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__START_DATE) //
						.withMessage("No start date specified") //
						.make(ctx, statuses);
			} else {
				if (startDate.getYear() < 2010) {
					factory.copyName() //
							.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__START_DATE) //
							.withMessage("Start date has invalid year") //
							.make(ctx, statuses);
				} else if (startDate.getYear() > 2100) {
					factory.copyName() //
							.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__START_DATE) //
							.withMessage("Start date has invalid year") //
							.make(ctx, statuses);
				}
			}

			final LocalDate endDate = paperDeal.getEndDate();
			if (endDate == null) {
				factory.copyName() //
						.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__END_DATE) //
						.withMessage("No end date specified") //
						.make(ctx, statuses);
			} else {
				if (endDate.getYear() < 2010) {
					factory.copyName() //
							.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__END_DATE) //
							.withMessage("End date has invalid year") //
							.make(ctx, statuses);
				} else if (endDate.getYear() > 2100) {
					factory.copyName() //
							.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__END_DATE) //
							.withMessage("End date has invalid year") //
							.make(ctx, statuses);
				}
			}

			if (startDate != null && endDate != null) {
				if (endDate.isBefore(startDate)) {
					factory.copyName() //
							.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__START_DATE) //
							.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__END_DATE) //
							.withMessage("End date is before start date") //
							.make(ctx, statuses);
				}
			}
			final String curve = paperDeal.getIndex();
			if (curve == null || curve.trim().isEmpty()) {
				factory.copyName() //
						.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__INDEX) //
						.withMessage("No price specified") //
						.make(ctx, statuses);
			} else {
				final AbstractYearMonthCurve data = PriceExpressionUtils.getMarketCurveProvider().getCurve(PriceIndexType.COMMODITY, curve.toLowerCase());
				if (data == null) {
					factory.copyName() //
							.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__INDEX) //
							.withFormattedMessage("No commodity index named %s found", curve) //
							.make(ctx, statuses);
				}
			}

		}

		return Activator.PLUGIN_ID;

	}

}
