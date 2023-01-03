/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.time.ZonedDateTime;
import java.util.function.Consumer;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;

/**
 * Small builder class used to build  simple Schedule model instances for use in unit tests
 * @author sg
 *
 */
public class SimpleScheduleBuilder {

	private Schedule schedule = ScheduleFactory.eINSTANCE.createSchedule();

	public SequenceBuilder withSequence(VesselCharter vesselCharter) {
		return new SequenceBuilder(vesselCharter);
	}

	public SequenceBuilder withSequence(CharterInMarket charterInMarket, int spotIndex) {
		return new SequenceBuilder(charterInMarket, spotIndex);
	}

	public Schedule make() {
		return schedule;
	}

	class CargoAllocationBuilder {
		private CargoAllocation cargoAllocation;
		private SequenceBuilder sb;

		CargoAllocationBuilder(SequenceBuilder sb) {
			this.sb = sb;
			this.cargoAllocation = ScheduleFactory.eINSTANCE.createCargoAllocation();
			schedule.getCargoAllocations().add(cargoAllocation);
		}

		public CargoAllocationBuilder forSlot(Slot<?> slot, ZonedDateTime time) {
			SlotVisit visit = ScheduleFactory.eINSTANCE.createSlotVisit();
			SlotAllocation allocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
			allocation.setCargoAllocation(cargoAllocation);
			allocation.setSlot(slot);
			allocation.setSlotVisit(visit);
			allocation.setSlotAllocationType(slot instanceof LoadSlot ? SlotAllocationType.PURCHASE :  SlotAllocationType.SALE);

			// TODO: Need to handle counterparty windows properly
			visit.setStart(time);
			visit.setEnd(visit.getStart().plusHours(slot.getDuration()));
			visit.setPort(slot.getPort());

			cargoAllocation.getEvents().add(visit);

			sb.sequence.getEvents().add(visit);
			
			return this;
		}

		public SequenceBuilder make() {
			return sb;
		}

	}

	class SequenceBuilder {
		Sequence sequence = ScheduleFactory.eINSTANCE.createSequence();

		private SequenceBuilder(VesselCharter vesselCharter) {
			sequence.setVesselCharter(vesselCharter);
			sequence.setSequenceType(SequenceType.VESSEL);
			schedule.getSequences().add(sequence);
		}

		private SequenceBuilder(CharterInMarket charterInMarket, int spotIndex) {
			sequence.setCharterInMarket(charterInMarket);
			sequence.setSpotIndex(spotIndex);
			sequence.setSequenceType(SequenceType.SPOT_VESSEL);
			schedule.getSequences().add(sequence);
		}

		public SimpleScheduleBuilder make() {
			// Fix the prev/next event sequence
			Event prev = null;
			for (Event evt : sequence.getEvents()) {
				if (prev != null) {
					prev.setNextEvent(evt);
				}
				prev = evt;
			}

			return SimpleScheduleBuilder.this;
		}

		public SequenceBuilder withStartEvent(Port port, final ZonedDateTime date) {
			final StartEvent portVisit = ScheduleFactory.eINSTANCE.createStartEvent();
			portVisit.setPort(port);
			portVisit.setStart(date);
			portVisit.setEnd(date);

			sequence.getEvents().add(portVisit);

			return this;
		}
		
		public SequenceBuilder withPortVisitMaker(Consumer<PortVisit> consumer) {
			final PortVisit portVisit = ScheduleFactory.eINSTANCE.createPortVisit();

			consumer.accept(portVisit);
			
			sequence.getEvents().add(portVisit);

			return this;
		}

		

		public SequenceBuilder withEndEvent(Port port, final ZonedDateTime date) {
			final EndEvent portVisit = ScheduleFactory.eINSTANCE.createEndEvent();
			portVisit.setPort(port);
			portVisit.setStart(date);
			portVisit.setEnd(date);

			sequence.getEvents().add(portVisit);

			return this;
		}

		public SequenceBuilder forCargo(Cargo cargo) {
			CargoAllocation cargoAllocation = ScheduleFactory.eINSTANCE.createCargoAllocation();

			for (Slot<?> slot : cargo.getSortedSlots()) {
				SlotVisit visit = ScheduleFactory.eINSTANCE.createSlotVisit();
				SlotAllocation allocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
				allocation.setCargoAllocation(cargoAllocation);
				allocation.setSlot(slot);
				allocation.setSlotVisit(visit);
				allocation.setSlotAllocationType(slot instanceof LoadSlot ? SlotAllocationType.PURCHASE :  SlotAllocationType.SALE);

				// TODO: Need to handle counterparty windows properly
				visit.setStart(slot.getSchedulingTimeWindow().getStart());
				visit.setEnd(visit.getStart().plusHours(slot.getDuration()));
				visit.setPort(slot.getPort());

				cargoAllocation.getEvents().add(visit);

				sequence.getEvents().add(visit);

			}
			schedule.getCargoAllocations().add(cargoAllocation);

			return this;
		}

		public CargoAllocationBuilder withCargoAllocation() {
			return new CargoAllocationBuilder(this);
		}

		public SequenceBuilder forVesselEvent(VesselEvent vesselEvent) {
			return forVesselEvent(vesselEvent, null);
		}
		public SequenceBuilder forVesselEvent(VesselEvent vesselEvent, Consumer<VesselEventVisit> consumer) {
			VesselEventVisit visit = ScheduleFactory.eINSTANCE.createVesselEventVisit();
			visit.setVesselEvent(vesselEvent);

			visit.setStart(vesselEvent.getStartAfterAsDateTime());
			visit.setEnd(visit.getStart().plusDays(vesselEvent.getDurationInDays()));
			visit.setPort(vesselEvent.getPort());

			if (vesselEvent instanceof CharterOutEvent) {
				CharterOutEvent charterOutEvent = (CharterOutEvent) vesselEvent;
				if (charterOutEvent.isSetRelocateTo()) {
					visit.setRedeliveryPort(charterOutEvent.getRelocateTo());
				}
			}

			sequence.getEvents().add(visit);

			if (consumer != null) {
				consumer.accept(visit);
			}
			
			return this;
		}
	}
}
