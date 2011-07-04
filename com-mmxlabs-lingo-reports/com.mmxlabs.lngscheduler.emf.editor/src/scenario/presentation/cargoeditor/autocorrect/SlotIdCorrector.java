/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.autocorrect;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;

import com.mmxlabs.common.Pair;

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
	 * @see
	 * scenario.presentation.cargoeditor.autocorrect.AutoCorrector.ICorrector
	 * #correct(org.eclipse.emf.common.notify.Notification,
	 * org.eclipse.emf.edit.domain.EditingDomain)
	 */
	@Override
	public Pair<String, Command> correct(Notification notification,
			EditingDomain editingDomain) {

		if (notification.isTouch()) {
			return null;
		}
		if (notification.getEventType() == Notification.SET) {
			if (notification.getFeature() == CargoPackage.eINSTANCE
					.getCargo_Id()) {
				final Cargo target = (Cargo) notification.getNotifier();
				final CompoundCommand fix = new CompoundCommand();
				if (target.getLoadSlot() != null) {
					fix.append(SetCommand.create(editingDomain,
							target.getLoadSlot(),
							CargoPackage.eINSTANCE.getSlot_Id(), "load-"
									+ target.getId()));
				}
				if (target.getDischargeSlot() != null) {
					fix.append(SetCommand.create(editingDomain,
							target.getDischargeSlot(),
							CargoPackage.eINSTANCE.getSlot_Id(), "discharge-"
									+ target.getId()));
				}
			}
		}
		return null;
	}

}
