/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Test;

import scenario.fleet.FleetPackage;
import scenario.fleet.PortAndTime;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.port.Port;

import com.mmxlabs.lngscheduler.emf.extras.validation.StartEndRequirementPortConstraint;
import com.mmxlabs.lngscheduler.emf.extras.validation.status.DetailConstraintStatusDecorator;

/**
 * Test the {@link StartEndRequirementPortConstraintTest}. You will need the
 * org.jmock eclipse plugin from our codebase repository to run this, as jmock
 * doesn't currently provide an eclipse plugin.
 * 
 * TODO test behaviour of test when passed a VesselClass. It's a bit less tidy.
 * 
 * @author hinton
 * 
 */
public class StartEndRequirementPortConstraintTest {
	@Test
	public void testValidityConstraint() {
		// Create a mockery to mock up all the objects involved in a test
		final Mockery context = new Mockery();
		// This is the constraint we will be testing
		final StartEndRequirementPortConstraint constraint = new StartEndRequirementPortConstraint();
		// Mock a port and time to test
		final PortAndTime pat = context.mock(PortAndTime.class);
		// Mock a vessel for it to belong to.
		final Vessel vessel = context.mock(Vessel.class);
		// Mock a port to think about
		final Port portA = context.mock(Port.class, "Port A");
		final Port portB = context.mock(Port.class, "Port B");
		// Finally mock a vessel class for the vessel to have
		final VesselClass vesselClass = context.mock(VesselClass.class);

		final IValidationContext validationContext = context
				.mock(IValidationContext.class);

		final IConstraintStatus failureStatus = context
				.mock(IConstraintStatus.class);

		final EList inaccessiblePorts = context.mock(EList.class);

		context.checking(new Expectations() {
			{
				// The constraint will need to be able to get at the port and
				// time's eContainer (the vessel owning it, in this case)
				atLeast(1).of(pat).eContainer();
				will(returnValue(vessel));
				// It will also need to know which feature in the vessel is
				// containing pat (the start requirement)
				atLeast(1).of(pat).eContainingFeature();
				will(returnValue(FleetPackage.eINSTANCE
						.getVessel_StartRequirement()));
				// It will need to ask the vessel for its class, but this should
				// only happen once
				atLeast(1).of(vessel).getClass_();
				will(returnValue(vesselClass));
				// And then it will need to ask the class for its inaccessible
				// port list
				exactly(1).of(vesselClass).getInaccessiblePorts();
				will(returnValue(inaccessiblePorts));

				// the inaccessible port list will be queried whether it
				// contains port A
				exactly(1).of(inaccessiblePorts).contains(portA);
				will(returnValue(true));

				// The constraint will want to ask if the port has been set (it
				// has)
				atLeast(1).of(pat).isSetPort();
				will(returnValue(true));

				// Then it will want to get the port
				atLeast(1).of(pat).getPort();
				will(returnValue(portA));

				// It will ask for the port's name
				atLeast(1).of(portA).getName();
				will(returnValue("port A"));

				// And the vessel's name
				atLeast(1).of(vessel).getName();
				will(returnValue("vessel"));

				// and the vessel class' name
				atLeast(1).of(vesselClass).getName();
				will(returnValue("vc"));

				// finally it will ask some questions to the context
				// what's the target?
				exactly(1).of(validationContext).getTarget();
				will(returnValue(pat));

				// and it will want a failure status thing
				exactly(1).of(validationContext).createFailureStatus("vessel",
						"start", "vc", "port A");
				will(returnValue(failureStatus));
			}
		});

		final IStatus result = constraint.validate(validationContext);

		Assert.assertTrue(
				"Result should be a detail constraint status decorator",
				result instanceof DetailConstraintStatusDecorator);
		Assert.assertTrue(
				"Port feature should be flagged up",
				((DetailConstraintStatusDecorator) result)
						.getFeaturesForEObject(pat).contains(
								FleetPackage.eINSTANCE.getPortAndTime_Port()));

		context.assertIsSatisfied();
	}
	
	@Test
	public void testValidityConstraintFromVesselClass() {
		// Create a mockery to mock up all the objects involved in a test
		final Mockery context = new Mockery();
		// This is the constraint we will be testing
		final StartEndRequirementPortConstraint constraint = new StartEndRequirementPortConstraint();
		// Mock a port and time to test
		final VesselClass vc = context.mock(VesselClass.class);
		// Mock a vessel for it to belong to.
		final Vessel vessel = context.mock(Vessel.class);
		// Mock a port to think about
		final Port portA = context.mock(Port.class, "Port A");
		final Port portB = context.mock(Port.class, "Port B");

		final IValidationContext validationContext = context
				.mock(IValidationContext.class);

		final IConstraintStatus failureStatus = context
				.mock(IConstraintStatus.class);

		final EList inaccessiblePorts = context.mock(EList.class);

		context.checking(new Expectations() {
			{
				// The constraint will need to be able to get at the port and
				// time's eContainer (the vessel owning it, in this case)
				atLeast(1).of(vc).eContainer();
				will(returnValue(vessel));
				// It will also need to know which feature in the vessel is
				// containing pat (the start requirement)
				atLeast(1).of(vc).eContainingFeature();
				will(returnValue(FleetPackage.eINSTANCE
						.getVessel_StartRequirement()));
				

				// the inaccessible port list will be queried whether it
				// contains port A
				exactly(1).of(inaccessiblePorts).contains(portA);
				will(returnValue(true));

//				// The constraint will want to ask if the port has been set (it
//				// has)
//				atLeast(1).of(vc).isSetPort();
//				will(returnValue(true));
//
//				// Then it will want to get the port
//				atLeast(1).of(vc).getPort();
//				will(returnValue(portA));

				// It will ask for the port's name
				atLeast(1).of(portA).getName();
				will(returnValue("port A"));

				// And the vessel's name
				atLeast(1).of(vessel).getName();
				will(returnValue("vessel"));

//				// finally it will ask some questions to the context
//				// what's the target?
//				exactly(1).of(validationContext).getTarget();
//				will(returnValue(pat));

				// and it will want a failure status thing
				exactly(1).of(validationContext).createFailureStatus("vessel",
						"start", "vc", "port A");
				will(returnValue(failureStatus));
			}
		});

		final IStatus result = constraint.validate(validationContext);

		Assert.assertTrue(
				"Result should be a detail constraint status decorator",
				result instanceof DetailConstraintStatusDecorator);
//		Assert.assertTrue(
//				"Port feature should be flagged up",
//				((DetailConstraintStatusDecorator) result)
//						.getFeaturesForEObject(pat).contains(
//								FleetPackage.eINSTANCE.getPortAndTime_Port()));

		context.assertIsSatisfied();
	}
}
