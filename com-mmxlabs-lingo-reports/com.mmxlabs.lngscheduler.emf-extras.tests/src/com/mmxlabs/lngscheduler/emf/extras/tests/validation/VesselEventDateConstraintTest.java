/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.validation;

import java.util.Date;

import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.Test;

import scenario.fleet.VesselEvent;

import com.mmxlabs.lngscheduler.emf.extras.validation.VesselEventDateConstraint;

/**
 * The VesselEventDateConstraint expects a VesselEvent as a target, and checks that the startDate is not after the endDate.
 */
public class VesselEventDateConstraintTest {

	// Create some dates to test.
	private static final long time = System.currentTimeMillis();
	private static final Date now = new Date(time);
	private static final Date nowPlusOne = new Date(time + 1);

	/**
	 * Test the constraint succeeds with the correct input.
	 */
	@Test
	public void testVesselEventDateConstraint() {
		// right way around
		testVesselEventDateConstraint(true, now, nowPlusOne);
	}

	/**
	 * Test the constraint fails if the dates are the wrong way around.
	 */
	@Test
	public void testVesselEventDateConstraintWrongWayAround() {
		// wrong way around
		testVesselEventDateConstraint(false, nowPlusOne, now);
	}

	/**
	 * Test the constraint succeeds if given the same date.
	 */
	@Test
	public void testVesselEventDateConstraintSameDay() {
		// the same day should be fine.
		testVesselEventDateConstraint(true, now, now);
	}

	/**
	 * Test the constraint fails if the first date is null.
	 */
	@Test
	public void testVesselEventDateConstraintFirstDateNull() {
		testVesselEventDateConstraint(false, null, nowPlusOne);
	}

	/**
	 * Test the constraint fails if the second date is null.
	 */
	@Test
	public void testVesselEventDateConstraintSecondDateNull() {
		testVesselEventDateConstraint(false, now, null);
	}

	/**
	 * Test the constraint fails if both dates are null.
	 */
	@Test
	public void testVesselEventDateConstraintDatesBothNull() {
		testVesselEventDateConstraint(false, null, null);
	}

	/**
	 * Tests VesselEventDateConstraint given a start and end date for a VesselDate. If the test is expected to pass, the boolean expectSuccess should be true.
	 * 
	 * @param expectSuccess
	 *            Whether the test is expected to succeed. If false then the test will be expected to fail.
	 * @param start
	 *            The start date of the VesselEvent.
	 * @param end
	 *            The end date of the VesselEvent.
	 */
	private void testVesselEventDateConstraint(final boolean expectSuccess, final Date start, final Date end) {
		// This is the constraint we will be testing
		final VesselEventDateConstraint constraint = new VesselEventDateConstraint();
		final String vesselEventID = "vesselEventID";

		final VesselEvent vesselEvent = mock(VesselEvent.class);
		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus resultStatus = mock(IConstraintStatus.class);

		when(validationContext.getTarget()).thenReturn(vesselEvent);
		// Set the start and end dates as given in the arguments.
		when(vesselEvent.getStartDate()).thenReturn(start);
		when(vesselEvent.getEndDate()).thenReturn(end);
		if (expectSuccess) {
			when(validationContext.createSuccessStatus()).thenReturn(resultStatus);
		} else {
			when(vesselEvent.getId()).thenReturn(vesselEventID);
			when(validationContext.createFailureStatus(vesselEventID)).thenReturn(resultStatus);
		}

		constraint.validate(validationContext);

		verify(validationContext).getTarget();
		verify(vesselEvent).getStartDate();
		verify(vesselEvent).getEndDate();
		if (expectSuccess) {
			verify(validationContext).createSuccessStatus();
		} else {
			verify(vesselEvent).getId();
			verify(validationContext).createFailureStatus(anyString());
		}
		// verify that only the methods above are called.
		verifyNoMoreInteractions(vesselEvent);
		verifyNoMoreInteractions(validationContext);
		verifyNoMoreInteractions(resultStatus);
	}
}
