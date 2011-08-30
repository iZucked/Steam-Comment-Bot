/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.validation;

import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import scenario.fleet.VesselClass;

import com.mmxlabs.lngscheduler.emf.extras.validation.ShipFillPercentageConstraint;

/**
 * Test the {@link ShipFillPercentageConstraintTest}. You will need the
 * org.jmock eclipse plugin from our codebase repository to run this, as jmock
 * doesn't currently provide an eclipse plugin.
 * 
 * TODO test behaviour of test when passed a VesselClass. It's a bit less tidy.
 * 
 * ----
 * 
 * Info from case 170:
 * 
 * Write a test for the ShipFillPercentageConstraint.
 * 
 * The ship fill percentage is the percentage of a vessel class' tanks that can
 * actually be used. In the model, percentages are held as floating point
 * numbers from 0 to 1 (i.e. proportions). The ShipFillPercentageConstraint is
 * supposed to check that this value is (a) valid and (b) reasonable.
 * 
 * A valid ship fill percentage is strictly greater than zero and less than or
 * equal to 100%
 * 
 * A reasonable ship fill percentage is more than 80% (0.8).
 * 
 * The constraint expects its IValidationContext to provide a VesselClass as the
 * target, and one of
 * "com.mmxlabs.lngscheduler.emf-extras.constraints.ship_fill_validity" or
 * "com.mmxlabs.lngscheduler.emf-extras.constraints.ship_fill_sanity" from
 * IValidationContext.getCurrentConstraintID(), indicating whether it's checking
 * validity or reasonableness.
 * 
 */
public class ShipFillPercentageConstraintTest {

	private static final double sensiblefill = 0.8;

	@Test
	public void testValidityConstraintSensibleFill() {

		final double fill = 0.9d;
		testValidityConstraintSuccess(fill,
				ShipFillPercentageConstraint.SANITY_ID);
	}

	@Test
	public void testValidityConstraintUnderSensibleFill() {

		final double undersensiblefill = 0.7d;
		testValidityConstraintFailure(undersensiblefill,
				ShipFillPercentageConstraint.SANITY_ID);
	}

	@Test
	public void testValidityConstraintNegativeFill() {

		final double negativefill = -1;
		testValidityConstraintFailure(negativefill,
				ShipFillPercentageConstraint.VALIDITY_ID);
	}

	@Test
	public void testValidityConstraintOverfullFill() {

		final double overfill = 1.1d;
		testValidityConstraintFailure(overfill,
				ShipFillPercentageConstraint.VALIDITY_ID);
	}

	public void testValidityConstraintFailure(final double fill, final String id) {

		// Create a mockery to mock up all the objects involved in a test
		final Mockery context = new Mockery();
		// This is the constraint we will be testing
		final ShipFillPercentageConstraint constraint = new ShipFillPercentageConstraint();

		// mock a vessel class
		final VesselClass vesselClass = context.mock(VesselClass.class);

		final IValidationContext validationContext = context
				.mock(IValidationContext.class);

		final IConstraintStatus failureStatus = context
				.mock(IConstraintStatus.class);

		context.checking(new Expectations() {
			{
				atLeast(1).of(vesselClass).getName();
				will(returnValue("vc"));

				atLeast(1).of(vesselClass).getFillCapacity();
				will(returnValue(fill));

				// what's the target?
				exactly(1).of(validationContext).getTarget();
				will(returnValue(vesselClass));

				atLeast(1).of(validationContext).getCurrentConstraintId();
				will(returnValue(id));

				// and it will want a failure status thing
				atLeast(1).of(validationContext).createFailureStatus("vc",
						fill * 100, sensiblefill * 100);
				will(returnValue(failureStatus));
			}
		});

		constraint.validate(validationContext);

		context.assertIsSatisfied();
	}

	public void testValidityConstraintSuccess(final double fill, final String id) {

		// Create a mockery to mock up all the objects involved in a test
		final Mockery context = new Mockery();
		// This is the constraint we will be testing
		final ShipFillPercentageConstraint constraint = new ShipFillPercentageConstraint();

		// mock a vessel class
		final VesselClass vesselClass = context.mock(VesselClass.class);

		final IValidationContext validationContext = context
				.mock(IValidationContext.class);

		context.checking(new Expectations() {
			{
				atLeast(1).of(vesselClass).getFillCapacity();
				will(returnValue(fill));

				// what's the target?
				exactly(1).of(validationContext).getTarget();
				will(returnValue(vesselClass));

				atLeast(1).of(validationContext).getCurrentConstraintId();
				will(returnValue(id));

				atLeast(1).of(validationContext).createSuccessStatus();
			}
		});

		constraint.validate(validationContext);

		context.assertIsSatisfied();
	}
}
