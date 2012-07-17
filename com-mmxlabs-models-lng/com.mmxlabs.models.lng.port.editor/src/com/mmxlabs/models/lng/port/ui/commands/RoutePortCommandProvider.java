/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.commands;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.BaseModelCommandProvider;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Deletes old distance lines when their ports are being deleted
 * 
 * @author hinton
 * 
 */
public class RoutePortCommandProvider extends BaseModelCommandProvider {
	@Override
	protected Command handleDeletion(EditingDomain editingDomain, MMXRootObject rootObject, Collection<Object> deleted) {
		final PortModel portModel = rootObject.getSubModel(PortModel.class);
		if (portModel == null) return null;
		final HashSet<RouteLine> dead = new HashSet<RouteLine>();
		for (final Object object : deleted) {
			if (object instanceof Port) {
				for (final Route route : portModel.getRoutes()) {
					for (final RouteLine line : route.getLines()) {
						if (line.getFrom() == object || line.getTo() == object) dead.add(line);
					}
				}
			}
		}
		if (dead.isEmpty()) return null;
		else return DeleteCommand.create(editingDomain, dead);
	}
}
