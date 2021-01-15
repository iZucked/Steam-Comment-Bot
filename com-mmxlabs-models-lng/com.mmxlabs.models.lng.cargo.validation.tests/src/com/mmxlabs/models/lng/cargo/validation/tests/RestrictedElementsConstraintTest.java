/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.validation.RestrictedElementsConstraint;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortGroup;

public class RestrictedElementsConstraintTest {

	@Test
	public void testRestrictedElementsContraint_Pass() {

		// This is the constraint we will be testing
		final RestrictedElementsConstraint constraint = new RestrictedElementsConstraint();

		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);

		final Port loadPort = PortFactory.eINSTANCE.createPort();
		final Port dischargePort = PortFactory.eINSTANCE.createPort();
		final Port otherPort = PortFactory.eINSTANCE.createPort();

		final PurchaseContract loadContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract dischargeContract = CommercialFactory.eINSTANCE.createSalesContract();
		final PurchaseContract otherPurchaseContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract otherSalesContract = CommercialFactory.eINSTANCE.createSalesContract();

		loadSlot.setPort(loadPort);
		dischargeSlot.setPort(dischargePort);

		loadSlot.setContract(loadContract);
		dischargeSlot.setContract(dischargeContract);

		loadContract.getRestrictedContracts().add(otherSalesContract);
		dischargeContract.getRestrictedContracts().add(otherPurchaseContract);

		loadContract.getRestrictedPorts().add(otherPort);
		dischargeContract.getRestrictedPorts().add(otherPort);

		loadContract.setRestrictedPortsArePermissive(false);
		loadContract.setRestrictedContractsArePermissive(false);
		dischargeContract.setRestrictedPortsArePermissive(false);
		dischargeContract.setRestrictedContractsArePermissive(false);

		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(validationContext.createSuccessStatus()).thenReturn(successStatus);
		when(validationContext.createFailureStatus()).thenReturn(failureStatus);
		when(validationContext.createFailureStatus(ArgumentMatchers.any())).thenReturn(failureStatus);
		when(successStatus.isOK()).thenReturn(true);
		when(failureStatus.isOK()).thenReturn(false);
		when(validationContext.getTarget()).thenReturn(cargo);
		when(successStatus.getSeverity()).thenReturn(IStatus.OK);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		// Set up the expected return values of methods.

		// validate the constraint using the mocked expected values set above
		final IStatus status1 = constraint.validate(validationContext);
		Assertions.assertTrue(status1.isOK());

		// Flip black/white mode

		loadContract.setRestrictedPortsArePermissive(true);
		loadContract.setRestrictedContractsArePermissive(true);
		dischargeContract.setRestrictedContractsArePermissive(true);
		dischargeContract.setRestrictedPortsArePermissive(true);

