/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation.tests;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.Matchers;

import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.fleet.validation.VesselClassSpeedConstraint;

/**
 * Info from case 172:
 * 
 * This constraint expects to be given a VesselClass (that's the one in scenario.fleet, not the other one) and checks that its minimum and maximum speed (getMinSpeed / getMaxSpeed methods) are
 * 
 * (a) sensible relative to one another (min <= max)
 * 
 * (b) the min and max speeds lie within the bounds of the laden and ballast fuel consumption curves. These curves are lists of speed/ consumption pairs provided by
 * VesselStateAttributes.getFuelConsumptionCurve(), where the VesselStateAttributes are found by VesselClass.getLadenAttributes() and .getBallastAttributes().
 * 
 * The bounds that the speed of the vessel class should lie between are the largest minimum and smallest maximum speeds of both the ballast and laden FCLs
 * 
 */
public class VesselClassSpeedConstraintTest {

	// these three are copied from VesselClassSpeedConstraint because they're private.
	private static final String MIN_ID = "com.mmxlabs.models.lng.fleet.validation.VesselClassSpeedConstraint.min_speed";
	private static final String MAX_ID = "com.mmxlabs.models.lng.fleet.validation.VesselClassSpeedConstraint.max_speed";
	private static final String ORDER_ID = "com.mmxlabs.models.lng.fleet.validation.VesselClassSpeedConstraint.speed_order";

	/**
	 * Test for success if the object for the laden voyage is null.
	 */
	@Test
	public void testVesselClassSpeedConstraintNullLaden() {

		final VesselStateAttributes laden = null;
		final VesselStateAttributes ballast = mock(VesselStateAttributes.class);

		testVesselClassSpeedConstraintSuccess(laden, ballast);
	}

	/**
	 * Test for success if the object for the ballast voyage is null.
	 */
	@Test
	public void testVesselClassSpeedConstraintNullBallast() {

		final VesselStateAttributes laden = mock(VesselStateAttributes.class);
		final VesselStateAttributes ballast = null;

		testVesselClassSpeedConstraintSuccess(laden, ballast);
	}

	/**
	 * Test for success if the objects for both the laden and ballast voyages are null.
	 */
	@Test
	public void testVesselClassSpeedConstraintNullBallastAndLaden() {

		final VesselStateAttributes ladenNull = null;
		final VesselStateAttributes ballastNull = null;

		testVesselClassSpeedConstraintSuccess(ladenNull, ballastNull);
	}

	/**
	 * Test for a failure if minimum speed of the vessel class is greater than the maximum speed.
	 */
	@Test
	public void testVesselClassSpeedConstraintMinGreaterThanMax() {

		// The max is less than the min
		final float maxSpeed = 1;
		final float minSpeed = 2;

		testVesselClassSpeedConstraintOrderFailure(minSpeed, maxSpeed);
	}

	/**
	 * Test for a success if the min and max speeds of a vessel class are the same.
	 */
	@Test
	public void testVesselClassSpeedConstraintMinEqualMax() {

		final float speed = 1;

		testVesselClassSpeedConstraintOrderSuccess(speed, speed);
	}

	/**
	 * Test the VesselClassSpeedConstraint given a context and vessel state attributes for the laden and ballast.
	 * 
	 * The laden and ballast should have already been mocked using the Mockery context given in the arguments.
	 * 
	 * This method expects a successful test.
	 * 
	 * @param context
	 *            The context used to mock the laden or ballast VesselStateAttributes.
	 * @param laden
	 * @param ballast
	 */
	private void testVesselClassSpeedConstraintSuccess(final VesselStateAttributes laden, final VesselStateAttributes ballast) {

		// mock a vessel class
		final VesselClass vesselClass = mock(VesselClass.class);
		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus resultStatus = mock(IConstraintStatus.class);

		final VesselClassSpeedConstraint constraint = new VesselClassSpeedConstraint();

		// what's the target?
		when(validationContext.getTarget()).thenReturn(vesselClass);

		when(vesselClass.getBallastAttributes()).thenReturn(ballast);
		when(vesselClass.getLadenAttributes()).thenReturn(laden);
		when(validationContext.createSuccessStatus()).thenReturn(resultStatus);
		when(validationContext.getTarget()).thenReturn(vesselClass);

		constraint.validate(validationContext);

		verify(vesselClass).getBallastAttributes();
		verify(vesselClass).getLadenAttributes();
		verify(validationContext).createSuccessStatus();
		verify(validationContext).getTarget();
		// verify that only the methods above are called.
		verifyNoMoreInteractions(vesselClass);
		verifyNoMoreInteractions(validationContext);
	}

