/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 */
public interface IShippingTypeRequirementProvider extends IDataComponentProvider {
	CargoDeliveryType getPurchaseSlotDeliveryType(ISequenceElement element);
	CargoDeliveryType getSalesSlotRequiredDeliveryType(ISequenceElement element);
	
	CargoDeliveryType getPurchaseSlotRequiredDeliveryType(ISequenceElement element);
	CargoDeliveryType getSalesSlotDeliveryType(ISequenceElement element);

}
