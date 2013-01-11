/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
import org.junit.Test;
import org.mockito.Matchers;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.Slot;
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

		// these are initially null, so create and set instances
		cargo.setLoadSlot(CargoFactory.eINSTANCE.createLoadSlot());
		cargo.setDischargeSlot(CargoFactory.eINSTANCE.createDischargeSlot());
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
	 * Test {@link Cargo#getLoadSlot()} and {@link Cargo#getDischargeSlot()} are not null. If either are null expect a failure.
	 */
	@Test
	public void testCargoLoadSlotNull() {
		final Cargo cargo = initCargo();

		// set load slot to null and expect a failure
		cargo.setLoadSlot(null);
		testNullReferenceConstraint(false, cargo);
	}

	/**
	 * Test {@link Cargo#getLoadSlot()} and {@link Cargo#getDischargeSlot()} are not null. If either are null expect a failure.
	 */
	@Test
	public void testCargoDischargeSlotNull() {
		final Cargo cargo = initCargo();

		// set discharge slot to null and expect a failure
		cargo.setDischargeSlot(null);
		testNullReferenceConstraint(false, cargo);
	}

	/**
	 * Test {@link Cargo#getLoadSlot()} and {@link Cargo#getDischargeSlot()} are not null. If either are null expect a failure.
	 */
	@Test
	public void testCargoDischargeAllNull() {
		final Cargo cargo = initCargo();

		// set discharge slot to null and expect a failure
		cargo.setDischargeSlot(null);
		cargo.setLoadSlot(null);
		testNullReferenceConstraint(false, cargo);
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
