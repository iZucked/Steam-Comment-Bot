/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.commands;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.AbstractModelCommandProvider;
import com.mmxlabs.models.lng.port.PortCountryGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * 
 * When setting an uncontained {@link PortCountryGroup} - add it to the data model.
 * 
 * @author Simon Goodall
 * 
 */
public class PortCountryGroupCommandProvider extends AbstractModelCommandProvider<Object> {

	@Override
	public Command provideAdditionalAfterCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {

		// TODO: Clean up importer code? ((Leave as is...))
		// TODO: Create new PortGroup value provider to work with this class in presenting un-contained objects. (Test this works with dialogs, validation, undo/re etc)

		if (commandClass == SetCommand.class) {
			if (parameter.getEValue() instanceof PortCountryGroup) {
				if (parameter.getEValue() instanceof PortCountryGroup && !(parameter.getOwner() instanceof PortModel)) {

					final PortCountryGroup portCountryGroup = (PortCountryGroup) parameter.getEValue();
					if (portCountryGroup.eContainer() == null) {
						if (rootObject instanceof LNGScenarioModel) {
							final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
							final PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
							return AddCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__PORT_COUNTRY_GROUPS, parameter.getEValue());
						}
					}
				}
			}
		} else if (commandClass == AddCommand.class) {
			final CompoundCommand cmd = new CompoundCommand();

			if (!(parameter.getOwner() instanceof PortModel)) {
				final Collection<?> collection = parameter.getCollection();
				for (final Object o : collection) {
					if (o instanceof PortCountryGroup) {

						final PortCountryGroup portCountryGroup = (PortCountryGroup) o;
						if (portCountryGroup.eContainer() == null) {
							if (rootObject instanceof LNGScenarioModel) {
								final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
								final PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
								cmd.append(AddCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__PORT_COUNTRY_GROUPS, portCountryGroup));
							}
						}
					}
				}
				if (!cmd.isEmpty()) {
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
