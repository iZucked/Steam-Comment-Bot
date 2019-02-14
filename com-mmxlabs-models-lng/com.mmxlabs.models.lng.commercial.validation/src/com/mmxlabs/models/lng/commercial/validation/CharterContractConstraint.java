/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.CharterContract;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class CharterContractConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof CharterContract) {
			final CharterContract contract = (CharterContract) target;
			if (contract.isSetMinDuration() && contract.isSetMaxDuration()) {
				if (contract.getMinDuration() > contract.getMaxDuration()) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("[Charter Contract] Min duration is greater than max duration"));
					dcsd.addEObjectAndFeature(contract, CommercialPackage.Literals.CHARTER_CONTRACT__MIN_DURATION);
					dcsd.addEObjectAndFeature(contract, CommercialPackage.Literals.CHARTER_CONTRACT__MAX_DURATION);
					statuses.add(dcsd);
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}
