/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import java.util.HashMap;

import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 */
public class HashMapShippingTypeRequirementProvider implements IShippingTypeRequirementProviderEditor {

	final HashMap<ISequenceElement, CargoDeliveryType> salesSlotsShippingRequirements = new HashMap<ISequenceElement, CargoDeliveryType>();
	final HashMap<ISequenceElement, CargoDeliveryType> purchaseSlotsShippingTypes = new HashMap<ISequenceElement, CargoDeliveryType>();
	final HashMap<ISequenceElement, CargoDeliveryType> purchaseSlotsShippingRequirements = new HashMap<ISequenceElement, CargoDeliveryType>();
	final HashMap<ISequenceElement, CargoDeliveryType> salesSlotsShippingTypes = new HashMap<ISequenceElement, CargoDeliveryType>();

	public void setPurchaseSlotDeliveryType(final ISequenceElement element, final CargoDeliveryType cargoType) {
		purchaseSlotsShippingTypes.put(element, cargoType);
	}

	public void setSalesSlotRequiredDeliveryType(final ISequenceElement element, final CargoDeliveryType cargoType) {
		salesSlotsShippingRequirements.put(element, cargoType);
	}

	@Override
	public CargoDeliveryType getPurchaseSlotDeliveryType(final ISequenceElement element) {
		return purchaseSlotsShippingTypes.get(element);
	}

	@Override
	public CargoDeliveryType getSalesSlotRequiredDeliveryType(final ISequenceElement element) {
		return salesSlotsShippingRequirements.get(element);
	}

	public void setSalesSlotDeliveryType(final ISequenceElement element, final CargoDeliveryType cargoType) {
		salesSlotsShippingTypes.put(element, cargoType);
	}

	public void setPurchaseSlotRequiredDeliveryType(final ISequenceElement element, final CargoDeliveryType cargoType) {
		purchaseSlotsShippingRequirements.put(element, cargoType);
	}

	@Override
	public CargoDeliveryType getSalesSlotDeliveryType(final ISequenceElement element) {
		return salesSlotsShippingTypes.get(element);
	}

	@Override
	public CargoDeliveryType getPurchaseSlotRequiredDeliveryType(final ISequenceElement element) {
		return purchaseSlotsShippingRequirements.get(element);
	}
}
