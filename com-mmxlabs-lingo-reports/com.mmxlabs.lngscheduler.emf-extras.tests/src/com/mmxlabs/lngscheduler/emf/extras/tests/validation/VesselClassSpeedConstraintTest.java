package com.mmxlabs.lngscheduler.emf.extras.tests.validation;

import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import scenario.fleet.VesselClass;
import scenario.fleet.VesselStateAttributes;

import com.mmxlabs.lngscheduler.emf.extras.validation.VesselClassSpeedConstraint;

/**
 * Info from case 172:
 * 
 * This constraint expects to be given a VesselClass (that's the one in
 * scenario.fleet, not the other one) and checks that its minimum and maximum
 * speed (getMinSpeed / getMaxSpeed methods) are
 * 
 * (a) sensible relative to one another (min <= max)
 * 
 * (b) the min and max speeds lie within the bounds of the laden and ballast
 * fuel consumption curves. These curves are lists of speed/ consumption pairs
 * provided by VesselStateAttributes.getFuelConsumptionCurve(), where the
 * VesselStateAttributes are found by VesselClass.getLadenAttributes() and
 * .getBallastAttributes().
 */
public class VesselClassSpeedConstraintTest {

	// these three are copied from VesselClassSpeedConstraint because they're
	// not private.
	private static final String MIN_ID = "com.mmxlabs.lngscheduler.emf-extras.vessel_class_min_speed";
	private static final String MAX_ID = "com.mmxlabs.lngscheduler.emf-extras.vessel_class_max_speed";
	private static final String ORDER_ID = "com.mmxlabs.lngscheduler.emf-extras.vessel_class_speed_order";

	@Test
	public void testVesselClassSpeedConstraintNullLaden() {

		final Mockery context = new Mockery();
		final VesselStateAttributes laden = null;
		final VesselStateAttributes ballast = context
				.mock(VesselStateAttributes.class);

		testVesselClassSpeedConstraintSuccess(context, laden, ballast);
	}

	@Test
	public void testVesselClassSpeedConstraintNullBallast() {

		final Mockery context = new Mockery();
		final VesselStateAttributes laden = context
				.mock(VesselStateAttributes.class);
		final VesselStateAttributes ballast = null;

		testVesselClassSpeedConstraintSuccess(context, laden, ballast);
	}

	@Test
	public void testVesselClassSpeedConstraintNullBallastAndLaden() {

		final Mockery context = new Mockery();
		final VesselStateAttributes laden = null;
		final VesselStateAttributes ballast = null;

		testVesselClassSpeedConstraintSuccess(context, laden, ballast);
	}

	@Test
	public void testVesselClassSpeedConstraintMinGreaterThanMax() {

		final float maxSpeed = 1;
		final float minSpeed = 2;

		testVesselClassSpeedConstraintOrderFailure(minSpeed, maxSpeed);
	}

	@Test
	public void testVesselClassSpeedConstraintMinEqualMax() {

		final float speed = 1;

		testVesselClassSpeedConstraintOrderFailure(speed, speed);
	}

	private void testVesselClassSpeedConstraintSuccess(final Mockery context,
			final VesselStateAttributes laden,
			final VesselStateAttributes ballast) {

		// mock a vessel class
		final VesselClass vesselClass = context.mock(VesselClass.class);

		final IValidationContext validationContext = context
				.mock(IValidationContext.class);

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

	private void testVesselClassSpeedConstraintOrderFailure(
			final float minSpeed, final float maxSpeed) {

		// Create a mockery to mock up all the objects involved in a test
		final Mockery context = new Mockery();

		// mock a vessel class
		final VesselClass vesselClass = context.mock(VesselClass.class);

		final IValidationContext validationContext = context
				.mock(IValidationContext.class);

		// make sure to give these two different names, else they are given the
		// same name (vesselStateAttributes), and same names aren't allowed.
		final VesselStateAttributes laden = context
				.mock(VesselStateAttributes.class, "ladenVesselStateAttributes");
		final VesselStateAttributes ballast = context
				.mock(VesselStateAttributes.class, "ballastVesselStateAttributes");
		final VesselClassSpeedConstraint constraint = new VesselClassSpeedConstraint();
		
		final IConstraintStatus failureStatus = context
		.mock(IConstraintStatus.class);

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
				exactly(2).of(vesselClass).getMinSpeed();
				will(returnValue(minSpeed));
				exactly(2).of(vesselClass).getMaxSpeed();
				will(returnValue(maxSpeed));

				exactly(1).of(validationContext).createFailureStatus("vc", minSpeed, maxSpeed);
				will(returnValue(failureStatus));
			}
		});

		constraint.validate(validationContext);

		context.assertIsSatisfied();
	}

}
