/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;

public class CargoUtils {
	public static @Nullable LoadSlot getLoadSlot(Cargo cargo) {
		Slot firstElement = CollectionsUtil.getFirstElement(cargo.getSortedSlots());
		LoadSlot load = null;
		if (firstElement != null && firstElement instanceof LoadSlot) {
			load = (LoadSlot) firstElement;
		}
		return load;
	}

	public static @Nullable DischargeSlot getDischargeSlot(Cargo cargo) {
		Slot lastElement = CollectionsUtil.getLastElement(cargo.getSortedSlots());
		DischargeSlot discharge = null;
		if (lastElement != null && lastElement instanceof DischargeSlot) {
			discharge = (DischargeSlot) lastElement;
		}
		return discharge;
	}
}
