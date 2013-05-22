/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Adds a set command to set commands on port, which will update the date to be locally correct (midnight on the day at the port).
 * 
 * @author hinton
 * 
 */
public class DateUpdatingCommandProvider implements IModelCommandProvider {

	@Override
	public Command provideAdditionalCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		
		// only modify commands which SET the port on a slot
		if (commandClass == SetCommand.class) {
			// only modify commands which set the port on a SLOT 
			if (parameter.getEOwner() instanceof Slot) {
				final Slot slot = (Slot) parameter.getEOwner();
				// no modification if the window start is unset
				if (slot.getWindowStart() == null)
					return null;
				// only modify commands which set the PORT on a slot
				if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_Port()) {
					// port is changing, so update time to suit
					final EObject newValue = parameter.getEValue();
					if (newValue instanceof Port) {

						final String newZone = ((Port) newValue).getTimeZone();
						final String oldZone = slot.getTimeZone(CargoPackage.eINSTANCE.getSlot_WindowStart());
						// remap from old zone to new zone
						if ((newZone == null && !(oldZone == null)) || (newZone != null && !newZone.equals(oldZone))) {
							final Calendar oldCalendar = Calendar.getInstance(getZone(oldZone));
							final Calendar newCalendar = Calendar.getInstance(getZone(newZone));

							// Prime with current date in old tz
							oldCalendar.setTime(slot.getWindowStart());
							// Clear all components (specifically time)
							newCalendar.clear();
							// Replicate the date components in new TZ
							newCalendar.set(Calendar.YEAR, oldCalendar.get(Calendar.YEAR));
							newCalendar.set(Calendar.MONTH, oldCalendar.get(Calendar.MONTH));
							newCalendar.set(Calendar.DAY_OF_MONTH, oldCalendar.get(Calendar.DAY_OF_MONTH));

							final Date newDate = newCalendar.getTime();

							// return a new command setting the window start on the slot to the port's local midnight time
							return SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStart(), newDate);
						}
					}
				}
			} 
		}
		return null;
	}

	private TimeZone getZone(final String zone) {
		if (zone == null || zone.isEmpty())
			return TimeZone.getTimeZone("UTC");
		else
			return TimeZone.getTimeZone(zone);
	}

	@Override
	public void startCommandProvision() {

	}

	@Override
	public void endCommandProvision() {

	}
}
