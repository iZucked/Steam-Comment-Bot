/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.autocorrector;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * Make slot ids match cargo ids
 * 
 * @author Tom Hinton
 * 
 */
public class SlotIdCorrector extends BaseCorrector {
	/*
	 * (non-Javadoc)
	 * 
	 * @see scenario.presentation.cargoeditor.autocorrect.AutoCorrector.ICorrector #correct(org.eclipse.emf.common.notify.Notification, org.eclipse.emf.edit.domain.EditingDomain)
	 */
	@Override
	public Pair<String, Command> correct(final Notification notification, final EditingDomain editingDomain) {

		if (notification.isTouch()) {
			return null;
		}
		if (notification.getEventType() == Notification.SET) {
			if (notification.getFeature() == MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
				final Cargo target = (Cargo) notification.getNotifier();
				final CompoundCommand fix = new CompoundCommand();
				if (target.getLoadSlot() != null) {
					fix.append(SetCommand.create(editingDomain, target.getLoadSlot(), MMXCorePackage.eINSTANCE.getNamedObject_Name(), "load-" + target.getName()));
				}
				if (target.getDischargeSlot() != null) {
					fix.append(SetCommand.create(editingDomain, target.getDischargeSlot(), MMXCorePackage.eINSTANCE.getNamedObject_Name(), "discharge-" + target.getName()));
				}
			}
		}
		return null;
	}

}
