/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public class ShippingTypeRequirementConstraintCheckerTest {
	public void testPurchaseConstraints(CargoDeliveryType cargoType, CargoDeliveryType requiredType) {

		final String name = "name";
		final IShippingTypeRequirementProviderEditor shippingTypeRequirementProviderEditor = Mockito.mock(IShippingTypeRequirementProviderEditor.class);
		final ShippingTypeRequirementConstraintChecker checker = createChecker(name, shippingTypeRequirementProviderEditor);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);
		final IResource resource1 = Mockito.mock(IResource.class);

		Mockito.when(shippingTypeRequirementProviderEditor.getPurchaseSlotDeliveryType(element1)).thenReturn(cargoType);
		Mockito.when(shippingTypeRequirementProviderEditor.getSalesSlotRequiredDeliveryType(element2)).thenReturn(requiredType);

		Mockito.when(shippingTypeRequirementProviderEditor.getSalesSlotDeliveryType(element1)).thenReturn(CargoDeliveryType.ANY);
		Mockito.when(shippingTypeRequirementProviderEditor.getPurchaseSlotRequiredDeliveryType(element2)).thenReturn(CargoDeliveryType.ANY);

		boolean expectedResult = (requiredType == CargoDeliveryType.ANY || requiredType == cargoType);
		Assert.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource1) == expectedResult);

		Mockito.verify(shippingTypeRequirementProviderEditor).getSalesSlotRequiredDeliveryType(element2);
		if (requiredType != CargoDeliveryType.ANY) {
			Mockito.verify(shippingTypeRequirementProviderEditor).getPurchaseSlotDeliveryType(element1);
		}
		if (expectedResult == true) {
			Mockito.verify(shippingTypeRequirementProviderEditor).getPurchaseSlotRequiredDeliveryType(element1);
		}

		Mockito.verifyNoMoreInteractions(shippingTypeRequirementProviderEditor);
	}

	public void testSalesConstraints(CargoDeliveryType cargoType, CargoDeliveryType requiredType) {

		final String name = "name";
		final IShippingTypeRequirementProviderEditor shippingTypeRequirementProviderEditor = Mockito.mock(IShippingTypeRequirementProviderEditor.class);
		final ShippingTypeRequirementConstraintChecker checker = createChecker(name, shippingTypeRequirementProviderEditor);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);
		final IResource resource1 = Mockito.mock(IResource.class);

		Mockito.when(shippingTypeRequirementProviderEditor.getPurchaseSlotDeliveryType(element1)).thenReturn(CargoDeliveryType.ANY);
		Mockito.when(shippingTypeRequirementProviderEditor.getSalesSlotRequiredDeliveryType(element2)).thenReturn(CargoDeliveryType.ANY);

		Mockito.when(shippingTypeRequirementProviderEditor.getPurchaseSlotRequiredDeliveryType(element1)).thenReturn(requiredType);
		Mockito.when(shippingTypeRequirementProviderEditor.getSalesSlotDeliveryType(element2)).thenReturn(cargoType);

		boolean expectedResult = (requiredType == CargoDeliveryType.ANY || requiredType == cargoType);
		Assert.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource1) == expectedResult);

		Mockito.verify(shippingTypeRequirementProviderEditor).getPurchaseSlotRequiredDeliveryType(element1);
		if (requiredType != CargoDeliveryType.ANY) {
			Mockito.verify(shippingTypeRequirementProviderEditor).getSalesSlotDeliveryType(element2);
		}
		
		Mockito.verify(shippingTypeRequirementProviderEditor).getSalesSlotRequiredDeliveryType(element2);

		Mockito.verifyNoMoreInteractions(shippingTypeRequirementProviderEditor);
	}

	@Test
	public void test1() {
		testPurchaseConstraints(CargoDeliveryType.SHIPPED, CargoDeliveryType.NOT_SHIPPED);
	}

	@Test
	public void test2() {
		testPurchaseConstraints(CargoDeliveryType.SHIPPED, CargoDeliveryType.SHIPPED);
	}

	@Test
	public void test3() {
		testPurchaseConstraints(CargoDeliveryType.SHIPPED, CargoDeliveryType.ANY);
	}

	@Test
	public void test4() {
		testPurchaseConstraints(CargoDeliveryType.NOT_SHIPPED, CargoDeliveryType.SHIPPED);
	}
	
	
	@Test
	public void test5() {
		testSalesConstraints(CargoDeliveryType.SHIPPED, CargoDeliveryType.NOT_SHIPPED);
	}

	@Test
	public void test6() {
		testSalesConstraints(CargoDeliveryType.SHIPPED, CargoDeliveryType.SHIPPED);
	}

	@Test
	public void test7() {
		testSalesConstraints(CargoDeliveryType.SHIPPED, CargoDeliveryType.ANY);
	}

	@Test
	public void test8() {
		testSalesConstraints(CargoDeliveryType.NOT_SHIPPED, CargoDeliveryType.SHIPPED);
	}

	private ShippingTypeRequirementConstraintChecker createChecker(final String name, final IShippingTypeRequirementProviderEditor restrictedElementsProvider) {

		final AbstractModule module = new AbstractModule() {

			@Override
			protected void configure() {
				bind(IShippingTypeRequirementProvider.class).toInstance(restrictedElementsProvider);
			}

			@Provides
			ShippingTypeRequirementConstraintChecker create(Injector injector) {
				ShippingTypeRequirementConstraintChecker checker = new ShippingTypeRequirementConstraintChecker(name);
				injector.injectMembers(checker);
				return checker;
			}
		};
		final Injector injector = Guice.createInjector(module);
		return injector.getInstance(ShippingTypeRequirementConstraintChecker.class);
	}
}
