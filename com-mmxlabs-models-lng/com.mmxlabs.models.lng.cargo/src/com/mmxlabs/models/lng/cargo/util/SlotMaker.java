/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;

public class SlotMaker extends AbstractSlotMaker<SlotMaker> {

	public SlotMaker(@NonNull final CargoModelBuilder cargoModelBuilder) {
		super(cargoModelBuilder);
	}

	@NonNull
	public Slot build() {

		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			cargoModelBuilder.getCargoModel().getLoadSlots().add(loadSlot);
		} else if (slot instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) slot;
			cargoModelBuilder.getCargoModel().getDischargeSlots().add(dischargeSlot);
		} else {
			assert false;
		}

		return slot;
	}
}
