/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

public class ObjectCopierTests {

	@Test
	public void copySlotAttributesTest() {

		final SpotLoadSlot sourceLoad1 = CargoFactory.eINSTANCE.createSpotLoadSlot();

		// Set some arbitrary attributes.
		sourceLoad1.setName("LoadSlot1");
		sourceLoad1.setArriveCold(true);
		sourceLoad1.setNominatedVessel(null);
		sourceLoad1.setCancellationExpression("1000");
		sourceLoad1.setPricingEvent(PricingEvent.START_DISCHARGE);

		// Perform the copy
		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);
		final SpotLoadSlot newLoad1 = ObjectCopier.copySlot(sourceLoad1, mapping);

		// Test new has the same attribute values as the old
		for (final EAttribute attrib : sourceLoad1.eClass().getEAllAttributes()) {
			Assert.assertEquals(sourceLoad1.eIsSet(attrib), newLoad1.eIsSet(attrib));
			if (newLoad1.eIsSet(attrib)) {
				Assert.assertEquals(sourceLoad1.eGet(attrib), newLoad1.eGet(attrib));
			}
		}
		// Redundant test, but here as a sanity check.
		Assert.assertEquals("LoadSlot1", newLoad1.getName());
	}

	/**
	 * Given a new load slot expected to have been created in our new scenario, create a copy of the load slot with references to the original data.
	 */
	@Test
	public void copySlotSingleNonContainmentReferenceTest() {

		final Port originalPort1 = PortFactory.eINSTANCE.createPort();
		final Port newPort1 = PortFactory.eINSTANCE.createPort();

		final LoadSlot sourceLoad1 = CargoFactory.eINSTANCE.createLoadSlot();

		// Set a non contained single reference
		sourceLoad1.setPort(newPort1);

		// Configure old/new mappings
		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);
		Mockito.when(mapping.getCopyFromOriginal(originalPort1)).thenReturn(newPort1);
		Mockito.when(mapping.getOriginalFromCopy(newPort1)).thenReturn(originalPort1);

		// Perform the copy
		final LoadSlot newLoad1 = ObjectCopier.copySlot(sourceLoad1, mapping);

		Assert.assertSame(originalPort1, newLoad1.getPort());
	}

	/**
	 * Given a new load slot expected to have been created in our new scenario, create a copy of the load slot with references to the original data.
	 */
	@Ignore("No such feature available")
	@Test
	public void copySlotSingleContainmentReferenceTest() {

		final Port originalPort1 = PortFactory.eINSTANCE.createPort();
		final Port newPort1 = PortFactory.eINSTANCE.createPort();

		final LoadSlot sourceLoad1 = CargoFactory.eINSTANCE.createLoadSlot();

		// Set a non contained single reference
		sourceLoad1.setPort(newPort1);

		// Configure old/new mappings
		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);
		Mockito.when(mapping.getCopyFromOriginal(originalPort1)).thenReturn(newPort1);
		Mockito.when(mapping.getOriginalFromCopy(newPort1)).thenReturn(originalPort1);

		// Perform the copy
		final LoadSlot newLoad1 = ObjectCopier.copySlot(sourceLoad1, mapping);

		Assert.assertSame(originalPort1, newLoad1.getPort());
	}

	/**
	 * Given a new load slot expected to have been created in our new scenario, create a copy of the load slot with references to the original data.
	 */
	@Test
	public void copySlotMultipleNonContainmentReferenceTest() {

		final Port originalPort1 = PortFactory.eINSTANCE.createPort();
		final Port originalPort2 = PortFactory.eINSTANCE.createPort();
		final Port newPort1 = PortFactory.eINSTANCE.createPort();
		final Port newPort2 = PortFactory.eINSTANCE.createPort();

		final DischargeSlot sourceDischarge1 = CargoFactory.eINSTANCE.createDischargeSlot();

		// Set a non contained single reference

		sourceDischarge1.getRestrictedPorts().add(newPort1);
		sourceDischarge1.getRestrictedPorts().add(newPort2);

		// Configure old/new mappings
		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);
		Mockito.when(mapping.getCopyFromOriginal(originalPort1)).thenReturn(newPort1);
		Mockito.when(mapping.getOriginalFromCopy(newPort1)).thenReturn(originalPort1);

		Mockito.when(mapping.getCopyFromOriginal(originalPort2)).thenReturn(newPort2);
		Mockito.when(mapping.getOriginalFromCopy(newPort2)).thenReturn(originalPort2);

		// Perform the copy
		final DischargeSlot newDischarge1 = ObjectCopier.copySlot(sourceDischarge1, mapping);

		final List<Port> expectedPortsList = Lists.newArrayList(originalPort1, originalPort2);
		Assert.assertEquals(expectedPortsList, newDischarge1.getRestrictedPorts());
	}

	/**
	 * Given a new load slot expected to have been created in our new scenario, create a copy of the load slot with references to the original data.
	 */
	@Test
	public void copySlotMultipleContainmentReferenceTest() {

		// Create dynamic ecore package to create a sub class of SlotContractParams, our main contained multiple refernce for Slots.
		final EPackage dummyPackage = EcoreFactory.eINSTANCE.createEPackage();

		final EClass dummySlotContractParams = EcoreFactory.eINSTANCE.createEClass();
		dummyPackage.getEClassifiers().add(dummySlotContractParams);

		dummySlotContractParams.setName("dummySlotContractParams");

		final EAttribute attributeA = EcoreFactory.eINSTANCE.createEAttribute();
		dummySlotContractParams.getEStructuralFeatures().add(attributeA);

		attributeA.setName("attributeA");
		attributeA.setEType(EcorePackage.Literals.ESTRING);

		final EReference referenceA = EcoreFactory.eINSTANCE.createEReference();
		dummySlotContractParams.getEStructuralFeatures().add(referenceA);

		referenceA.setName("referenceA");
		referenceA.setContainment(false);
		referenceA.setEType(PortPackage.Literals.PORT);

		final Port originalPort1 = PortFactory.eINSTANCE.createPort();
		final Port newPort1 = PortFactory.eINSTANCE.createPort();

		// Ensure EObject is first in super types to use the standard ecore factory rather than failing to use the commercial factory.
		dummySlotContractParams.getESuperTypes().add(EcorePackage.Literals.EOBJECT);
		dummySlotContractParams.getESuperTypes().add(CommercialPackage.Literals.SLOT_CONTRACT_PARAMS);

		final EObject slotContractParams1 = dummyPackage.getEFactoryInstance().create(dummySlotContractParams);
		slotContractParams1.eSet(MMXCorePackage.Literals.UUID_OBJECT__UUID, "abc");
		slotContractParams1.eSet(attributeA, "valueA");
		slotContractParams1.eSet(referenceA, newPort1);

		final SpotDischargeSlot sourceDischarge1 = CargoFactory.eINSTANCE.createSpotDischargeSlot();

		// Set a non contained single reference

		sourceDischarge1.getExtensions().add(slotContractParams1);

		// Configure old/new mappings
		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);
		Mockito.when(mapping.getCopyFromOriginal(originalPort1)).thenReturn(newPort1);
		Mockito.when(mapping.getOriginalFromCopy(newPort1)).thenReturn(originalPort1);

		// Perform the copy
		final DischargeSlot newDischarge1 = ObjectCopier.copySlot(sourceDischarge1, mapping);

		final List<EObject> newExtensions = newDischarge1.getExtensions();
		Assert.assertEquals(1, newExtensions.size());

		final EObject newExtension = newExtensions.get(0);
		Assert.assertNotNull(newExtension);
		Assert.assertNotSame(slotContractParams1, newExtension);
		Assert.assertEquals("abc", newExtension.eGet(MMXCorePackage.Literals.UUID_OBJECT__UUID));
		Assert.assertEquals("valueA", newExtension.eGet(attributeA));
		Assert.assertSame(originalPort1, newExtension.eGet(referenceA));
	}

	@Test
	public void copySlotWithoutCargoTest() {

		final LoadSlot sourceLoad1 = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot sourceDischarge1 = CargoFactory.eINSTANCE.createDischargeSlot();
		final Cargo sourceCargo1 = CargoFactory.eINSTANCE.createCargo();

		sourceCargo1.getSlots().add(sourceLoad1);
		sourceCargo1.getSlots().add(sourceDischarge1);

		// Perform the copy
		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		final LoadSlot newLoad1 = ObjectCopier.copySlot(sourceLoad1, mapping);

		Assert.assertNull(newLoad1.getCargo());
	}

	@Test
	public void copyCargoWithoutSlotsTest() {

		final LoadSlot sourceLoad1 = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot sourceDischarge1 = CargoFactory.eINSTANCE.createDischargeSlot();
		final Cargo sourceCargo1 = CargoFactory.eINSTANCE.createCargo();

		sourceCargo1.getSlots().add(sourceLoad1);
		sourceCargo1.getSlots().add(sourceDischarge1);

		// Perform the copy
		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		final Cargo newCargo1 = ObjectCopier.copyCargo(sourceCargo1, mapping);

		Assert.assertTrue(newCargo1.getSlots().isEmpty());
	}
}
