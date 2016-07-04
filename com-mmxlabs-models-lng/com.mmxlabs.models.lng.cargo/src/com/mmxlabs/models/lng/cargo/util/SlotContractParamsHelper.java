/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.SlotContractParams;

public final class SlotContractParamsHelper {

	public static final String ANNOTATION_SLOT_CONTRACT_PARAMS = "http://minimaxlabs.com/models/commercial/slot/parameters";

	/**
	 * Find and return if present a valid {@link SlotContractParams} instance, otherwise return null. A valid params instance is one that is valid for the current contract. It is possible that a slot
	 * may have many different params objects created as different contracts are selected. However on the one for the current contract is valid.
	 * 
	 * @param slot
	 * @param paramsCls
	 * @param contractCls
	 * @return
	 */
	@Nullable
	public static <T extends SlotContractParams, U extends LNGPriceCalculatorParameters> T findSlotContractParams(@NonNull final Slot slot, @NonNull final Class<T> paramsCls,
			@NonNull final Class<U> contractCls) {

		// Do we have a contract?
		final Contract contract = slot.getContract();
		if (contract == null) {
			// No contract - no valid params, exit
			return null;
		}
		// Are we of the correct contract? If not we may have stale, invalid params, so exit now to avoid returning them
		if (!contractCls.isInstance(contract.getPriceInfo())) {
			return null;
		}
		// Search through extensions to find the correct params object.
		for (final EObject ext : slot.getExtensions()) {
			if (paramsCls.isInstance(ext)) {
				return paramsCls.cast(ext);
			}
		}
		return null;
	}

	@Nullable
	public static SlotContractParams findSlotContractParams(@NonNull final Slot slot) {
		final Contract contract = slot.getContract();
		if (contract == null) {
			return null;
		}
		final LNGPriceCalculatorParameters priceInfo = contract.getPriceInfo();
		if (priceInfo == null) {
			return null;
		}
		final EClass priceInfoClass = priceInfo.eClass();
		final EClass paramsObjectEClass = getSlotContractParamsEClass(priceInfoClass);
		if (paramsObjectEClass == null ) {
			return null;
		}
		Class<?> cls = paramsObjectEClass.getInstanceClass();

		if (!SlotContractParams.class.isAssignableFrom(cls)) {
			return null;
		}
		if (paramsObjectEClass != null) {
			for (final EObject ext : slot.getExtensions()) {
				if (paramsObjectEClass.isInstance(ext)) {
					return (SlotContractParams) ext;
				}
			}
		}
		return null;
	}

	@Nullable
	public static EClass getSlotContractParamsEClass(final EClass priceInfoClass) {
		final EAnnotation annotation = priceInfoClass.getEAnnotation(ANNOTATION_SLOT_CONTRACT_PARAMS);
		if (annotation == null) {
			return null;
		}
		final EClass paramsObjectEClass = (EClass) annotation.getReferences().get(0);
		if (paramsObjectEClass == null) {
			return null;
		}
		return paramsObjectEClass;
	}
}
