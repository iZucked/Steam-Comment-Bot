/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.assignment.validation.AllowedVesselAssignmentConstraint;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class AllowedVesselAssignmentConstraintTest {

	@Test
	public void testAllowedCargo_EmptyAllowedList_OK() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);
		cargo.setVesselAssignmentType(vesselAvailability);

		checkConstraint(cargo, true);
	}

	@Test
	public void testAllowedCargo_AllowedList_Vessel1_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final Vessel vessel2 = FleetFactory.eINSTANCE.createVessel();

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getRestrictedVessels().add(vessel1);
		loadSlot.setRestrictedVesselsArePermissive(true);
		loadSlot.setRestrictedVesselsOverride(true);

		// Permitted!
		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel1);
		cargo.setVesselAssignmentType(vesselAvailability);

		checkConstraint(cargo, true);
	}

	@Test
	public void testAllowedCargo_AllowedList_Vessel2_NOT_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final Vessel vessel2 = FleetFactory.eINSTANCE.createVessel();

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getRestrictedVessels().add(vessel1);
		loadSlot.setRestrictedVesselsArePermissive(true);
		loadSlot.setRestrictedVesselsOverride(true);

		// Not permitted!
		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel2);
		cargo.setVesselAssignmentType(vesselAvailability);

		checkConstraint(cargo, false);
	}

	@Test
	public void testAllowedCargo_AllowedList_Class1_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getRestrictedVessels().add(vessel1);
		loadSlot.setRestrictedVesselsArePermissive(true);
		loadSlot.setRestrictedVesselsOverride(true);

		// Permitted!
		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel1);
		cargo.setVesselAssignmentType(vesselAvailability);

		checkConstraint(cargo, true);
	}

	@Test
	public void testAllowedCargo_AllowedList_Class2_NOT_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final Vessel vessel2 = FleetFactory.eINSTANCE.createVessel();

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getRestrictedVessels().add(vessel2);
		loadSlot.setRestrictedVesselsArePermissive(true);
		loadSlot.setRestrictedVesselsOverride(true);

		// Not permitted!
		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel1);
		cargo.setVesselAssignmentType(vesselAvailability);

		checkConstraint(cargo, false);
	}

	@Test
	public void testAllowedCargoVC_AllowedList_Class1_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getRestrictedVessels().add(vessel1);
		loadSlot.setRestrictedVesselsArePermissive(true);
		loadSlot.setRestrictedVesselsOverride(true);

		// Permitted!
		final CharterInMarket charterInMarket = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
		charterInMarket.setVessel(vessel1);
		cargo.setVesselAssignmentType(charterInMarket);

		checkConstraint(cargo, true);
	}

	@Test
	public void testAllowedCargoVC_AllowedList_Class2_NOT_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final Vessel vessel2 = FleetFactory.eINSTANCE.createVessel();

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getRestrictedVessels().add(vessel2);
		loadSlot.setRestrictedVesselsArePermissive(true);
		loadSlot.setRestrictedVesselsOverride(true);

		// Not permitted!
		final CharterInMarket charterInMarket = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
		charterInMarket.setVessel(vessel1);
		cargo.setVesselAssignmentType(charterInMarket);
		checkConstraint(cargo, false);
	}

	@Test
	public void testAllowedCargo_AllowedList_Market_OK() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getRestrictedVessels().add(vessel);
		loadSlot.setRestrictedVesselsArePermissive(true);
		loadSlot.setRestrictedVesselsOverride(true);
		// Permitted!
		CharterInMarket market = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
		market.setVessel(vessel);
		cargo.setVesselAssignmentType(market);

		checkConstraint(cargo, true);
	}

	@Test
	public void testAllowedCargo_AllowedList_Market_NOT_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final Vessel vessel2 = FleetFactory.eINSTANCE.createVessel();

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getRestrictedVessels().add(vessel1);
		loadSlot.setRestrictedVesselsArePermissive(true);
		loadSlot.setRestrictedVesselsOverride(true);
		// Not Permitted!
		CharterInMarket market = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
		market.setVessel(vessel2);
		cargo.setVesselAssignmentType(market);

		checkConstraint(cargo, false);
	}

	private void checkConstraint(final AssignableElement target, final boolean expectSuccess) {
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		System.out.println("AllowedVesselAssignmentConstraintTest: Before mock context");

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(target);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(ArgumentMatchers.anyString())).thenReturn(failureStatus);

		System.out.println("AllowedVesselAssignmentConstraintTest: Before new");
		final AllowedVesselAssignmentConstraint constraint = new AllowedVesselAssignmentConstraint();
		System.out.println("AllowedVesselAssignmentConstraintTest: After new");
		System.out.println("AllowedVesselAssignmentConstraintTest: Activator is " + com.mmxlabs.models.lng.assignment.validation.internal.Activator.getDefault());
		try {
			final IStatus status = constraint.validate(ctx);

			System.out.println("AllowedVesselAssignmentConstraintTest: After validation");

			if (expectSuccess) {
				Assertions.assertTrue(status.isOK(), "Success expected");
			} else {
				Assertions.assertFalse(status.isOK(), "Failure expected");

			}
		} catch (Throwable t) {
			t.printStackTrace();
			Assertions.fail(t.getMessage());
		}
	}

}
