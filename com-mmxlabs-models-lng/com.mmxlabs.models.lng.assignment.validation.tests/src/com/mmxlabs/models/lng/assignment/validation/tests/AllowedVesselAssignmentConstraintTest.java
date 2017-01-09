/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
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
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;

public class AllowedVesselAssignmentConstraintTest {

	@Test
	public void testAllowedCargo_EmptyAllowedList_OK() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass = FleetFactory.eINSTANCE.createVesselClass();
		vessel.setVesselClass(vesselClass);

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
		final VesselClass vesselClass = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass);
		vessel2.setVesselClass(vesselClass);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getAllowedVessels().add(vessel1);

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
		final VesselClass vesselClass = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass);
		vessel2.setVesselClass(vesselClass);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getAllowedVessels().add(vessel1);

		// Not permitted!
		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel2);
		cargo.setVesselAssignmentType(vesselAvailability);

		checkConstraint(cargo, false);
	}

	@Test
	public void testAllowedCargo_AllowedList_Class1_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getAllowedVessels().add(vesselClass1);

		// Permitted!
		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel1);
		cargo.setVesselAssignmentType(vesselAvailability);

		checkConstraint(cargo, true);
	}

	@Test
	public void testAllowedCargo_AllowedList_Class2_NOT_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		final VesselClass vesselClass2 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getAllowedVessels().add(vesselClass2);

		// Not permitted!
		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel1);
		cargo.setVesselAssignmentType(vesselAvailability);

		checkConstraint(cargo, false);
	}

	@Test
	public void testAllowedCargoVC_AllowedList_Class1_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getAllowedVessels().add(vesselClass1);

		// Permitted!
		final CharterInMarket charterInMarket = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
		charterInMarket.setVesselClass(vesselClass1);
		cargo.setVesselAssignmentType(charterInMarket);

		checkConstraint(cargo, true);
	}

	@Test
	public void testAllowedCargoVC_AllowedList_Class2_NOT_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		final VesselClass vesselClass2 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getAllowedVessels().add(vesselClass2);

		// Not permitted!
		final CharterInMarket charterInMarket = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
		charterInMarket.setVesselClass(vesselClass1);
		cargo.setVesselAssignmentType(charterInMarket);
		checkConstraint(cargo, false);
	}

	/**
	 * If the allowed list is a vessel, then we cannot assign a vessel class - even if it is of correct class
	 */
	@Test
	public void testAllowedCargoVC_AllowedList_Vessel_NOT_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getAllowedVessels().add(vessel1);

		final CharterInMarket charterInMarket = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
		charterInMarket.setVesselClass(vesselClass1);
		cargo.setVesselAssignmentType(charterInMarket);
		checkConstraint(cargo, false);
	}

	@Test
	public void testAllowedCargo_AllowedList_Market_OK() {

		final VesselClass vesselClass = FleetFactory.eINSTANCE.createVesselClass();

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getAllowedVessels().add(vesselClass);

		// Permitted!
		CharterInMarket market = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
		market.setVesselClass(vesselClass);
		cargo.setVesselAssignmentType(market);

		checkConstraint(cargo, true);
	}

	@Test
	public void testAllowedCargo_AllowedList_Market_NOT_OK() {

		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		final VesselClass vesselClass2 = FleetFactory.eINSTANCE.createVesselClass();

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		loadSlot.getAllowedVessels().add(vesselClass1);

		// Not Permitted!
		CharterInMarket market = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
		market.setVesselClass(vesselClass2);
		cargo.setVesselAssignmentType(market);

		checkConstraint(cargo, false);
	}

	private void checkConstraint(final AssignableElement target, final boolean expectSuccess) {
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(target);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(Matchers.anyString())).thenReturn(failureStatus);

		final AllowedVesselAssignmentConstraint constraint = new AllowedVesselAssignmentConstraint();
		final IStatus status = constraint.validate(ctx);

		if (expectSuccess) {
			Assert.assertTrue("Sucess expected", status.isOK());
		} else {
			Assert.assertFalse("Failure expected", status.isOK());

		}
	}

}
