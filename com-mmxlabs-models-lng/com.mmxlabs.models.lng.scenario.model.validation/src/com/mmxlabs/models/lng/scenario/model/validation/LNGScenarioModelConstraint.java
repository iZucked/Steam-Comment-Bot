/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.model.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.scenario.model.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class LNGScenarioModelConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof LNGScenarioModel lngScenarioModel) {

			final LocalDate schedulingEndDate = lngScenarioModel.getSchedulingEndDate();
			final LocalDate earliestSlotDate = getEarliestSlotDate(lngScenarioModel);
			final LocalDate promptPeriodStart = lngScenarioModel.getPromptPeriodStart();
			if (promptPeriodStart == null) {
				statuses.add(DetailConstraintStatusFactory.makeStatus() //
						.withMessage("No prompt start date set") //
						.withSeverity(IStatus.ERROR).withObjectAndFeature(lngScenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodStart()) //
						.make(ctx));
			}
			if (promptPeriodStart != null && promptPeriodStart.isBefore(earliestSlotDate)) {
				statuses.add(DetailConstraintStatusFactory.makeStatus() //
						.withMessage("Prompt start date preceeds earliest slots' window start") //
						.withObjectAndFeature(lngScenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodStart()) //
						.make(ctx));
			}
			final LocalDate promptPeriodEnd = lngScenarioModel.getPromptPeriodEnd();
			if (promptPeriodEnd == null) {
				statuses.add(DetailConstraintStatusFactory.makeStatus() //
						.withMessage("No prompt end date set") //
						.withSeverity(IStatus.ERROR).withObjectAndFeature(lngScenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodEnd()) //
						.make(ctx));
			}

			if (promptPeriodStart != null && promptPeriodEnd != null) {
				if (!promptPeriodStart.isBefore(promptPeriodEnd)) {
					statuses.add(DetailConstraintStatusFactory.makeStatus() //
							.withMessage("Prompt start date must be before the prompt end date") //
							.withSeverity(IStatus.ERROR).withObjectAndFeature(lngScenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodStart()) //
							.withObjectAndFeature(lngScenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodEnd()) //
							.make(ctx));
				}
			}

			if (lngScenarioModel.isSetSchedulingEndDate()) {
				if (earliestSlotDate != LocalDate.MAX && schedulingEndDate != null && !schedulingEndDate.isAfter(earliestSlotDate)) {
					final String message = String.format("Schedule horizon date (%s) must be after earliest slot date start (%s)", schedulingEndDate.format(DateTimeFormatter.ISO_DATE),
							earliestSlotDate.format(DateTimeFormatter.ISO_DATE));
					statuses.add(DetailConstraintStatusFactory.makeStatus() //
							.withMessage(message) //
							.withSeverity(IStatus.ERROR).withObjectAndFeature(lngScenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_SchedulingEndDate()) //
							.make(ctx));
				}
			}
		}
	}

	private LocalDate getEarliestSlotDate(final @NonNull LNGScenarioModel lngScenarioModel) {
		LocalDate result = LocalDate.MAX;

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);

		LocalDate erl = result;

		for (final LoadSlot ls : cargoModel.getLoadSlots()) {
			if (ls.getSchedulingTimeWindow().getStart() != null && ls.getSchedulingTimeWindow().getStart().toLocalDate().isBefore(erl)) {
				erl = ls.getSchedulingTimeWindow().getStart().toLocalDate();
			}
		}
		for (final DischargeSlot ds : cargoModel.getDischargeSlots()) {
			if (ds.getSchedulingTimeWindow().getStart() != null && ds.getSchedulingTimeWindow().getStart().toLocalDate().isBefore(erl)) {
				erl = ds.getSchedulingTimeWindow().getStart().toLocalDate();
			}
		}
		if (result.isAfter(erl)) {
			result = erl;
		}

		return result;
	}

}