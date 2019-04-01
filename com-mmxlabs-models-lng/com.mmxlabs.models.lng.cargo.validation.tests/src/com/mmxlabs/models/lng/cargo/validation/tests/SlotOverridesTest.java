/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;

public class SlotOverridesTest {

	@Test
	public void testSlot_NoContract_Port() {

		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();

		final Port loadPort = PortFactory.eINSTANCE.createPort();
		final Port dischargePort = PortFactory.eINSTANCE.createPort();
		final Port otherPort = PortFactory.eINSTANCE.createPort();

		final PurchaseContract loadContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract dischargeContract = CommercialFactory.eINSTANCE.createSalesContract();
		final PurchaseContract otherPurchaseContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract otherSalesContract = CommercialFactory.eINSTANCE.createSalesContract();

		loadSlot.getRestrictedPorts().add(dischargePort);
		loadSlot.setRestrictedPortsArePermissive(true);

		Assertions.assertTrue(loadSlot.getSlotOrDelegatePortRestrictions().contains(dischargePort));
		Assertions.assertTrue(loadSlot.getSlotOrDelegatePortRestrictionsArePermissive());
	}

	@Test
	public void testSlot_NoContract_Contract() {

		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();

		final Port loadPort = PortFactory.eINSTANCE.createPort();
		final Port dischargePort = PortFactory.eINSTANCE.createPort();
		final Port otherPort = PortFactory.eINSTANCE.createPort();

		final PurchaseContract loadContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract dischargeContract = CommercialFactory.eINSTANCE.createSalesContract();
		final PurchaseContract otherPurchaseContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract otherSalesContract = CommercialFactory.eINSTANCE.createSalesContract();

		loadSlot.getRestrictedContracts().add(dischargeContract);
		loadSlot.setRestrictedContractsArePermissive(true);

		Assertions.assertTrue(loadSlot.getSlotOrDelegateContractRestrictions().contains(dischargeContract));
		Assertions.assertTrue(loadSlot.getSlotOrDelegateContractRestrictionsArePermissive());
	}

	@Test
	public void testSlot_NoContract_Vessel() {

		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();

		final Port loadPort = PortFactory.eINSTANCE.createPort();
		final Port dischargePort = PortFactory.eINSTANCE.createPort();
		final Port otherPort = PortFactory.eINSTANCE.createPort();

		final PurchaseContract loadContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract dischargeContract = CommercialFactory.eINSTANCE.createSalesContract();
		final PurchaseContract otherPurchaseContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract otherSalesContract = CommercialFactory.eINSTANCE.createSalesContract();

		Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		loadSlot.getRestrictedVessels().add(vessel);
		loadSlot.setRestrictedVesselsArePermissive(true);

		Assertions.assertTrue(loadSlot.getSlotOrDelegateVesselRestrictions().contains(vessel));
		Assertions.assertTrue(loadSlot.getSlotOrDelegateVesselRestrictionsArePermissive());
	}

	@Test
	public void testSlot_Contract_Port() {

		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();

		final Port loadPort = PortFactory.eINSTANCE.createPort();
		final Port dischargePort = PortFactory.eINSTANCE.createPort();
		final Port otherPort = PortFactory.eINSTANCE.createPort();

		final PurchaseContract loadContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract dischargeContract = CommercialFactory.eINSTANCE.createSalesContract();
		final PurchaseContract otherPurchaseContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract otherSalesContract = CommercialFactory.eINSTANCE.createSalesContract();

		loadContract.getRestrictedPorts().add(dischargePort);
		loadContract.setRestrictedPortsArePermissive(true);
		loadSlot.setContract(loadContract);

		Assertions.assertTrue(loadSlot.getSlotOrDelegatePortRestrictions().contains(dischargePort));
		Assertions.assertTrue(loadSlot.getSlotOrDelegatePortRestrictionsArePermissive());
	}

	@Test
	public void testSlot_Contract_Contract() {

		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();

		final Port loadPort = PortFactory.eINSTANCE.createPort();
		final Port dischargePort = PortFactory.eINSTANCE.createPort();
		final Port otherPort = PortFactory.eINSTANCE.createPort();

		final PurchaseContract loadContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract dischargeContract = CommercialFactory.eINSTANCE.createSalesContract();
		final PurchaseContract otherPurchaseContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract otherSalesContract = CommercialFactory.eINSTANCE.createSalesContract();

		loadContract.getRestrictedContracts().add(dischargeContract);
		loadContract.setRestrictedContractsArePermissive(true);
		loadSlot.setContract(loadContract);

		Assertions.assertTrue(loadSlot.getSlotOrDelegateContractRestrictions().contains(dischargeContract));
		Assertions.assertTrue(loadSlot.getSlotOrDelegateContractRestrictionsArePermissive());
	}

