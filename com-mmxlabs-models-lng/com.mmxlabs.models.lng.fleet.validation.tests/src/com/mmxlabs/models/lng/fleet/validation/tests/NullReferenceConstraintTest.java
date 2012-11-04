/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation.tests;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;

import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.fleet.validation.NullReferenceConstraint;
import com.mmxlabs.models.lng.port.PortFactory;

/**
 * 
 * From case 174:
 * <p>
 * This test is meant to check for certain null values on scenario objects - because of how it works you probably don't want to use mock objects for the targets (you'll have to mock loads of EMF
 * generated methods), but instead construct them using the EMF generated factories.
 * <p>
 * 
 * For example to create a VesselClass you do FleetFactory.eINSTANCE.createVesselClass(), or to create an Entity you do ContractPackage.eINSTANCE.createEntity(). If you look in lng.ecore each EClass
 * belongs to a package, which has an associated factory which is called <Packagename>Factory and has a single instance you can get through the .eINSTANCE field.
 * <p>
 * 
 * Anyway, the following fields on the following classes should make the validation constraint produce an error if they are null:
 * <p>
 * 
 * VesselClass --- basefuel, ladenAttributes, ballastAttributes
 * <p>
 * 
 * Vessel --- class (called class_ in the generated class, because of the java getClass() method)
 * <p>
 * 
 * Slot --- port
 * <p>
 * 
 * Cargo --- loadSlot, dischargeSlot
 * <p>
 * 
 * PortAndTime --- port
 * <p>
 * 
 * VesselEvent --- startPort
 * <p>
 * 
 * CharterOut --- endPort
 * <p>
 * 
 */
public class NullReferenceConstraintTest {

	private VesselEvent initVesselEvent() {
		final VesselEvent vesselEvent = FleetFactory.eINSTANCE.createDryDockEvent();
		vesselEvent.setPort(PortFactory.eINSTANCE.createPort());
		return vesselEvent;
	}

	private CharterOutEvent initCharterOut() {
		final CharterOutEvent co = FleetFactory.eINSTANCE.createCharterOutEvent();

		// start and end ports are both checked and are initially null, so set them.
		co.setRelocateTo(PortFactory.eINSTANCE.createPort());
		co.setPort(PortFactory.eINSTANCE.createPort());
		return co;
	}


	private VesselClass initVesselClass() {
		final VesselClass vesselClass = FleetFactory.eINSTANCE.createVesselClass();
		// create and set values for fields that start out as null
		vesselClass.setBaseFuel(FleetFactory.eINSTANCE.createBaseFuel());
		vesselClass.setBallastAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
		vesselClass.setLadenAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());

