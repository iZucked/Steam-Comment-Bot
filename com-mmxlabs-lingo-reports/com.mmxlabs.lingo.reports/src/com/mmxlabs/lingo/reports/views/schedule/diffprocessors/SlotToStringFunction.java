/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.diffprocessors;

import com.google.common.base.Function;
import com.mmxlabs.models.lng.cargo.Slot;

/**
 * Guava {@link Function} to convert a Slot to a String based on it's name;
 * 
 */
public class SlotToStringFunction implements Function<Slot, String> {

	@Override
	public String apply(final Slot o) {
		return o.getName();
	}

	@Override
	public String toString() {
		return "toString";
	}

}