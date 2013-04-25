package com.mmxlabs.models.lng.transformer.util;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.assignment.AssignmentFactory;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class LNGSchedulerJobUtilsTest {

	@Test
	public void testAssignmentChange() {
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		final EditingDomain domain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());
		final Schedule schedule;
		final AssignmentModel assignmentModel;
		final CargoModel cargoModel;

		final ElementAssignment elementAssignment;
		final Vessel vessel2;
		// Build simple single cargo scenario
		{
			cargoModel = CargoFactory.eINSTANCE.createCargoModel();

			final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();

			final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

			cargo.getSlots().add(loadSlot);
			cargo.getSlots().add(dischargeSlot);

			cargoModel.getCargoes().add(cargo);
			cargoModel.getLoadSlots().add(loadSlot);
			cargoModel.getDischargeSlots().add(dischargeSlot);

			final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
			vessel1.setName("VESSEL1");

			vessel2 = FleetFactory.eINSTANCE.createVessel();
			vessel2.setName("VESSEL2");

			assignmentModel = AssignmentFactory.eINSTANCE.createAssignmentModel();
			elementAssignment = AssignmentFactory.eINSTANCE.createElementAssignment();
			elementAssignment.setAssignedObject(cargo);
			elementAssignment.setAssignment(vessel1);

			assignmentModel.getElementAssignments().add(elementAssignment);

			schedule = ScheduleFactory.eINSTANCE.createSchedule();

			final Sequence sequence = ScheduleFactory.eINSTANCE.createSequence();
			sequence.setVessel(vessel2);
			sequence.setSequenceType(SequenceType.VESSEL);

			final CargoAllocation cargoAllocation = ScheduleFactory.eINSTANCE.createCargoAllocation();
			cargoAllocation.setInputCargo(cargo);
			cargoAllocation.setSequence(sequence);
			schedule.getCargoAllocations().add(cargoAllocation);
			{
				final SlotAllocation loadAllocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
				final SlotVisit loadVisit = ScheduleFactory.eINSTANCE.createSlotVisit();

				loadAllocation.setCargoAllocation(cargoAllocation);
				loadAllocation.setSlot(loadSlot);
				loadAllocation.setSlotVisit(loadVisit);

				loadVisit.setPort(loadSlot.getPort());
				loadVisit.setSequence(sequence);

				schedule.getSlotAllocations().add(loadAllocation);
			}
			{
				final SlotAllocation dischargeAllocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
				final SlotVisit dischargeVisit = ScheduleFactory.eINSTANCE.createSlotVisit();

				dischargeAllocation.setCargoAllocation(cargoAllocation);
				dischargeAllocation.setSlot(dischargeSlot);
				dischargeAllocation.setSlotVisit(dischargeVisit);

				dischargeVisit.setPort(loadSlot.getPort());
				dischargeVisit.setSequence(sequence);

				schedule.getSlotAllocations().add(dischargeAllocation);
			}

		}
		final Command cmd = LNGSchedulerJobUtils.derive(domain, null, schedule, assignmentModel, cargoModel, null);
		Assert.assertTrue(cmd.canExecute());
		domain.getCommandStack().execute(cmd);

		// Check output
		Assert.assertEquals(1, assignmentModel.getElementAssignments().size());
		Assert.assertEquals(vessel2, assignmentModel.getElementAssignments().get(0).getAssignment());

	}
}
