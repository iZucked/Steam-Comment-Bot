/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.commandservice.BaseModelCommandProvider;

/**
 * Deletes old distance lines when their ports are being deleted
 * 
 * @author hinton
 * 
 */
public class RoutePortCommandProvider extends BaseModelCommandProvider {
	@Override
	protected boolean shouldHandleDeletion(Object deletedObject) {
		return deletedObject instanceof Port;
	}

	@Override
	protected Command objectDeleted(EditingDomain domain, MMXRootObject rootObject, Object deleted) {
		final PortModel portModel = rootObject.getSubModel(PortModel.class);

		if (deleted instanceof Port) {
			final Port port = (Port) deleted;
			final List<RouteLine> dead = new ArrayList<RouteLine>();
			for (final Route route : portModel.getRoutes()) {
				for (final RouteLine line : route.getLines()) {
					if (line.getFrom() == port || line.getTo() == port) dead.add(line);
				}
			}
			if (dead.isEmpty() == false) {
				return DeleteCommand.create(domain, dead);
			}
		}

		return null;
	}
}
