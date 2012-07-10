/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.commandservice.BaseModelCommandProvider;

public class SlotDeletingCommandProvider extends BaseModelCommandProvider {
	@Override
	protected boolean shouldHandleDeletion(Object deletedObject) {
		return deletedObject instanceof Cargo;
	}

	@Override
	protected Command objectDeleted(EditingDomain domain,
			MMXRootObject rootObject, Object deleted) {
		if (deleted instanceof Cargo) {
			final CompoundCommand deleteSlots = new CompoundCommand();
			deleteSlots.append(IdentityCommand.INSTANCE);
			final Cargo c = (Cargo) deleted;
			if (c.getLoadSlot() != null)
				deleteSlots.append(DeleteCommand.create(domain, c.getLoadSlot()));
			if (c.getDischargeSlot() != null)
				deleteSlots.append(DeleteCommand.create(domain, c.getDischargeSlot()));
			return deleteSlots;
		}
		
		return null;
	}
}