		return vesselClass;
	}

	private Vessel initVessel() {
		final Vessel v = FleetFactory.eINSTANCE.createVessel();

		// vessel class is null by default, so add a value.
		v.setVesselClass(FleetFactory.eINSTANCE.createVesselClass());
		return v;
	}

	/**
	 * Test the interface VesselEvent by using the implementation CharterOut.
	 * <p>
	 * {@link VesselEvent#getStartPort()} should not be null, and the constraint should fail when it is.
	 */
	@Test
	public void testVesselEvent() {
		final VesselEvent ve = initVesselEvent();
		// test it
		Assert.assertNotNull(ve.getPort());
		testNullReferenceConstraint(true, ve);
	}

	/**
	 * Test the interface VesselEvent by using the implementation CharterOut.
	 * <p>
	 * {@link VesselEvent#getStartPort()} should not be null, and the constraint should fail when it is.
	 */
	@Test
	public void testVesselEventNullPort() {
		// CharterOut is an implementation of VesselEvent
		final VesselEvent ve = initVesselEvent();
		// Set start port to null.
		ve.setPort(null);
		// now the test should fail.
		testNullReferenceConstraint(false, ve);
	}

	/**
	 * Check {@link CharterOut#getEndPort()} is not null, and expect a failure if it is.
	 * <p>
	 * NullReferenceConstraint also checks the start port (since CharterOut is an implementation of VesselEvent, which is checked for the start port), so here it is set to a value.
	 */
	@Test
	public void testCharterOut() {
		final CharterOutEvent co = initCharterOut();

		// only test end port. Start port is tested by testVesselEvent()
		Assert.assertNotNull(co.getRelocateTo());
		testNullReferenceConstraint(true, co);
	}

	/**
	 * Check {@link CharterOut#getEndPort()} is not null, and expect a failure if it is.
	 * <p>
	 * NullReferenceConstraint also checks the start port (since CharterOut is an implementation of VesselEvent, which is checked for the start port), so here it is set to a value.
	 */
	@Test
	public void testCharterOutEndPortNull() {
		final CharterOutEvent co = initCharterOut();

		// Set the end port to null.
		co.setRelocateTo(null);
		// now the test should fail.
		testNullReferenceConstraint(false, co);
	}


	/**
	 * Test that {@link VesselClass#getBaseFuel()}, {@link VesselClass#getBallastAttributes()}, and {@link VesselClass#getLadenAttributes()} are not null. If any one is null, the test should fail.
	 */
	@Test
	public void testVesselClass() {

		final VesselClass vc = initVesselClass();

		// expect it not to be null, and so succeed.
		testNullReferenceConstraint(true, vc);
	}

	/**
	 * Test that {@link VesselClass#getBaseFuel()}, {@link VesselClass#getBallastAttributes()}, and {@link VesselClass#getLadenAttributes()} are not null. If any one is null, the test should fail.
	 */
	@Test
	public void testVesselClassBaseFuelNull() {

		final VesselClass vc = initVesselClass();

		// test the base fuel being null
		vc.setBaseFuel(null);
		testNullReferenceConstraint(false, vc);
	}

	/**
	 * Test that {@link VesselClass#getBaseFuel()}, {@link VesselClass#getBallastAttributes()}, and {@link VesselClass#getLadenAttributes()} are not null. If any one is null, the test should fail.
	 */
	@Test
	public void testVesselClassNullBallastAttributes() {

		final VesselClass vc = initVesselClass();

		// test the ballast attributes being null
		vc.setBallastAttributes(null);
		testNullReferenceConstraint(false, vc);
	}

	/**
	 * Test that {@link VesselClass#getBaseFuel()}, {@link VesselClass#getBallastAttributes()}, and {@link VesselClass#getLadenAttributes()} are not null. If any one is null, the test should fail.
	 */
	@Test
	public void testVesselClassLadenAttributesNull() {

		final VesselClass vc = initVesselClass();
		// test the laden attributes being null
		vc.setLadenAttributes(null);
		testNullReferenceConstraint(false, vc);
	}

	/**
	 * Test that {@link Vessel#getClass_()} is not null. If it is then expect a failure.
	 */
	@Test
	public void testVessel() {

		final Vessel v = initVessel();

		// expect it not to be null, and so succeed.
		testNullReferenceConstraint(true, v);
	}

	/**
	 * Test that {@link Vessel#getClass_()} is not null. If it is then expect a failure.
	 */
	@Test
	public void testVesselClassNull() {

		final Vessel v = initVessel();

		v.setVesselClass(null);
		// expect it to be null, so fail.
		testNullReferenceConstraint(false, v);
	}

	/**
	 * Generically test an EObject against the constraint NullReferenceConstraint.
	 * 
	 * @param expectSuccess
	 *            Whether the test is expected to succeed or fail.
	 * @param target
	 *            The EObject to validate.
	 */
	private void testNullReferenceConstraint(final boolean expectSuccess, final EObject target) {

		// This is the constraint we will be testing
		final NullReferenceConstraint constraint = new NullReferenceConstraint();

		// mock things
		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus resultStatus = mock(IConstraintStatus.class);

		// mock results of method calls
		when(validationContext.getTarget()).thenReturn(target);
		if (expectSuccess) {
			when(validationContext.createSuccessStatus()).thenReturn(resultStatus);
		} else {
			when(validationContext.createFailureStatus(Matchers.anyString())).thenReturn(resultStatus);
		}

		// run the thing
		constraint.validate(validationContext);

		// verify mocked methods are called
		verify(validationContext).getTarget();
		verify(validationContext, atLeast(0)).createSuccessStatus();
		verify(validationContext, atLeast(0)).createFailureStatus(Matchers.anyString());
		// verify that only the methods above are called.
		verifyNoMoreInteractions(validationContext);
	}

}
