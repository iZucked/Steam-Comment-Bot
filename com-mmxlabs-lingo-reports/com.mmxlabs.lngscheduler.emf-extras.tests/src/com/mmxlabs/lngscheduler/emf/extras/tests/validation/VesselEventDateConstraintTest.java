/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.validation;

import java.util.Date;

import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import scenario.fleet.VesselEvent;

import com.mmxlabs.lngscheduler.emf.extras.validation.VesselEventDateConstraint;

/**
 * The VesselEventDateConstraint expects a VesselEvent as a target, and checks that the startDate is not after the endDate.
 */
public class VesselEventDateConstraintTest {

	/**
	 * Test various combinations of test input.
	 */
	@Test
	public void testVesselEventDateConstraint() {
		
		// Create some dates to test.
		final long time = System.currentTimeMillis();
		final Date now = new Date(time);
		final Date nowPlusOne = new Date(time + 1);
		
		// right way around
		testVesselEventDateConstraint(true, now, nowPlusOne);
		// wrong way around
		testVesselEventDateConstraint(false, nowPlusOne, now);
		// the same day should be fine.
		testVesselEventDateConstraint(true, now, now);
		
		// Test null dates. Any null should produce a failure.
		testVesselEventDateConstraint(false, now, null);
		testVesselEventDateConstraint(false, null, nowPlusOne);
		testVesselEventDateConstraint(false, null, null);
	}

	/**
	 * Tests VesselEventDateConstraint given a start and end date for a VesselDate. If the test is expected to pass, the boolean expectSuccess should be true.
	 * 
	 * @param expectSuccess Whether the test is expected to succeed. If false then the test will be expected to fail.
	 * @param start The start date of the VesselEvent.
	 * @param end The end date of the VesselEvent.
	 */
	private void testVesselEventDateConstraint(final boolean expectSuccess, final Date start, final Date end) {

		// Create a mockery to mock up all the objects involved in a test
		final Mockery context = new Mockery();
		// This is the constraint we will be testing
		final VesselEventDateConstraint constraint = new VesselEventDateConstraint();

		final VesselEvent vesselEvent = context.mock(VesselEvent.class);

		final IValidationContext validationContext = context.mock(IValidationContext.class);

		final IConstraintStatus resultStatus = context.mock(IConstraintStatus.class);

		context.checking(new Expectations() {
			{
				exactly(1).of(validationContext).getTarget();
				will(returnValue(vesselEvent));

				// Set the start and end dates as given in the arguments.
				exactly(1).of(vesselEvent).getStartDate();
				will(returnValue(start));
				exactly(1).of(vesselEvent).getEndDate();
				will(returnValue(end));

				if (expectSuccess)
					exactly(1).of(validationContext).createSuccessStatus();
				else {
					exactly(1).of(vesselEvent).getId();
					will(returnValue("vesselEventID"));
					
					exactly(1).of(validationContext).createFailureStatus("vesselEventID");
					will(returnValue(resultStatus));
				}
			}
		});

		constraint.validate(validationContext);

		context.assertIsSatisfied();
	}
}
