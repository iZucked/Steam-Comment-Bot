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


import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class NominationTimePeriodUnitsConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		// Valid contract data checks
		if (object instanceof Contract) {
			
			final Contract contract = (Contract) object;
			String message = null;
			
			final TimePeriod tp = contract.getWindowNominationSizeUnits();
			if (tp == null) {
				return Activator.PLUGIN_ID;
			}
			
			if (tp == TimePeriod.HOURS) {
				message = "Hours are not supported unit for the nomination period.";
			}
			if (message != null) {
				final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
				dsd.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getContract_WindowNominationSizeUnits());
				failures.add(dsd);
			}
		}
		return Activator.PLUGIN_ID;
	}

}
