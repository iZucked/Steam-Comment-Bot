/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.RestrictedElementsConstraint;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.types.APortSet;

public class RestrictedElementsConstraintTest {

	@Test
	public void testRestrictedElementsContraint_Pass() {

		// This is the constraint we will be testing
		final RestrictedElementsConstraint constraint = new RestrictedElementsConstraint();

		final LoadSlot loadSlot = mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = mock(DischargeSlot.class);
		final Cargo cargo = mock(Cargo.class);

		final EList<Slot> slots = new BasicEList<Slot>();
		slots.add(loadSlot);
		slots.add(dischargeSlot);
		when(cargo.getSlots()).thenReturn(slots);
		final Port loadPort = mock(Port.class);
		final Port dischargePort = mock(Port.class);
		final Port otherPort = mock(Port.class);

		final Contract loadContract = mock(Contract.class);
		final Contract dischargeContract = mock(Contract.class);
		final Contract otherContract = mock(Contract.class);

		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(validationContext.createSuccessStatus()).thenReturn(successStatus);
		when(validationContext.createFailureStatus()).thenReturn(failureStatus);
		when(validationContext.createFailureStatus(Matchers.any())).thenReturn(failureStatus);
		when(successStatus.isOK()).thenReturn(true);
		when(failureStatus.isOK()).thenReturn(false);
		when(validationContext.getTarget()).thenReturn(cargo);
		when(successStatus.getSeverity()).thenReturn(IStatus.OK);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		// Set up the expected return values of methods.
		when(loadSlot.getPort()).thenReturn(loadPort);
		when(dischargeSlot.getPort()).thenReturn(dischargePort);

		when(loadSlot.getContract()).thenReturn(loadContract);
		when(dischargeSlot.getContract()).thenReturn(dischargeContract);

		when(loadContract.getRestrictedContracts()).thenReturn(ECollections.singletonEList(otherContract));
		when(dischargeContract.getRestrictedContracts()).thenReturn(ECollections.singletonEList(otherContract));

		when(loadPort.collect(Matchers.any())).thenReturn(ECollections.<Port> singletonEList(loadPort));
		when(dischargePort.collect(Matchers.any())).thenReturn(ECollections.<Port> singletonEList(dischargePort));
		when(otherPort.collect(Matchers.any())).thenReturn(ECollections.<Port> singletonEList(otherPort));

		when(loadContract.getRestrictedPorts()).thenReturn(ECollections.<APortSet<Port>> singletonEList(otherPort));
		when(dischargeContract.getRestrictedPorts()).thenReturn(ECollections.<APortSet<Port>> singletonEList(otherPort));

		when(loadContract.isRestrictedListsArePermissive()).thenReturn(false);
		when(dischargeContract.isRestrictedListsArePermissive()).thenReturn(false);

		// validate the constraint using the mocked expected values set above
		final IStatus status1 = constraint.validate(validationContext);
		Assert.assertTrue(status1.isOK());

		// Flip black/white mode

		when(loadContract.isRestrictedListsArePermissive()).thenReturn(true);
		when(dischargeContract.isRestrictedListsArePermissive()).thenReturn(true);

		// validate the constraint using the mocked expected values set above
		final IStatus status2 = constraint.validate(validationContext);
		Assert.assertFalse(status2.isOK());
	}

