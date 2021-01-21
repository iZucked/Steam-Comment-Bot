/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.validation;

import java.text.NumberFormat;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.SlotNomination;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.nominations.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class NominatedValueConstraint extends AbstractModelMultiConstraint {

	static final NumberFormat volumeFormatter = NumberFormat.getIntegerInstance();
	
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
			final String nominatedValue = nomination.getNominatedValue();
			final String type = nomination.getType();
			if (nominatedValue != null && !nominatedValue.isBlank() && type != null) {
				if (type.contains("volume")) {		
					int volume = NominationsModelUtils.getNominatedVolumeValue(nomination);
					if (volume < 0 || volume > 10_000_000) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Nominated value for volume nominations must be an integer in mmBtu >= 0 and <= 10,000,000"));
						status.addEObjectAndFeature(nomination, NominationsPackage.eINSTANCE.getAbstractNomination_NomineeId());
						statuses.add(status);
					}
				}
			}
		
		}
		return Activator.PLUGIN_ID;
	}
}
