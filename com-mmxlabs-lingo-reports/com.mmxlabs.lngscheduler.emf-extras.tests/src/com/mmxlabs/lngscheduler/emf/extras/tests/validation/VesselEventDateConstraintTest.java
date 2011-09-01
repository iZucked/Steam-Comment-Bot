/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.validation;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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

	@Test
	public void testVesselEventDateConstraint() {
		
		// Create some dates to test.
		final long time = System.currentTimeMillis();
		final Date today = new Date(time);
		final Date tomorrow = new Date(time + TimeUnit.DAYS.toMillis(1));
		
		// right way around
		testVesselEventDateConstraint(true, today, tomorrow);
		// wrong way around
		testVesselEventDateConstraint(false, tomorrow, today);
		// the same day should be fine.
		testVesselEventDateConstraint(true, today, today);
		
		// test nulls, any null should produce a failure
		testVesselEventDateConstraint(false, today, null);
		testVesselEventDateConstraint(false, null, tomorrow);
		testVesselEventDateConstraint(false, null, null);
	}

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