	/**
	 * Test the VesselClassSpeedConstraint Order condition. This tests whether the min and max speed of a vessel class are sensible (i.e. max >= min). This method expects that this will fail, however.
	 * 
	 * @param minSpeed
	 * @param maxSpeed
	 */
	private void testVesselClassSpeedConstraintOrderFailure(final double minSpeed, final double maxSpeed) {

		// mock a vessel class
		final VesselClass vesselClass = mock(VesselClass.class);

		final IValidationContext validationContext = mock(IValidationContext.class);

		// make sure to give these two different names, else they are given the
		// same name (the class, vesselStateAttributes), and same names aren't
		// allowed.
		final VesselStateAttributes laden = mock(VesselStateAttributes.class, "ladenVesselStateAttributes");
		final VesselStateAttributes ballast = mock(VesselStateAttributes.class, "ballastVesselStateAttributes");
		final VesselClassSpeedConstraint constraint = new VesselClassSpeedConstraint();

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);

		// what's the target?
		when(validationContext.getTarget()).thenReturn(vesselClass);
		when(vesselClass.getBallastAttributes()).thenReturn(ballast);
		when(vesselClass.getLadenAttributes()).thenReturn(laden);
		when(validationContext.getCurrentConstraintId()).thenReturn(ORDER_ID);
		when(vesselClass.getName()).thenReturn("vc");
		// one call for testing, another call for producing the error.
		when(vesselClass.getMinSpeed()).thenReturn(minSpeed);
		// one call for testing, another call for producing the error.
		when(vesselClass.getMaxSpeed()).thenReturn(maxSpeed);
		when(validationContext.createFailureStatus(anyString(), anyFloat(), anyFloat())).thenReturn(failureStatus);

		constraint.validate(validationContext);