	@Test
	public void testSlot_Contract_Vessel() {

		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();

		final Port loadPort = PortFactory.eINSTANCE.createPort();
		final Port dischargePort = PortFactory.eINSTANCE.createPort();
		final Port otherPort = PortFactory.eINSTANCE.createPort();

		final PurchaseContract loadContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract dischargeContract = CommercialFactory.eINSTANCE.createSalesContract();
		final PurchaseContract otherPurchaseContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract otherSalesContract = CommercialFactory.eINSTANCE.createSalesContract();

		Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		loadContract.getRestrictedVessels().add(vessel);
		loadContract.setRestrictedVesselsArePermissive(true);
		loadSlot.setContract(loadContract);

		Assertions.assertTrue(loadSlot.getSlotOrDelegateVesselRestrictions().contains(vessel));
		Assertions.assertTrue(loadSlot.getSlotOrDelegateVesselRestrictionsArePermissive());
	}
	@Test
	public void testSlot_Contract_SlotPort() {
		
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		
		final Port loadPort = PortFactory.eINSTANCE.createPort();
		final Port dischargePort = PortFactory.eINSTANCE.createPort();
		final Port otherPort = PortFactory.eINSTANCE.createPort();
		
		final PurchaseContract loadContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract dischargeContract = CommercialFactory.eINSTANCE.createSalesContract();
		final PurchaseContract otherPurchaseContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract otherSalesContract = CommercialFactory.eINSTANCE.createSalesContract();
		
		loadContract.getRestrictedPorts().add(dischargePort);
		loadContract.setRestrictedPortsArePermissive(true);
		loadSlot.setContract(loadContract);
		loadSlot.setRestrictedPortsArePermissive(false);
		loadSlot.setRestrictedPortsOverride(true);
		
		Assertions.assertFalse(loadSlot.getSlotOrDelegatePortRestrictions().contains(dischargePort));
		Assertions.assertFalse(loadSlot.getSlotOrDelegatePortRestrictionsArePermissive());
	}
	
	@Test
	public void testSlot_Contract_SlotContract() {
		
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		
		final Port loadPort = PortFactory.eINSTANCE.createPort();
		final Port dischargePort = PortFactory.eINSTANCE.createPort();
		final Port otherPort = PortFactory.eINSTANCE.createPort();
		
		final PurchaseContract loadContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract dischargeContract = CommercialFactory.eINSTANCE.createSalesContract();
		final PurchaseContract otherPurchaseContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract otherSalesContract = CommercialFactory.eINSTANCE.createSalesContract();
		
		loadContract.getRestrictedContracts().add(dischargeContract);
		loadContract.setRestrictedContractsArePermissive(true);
		loadSlot.setContract(loadContract);
		loadSlot.setRestrictedContractsArePermissive(false);
		loadSlot.setRestrictedContractsOverride(true);
		Assertions.assertFalse(loadSlot.getSlotOrDelegateContractRestrictions().contains(dischargeContract));
		Assertions.assertFalse(loadSlot.getSlotOrDelegateContractRestrictionsArePermissive());
	}
	
	@Test
	public void testSlot_Contract_SlotVessel() {
		
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		
		final Port loadPort = PortFactory.eINSTANCE.createPort();
		final Port dischargePort = PortFactory.eINSTANCE.createPort();
		final Port otherPort = PortFactory.eINSTANCE.createPort();
		
		final PurchaseContract loadContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract dischargeContract = CommercialFactory.eINSTANCE.createSalesContract();
		final PurchaseContract otherPurchaseContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract otherSalesContract = CommercialFactory.eINSTANCE.createSalesContract();
		
		Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		
		loadContract.getRestrictedVessels().add(vessel);
		loadContract.setRestrictedVesselsArePermissive(true);
		loadSlot.setContract(loadContract);
		loadSlot.setRestrictedVesselsArePermissive(false);
		loadSlot.setRestrictedVesselsOverride(true);
		
		Assertions.assertFalse(loadSlot.getSlotOrDelegateVesselRestrictions().contains(vessel));
		Assertions.assertFalse(loadSlot.getSlotOrDelegateVesselRestrictionsArePermissive());
	}
}
