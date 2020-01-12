/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

public interface IShippingTypeRequirementProvider extends IDataComponentProvider {
	CargoDeliveryType getSalesSlotRequiredDeliveryType(ISequenceElement element);
	CargoDeliveryType getPurchaseSlotRequiredDeliveryType(ISequenceElement element);
}
