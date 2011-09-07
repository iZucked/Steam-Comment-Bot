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
 * Info from case 170:
 * 
 * Write a test for the ShipFillPercentageConstraint.
 * 
 * The ship fill percentage is the percentage of a vessel class' tanks that can actually be used. In the model, percentages are held as floating point numbers from 0 to 1 (i.e. proportions). The
 * ShipFillPercentageConstraint is supposed to check that this value is (a) valid and (b) reasonable.
 * 
 * A valid ship fill percentage is strictly greater than zero and less than or equal to 100%
 * 
 * A reasonable ship fill percentage is more than 80% (0.8).
 * 
 * The constraint expects its IValidationContext to provide a VesselClass as the target, and one of "com.mmxlabs.lngscheduler.emf-extras.constraints.ship_fill_validity" or
 * "com.mmxlabs.lngscheduler.emf-extras.constraints.ship_fill_sanity" from IValidationContext.getCurrentConstraintID(), indicating whether it's checking validity or reasonableness.
 * 
 */
public class ShipFillPercentageConstraintTest {

	// Create a mockery to mock up all the objects involved in a test
	final Mockery context = new Mockery();
	// the fill that is defined as sensible in ShipFillPercentageConstraint.
	private static final double sensiblefill = 0.8;

	/**
	 * Test that a fill of 0.9 is reasonable.
	 */
	@Test
	public void testSanityConstraintOverSensibleFill() {

		final double fill = 0.9d;
		testValidityConstraint(true, fill, ShipFillPercentageConstraint.SANITY_ID);
	}

	/**
	 * Test that a fill of 0.8 (the limit) is reasonable.
	 */
	@Test
	public void testSanityConstraintAtSensibleFill() {

		final double fill = 0.8d;
		testValidityConstraint(true, fill, ShipFillPercentageConstraint.SANITY_ID);
	}

	/**
	 * Test a fill of 0.7 is reasonable.
	 */
	@Test
	public void testSanityConstraintUnderSensibleFill() {

		final double undersensiblefill = 0.7d;
		testValidityConstraint(false, undersensiblefill, ShipFillPercentageConstraint.SANITY_ID);
	}

	/**
	 * Test a fill of 0.5 is in the valid range.
	 */
	@Test
	public void testValidityConstraintFillInRage() {

		final double negativefill = 0.5d;
		// negative fill should fail validity test
		testValidityConstraint(true, negativefill, ShipFillPercentageConstraint.VALIDITY_ID);
	}

	/**
	 * Test that a negative fill is not a valid fill.
	 */
	@Test
	public void testValidityConstraintNegativeFill() {

		final double negativefill = -1;
		// negative fill should fail validity test
		testValidityConstraint(false, negativefill, ShipFillPercentageConstraint.VALIDITY_ID);
	}

	/**
	 * Test that a fill over 1 is not valid.
	 */
	@Test
	public void testValidityConstraintOverfullFill() {

		final double overfill = 1.1d;
		// fills over 1 should fail validity test
		testValidityConstraint(false, overfill, ShipFillPercentageConstraint.VALIDITY_ID);
	}

	/**
	 * Test that a fill of 0 is not valid.
	 */
	@Test
	public void testValidityConstraintZeroFill() {

		final double fill = 0d;
		// fills over 1 should fail validity test
		testValidityConstraint(false, fill, ShipFillPercentageConstraint.VALIDITY_ID);
	}

	/**
	 * Test that a fill of 1 is valid.
	 */
	@Test
	public void testValidityConstraintOneFill() {

		final double fill = 1d;
		// fills over 1 should fail validity test
		testValidityConstraint(true, fill, ShipFillPercentageConstraint.VALIDITY_ID);
	}

	/**
	 * Runs a test given a fill and a constraint to check. Expects the test to succeed and produce a success status.
	 * 
	 * @param fill
	 * @param id
	 */
	private void testValidityConstraint(final boolean expectSuccess, final double fill, final String id) {

		// This is the constraint we will be testing
		final ShipFillPercentageConstraint constraint = new ShipFillPercentageConstraint();

		// mock a vessel class
		final VesselClass vesselClass = context.mock(VesselClass.class);

		final IValidationContext validationContext = context.mock(IValidationContext.class);

		final IConstraintStatus failureStatus = context.mock(IConstraintStatus.class);

		context.checking(new Expectations() {
			{
				atMost(1).of(vesselClass).getName();
				will(returnValue("vc"));
				
				atMost(3).of(vesselClass).getFillCapacity();
				will(returnValue(fill));

				// what's the target?
				atMost(1).of(validationContext).getTarget();
				will(returnValue(vesselClass));

				atMost(2).of(validationContext).getCurrentConstraintId();
				will(returnValue(id));

				if (expectSuccess) {
					atMost(1).of(validationContext).createSuccessStatus();
				} else {
					atMost(1).of(validationContext).createFailureStatus("vc", fill * 100, sensiblefill * 100);
					will(returnValue(failureStatus));
				}
			}
		});

		constraint.validate(validationContext);

		context.assertIsSatisfied();
	}
}
