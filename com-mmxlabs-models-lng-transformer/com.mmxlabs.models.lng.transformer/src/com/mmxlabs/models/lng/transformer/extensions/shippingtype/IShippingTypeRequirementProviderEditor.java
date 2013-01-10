package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * An editor interface for {@link IShippingTypeRequirementProvider}
 * 
 * @author Simon Goodall
 * @since 2.0
 */
public interface IShippingTypeRequirementProviderEditor extends IShippingTypeRequirementProvider {
	/**
	 * Sets the shipping type of a purchase slot.
	 * @param element
	 * @param cargoType TODO
	 */
	void setPurchaseSlotDeliveryType(ISequenceElement element, CargoDeliveryType cargoType);
	/**
	 * Sets the required shipping type for a sales slot.
	 * @param element
	 * @param cargoType TODO
	 */
	void setSalesSlotRequiredPurchaseType(ISequenceElement element, CargoDeliveryType cargoType);
}
