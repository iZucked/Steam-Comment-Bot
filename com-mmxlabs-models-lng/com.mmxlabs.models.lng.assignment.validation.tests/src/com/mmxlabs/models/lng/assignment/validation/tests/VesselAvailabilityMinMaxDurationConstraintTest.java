/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Month;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.assignment.validation.VesselAvailabilityMinMaxConstraint;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;

public class VesselAvailabilityMinMaxDurationConstraintTest {

	@Test
	public void maxDurationInEndRangeWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 5, 0, 0));

		// End
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselAvailability.setMaxDuration(15);

		checkConstraint(vesselAvailability, true);
	}

	@Test
	public void maxDurationOutsideEndRangeLeftWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselAvailability.setMaxDuration(8);

		checkConstraint(vesselAvailability, false);
	}

	@Test
	public void maxDurationBeforeEndBy() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselAvailability.setMaxDuration(17);

		checkConstraint(vesselAvailability, true);
	}

	@Test
	public void maxDurationAfterEndBy() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselAvailability.setMaxDuration(25);

		// Success as range is less than max
		checkConstraint(vesselAvailability, true);
	}

	@Test
	public void maxDurationAfterEndAfter() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselAvailability.setMaxDuration(25);

		checkConstraint(vesselAvailability, true);
	}

	@Test
	public void maxDurationBeforeEndAfter() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselAvailability.setMaxDuration(17);

		checkConstraint(vesselAvailability, false);
	}

	@Test
	public void maxDurationOutsideEndRangeRightWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselAvailability.setMaxDuration(25);

		// Success as range is less than max
		checkConstraint(vesselAvailability, true);
	}

	@Test
	public void maxDurationOnLeftEdgeEndWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselAvailability.setMaxDuration(10);

		checkConstraint(vesselAvailability, true);
	}

	@Test
	public void maxDurationOnRightEdgeEndWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselAvailability.setMaxDuration(19);

		checkConstraint(vesselAvailability, true);
	}

	@Test
	public void minIsLesserThanMaxDurationTest() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Duration constraints
		vesselAvailability.setMinDuration(15);
		vesselAvailability.setMaxDuration(20);

		checkConstraint(vesselAvailability, true);
	}

	@Test
	public void minIsGreaterThanMaxDurationTest() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Duration constraints
		vesselAvailability.setMinDuration(25);
		vesselAvailability.setMaxDuration(20);

		checkConstraint(vesselAvailability, false);
	}

	@Test
	public void minIsEqualsToMaxDurationTest() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Duration constraints
		vesselAvailability.setMinDuration(20);
		vesselAvailability.setMaxDuration(20);

		checkConstraint(vesselAvailability, true);
	}

	private void checkConstraint(final VesselAvailability target, final boolean expectSuccess) {

		CargoModel cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		cargoModel.getVesselAvailabilities().add(target);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(target);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(Matchers.anyString(), Matchers.any())).thenReturn(failureStatus);

		final VesselAvailabilityMinMaxConstraint constraint = new VesselAvailabilityMinMaxConstraint();
		Assert.assertNotNull(constraint);
		final IStatus status = constraint.validate(ctx);
		Assert.assertNotNull(status);

		if (expectSuccess) {
			Assert.assertTrue("Sucess expected", status.isOK());
		} else {
			Assert.assertFalse("Failure expected", status.isOK());

		}
	}

	@Test
	public void minDurationInStartRangeWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 5, 0, 0));

		// End
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 25, 0, 0));

		// Duration constraints
		vesselAvailability.setMinDuration(15);

		checkConstraint(vesselAvailability, true);
	}

	@Test
	public void minDurationOutsideStartRangeLeftWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 5, 0, 0));

		// End
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 15, 0, 0));
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 15, 0, 0));

		// Duration constraints
		vesselAvailability.setMinDuration(20);

		checkConstraint(vesselAvailability, false);
	}

	@Test
	public void minDurationBeforeStartBy() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));

		// End
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselAvailability.setMinDuration(9);

		checkConstraint(vesselAvailability, true);
	}

	@Test
	public void minDurationAfterStartBy() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));

		// End
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints (no startAfter so can extend range as needed)
		vesselAvailability.setMinDuration(15);

		checkConstraint(vesselAvailability, true);
	}

	@Test
	public void minDurationAfterStartAfter() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselAvailability.setMinDuration(25);

		checkConstraint(vesselAvailability, true);
	}

	@Test
	public void minDurationBeforeStartAfter() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));

		// End
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselAvailability.setMinDuration(15);

		checkConstraint(vesselAvailability, false);
	}

	@Test
	public void minDurationOnRightEdgeEndWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));

		// Duration constraints
		vesselAvailability.setMinDuration(9);

		checkConstraint(vesselAvailability, true);
	}

	@Test
	public void minMaxWithinStartEndWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		// Start
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));

		// End
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 15, 0, 0));

		// Duration constraints
		vesselAvailability.setMinDuration(5);
		vesselAvailability.setMaxDuration(14);

		checkConstraint(vesselAvailability, true);
	}
}