	@Test
	public void testRestrictedElementsContraint_LoadContractRestricted() {

		// This is the constraint we will be testing
		final RestrictedElementsConstraint constraint = new RestrictedElementsConstraint();

		final LoadSlot loadSlot = mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = mock(DischargeSlot.class);
		final Cargo cargo = mock(Cargo.class);

		final EList<Slot> slots = new BasicEList<Slot>();
		slots.add(loadSlot);
		slots.add(dischargeSlot);
		when(cargo.getSlots()).thenReturn(slots);

		final Port loadPort = mock(Port.class);
		final Port dischargePort = mock(Port.class);

		final Contract loadContract = mock(Contract.class);
		final Contract dischargeContract = mock(Contract.class);

		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(validationContext.createSuccessStatus()).thenReturn(successStatus);
		when(validationContext.createFailureStatus()).thenReturn(failureStatus);
		when(validationContext.createFailureStatus(Matchers.any())).thenReturn(failureStatus);
		when(successStatus.isOK()).thenReturn(true);
		when(failureStatus.isOK()).thenReturn(false);
		when(validationContext.getTarget()).thenReturn(cargo);
		when(successStatus.getSeverity()).thenReturn(IStatus.OK);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		when(loadPort.collect(Matchers.any())).thenReturn(ECollections.<Port> singletonEList(loadPort));
		when(dischargePort.collect(Matchers.any())).thenReturn(ECollections.<Port> singletonEList(dischargePort));

		// Set up the expected return values of methods.
		when(loadSlot.getPort()).thenReturn(loadPort);
		when(dischargeSlot.getPort()).thenReturn(dischargePort);

		when(loadSlot.getContract()).thenReturn(loadContract);
		when(dischargeSlot.getContract()).thenReturn(dischargeContract);

		when(loadContract.getRestrictedContracts()).thenReturn(ECollections.<Contract> emptyEList());
		when(dischargeContract.getRestrictedContracts()).thenReturn(ECollections.singletonEList(loadContract));

		when(loadContract.getRestrictedPorts()).thenReturn(ECollections.<APortSet<Port>> emptyEList());
		when(dischargeContract.getRestrictedPorts()).thenReturn(ECollections.<APortSet<Port>> emptyEList());

		when(loadContract.isRestrictedListsArePermissive()).thenReturn(false);
		when(dischargeContract.isRestrictedListsArePermissive()).thenReturn(false);

		// validate the constraint using the mocked expected values set above
		final IStatus status1 = constraint.validate(validationContext);
		Assert.assertFalse(status1.isOK());

		// Flip black/white mode

		when(loadContract.isRestrictedListsArePermissive()).thenReturn(true);
		when(dischargeContract.isRestrictedListsArePermissive()).thenReturn(true);

		// validate the constraint using the mocked expected values set above
		final IStatus status2 = constraint.validate(validationContext);
		Assert.assertTrue(status2.isOK());
	}

	@Test
	public void testRestrictedElementsContraint_DischargeContractRestricted() {

		// This is the constraint we will be testing
		final RestrictedElementsConstraint constraint = new RestrictedElementsConstraint();

		final LoadSlot loadSlot = mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = mock(DischargeSlot.class);
		final Cargo cargo = mock(Cargo.class);

		final EList<Slot> slots = new BasicEList<Slot>();
		slots.add(loadSlot);
		slots.add(dischargeSlot);
		when(cargo.getSlots()).thenReturn(slots);

		final Port loadPort = mock(Port.class);
		final Port dischargePort = mock(Port.class);

		final Contract loadContract = mock(Contract.class);
		final Contract dischargeContract = mock(Contract.class);

		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(validationContext.createSuccessStatus()).thenReturn(successStatus);
		when(validationContext.createFailureStatus()).thenReturn(failureStatus);
		when(validationContext.createFailureStatus(Matchers.any())).thenReturn(failureStatus);
		when(successStatus.isOK()).thenReturn(true);
		when(failureStatus.isOK()).thenReturn(false);
		when(validationContext.getTarget()).thenReturn(cargo);
		when(successStatus.getSeverity()).thenReturn(IStatus.OK);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		when(loadPort.collect(Matchers.any())).thenReturn(ECollections.<Port> singletonEList(loadPort));
		when(dischargePort.collect(Matchers.any())).thenReturn(ECollections.<Port> singletonEList(dischargePort));

		// Set up the expected return values of methods.
		when(loadSlot.getPort()).thenReturn(loadPort);
		when(dischargeSlot.getPort()).thenReturn(dischargePort);

		when(loadSlot.getContract()).thenReturn(loadContract);
		when(dischargeSlot.getContract()).thenReturn(dischargeContract);

		when(loadContract.getRestrictedContracts()).thenReturn(ECollections.singletonEList(dischargeContract));
		when(dischargeContract.getRestrictedContracts()).thenReturn(ECollections.<Contract> emptyEList());

		when(loadContract.getRestrictedPorts()).thenReturn(ECollections.<APortSet<Port>> emptyEList());
		when(dischargeContract.getRestrictedPorts()).thenReturn(ECollections.<APortSet<Port>> emptyEList());

		when(loadContract.isRestrictedListsArePermissive()).thenReturn(false);
		when(dischargeContract.isRestrictedListsArePermissive()).thenReturn(false);

		// validate the constraint using the mocked expected values set above
		final IStatus status1 = constraint.validate(validationContext);
		Assert.assertFalse(status1.isOK());

		// Flip black/white mode

		when(loadContract.isRestrictedListsArePermissive()).thenReturn(true);
		when(dischargeContract.isRestrictedListsArePermissive()).thenReturn(true);

		// validate the constraint using the mocked expected values set above
		final IStatus status2 = constraint.validate(validationContext);
		Assert.assertTrue(status2.isOK());
	}

