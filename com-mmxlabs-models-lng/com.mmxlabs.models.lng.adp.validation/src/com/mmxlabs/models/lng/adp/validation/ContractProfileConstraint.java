/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.time.Months;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.validation.internal.Activator;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ContractProfileConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof ContractProfile<?, ?>) {
			final ContractProfile profile = (ContractProfile) target;

			DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withName("ADP Profile") //
					.withTag(ValidationConstants.TAG_ADP);

			if (profile.getContract() == null) {
				factory.copyName() //
						.withObjectAndFeature(profile, ADPPackage.Literals.CONTRACT_PROFILE__CONTRACT) //
						.withMessage("No contract specified") //
						.make(ctx, statuses);
			}
			// Update name
			factory = factory.withTypedName("ADP Profile", factory.getName(profile.getContract(), "<Unknown contract>"));

			if (profile.isEnabled() && profile.getSubProfiles().isEmpty()) {
				factory.copyName() //
						.withObjectAndFeature(profile, ADPPackage.Literals.CONTRACT_PROFILE__SUB_PROFILES) //
						.withMessage("No volume profiles defined") //
						.make(ctx, statuses);
			}
		}

		return Activator.PLUGIN_ID;
	}

}
