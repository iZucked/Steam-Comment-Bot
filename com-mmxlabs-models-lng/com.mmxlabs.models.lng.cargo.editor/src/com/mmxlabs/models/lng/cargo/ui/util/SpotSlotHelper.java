package com.mmxlabs.models.lng.cargo.ui.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.ui.dates.LocalDateUtil;

public class SpotSlotHelper {

	public static void setSpotSlotTimeWindow(final EditingDomain editingDomain, final Slot slot, final Slot otherSlot, final CompoundCommand cmd) {
		setSpotSlotTimeWindow(editingDomain, slot, otherSlot, otherSlot.getWindowStart(), cmd);
	}

	public static void setSpotSlotTimeWindow(final EditingDomain editingDomain, final Slot slot, final Slot otherSlot, final Date newDate, final CompoundCommand cmd) {
		// Spot market - make a month range.
		final Calendar cal = Calendar.getInstance();
		// Get the timezone
		final TimeZone zone = LocalDateUtil.getTimeZone(otherSlot, CargoPackage.eINSTANCE.getSlot_WindowStart());
		cal.setTimeZone(zone);
		// Prime with date
		cal.setTime(newDate);
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

		cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowSize(), windowSize));
		cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStart(), start));
		cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), 0));
	}

}
