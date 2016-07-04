/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.ISequenceElement;

public class HashMapShippingTypeRequirementProviderEditorTest {
	@Test
	public void test() {

		final HashMapShippingTypeRequirementProvider provider = new HashMapShippingTypeRequirementProvider();

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		final CargoDeliveryType cargoType1 = CargoDeliveryType.NOT_SHIPPED;
		final CargoDeliveryType cargoType2 = CargoDeliveryType.SHIPPED;

		provider.setPurchaseSlotDeliveryType(element1, cargoType1);
		provider.setPurchaseSlotDeliveryType(element2, cargoType2);

		Assert.assertEquals(provider.getPurchaseSlotDeliveryType(element1), cargoType1);
		Assert.assertEquals(provider.getPurchaseSlotDeliveryType(element2), cargoType2);
	}
}
