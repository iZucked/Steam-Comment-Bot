/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class EntityBusinessUnitsConstraint extends AbstractModelMultiConstraint {
	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> failures) {

		final EObject target = ctx.getTarget();

		if (target instanceof final BaseLegalEntity entity) {
			if (entity.isThirdParty() || entity.getBusinessUnits() == null || entity.getBusinessUnits().isEmpty()) {
				return;
			}
			final int[] defaultCounter = {0};
			entity.getBusinessUnits().forEach( bu -> {
				if (bu.isDefault()) {
					defaultCounter[0]++ ;
				}
			});
			if (defaultCounter[0] > 1) {
				String failureMessage = String.format("Entity '%s' has %d default business units. Only one default is allowed.", entity.getName(), defaultCounter[0]);
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
				dsd.addEObjectAndFeature(entity, CommercialPackage.Literals.BASE_LEGAL_ENTITY__BUSINESS_UNITS);
				failures.add(dsd);
			}
			if (defaultCounter[0] == 0) {
				String failureMessage = String.format("Entity '%s' has %d default business units. At least one should be present.", entity.getName(), defaultCounter[0]);
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
				dsd.addEObjectAndFeature(entity, CommercialPackage.Literals.BASE_LEGAL_ENTITY__BUSINESS_UNITS);
				failures.add(dsd);
			}
		}
	}
}
