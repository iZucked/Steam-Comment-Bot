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
import com.mmxlabs.models.lng.assignment.validation.InaccessiblePortsConstraint;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;

public class InaccessiblePortsConstraintTest {

	@Test
	public void testCargoSlot_OK() {

		final Port port1 = PortFactory.eINSTANCE.createPort();

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		slot.setPort(port1);
		cargo.getSlots().add(slot);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(cargo);

		target.setAssignment(vessel1);

		checkConstraint(target, true);
	}

	@Test
	public void testCargoSlot_VesselInaccessible() {

		final Port port1 = PortFactory.eINSTANCE.createPort();

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		slot.setPort(port1);
		cargo.getSlots().add(slot);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(cargo);

		vessel1.getInaccessiblePorts().add(port1);

		target.setAssignment(vessel1);

		checkConstraint(target, false);
	}

	@Test
	public void testCargoSlot_VesselClassInaccessible1() {

		final Port port1 = PortFactory.eINSTANCE.createPort();

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		slot.setPort(port1);
		cargo.getSlots().add(slot);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(cargo);

		// Test restriction on vessel class
		vesselClass1.getInaccessiblePorts().add(port1);

		// Keep vessel assigned
		target.setAssignment(vessel1);

		checkConstraint(target, false);
	}

	@Test
	public void testCargoSlot_VesselClassInaccessible2() {

		final Port port1 = PortFactory.eINSTANCE.createPort();

		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		slot.setPort(port1);
		cargo.getSlots().add(slot);
		
		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(cargo);

		// Test restriction on vessel class
		vesselClass1.getInaccessiblePorts().add(port1);

		// Test vessel class as assignment

		target.setAssignment(vesselClass1);

		checkConstraint(target, false);
	}

	@Test
	public void testVesselEvent_OK() {

		final Port port1 = PortFactory.eINSTANCE.createPort();

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final DryDockEvent event = FleetFactory.eINSTANCE.createDryDockEvent();
		event.setPort(port1);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(event);

		target.setAssignment(vessel1);

		checkConstraint(target, true);
	}

	@Test
	public void testVesselEvent_VesselInaccessible() {

		final Port port1 = PortFactory.eINSTANCE.createPort();

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final DryDockEvent event = FleetFactory.eINSTANCE.createDryDockEvent();
		event.setPort(port1);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(event);

		vessel1.getInaccessiblePorts().add(port1);

		target.setAssignment(vessel1);

		checkConstraint(target, false);
	}

	@Test
	public void testVesselEvent_VesselClassInaccessible1() {

		final Port port1 = PortFactory.eINSTANCE.createPort();

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final DryDockEvent event = FleetFactory.eINSTANCE.createDryDockEvent();
		event.setPort(port1);
		
		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(event);

		// Test restriction on vessel class
		vesselClass1.getInaccessiblePorts().add(port1);

		// Keep vessel assigned
		target.setAssignment(vessel1);

		checkConstraint(target, false);
	}

	@Test
	public void testVesselEvent_VesselClassInaccessible2() {

		final Port port1 = PortFactory.eINSTANCE.createPort();

		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();

		final DryDockEvent event = FleetFactory.eINSTANCE.createDryDockEvent();
		event.setPort(port1);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(event);

		// Test restriction on vessel class
		vesselClass1.getInaccessiblePorts().add(port1);

		// Test vessel class as assignment

		target.setAssignment(vesselClass1);

		checkConstraint(target, false);
	}
	
	

	@Test
	public void testCharterOut_OK() {

		final Port port1 = PortFactory.eINSTANCE.createPort();

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final CharterOutEvent event = FleetFactory.eINSTANCE.createCharterOutEvent();
		event.setRelocateTo(port1);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(event);

		target.setAssignment(vessel1);

		checkConstraint(target, true);
	}

	@Test
	public void testCharterOut_VesselInaccessible() {

		final Port port1 = PortFactory.eINSTANCE.createPort();

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final CharterOutEvent event = FleetFactory.eINSTANCE.createCharterOutEvent();
		event.setRelocateTo(port1);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(event);

		vessel1.getInaccessiblePorts().add(port1);

		target.setAssignment(vessel1);

		checkConstraint(target, false);
	}

	@Test
	public void testCharterOut_VesselClassInaccessible1() {

		final Port port1 = PortFactory.eINSTANCE.createPort();

		final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		vessel1.setVesselClass(vesselClass1);

		final CharterOutEvent event = FleetFactory.eINSTANCE.createCharterOutEvent();
		event.setRelocateTo(port1);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(event);

		// Test restriction on vessel class
		vesselClass1.getInaccessiblePorts().add(port1);

		// Keep vessel assigned
		target.setAssignment(vessel1);

		checkConstraint(target, false);
	}

	@Test
	public void testCharterOut_VesselClassInaccessible2() {

		final Port port1 = PortFactory.eINSTANCE.createPort();

		final VesselClass vesselClass1 = FleetFactory.eINSTANCE.createVesselClass();

		final CharterOutEvent event = FleetFactory.eINSTANCE.createCharterOutEvent();
		event.setRelocateTo(port1);

		final ElementAssignment target = AssignmentFactory.eINSTANCE.createElementAssignment();
		target.setAssignedObject(event);

		// Test restriction on vessel class
		vesselClass1.getInaccessiblePorts().add(port1);

		// Test vessel class as assignment

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

		final InaccessiblePortsConstraint constraint = new InaccessiblePortsConstraint();
		final IStatus status = constraint.validate(ctx);

		if (expectSuccess) {
			Assert.assertTrue("Sucess expected", status.isOK());
		} else {
			Assert.assertFalse("Failure expected", status.isOK());

		}
	}

}
