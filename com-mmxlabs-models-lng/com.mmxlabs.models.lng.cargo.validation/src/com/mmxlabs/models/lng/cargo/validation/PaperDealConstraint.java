/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.YearMonth;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.PaperPricingType;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PaperDealConstraint extends AbstractModelMultiConstraint {

	private static final int EARLIEST_VALID_YEAR = 2010;
	private static final int LATEST_VALID_YEAR = 2100;
	
	private static boolean isInvalidYear(int year) {
		return year < EARLIEST_VALID_YEAR || year > LATEST_VALID_YEAR;
	}

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof PaperDeal paperDeal) {
			final PaperPricingType ppt = paperDeal.getPricingType();
			String name = paperDeal.getName();
			if (name == null || name.trim().isEmpty()) {
				name = "<no name>";
			}

			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus().withTypedName("Paper deal", name);

			final YearMonth startMonth = paperDeal.getPricingMonth();
			if (startMonth == null) {
				factory.copyName() //
						.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__PRICING_MONTH) //
						.withMessage("No pricing month specified") //
						.make(ctx, statuses);
			} else {
				if (isInvalidYear(startMonth.getYear())) {
					factory.copyName() //
							.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__PRICING_MONTH) //
							.withMessage("Pricing month has invalid year") //
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
			final int year = paperDeal.getYear();
			if (year == 0 || year < EARLIEST_VALID_YEAR || year > LATEST_VALID_YEAR) {
				factory.copyName() //
						.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__YEAR) //
						.withMessage("Fiscal year date is not a valid year") //
						.make(ctx, statuses);
			}
			final BaseLegalEntity ble = paperDeal.getEntity();
			if (ble == null) {
				factory.copyName() //
						.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__ENTITY) //
						.withMessage("Paper deal requires entity") //
						.make(ctx, statuses);
			}
			if (ppt.equals(PaperPricingType.INSTRUMENT)) {
				if (paperDeal.getInstrument() == null) {
					factory.copyName() //
							.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__INSTRUMENT) //
							.withMessage("Paper deal requires an instrument") //
							.make(ctx, statuses);
				}
			}
			if (paperDeal.getQuantity() > 100_000_000) {
				factory.copyName() //
						.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__QUANTITY) //
						.withMessage("Paper deal quantity should be at most 100,000,000") //
						.make(ctx, statuses);
			}
			
			/*
			 *  Pricing period checks
			 */
			if (paperDeal.getPricingPeriodStart() != null && paperDeal.getPricingPeriodEnd() != null &&  (paperDeal.getPricingPeriodStart().isAfter(paperDeal.getPricingPeriodEnd()))) {
				factory.copyName()
					.withMessage("Pricing period start should not be later than pricing period end")
					.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__PRICING_PERIOD_START)
					.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__PRICING_PERIOD_END)
					.make(ctx, statuses);
			} 
			if (paperDeal.getPricingPeriodStart() == null) {
				factory.copyName()
					.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__PRICING_PERIOD_START)
					.withMessage("Pricing period start is not set")
					.make(ctx, statuses);
			} 
			if (paperDeal.getPricingPeriodEnd() == null) {
				factory.copyName()
					.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__PRICING_PERIOD_END)
					.withMessage("Pricing period end is not set")
					.make(ctx, statuses);
			}
			
			/*
			 *  Hedging period checks
			 */
			if (paperDeal.getHedgingPeriodStart() != null && paperDeal.getHedgingPeriodEnd() != null &&  (paperDeal.getHedgingPeriodStart().isAfter(paperDeal.getHedgingPeriodEnd()))) {
				factory.copyName()
					.withMessage("Hedging period start should not be later than hedging period end")
					.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__HEDGING_PERIOD_START)
					.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__HEDGING_PERIOD_END)
					.make(ctx, statuses);
			} 
			if (paperDeal.getHedgingPeriodStart() == null) {
				factory.copyName()
					.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__HEDGING_PERIOD_START)
					.withMessage("Hedging period start is not set")
					.make(ctx, statuses);
			} 
			if (paperDeal.getHedgingPeriodEnd() == null) {
				factory.copyName()
					.withObjectAndFeature(paperDeal, CargoPackage.Literals.PAPER_DEAL__HEDGING_PERIOD_END)
					.withMessage("Hedging period end is not set")
					.make(ctx, statuses);
			}
		}
	}
}
