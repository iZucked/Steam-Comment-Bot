/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation.tests;

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

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.validation.NullReferenceConstraint;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
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
 * Slot --- port
 * <p>
 * 
 * Cargo --- loadSlot, dischargeSlot
 * <p>
 * 
 */
public class NullReferenceConstraintTest {

	private Slot initSlot() {
		final Slot slot = CargoFactory.eINSTANCE.createLoadSlot();
		// port is initially null, so set it.
		slot.setPort(PortFactory.eINSTANCE.createPort());
		slot.setContract(CommercialFactory.eINSTANCE.createContract());
		return slot;
	}

	private Cargo initCargo() {
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		return cargo;
	}

	/**
	 * Test the {@link Slot#getPort()} of a Slot is not null, and expect a failure if it is.
	 */
	@Test
	public void testSlot() {
		final Slot slot = initSlot();

		testNullReferenceConstraint(true, slot);
	}

	/**
	 * Test the {@link Slot#getPort()} of a Slot is not null, and expect a failure if it is.
	 */
	@Test
	public void testSlotPortNull() {
		final Slot slot = initSlot();
		slot.setPort(null);
		// now the test should fail.
		testNullReferenceConstraint(false, slot);
	}

	/**
	 * Test {@link Cargo#getLoadSlot()} and {@link Cargo#getDischargeSlot()} are not null. If either are null expect a failure.
	 */
	@Test
	public void testCargo() {
		final Cargo cargo = initCargo();

		// Test for success, neither are null.
		testNullReferenceConstraint(true, cargo);
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

	private CharterOutEvent initCharterOut() {
		final CharterOutEvent co = CargoFactory.eINSTANCE.createCharterOutEvent();
	
		// start and end ports are both checked and are initially null, so set them.
		co.setRelocateTo(PortFactory.eINSTANCE.createPort());
		co.setPort(PortFactory.eINSTANCE.createPort());
		return co;
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

	private VesselEvent initVesselEvent() {
		final VesselEvent vesselEvent = CargoFactory.eINSTANCE.createDryDockEvent();
		vesselEvent.setPort(PortFactory.eINSTANCE.createPort());
		return vesselEvent;
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
}
