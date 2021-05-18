/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.model.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

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
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof LNGScenarioModel) {

			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) target;
			final LocalDate promptPeriodStart = lngScenarioModel.getPromptPeriodStart();
			if (promptPeriodStart == null) {
				statuses.add(DetailConstraintStatusFactory.makeStatus() //
						.withMessage("No prompt start date set") //
						.withObjectAndFeature(lngScenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodStart()) //
						.make(ctx));
			}
			final LocalDate promptPeriodEnd = lngScenarioModel.getPromptPeriodEnd();
			if (promptPeriodEnd == null) {
				statuses.add(DetailConstraintStatusFactory.makeStatus() //
						.withMessage("No prompt end date set") //
						.withObjectAndFeature(lngScenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodEnd()) //
						.make(ctx));
			}

			if (promptPeriodStart != null && promptPeriodEnd != null) {
				if (!promptPeriodStart.isBefore(promptPeriodEnd)) {
					statuses.add(DetailConstraintStatusFactory.makeStatus() //
							.withMessage("Prompt start date must be before the prompt end date") //
							.withObjectAndFeature(lngScenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodStart()) //
							.withObjectAndFeature(lngScenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodEnd()) //
							.make(ctx));
				}
			}

			if (lngScenarioModel.isSetSchedulingEndDate()) {
				final LocalDate schedulingEndDate = lngScenarioModel.getSchedulingEndDate();
				final LocalDate earliestSlotDate = getEarliestSlotDate(lngScenarioModel);
				if (schedulingEndDate != null && !schedulingEndDate.isAfter(earliestSlotDate)) {
					final String message = String.format("Schedule horizon date (%s) must be after earliest slot date start (%s)", 
							schedulingEndDate.format(DateTimeFormatter.ISO_DATE), earliestSlotDate.format(DateTimeFormatter.ISO_DATE));
					statuses.add(DetailConstraintStatusFactory.makeStatus() //
							.withMessage(message) //
							.withObjectAndFeature(lngScenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_SchedulingEndDate()) //
							.make(ctx));
				}
			}

		}
		return Activator.PLUGIN_ID;
	}


	public static LocalDate getEarliestSlotDate(final LNGScenarioModel lngScenarioModel) {
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