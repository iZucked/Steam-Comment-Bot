/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * @since 2.0
 */
public interface IShippingTypeRequirementProvider extends IDataComponentProvider {
	CargoDeliveryType getPurchaseSlotDeliveryType(ISequenceElement element);
	CargoDeliveryType getSalesSlotRequiredDeliveryType(ISequenceElement element);

}
