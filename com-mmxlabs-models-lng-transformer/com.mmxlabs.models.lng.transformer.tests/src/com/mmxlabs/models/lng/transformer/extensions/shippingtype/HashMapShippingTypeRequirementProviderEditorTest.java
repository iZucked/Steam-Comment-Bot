package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.ISequenceElement;


public class HashMapShippingTypeRequirementProviderEditorTest {
	@Test
	public void test() {
		final String name = "name";

		final HashMapShippingTypeRequirementProvider provider = new HashMapShippingTypeRequirementProvider(name);

		Assert.assertEquals(name, provider.getName());

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);
		
		final CargoDeliveryType cargoType1 = CargoDeliveryType.DELIVERED;
		final CargoDeliveryType cargoType2 = CargoDeliveryType.SHIPPED;

		provider.setPurchaseSlotDeliveryType(element1, cargoType1);
		provider.setPurchaseSlotDeliveryType(element2, cargoType2);

		Assert.assertEquals(provider.getPurchaseSlotDeliveryType(element1), cargoType1);
		Assert.assertEquals(provider.getPurchaseSlotDeliveryType(element2), cargoType2);
	}
}
