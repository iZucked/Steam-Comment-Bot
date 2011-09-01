package com.mmxlabs.lngscheduler.emf.extras.tests.validation;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselStateAttributes;

import com.mmxlabs.lngscheduler.emf.extras.validation.VesselClassSpeedConstraint;

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
	private static final String MIN_ID = "com.mmxlabs.lngscheduler.emf-extras.vessel_class_min_speed";
	private static final String MAX_ID = "com.mmxlabs.lngscheduler.emf-extras.vessel_class_max_speed";
	private static final String ORDER_ID = "com.mmxlabs.lngscheduler.emf-extras.vessel_class_speed_order";

	/**
	 * Test for success if the object for the laden voyage is null.
	 */
	@Test
	public void testVesselClassSpeedConstraintNullLaden() {

		final Mockery context = new Mockery();
		final VesselStateAttributes laden = null;
		final VesselStateAttributes ballast = context.mock(VesselStateAttributes.class);

		testVesselClassSpeedConstraintSuccess(context, laden, ballast);
	}

	/**
	 * Test for success if the object for the ballast voyage is null.
	 */
	@Test
	public void testVesselClassSpeedConstraintNullBallast() {

		final Mockery context = new Mockery();
		final VesselStateAttributes laden = context.mock(VesselStateAttributes.class);
		final VesselStateAttributes ballast = null;

		testVesselClassSpeedConstraintSuccess(context, laden, ballast);
	}

	/**
	 * Test for success if the objects for both the laden and ballast voyages are null.
	 */
	@Test
	public void testVesselClassSpeedConstraintNullBallastAndLaden() {

		final Mockery context = new Mockery();
		final VesselStateAttributes ladenNull = null;
		final VesselStateAttributes ballastNull = null;

		testVesselClassSpeedConstraintSuccess(context, ladenNull, ballastNull);
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
	private void testVesselClassSpeedConstraintSuccess(final Mockery context, final VesselStateAttributes laden, final VesselStateAttributes ballast) {

		// mock a vessel class
		final VesselClass vesselClass = context.mock(VesselClass.class);

		final IValidationContext validationContext = context.mock(IValidationContext.class);

		final VesselClassSpeedConstraint constraint = new VesselClassSpeedConstraint();

		context.checking(new Expectations() {
			{
				// what's the target?
				exactly(1).of(validationContext).getTarget();
				will(returnValue(vesselClass));

				exactly(1).of(vesselClass).getBallastAttributes();
				will(returnValue(ballast));
				exactly(1).of(vesselClass).getLadenAttributes();
				will(returnValue(laden));

				exactly(1).of(validationContext).createSuccessStatus();
			}
		});

		constraint.validate(validationContext);

		context.assertIsSatisfied();
	}

	/**
	 * Test the VesselClassSpeedConstraint Order condition. This tests whether the min and max speed of a vessel class are sensible (i.e. max >= min). This method expects that this will fail, however.
	 * 
	 * @param minSpeed
	 * @param maxSpeed
	 */
	private void testVesselClassSpeedConstraintOrderFailure(final float minSpeed, final float maxSpeed) {

		// Create a mockery to mock up all the objects involved in a test
		final Mockery context = new Mockery();

		// mock a vessel class
		final VesselClass vesselClass = context.mock(VesselClass.class);

		final IValidationContext validationContext = context.mock(IValidationContext.class);

		// make sure to give these two different names, else they are given the
		// same name (the class, vesselStateAttributes), and same names aren't
		// allowed.
		final VesselStateAttributes laden = context.mock(VesselStateAttributes.class, "ladenVesselStateAttributes");
		final VesselStateAttributes ballast = context.mock(VesselStateAttributes.class, "ballastVesselStateAttributes");
		final VesselClassSpeedConstraint constraint = new VesselClassSpeedConstraint();

		final IConstraintStatus failureStatus = context.mock(IConstraintStatus.class);

		context.checking(new Expectations() {
			{
				// what's the target?
				exactly(1).of(validationContext).getTarget();
				will(returnValue(vesselClass));

				exactly(1).of(vesselClass).getBallastAttributes();
				will(returnValue(ballast));
				exactly(1).of(vesselClass).getLadenAttributes();
				will(returnValue(laden));

				exactly(1).of(validationContext).getCurrentConstraintId();
				will(returnValue(ORDER_ID));

				exactly(1).of(vesselClass).getName();
				will(returnValue("vc"));

				// one call for testing, another call for producing the error.
				exactly(2).of(vesselClass).getMinSpeed();
				will(returnValue(minSpeed));
				// one call for testing, another call for producing the error.
				exactly(2).of(vesselClass).getMaxSpeed();
				will(returnValue(maxSpeed));

				exactly(1).of(validationContext).createFailureStatus("vc", minSpeed, maxSpeed);
				will(returnValue(failureStatus));
			}
		});

		constraint.validate(validationContext);

		context.assertIsSatisfied();
	}

	/**
	 * Test the VesselClassSpeedConstraint Order condition. This tests whether the min and max speed of a vessel class are sensible (i.e. max >= min). This method expects that this test will succeed.
	 * 
	 * @param minSpeed
	 * @param maxSpeed
	 */
	private void testVesselClassSpeedConstraintOrderSuccess(final float minSpeed, final float maxSpeed) {

		// Create a mockery to mock up all the objects involved in a test
		final Mockery context = new Mockery();

		// mock a vessel class
		final VesselClass vesselClass = context.mock(VesselClass.class);

		final IValidationContext validationContext = context.mock(IValidationContext.class);

		// make sure to give these two different names, else they are given the
		// same name (the class, vesselStateAttributes), and same names aren't
		// allowed.
		final VesselStateAttributes laden = context.mock(VesselStateAttributes.class, "ladenVesselStateAttributes");
		final VesselStateAttributes ballast = context.mock(VesselStateAttributes.class, "ballastVesselStateAttributes");
		final VesselClassSpeedConstraint constraint = new VesselClassSpeedConstraint();

		context.checking(new Expectations() {
			{
				// what's the target?
				exactly(1).of(validationContext).getTarget();
				will(returnValue(vesselClass));

				exactly(1).of(vesselClass).getBallastAttributes();
				will(returnValue(ballast));
				exactly(1).of(vesselClass).getLadenAttributes();
				will(returnValue(laden));

				exactly(1).of(validationContext).getCurrentConstraintId();
				will(returnValue(ORDER_ID));

				// one call for testing, another call for producing the error.
				exactly(1).of(vesselClass).getMinSpeed();
				will(returnValue(minSpeed));
				// one call for testing, another call for producing the error.
				exactly(1).of(vesselClass).getMaxSpeed();
				will(returnValue(maxSpeed));

				exactly(1).of(validationContext).createSuccessStatus();
			}
		});

		constraint.validate(validationContext);

		context.assertIsSatisfied();
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

		testVesselClassSpeedConstraintIDSuccess(wrongID, speed, speed, speed, speed);
	}

	/**
	 * Test that the constraint succeeds for sensible input.
	 */
	@Test
	public void testVesselClassSpeedConstraintMax() {

		// Depending on whether MAX_ID or MIN_ID is sent only the min or max vessel class speed will be tested, so they might as well be the same.
		final float vcSpeed = 10;
		final float minFCLSpeed = 5;
		final float maxFCLSpeed = 15;

		// Test the case for both the MAX_ID and MIN_ID cases.
		testVesselClassSpeedConstraintIDSuccess(MAX_ID, vcSpeed, vcSpeed, minFCLSpeed, maxFCLSpeed);
		testVesselClassSpeedConstraintIDSuccess(MIN_ID, vcSpeed, vcSpeed, minFCLSpeed, maxFCLSpeed);
	}

	/**
	 * If the largest FCL minimum speed is greater than the miniumum vessel class speed then the constraint should fail.
	 */
	@Test
	public void testVesselClassSpeedConstraintIDFCLsMinGreaterThanVCMin() {

		// The max and min speeds for the vessel class.
		final float minSpeed = 10;
		final float maxSpeed = 11;
		// the max and min speeds allowed for the fuel consumption line.
		final float minFCLSpeed = 8;
		final float maxFCLSpeed = 9;

		// Test once for the max and for the min. Shouldn't make a difference.
		testVesselClassSpeedConstraintIDFailure(MAX_ID, minSpeed, maxSpeed, minFCLSpeed, maxFCLSpeed);
		testVesselClassSpeedConstraintIDFailure(MIN_ID, minSpeed, maxSpeed, minFCLSpeed, maxFCLSpeed);
	}


	/**
	 * If the smallest FCL maximum speed is greater than the maximum vessel class speed then the constraint should fail.
	 */
	@Test
	public void testVesselClassSpeedConstraintIDFCLsMaxGreaterThanVCMax() {

		// The max and min speeds for the vessel class.
		final float minSpeed = 9;
		final float maxSpeed = 10;
		// the max and min speeds allowed for the fuel consumption line.
		final float minFCLSpeed = 11;
		final float maxFCLSpeed = 12;

		// Test once for the max and for the min. Shouldn't make a difference.
		testVesselClassSpeedConstraintIDFailure(MAX_ID, minSpeed, maxSpeed, minFCLSpeed, maxFCLSpeed);
		testVesselClassSpeedConstraintIDFailure(MIN_ID, minSpeed, maxSpeed, minFCLSpeed, maxFCLSpeed);
	}

	/**
	 * Runs a test on the VesselClassSpeedConstraint and expects a success.
	 *  
	 * @param ID Should either be MAX_ID or MIN_ID.
	 * @param minVCSpeed The minimum speed of the vessel class.
	 * @param maxVCSpeed The maximum speed of the vessel class.
	 * @param minFCLSpeed The minimum speed of fuel consumption line for both laden and ballast.
	 * @param maxFCLSpeed The maximum speed of fuel consumption line for both laden and ballast.
	 */
	private void testVesselClassSpeedConstraintIDSuccess(final String ID, final float minVCSpeed, final float maxVCSpeed, final float minFCLSpeed, final float maxFCLSpeed) {

		// Create a mockery to mock up all the objects involved in a test
		final Mockery context = new Mockery();

		// mock a vessel class
		final VesselClass vesselClass = context.mock(VesselClass.class);

		final IValidationContext validationContext = context.mock(IValidationContext.class);

		// make sure to give these two different names, else they are given the same name (the class, vesselStateAttributes), and same names aren't allowed.
		final VesselStateAttributes laden = context.mock(VesselStateAttributes.class, "ladenVesselStateAttributes");
		final VesselStateAttributes ballast = context.mock(VesselStateAttributes.class, "ballastVesselStateAttributes");
		final VesselClassSpeedConstraint constraint = new VesselClassSpeedConstraint();

		// Can't mock up a list without having some elements, so mock some elements and then add them to a created list.
		final FuelConsumptionLine minFCL = context.mock(FuelConsumptionLine.class, "minFCL");
		final FuelConsumptionLine maxFCL = context.mock(FuelConsumptionLine.class, "maxFCL");
		final BasicEList<FuelConsumptionLine> list = new BasicEList<FuelConsumptionLine>();
		list.add(minFCL);
		list.add(maxFCL);

		context.checking(new Expectations() {
			{
				// what's the target?
				exactly(1).of(validationContext).getTarget();
				will(returnValue(vesselClass));

				// Make sure the vessel class returns the ballast and laden attribs.
				exactly(1).of(vesselClass).getBallastAttributes();
				will(returnValue(ballast));
				exactly(1).of(vesselClass).getLadenAttributes();
				will(returnValue(laden));

				// The ID that we are testing for.
				atLeast(1).of(validationContext).getCurrentConstraintId();
				will(returnValue(ID));

				// The vessel class has a min and max speed as specified by the method arguments.
				atMost(1).of(vesselClass).getMinSpeed();
				will(returnValue(minVCSpeed));
				atMost(1).of(vesselClass).getMaxSpeed();
				will(returnValue(maxVCSpeed));

				// Set the fuel consump. lines to return a speed as specified by the method arguments.
				atLeast(1).of(minFCL).getSpeed();
				will(returnValue(minFCLSpeed));
				atLeast(1).of(maxFCL).getSpeed();
				will(returnValue(maxFCLSpeed));

				// Return the same list of fuel consumption values for the laden and ballast
				atLeast(1).of(laden).getFuelConsumptionCurve();
				will(returnValue(list));
				atLeast(1).of(ballast).getFuelConsumptionCurve();
				will(returnValue(list));

				// expect a success
				exactly(1).of(validationContext).createSuccessStatus();
			}
		});

		constraint.validate(validationContext);
		context.assertIsSatisfied();
	}


	/**
	 * Runs a test on the VesselClassSpeedConstraint and expects a failure.
	 *  
	 * @param ID Should either be MAX_ID or MIN_ID.
	 * @param minVCSpeed The minimum speed of the vessel class.
	 * @param maxVCSpeed The maximum speed of the vessel class.
	 * @param minFCLSpeed The minimum speed of fuel consumption line for both laden and ballast.
	 * @param maxFCLSpeed The maximum speed of fuel consumption line for both laden and ballast.
	 */
	private void testVesselClassSpeedConstraintIDFailure(final String ID, final float minVCSpeed, final float maxVCSpeed, final float minFCLSpeed, final float maxFCLSpeed) {

		// Create a mockery to mock up all the objects involved in a test
		final Mockery context = new Mockery();

		// mock a vessel class
		final VesselClass vesselClass = context.mock(VesselClass.class);

		final IValidationContext validationContext = context.mock(IValidationContext.class);

		// make sure to give these two different names, else they are given the
		// same name (the class, vesselStateAttributes), and same names aren't
		// allowed.
		final VesselStateAttributes laden = context.mock(VesselStateAttributes.class, "ladenVesselStateAttributes");
		final VesselStateAttributes ballast = context.mock(VesselStateAttributes.class, "ballastVesselStateAttributes");
		final VesselClassSpeedConstraint constraint = new VesselClassSpeedConstraint();

		// Can't mock up a list without having some elements, so mock some
		// elements and then add them to a created list.
		final FuelConsumptionLine minFCL = context.mock(FuelConsumptionLine.class, "minFCL");
		final FuelConsumptionLine maxFCL = context.mock(FuelConsumptionLine.class, "maxFCL");
		final BasicEList<FuelConsumptionLine> list = new BasicEList<FuelConsumptionLine>();
		list.add(minFCL);
		list.add(maxFCL);

		// mock a failure
		final IConstraintStatus failureStatus = context.mock(IConstraintStatus.class);

		context.checking(new Expectations() {
			{
				// what's the target?
				exactly(1).of(validationContext).getTarget();
				will(returnValue(vesselClass));

				// Make sure the vessel class returns the ballast and laden attribs.
				exactly(1).of(vesselClass).getBallastAttributes();
				will(returnValue(ballast));
				exactly(1).of(vesselClass).getLadenAttributes();
				will(returnValue(laden));

				// The ID that we are testing for.
				atLeast(1).of(validationContext).getCurrentConstraintId();
				will(returnValue(ID));

				// The vessel class has a min and max speed as specified by the method arguments.
				atMost(1).of(vesselClass).getMinSpeed();
				will(returnValue(minVCSpeed));
				atMost(1).of(vesselClass).getMaxSpeed();
				will(returnValue(maxVCSpeed));

				// Set the fuel consump. lines to return a speed as specified by the method arguments.
				atLeast(1).of(minFCL).getSpeed();
				will(returnValue(minFCLSpeed));
				atLeast(1).of(maxFCL).getSpeed();
				will(returnValue(maxFCLSpeed));

				// Return the same list of fuel consumption values for the laden and ballast
				atLeast(1).of(laden).getFuelConsumptionCurve();
				will(returnValue(list));
				atLeast(1).of(ballast).getFuelConsumptionCurve();
				will(returnValue(list));

				// find out the speed which the test is failing about, so the failure status can be called using the correct speed.
				float failureSpeed = 0;
				if (ID.equals(MIN_ID))
					failureSpeed = minVCSpeed;
				else if (ID.equals(MAX_ID))
					failureSpeed = maxVCSpeed;

				// expect a failure
				exactly(1).of(validationContext).createFailureStatus(failureSpeed, minFCLSpeed, maxFCLSpeed);
				will(returnValue(failureStatus));
			}
		});

		constraint.validate(validationContext);
		context.assertIsSatisfied();
	}

}
