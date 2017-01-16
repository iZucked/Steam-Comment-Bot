/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation.tests;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.validation.VesselAvailabilityPortConstraint;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Test the {@link VesselAvailabilityPortConstraintTest}. You will need the org.jmock eclipse plugin from our codebase repository to run this, as jmock doesn't currently provide an eclipse plugin.
 * 
 * TODO test behaviour of test when passed a VesselClass. It's a bit less tidy.
 * 
 * @author hinton
 * 
 */
public class VesselAvailabilityPortConstraintTest {

	@Ignore("Does not work with API changes")
	@Test
	public void testValidityConstraintFromPortAndTime() {
		// This is the constraint we will be testing
		final VesselAvailabilityPortConstraint constraint = new VesselAvailabilityPortConstraint();
		// Mock a port and time to test
		final VesselAvailability availability = mock(VesselAvailability.class);
		// Mock a vessel for it to belong to.
		final Vessel vessel = mock(Vessel.class);
		// Mock a port to think about
		@SuppressWarnings("unchecked")
		final EList<APortSet<Port>> startPortSet = mock(EList.class,  "Port Set A");
		@SuppressWarnings("unchecked")
		final EList<APortSet<Port>> endPortSet = mock(EList.class,  "Port Set B");
		
		final Port portA= mock(Port.class,  "Port A");
		
		// Finally mock a vessel class for the vessel to have
		final VesselClass vesselClass = mock(VesselClass.class);

		final IValidationContext validationContext = mock(IValidationContext.class);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);

		@SuppressWarnings("unchecked")
		final EList<APortSet<Port>> inaccessiblePorts = mock(EList.class, "Inaccessible Ports");

		// The constraint will need to be able to get at the port and
		// time's eContainer (the vessel owning it, in this case)
//		when(availability.eContainer()).thenReturn(vessel);
		// It will also need to know which feature in the vessel is
		// containing pat (the start requirement)
//		when(availability.eContainingFeature()).thenReturn(FleetPackage.eINSTANCE.getVessel_Availability());
		when(availability.getVessel()).thenReturn(vessel);
		// It will need to ask the vessel for its class, but this should
		// only happen once
		when(vessel.getVesselClass()).thenReturn(vesselClass);
		// And then it will need to ask the class for its inaccessible
		// port list
		when(vesselClass.getInaccessiblePorts()).thenReturn(inaccessiblePorts);
		// the inaccessible port list will be queried whether it
		// contains port A
		when(inaccessiblePorts.contains(portA)).thenReturn(true);
		// Then it will want to get the port
		when(availability.getStartAt()).thenReturn(startPortSet);
		// Then it will want to get the port
		when(availability.getEndAt()).thenReturn(endPortSet);
		// It will ask for the port's name
		when(portA.getName()).thenReturn("port A");
		// And the vessel's name
		when(vessel.getName()).thenReturn("vessel");
		// and the vessel class' name
		when(vesselClass.getName()).thenReturn("vc");
		// finally it will ask some questions to the context
		// what's the target?
		when(validationContext.getTarget()).thenReturn(availability);
		// and it will want a failure status thing
		when(validationContext.createFailureStatus("vessel", "start", "vc", "port A")).thenReturn(failureStatus);

		final IStatus result = constraint.validate(validationContext);

		Assert.assertTrue("Result should be a detail constraint status decorator", result instanceof DetailConstraintStatusDecorator);
		Assert.assertTrue("Port feature should be flagged up", ((DetailConstraintStatusDecorator) result).getFeaturesForEObject(availability).contains(CargoPackage.eINSTANCE.getVesselAvailability_StartAt()));

		verify(availability, atLeastOnce()).eContainer();
		verify(availability, atLeastOnce()).eContainingFeature();
		verify(availability, atLeastOnce()).getStartAt();
		verify(availability, atLeastOnce()).getEndAt();
		verify(vessel, atLeastOnce()).getVesselClass();
		verify(vessel, atLeastOnce()).getName();
		verify(vesselClass, atLeastOnce()).getInaccessiblePorts();
		verify(vesselClass, atLeastOnce()).getName();
		verify(inaccessiblePorts, atLeastOnce()).contains(portA);
		verify(portA, atLeastOnce()).getName();
		verify(validationContext, atLeastOnce()).getTarget();
		verify(validationContext, atLeastOnce()).createFailureStatus("vessel", "start", "vc", "port A");
		// verify that only the methods above are called.
		verifyNoMoreInteractions(availability);
		verifyNoMoreInteractions(vessel);
		verifyNoMoreInteractions(vesselClass);
		verifyNoMoreInteractions(inaccessiblePorts);
		verifyNoMoreInteractions(portA);
		verifyNoMoreInteractions(validationContext);
	}

	/**
	 * Don't bother with mocking, instead just create a scenario with a port, vessel, etc, and then run it.
	 */
	@Ignore("ValidationSupport doesn't allow mocking, test disabled until mocking removed.")
	@Test
	public void testValidityConstraintFromVesselClass() {
		// This is the constraint we will be testing
		final VesselAvailabilityPortConstraint constraint = new VesselAvailabilityPortConstraint();
		// Mock a port and time to test
		final VesselClass vc = mock(VesselClass.class);
		// Mock a vessel for it to belong to.
		final Vessel vessel = mock(Vessel.class);
		// Mock a port to think about
		final Port portA = mock(Port.class, "Port A");
		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);

		@SuppressWarnings("unchecked")
		final EList<Port> inaccessiblePorts = mock(EList.class);

		// The constraint will need to be able to get vessel class' eContainer (the vessel owning it, in this case)
//		when(vc.eContainer()).thenReturn(vessel);
		// It will also need to know which feature in the vessel is containing vessel class (the start requirement)
//		when(vc.eContainingFeature()).thenReturn(FleetPackage.eINSTANCE.getVessel_Availability());
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
		when(validationContext.getTarget()).thenReturn(vc);

		final IStatus result = constraint.validate(validationContext);

		System.out.println(result);

		Assert.assertTrue("Result should be a detail constraint status decorator", result instanceof DetailConstraintStatusDecorator);

		verify(vc, atLeastOnce()).eContainer();
		verify(vc, atLeastOnce()).eContainingFeature();
		verify(inaccessiblePorts, atLeastOnce()).contains(portA);
		verify(portA, atLeastOnce()).getName();
		verify(vessel, atLeastOnce()).getName();
		verify(validationContext, atLeastOnce()).createFailureStatus(anyString(), anyString(), anyString(), anyString());
		verify(validationContext, atLeastOnce()).getTarget();
		// verify that only the methods above are called.
		verifyNoMoreInteractions(vc);
		verifyNoMoreInteractions(inaccessiblePorts);
		verifyNoMoreInteractions(portA);
		verifyNoMoreInteractions(vessel);
		verifyNoMoreInteractions(validationContext);
	}
}
