/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
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
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;

public class AllowedVesselAssignmentConstraintTest {

	@Test
	public void testAllowedCargo_EmptyAllowedList_OK() {

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass = FleetFactory.eINSTANCE.createVesselClass();
		vessel.setVesselClass(vesselClass);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		cargo.setAssignment(vessel);

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
		cargo.setAssignment(vessel1);

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
		cargo.setAssignment(vessel2);

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
		cargo.setAssignment(vessel1);

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
		cargo.setAssignment(vessel1);

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
		cargo.setAssignment(vesselClass1);

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
		cargo.setAssignment(vesselClass1);

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

		cargo.setAssignment(vesselClass1);

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
