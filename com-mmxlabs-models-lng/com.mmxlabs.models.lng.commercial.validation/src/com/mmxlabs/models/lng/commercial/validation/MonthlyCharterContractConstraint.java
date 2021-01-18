/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusCharterContract;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContract;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class MonthlyCharterContractConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		final EObject rootObject = extraContext.getRootObject();

		if (target instanceof MonthlyBallastBonusCharterContract && rootObject instanceof LNGScenarioModel) {
			final MonthlyBallastBonusCharterContract contract = (MonthlyBallastBonusCharterContract) target;
			final LNGScenarioModel scenario = (LNGScenarioModel) rootObject;
			final LocalDate scheduleStart = getStartOfSchedule(scenario);

			if (scheduleStart != LocalDate.MAX) {

				if (contract.getBallastBonusContract() instanceof MonthlyBallastBonusContract) {
					final MonthlyBallastBonusContract bbContract = (MonthlyBallastBonusContract) contract.getBallastBonusContract();

					LocalDate earliestRule = LocalDate.MAX;
					for (BallastBonusContractLine rule : bbContract.getRules()) {
						if (rule instanceof MonthlyBallastBonusContractLine) {
							MonthlyBallastBonusContractLine monthlyRule = (MonthlyBallastBonusContractLine) rule;
							YearMonth ym = monthlyRule.getMonth();
							if (ym != null) {
								LocalDate ymStart = LocalDate.of(ym.getYear(), ym.getMonthValue(), 1);
								if (ymStart.isBefore(earliestRule)) {
									earliestRule = ymStart;
								}
							}
							else {
								addValidationError(ctx, statuses, contract, "Month not set on monthly rule.", CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES);			
							}
						} else {
							// If not a monthly rule, in case we allow in the future, covers all dates.
							earliestRule = LocalDate.MIN;
						}
					}

					if (scheduleStart.isBefore(earliestRule)) {
						String earliestRuleStr = (earliestRule != LocalDate.MAX) ? ", "+earliestRule.toString()+")" : ",]";
						addValidationError(ctx, statuses, contract, "Monthly range does not cover [" + scheduleStart.toString() + earliestRuleStr, CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES);
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

	private void addValidationError(final IValidationContext ctx, final List<IStatus> statuses, final MonthlyBallastBonusCharterContract contract, final String errorMsg,
			EStructuralFeature... features) {
		final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
				(IConstraintStatus) ctx.createFailureStatus(String.format("[Monthly Ballast Bonus Charter Contract] %s", errorMsg)));
		for (EStructuralFeature feature : features) {
			dcsd.addEObjectAndFeature(contract, feature);
		}
		statuses.add(dcsd);
	}

	private LocalDate getStartOfSchedule(LNGScenarioModel scenario) {
		LocalDate scheduleStartDate = LocalDate.MAX;

		// Check the fleet start dates.
		for (VesselAvailability va : scenario.getCargoModel().getVesselAvailabilities()) {
			if (va.getStartBy() != null && va.getStartBy().toLocalDate().isBefore(scheduleStartDate)) {
				scheduleStartDate = va.getStartBy().toLocalDate();
			}
			if (va.getStartAfter() != null && va.getStartAfter().toLocalDate().isBefore(scheduleStartDate)) {
				scheduleStartDate = va.getStartAfter().toLocalDate();
			}
		}

		// Check for earliest load slot (in case uses spot charter).
		for (Slot<?> slot : scenario.getCargoModel().getLoadSlots()) {
			if (slot.getWindowStart() != null && slot.getWindowStart().isBefore(scheduleStartDate)) {
				scheduleStartDate = slot.getWindowStart();
			}
		}

		// Check for earliest discharge slot (in case uses spot charter).
		for (Slot<?> slot : scenario.getCargoModel().getDischargeSlots()) {
			if (slot.getWindowStart() != null && slot.getWindowStart().isBefore(scheduleStartDate)) {
				scheduleStartDate = slot.getWindowStart();
			}
		}

		return scheduleStartDate;
	}
}
