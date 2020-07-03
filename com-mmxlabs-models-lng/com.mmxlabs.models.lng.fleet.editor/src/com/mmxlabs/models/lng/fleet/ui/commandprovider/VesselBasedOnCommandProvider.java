/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.commandprovider;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.AbstractModelCommandProvider;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.mmxcore.MMXRootObject;

public class VesselBasedOnCommandProvider extends AbstractModelCommandProvider<Object> {

	@Override
	public Command provideAdditionalAfterCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {

		if (parameter.getFeature() == FleetPackage.Literals.VESSEL__REFERENCE) {
			if (parameter.getEOwner() instanceof Vessel) {
				Vessel vessel = (Vessel) parameter.getEOwner();
				if (vessel.getReference() == null) {
					CompoundCommand cmd = new CompoundCommand();
					cmd.append(SetCommand.create(editingDomain, vessel, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS_OVERRIDE, Boolean.TRUE));
					cmd.append(SetCommand.create(editingDomain, vessel, FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES_OVERRIDE, Boolean.TRUE));
					cmd.append(SetCommand.create(editingDomain, vessel, FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS_OVERRIDE, Boolean.TRUE));
					cmd.append(SetCommand.create(editingDomain, vessel, FleetPackage.Literals.VESSEL__HAS_RELIQ_CAPABILITY_OVERRIDE, Boolean.TRUE));

					if (vessel.getLadenAttributes() != null) {
						cmd.append(SetCommand.create(editingDomain, vessel.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE, Boolean.TRUE));
					}

					if (vessel.getBallastAttributes() != null) {
						cmd.append(SetCommand.create(editingDomain, vessel.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE, Boolean.TRUE));
					}

					return cmd;
				}
			}
		}

		return null;
	}

	@Override
	public Command provideAdditionalBeforeCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		return null;
	}

}
