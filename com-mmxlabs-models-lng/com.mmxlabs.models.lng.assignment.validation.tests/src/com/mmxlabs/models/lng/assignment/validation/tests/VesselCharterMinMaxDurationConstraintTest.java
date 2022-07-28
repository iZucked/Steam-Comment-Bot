/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.assignment.validation.VesselCharterMinMaxConstraint;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;

public class VesselCharterMinMaxDurationConstraintTest {

	@Test
	public void maxDurationInEndRangeWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselCharter.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 5, 0, 0));

		// End
		vesselCharter.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));
		vesselCharter.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselCharter.setMaxDuration(15);

		checkConstraint(vesselCharter, true);
	}

	@Test
	public void maxDurationOutsideEndRangeLeftWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselCharter.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselCharter.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));
		vesselCharter.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselCharter.setMaxDuration(8);

		checkConstraint(vesselCharter, false);
	}

	@Test
	public void maxDurationBeforeEndBy() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselCharter.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselCharter.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselCharter.setMaxDuration(17);

		checkConstraint(vesselCharter, true);
	}

	@Test
	public void maxDurationAfterEndBy() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselCharter.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselCharter.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselCharter.setMaxDuration(25);

		// Success as range is less than max
		checkConstraint(vesselCharter, true);
	}

	@Test
	public void maxDurationAfterEndAfter() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselCharter.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselCharter.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselCharter.setMaxDuration(25);

		checkConstraint(vesselCharter, true);
	}

	@Test
	public void maxDurationBeforeEndAfter() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselCharter.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselCharter.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselCharter.setMaxDuration(17);

		checkConstraint(vesselCharter, false);
	}

	@Test
	public void maxDurationOutsideEndRangeRightWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselCharter.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselCharter.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));
		vesselCharter.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselCharter.setMaxDuration(25);

		// Success as range is less than max
		checkConstraint(vesselCharter, true);
	}

	@Test
	public void maxDurationOnLeftEdgeEndWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselCharter.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselCharter.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));
		vesselCharter.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselCharter.setMaxDuration(10);

		checkConstraint(vesselCharter, true);
	}

	@Test
	public void maxDurationOnRightEdgeEndWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselCharter.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselCharter.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));
		vesselCharter.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselCharter.setMaxDuration(19);

		checkConstraint(vesselCharter, true);
	}

	@Test
	public void minIsLesserThanMaxDurationTest() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Duration constraints
		vesselCharter.setMinDuration(15);
		vesselCharter.setMaxDuration(20);

		checkConstraint(vesselCharter, true);
	}

	@Test
	public void minIsGreaterThanMaxDurationTest() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Duration constraints
		vesselCharter.setMinDuration(25);
		vesselCharter.setMaxDuration(20);

		checkConstraint(vesselCharter, false);
	}

	@Test
	public void minIsEqualsToMaxDurationTest() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Duration constraints
		vesselCharter.setMinDuration(20);
		vesselCharter.setMaxDuration(20);

		checkConstraint(vesselCharter, true);
	}

	private void checkConstraint(final VesselCharter target, final boolean expectSuccess) {

		CargoModel cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		cargoModel.getVesselCharters().add(target);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(target);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenReturn(failureStatus);

		final VesselCharterMinMaxConstraint constraint = new VesselCharterMinMaxConstraint();
		Assertions.assertNotNull(constraint);
		final IStatus status = constraint.validate(ctx);
		Assertions.assertNotNull(status);

		if (expectSuccess) {
			Assertions.assertTrue(status.isOK(), "Success expected");
		} else {
			Assertions.assertFalse(status.isOK(), "Failure expected");

		}
	}

	@Test
	public void minDurationInStartRangeWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselCharter.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 5, 0, 0));

		// End
		vesselCharter.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));
		vesselCharter.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 25, 0, 0));

		// Duration constraints
		vesselCharter.setMinDuration(15);

		checkConstraint(vesselCharter, true);
	}

	@Test
	public void minDurationOutsideStartRangeLeftWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselCharter.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 5, 0, 0));

		// End
		vesselCharter.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 15, 0, 0));
		vesselCharter.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 15, 0, 0));

		// Duration constraints
		vesselCharter.setMinDuration(20);

		checkConstraint(vesselCharter, false);
	}

	@Test
	public void minDurationBeforeStartBy() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));

		// End
		vesselCharter.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselCharter.setMinDuration(9);

		checkConstraint(vesselCharter, true);
	}

	@Test
	public void minDurationAfterStartBy() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));

		// End
		vesselCharter.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints (no startAfter so can extend range as needed)
		vesselCharter.setMinDuration(15);

		checkConstraint(vesselCharter, true);
	}

	@Test
	public void minDurationAfterStartAfter() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselCharter.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselCharter.setMinDuration(25);

		checkConstraint(vesselCharter, true);
	}

	@Test
	public void minDurationBeforeStartAfter() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));

		// End
		vesselCharter.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));
		vesselCharter.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 20, 0, 0));

		// Duration constraints
		vesselCharter.setMinDuration(15);

		checkConstraint(vesselCharter, false);
	}

	@Test
	public void minDurationOnRightEdgeEndWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselCharter.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));

		// End
		vesselCharter.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));
		vesselCharter.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));

		// Duration constraints
		vesselCharter.setMinDuration(9);

		checkConstraint(vesselCharter, true);
	}

	@Test
	public void minMaxWithinStartEndWindow() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);

		// Start
		vesselCharter.setStartAfter(LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0));
		vesselCharter.setStartBy(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));

		// End
		vesselCharter.setEndAfter(LocalDateTime.of(2017, Month.JANUARY, 10, 0, 0));
		vesselCharter.setEndBy(LocalDateTime.of(2017, Month.JANUARY, 15, 0, 0));

		// Duration constraints
		vesselCharter.setMinDuration(5);
		vesselCharter.setMaxDuration(14);

		checkConstraint(vesselCharter, true);
	}
}
