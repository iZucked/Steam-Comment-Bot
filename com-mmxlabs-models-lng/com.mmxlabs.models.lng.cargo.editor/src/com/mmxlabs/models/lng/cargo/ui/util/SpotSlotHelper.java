/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.port.Port;

public class SpotSlotHelper {

	public static void setSpotSlotTimeWindow(final EditingDomain editingDomain, final Slot slot, final Slot otherSlot, final CompoundCommand cmd) {
		setSpotSlotTimeWindow(editingDomain, slot, otherSlot.getPort(), otherSlot.getPort(), otherSlot.getWindowStart(), cmd);
	}

	public static void setSpotSlotTimeWindow(final EditingDomain editingDomain, final Slot slot, final Port oldPort, Port newPort, final Date newDate, final CompoundCommand cmd) {
		// Spot market - make a month range.
		final Calendar cal = Calendar.getInstance();
		// Get the timezone
		final TimeZone zone = TimeZone.getTimeZone(newPort.getTimeZone());
		cal.setTimeZone(zone);
		if (oldPort == newPort) {
			// Prime with date
			cal.setTime(newDate);
		} else {
			Calendar oldCal = Calendar.getInstance();
			final TimeZone oldZone = TimeZone.getTimeZone(oldPort.getTimeZone());
			oldCal.setTimeZone(oldZone);
			oldCal.setTime(newDate);

			cal.set(Calendar.YEAR, oldCal.get(Calendar.YEAR));
			cal.set(Calendar.MONTH, oldCal.get(Calendar.MONTH));
		}
		// Clear any existing time values
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);

		// Set to start of month
		cal.set(Calendar.DAY_OF_MONTH, 1);

		// Get start Date
		final Date start = cal.getTime();

		// Now find month duration
		final long startMillis = cal.getTimeInMillis();
		cal.add(Calendar.MONTH, 1);
		final long endMillis = cal.getTimeInMillis();
		final int windowSize = (int) ((endMillis - startMillis) / 1000 / 60 / 60);

		if (slot.getWindowSize() != windowSize) {
			cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowSize(), windowSize));
		}
		if (!slot.getWindowStart().equals(start)) {
			cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStart(), start));
		}
		if (slot.getWindowStartTime() != 0) {
			cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), 0));
		}
	}

}
