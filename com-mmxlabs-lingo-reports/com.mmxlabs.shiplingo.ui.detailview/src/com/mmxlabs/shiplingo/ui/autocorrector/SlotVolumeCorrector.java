/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.autocorrector;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;

public class SlotVolumeCorrector extends BaseCorrector {
	final static EAttribute minQuantity = CargoPackage.eINSTANCE.getSlot_MinQuantity();
	final static EAttribute maxQuantity = CargoPackage.eINSTANCE.getSlot_MaxQuantity();

	@Override
	public Pair<String, Command> correct(final Notification notification, final EditingDomain editingDomain) {
		final Object feature = notification.getFeature();
		final Object target = notification.getNotifier();
		// min has changed

		if (minQuantity.equals(feature)) {
			final Slot slot = (Slot) target;
			if (slot.getSlotOrContractMaxQuantity() < slot.getSlotOrContractMinQuantity()) {
				return new Pair<String, Command>("Adjust maximum quantity to match minimum quantity", makeSetter(editingDomain, slot, maxQuantity, slot.getSlotOrContractMinQuantity()));
			}
		} else if (maxQuantity.equals(feature)) {
			final Slot slot = (Slot) target;
			if (slot.getSlotOrContractMaxQuantity() < slot.getSlotOrContractMinQuantity()) {
				return new Pair<String, Command>("Adjust minimum quantity to match maximum quantity", makeSetter(editingDomain, slot, minQuantity, slot.getMaxQuantity()));
			}
		}
		return null;
	}
}