		// what's the target?
		verify(vesselClass).getBallastAttributes();
		verify(vesselClass).getLadenAttributes();
		verify(vesselClass).getName();
		verify(vesselClass, times(2)).getMinSpeed();
		verify(vesselClass, times(2)).getMaxSpeed();
		verify(validationContext).getTarget();
		verify(validationContext).getCurrentConstraintId();
		verify(validationContext).createFailureStatus(Matchers.anyString(), AdditionalMatchers.eq(minSpeed, 1.0), AdditionalMatchers.eq(maxSpeed, 1.0));
		// verify that only the methods above are called.
		verifyNoMoreInteractions(vesselClass);
		verifyNoMoreInteractions(validationContext);
	}

	/**
	 * Test the VesselClassSpeedConstraint Order condition. This tests whether the min and max speed of a vessel class are sensible (i.e. max >= min). This method expects that this test will succeed.
	 * 
	 * @param minSpeed
	 * @param maxSpeed
	 */
	private void testVesselClassSpeedConstraintOrderSuccess(final double minSpeed, final double maxSpeed) {

		// mock a vessel class
		final VesselClass vesselClass = mock(VesselClass.class);

		final IValidationContext validationContext = mock(IValidationContext.class);

		// make sure to give these two different names, else they are given the
		// same name (the class, vesselStateAttributes), and same names aren't
		// allowed.
		final VesselStateAttributes laden = mock(VesselStateAttributes.class, "ladenVesselStateAttributes");
		final VesselStateAttributes ballast = mock(VesselStateAttributes.class, "ballastVesselStateAttributes");
		final IConstraintStatus resultStatus = mock(IConstraintStatus.class);
		final VesselClassSpeedConstraint constraint = new VesselClassSpeedConstraint();

		// what's the target?
		when(validationContext.getTarget()).thenReturn(vesselClass);
		when(vesselClass.getBallastAttributes()).thenReturn(ballast);
		when(vesselClass.getLadenAttributes()).thenReturn(laden);
		when(validationContext.getCurrentConstraintId()).thenReturn(ORDER_ID);
		// one call for testing, another call for producing the error.
		when(vesselClass.getMinSpeed()).thenReturn(minSpeed);
		// one call for testing, another call for producing the error.
		when(vesselClass.getMaxSpeed()).thenReturn(maxSpeed);
		when(validationContext.createSuccessStatus()).thenReturn(resultStatus);

		constraint.validate(validationContext);

		verify(vesselClass).getBallastAttributes();
		verify(vesselClass).getLadenAttributes();
		verify(vesselClass).getMinSpeed();
		verify(vesselClass).getMaxSpeed();
		verify(validationContext).getTarget();
		verify(validationContext).getCurrentConstraintId();
		verify(validationContext).createSuccessStatus();
		// verify that only the methods above are called.
		verifyNoMoreInteractions(vesselClass);
		verifyNoMoreInteractions(validationContext);
	}

	/**
	 * Test that a test will succeed when given a blank ID.
	 */
	@Test
	public void testVesselClassSpeedConstraintWrongID() {

		// this isn't a valid ID, but it should still succeed.
		final String wrongID = new String();

		// the speeds won't get tested so their values aren't important.
		final float speed = 10;

		testVesselClassSpeedConstraintID(true, wrongID, speed, speed, speed, speed);
	}

	@Test
	public void testVesselClassSpeedConstraintMAX_ID() {
		testVesselClassSpeedConstraint(MAX_ID);
	}

	@Test
	public void testVesselClassSpeedConstraintMIN_ID() {
		testVesselClassSpeedConstraint(MIN_ID);
	}

	/**
	 * Test that the constraint succeeds for sensible input.
	 */
	private void testVesselClassSpeedConstraint(final String ID) {
		final float vcSpeed = 10;
		final float minFCLSpeed = 5;
		final float maxFCLSpeed = 15;

		testVesselClassSpeedConstraintID(true, ID, vcSpeed, vcSpeed, minFCLSpeed, maxFCLSpeed);
	}

	@Test
	public void testVesselClassSpeedConstraintIDFCLsMinGreaterThanVCMinMax_ID() {
		testVesselClassSpeedConstraintIDFCLsMinGreaterThanVCMin(MAX_ID);
	}

	@Test
	public void testVesselClassSpeedConstraintIDFCLsMinGreaterThanVCMinMin_ID() {
		testVesselClassSpeedConstraintIDFCLsMinGreaterThanVCMin(MIN_ID);
	}

	/**
	 * If the largest FCL minimum speed is greater than the miniumum vessel class speed then the constraint should fail.
	 */
	private void testVesselClassSpeedConstraintIDFCLsMinGreaterThanVCMin(final String ID) {

		// The max and min speeds for the vessel class.
		final float minSpeed = 10;
		final float maxSpeed = 11;
		// the max and min speeds allowed for the fuel consumption line.
		final float minFCLSpeed = 8;
		final float maxFCLSpeed = 9;

		// Test with the min. Should be the same result as MAX_ID.
		testVesselClassSpeedConstraintID(false, ID, minSpeed, maxSpeed, minFCLSpeed, maxFCLSpeed);
	}

	@Test
	public void testVesselClassSpeedConstraintIDFCLsMaxGreaterThanVCMaxMAX_ID() {
		testVesselClassSpeedConstraintIDFCLsMaxGreaterThanVCMax(MAX_ID);
	}

	@Test
	public void testVesselClassSpeedConstraintIDFCLsMaxGreaterThanVCMaxMIN_ID() {
		testVesselClassSpeedConstraintIDFCLsMaxGreaterThanVCMax(MIN_ID);
	}

	/**
	 * If the smallest FCL maximum speed is greater than the maximum vessel class speed then the constraint should fail.
	 */
	private void testVesselClassSpeedConstraintIDFCLsMaxGreaterThanVCMax(final String ID) {

		// The max and min speeds for the vessel class.
		final float minSpeed = 9;
		final float maxSpeed = 10;
		// the max and min speeds allowed for the fuel consumption line.
		final float minFCLSpeed = 11;
		final float maxFCLSpeed = 12;

		testVesselClassSpeedConstraintID(false, ID, minSpeed, maxSpeed, minFCLSpeed, maxFCLSpeed);
	}

	/**
	 * Runs a test on the VesselClassSpeedConstraint. Whether the outcome is a success or failure is based on the argument expectSuccess.
	 * 
	 * @param expectSuccess
	 *            Whether the constraint is expected to pass or fail.
	 * @param ID
	 *            Should either be MAX_ID or MIN_ID.
	 * @param minVCSpeed
	 *            The minimum speed of the vessel class.
	 * @param maxVCSpeed
	 *            The maximum speed of the vessel class.
	 * @param minFCLSpeed
	 *            The minimum speed of fuel consumption line for both laden and ballast.
	 * @param maxFCLSpeed
	 *            The maximum speed of fuel consumption line for both laden and ballast.
	 */
	private void testVesselClassSpeedConstraintID(final boolean expectSuccess, final String ID, final double minVCSpeed, final double maxVCSpeed, final double minFCLSpeed, final double maxFCLSpeed) {

		// mock a vessel class
		final VesselClass vesselClass = mock(VesselClass.class);

		final IValidationContext validationContext = mock(IValidationContext.class);

		// make sure to give these two different names, else they are given the
		// same name (the class, vesselStateAttributes), and same names aren't
		// allowed.
		final VesselStateAttributes laden = mock(VesselStateAttributes.class, "ladenVesselStateAttributes");
		final VesselStateAttributes ballast = mock(VesselStateAttributes.class, "ballastVesselStateAttributes");
		final VesselClassSpeedConstraint constraint = new VesselClassSpeedConstraint();

		// Can't mock up a list without having some elements, so mock some
		// elements and then add them to a created list.
		final FuelConsumption minFCL = mock(FuelConsumption.class, "minFCL");
		final FuelConsumption maxFCL = mock(FuelConsumption.class, "maxFCL");
		final BasicEList<FuelConsumption> list = new BasicEList<FuelConsumption>();
		list.add(minFCL);
		list.add(maxFCL);

		// mock a failure
		final IConstraintStatus resultStatus = mock(IConstraintStatus.class);

		// what's the target?
		when(validationContext.getTarget()).thenReturn(vesselClass);
		// Make sure the vessel class returns the ballast and laden attribs.
		when(vesselClass.getBallastAttributes()).thenReturn(ballast);
		when(vesselClass.getLadenAttributes()).thenReturn(laden);
		// The ID that we are testing for.
		when(validationContext.getCurrentConstraintId()).thenReturn(ID);
		// The vessel class has a min and max speed as specified by the method arguments.
		when(vesselClass.getMinSpeed()).thenReturn(minVCSpeed);
		when(vesselClass.getMaxSpeed()).thenReturn(maxVCSpeed);
		// Set the fuel consump. lines to return a speed as specified by the method arguments.
		when(minFCL.getSpeed()).thenReturn(minFCLSpeed);
		when(maxFCL.getSpeed()).thenReturn(maxFCLSpeed);
		// Return the same list of fuel consumption values for the laden and ballast
		when(laden.getFuelConsumption()).thenReturn(list);
		when(ballast.getFuelConsumption()).thenReturn(list);

		// find out the speed which the test is failing about, so the failure status can be called using the correct speed.
		double failureSpeed = 0;
		if (ID.equals(MIN_ID)) {
			failureSpeed = minVCSpeed;
		} else if (ID.equals(MAX_ID)) {
			failureSpeed = maxVCSpeed;
		}

		if (expectSuccess) {
			when(validationContext.createSuccessStatus()).thenReturn(resultStatus);
		} else {
			when(validationContext.createFailureStatus(Matchers.<Object>anyVararg())).thenReturn(resultStatus);
		}

		constraint.validate(validationContext);

		// what's the target?
		verify(validationContext).getTarget();
		// Make sure the vessel class returns the ballast and laden attribs.
		verify(vesselClass).getBallastAttributes();
		verify(vesselClass).getLadenAttributes();
		// The ID that we are testing for.
		verify(validationContext, atLeast(2)).getCurrentConstraintId();
		// The vessel class has a min and max speed as specified by the method arguments.
		verify(vesselClass, atLeast(0)).getMinSpeed();
		verify(vesselClass, atLeast(0)).getMaxSpeed();
		// Set the fuel consump. lines to return a speed as specified by the method arguments.
		verify(minFCL, atLeast(0)).getSpeed();
		verify(maxFCL, atLeast(0)).getSpeed();
		// Return the same list of fuel consumption values for the laden and ballast
		verify(laden, atLeast(0)).getFuelConsumption();
		verify(ballast, atLeast(0)).getFuelConsumption();

		if (expectSuccess) {
			verify(validationContext).createSuccessStatus();
		} else {
			verify(validationContext).createFailureStatus(Matchers.<Object>anyVararg());
		}
	}

}
