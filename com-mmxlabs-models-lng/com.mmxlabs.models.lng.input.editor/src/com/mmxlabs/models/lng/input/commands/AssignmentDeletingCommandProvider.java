/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.commands;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.BaseModelCommandProvider;
import com.mmxlabs.models.lng.input.Assignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;

public class AssignmentDeletingCommandProvider extends BaseModelCommandProvider {

	@Override
	protected Command handleDeletion(EditingDomain editingDomain,
			MMXRootObject rootObject, Collection<Object> deleted) {
		final InputModel im = rootObject.getSubModel(InputModel.class);
		if (im != null) {
			final CompoundCommand cc = new CompoundCommand();
			boolean hasDeletedSomething = false;
			for (final Assignment a : im.getAssignments()) {
				if (deleted.containsAll(a.getVessels())) {
					cc.append(DeleteCommand.create(editingDomain, a));
					hasDeletedSomething = true;
				}
			}
			if (hasDeletedSomething) return cc;
		}
		return null;
	}
	
}
