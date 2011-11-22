/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.validation;

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

import scenario.fleet.FleetPackage;
import scenario.fleet.PortAndTime;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.port.Port;

import com.mmxlabs.lngscheduler.emf.extras.validation.StartEndRequirementPortConstraint;
import com.mmxlabs.lngscheduler.emf.extras.validation.status.DetailConstraintStatusDecorator;

/**
 * Test the {@link StartEndRequirementPortConstraintTest}. You will need the org.jmock eclipse plugin from our codebase repository to run this, as jmock doesn't currently provide an eclipse plugin.
 * 
 * TODO test behaviour of test when passed a VesselClass. It's a bit less tidy.
 * 
 * @author hinton
 * 
 */
public class StartEndRequirementPortConstraintTest {

	@Test
	public void testValidityConstraintFromPortAndTime() {
		// This is the constraint we will be testing
		final StartEndRequirementPortConstraint constraint = new StartEndRequirementPortConstraint();
		// Mock a port and time to test
		final PortAndTime pat = mock(PortAndTime.class);
		// Mock a vessel for it to belong to.
		final Vessel vessel = mock(Vessel.class);
		// Mock a port to think about
		final Port portA = mock(Port.class, "Port A");
		// Finally mock a vessel class for the vessel to have
		final VesselClass vesselClass = mock(VesselClass.class);

		final IValidationContext validationContext = mock(IValidationContext.class);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);

		@SuppressWarnings("unchecked")
		final EList<Port> inaccessiblePorts = mock(EList.class);

		// The constraint will need to be able to get at the port and
		// time's eContainer (the vessel owning it, in this case)
		when(pat.eContainer()).thenReturn(vessel);
		// It will also need to know which feature in the vessel is
		// containing pat (the start requirement)
		when(pat.eContainingFeature()).thenReturn(FleetPackage.eINSTANCE.getVessel_StartRequirement());
		// It will need to ask the vessel for its class, but this should
		// only happen once
		when(vessel.getClass_()).thenReturn(vesselClass);
		// And then it will need to ask the class for its inaccessible
		// port list
		when(vesselClass.getInaccessiblePorts()).thenReturn(inaccessiblePorts);
		// the inaccessible port list will be queried whether it
		// contains port A
		when(inaccessiblePorts.contains(portA)).thenReturn(true);
		// The constraint will want to ask if the port has been set (it
		// has)
		when(pat.isSetPort()).thenReturn(true);
		// Then it will want to get the port
		when(pat.getPort()).thenReturn(portA);
		// It will ask for the port's name
		when(portA.getName()).thenReturn("port A");
		// And the vessel's name
		when(vessel.getName()).thenReturn("vessel");
		// and the vessel class' name
		when(vesselClass.getName()).thenReturn("vc");
		// finally it will ask some questions to the context
		// what's the target?
		when(validationContext.getTarget()).thenReturn(pat);
		// and it will want a failure status thing
		when(validationContext.createFailureStatus("vessel", "start", "vc", "port A")).thenReturn(failureStatus);

		final IStatus result = constraint.validate(validationContext);

		Assert.assertTrue("Result should be a detail constraint status decorator", result instanceof DetailConstraintStatusDecorator);
		Assert.assertTrue("Port feature should be flagged up", ((DetailConstraintStatusDecorator) result).getFeaturesForEObject(pat).contains(FleetPackage.eINSTANCE.getPortAndTime_Port()));

		verify(pat, atLeastOnce()).eContainer();
		verify(pat, atLeastOnce()).eContainingFeature();
		verify(pat, atLeastOnce()).isSetPort();
		verify(pat, atLeastOnce()).getPort();
		verify(vessel, atLeastOnce()).getClass_();
		verify(vessel, atLeastOnce()).getName();
		verify(vesselClass, atLeastOnce()).getInaccessiblePorts();
		verify(vesselClass, atLeastOnce()).getName();
		verify(inaccessiblePorts, atLeastOnce()).contains(portA);
		verify(portA, atLeastOnce()).getName();
		verify(validationContext, atLeastOnce()).getTarget();
		verify(validationContext, atLeastOnce()).createFailureStatus("vessel", "start", "vc", "port A");
		// verify that only the methods above are called.
		verifyNoMoreInteractions(pat);
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
		final StartEndRequirementPortConstraint constraint = new StartEndRequirementPortConstraint();
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
		when(vc.eContainer()).thenReturn(vessel);
		// It will also need to know which feature in the vessel is containing vessel class (the start requirement)
		when(vc.eContainingFeature()).thenReturn(FleetPackage.eINSTANCE.getVessel_StartRequirement());
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
