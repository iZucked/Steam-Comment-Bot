/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.BallastBonusCharterContract;
import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import com.mmxlabs.models.lng.commercial.BallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class BallastBonusCharterContractConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof BallastBonusCharterContract) {
			final BallastBonusCharterContract contract = (BallastBonusCharterContract) target;
			BallastBonusContract ballastBonusContract = contract.getBallastBonusContract();
			EStructuralFeature feature = CommercialPackage.Literals.COMMERCIAL_MODEL__CHARTERING_CONTRACTS;
			String message = String.format("Charter contract | %s", contract.getName());
			if (ballastBonusContract instanceof RuleBasedBallastBonusContract) {
				ruleBasedballastBonusValidation(ctx, extraContext, statuses, (RuleBasedBallastBonusContract) ballastBonusContract, ballastBonusContract, feature, message);
			}
		}
		return Activator.PLUGIN_ID;
	}

//	private void ruleBasedballastBonusCheckPortGroups(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures,
//			VesselAvailability va, RuleBasedBallastBonusContract ballastBonusContract) {
//		Set<APortSet<Port>> coveredPorts = new HashSet<APortSet<Port>>();
//		List<APortSet<Port>> endAtPorts = new LinkedList<>();
//		boolean anywhere = false;
//		if (va.getEndAt().isEmpty()) {
//			// could end anywhere - add all ports
//			anywhere = true;
//			endAtPorts.addAll(((LNGScenarioModel) extraContext.getRootObject()).getReferenceModel().getPortModel().getPorts());
//		} else {
//			endAtPorts.addAll(SetUtils.getObjects(va.getEndAt()));
//		}
//		if (!ballastBonusContract.getRules().isEmpty()) {
//			for (BallastBonusContractLine ballastBonusContractLine : ballastBonusContract.getRules()) {
//					EList<APortSet<Port>> redeliveryPorts = ballastBonusContractLine.getRedeliveryPorts();
//					if (redeliveryPorts.isEmpty()) {
//						return;
//					} else {
//						coveredPorts.addAll(SetUtils.getObjects(redeliveryPorts));
//					}
//			}
//			for (APortSet<Port> endAtPort : endAtPorts) {
//				if (!coveredPorts.contains(endAtPort)) {
//					final DetailConstraintStatusDecorator dcsd;
//					if (anywhere) {
//						dcsd = new DetailConstraintStatusDecorator(
//								(IConstraintStatus) ctx.createFailureStatus(String.format("[Availability|%s] Port %s is not covered by the ballast bonus rules (note the vessel can end anywhere)",
//										va.getVessel().getName(), endAtPort.getName())));
//					} else {
//						dcsd = new DetailConstraintStatusDecorator(
//								(IConstraintStatus) ctx.createFailureStatus(String.format("[Availability|%s] Port %s is not covered by the ballast bonus rules",
//										va.getVessel().getName(), endAtPort.getName())));
//					}
//					
//					dcsd.addEObjectAndFeature(va, CargoPackage.Literals.CARGO_MODEL__VESSEL_AVAILABILITIES);
//					failures.add(dcsd);
//					return;
//				}
//			}
//		}
//	}

	private void ruleBasedballastBonusValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures,
			final RuleBasedBallastBonusContract ballastBonusContract, final EObject topLevelEObject, final EStructuralFeature topFeature, String topFeatureMessage) {
		boolean atLeastOneProblem = false;
		if (ballastBonusContract.getRules().isEmpty()) {
			// need rules
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus("[Ballast Bonus Contract] Must have rules"));
//			dcsd.addEObjectAndFeature(topLevelEObject, topFeature);
			dcsd.addEObjectAndFeature(ballastBonusContract, CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES);
			failures.add(dcsd);
			atLeastOneProblem = true;
		}
		for (final BallastBonusContractLine ballastBonusContractLine : ballastBonusContract.getRules()) {
			if (ballastBonusContractLine instanceof LumpSumBallastBonusContractLine) {
				if (!lumpSumBallastBonusValidation(ctx, extraContext, failures, (LumpSumBallastBonusContractLine) ballastBonusContractLine)) {
					atLeastOneProblem = true;
				}
			} else if (ballastBonusContractLine instanceof NotionalJourneyBallastBonusContractLine) {
				if (!notionalJourneyBallastBonusValidation(ctx, extraContext, failures, (NotionalJourneyBallastBonusContractLine) ballastBonusContractLine)) {
					atLeastOneProblem = true;
				}
			}
		}
		
		if (atLeastOneProblem) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
				(IConstraintStatus) ctx.createFailureStatus(String.format("[%s] contains a ballast bonus contract with at least one error", topFeatureMessage)));
			dcsd.addEObjectAndFeature(topLevelEObject, topFeature);
			failures.add(dcsd);
		}
	}

	private boolean lumpSumBallastBonusValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures,
			final LumpSumBallastBonusContractLine line) {
		final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, line, CommercialPackage.Literals.LUMP_SUM_BALLAST_BONUS_CONTRACT_LINE__PRICE_EXPRESSION,
				line.getPriceExpression(), PriceIndexType.COMMODITY);
		if (!result.isOk()) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("Ballast bonus contract lump sum is invalid")));
			dcsd.addEObjectAndFeature(line, CommercialPackage.Literals.LUMP_SUM_BALLAST_BONUS_CONTRACT_LINE__PRICE_EXPRESSION);
			failures.add(dcsd);
			return false;
		}
		return true;
	}

	private boolean notionalJourneyBallastBonusValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures,
			final NotionalJourneyBallastBonusContractLine line) {
		boolean valid = true;
		final ValidationResult fuelResult = PriceExpressionUtils.validatePriceExpression(ctx, line, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__FUEL_PRICE_EXPRESSION,
				line.getFuelPriceExpression(), PriceIndexType.BUNKERS);
		if (!fuelResult.isOk()) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("[Ballast bonus contract] fuel price is invalid")));
			dcsd.addEObjectAndFeature(line, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__FUEL_PRICE_EXPRESSION);
			failures.add(dcsd);
			valid = false;
		}
		final ValidationResult hireResult = PriceExpressionUtils.validatePriceExpression(ctx, line, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__HIRE_PRICE_EXPRESSION,
				line.getHirePriceExpression(), PriceIndexType.CHARTER);
		if (!hireResult.isOk()) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("[Ballast bonus contract] hire price is invalid")));
			dcsd.addEObjectAndFeature(line, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__HIRE_PRICE_EXPRESSION);
			failures.add(dcsd);
			valid = false;
		}
		if (line.getReturnPorts().isEmpty()) {
			// need ports
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("[Ballast bonus contract] return ports are needed on a notional journey")));
			dcsd.addEObjectAndFeature(line, CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES);
			failures.add(dcsd);
			valid = false;
		}
		// Determine real vessel ballast speed range. Note as notional, we are not limited to the min/max speed defined on the class.

		// Default range if no data exists.
		double minSpeed = 5.0;
		double maxSpeed = 25.0;

		if (line.getSpeed() < minSpeed || line.getSpeed() > maxSpeed) {
			// need valid speed
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(
					String.format("[Ballast bonus contract] speed must be between %.01f and %.01f knots on a notional journey", minSpeed, maxSpeed)));
			dcsd.addEObjectAndFeature(line, CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES);
			failures.add(dcsd);
			valid = false;
		}

		return valid;
	}

}
