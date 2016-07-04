/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * An editor interface for {@link IShippingTypeRequirementProvider}
 * 
 * @author Simon Goodall
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
	void setSalesSlotRequiredDeliveryType(ISequenceElement element, CargoDeliveryType cargoType);
	
	/**
	 * Sets the shipping type of a purchase slot.
	 * @param element
	 * @param cargoType TODO
	 */
	void setPurchaseSlotRequiredDeliveryType(ISequenceElement element, CargoDeliveryType cargoType);
	/**
	 * Sets the required shipping type for a sales slot.
	 * @param element
	 * @param cargoType TODO
	 */
	void setSalesSlotDeliveryType(ISequenceElement element, CargoDeliveryType cargoType);
	
}
