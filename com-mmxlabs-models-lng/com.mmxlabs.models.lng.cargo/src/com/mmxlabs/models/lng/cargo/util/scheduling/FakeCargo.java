/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util.scheduling;

import java.util.List;
import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.impl.AssignableElementImpl;

/**
 * A "fake" {@link Cargo} implementation allowing an arbitrary pair of slots to be linked to a vessel assignment without updating the EOpposite Slot#getCargo()
 * 
 * @author Simon Goodall
 *
 */
public class FakeCargo extends AssignableElementImpl {
	private final List<Slot<?>> slots;

	public FakeCargo(final Slot<?>... slots) {
		this.slots = Lists.newArrayList(slots);
	}

	public List<Slot<?>> getSlots() {
		return slots;
	}
}