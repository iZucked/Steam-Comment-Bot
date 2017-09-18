/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * @author Simon Goodall
 * 
 */
public class PortVersionCommandProvider implements IModelCommandProvider {
	@Override
	public Command provideAdditionalBeforeCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		return null;
	}

	@Override
	public Command provideAdditionalAfterCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		if (parameter.getEOwner() instanceof Location) {
			// Find parent
			EObject target = parameter.getEOwner();
			if (target instanceof Location) {
				target = target.eContainer();
			}
			if (target instanceof Port) {
				target = target.eContainer();
			}
			if (target instanceof PortModel) {
				return SetCommand.create(editingDomain, target, PortPackage.Literals.PORT_MODEL__PORT_DATA_VERSION, "private-" + EcoreUtil.generateUUID());
			}
		}
		if (parameter.getEOwner() instanceof Port && parameter.getFeature() == PortPackage.Literals.PORT__LOCATION) {
			// Find parent
			EObject target = parameter.getEOwner();
			if (target instanceof Port) {
				target = target.eContainer();
			}
			if (target instanceof PortModel) {
				return SetCommand.create(editingDomain, target, PortPackage.Literals.PORT_MODEL__PORT_DATA_VERSION, "private-" + EcoreUtil.generateUUID());
			}
		}
		return null;
	}

	@Override
	public void startCommandProvision() {

	}

	@Override
	public void endCommandProvision() {

	}
}
