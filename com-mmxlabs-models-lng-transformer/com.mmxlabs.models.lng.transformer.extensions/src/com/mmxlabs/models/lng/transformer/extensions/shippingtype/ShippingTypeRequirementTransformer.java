/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.transformer.contracts.ISlotTransformer;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

/**
 */
public class ShippingTypeRequirementTransformer implements ISlotTransformer {

	@Inject
	private IShippingTypeRequirementProviderEditor shippingTypeProviderEditor;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Override
	public void slotTransformed(@NonNull final Slot<?> modelSlot, @NonNull final IPortSlot optimiserSlot) {
		final ISequenceElement sequenceElement = portSlotProvider.getElement(optimiserSlot);

		if (modelSlot instanceof LoadSlot loadSlot) {
			// Set the slot type
//			if (loadSlot.isDESPurchase()) {
//				shippingTypeProviderEditor.setPurchaseSlotDeliveryType(sequenceElement, CargoDeliveryType.NOT_SHIPPED);
//			} else {
//				shippingTypeProviderEditor.setPurchaseSlotDeliveryType(sequenceElement, CargoDeliveryType.SHIPPED);
//			}

			// Set the required match type
			final CargoDeliveryType cargoType = loadSlot.getSlotOrDelegateDeliveryType();
			if (cargoType != CargoDeliveryType.ANY) {
				shippingTypeProviderEditor.setPurchaseSlotRequiredDeliveryType(sequenceElement, cargoType);
			}

		}

		else if (modelSlot instanceof DischargeSlot dischargeSlot) {

//			// Set the slot type
//			if (dischargeSlot.isFOBSale()) {
//				shippingTypeProviderEditor.setSalesSlotDeliveryType(sequenceElement, CargoDeliveryType.NOT_SHIPPED);
//			} else {
//				shippingTypeProviderEditor.setSalesSlotDeliveryType(sequenceElement, CargoDeliveryType.SHIPPED);
//			}
			// Set the required match type
			final CargoDeliveryType cargoType = dischargeSlot.getSlotOrDelegateDeliveryType();
			if (cargoType != CargoDeliveryType.ANY) {
				shippingTypeProviderEditor.setSalesSlotRequiredDeliveryType(sequenceElement, cargoType);
			}
		}
	}
}
