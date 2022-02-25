/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.SlotContractParamsHelper;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SlotContractParamsConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> statuses) {

		final EObject target = ctx.getTarget();

		if (target instanceof Slot<?> slot) {
			final Contract contract = slot.getContract();
			if (contract != null) {
				final LNGPriceCalculatorParameters params = contract.getPriceInfo();

				String contractName = "<Unknown>";
				if (contract.getName() != null) {
					contractName = contract.getName();
				}

				if (params != null) {

					EClass slotContractParamsEClass = SlotContractParamsHelper.getSlotContractParamsEClass(params.eClass());
					if (slotContractParamsEClass != null) {
						SlotContractParamsConstraintHelper.checkBasicContractParams(ctx, extraContext, statuses, slot, contractName, (Class<SlotContractParams>)slotContractParamsEClass.getInstanceClass(), true, false, false);
					}
				}
			}
		}
	}
}
