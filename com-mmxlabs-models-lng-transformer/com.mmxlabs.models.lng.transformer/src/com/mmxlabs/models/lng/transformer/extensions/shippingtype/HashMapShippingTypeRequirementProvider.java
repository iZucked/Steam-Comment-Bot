package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import java.util.HashMap;

import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * @since 2.0
 */
public class HashMapShippingTypeRequirementProvider implements IShippingTypeRequirementProviderEditor {
	final String name;
	
	final HashMap<ISequenceElement, CargoDeliveryType> salesSlotsShippingRequirements = new HashMap<ISequenceElement, CargoDeliveryType>();
	final HashMap<ISequenceElement, CargoDeliveryType> purchaseSlotsShippingTypes = new HashMap<ISequenceElement, CargoDeliveryType>();

	public HashMapShippingTypeRequirementProvider(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		salesSlotsShippingRequirements.clear();
		purchaseSlotsShippingTypes.clear();		
	}
	
	public void setPurchaseSlotDeliveryType(ISequenceElement element, CargoDeliveryType cargoType) {
		purchaseSlotsShippingTypes.put(element, cargoType);
	}

	public void setSalesSlotRequiredPurchaseType(ISequenceElement element, CargoDeliveryType cargoType) {
		salesSlotsShippingRequirements.put(element, cargoType);
	}

	@Override
	public CargoDeliveryType getPurchaseSlotDeliveryType(
			ISequenceElement element) {
		// TODO Auto-generated method stub
		return purchaseSlotsShippingTypes.get(element);
	}

	@Override
	public CargoDeliveryType getSalesSlotRequiredDeliveryType(
			ISequenceElement element) {
		// TODO Auto-generated method stub
		return salesSlotsShippingRequirements.get(element);
	}

}