	@Test
	public void testRestrictedElementsContraint_LoadPortRestricted() {

		// This is the constraint we will be testing
		final RestrictedElementsConstraint constraint = new RestrictedElementsConstraint();

		final LoadSlot loadSlot = mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = mock(DischargeSlot.class);
		final Cargo cargo = mock(Cargo.class);
		final EList<Slot> slots = new BasicEList<Slot>();
		slots.add(loadSlot);
		slots.add(dischargeSlot);
		when(cargo.getSlots()).thenReturn(slots);

		final Port loadPort = mock(Port.class);
		final Port dischargePort = mock(Port.class);

		final Contract loadContract = mock(Contract.class);
		final Contract dischargeContract = mock(Contract.class);

		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(validationContext.createSuccessStatus()).thenReturn(successStatus);
		when(validationContext.createFailureStatus()).thenReturn(failureStatus);
		when(validationContext.createFailureStatus(Matchers.any())).thenReturn(failureStatus);
		when(successStatus.isOK()).thenReturn(true);
		when(failureStatus.isOK()).thenReturn(false);
		when(validationContext.getTarget()).thenReturn(cargo);
		when(successStatus.getSeverity()).thenReturn(IStatus.OK);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		when(loadPort.collect(Matchers.any())).thenReturn(ECollections.<Port> singletonEList(loadPort));
		when(dischargePort.collect(Matchers.any())).thenReturn(ECollections.<Port> singletonEList(dischargePort));

		// Set up the expected return values of methods.
		when(loadSlot.getPort()).thenReturn(loadPort);
		when(dischargeSlot.getPort()).thenReturn(dischargePort);

		when(loadSlot.getContract()).thenReturn(loadContract);
		when(dischargeSlot.getContract()).thenReturn(dischargeContract);

		when(loadContract.getRestrictedContracts()).thenReturn(ECollections.<Contract> emptyEList());
		when(dischargeContract.getRestrictedContracts()).thenReturn(ECollections.<Contract> emptyEList());

		when(loadContract.getRestrictedPorts()).thenReturn(ECollections.<APortSet<Port>> emptyEList());
		when(dischargeContract.getRestrictedPorts()).thenReturn(ECollections.<APortSet<Port>> singletonEList(loadPort));

		when(loadContract.isRestrictedListsArePermissive()).thenReturn(false);
		when(dischargeContract.isRestrictedListsArePermissive()).thenReturn(false);

		// validate the constraint using the mocked expected values set above
		final IStatus status1 = constraint.validate(validationContext);
		Assert.assertFalse(status1.isOK());

		// Flip black/white mode

		when(loadContract.isRestrictedListsArePermissive()).thenReturn(true);
		when(dischargeContract.isRestrictedListsArePermissive()).thenReturn(true);

		// validate the constraint using the mocked expected values set above
		final IStatus status2 = constraint.validate(validationContext);
		Assert.assertTrue(status2.isOK());
	}

