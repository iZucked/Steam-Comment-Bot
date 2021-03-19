/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.BallastBonusTerm;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IBallastBonus;
import com.mmxlabs.models.lng.commercial.IRepositioningFee;
import com.mmxlabs.models.lng.commercial.LumpSumTerm;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NotionalJourneyTerm;
import com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.RepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class GenericCharterContractContainerConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof GenericCharterContract) {
			final GenericCharterContract contract = (GenericCharterContract) target;
			final IBallastBonus ballastBonusContract = contract.getBallastBonusTerms();
			EStructuralFeature feature = CommercialPackage.Literals.COMMERCIAL_MODEL__CHARTERING_CONTRACTS;
			String message = String.format("Charter contract | %s", contract.getName());
			if (ballastBonusContract instanceof SimpleBallastBonusContainer) {
				simpleBallastBonusValidation(ctx, extraContext, statuses, (SimpleBallastBonusContainer) ballastBonusContract, contract, feature, message);
			}
			final IRepositioningFee repositioningFee = contract.getRepositioningFeeTerms();
			if (repositioningFee instanceof SimpleRepositioningFeeContainer) {
				simpleRepositioningFeeValidation(ctx, extraContext, statuses, (SimpleRepositioningFeeContainer) repositioningFee, contract, feature, message);
			}
		}
		return Activator.PLUGIN_ID;
	}

	private void simpleBallastBonusValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures,
			final SimpleBallastBonusContainer ballastBonusContainer, final EObject topLevelEObject, final EStructuralFeature topFeature, String topFeatureMessage) {
		boolean atLeastOneProblem = false;
		for (final BallastBonusTerm ballastBonusTerm : ballastBonusContainer.getTerms()) {
			if (ballastBonusTerm instanceof LumpSumTerm) {
				if (!lumpSumValidation(ctx, extraContext, failures, (LumpSumTerm) ballastBonusTerm)) {
					atLeastOneProblem = true;
				}
			} else if (ballastBonusTerm instanceof NotionalJourneyBallastBonusTerm) {
				if (!notionalJourneyTermsValidation(ctx, extraContext, failures, (NotionalJourneyTerm) ballastBonusTerm)) {
					atLeastOneProblem = true;
				}
			}
		}
		
		if (atLeastOneProblem) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
				(IConstraintStatus) ctx.createFailureStatus(String.format("[%s] contains a ballast bonus containter with at least one error", topFeatureMessage)));
			dcsd.addEObjectAndFeature(topLevelEObject, topFeature);
			failures.add(dcsd);
		}
	}

	private boolean lumpSumValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures,
			final LumpSumTerm term) {
		final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, term, CommercialPackage.Literals.LUMP_SUM_TERM__PRICE_EXPRESSION,
				term.getPriceExpression(), PriceExpressionUtils.getPriceIndexType(CommercialPackage.Literals.LUMP_SUM_TERM__PRICE_EXPRESSION));
		if (!result.isOk()) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("Lump sum is invalid")));
			dcsd.addEObjectAndFeature(term, CommercialPackage.Literals.LUMP_SUM_TERM__PRICE_EXPRESSION);
			failures.add(dcsd);
			return false;
		}
		return true;
	}
	
	private boolean originPortValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures,
			final RepositioningFeeTerm term) {
		if (term.getOriginPort() == null) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("Origin port is invalid")));
			dcsd.addEObjectAndFeature(term, CommercialPackage.Literals.REPOSITIONING_FEE_TERM__ORIGIN_PORT);
			failures.add(dcsd);
			return false;
		}
		return true;
	}
	
	private boolean notionalJourneyTermsValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures,
			final NotionalJourneyTerm term) {
		boolean valid = true;
		final ValidationResult fuelResult = PriceExpressionUtils.validatePriceExpression(ctx, term, CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__FUEL_PRICE_EXPRESSION,
				term.getFuelPriceExpression(), PriceIndexType.BUNKERS);
		if (!fuelResult.isOk()) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("[Charter contract] fuel price is invalid")));
			dcsd.addEObjectAndFeature(term, CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__FUEL_PRICE_EXPRESSION);
			failures.add(dcsd);
			valid = false;
		}
		final ValidationResult hireResult = PriceExpressionUtils.validatePriceExpression(ctx, term, CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__HIRE_PRICE_EXPRESSION,
				term.getHirePriceExpression(), PriceIndexType.CHARTER);
		if (!hireResult.isOk()) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("[Charter contract] hire price is invalid")));
			dcsd.addEObjectAndFeature(term, CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__HIRE_PRICE_EXPRESSION);
			failures.add(dcsd);
			valid = false;
		}
		if (term instanceof NotionalJourneyBallastBonusTerm) {
			if (((NotionalJourneyBallastBonusTerm) term).getReturnPorts().isEmpty() && !(term instanceof MonthlyBallastBonusTerm)) {
				// need ports
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("[Charter contract] return ports are needed on a notional journey")));
				dcsd.addEObjectAndFeature(term, CommercialPackage.Literals.SIMPLE_BALLAST_BONUS_CONTAINER__TERMS);
				failures.add(dcsd);
				valid = false;
			}
		}
		// Determine real vessel ballast speed range. Note as notional, we are not limited to the min/max speed defined on the class.

		// Default range if no data exists.
		double minSpeed = 5.0;
		double maxSpeed = 25.0;

		if (term.getSpeed() < minSpeed || term.getSpeed() > maxSpeed) {
			// need valid speed
			final EReference ref;
			if (term instanceof OriginPortRepositioningFeeTerm) {
				ref = CommercialPackage.Literals.SIMPLE_REPOSITIONING_FEE_CONTAINER__TERMS;
			} else {
				ref = CommercialPackage.Literals.SIMPLE_BALLAST_BONUS_CONTAINER__TERMS;
			}
			
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(
					String.format("[Charter contract] speed must be between %.01f and %.01f knots on a notional journey", minSpeed, maxSpeed)));
			dcsd.addEObjectAndFeature(term, ref);
			failures.add(dcsd);
			valid = false;
		}

		return valid;
	}
	
	private void simpleRepositioningFeeValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures,
			final SimpleRepositioningFeeContainer repositioningFeeContainer, final EObject topLevelEObject, final EStructuralFeature topFeature, String topFeatureMessage) {
		boolean atLeastOneProblem = false;
		for (final RepositioningFeeTerm repositioningFeeTerm : repositioningFeeContainer.getTerms()) {
			if (repositioningFeeTerm instanceof LumpSumTerm) {
				if (!lumpSumValidation(ctx, extraContext, failures, (LumpSumTerm) repositioningFeeTerm)) {
					atLeastOneProblem = true;
				}
			} else if (repositioningFeeTerm instanceof OriginPortRepositioningFeeTerm) {
				if (!notionalJourneyTermsValidation(ctx, extraContext, failures, (NotionalJourneyTerm) repositioningFeeTerm)) {
					atLeastOneProblem = true;
				}
				if (!originPortValidation(ctx, extraContext, failures, repositioningFeeTerm)) {
					atLeastOneProblem = true;
				}
			}
		}
		
		if (atLeastOneProblem) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
				(IConstraintStatus) ctx.createFailureStatus(String.format("[%s] contains a repositioning fee containter with at least one error", topFeatureMessage)));
			dcsd.addEObjectAndFeature(topLevelEObject, topFeature);
			failures.add(dcsd);
		}
	}

}
