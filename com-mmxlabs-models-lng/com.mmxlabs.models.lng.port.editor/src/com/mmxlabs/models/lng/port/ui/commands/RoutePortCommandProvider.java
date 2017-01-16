/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.commands;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.BaseModelCommandProvider;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Deletes old distance lines when their ports are being deleted
 * 
 * @author hinton
 * 
 */
public class RoutePortCommandProvider extends BaseModelCommandProvider<Object> {
	@Override
	protected Command handleDeletion(final EditingDomain editingDomain, final MMXRootObject rootObject, final Collection<Object> deleted, final Map<EObject, EObject> overrides,
			final Set<EObject> editSet) {

		if (rootObject instanceof LNGScenarioModel) {

			final PortModel portModel = ((LNGScenarioModel) rootObject).getReferenceModel().getPortModel();
			if (portModel == null)
				return null;
			final HashSet<RouteLine> dead = new HashSet<RouteLine>();
			for (final Object object : deleted) {
				if (object instanceof Port) {
					for (final Route route : portModel.getRoutes()) {
						for (final RouteLine line : route.getLines()) {
							if (line.getFrom() == object || line.getTo() == object)
								dead.add(line);
						}
					}
				}
			}
			if (!dead.isEmpty()) {
				return DeleteCommand.create(editingDomain, dead);
			}
		}
		return null;
	}
}