	@Test
	public void testRestrictedElementsContraint_LoadPortRestricted_PortGroup() {

		// This is the constraint we will be testing
		final RestrictedElementsConstraint constraint = new RestrictedElementsConstraint();

		final LoadSlot loadSlot = mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = mock(DischargeSlot.class);
		final Cargo cargo = mock(Cargo.class);
		final EList<Slot> slots = new BasicEList<Slot>();
		slots.add(loadSlot);
		slots.add(dischargeSlot);
		when(cargo.getSlots()).thenReturn(slots);

		final Port loadPort1 = mock(Port.class);
		final Port loadPort2 = mock(Port.class);

		PortGroup portGroup = mock(PortGroup.class);
		when(portGroup.getContents()).thenReturn(ECollections.<APortSet<Port>> asEList(loadPort1, loadPort2));

		final Port dischargePort = mock(Port.class);

		final Contract loadContract = mock(Contract.class);
		final Contract dischargeContract = mock(Contract.class);

		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(validationContext.createSuccessStatus()).thenReturn(successStatus);
		when(validationContext.createFailureStatus()).thenReturn(failureStatus);
		when(validationContext.createFailureStatus(Matchers.any())).thenReturn(failureStatus);
		when(successStatus.isOK()).thenReturn(true);
		when(failureStatus.isOK()).thenReturn(false);
		when(validationContext.getTarget()).thenReturn(cargo);
		when(successStatus.getSeverity()).thenReturn(IStatus.OK);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		
		when(loadPort1.collect(Matchers.any())).thenReturn(ECollections.<Port> singletonEList(loadPort1));
		when(loadPort2.collect(Matchers.any())).thenReturn(ECollections.<Port> singletonEList(loadPort2));
		when(dischargePort.collect(Matchers.any())).thenReturn(ECollections.<Port> singletonEList(dischargePort));
		when(portGroup.collect(Matchers.any())).thenReturn(ECollections.<Port>   asEList(loadPort1, loadPort2));
		
		// Set up the expected return values of methods.
		when(loadSlot.getPort()).thenReturn(loadPort1);
		when(dischargeSlot.getPort()).thenReturn(dischargePort);

		when(loadSlot.getContract()).thenReturn(loadContract);
		when(dischargeSlot.getContract()).thenReturn(dischargeContract);

		when(loadContract.getRestrictedContracts()).thenReturn(ECollections.<Contract> emptyEList());
		when(dischargeContract.getRestrictedContracts()).thenReturn(ECollections.<Contract> emptyEList());

		when(loadContract.getRestrictedPorts()).thenReturn(ECollections.<APortSet<Port>> emptyEList());

		when(dischargeContract.getRestrictedPorts()).thenReturn(ECollections.<APortSet<Port>> singletonEList(portGroup));

		when(loadContract.isRestrictedListsArePermissive()).thenReturn(false);
		when(dischargeContract.isRestrictedListsArePermissive()).thenReturn(false);

		// validate the constraint using the mocked expected values set above
		final IStatus status1 = constraint.validate(validationContext);
		Assert.assertFalse(status1.isOK());

		// Flip black/white mode

		when(loadContract.isRestrictedListsArePermissive()).thenReturn(true);
		when(dischargeContract.isRestrictedListsArePermissive()).thenReturn(true);

		// validate the constraint using the mocked expected values set above
		final IStatus status2 = constraint.validate(validationContext);
		Assert.assertTrue(status2.isOK());
	}

	@Test
	public void testRestrictedElementsContraint_DischargePortRestricted() {

		// This is the constraint we will be testing
		final RestrictedElementsConstraint constraint = new RestrictedElementsConstraint();

		final LoadSlot loadSlot = mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = mock(DischargeSlot.class);
		final Cargo cargo = mock(Cargo.class);

		final EList<Slot> slots = new BasicEList<Slot>();
		slots.add(loadSlot);
		slots.add(dischargeSlot);
		when(cargo.getSlots()).thenReturn(slots);

		final Port loadPort = mock(Port.class);
		final Port dischargePort = mock(Port.class);

		final Contract loadContract = mock(Contract.class);
		final Contract dischargeContract = mock(Contract.class);

		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(validationContext.createSuccessStatus()).thenReturn(successStatus);
		when(validationContext.createFailureStatus()).thenReturn(failureStatus);
		when(validationContext.createFailureStatus(Matchers.any())).thenReturn(failureStatus);
		when(successStatus.isOK()).thenReturn(true);
		when(failureStatus.isOK()).thenReturn(false);
		when(validationContext.getTarget()).thenReturn(cargo);
		when(successStatus.getSeverity()).thenReturn(IStatus.OK);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		// Set up the expected return values of methods.
		when(loadSlot.getPort()).thenReturn(loadPort);
		when(dischargeSlot.getPort()).thenReturn(dischargePort);

		when(loadSlot.getContract()).thenReturn(loadContract);
		when(dischargeSlot.getContract()).thenReturn(dischargeContract);

		when(loadContract.getRestrictedContracts()).thenReturn(ECollections.<Contract> emptyEList());
		when(dischargeContract.getRestrictedContracts()).thenReturn(ECollections.<Contract> emptyEList());

		when(loadContract.getRestrictedPorts()).thenReturn(ECollections.<APortSet<Port>> singletonEList(dischargePort));
		when(dischargeContract.getRestrictedPorts()).thenReturn(ECollections.<APortSet<Port>> emptyEList());

		when(loadContract.isRestrictedListsArePermissive()).thenReturn(false);
		when(dischargeContract.isRestrictedListsArePermissive()).thenReturn(false);

		// validate the constraint using the mocked expected values set above
		final IStatus status1 = constraint.validate(validationContext);
		Assert.assertFalse(status1.isOK());

		// Flip black/white mode

		when(loadContract.isRestrictedListsArePermissive()).thenReturn(true);
		when(dischargeContract.isRestrictedListsArePermissive()).thenReturn(true);

		// validate the constraint using the mocked expected values set above
		final IStatus status2 = constraint.validate(validationContext);
		Assert.assertTrue(status2.isOK());
	}
}
