/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation.tests;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.validation.VesselCharterPortConstraint;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Test the {@link VesselCharterPortConstraintTest}. You will need the org.jmock eclipse plugin from our codebase repository to run this, as jmock doesn't currently provide an eclipse plugin.
 * 
 * TODO test behaviour of test when passed a VesselClass. It's a bit less tidy.
 * 
 * @author hinton
 * 
 */
public class VesselCharterPortConstraintTest {

	@Disabled("Does not work with API changes")
	@Test
	public void testValidityConstraintFromPortAndTime() {
		// This is the constraint we will be testing
		final VesselCharterPortConstraint constraint = new VesselCharterPortConstraint();
		// Mock a port and time to test
		final VesselCharter vesselCharter = mock(VesselCharter.class);
		// Mock a vessel for it to belong to.
		final Vessel vessel = mock(Vessel.class);
		// Mock a port to think about
		// @SuppressWarnings("unchecked")
		// final EList<APortSet<Port>> startPortSet = mock(EList.class, "Port Set A");
		@SuppressWarnings("unchecked")
		final EList<APortSet<Port>> endPortSet = mock(EList.class, "Port Set B");

		final Port portA = mock(Port.class, "Port A");

		final IValidationContext validationContext = mock(IValidationContext.class);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);

		@SuppressWarnings("unchecked")
		final EList<APortSet<Port>> inaccessiblePorts = mock(EList.class, "Inaccessible Ports");

		// The constraint will need to be able to get at the port and
		// time's eContainer (the vessel owning it, in this case)
		// when(vesselCharter.eContainer()).thenReturn(vessel);
		// It will also need to know which feature in the vessel is
		// containing pat (the start requirement)
		when(vesselCharter.getVessel()).thenReturn(vessel);
		// And then it will need to ask the class for its inaccessible
		// port list
		when(vessel.getVesselOrDelegateInaccessiblePorts()).thenReturn(inaccessiblePorts);
		// the inaccessible port list will be queried whether it
		// contains port A
		when(inaccessiblePorts.contains(portA)).thenReturn(true);
		// Then it will want to get the port
		// when(availability.getStartAt()).thenReturn(startPortSet);
		// Then it will want to get the port
		when(vesselCharter.getEndAt()).thenReturn(endPortSet);
		// It will ask for the port's name
		when(portA.getName()).thenReturn("port A");
		// And the vessel's name
		when(vessel.getName()).thenReturn("vessel");
		// finally it will ask some questions to the context
		// what's the target?
		when(validationContext.getTarget()).thenReturn(vesselCharter);
		// and it will want a failure status thing
		when(validationContext.createFailureStatus("vessel", "start", "vc", "port A")).thenReturn(failureStatus);

		final IStatus result = constraint.validate(validationContext);

		Assertions.assertTrue(result instanceof DetailConstraintStatusDecorator, "Result should be a detail constraint status decorator");
		Assertions.assertTrue(((DetailConstraintStatusDecorator) result).getFeaturesForEObject(vesselCharter).contains(CargoPackage.eINSTANCE.getVesselCharter_StartAt()),
				"Port feature should be flagged up");

		verify(vesselCharter, atLeastOnce()).eContainer();
		verify(vesselCharter, atLeastOnce()).eContainingFeature();
		verify(vesselCharter, atLeastOnce()).getStartAt();
		verify(vesselCharter, atLeastOnce()).getEndAt();
		verify(vessel, atLeastOnce()).getName();
		verify(vessel, atLeastOnce()).getVesselOrDelegateInaccessiblePorts();
		verify(inaccessiblePorts, atLeastOnce()).contains(portA);
		verify(portA, atLeastOnce()).getName();
		verify(validationContext, atLeastOnce()).getTarget();
		verify(validationContext, atLeastOnce()).createFailureStatus("vessel", "start", "vc", "port A");
		// verify that only the methods above are called.
		verifyNoMoreInteractions(vesselCharter);
		verifyNoMoreInteractions(vessel);
		verifyNoMoreInteractions(inaccessiblePorts);
		verifyNoMoreInteractions(portA);
		verifyNoMoreInteractions(validationContext);
	}

	/**
	 * Don't bother with mocking, instead just create a scenario with a port, vessel, etc, and then run it.
	 */
	@Disabled("ValidationSupport doesn't allow mocking, test disabled until mocking removed.")
	@Test
	public void testValidityConstraintFromVesselClass() {
		// This is the constraint we will be testing
		final VesselCharterPortConstraint constraint = new VesselCharterPortConstraint();
		// Mock a vessel for it to belong to.
		final Vessel vessel = mock(Vessel.class);
		// Mock a port to think about
		final Port portA = mock(Port.class, "Port A");
		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);

		@SuppressWarnings("unchecked")
		final EList<Port> inaccessiblePorts = mock(EList.class);

		// the inaccessible port list will be queried whether it contains port A
		when(inaccessiblePorts.contains(portA)).thenReturn(true);
		// It will ask for the port's name
		when(portA.getName()).thenReturn("port A");
		// And the vessel's name
		when(vessel.getName()).thenReturn("vessel");
		// and it will want a failure status thing
		when(validationContext.createFailureStatus("vessel", "start", "vc", "port A")).thenReturn(failureStatus);
		// finally it will ask some questions to the context
		// what's the target?
		when(validationContext.getTarget()).thenReturn(vessel);

		final IStatus result = constraint.validate(validationContext);

		System.out.println(result);

		Assertions.assertTrue(result instanceof DetailConstraintStatusDecorator, "Result should be a detail constraint status decorator");

		verify(vessel, atLeastOnce()).eContainer();
		verify(vessel, atLeastOnce()).eContainingFeature();
		verify(inaccessiblePorts, atLeastOnce()).contains(portA);
		verify(portA, atLeastOnce()).getName();
		verify(vessel, atLeastOnce()).getName();
		verify(validationContext, atLeastOnce()).createFailureStatus(anyString(), anyString(), anyString(), anyString());
		verify(validationContext, atLeastOnce()).getTarget();
		// verify that only the methods above are called.
		verifyNoMoreInteractions(inaccessiblePorts);
		verifyNoMoreInteractions(portA);
		verifyNoMoreInteractions(vessel);
		verifyNoMoreInteractions(validationContext);
	}
}
