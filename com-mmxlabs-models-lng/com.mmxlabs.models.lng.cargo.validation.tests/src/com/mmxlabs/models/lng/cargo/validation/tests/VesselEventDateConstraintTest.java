/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation.tests;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.Test;

import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.validation.VesselEventDateConstraint;

/**
 * The VesselEventDateConstraint expects a VesselEvent as a target, and checks that the startDate is not after the endDate.
 */
public class VesselEventDateConstraintTest {

	// Create some dates to test.
	private static final long time = System.currentTimeMillis();
	private static final LocalDateTime now = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC"));
	private static final LocalDateTime nowPlusOne = LocalDateTime.ofInstant(Instant.ofEpochMilli(time + 1), ZoneId.of("UTC"));

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
	private void testVesselEventDateConstraint(final boolean expectSuccess, final LocalDateTime start, final LocalDateTime end) {
		// This is the constraint we will be testing
		final VesselEventDateConstraint constraint = new VesselEventDateConstraint();
		final String vesselEventID = "vesselEventID";

		final VesselEvent vesselEvent = mock(VesselEvent.class);
		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus resultStatus = mock(IConstraintStatus.class);

		when(validationContext.getTarget()).thenReturn(vesselEvent);
		// Set the start and end dates as given in the arguments.
		when(vesselEvent.getStartAfter()).thenReturn(start);
		when(vesselEvent.getStartBy()).thenReturn(end);
		if (expectSuccess) {
			when(validationContext.createSuccessStatus()).thenReturn(resultStatus);
			// when(resultStatus.getSeverity()).thenReturn(IStatus.OK);
		} else {
			when(vesselEvent.getName()).thenReturn(vesselEventID);
			when(validationContext.createFailureStatus(vesselEventID)).thenReturn(resultStatus);
			when(resultStatus.getSeverity()).thenReturn(IStatus.ERROR);
		}

		constraint.validate(validationContext);

		verify(validationContext).getTarget();
		verify(vesselEvent).getStartAfter();
		verify(vesselEvent).getStartBy();
		if (expectSuccess) {
			verify(validationContext).createSuccessStatus();
		} else {
			verify(vesselEvent).getName();
			verify(validationContext).createFailureStatus(anyString());
			verify(resultStatus).getSeverity();
		}
		// verify that only the methods above are called.
		verifyNoMoreInteractions(vesselEvent);
		verifyNoMoreInteractions(validationContext);
		verifyNoMoreInteractions(resultStatus);
	}
}
