/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics.spec;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.SlotSpecification;
import com.mmxlabs.models.lng.cargo.VesselEventSpecification;
import com.mmxlabs.models.lng.cargo.VesselScheduleSpecification;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class ScheduleModelToScheduleSpecification {

	public ScheduleSpecification generateScheduleSpecifications(final ScheduleModel baseSchedule) {

		final ScheduleSpecification scheduleSpecification = CargoFactory.eINSTANCE.createScheduleSpecification();

		final Schedule schedule = baseSchedule.getSchedule();

		for (final Sequence sequence : schedule.getSequences()) {

			if (sequence.getCharterInMarket() != null || sequence.getVesselCharter() != null) {
				final VesselScheduleSpecification vesselSpec = CargoFactory.eINSTANCE.createVesselScheduleSpecification();
				if (sequence.getCharterInMarket() != null) {
					// Nominals
					vesselSpec.setVesselAllocation(sequence.getCharterInMarket());
					vesselSpec.setSpotIndex(sequence.getSpotIndex());
				} else if (sequence.getVesselCharter() != null) {
					vesselSpec.setVesselAllocation(sequence.getVesselCharter());
				}
				scheduleSpecification.getVesselScheduleSpecifications().add(vesselSpec);

				for (final Event evt : sequence.getEvents()) {
					if (evt instanceof SlotVisit) {
						final SlotVisit visit = (SlotVisit) evt;
						final SlotSpecification slotSpec = CargoFactory.eINSTANCE.createSlotSpecification();
						slotSpec.setSlot(visit.getSlotAllocation().getSlot());
						vesselSpec.getEvents().add(slotSpec);
					} else if (evt instanceof VesselEventVisit) {
						final VesselEventVisit vesselEventVisit = (VesselEventVisit) evt;
						final VesselEventSpecification eventSpec = CargoFactory.eINSTANCE.createVesselEventSpecification();
						eventSpec.setVesselEvent(vesselEventVisit.getVesselEvent());
						vesselSpec.getEvents().add(eventSpec);
					}
				}
			}
		}
		// Non-shipped cargoes
		for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
			if (cargoAllocation.getCargoType() == CargoType.FOB || cargoAllocation.getCargoType() == CargoType.DES) {
				final NonShippedCargoSpecification cargoSpec = CargoFactory.eINSTANCE.createNonShippedCargoSpecification();
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final SlotSpecification slotSpec = CargoFactory.eINSTANCE.createSlotSpecification();
					slotSpec.setSlot(slotAllocation.getSlot());
					cargoSpec.getSlotSpecifications().add(slotSpec);
				}
				scheduleSpecification.getNonShippedCargoSpecifications().add(cargoSpec);
			}
		}
		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			final SlotSpecification spec = CargoFactory.eINSTANCE.createSlotSpecification();
			spec.setSlot(openSlotAllocation.getSlot());
			scheduleSpecification.getOpenEvents().add(spec);
		}
		return scheduleSpecification;
	}
}
