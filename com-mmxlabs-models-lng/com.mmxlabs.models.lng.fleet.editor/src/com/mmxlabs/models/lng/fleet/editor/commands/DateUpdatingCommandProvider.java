/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.editor.commands;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Adds a set command to set commands on port, which will update the date to be locally correct (midnight on the day at the port).
 * 
 * @author hinton, Simon Goodall
 * 
 */
public class DateUpdatingCommandProvider implements IModelCommandProvider {

	@Override
	public Command provideAdditionalCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		if (commandClass == SetCommand.class) {
			if (parameter.getEOwner() instanceof VesselEvent) {
				final CompoundCommand cmd = new CompoundCommand("Update VesselEvent Dates");
				final VesselEvent vesselEvent = (VesselEvent) parameter.getEOwner();
				if (parameter.getEStructuralFeature() == FleetPackage.eINSTANCE.getVesselEvent_Port()) {
					{
						final Date vesselEventDate = vesselEvent.getStartBy();
						final EAttribute vesselEventDateFeature = FleetPackage.eINSTANCE.getVesselEvent_StartBy();
						final Command c = updateVesselEventDate(editingDomain, parameter, vesselEvent, vesselEventDate, vesselEventDateFeature);
						if (c != null) {
							cmd.append(c);
						}
					}
					{
						final Date vesselEventDate = vesselEvent.getStartAfter();
						final EAttribute vesselEventDateFeature = FleetPackage.eINSTANCE.getVesselEvent_StartAfter();
						final Command c = updateVesselEventDate(editingDomain, parameter, vesselEvent, vesselEventDate, vesselEventDateFeature);
						if (c != null) {
							cmd.append(c);
						}
					}
					if (cmd.isEmpty()) {
						return null;
					} else {
						return cmd.unwrap();
					}
				}
			}
		}
		return null;
	}

	private Command updateVesselEventDate(final EditingDomain editingDomain, final CommandParameter parameter, final VesselEvent vesselEvent, final Date vesselEventDate,
			final EAttribute vesselEventDateFeature) {
		if (vesselEventDate != null) {

			// port is changing, so update time to suit
			final EObject newValue = parameter.getEValue();
			if (newValue instanceof Port) {

				final String newZone = ((Port) newValue).getTimeZone();
				final String oldZone = vesselEvent.getTimeZone(vesselEventDateFeature);
				// remap from old zone to new zone
				if ((newZone == null && !(oldZone == null)) || (newZone != null && !newZone.equals(oldZone))) {
					final Calendar oldCalendar = Calendar.getInstance(getZone(oldZone));
					final Calendar newCalendar = Calendar.getInstance(getZone(newZone));

					// Prime with current date in old tz
					oldCalendar.setTime(vesselEventDate);
					// Clear all components (specifically time)
					newCalendar.clear();
					// Replicate the date components in new TZ
					newCalendar.set(Calendar.YEAR, oldCalendar.get(Calendar.YEAR));
					newCalendar.set(Calendar.MONTH, oldCalendar.get(Calendar.MONTH));
					newCalendar.set(Calendar.DAY_OF_MONTH, oldCalendar.get(Calendar.DAY_OF_MONTH));

					final Date newDate = newCalendar.getTime();

					return SetCommand.create(editingDomain, vesselEvent, vesselEventDateFeature, newDate);
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
