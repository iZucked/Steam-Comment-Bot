/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IBallastBonus;
import com.mmxlabs.models.lng.commercial.IRepositioningFee;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class GenericCharterContractContainerConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof GenericCharterContract contract) {
			EStructuralFeature feature = CommercialPackage.Literals.COMMERCIAL_MODEL__CHARTER_CONTRACTS;
			String message = String.format("Charter contract | %s", contract.getName());
			CharterContractValidationUtils.durationValidation(ctx, statuses, contract, message);

			final IBallastBonus ballastBonusContract = contract.getBallastBonusTerms();
			if (ballastBonusContract instanceof SimpleBallastBonusContainer container) {
				CharterContractValidationUtils.simpleBallastBonusValidation(ctx, extraContext, statuses, container, contract, feature, message);
			} else if (ballastBonusContract instanceof MonthlyBallastBonusContainer) {
				CharterContractValidationUtils.monthlyBallastBonusValidation(ctx, extraContext, statuses, contract, ballastBonusContract, message);
			}

			final IRepositioningFee repositioningFee = contract.getRepositioningFeeTerms();
			if (repositioningFee instanceof SimpleRepositioningFeeContainer container) {
				CharterContractValidationUtils.simpleRepositioningFeeValidation(ctx, extraContext, statuses, container, contract, feature, message, null);
			}
		}
	}
}
