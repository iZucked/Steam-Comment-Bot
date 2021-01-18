/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.ContractNomination;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.SlotNomination;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.nominations.validation.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class NomineeIdConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof AbstractNomination) {
			final AbstractNomination nomination = (AbstractNomination)target;
			
			//Do not validate deleted or done nominations.
			if (nomination.isDeleted() || nomination.isDone()) {
				return Activator.PLUGIN_ID;
			}	
		}
		if (target instanceof SlotNomination) {
			final AbstractNomination nomination = (AbstractNomination)target;
			final String nomineeId = nomination.getNomineeId();
			if (nomineeId != null && !"".equals(nomineeId)) {
				final LNGScenarioModel scenarioModel = (LNGScenarioModel)extraContext.getRootObject();
				final Slot<?> slot = NominationsModelUtils.findSlot(scenarioModel, nomination);
				if (slot == null) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
							"Cargo (nomineeId) with name \"%s\" cannot be found.", nomination.getNomineeId())));
					status.addEObjectAndFeature(nomination, NominationsPackage.eINSTANCE.getAbstractNomination_NomineeId());
					statuses.add(status);	
				}
			}
			else {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Missing nomineeId."));
				status.addEObjectAndFeature(nomination, NominationsPackage.eINSTANCE.getAbstractNomination_NomineeId());
				statuses.add(status);
			}
		}
		if (target instanceof ContractNomination) {
			final AbstractNomination nomination = (AbstractNomination)target;
			final String nomineeId = nomination.getNomineeId();
			if (nomineeId != null && !"".equals(nomineeId)) {
				final LNGScenarioModel scenarioModel = (LNGScenarioModel)extraContext.getRootObject();
				final Contract contract = NominationsModelUtils.findContract(scenarioModel, nomination);
				if (contract == null) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
							"Contract (nomineeId) with name \"%s\" cannot be found.", nomination.getNomineeId())));
					status.addEObjectAndFeature(nomination, NominationsPackage.eINSTANCE.getAbstractNomination_NomineeId());
					statuses.add(status);	
				}
			}
			else {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Missing nomineeId."));
				status.addEObjectAndFeature(nomination, NominationsPackage.eINSTANCE.getAbstractNomination_NomineeId());
				statuses.add(status);
			}
		}
		return Activator.PLUGIN_ID;
	}
}
