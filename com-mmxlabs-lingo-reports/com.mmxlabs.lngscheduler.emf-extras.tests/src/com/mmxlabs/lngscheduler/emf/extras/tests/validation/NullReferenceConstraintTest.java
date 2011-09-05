/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.validation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Test;

import scenario.cargo.Cargo;
import scenario.cargo.CargoFactory;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
import scenario.fleet.CharterOut;
import scenario.fleet.FleetFactory;
import scenario.fleet.PortAndTime;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselEvent;
import scenario.fleet.VesselFuel;
import scenario.fleet.VesselStateAttributes;
import scenario.port.PortFactory;

import com.mmxlabs.lngscheduler.emf.extras.validation.NullReferenceConstraint;

/**
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

	@Test
	public void testVesselEvent() {
		// CharterOut is an implementation of VesselEvent
		final VesselEvent ve = FleetFactory.eINSTANCE.createCharterOut();

		// start port is initially null, so set it.
		ve.setStartPort(PortFactory.eINSTANCE.createPort());
		
		Assert.assertNotNull(ve.getStartPort());
		
		testNullReferenceConstraint(true, ve);

		// now the test should fail.
		ve.setStartPort(null);
		testNullReferenceConstraint(false, ve);
	}

	@Test
	public void testCharterOut() {
		final CharterOut co = FleetFactory.eINSTANCE.createCharterOut();

		// start and end ports are both checked and are initially null, so set them.
		co.setEndPort(PortFactory.eINSTANCE.createPort());
		co.setStartPort(PortFactory.eINSTANCE.createPort());
		
		// only test end port. Start port is tested by testVesselEvent()
		Assert.assertNotNull(co.getEndPort());
		testNullReferenceConstraint(true, co);

		// now the test should fail.
		co.setEndPort(null);
		testNullReferenceConstraint(false, co);
	}

	@Test
	public void testSlot() {
		final Slot slot = CargoFactory.eINSTANCE.createSlot();

		// port is initially null, so set it.
		slot.setPort(PortFactory.eINSTANCE.createPort());
		Assert.assertNotNull(slot.getPort());

		testNullReferenceConstraint(true, slot);

		// now the test should fail.
		slot.setPort(null);
		testNullReferenceConstraint(false, slot);
	}

	@Test
	public void testCargo() {
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		// these are initially null, so create and set instances
		final LoadSlot load = CargoFactory.eINSTANCE.createLoadSlot();
		final Slot dischange = CargoFactory.eINSTANCE.createSlot();
		cargo.setLoadSlot(load);
		cargo.setDischargeSlot(dischange);

		Assert.assertNotNull(cargo.getLoadSlot());
		Assert.assertNotNull(cargo.getDischargeSlot());

		testNullReferenceConstraint(true, cargo);

		cargo.setLoadSlot(null);
		testNullReferenceConstraint(false, cargo);
		cargo.setLoadSlot(load);

		cargo.setDischargeSlot(null);
		testNullReferenceConstraint(false, cargo);
	}

	@Test
	public void testVesselClass() {

		final VesselClass vc = FleetFactory.eINSTANCE.createVesselClass();

		// create and set values for fields that start out as null
		VesselFuel bf = FleetFactory.eINSTANCE.createVesselFuel();
		VesselStateAttributes ballast = FleetFactory.eINSTANCE.createVesselStateAttributes();
		VesselStateAttributes laden = FleetFactory.eINSTANCE.createVesselStateAttributes();
		vc.setBaseFuel(bf);
		vc.setBallastAttributes(ballast);
		vc.setLadenAttributes(laden);

		Assert.assertNotNull(vc.getBaseFuel());
		Assert.assertNotNull(vc.getBallastAttributes());
		Assert.assertNotNull(vc.getLadenAttributes());

		// expect it not to be null, and so succeed.
		testNullReferenceConstraint(true, vc);

		// test the base fuel being null
		vc.setBaseFuel(null);
		testNullReferenceConstraint(false, vc);
		// and reset for the next test
		vc.setBaseFuel(bf);

		// test the ballast attributes being null
		vc.setBallastAttributes(null);
		testNullReferenceConstraint(false, vc);
		// and reset for the next test
		vc.setBallastAttributes(ballast);

		// test the laden attributes being null
		vc.setLadenAttributes(null);
		testNullReferenceConstraint(false, vc);
	}

	@Test
	public void testVessel() {

		final Vessel v = FleetFactory.eINSTANCE.createVessel();

		// vessel class is null by default, so add a value.
		v.setClass(FleetFactory.eINSTANCE.createVesselClass());

		Assert.assertNotNull(v.getClass_());

		// expect it not to be null, and so succeed.
		testNullReferenceConstraint(true, v);

		v.setClass(null);
		Assert.assertNull(v.getClass_());

		// expect it to be null, so fail.
		testNullReferenceConstraint(false, v);
	}

	@Test
	public void testPortAndTime() {

		final PortAndTime pat = FleetFactory.eINSTANCE.createPortAndTime();

		testNullReferenceConstraint(true, pat);

		pat.setPort(null);

		testNullReferenceConstraint(false, pat);
	}

	private void testNullReferenceConstraint(final boolean expectSuccess, final EObject target) {

		// Create a mockery to mock up all the objects involved in a test
		final Mockery context = new Mockery();
		// This is the constraint we will be testing
		final NullReferenceConstraint constraint = new NullReferenceConstraint();

		final IValidationContext validationContext = context.mock(IValidationContext.class);

		final IConstraintStatus failureStatus = context.mock(IConstraintStatus.class);

		context.checking(new Expectations() {
			{
				exactly(1).of(validationContext).getTarget();
				will(returnValue(target));

				if (expectSuccess)
					exactly(1).of(validationContext).createSuccessStatus();
				else {
					exactly(1).of(validationContext).createFailureStatus();
					will(returnValue(failureStatus));
				}
			}
		});

		constraint.validate(validationContext);

		context.assertIsSatisfied();
	}

}
