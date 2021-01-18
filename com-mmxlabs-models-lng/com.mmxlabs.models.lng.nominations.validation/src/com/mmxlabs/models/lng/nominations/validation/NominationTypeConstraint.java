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

import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.AbstractNominationSpec;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.nominations.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class NominationTypeConstraint extends AbstractModelMultiConstraint {

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
		if (target instanceof AbstractNominationSpec) {
			final AbstractNominationSpec nomination = (AbstractNominationSpec)target;
			final String nominationType = nomination.getType();
			if (nominationType != null && !"".equals(nominationType)) {
				if (!NominationsModelUtils.isKnownNominationType(nominationType)) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
							"Unknown nomination type: %s.", nominationType)));
					status.addEObjectAndFeature(nomination, NominationsPackage.eINSTANCE.getAbstractNomination_NomineeId());
					statuses.add(status);		
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}
