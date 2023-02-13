/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;

public class ScheduleModelFinder {
	private final @NonNull Schedule schedule;

	public ScheduleModelFinder(final @NonNull Schedule schedule) {
		this.schedule = schedule;
	}

	@NonNull
	public Schedule getSchedule() {
		return schedule;
	}

	public @Nullable CargoAllocation findCargoAllocation(final @NonNull String cargoID) {
		for (final CargoAllocation ca : schedule.getCargoAllocations()) {
			if (cargoID.equals(ca.getName())) {
				return ca;
			}
		}

		return null;
	}
	
//	public @Nullable CargoAllocation findCargoAllocation(final @NonNull Cargo cargo) {
//		for (final CargoAllocation ca : schedule.getCargoAllocations()) {
//			
//			
//			if (cargoID.equals(ca.getName())) {
//				return ca;
//			}
//		}
//		
//		return null;
//	}

	public @Nullable CargoAllocation findCargoAllocationByDischargeID(final @NonNull String dischargeID) {
		for (final CargoAllocation ca : schedule.getCargoAllocations()) {
			for (final SlotAllocation sa : ca.getSlotAllocations()) {
				final Slot s = sa.getSlot();
				if (s instanceof DischargeSlot) {
					if (dischargeID.equals(s.getName())) {
						return ca;
					}
				}
			}
		}

		return null;
	}

	public @Nullable SlotAllocation findSlotAllocation(final @NonNull SlotAllocationType type, final @NonNull String slotID) {
		for (final SlotAllocation slotAllocation : schedule.getSlotAllocations()) {
			if (slotAllocation.getSlotAllocationType() == type) {
				if (slotID.equals(slotAllocation.getName())) {
					return slotAllocation;
				}
			}
		}

		return null;
	}

	public @Nullable OpenSlotAllocation findOpenSlotAllocation(final @NonNull SlotAllocationType type, final @NonNull String slotID) {
		for (final OpenSlotAllocation slotAllocation : schedule.getOpenSlotAllocations()) {
			final Slot slot = slotAllocation.getSlot();
			if (slot != null) {
				if (slotID.equals(slot.getName())) {
					if (slot instanceof LoadSlot && type == SlotAllocationType.PURCHASE) {
						return slotAllocation;
					}
					if (slot instanceof DischargeSlot && type == SlotAllocationType.SALE) {
						return slotAllocation;
					}
				}
			}
		}

		return null;
	}

}
