/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.assignment.AssignmentFactory;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.validation.AllowedVesselAssignmentConstraint;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
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

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(cargo);
		target.setAssignment(vessel);

		checkConstraint(target, true);
	}

	@Test
	public void testAllowedCargo_AllowedList_Vessel1_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final Vessel vessel2 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass);
		vessel2.setVesselClass(vesselClass);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		cargo.getAllowedVessels().add(vessel1);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(cargo);
		// Permitted!
		target.setAssignment(vessel1);

		checkConstraint(target, true);
	}

	@Test
	public void testAllowedCargo_AllowedList_Vessel2_NOT_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final Vessel vessel2 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass);
		vessel2.setVesselClass(vesselClass);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		cargo.getAllowedVessels().add(vessel1);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(cargo);
		// Not permitted!
		target.setAssignment(vessel2);

		checkConstraint(target, false);
	}

	@Test
	public void testAllowedCargo_AllowedList_Class1_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		cargo.getAllowedVessels().add(vesselClass1);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(cargo);
		// Permitted!
		target.setAssignment(vessel1);

		checkConstraint(target, true);
	}

	@Test
	public void testAllowedCargo_AllowedList_Class2_NOT_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		final VesselClass vesselClass2 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		cargo.getAllowedVessels().add(vesselClass2);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(cargo);
		// Not permitted!
		target.setAssignment(vessel1);

		checkConstraint(target, false);
	}

	@Test
	public void testAllowedCargoVC_AllowedList_Class1_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		cargo.getAllowedVessels().add(vesselClass1);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(cargo);
		// Permitted!
		target.setAssignment(vesselClass1);

		checkConstraint(target, true);
	}

	@Test
	public void testAllowedCargoVC_AllowedList_Class2_NOT_OK() {

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		final VesselClass vesselClass2 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		cargo.getAllowedVessels().add(vesselClass2);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(cargo);
		// Not permitted!
		target.setAssignment(vesselClass1);

		checkConstraint(target, false);
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
		cargo.getAllowedVessels().add(vessel1);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(cargo);
		// Not permitted!
		target.setAssignment(vesselClass1);

		checkConstraint(target, false);
	}

	private void checkConstraint(final EObject target, final boolean expectSuccess) {
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
