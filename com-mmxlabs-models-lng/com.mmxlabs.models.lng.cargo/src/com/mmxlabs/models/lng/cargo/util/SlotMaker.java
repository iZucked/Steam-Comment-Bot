/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.Slot;

public class SlotMaker extends AbstractSlotMaker<SlotMaker> {

	public SlotMaker(@NonNull CargoModelBuilder cargoModelBuilder) {
		super(cargoModelBuilder);
	}

	@NonNull
	public Slot make() {
		return slot;
	}
}
