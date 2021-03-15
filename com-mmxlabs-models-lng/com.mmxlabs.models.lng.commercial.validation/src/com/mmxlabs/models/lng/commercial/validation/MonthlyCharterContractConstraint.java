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
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm;
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

		if (target instanceof GenericCharterContract && rootObject instanceof LNGScenarioModel) {
			final GenericCharterContract contract = (GenericCharterContract) target;
			final LNGScenarioModel scenario = (LNGScenarioModel) rootObject;
			final LocalDate scheduleStart = getStartOfSchedule(scenario);

			if (scheduleStart != LocalDate.MAX) {

				if (contract.getBallastBonusTerms() instanceof MonthlyBallastBonusContainer) {
					final MonthlyBallastBonusContainer bbContract = (MonthlyBallastBonusContainer) contract.getBallastBonusTerms();

					LocalDate earliestRule = LocalDate.MAX;
					for (final MonthlyBallastBonusTerm monthlyRule : bbContract.getTerms()) {
						YearMonth ym = monthlyRule.getMonth();
						if (ym != null) {
							LocalDate ymStart = LocalDate.of(ym.getYear(), ym.getMonthValue(), 1);
							if (ymStart.isBefore(earliestRule)) {
								earliestRule = ymStart;
							}
						}
						else {
							addValidationError(ctx, statuses, contract, "Month not set on monthly rule.", CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_CONTAINER__TERMS);			
						}
					}

					if (scheduleStart.isBefore(earliestRule)) {
						String earliestRuleStr = (earliestRule != LocalDate.MAX) ? ", "+earliestRule.toString()+")" : ",]";
						addValidationError(ctx, statuses, contract, "Monthly range does not cover [" + scheduleStart.toString() + earliestRuleStr, CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_CONTAINER__TERMS);
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

	private void addValidationError(final IValidationContext ctx, final List<IStatus> statuses, final GenericCharterContract contract, final String errorMsg,
			EStructuralFeature... features) {
		final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
				(IConstraintStatus) ctx.createFailureStatus(String.format("[Charter contract] - monthly ballast bonus term has the following issue: %s", errorMsg)));
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
