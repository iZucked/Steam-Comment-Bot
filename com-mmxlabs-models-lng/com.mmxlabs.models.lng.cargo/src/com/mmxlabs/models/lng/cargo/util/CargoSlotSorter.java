/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;

/**
 * Helper class to sort {@link Cargo#getSlots()} into date order. Normally these are maintained in an unsorted order (due to problems running multiple EMF editing commands on the slot list at the same
 * time)
 * 
 * @author Simon Goodall
 * 
 */
public class CargoSlotSorter {
	/**
	 * A {@link TimeZone} aware {@link Comparator} implementation to sort {@link Slot}s based on their start window.
	 * 
	 */
	public static class SlotComparator implements Comparator<Slot> {
		@Override
		public int compare(final Slot o1, final Slot o2) {

			// Implicit Loads followed by discharges - does not support LDLD cargoes
			final int c = getSlotType(o1) - getSlotType(o2);
			if (c != 0) {
				return c;
			}

			final ZonedDateTime d1 = o1.getWindowStartWithSlotOrPortTime();
			final ZonedDateTime d2 = o2.getWindowStartWithSlotOrPortTime();
			if (d1 == null) {
				return -1;
			} else if (d2 == null) {
				return 1;
			}

			return d1.compareTo(d2);
		}

		private int getSlotType(final Slot s) {
			if (s instanceof LoadSlot) {
				return 1;
			}
			if (s instanceof DischargeSlot) {
				return 2;
			}
			return 10;
		}
	}

	/**
	 * Sort the given list of slots by {@link Date}. This method creates a new {@link List} and sorts the input slot list using a {@link SlotComparator}.
	 * 
	 * @param slots
	 * @return
	 */
	public static EList<Slot> sortedSlots(final List<Slot> slots) {
		// Copy original list
		final EList<Slot> sortedSlots = new BasicEList<Slot>(slots);
		// Sort!
		Collections.sort(sortedSlots, new SlotComparator());
		// Return
		return sortedSlots;
	}
}
