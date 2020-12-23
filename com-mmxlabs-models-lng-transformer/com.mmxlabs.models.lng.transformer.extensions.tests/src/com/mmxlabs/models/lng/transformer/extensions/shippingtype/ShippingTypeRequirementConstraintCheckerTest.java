/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVesselEditor;

public class ShippingTypeRequirementConstraintCheckerTest {
	public void testPurchaseConstraints(CargoDeliveryType cargoType, CargoDeliveryType requiredType) {

		final String name = "name";
		final IShippingTypeRequirementProviderEditor shippingTypeRequirementProviderEditor = Mockito.mock(IShippingTypeRequirementProviderEditor.class);
		final IResource resource1 = Mockito.mock(IResource.class);
		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		if (cargoType == CargoDeliveryType.SHIPPED) {
			Mockito.when(vesselAvailability.getVesselInstanceType()).thenReturn(VesselInstanceType.SPOT_CHARTER);
		}
		else {
			Mockito.when(vesselAvailability.getVesselInstanceType()).thenReturn(VesselInstanceType.DES_PURCHASE);
		}
		Mockito.when(vesselProvider.getVesselAvailability(resource1)).thenReturn(vesselAvailability);
		final ShippingTypeRequirementConstraintChecker checker = createChecker(name, shippingTypeRequirementProviderEditor, vesselProvider);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);
		
		Mockito.when(shippingTypeRequirementProviderEditor.getSalesSlotRequiredDeliveryType(element2)).thenReturn(requiredType);

		Mockito.when(shippingTypeRequirementProviderEditor.getPurchaseSlotRequiredDeliveryType(element2)).thenReturn(CargoDeliveryType.ANY);

		boolean expectedResult = (requiredType == CargoDeliveryType.ANY || requiredType == cargoType);
		Assertions.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource1, new ArrayList<>()) == expectedResult);

		Mockito.verify(shippingTypeRequirementProviderEditor).getSalesSlotRequiredDeliveryType(element2);
		
		if (expectedResult == true) {
			Mockito.verify(shippingTypeRequirementProviderEditor).getPurchaseSlotRequiredDeliveryType(element1);
		}

		Mockito.verifyNoMoreInteractions(shippingTypeRequirementProviderEditor);
	}

	public void testSalesConstraints(CargoDeliveryType cargoType, CargoDeliveryType requiredType) {

		final String name = "name";
		final IShippingTypeRequirementProviderEditor shippingTypeRequirementProviderEditor = Mockito.mock(IShippingTypeRequirementProviderEditor.class);
		final IResource resource1 = Mockito.mock(IResource.class);
		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		if (cargoType == CargoDeliveryType.SHIPPED) {
			Mockito.when(vesselAvailability.getVesselInstanceType()).thenReturn(VesselInstanceType.SPOT_CHARTER);
		}
		else {
			Mockito.when(vesselAvailability.getVesselInstanceType()).thenReturn(VesselInstanceType.DES_PURCHASE);
		}
		Mockito.when(vesselProvider.getVesselAvailability(resource1)).thenReturn(vesselAvailability);
		final ShippingTypeRequirementConstraintChecker checker = createChecker(name, shippingTypeRequirementProviderEditor, vesselProvider);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(shippingTypeRequirementProviderEditor.getSalesSlotRequiredDeliveryType(element2)).thenReturn(CargoDeliveryType.ANY);

		Mockito.when(shippingTypeRequirementProviderEditor.getPurchaseSlotRequiredDeliveryType(element1)).thenReturn(requiredType);

		boolean expectedResult = (requiredType == CargoDeliveryType.ANY || requiredType == cargoType);
		Assertions.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource1, new ArrayList<>()) == expectedResult);

		Mockito.verify(shippingTypeRequirementProviderEditor).getPurchaseSlotRequiredDeliveryType(element1);

		
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

	private ShippingTypeRequirementConstraintChecker createChecker(final String name, final IShippingTypeRequirementProviderEditor restrictedElementsProvider,
			IVesselProvider vesselProvider) {

		final AbstractModule module = new AbstractModule() {

			@Override
			protected void configure() {
				bind(IShippingTypeRequirementProvider.class).toInstance(restrictedElementsProvider);
				bind(IVesselProvider.class).toInstance(vesselProvider);
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