		// validate the constraint using the mocked expected values set above
		final IStatus status2 = constraint.validate(validationContext);
		Assertions.assertFalse(status2.isOK());
	}

	@Test
	public void testRestrictedElementsContraint_LoadContractRestricted() {

		// This is the constraint we will be testing
		final RestrictedElementsConstraint constraint = new RestrictedElementsConstraint();

		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);

		final Port loadPort = PortFactory.eINSTANCE.createPort();
		final Port dischargePort = PortFactory.eINSTANCE.createPort();
		final Port otherPort = PortFactory.eINSTANCE.createPort();

		final PurchaseContract loadContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract dischargeContract = CommercialFactory.eINSTANCE.createSalesContract();
		final PurchaseContract otherPurchaseContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract otherSalesContract = CommercialFactory.eINSTANCE.createSalesContract();

		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(validationContext.createSuccessStatus()).thenReturn(successStatus);
		when(validationContext.createFailureStatus()).thenReturn(failureStatus);
		when(validationContext.createFailureStatus(ArgumentMatchers.any())).thenReturn(failureStatus);
		when(successStatus.isOK()).thenReturn(true);
		when(failureStatus.isOK()).thenReturn(false);
		when(validationContext.getTarget()).thenReturn(cargo);
		when(successStatus.getSeverity()).thenReturn(IStatus.OK);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		loadSlot.setPort(loadPort);
		dischargeSlot.setPort(dischargePort);

		loadSlot.setContract(loadContract);
		dischargeSlot.setContract(dischargeContract);

		dischargeContract.getRestrictedContracts().add(loadContract);

		loadContract.setRestrictedPortsArePermissive(false);
		loadContract.setRestrictedContractsArePermissive(false);
		dischargeContract.setRestrictedPortsArePermissive(false);
		dischargeContract.setRestrictedContractsArePermissive(false);

		// validate the constraint using the mocked expected values set above
		final IStatus status1 = constraint.validate(validationContext);
		Assertions.assertFalse(status1.isOK());

		// Flip black/white mode

		dischargeContract.setRestrictedContractsArePermissive(true);

		// validate the constraint using the mocked expected values set above
		final IStatus status2 = constraint.validate(validationContext);
		Assertions.assertTrue(status2.isOK());
	}

	@Test
	public void testRestrictedElementsContraint_DischargeContractRestricted() {

		// This is the constraint we will be testing
		final RestrictedElementsConstraint constraint = new RestrictedElementsConstraint();

		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);

		final Port loadPort = PortFactory.eINSTANCE.createPort();
		final Port dischargePort = PortFactory.eINSTANCE.createPort();
		final Port otherPort = PortFactory.eINSTANCE.createPort();

		final PurchaseContract loadContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract dischargeContract = CommercialFactory.eINSTANCE.createSalesContract();
		final PurchaseContract otherPurchaseContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract otherSalesContract = CommercialFactory.eINSTANCE.createSalesContract();

		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(validationContext.createSuccessStatus()).thenReturn(successStatus);
		when(validationContext.createFailureStatus()).thenReturn(failureStatus);
		when(validationContext.createFailureStatus(ArgumentMatchers.any())).thenReturn(failureStatus);
		when(successStatus.isOK()).thenReturn(true);
		when(failureStatus.isOK()).thenReturn(false);
		when(validationContext.getTarget()).thenReturn(cargo);
		when(successStatus.getSeverity()).thenReturn(IStatus.OK);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		loadSlot.setPort(loadPort);
		dischargeSlot.setPort(dischargePort);

		loadSlot.setContract(loadContract);
		dischargeSlot.setContract(dischargeContract);

		loadContract.getRestrictedContracts().add(dischargeContract);

		loadContract.setRestrictedPortsArePermissive(false);
		loadContract.setRestrictedContractsArePermissive(false);
		dischargeContract.setRestrictedPortsArePermissive(false);
		dischargeContract.setRestrictedContractsArePermissive(false);

		// validate the constraint using the mocked expected values set above
		final IStatus status1 = constraint.validate(validationContext);
		Assertions.assertFalse(status1.isOK());

		// Flip black/white mode

		loadContract.setRestrictedContractsArePermissive(true);

		// validate the constraint using the mocked expected values set above
		final IStatus status2 = constraint.validate(validationContext);
		Assertions.assertTrue(status2.isOK());
	}

	@Test
	public void testRestrictedElementsContraint_LoadPortRestricted() {

		// This is the constraint we will be testing
		final RestrictedElementsConstraint constraint = new RestrictedElementsConstraint();

		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);

		final Port loadPort = PortFactory.eINSTANCE.createPort();
		final Port dischargePort = PortFactory.eINSTANCE.createPort();
		final Port otherPort = PortFactory.eINSTANCE.createPort();

		final PurchaseContract loadContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract dischargeContract = CommercialFactory.eINSTANCE.createSalesContract();
		final PurchaseContract otherPurchaseContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract otherSalesContract = CommercialFactory.eINSTANCE.createSalesContract();

		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(validationContext.createSuccessStatus()).thenReturn(successStatus);
		when(validationContext.createFailureStatus()).thenReturn(failureStatus);
		when(validationContext.createFailureStatus(ArgumentMatchers.any())).thenReturn(failureStatus);
		when(successStatus.isOK()).thenReturn(true);
		when(failureStatus.isOK()).thenReturn(false);
		when(validationContext.getTarget()).thenReturn(cargo);
		when(successStatus.getSeverity()).thenReturn(IStatus.OK);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		loadSlot.setPort(loadPort);
		dischargeSlot.setPort(dischargePort);

		loadSlot.setContract(loadContract);
		dischargeSlot.setContract(dischargeContract);

		dischargeContract.getRestrictedPorts().add(loadPort);

		loadContract.setRestrictedPortsArePermissive(false);
		loadContract.setRestrictedContractsArePermissive(false);
		dischargeContract.setRestrictedPortsArePermissive(false);
		dischargeContract.setRestrictedContractsArePermissive(false);

		// validate the constraint using the mocked expected values set above
		final IStatus status1 = constraint.validate(validationContext);
		Assertions.assertFalse(status1.isOK());

		// Flip black/white mode

		dischargeContract.setRestrictedPortsArePermissive(true);

		// validate the constraint using the mocked expected values set above
		final IStatus status2 = constraint.validate(validationContext);
		Assertions.assertTrue(status2.isOK());
	}

	@Test
	public void testRestrictedElementsContraint_LoadPortRestricted_PortGroup() {

		// This is the constraint we will be testing
		final RestrictedElementsConstraint constraint = new RestrictedElementsConstraint();

		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);

		final Port loadPort1 = PortFactory.eINSTANCE.createPort();
		final Port loadPort2 = PortFactory.eINSTANCE.createPort();
		final Port dischargePort = PortFactory.eINSTANCE.createPort();
		final Port otherPort = PortFactory.eINSTANCE.createPort();

		final PurchaseContract loadContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract dischargeContract = CommercialFactory.eINSTANCE.createSalesContract();
		final PurchaseContract otherPurchaseContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract otherSalesContract = CommercialFactory.eINSTANCE.createSalesContract();

		PortGroup portGroup = PortFactory.eINSTANCE.createPortGroup();
		portGroup.getContents().add(loadPort1);
		portGroup.getContents().add(loadPort2);

		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(validationContext.createSuccessStatus()).thenReturn(successStatus);
		when(validationContext.createFailureStatus()).thenReturn(failureStatus);
		when(validationContext.createFailureStatus(ArgumentMatchers.any())).thenReturn(failureStatus);
		when(successStatus.isOK()).thenReturn(true);
		when(failureStatus.isOK()).thenReturn(false);
		when(validationContext.getTarget()).thenReturn(cargo);
		when(successStatus.getSeverity()).thenReturn(IStatus.OK);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		loadSlot.setPort(loadPort1);
		dischargeSlot.setPort(dischargePort);

		loadSlot.setContract(loadContract);
		dischargeSlot.setContract(dischargeContract);

		dischargeContract.getRestrictedPorts().add(portGroup);

		loadContract.setRestrictedPortsArePermissive(false);
		loadContract.setRestrictedContractsArePermissive(false);
		dischargeContract.setRestrictedPortsArePermissive(false);
		dischargeContract.setRestrictedContractsArePermissive(false);

		// validate the constraint using the mocked expected values set above
		final IStatus status1 = constraint.validate(validationContext);
		Assertions.assertFalse(status1.isOK());

		// Flip black/white mode
		dischargeContract.setRestrictedPortsArePermissive(true);

		// validate the constraint using the mocked expected values set above
		final IStatus status2 = constraint.validate(validationContext);
		Assertions.assertTrue(status2.isOK());
	}

	@Test
	public void testRestrictedElementsContraint_DischargePortRestricted() {

		// This is the constraint we will be testing
		final RestrictedElementsConstraint constraint = new RestrictedElementsConstraint();

		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);

		final Port loadPort = PortFactory.eINSTANCE.createPort();
		final Port dischargePort = PortFactory.eINSTANCE.createPort();
		final Port otherPort = PortFactory.eINSTANCE.createPort();

		final PurchaseContract loadContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final SalesContract dischargeContract = CommercialFactory.eINSTANCE.createSalesContract();

		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(validationContext.createSuccessStatus()).thenReturn(successStatus);
		when(validationContext.createFailureStatus()).thenReturn(failureStatus);
		when(validationContext.createFailureStatus(ArgumentMatchers.any())).thenReturn(failureStatus);
		when(successStatus.isOK()).thenReturn(true);
		when(failureStatus.isOK()).thenReturn(false);
		when(validationContext.getTarget()).thenReturn(cargo);
		when(successStatus.getSeverity()).thenReturn(IStatus.OK);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		loadSlot.setPort(loadPort);
		dischargeSlot.setPort(dischargePort);

		loadSlot.setContract(loadContract);
		dischargeSlot.setContract(dischargeContract);

		loadContract.getRestrictedPorts().add(dischargePort);

		loadContract.setRestrictedPortsArePermissive(false);
		loadContract.setRestrictedContractsArePermissive(false);
		dischargeContract.setRestrictedPortsArePermissive(false);
		dischargeContract.setRestrictedContractsArePermissive(false);
		// validate the constraint using the mocked expected values set above
		final IStatus status1 = constraint.validate(validationContext);
		Assertions.assertFalse(status1.isOK());

		// Flip black/white mode

		loadContract.setRestrictedPortsArePermissive(true);

		// validate the constraint using the mocked expected values set above
		final IStatus status2 = constraint.validate(validationContext);
		Assertions.assertTrue(status2.isOK());
	}
}
