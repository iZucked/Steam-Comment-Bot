/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.autocorrector;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.cargo.CargoPackage;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
import scenario.contract.Contract;
import scenario.port.Port;
import scenario.port.PortCapability;
import scenario.port.PortSelection;

import com.mmxlabs.common.Pair;

/**
 * An auto-corrector which sets the port to a value from the contract's default port list if necessary
 * 
 * @author hinton
 * 
 */
public class SetPortForContractCorrector extends BaseCorrector {

	@Override
	public Pair<String, Command> correct(final Notification notification, final EditingDomain editingDomain) {
		final Object o = notification.getNotifier();
		if (o instanceof Slot) {
			if (notification.getFeature() == CargoPackage.eINSTANCE.getSlot_Contract()) {
				final Slot s = (Slot) o;
				final Contract c = (Contract) notification.getNewValue();
				if (c != null) {
					final Port p = s.getPort();
					if (c.getDefaultPorts().isEmpty()) {
						return null;
					}
					if (c.getDefaultPorts().contains(p)) {
						return null;
					}
					final PortCapability capability = (s instanceof LoadSlot) ? PortCapability.LOAD : PortCapability.DISCHARGE;
					// TODO add a flag for default port?
					for (final PortSelection ps : c.getDefaultPorts()) {
						for (final Port p2 : ps.getClosure(new UniqueEList<PortSelection>())) {
							if (p2.getCapabilities().contains(capability)) {
								return new Pair<String, Command>("Set port to a compatible port from " + c.getName(), SetCommand.create(editingDomain, s, CargoPackage.eINSTANCE.getSlot_Port(), p2));
							}
						}
					}
				}
			}
		}

		return null;
	}

}
