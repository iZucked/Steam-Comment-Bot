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

public class LNGScenarioModelWarningConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof LNGScenarioModel) {

			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) target;
			final LocalDate earliestSlotDate = LNGScenarioModelValidationUtils.getEarliestSlotDate(lngScenarioModel);
			final LocalDate promptPeriodStart = lngScenarioModel.getPromptPeriodStart();
			if (promptPeriodStart != null && promptPeriodStart.isBefore(earliestSlotDate)) {
				statuses.add(DetailConstraintStatusFactory.makeStatus() //
						.withMessage("Prompt start date preceeds earliest slots' window start") //
						.withObjectAndFeature(lngScenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodStart()) //
						.make(ctx));
			}

		}
		return Activator.PLUGIN_ID;
	}

}