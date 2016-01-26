/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.util;

import java.time.LocalDate;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.port.Port;

public class SpotSlotHelper {

	public static void setSpotSlotTimeWindow(final EditingDomain editingDomain, final Slot slot, final Slot otherSlot, final CompoundCommand cmd) {
		setSpotSlotTimeWindow(editingDomain, slot, otherSlot.getPort(), otherSlot.getPort(), otherSlot.getWindowStart(), cmd);
	}

	public static void setSpotSlotTimeWindow(final EditingDomain editingDomain, final Slot slot, final Port oldPort, final Port newPort, final LocalDate newDate, final CompoundCommand cmd) {
		if (newDate != null) {
			// Spot market - make a month range.
			final LocalDate start = newDate.withDayOfMonth(1);

			// Now find month duration

			final int windowSize = Hours.between(start, start.plusMonths(1));

			if (slot.getWindowSize() != windowSize) {
				cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowSize(), windowSize));
			}
			if (slot.getWindowStart() != null && !slot.getWindowStart().equals(start)) {
				cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStart(), start));
			} else {
				// if slot.getWindowStart() == null ????
			}
			if (slot.getWindowStartTime() != 0) {
				cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), 0));
			}
		} else {
			// ????
		}
	}
}
