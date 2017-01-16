/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class LNGSchedulerJobUtilsTests {

	@Test
	public void testAssignmentChange() {
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		final EditingDomain domain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());
		final Schedule schedule;
		final CargoModel cargoModel;

		final VesselAvailability availability2;
		final Cargo cargo;
		// Build simple single cargo scenario
		{
			cargoModel = CargoFactory.eINSTANCE.createCargoModel();

			final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();

			cargo = CargoFactory.eINSTANCE.createCargo();

			cargo.getSlots().add(loadSlot);
			cargo.getSlots().add(dischargeSlot);

			cargoModel.getCargoes().add(cargo);
			cargoModel.getLoadSlots().add(loadSlot);
			cargoModel.getDischargeSlots().add(dischargeSlot);

			final Vessel vessel1 = FleetFactory.eINSTANCE.createVessel();
			vessel1.setName("VESSEL1");

			Vessel vessel2 = FleetFactory.eINSTANCE.createVessel();
			vessel2.setName("VESSEL2");

			final VesselAvailability availability1 = CargoFactory.eINSTANCE.createVesselAvailability();
			availability1.setVessel(vessel1);

			availability2 = CargoFactory.eINSTANCE.createVesselAvailability();
			availability2.setVessel(vessel2);

			cargo.setVesselAssignmentType(availability1);

			schedule = ScheduleFactory.eINSTANCE.createSchedule();

			final Sequence sequence = ScheduleFactory.eINSTANCE.createSequence();
			sequence.setVesselAvailability(availability2);
			sequence.setSequenceType(SequenceType.VESSEL);
			schedule.getSequences().add(sequence);

			final CargoAllocation cargoAllocation = ScheduleFactory.eINSTANCE.createCargoAllocation();
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
		final Command cmd = LNGSchedulerJobUtils.derive(domain, null, schedule, cargoModel, null);
		Assert.assertTrue(cmd.canExecute());
		domain.getCommandStack().execute(cmd);

		// Check output
		Assert.assertEquals(availability2, cargo.getVesselAssignmentType());

	}
}
